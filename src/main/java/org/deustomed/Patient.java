package org.deustomed;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
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
