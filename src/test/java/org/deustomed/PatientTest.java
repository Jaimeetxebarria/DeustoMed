package org.deustomed;

import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestClientFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class PatientTest {
    @Test
    void getFromDatabase() {
        PostgrestClient postgrestClient = PostgrestClientFactory.createSuperuserClient();
        Patient patient = new Patient("00AAF", postgrestClient);

        Patient expectedPatient = new Patient("00AAF", "Javier", "Ruiz", "González",
                LocalDate.parse("1992-11-29"), Sex.MALE, "67890133F", "javier.ruiz@email.com",
                "+34956789012", "Circle 678, Town", new ArrayList<>());

        assertEquals(expectedPatient, patient);
    }
}
