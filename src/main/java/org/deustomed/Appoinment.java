package org.deustomed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Appoinment {
    private Patient patient;
    private LocalDateTime date;
    private String shortDesciption;
    private String longDescription;

    public Appoinment(Patient patient, LocalDateTime date, String shortDesciption, String longDescription) {
        this.patient = patient;
        this.date = date;
        this.shortDesciption = shortDesciption;
        this.longDescription = longDescription;
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
}
