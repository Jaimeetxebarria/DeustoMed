package org.deustomed;

import java.util.ArrayList;

public class Doctor extends User {
    private String speciality;
    private ArrayList<Appoinment> appointments = new ArrayList<>();

    public Doctor(int id, String name, String surname1, String surname2, String email, String password, String dni, Sex sex,  String speciality, ArrayList<Appoinment> appointments) {
        super(id, name, surname1, surname2, email, password, dni, sex);
        this.speciality = speciality;
        this.appointments = appointments;

    }
    public Doctor() {
        super(-1, "", "", "", "", "", "");
        this.speciality = "";
        this.appointments = new ArrayList<>();
    }

    public ArrayList<Appoinment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appoinment> appointments) {
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
                ", password='" + password + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
