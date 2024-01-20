package org.deustomed;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import java.util.ArrayList;

@Getter
@Setter
public class Disease {
    private int id;
    private String name;
    private boolean chronic;
    private boolean hereditary;

    public Disease(int id, String name, boolean chronic, boolean hereditary) {
        this.id = id;
        this.name = name;
        this.chronic = chronic;
        this.hereditary = hereditary;
    }

    public static ArrayList<Disease> loadDiseaseRegistry(PostgrestClient postgrestClient) {
        ArrayList<Disease> resultList = new ArrayList<>();
        PostgrestQuery query = postgrestClient
                .from("disease")
                .select("*")
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        for (int j = 0; j < jsonArray.size(); j++) {
            JsonObject jsonObject = jsonArray.get(j).getAsJsonObject();
            int id = jsonObject.get("id").getAsInt();
            String name = jsonObject.get("name").getAsString();
            String chronic = jsonObject.get("chronic").getAsString();
            String hereditary = jsonObject.get("hereditary").getAsString();

            boolean chronicB = chronic.equals("TRUE");
            boolean hereditaryB = hereditary.equals("TRUE");

            Disease newDisease = new Disease(id, name, chronicB, hereditaryB);
            resultList.add(newDisease);
        }
        return resultList;
    }
}
