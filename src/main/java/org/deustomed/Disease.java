package org.deustomed;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import java.util.ArrayList;

public class Disease {
    private String name;
    private boolean chronic;
    private boolean hereditary;

    public Disease(String name, boolean chronic, boolean hereditary) {
        this.name = name;
        this.chronic = chronic;
        this.hereditary = hereditary;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChronic() {
        return chronic;
    }

    public void setChronic(boolean chronic) {
        this.chronic = chronic;
    }

    public boolean isHereditary() {
        return hereditary;
    }

    public void setHereditary(boolean hereditary) {
        this.hereditary = hereditary;
    }

    public static ArrayList<Disease> loadDiseaseRegistry(PostgrestClient postgrestClient) {
        ArrayList<Disease> resultList = new ArrayList<>();
        PostgrestQuery query = postgrestClient
                .from("disease")
                .select("*")
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (int j = 0; j < jsonArray.size(); j++) {
            JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();
            String name = jsonObject.get("name").getAsString();
            String chronic = jsonObject.get("chronic").getAsString();
            String hereditary = jsonObject.get("hereditary").getAsString();

            boolean chronicB = chronic.equals("TRUE");
            boolean hereditaryB = hereditary.equals("TRUE");

            Disease newDisease = new Disease(name, chronicB, hereditaryB);
            resultList.add(newDisease);
        }
        return resultList;
    }
}
