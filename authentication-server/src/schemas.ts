import z from "zod";

const UserTypeSchema = z
    .string({
        required_error: "User type is required",
        invalid_type_error: "User type must be a string",
    })
    .transform((val) => val.toLowerCase())
    .pipe(z.enum(["admin", "patient", "doctor"]));
export type UserType = z.infer<typeof UserTypeSchema>;

const SexSchema = z
    .string({
        required_error: "Sex must be specified",
        invalid_type_error: "Sex must be a string",
    })
    .transform((val) => val.toUpperCase())
    .pipe(z.enum(["MALE", "FEMALE"]));
export type Sex = z.infer<typeof SexSchema>;

const passwordSchema = z
    .string({
        required_error: "Password is required",
        invalid_type_error: "Password must be a string",
    })
    .trim()
    .min(8, "Password must be at least 8 characters long")
    .max(20, "Password must be at most 20 characters long")
    .superRefine((pw, ctx) => {
        pw.match(/[a-z]/i), "Password must contain at least one letter";
    })
    .refine((pw) => pw.match(/[a-zA-Z]/i), "Password must contain at least one letter")
    .refine((pw) => pw.match(/[0-9]/), "Password must contain at least one number")
    .refine(
        (pw) => pw.match(/[^a-zA-Z0-9]/i),
        "Password must contain at least one special character"
    );

export const LoginDataSchema = z.object({
    id: z
        .string({ required_error: "ID is required" })
        .trim()
        .length(5, "ID must be 6 characters long")
        .regex(/^[a-zA-Z0-9]+$/, "Incorrect ID format"),
    password: z.string({ required_error: "Password is required" }).trim(),
    userType: UserTypeSchema,
});
export type LoginDataInfo = z.infer<typeof LoginDataSchema>;

//Check that the user is older that 18 if the user is a doctor
export const SignUpDataSchema = z
    .object({
        userType: UserTypeSchema,
        name: z
            .string({ required_error: "Name is required" })
            .trim()
            .min(2, "Name must be at least 2 characters long"),
        surname1: z
            .string({ required_error: "First surname is required" })
            .trim()
            .min(2, "First surname must be at least 2 characters long"),
        surname2: z
            .string({ required_error: "Second surname is required" })
            .trim()
            .min(2, "Second surname must be at least 2 characters long"),
        birthDate: z
            .date({
                required_error: "Birth date is required",
                invalid_type_error: "Birth date must be a date",
                coerce: true,
            })
            .refine((date) => date.getTime() < Date.now(), "Birth date must be in the past"),
        sex: SexSchema,
        password: passwordSchema,
        // OPTIONAL FIELDS
        dni: z
            .string()
            .length(9, "DNI must be 9 characters long")
            .regex(/^[0-9]{8}[a-zA-Z]$/, "Incorrect DNI format")
            .optional(),
        email: z.string().email("Invalid email format").optional(),
        phone: z
            .string()
            .transform((val) => val.replace(/\s/g, ""))
            .pipe(z.string().regex(/^\+34\s?[6-9][0-9]{8}$/))
            .optional(),
        address: z.string().optional(),
    })
    .refine(
        (signUpInfo) => {
            if (signUpInfo.userType !== "doctor") return true;
            const age = Math.floor(
                (Date.now() - signUpInfo.birthDate.getTime()) / (1000 * 60 * 60 * 24 * 365)
            );
            return age >= 18;
        },
        {
            message: "Doctors must be at least 18 years old",
            path: ["birthDate"],
        }
    );

export type SignUpDataInfo = z.infer<typeof SignUpDataSchema>;
