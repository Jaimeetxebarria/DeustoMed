import express, { Express, Request, Response } from "express";
import dotenv from "dotenv";
import selfsigned from "selfsigned";
import https from "https";
import { LoginDataSchema, SignUpDataSchema } from "./schemas";
import database from "./database";
import bcrypt from "bcrypt";
import cookieParser from "cookie-parser";
import crypto from "crypto";

dotenv.config();

const app: Express = express();
app.use(express.json());
app.use(cookieParser());

const attrs = [{ value: "localhost", type: "commonName" }];
const pems = selfsigned.generate(attrs, { days: 365 });
const privateKey = pems.private;
const certificate = pems.cert;

// Define the HTTPS options
const httpsOptions: https.ServerOptions = {
    key: privateKey,
    cert: certificate,
    minVersion: "TLSv1.3", //Force use of tls 1.3person
    maxVersion: "TLSv1.3",
};

// Create an HTTPS server
let server: https.Server = https.createServer(httpsOptions, app);

server.listen(process.env.PORT, () => {
    console.log(`⚡️[server]: Server is running on https://localhost:${process.env.PORT}`);
});

app.post("/signup", async (req: Request, res: Response) => {
    const data = req.body;
    if (Object.keys(data).length === 0) {
        return res.status(400).send("Sign up request must contain the user data in the body");
    }

    // Validate the body data, if it's not valid, return the errors
    const parseResult = SignUpDataSchema.safeParse(data);
    if (!parseResult.success) {
        const errors = parseResult.error.errors.map((e) => e.message);
        return res.status(400).send({ errors: errors });
    }

    let specialityId;
    if (parseResult.data.userType === "doctor") {
        specialityId = await database.getSpecialityId(parseResult.data.speciality!);
        if (typeof specialityId === "undefined")
            return res.status(404).send("Provided speciality does not exist");
    }

    const newUserData = { ...parseResult.data, specialityId: specialityId };

    // Check that the user doesn't exist in the database
    const userId = await database.getPersonId(data);

    // Person doesn't exist, create it and also the user
    if (typeof userId === "undefined") {
        const { data: newId, error: createPersonError } = await database.createPerson(newUserData);

        if (createPersonError) return res.status(500).send("Error creating user");
        if (newId[0] !== null) await database.createUser(newId[0].id, newUserData);

        return res.send({ id: newId[0].id, newPerson: true });
    }

    // Person exists, check that the user doesn't exist
    const userExists = await database.userExists(userId, data.userType);
    if (userExists) return res.status(400).send("User already exists");
    await database.createUser(userId, newUserData);

    return res.send({ id: userId, newPerson: false });
});

app.post("/login", async (req: Request, res: Response) => {
    const reqData: Object = req.body;
    if (Object.keys(reqData).length === 0) {
        return res.status(400).send("Login request must contain id and password in the body");
    }

    // Validate the body data, if it's not valid, return the errors
    const parseResult = LoginDataSchema.safeParse(reqData);

    if (!parseResult.success) {
        const errors = parseResult.error.errors.map((e) => e.message);
        return res.status(400).send({ errors: errors });
    }

    // Check that the person exists in the database and get their id and encrypted password
    const { data: personData, error: personError } = await database.public
        .from("person")
        .select("encrypted_password")
        .eq("id", parseResult.data.id)
        .limit(1);

    if (personError) {
        console.log(personError);
        return res.status(500).send("Error logging in");
    }

    if (personData.length === 0) return res.status(404).send("User does not exist");

    // Check that the password is correct
    if (!(await bcrypt.compare(parseResult.data.password, personData[0].encrypted_password))) {
        return res.status(401).send("Invalid credentials");
    }

    // Check that the user exists
    if (!(await database.userExists(parseResult.data.id, parseResult.data.userType))) {
        return res.status(404).send("User does not exist");
    }

    //Generate the access and refresh tokens using UUID v4 using a cryptographically secure random number generator
    const accessToken = crypto.randomUUID();
    const refreshToken = crypto.randomUUID();

    const userTypeId = (() => {
        switch (parseResult.data.userType) {
            case "patient":
                return 1;
            case "doctor":
                return 2;
            case "admin":
                return 3;
        }
    })();

    const { data: sessionData, error: sessionError } = await database.custom_auth
        .from("session")
        .insert({
            user_id: parseResult.data.id,
            fk_user_type: userTypeId,
            access_token: accessToken,
        })
        .select();

    if (sessionError) {
        console.log(sessionError);
        return res.status(500).send("Error logging in");
    }

    const { error: refreshTokenError } = await database.custom_auth.from("refresh_token").insert({
        token: refreshToken,
        fk_session_id: sessionData[0].id,
    });

    if (refreshTokenError) {
        console.log(refreshTokenError);
        return res.status(500).send("Error logging in");
    }

    res.send({
        sessionId: sessionData[0].id,
        accessToken: accessToken,
        refreshToken: refreshToken,
        expiresAt: sessionData[0].access_token_expiry,
    });
});

