package org.deustomed;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

@Getter
public class Patient extends User {
    private ArrayList<Appointment> medicalRecord = new ArrayList<>();

    public Patient(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                   @NotNull LocalDate birthDate, @NotNull Sex sex, String dni, String email, String phoneNumber,
                   String address, ArrayList<Appointment> medicalRecord) {
        super(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address);
        this.medicalRecord = medicalRecord;
    }


    public Patient(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                   @NotNull LocalDate birthDate, @NotNull Sex sex) {
        super(id, name, surname1, surname2, birthDate, sex, null, null, null, null);
    }

    public Patient(@NotNull String id, @NotNull PostgrestClient postgrestClient) {
        super(id, postgrestClient);

        //Get medical record (appointments)
        PostgrestQuery query = postgrestClient.from("appointment_with_type").select().eq("fk_patient_id", id).getQuery();
        JsonElement responseJson = postgrestClient.sendQuery(query);
        if (!responseJson.isJsonArray()) throw new RuntimeException("Doctor not found"); //TODO: Use Postgrest custom exception

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

        setMedicalRecord(appointments);
    }

    public void setMedicalRecord(ArrayList<Appointment> medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getMedicalRecord(), patient.getMedicalRecord());
    }

    @Override
    public String toString() {
        return "Patient{" +
                "name='" + name + '\'' +
                ", surname1='" + surname1 + '\'' +
                ", surname2='" + surname2 + '\'' +
                ", email='" + email + '\'' +
                ", dni='" + dni + '\'' +
                ", address='" + address + '\'' +
                ", birthDate=" + birthDate +
                ", Sex='" + sex + '\'' +
                '}';
    }
}
