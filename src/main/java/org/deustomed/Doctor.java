package org.deustomed;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;


@Getter
@Setter

public class Doctor extends User {
    private String speciality;
    private ArrayList<Appointment> appointments = new ArrayList<>();
    private ArrayList<String> registryOfPatients = new ArrayList<>();
    private ArrayList<String> ownPatients = new ArrayList<>();
    private ArrayList<String> inTreatmentPatients = new ArrayList<>();
    private ArrayList<String> treatedPatients = new ArrayList<>();

    public Doctor(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                  @NotNull LocalDate birthDate, @NotNull Sex sex, String dni, String email, String phoneNumber,
                  String address, String speciality, ArrayList<Appointment> appointments) {
        super(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address);
        this.speciality = speciality;
        this.appointments = appointments;

    }

    public Doctor(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                  @NotNull LocalDate birthDate, @NotNull Sex sex) {
        super(id, name, surname1, surname2, birthDate, sex, null, null, null, null);
    }

    public Doctor(@NotNull String id, @NotNull PostgrestClient postgrestClient) {
        super(id, postgrestClient);
        //Get doctor data
        PostgrestQuery query = postgrestClient.from("doctor_with_personal_data").select("speciality").eq("id", id).getQuery();
        JsonElement responseJson = postgrestClient.sendQuery(query);
        if (!responseJson.isJsonArray() || responseJson.getAsJsonArray().isEmpty())
            throw new RuntimeException("Doctor not found"); //TODO: Use Postgrest custom exception
        setSpeciality(responseJson.getAsJsonArray().get(0).getAsJsonObject().get("speciality").getAsString());

        //Get appointments
        query = postgrestClient.from("appointment_with_type").select().eq("fk_doctor_id", id).getQuery();
        responseJson = postgrestClient.sendQuery(query);
        if (!responseJson.isJsonArray())
            throw new RuntimeException("Doctor not found"); //TODO: Use Postgrest custom exception

        ArrayList<Appointment> appointments = new ArrayList<>();

        for (JsonElement jsonElement : responseJson.getAsJsonArray()) {
            JsonObject appointmentJson = jsonElement.getAsJsonObject();
            appointments.add(new Appointment(
                    appointmentJson.get("fk_patient_id").getAsString(),
                    appointmentJson.get("fk_doctor_id").getAsString(),
                    LocalDateTime.parse(appointmentJson.get("date").getAsString()),
                    appointmentJson.get("reason").getAsString(),
                    appointmentJson.get("appointment_type").getAsString()));
        }

        setAppointments(appointments);
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doctor doctor)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getSpeciality(), doctor.getSpeciality()) && Objects.equals(getAppointments(), doctor.getAppointments());
    }

    @Override
    public String toString() {
        return "Doctor{" +
                super.toString() + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }

    public static ArrayList<String> loadPatientIDs(String doctorID, PostgrestClient postgrestClient, boolean fullResgistry) {
        ArrayList<String> resultArrayList = new ArrayList<>();
        PostgrestQuery query;
        if (fullResgistry) {
            query = postgrestClient
                    .from("patient")
                    .select("*")
                    .getQuery();
        } else {
            query = postgrestClient
                    .from("patient")
                    .select("*")
                    .eq("doctor_id", doctorID)
                    .getQuery();
        }

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            resultArrayList.add(id);
        }

        return resultArrayList;
    }

    public static ArrayList<String> loadSpecialistPatientIDs(String doctorID, PostgrestClient postgrestClient, boolean treated) {
        ArrayList<String> resultArrayList = new ArrayList<>();

        String sTreated = (treated) ? "TRUE" : "FALSE";
        PostgrestQuery query = postgrestClient
                .from("in_treatment_patients")
                .select("patient_id")
                .eq("doctor_id", doctorID)
                .eq("treated", sTreated)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            resultArrayList.add(id);
        }

        return resultArrayList;
    }


    public ArrayList<Patient> loadPatients(PostgrestClient postgrestClient, int sourceListNumber) {

        ArrayList<String> sourceArraylist = switch (sourceListNumber) {
            case 0 -> new ArrayList<>(this.getRegistryOfPatients());
            case 1 -> new ArrayList<>(this.getOwnPatients());
            case 2 -> new ArrayList<>(this.getInTreatmentPatients());
            case 3 -> new ArrayList<>(this.getTreatedPatients());
            default -> new ArrayList<>();
        };

        ArrayList<Patient> resultArrayList = new ArrayList<>();

        for (int i = 0; i < sourceArraylist.size(); i++) {
            String currentPatientID = sourceArraylist.get(i);

            PostgrestQuery query = postgrestClient
                    .from("person")
                    .select("*")
                    .eq("id", currentPatientID)
                    .getQuery();

            String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
            Gson gson = new Gson();
            JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

            for (int j = 0; j < jsonArray.size(); j++) {
                JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();

                String id = jsonObject.get("id").getAsString();
                String name = jsonObject.get("name").getAsString();
                String surname1 = jsonObject.get("surname1").getAsString();
                String surname2 = jsonObject.get("surname2").getAsString();
                String dni = jsonObject.get("dni").getAsString();
                String birthdate = jsonObject.get("birthdate").getAsString();
                String email = jsonObject.get("email").getAsString();
                String phone = jsonObject.get("phone").getAsString();
                String address = jsonObject.get("address").getAsString();
                String sexString = jsonObject.get("sex").getAsString();
                Sex sex = (sexString.equals("MALE")) ? Sex.MALE : Sex.FEMALE;

                LocalDate localDate = LocalDate.parse(birthdate);

                Patient newPatient = new Patient(id, name, surname1, surname2, localDate, sex, dni, email, phone, address, new ArrayList<>());
                resultArrayList.add(newPatient);
            }
        }

        return resultArrayList;
    }

}