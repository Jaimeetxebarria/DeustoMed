package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.toedter.calendar.JDateChooser;
import org.deustomed.Appointment;
import org.deustomed.ConfigLoader;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TreeSet;

public class WindowAppointment extends JFrame {

    private JTextField patientNameField;
    private JComboBox reasonCombo;
    private JLabel dateLabel;
    private JLabel nameLabel;
    private JLabel reasonLabel;
    private JDateChooser dateChooser;
    private String patientId;
    private static PostgrestClient postgrestClient;


    public WindowAppointment(Date date, String patientID) {

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        PostgrestAuthenticationService authenticationService = new AnonymousAuthenticationService(configLoader.getAnonymousToken());
        postgrestClient = new PostgrestClient(hostname, endpoint, authenticationService);

        patientId = patientID;

        setTitle("Solicitud de Cita Médica");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dateLabel = new JLabel("Fecha de la cita:");
        nameLabel = new JLabel("Nombre del paciente:");
        reasonLabel = new JLabel("Motivo de la cita:");

        dateChooser = new JDateChooser();
        dateChooser.setMinSelectableDate(new Date());
        dateChooser.setDate(date);

        patientNameField = new JTextField(getPatientName(patientID));
        patientNameField.setEditable(false);

        reasonCombo = new JComboBox();
        reasonCombo.addItem("General");
        reasonCombo.addItem("Revisión");
        reasonCombo.addItem("Urgencia");


        JButton submitButton = new JButton("Enviar Solicitud");
        submitButton.addActionListener(e -> {
            String patientName = patientNameField.getText();

            Date selectedDate = dateChooser.getDate();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
            String formated = dateFormat.format(selectedDate);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.HOUR_OF_DAY, -24);
            Date dateminus = calendar.getTime();

            if (selectedDate != null && selectedDate.before(dateminus)) {
                JOptionPane.showMessageDialog(WindowAppointment.this,
                        "Error: La fecha seleccionada ya ha pasado.",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Appointment Generator
            PostgrestQuery query = postgrestClient
                    .from("patient")
                    .select("fk_doctor_id")
                    .eq("id", patientId)
                    .getQuery();

            JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            String docID = jsonObject.get("fk_doctor_id").getAsString();

            PostgrestQuery query2 = postgrestClient
                    .from("doctor_schedule")
                    .select("day_of_week", "start_time", "end_time")
                    .eq("doctor_id", docID)
                    .getQuery();

            JsonArray doctorScheduleJsonArray = postgrestClient.sendQuery(query2).getAsJsonArray();

            TreeSet<Appointment> appointments = new TreeSet<>();

            LocalDate localSelectedDate = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            // Loop through each day (selected date and next two days)
            for (int i = 0; i < 3; i++) {
                LocalDate currentDate = localSelectedDate.plusDays(i);
                DayOfWeek currentDayOfWeek = currentDate.getDayOfWeek();

                // Check each element in the schedule
                for (JsonElement jsonElement : doctorScheduleJsonArray) {
                    JsonObject scheduleJsonObject = jsonElement.getAsJsonObject();
                    DayOfWeek scheduleDayOfWeek = DayOfWeek.valueOf(scheduleJsonObject.get("day_of_week").getAsString().toUpperCase());

                    // Check if the day matches the doctor's schedule
                    if (currentDayOfWeek != scheduleDayOfWeek) continue;

                    String startTimeString = scheduleJsonObject.get("start_time").getAsString();
                    String endTimeString = scheduleJsonObject.get("end_time").getAsString();
                    LocalTime startTime = LocalTime.parse(startTimeString);
                    LocalTime endTime = LocalTime.parse(endTimeString);

                    // Create appointments every 15 minutes from start_time to end_time
                    LocalTime timeSlot = startTime;
                    while (timeSlot.isBefore(endTime)) {
                        LocalDateTime dateTime = LocalDateTime.of(currentDate, timeSlot);
                        Appointment appointment = new Appointment(null, docID, dateTime, null, null);
                        appointments.add(appointment);
                        timeSlot = timeSlot.plusMinutes(15);
                    }
                    break; // No other schedule elements will match the current day
                }
            }

            // Remove already created appointments
            PostgrestQuery query3 = postgrestClient
                    .from("appointment")
                    .select("*")
                    .eq("fk_doctor_id", docID)
                    .getQuery();

            JsonArray jsonArray3 = postgrestClient.sendQuery(query3).getAsJsonArray();

            for (JsonElement jsonElement : jsonArray3) {
                JsonObject jsonObject3 = jsonElement.getAsJsonObject();

                String dateStr = jsonObject3.get("date").getAsString();
                LocalDateTime date1 = LocalDateTime.parse(dateStr);

                Appointment tempAppointment = new Appointment(null, docID, date1, null, null);

                appointments.remove(tempAppointment);
            }

            new WindowAppointmentSelection(appointments, patientID);
            dispose();
        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(dateLabel)
                        .addComponent(nameLabel)
                        .addComponent(reasonLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(dateChooser)
                        .addComponent(patientNameField)
                        .addComponent(reasonCombo)
                        .addComponent(submitButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateLabel)
                        .addComponent(dateChooser))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(patientNameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(reasonLabel)
                        .addComponent(reasonCombo))
                .addComponent(submitButton)
        );

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public String getPatientName(String patientId) {
        PostgrestQuery query = postgrestClient
                .from("person")
                .select("name", "surname1", "surname2")
                .eq("id", patientId)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String surname1 = jsonObject.get("surname1").getAsString();
        String surname2 = jsonObject.get("surname2").getAsString();

        return (name + " " + surname1 + " " + surname2);
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();

        new WindowAppointment(new Date(), "00AAK");
    }
}
