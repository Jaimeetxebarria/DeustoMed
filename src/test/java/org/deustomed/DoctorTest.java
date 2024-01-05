package org.deustomed;

import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestClientFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoctorTest {

    @Test
    void getFromDatabase() {
        PostgrestClient postgrestClient = PostgrestClientFactory.createSuperuserClient();
        Doctor doctor = new Doctor("00AAA", postgrestClient);

        Doctor expectedDoctor = new Doctor("00AAA", "Elena", "García", "Martínez",
                LocalDate.parse("1990-05-15"), Sex.FEMALE, "12345678A", "elena.garcia@email.com",
                "+123456789", "Street 123, City", "Alergología", new ArrayList<>());

        assertEquals(expectedDoctor, doctor);
    }
}