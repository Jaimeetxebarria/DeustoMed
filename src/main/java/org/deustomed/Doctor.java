package org.deustomed;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
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
