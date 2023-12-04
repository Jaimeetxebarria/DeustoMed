package org.deustomed;

import java.util.ArrayList;

public class Medication {
    public static ArrayList<Medication> medications = new ArrayList<>();
    private String activeSubstance;
    private String commercialName;
    private int stock;
    private double dose;
    private String compnay;
    private String shortDescription;

    public Medication(String activeSubstance, String commercialName, int stock, double dose, String compnay, String shortDescription) {
        this.activeSubstance = activeSubstance;
        this.commercialName = commercialName;
        this.stock = stock;
        this.dose = dose;
        this.compnay = compnay;
        this.shortDescription = shortDescription;
    }

    public String getActiveSubstance() {
        return activeSubstance;
    }

    public void setActiveSubstance(String activeSubstance) {
        this.activeSubstance = activeSubstance;
    }

    public String getCommercialName() {
        return commercialName;
    }

    public void setCommercialName(String commercialName) {
        this.commercialName = commercialName;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getDose() {
        return dose;
    }

    public void setDose(double dose) {
        this.dose = dose;
    }

    public String getCompnay() {
        return compnay;
    }

    public void setCompnay(String compnay) {
        this.compnay = compnay;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
}
