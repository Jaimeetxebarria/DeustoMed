package org.deustomed;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
@Setter
public class SpecialistDoctor extends Doctor {
    ArrayList<Patient> treatedPatients;
    ArrayList<Patient> toTreatPatients;
    ArrayList<Patient> inTreatmentPatients;

    public SpecialistDoctor(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                            @NotNull LocalDate birthDate, @NotNull Sex sex, String dni, String email, String phoneNumber,
                            String address, ArrayList<Appointment> appointments, String speciality, ArrayList<Patient> treatedPatients,
                            ArrayList<Patient> toTreatPatients, ArrayList<Patient> inTreatmentPatients) {
        super(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address, speciality, appointments);
        this.treatedPatients = treatedPatients;
        this.toTreatPatients = toTreatPatients;
        this.inTreatmentPatients = inTreatmentPatients;
    }
}
