package org.deustomed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class Appoinment implements Comparable<Appoinment> {
    private String id;
    private Patient patient;
    private Doctor doctor;
    private LocalDateTime date;
    private String shortDesciption;
    private String longDescription;

    public Appoinment(Patient patient, Doctor doctor, LocalDateTime date, String shortDesciption, String longDescription) {
        this.patient = patient;
        this.doctor = doctor;
        this.date = date;
        this.shortDesciption = shortDesciption;
        this.longDescription = longDescription;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getShortDesciption() {
        return shortDesciption;
    }

    public void setShortDesciption(String shortDesciption) {
        this.shortDesciption = shortDesciption;
    }

    public String getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(String longDescription) {
        this.longDescription = longDescription;
    }

    @Override
    public int compareTo(Appoinment o) {
        return (this.date).compareTo(o.date);
    }

    @Override
    public String toString() {
        return "Appoinment{" +
                "patient=" + patient +
                ", doctor=" + doctor +
                ", date=" + date +
                ", shortDesciption='" + shortDesciption + '\'' +
                ", longDescription='" + longDescription + '\'' +
                '}';
    }
    public Date getDateAsDate(){
        return java.sql.Timestamp.valueOf(this.date);
    }
}
