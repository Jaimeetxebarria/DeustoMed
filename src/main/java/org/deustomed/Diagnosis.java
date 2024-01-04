package org.deustomed;

public class Diagnosis {
    private Appointment appointment;
    private Patient patient;
    private Doctor doctor;
    private String summary;

    public Diagnosis(Appointment appointment, Patient patient, Doctor doctor, String summary) {
        this.appointment = appointment;
        this.patient = patient;
        this.doctor = doctor;
        this.summary = summary;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
