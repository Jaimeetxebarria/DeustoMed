package org.deustomed;

import java.util.ArrayList;
import java.util.Objects;

public class Doctor extends User {
    private String speciality;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public Doctor(String id, String name, String surname1, String surname2, String email, String dni, Sex sex, String speciality,
                  ArrayList<Appointment> appointments) {
        super(id, name, surname1, surname2, email, dni, sex);
        this.speciality = speciality;
        this.appointments = appointments;

    }
    public Doctor() {
        super("-1", "", "", "", "", null);
        this.speciality = "";
        this.appointments = new ArrayList<>();
    }


    public ArrayList<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appointment> appointments) {
        this.appointments = appointments;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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
                "name='" + name + '\'' +
                ", surname='" + surname1 + '\'' +
                ", id=" + id +
                ", email='" + email + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
