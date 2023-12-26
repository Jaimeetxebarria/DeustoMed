package org.deustomed;

public class Diagnosis {
    private Appointment appointment;
    private Patient patient;
    private Disease disease;
    private Doctor doctor;

    public Diagnosis(Appointment appointment, Patient patient, Disease disease, Doctor doctor) {
        this.appointment = appointment;
        this.patient = patient;
        this.disease = disease;
        this.doctor = doctor;
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

    public Disease getDisease() {
        return disease;
    }

    public void setDisease(Disease disease) {
        this.disease = disease;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }
}