app.post("/refresh", async (req: Request, res: Response) => {
    // Check that the request has the necessary data
    const sessionId = req.body.sessionId;
    const refreshToken = req.body.refreshToken;

    if (!sessionId) return res.status(400).send("Refresh request must contain the session id");
    if (!refreshToken)
        return res.status(400).send("Refresh request must contain the refresh token");

    // Check that the refresh token is valid
    const { data: refreshTokenData, error: refreshTokenError } = await database.custom_auth
        .from("refresh_token")
        .select()
        .eq("fk_session_id", sessionId)
        .eq("token", refreshToken)
        .limit(1);

    if (refreshTokenError) return res.status(500).send("Error refreshing token");
    if (refreshTokenData.length === 0) return res.status(401).send("Invalid refresh credentials");

    // If the refresh token has been previously used, close the session, as it may have been compromised
    if (refreshTokenData[0].revoked) {
        await database.custom_auth.from("session").delete().eq("id", sessionId);
        return res.status(401).send("Reused refresh token. Closing session for security reasons");
    }

    // NOTE: No need to check if the session is unexpired, as they are deleted when they expire.

    // Create a new access and refresh token
    const newAccessToken = crypto.randomUUID();
    const newRefreshToken = crypto.randomUUID();

    // Revoke the old refresh token
    const { error: revokeRefreshTokenError } = await database.custom_auth
        .from("refresh_token")
        .update({ revoked: true })
        .eq("id", refreshTokenData[0].id);

    if (revokeRefreshTokenError) return res.status(500).send("Error refreshing token");

    // Save the new refresh token in the database
    const { error: newRefreshTokenError } = await database.custom_auth
        .from("refresh_token")
        .insert({ token: newRefreshToken, fk_session_id: sessionId })
        .select();

    if (newRefreshTokenError) return res.status(500).send("Error refreshing token");

    // Update the session with the new access token. Use rpc to update the access token expiry too
    const { data: newExpiresAt, error: sessionError } = await database.custom_auth.rpc(
        "update_session_token",
        { session_id: sessionId, new_access_token: newAccessToken }
    );

    if (sessionError) return res.status(500).send("Error refreshing token");

    // Send the new tokens to the client
    res.send({
        accessToken: newAccessToken,
        refreshToken: newRefreshToken,
        expiresAt: newExpiresAt,
    });
});

app.post("/logout", async (req: Request, res: Response) => {
    const sessionId = req.body.sessionId;
    const accessToken = req.body.accessToken;

    if (!sessionId) return res.status(400).send("Logout request must contain a refresh token");
    if (!accessToken) return res.status(400).send("Logout request must contain an access token");

    const { data, error } = await database.custom_auth
        .from("session")
        .delete()
        .eq("id", sessionId)
        .eq("access_token", accessToken)
        .select();

    if (error) {
        console.log(error);
        return res.status(500).send("Error logging out");
    }

    if (data.length === 0) return res.status(400).send("Invalid data: already logged out");

    res.clearCookie("sessionId").clearCookie("accessToken").send("Logged out successfully");
});
