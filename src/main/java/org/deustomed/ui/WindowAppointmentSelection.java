package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.deustomed.Appointment;
import org.deustomed.ConfigLoader;
import org.deustomed.DoctorMsgCode;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeSet;

public class WindowAppointmentSelection extends JFrame {
    protected JComboBox<String> comboBox;
    protected JTable table;
    protected JButton cancelButton;
    protected JButton confirmButton;
    private static PostgrestClient postgrestClient;
    private static final HashMap<String, String> doctorIdToName = new HashMap<>();

    public WindowAppointmentSelection(TreeSet<Appointment> appointments) {

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        PostgrestAuthenticationService authenticationService =  new AnonymousAuthenticationService(configLoader.getAnonymousToken());
        postgrestClient = new PostgrestClient(hostname, endpoint, authenticationService);

        setLayout(new BorderLayout());

        comboBox = new JComboBox<>();
        add(comboBox, BorderLayout.NORTH);

        String[] columnNames = {"Fecha","Médico", "Código Chat"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        table = new JTable(model);

        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(JLabel.CENTER);
                    return c;
                }
            });
        }

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm a");

        for (Appointment ap : appointments) {
            LocalDateTime localDateTime = ap.getDate();
            Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

            String chatCode = DoctorMsgCode.idToMsgCode(ap.getDoctorId());

            String[] row = {dateFormat.format(date), getDoctorName(ap.getDoctorId()), chatCode};
            model.addRow(row);
            comboBox.addItem(getDoctorName(ap.getDoctorId()) + " - " + dateFormat.format(date));
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        cancelButton = new JButton("Cancelar");
        confirmButton = new JButton("Confirmar");

        cancelButton.addActionListener(e -> dispose());

        confirmButton.addActionListener(e -> {
            String[] options = {"Sí", "No"};
            int confirmResult = JOptionPane.showOptionDialog(
                    null,
                    "¿Estás seguro de confirmar la cita?",
                    "Confirmación",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    new ImageIcon("src/main/java/ui/tick.png"),
                    options,
                    options[0]);

            if (confirmResult == JOptionPane.YES_OPTION) {
                // TODO: PUT PATIENT IN THE APPOINTMENT
                dispose();
            }

        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(confirmButton);
        buttonPanel.add(Box.createHorizontalGlue());
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Selección de cita");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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

    public String getDoctorName(String doctorId) {
        if (doctorIdToName.containsKey(doctorId)) return doctorIdToName.get(doctorId);

        PostgrestQuery query = postgrestClient
                .from("person")
                .select("name", "surname1", "surname2")
                .eq("id", doctorId)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String surname1 = jsonObject.get("surname1").getAsString();
        String surname2 = jsonObject.get("surname2").getAsString();

        String fullName = name + " " + surname1 + " " + surname2;
        doctorIdToName.put(doctorId, fullName);
        return fullName;
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();

        new WindowAppointmentSelection(new TreeSet<>());
    }
}
