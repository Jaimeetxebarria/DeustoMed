package org.deustomed;

import lombok.Getter;
import org.deustomed.postgrest.PostgrestClient;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
public class Appointment implements Comparable<Appointment> {
    private String patientId;
    private String doctorId;
    private Patient patient;
    private Doctor doctor;
    private final LocalDateTime date;
    private String shortDescription;
    private String longDescription;

    public Appointment(String patientId, String doctorId, LocalDateTime date, String shortDescription, String longDescription) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.date = date;
        this.shortDescription = shortDescription;
        this.longDescription = longDescription;
    }

    @Override
    public int compareTo(Appointment o) {
        return (this.date).compareTo(o.date);
    }

    // Important to get them from the database and not from the constructor
    // because the personal data may change while the program is running
    public Patient getPatient(@NotNull PostgrestClient postgrestClient) {
        if (patient == null) {
            patient = new Patient(patientId, postgrestClient);
        }
        return patient;
    }

    public Doctor getDoctor(@NotNull PostgrestClient postgrestClient) {
        if (doctor == null) {
            doctor = new Doctor(doctorId, postgrestClient);
        }
        return doctor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Appointment that)) return false;
        return Objects.equals(getPatientId(), that.getPatientId()) &&
                Objects.equals(getDoctorId(), that.getDoctorId()) &&
                Objects.equals(getPatient(), that.getPatient()) &&
                Objects.equals(getDoctor(), that.getDoctor()) &&
                Objects.equals(getDate(), that.getDate()) &&
                Objects.equals(getShortDescription(), that.getShortDescription()) &&
                Objects.equals(getLongDescription(), that.getLongDescription());
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "patient=" + patientId +
                ", doctor=" + doctorId +
                ", date=" + date +
                ", shortDescription='" + shortDescription + '\'' +
                ", longDescription='" + longDescription + '\'' +
                '}';
    }
}
