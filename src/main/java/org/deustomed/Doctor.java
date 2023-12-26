package org.deustomed;

import java.util.ArrayList;

public class Doctor extends User {
    private String speciality;
    private ArrayList<Appointment> appointments = new ArrayList<>();

    public Doctor(String id, String name, String surname1, String surname2, String email, String password, String dni, Sex sex,  String speciality, ArrayList<Appointment> appointments) {
        super(id, name, surname1, surname2, email, password, dni, sex);
        this.speciality = speciality;
        this.appointments = appointments;

    }
    public Doctor() {
        super("-1", "", "", "", "", "", "");
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
