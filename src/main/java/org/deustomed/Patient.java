package org.deustomed;

import com.google.gson.JsonObject;
import lombok.Getter;
import org.deustomed.gsonutils.GsonUtils;
import org.deustomed.postgrest.PostgrestClient;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

@Getter
public class Patient extends User {
    private ArrayList<Diagnosis> medicalRecord = new ArrayList<>();
    protected String assignedDoctorId;

    public Patient(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                   @NotNull LocalDate birthDate, @NotNull Sex sex, String dni, String email, String phoneNumber,
                   String address, ArrayList<Diagnosis> medicalRecord) {
        super(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address);
        this.medicalRecord = medicalRecord;
    }


    public Patient(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                   @NotNull LocalDate birthDate, @NotNull Sex sex) {
        super(id, name, surname1, surname2, birthDate, sex, null, null, null, null);
    }

    /**
     * @param jsonObject The JSON object to process. The function assumes that the doctor id has the key "doctor_id".
     */
    public Patient(@NotNull JsonObject jsonObject) {
        super(jsonObject);
        assignedDoctorId = GsonUtils.getStringOrNull(jsonObject, "doctor_id");
    }

    public Patient(@NotNull String id, @NotNull PostgrestClient postgrestClient) {
        super(id, postgrestClient);

    }

    public void setMedicalRecord(ArrayList<Diagnosis> medicalRecord) {
        this.medicalRecord = medicalRecord;
    }

    public void setAssignedDoctorId(String assignedDoctorId) {this.assignedDoctorId = assignedDoctorId;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Patient patient)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getMedicalRecord(), patient.getMedicalRecord());
    }

    @Override
    public String toString() {
        return "Patient{" +
                super.toString() + '\'' +
                '}';
    }



}
