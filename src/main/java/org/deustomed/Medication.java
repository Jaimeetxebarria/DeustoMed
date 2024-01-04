package org.deustomed;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import java.util.ArrayList;

public class Medication {
    public static ArrayList<Medication> medications = new ArrayList<>();
    private String id;
    private String activeSubstance;
    private String commercialName;
    private int stock;
    private double dose;
    private String compnay;
    private String shortDescription;

    public Medication(String id, String activeSubstance, String commercialName, int stock, double dose, String compnay, String shortDescription) {
        this.id = id;
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

    public static ArrayList<Medication> loadMedicationRegistry(PostgrestClient postgrestClient) {
        ArrayList<Medication> resultList = new ArrayList<>();
        PostgrestQuery query = postgrestClient
                .from("medication")
                .select("*")
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (int j = 0; j < jsonArray.size(); j++) {
            JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String activesubstance = jsonObject.get("activesubstance").getAsString();
            String commercialName = jsonObject.get("commercialname").getAsString();
            int stock = jsonObject.get("stock").getAsInt();
            double dose = jsonObject.get("dose").getAsDouble();
            String company = jsonObject.get("company").getAsString();
            String shortDescription = jsonObject.get("shortdescription").getAsString();

            Medication medication = new Medication(name, activesubstance, commercialName, stock, dose, company, shortDescription);
            resultList.add(medication);
        }
        return resultList;
    }
}
