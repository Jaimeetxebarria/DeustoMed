package org.deustomed;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.deustomed.gsonutils.GsonUtils;
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

    /**
     * @param jsonObject The JSON object to process. The function assumes that the speciality has the key "speciality".
     */
    public Doctor(@NotNull JsonObject jsonObject) {
        super(jsonObject);
        speciality = GsonUtils.getStringOrNull(jsonObject, "speciality");
    }

    public Doctor(@NotNull String id, @NotNull PostgrestClient postgrestClient) {
        super(id, postgrestClient);
        //Get doctor data
        PostgrestQuery query = postgrestClient
                .from("doctor_with_personal_data")
                .select("speciality")
                .eq("id", id)
                .getQuery();

        JsonElement responseJson = postgrestClient.sendQuery(query);
        if (!responseJson.isJsonArray() || responseJson.getAsJsonArray().isEmpty())
            throw new RuntimeException("Doctor not found"); //TODO: Use Postgrest custom exception

        setSpeciality(GsonUtils.getStringOrNull(responseJson.getAsJsonArray().get(0).getAsJsonObject(), "speciality"));

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
                    GsonUtils.getStringOrNull(appointmentJson, "reason"),
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

    /**
     * Returns a list with the IDs of patients. Depending on the fullRegistry variable, it would be loaded all the patients
     * from the patient registry or just the patients assigned to the doctor whose id matches doctorID.
     *
     * @param doctorID the ID (char(5)) of the doctor whose patients are going to be loaded
     * @param postgrestClient connection to execute the query
     * @param fullResgistry determines whether the IDs of all the patients in the patient registry should be loaded
     */
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
                    .eq("fk_doctor_id", doctorID)
                    .getQuery();
        }

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            resultArrayList.add(id);
        }

        return resultArrayList;
    }

    /**
     * Returns a list with the IDs of patients related to a specialist doctor. Depending on the treated variable, it would load from the
     * in_treatment_patients table, either the patients whose treatment is still being carried out or has already finished.
     *
     * @param doctorID the ID (char(5)) of the doctor whose patients are going to be loaded
     * @param postgrestClient connection to execute the query
     * @param treated determines whether the IDs should correspond to already treated or being treated patients
     */
    public static ArrayList<String> loadSpecialistPatientIDs(String doctorID, PostgrestClient postgrestClient, boolean treated) {
        ArrayList<String> resultArrayList = new ArrayList<>();
        System.out.println("doctor :"+doctorID);
        String sTreated = (treated) ? "TRUE" : "FALSE";
        PostgrestQuery query = postgrestClient
                .from("in_treatment_patients")
                .select("patient_id")
                .eq("specialist_doctor_id", doctorID)
                .eq("treated", sTreated)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();


        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            String id = jsonObject.get("patient_id").getAsString();
            resultArrayList.add(id);

        }

        return resultArrayList;
    }

    /**
     * Based on the two previous methods, loads the patient instances whose IDs are in this class' lists
     * (registryOfPatients, ownPatients, inTreatmentPatients, treatedPatients) in_treatment_patients table,
     * either the patients whose treatment is still being carried out or has already finished.
     *
     * @param sourceListNumber use to determine which kind of patients are going to be loaded.
     *                         Four values: 0 -> the patients of the full registry of patients, 1 -> the doctor instance is a familyDoctor so the ownPatients of them are loaded
     *                         2 -> the doctor instance is a SpecialistDoctor and the patients that are being treated will be loaded
     *                         3 -> the doctor instance is a SpecialistDoctor and the ones whose treatment has finished are loaded
     */
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

            JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

            for (int j = 0; j < jsonArray.size(); j++) {
                JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();
                resultArrayList.add(new Patient(jsonObject));
            }
        }

        return resultArrayList;
    }
}