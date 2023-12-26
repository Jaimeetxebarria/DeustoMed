package org.deustomed;

import java.util.ArrayList;

public class SpecialistDoctor extends Doctor {
    ArrayList<Patient> treatedPatients;
    ArrayList<Patient> toTreatPatients;
    ArrayList<Patient> inTreatmentPatients;

    public SpecialistDoctor(String id, String name, String surname1, String surname2, String email, String password, String dni, Sex sex, String speciality, ArrayList<Appointment> appointments, ArrayList<Patient> treatedPatients, ArrayList<Patient> toTreatPatients, ArrayList<Patient> inTreatmentPatients) {
        super(id, name, surname1, surname2, email, password, dni, sex, speciality, appointments);
        this.treatedPatients = treatedPatients;
        this.toTreatPatients = toTreatPatients;
        this.inTreatmentPatients = inTreatmentPatients;
    }

    public SpecialistDoctor(ArrayList<Patient> treatedPatients, ArrayList<Patient> toTreatPatients, ArrayList<Patient> inTreatmentPatients) {
        this.treatedPatients = treatedPatients;
        this.toTreatPatients = toTreatPatients;
        this.inTreatmentPatients = inTreatmentPatients;
    }

    public ArrayList<Patient> getTreatedPatients() {
        return treatedPatients;
    }

    public void setTreatedPatients(ArrayList<Patient> treatedPatients) {
        this.treatedPatients = treatedPatients;
    }

    public ArrayList<Patient> getToTreatPatients() {
        return toTreatPatients;
    }

    public void setToTreatPatients(ArrayList<Patient> toTreatPatients) {
        this.toTreatPatients = toTreatPatients;
    }

    public ArrayList<Patient> getInTreatmentPatients() {
        return inTreatmentPatients;
    }

    public void setInTreatmentPatients(ArrayList<Patient> inTreatmentPatients) {
        this.inTreatmentPatients = inTreatmentPatients;
    }
}
