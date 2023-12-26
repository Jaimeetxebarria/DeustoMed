package org.deustomed;

import java.util.ArrayList;

public class FamilyDoctor extends Doctor{
    ArrayList<Patient> ownPatients;

    public FamilyDoctor(String id, String name, String surname1, String surname2, String email, String dni, Sex sex,
                        ArrayList<Appointment> appointments, ArrayList<Patient> ownPatients) {
        super(id, name, surname1, surname2, email, dni, sex, "Medicina Familiar", appointments);
        this.ownPatients = ownPatients;
    }

    public FamilyDoctor(ArrayList<Patient> ownPatients) {
        this.ownPatients = ownPatients;
    }

    public ArrayList<Patient> getOwnPatients() {
        return ownPatients;
    }

    public void setOwnPatients(ArrayList<Patient> ownPatients) {
        this.ownPatients = ownPatients;
    }
}
