package org.deustomed;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;

@Getter
public class FamilyDoctor extends Doctor{
    @Setter
    ArrayList<Patient> ownPatients;

    public FamilyDoctor(@NotNull String id, @NotNull String name, @NotNull String surname1, @NotNull String surname2,
                        @NotNull LocalDate birthDate, @NotNull Sex sex, String dni, String email, String phoneNumber,
                        String address, ArrayList<Appointment> appointments, ArrayList<Patient> ownPatients) {
        super(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address, "Medicina Familiar", appointments);
        this.ownPatients = ownPatients;
    }
}
