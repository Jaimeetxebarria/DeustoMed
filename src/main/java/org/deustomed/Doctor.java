package org.deustomed;

import java.util.ArrayList;

public class Doctor extends User {
    private String speciality;
    private ArrayList<Appoinment> appointments = new ArrayList<>();
    private ArrayList<Patient> patients = new ArrayList<>();


    public Doctor(int id, String name, String surname1, String surname2, String email, String password, String dni, String speciality, ArrayList<Appoinment> appointments, ArrayList<Patient> patients) {
        super(id, name, surname1, surname2, email, password, dni);
        this.speciality = speciality;
        this.appointments = appointments;
        this.patients = patients;

    }
    public Doctor() {
        super(-1, "", "", "", "", "", "");
        this.speciality = "";
        this.appointments = new ArrayList<>();
        this.patients = new ArrayList<>();
    }


    public ArrayList<Patient> getPatients() {
        return patients;
    }

    public void setPatients(ArrayList<Patient> patients) {
        this.patients = patients;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public ArrayList<Appoinment> getAppointments() {
        return appointments;
    }

    public void setAppointments(ArrayList<Appoinment> appointments) {
        this.appointments = appointments;
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
