package org.deustomed;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.deustomed.*;
import org.deustomed.gsonutils.GsonUtils;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

@Getter @Setter
public class Diagnosis {
    private Appointment appointment;
    private Patient patient;
    private String doctor;
    private String summary;
    private ArrayList<Medication> prescribedMedication;
    private ArrayList<Medication> retiredMedication;
    private ArrayList<Disease> diagnosedDiseases;
    private ArrayList<Disease> curedDiseases;

    public Diagnosis(Appointment appointment, Patient patient, String doctor, String summary, ArrayList<Medication> prescribedMedication, ArrayList<Medication> retiredMedication, ArrayList<Disease> diagnosedDiseases, ArrayList<Disease> curedDiseases) {
        this.appointment = appointment;
        this.patient = patient;
        this.doctor = doctor;
        this.summary = summary;
        this.prescribedMedication = prescribedMedication;
        this.retiredMedication = retiredMedication;
        this.diagnosedDiseases = diagnosedDiseases;
        this.curedDiseases = curedDiseases;
    }


    public static ArrayList<Medication> loadDiagnosisMedication(String diagnosisID, PostgrestClient postgrestClient, boolean prescribed) {
        ArrayList<Medication> resultArrayList = new ArrayList<>();

        String prescribedString = (prescribed) ? "TRUE" : "FALSE";
        PostgrestQuery query = postgrestClient
                .from("diagnosis_medication")
                .select("*")
                .eq("diagnosisID", diagnosisID)
                .eq("prescribed/retired", prescribedString)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();

            PostgrestQuery query2 = postgrestClient
                    .from("medication")
                    .select("*")
                    .eq("id", String.valueOf(id))
                    .getQuery();

            JsonArray jsonArray2 = postgrestClient.sendQuery(query2).getAsJsonArray();
            JsonObject jsonObject2 = jsonArray2.get(0).getAsJsonObject();

            String activeSubstance = jsonObject2.get("activesubstance").getAsString();
            String commercialName = jsonObject2.get("commercialname").getAsString();
            int stock = jsonObject2.get("stock").getAsInt();
            float dose = jsonObject2.get("dose").getAsFloat();
            String company = jsonObject2.get("company").getAsString();
            String shortDescription = jsonObject2.get("shortdescription").getAsString();

            Medication newMedication = new Medication(id, activeSubstance, commercialName, stock, dose, company, shortDescription);
            resultArrayList.add(newMedication);
        }

        return resultArrayList;
    }

    public static ArrayList<Disease> loadDiagnosisDiseases(int diagnosisID, PostgrestClient postgrestClient, boolean addOrRemove) {
        ArrayList<Disease> resultArrayList = new ArrayList<>();

        String diagnosedString = (addOrRemove) ? "TRUE" : "FALSE";
        PostgrestQuery query = postgrestClient
                .from("diagnosis_disease")
                .select("*")
                .eq("diagnosisID", String.valueOf(diagnosisID))
                .eq("daignosed/cured", diagnosedString)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();

            PostgrestQuery query2 = postgrestClient
                    .from("medication")
                    .select("*")
                    .eq("id", String.valueOf(id))
                    .getQuery();

            JsonArray jsonArray2 = postgrestClient.sendQuery(query2).getAsJsonArray();
            JsonObject jsonObject2 = jsonArray2.get(0).getAsJsonObject();

            String name = jsonObject2.get("name").getAsString();
            boolean chronic = jsonObject2.get("name").getAsBoolean();
            boolean hereditary = jsonObject2.get("hereditary").getAsBoolean();

            Disease newDisease = new Disease(id, name, chronic, hereditary);
            resultArrayList.add(newDisease);
        }

        return resultArrayList;
    }

    public static void updateDiagnosisRelations(int diagnosisID, String patientID, ArrayList<Disease> diseases, ArrayList<Medication> medications, boolean diseaseOrMedication, boolean addOrRemove, PostgrestClient postgrestClient) {
        String additionalKey = (diseaseOrMedication) ? "disease_id" : "medication_id";
        String table = (diseaseOrMedication) ? "diagnosis_disease" : "diagnosis_medication";

        if (diseaseOrMedication) {
            for (Disease d : diseases) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("diagnosis_id", diagnosisID);
                jsonObject.addProperty(additionalKey, d.getId());
                jsonObject.addProperty("add_or_remove", addOrRemove);

                PostgrestQuery query = postgrestClient
                        .from(table)
                        .insert(jsonObject)
                        .select()
                        .getQuery();

                System.out.println(String.valueOf(postgrestClient.sendQuery(query)));
                if(addOrRemove){
                    updatePatientRelation(patientID, d.getId(), true, postgrestClient);
                } else {
                    removePatientRelation(patientID, d.getId(), true, postgrestClient);
                }
            }
        } else {
            for (Medication m : medications) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("diagnosis_id", diagnosisID);
                jsonObject.addProperty(additionalKey, m.getId());
                jsonObject.addProperty("add_or_remove", addOrRemove);

                PostgrestQuery query = postgrestClient
                        .from(table)
                        .insert(jsonObject)
                        .select()
                        .getQuery();

                System.out.println(String.valueOf(postgrestClient.sendQuery(query)));
                if(addOrRemove){
                    updatePatientRelation(patientID, m.getId(), false, postgrestClient);
                } else {
                    removePatientRelation(patientID, m.getId(), false, postgrestClient);
                }
            }
        }
    }

    public static void removePatientRelation (String patientID, int relationID, boolean diseaseOrMedication, PostgrestClient postgrestClient) {
        String additionalFK = (diseaseOrMedication) ? "fk_disease_id" : "fk_medication_id";
        String table = (diseaseOrMedication) ? "patient_suffers_disease" : "patient_undergoes_treatment";

        PostgrestQuery query = postgrestClient
                .from(table)
                .delete()
                .eq("fk_patient_id", patientID)
                .eq(additionalFK, String.valueOf(relationID))
                .getQuery();

        //postgrestClient.sendQuery(query);
        System.out.println("Remove anwser: "+String.valueOf(postgrestClient.sendQuery(query)));
    }

    public static void updatePatientRelation (String patientID, int relationID, boolean diseaseOrMedication, PostgrestClient postgrestClient) {
        String additionalFK = (diseaseOrMedication) ? "fk_disease_id" : "fk_medication_id";
        String table = (diseaseOrMedication) ? "patient_suffers_disease" : "patient_undergoes_treatment";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fk_patient_id", patientID);
        jsonObject.addProperty(additionalFK, relationID);

        PostgrestQuery query = postgrestClient
                .from(table)
                .insert(jsonObject)
                .select()
                .getQuery();

        //postgrestClient.sendQuery(query);
        System.out.println(String.valueOf(postgrestClient.sendQuery(query)));
    }
}