package org.deustomed;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
public class SpecialistDoctor extends Doctor {
    ArrayList<Patient> treatedPatients;
    ArrayList<Patient> toTreatPatients;
    ArrayList<Patient> inTreatmentPatients;

    public SpecialistDoctor(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                            @NotNull LocalDate birthDate, @NotNull Sex sex, String dni, String email, String phoneNumber,
                            String address, ArrayList<Appointment> appointments, String speciality, ArrayList<Patient> treatedPatients,
                            ArrayList<Patient> toTreatPatients, ArrayList<Patient> inTreatmentPatients) {
        super(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address, speciality, appointments);
        this.treatedPatients = treatedPatients;
        this.toTreatPatients = toTreatPatients;
        this.inTreatmentPatients = inTreatmentPatients;
    }

    public static ArrayList<Patient> loadSpecialistPatients(String doctorID, PostgrestClient postgrestClient, String whichPatients) {

        ArrayList<Patient> resultArrayList = new ArrayList<>();

        PostgrestQuery query = postgrestClient
                .from(whichPatients)
                .select("patient_id")
                .eq("doctor_id", doctorID)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String id = jsonObject.get("id").getAsString();

            PostgrestQuery queryPatient = postgrestClient
                    .from("patient_with_personal_data")
                    .select("*")
                    .eq("id", id)
                    .getQuery();

            String jsonResponsePatient = String.valueOf(postgrestClient.sendQuery(queryPatient));
            Gson gsonPatient = new Gson();
            JsonArray jsonArrayPatient = gsonPatient.fromJson(jsonResponsePatient, JsonArray.class);

            for (int j = 0; j < jsonArrayPatient.size(); j++) {
                JsonObject jsonObjectPatient = jsonArrayPatient.get(j).getAsJsonObject();

                String idPatient = jsonObjectPatient.get("id").getAsString();
                String name = jsonObjectPatient.get("name").getAsString();
                String surname1 = jsonObjectPatient.get("surname1").getAsString();
                String surname2 = jsonObjectPatient.get("surname2").getAsString();
                String dni = jsonObjectPatient.get("dni").getAsString();
                String birthdate = jsonObjectPatient.get("birthdate").getAsString();
                String email = jsonObjectPatient.get("email").getAsString();
                String phone = jsonObjectPatient.get("phone").getAsString();
                String address = jsonObjectPatient.get("address").getAsString();
                String sexString = jsonObjectPatient.get("sex").getAsString();
                Sex sex = (sexString.equals("MALE")) ? Sex.MALE : Sex.FEMALE;

                LocalDate localDate = LocalDate.parse(birthdate);

                Patient newPatient = new Patient(idPatient, name, surname1, surname2, localDate, sex, dni, email, phone, address, new ArrayList<>());
                resultArrayList.add(newPatient);
            }
        }
        return resultArrayList;
    }
}