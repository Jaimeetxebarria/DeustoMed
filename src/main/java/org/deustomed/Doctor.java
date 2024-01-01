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

//TODO make doctor abstract
@Getter
public class Doctor extends User {
    private String speciality;
    private ArrayList<Appointment> appointments = new ArrayList<>();

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
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname1 + '\'' +
                ", email='" + email + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
