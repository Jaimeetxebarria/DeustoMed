import dotenv from "dotenv";
import { SignUpDataInfo, UserType } from "./schemas";
import bcrypt from "bcrypt";
import { Database } from "./database.types";
import { PostgrestClient } from "@supabase/postgrest-js";
dotenv.config();

if (!process.env.SUPABASE_URL) {
    throw new Error("Missing SUPABASE_URL env var");
}
if (!process.env.SUPABASE_ANON_KEY) {
    throw new Error("Missing SUPABASE_ANON_KEY env var");
}

const SALT_ROUNDS = 12;

// Use custom authentication method to authenticate as a superuser
const fetchWithSuperuserAuth = async (input: any, init: any) => {
    let headers = new Headers(init?.headers);
    headers.append("apikey", process.env.SUPABASE_ANON_KEY as string);
    headers.append("cookie", `accessToken=${process.env.SUPABASE_SUPERUSER_ACCESS_TOKEN}`);
    return fetch(input, { ...init, headers });
};

// Create the postgrest client
const postgrestClient = new PostgrestClient<Database>(`${process.env.SUPABASE_URL}/rest/v1`, {
    fetch: fetchWithSuperuserAuth,
});

async function encryptPassword(password: string) {
    return bcrypt.hash(password, SALT_ROUNDS);
}

async function getPersonId(signUpInfo: SignUpDataInfo) {
    const { data: personData, error: personError } = await postgrestClient
        .from("person")
        .select()
        .eq("name", signUpInfo.name)
        .eq("surname1", signUpInfo.surname1)
        .eq("surname2", signUpInfo.surname2)
        .eq("birthdate", signUpInfo.birthDate)
        .eq("sex", signUpInfo.sex)
        .limit(1);

    if (personError) {
        console.log(personError);
        throw personError;
    }

    return personData[0]?.id;
}

async function userExists(id: string, userType: UserType) {
    const { data, error } = await postgrestClient.from(userType).select().eq("id", id).limit(1);

    if (error) {
        console.log(error);
        throw error;
    }
    return data.length !== 0;
}

async function getSpecialityId(speciality: string) {
    const { data, error } = await postgrestClient
        .from("speciality")
        .select()
        .eq("name", speciality)
        .limit(1);

    if (error) {
        console.log(error);
        throw error;
    }

    return data[0]?.id;
}

async function createPerson(signUpInfo: SignUpDataInfo) {
    return await postgrestClient
        .from("person")
        .insert({
            name: signUpInfo.name,
            surname1: signUpInfo.surname1,
            surname2: signUpInfo.surname2,
            birthdate: signUpInfo.birthDate.toISOString(),
            sex: signUpInfo.sex,
            encrypted_password: await encryptPassword(signUpInfo.password),
            dni: signUpInfo.dni as string,
            email: signUpInfo.email,
            phone: signUpInfo.phone,
            address: signUpInfo.address,
        })
        .select();
}

async function createUser(id: string, signUpInfo: SignUpDataInfo & { specialityId: number }) {
    switch (signUpInfo.userType) {
        case "patient":
            return postgrestClient.from("patient").insert({
                id: id,
            });
        case "doctor":
            return postgrestClient.from("doctor").insert({
                id: id,
                fk_speciality_id: signUpInfo.specialityId,
            });

        case "admin":
            return postgrestClient.from("admin").insert({
                id: id,
            });
    }
}

export default {
    public: postgrestClient,
    custom_auth: postgrestClient.schema("custom_auth"),
    getPersonId,
    getSpecialityId,
    userExists,
    createPerson,
    createUser,
};
