package org.deustomed;

import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestClientFactory;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DoctorTest {

    @Test
    void getFromDatabase() {
        PostgrestClient postgrestClient = PostgrestClientFactory.createSuperuserClient();
        Doctor doctor = new Doctor("00AAA", postgrestClient);

        List<Appointment> expectedAppointments = List.of(
                new Appointment("00ABG", "00AAA",
                        LocalDateTime.of(2024, 1, 22, 14, 30, 0),
                        "Posible choque anafiláctico leve", "General"),
                new Appointment("00ABH", "00AAA",
                        LocalDateTime.of(2024, 1, 22, 14, 45, 0),
                        "Síntomas de alergia", "General")
        );

        Doctor expectedDoctor = new Doctor("00AAA", "Elena", "García", "Martínez",
                LocalDate.parse("1990-05-15"), Sex.FEMALE, "12345679A", "elena.garcia@email.com",
                "623456789", "Street 123, City", "Alergología", new ArrayList<>(expectedAppointments));

        assertEquals(expectedDoctor, doctor);
    }
}