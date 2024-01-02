package org.deustomed.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.toedter.calendar.JDateChooser;
import org.deustomed.Appointment;
import org.deustomed.ConfigLoader;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;
import java.text.SimpleDateFormat;

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
        PostgrestAuthenticationService authenticationService =  new AnonymousAuthenticationService(configLoader.getAnonymousToken());
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
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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
                }else{
                    //TODO: Algoritmo que genere HashSet de citas libres (ordenadas por fecha)


                    //TEST DATA
                    TreeSet citas = new TreeSet();
                    LocalDateTime fecha1 = LocalDateTime.of(2023, Month.DECEMBER, 17, 6, 0);
                    LocalDateTime fecha2 = LocalDateTime.of(2023, Month.DECEMBER, 23, 14, 15);
                    LocalDateTime fecha3 = LocalDateTime.of(2023, Month.DECEMBER, 31, 19, 45);

                    Appointment ap1 = new Appointment(null, "00AAA", fecha1, null, null);
                    Appointment ap2 = new Appointment(null, "00AAB", fecha2, null, null);
                    Appointment ap3 = new Appointment(null, "00AAA", fecha3, null, null);

                    citas.add(ap1);
                    citas.add(ap2);
                    citas.add(ap3);
                    System.out.println(citas);
                    new WindowAppointmentSelection(citas);
                    dispose();
                }

            }

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

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String surname1 = jsonObject.get("surname1").getAsString();
        String surname2 = jsonObject.get("surname2").getAsString();

        return (name + " " + surname1 + " " + surname2);
    }

    public static void main(String[] args) {
        new WindowAppointment(new Date(),"00AAK");

    }
}
