package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDayChooser;
import lombok.Getter;
import org.deustomed.ConfigLoader;
import org.deustomed.DoctorMsgCode;
import org.deustomed.GreenDateHighlighter;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.authentication.UserAuthenticationService;
import org.deustomed.chat.ChatUser;
import org.deustomed.chat.MessageCheckerThread;
import org.deustomed.gsonutils.GsonUtils;
import org.deustomed.logs.LoggerMaker;
import org.deustomed.postgrest.Entry;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.logging.Logger;

public class WindowPatient extends UserAuthenticatedWindow implements MessageCheckerThread {
    protected String selectedButton = ""; //info, calendar,chat
    protected String prevDirect, prevTfn, prevEmail;

    protected JButton infoButton, calendarButton,chatButton, logoutButton, pedirCitaButton, guardarCambiosButton;
    protected JPanel menuPanel, infoPanel, calendarPanel, chatPanel;

    @Getter
    protected JCalendar calendar;
    protected JTable calendarTable;
    protected DefaultTableModel calendarTableModel;

    protected JLabel lblNombre, lblApellido1, lblApellido2, lblEmail, lblDNI, lblTelefono, lblFechaNacimiento, lblDireccion;
    protected JTextField txtNombre, txtApellido1, txtApellido2, txtEmail, txtDNI, txtTelefono, txtFechaNacimiento, txtDireccion;

    protected JTextPane chatArea = new JTextPane();
    protected StyledDocument chatDoc = chatArea.getStyledDocument();
    protected JTextField messageField;
    protected JButton sendButton;
    protected JButton saveChatButton;
    @Getter
    protected String lastMessage = "";
    protected String patientId;
    final String[] docCode = {""};
    protected Thread msgThread;
    private static PostgrestClient postgrestClient;
    private Logger logger;
    private GreenDateHighlighter highlighter =  new GreenDateHighlighter();

    public WindowPatient(String patientId, PostgrestAuthenticationService authenticationService) {
        super(authenticationService instanceof UserAuthenticationService ? (UserAuthenticationService) authenticationService : null);
        this.patientId = patientId;

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        postgrestClient = new PostgrestClient(hostname, endpoint, authenticationService);

        LoggerMaker.setlogFilePath("src/main/java/org/deustomed/logs/WindowPatient.log");
        logger = LoggerMaker.getLogger();

        //WINDOW SETTINGS--------------------------------------------------------------------------------------------
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Button.foreground", Color.BLACK);

        setTitle("Paciente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        //SIDE MENU PANEL--------------------------------------------------------------------------------------------

        Color colorizq = Color.DARK_GRAY;
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(colorizq);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ImageIcon infoIcon = new ImageIcon("src/main/java/ui/info.png");
        ImageIcon calendarIcon = new ImageIcon("src/main/java/ui/calendario.png");
        ImageIcon chatIcon = new ImageIcon("src/main/java/ui/mensaje.png");
        ImageIcon logoutIcon = new ImageIcon("src/main/java/ui/logout.png");

        infoButton = new JButton(infoIcon);
        infoButton.setBackground(colorizq);
        infoButton.setBorder(null);
        calendarButton = new JButton(calendarIcon);
        calendarButton.setBackground(colorizq);
        calendarButton.setBorder(null);
        chatButton = new JButton(chatIcon);
        chatButton.setBackground(colorizq);
        chatButton.setBorder(null);
        logoutButton = new JButton(logoutIcon);
        logoutButton.setBackground(colorizq);
        logoutButton.setBorder(null);

        infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(infoButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(calendarButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(chatButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 80)));
        menuPanel.add(logoutButton);

        //CALENDAR PANEL--------------------------------------------------------------------------------------------

        calendarPanel = new JPanel(new BorderLayout());
        calendar = new JCalendar();
        calendar.setPreferredSize(new Dimension(400, 400));
        calendar.setWeekOfYearVisible(false);
        calendarPanel.add(calendar, BorderLayout.WEST);

        calendarTableModel = new DefaultTableModel();
        String[] calendarColumns = {"Fecha", "Motivo cita", "Médico", "Código Chat"};
        calendarTableModel.setColumnIdentifiers(calendarColumns);
        updateCalendarTable();
        calendarTable = new JTable(calendarTableModel);
        JScrollPane scrollPane = new JScrollPane(calendarTable);
        calendarPanel.add(scrollPane, BorderLayout.CENTER);
        calendarTable.setDefaultEditor(Object.class, null);

        pedirCitaButton = new JButton("Pedir Cita");
        calendarPanel.add(pedirCitaButton, BorderLayout.SOUTH);

        calendar.getDayChooser().addDateEvaluator(highlighter);

        JDayChooser dayChooser = calendar.getDayChooser();
        dayChooser.addPropertyChangeListener("day", evt -> calendar.setCalendar(calendar.getCalendar()));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                switch (selectedButton) {
                    case "calendar" -> {
                        deleteContent();
                        getContentPane().add(calendarPanel, BorderLayout.CENTER);
                    }
                    case "info" -> {
                        deleteContent();
                        getContentPane().add(infoPanel, BorderLayout.CENTER);
                    }
                    case "chat" -> {
                        deleteContent();
                        getContentPane().add(chatPanel, BorderLayout.CENTER);
                    }
                }
            }
        });


        calendarButton.addActionListener(e -> {
            deleteContent();
            getContentPane().add(calendarPanel, BorderLayout.CENTER);
            selectedButton = "calendar";
            revalidate();
            calendar.setCalendar(calendar.getCalendar());
        });

        pedirCitaButton.addActionListener(e -> {
            WindowAppointment windowAppointment = new WindowAppointment(calendar.getDate(),patientId,this);
        });


        //LOGOUT------------------------------------------------------------------------------------------------------------

        logoutButton.addActionListener(e -> {
            Object[] options = {"Sí", "No"};
            ImageIcon atentionIcon = new ImageIcon("src/main/java/ui/atencion.png");
            int option = JOptionPane.showOptionDialog(null, "¿Estás seguro de que quieres cerrar sesión?",
                    "Confirmar Cierre de Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, atentionIcon, options,
                    options[0]);

            if (option == JOptionPane.YES_OPTION) {
                dispose();
                new WindowLogin();
            }

        });

        //CHAT PANEL---------------------------------------------------------------------------------------------------------
        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel chatButtonsPanel = new JPanel(new FlowLayout());

        chatArea.setEditable(false);
        chatArea.setBackground(Color.lightGray);
        JScrollPane scrollPaneChat = new JScrollPane(chatArea);
        chatPanel.add(scrollPaneChat, BorderLayout.CENTER);

        messageField = new JTextField();
        sendButton = new JButton("Enviar");
        saveChatButton = new JButton("Descargar");
        chatButtonsPanel.add(sendButton);
        chatButtonsPanel.add(saveChatButton);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(chatButtonsPanel, BorderLayout.EAST);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        chatButton.addActionListener(e -> {
            deleteContent();
            getContentPane().add(chatPanel, BorderLayout.CENTER);
            selectedButton = "chat";
            revalidate();
            showDoctorCodeDialog();
        });

        sendButton.addActionListener(e -> {
            try {
                sendMessage();
            } catch (BadLocationException ex) {
                logger.severe("Error al enviar mensaje: " + ex.getMessage() + "por el paciente: " + patientId);
            }
        });

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        sendMessage();
                    } catch (BadLocationException ex) {
                        logger.severe("Error al enviar mensaje: " + ex.getMessage() + "por el paciente: " + patientId);
                    }
                }
            }
        });

        saveChatButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar chat como...");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                saveChatToFile(fileToSave.getAbsolutePath());
            }
        });


        //INFO MENU PANEL-----------------------------------------------------------------------------------------------------

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();
        lblApellido1 = new JLabel("Primer Apellido:");
        txtApellido1 = new JTextField();
        lblApellido2 = new JLabel("Segundo Apellido:");
        txtApellido2 = new JTextField();
        lblDNI = new JLabel("DNI:");
        txtDNI = new JTextField();
        lblFechaNacimiento = new JLabel("Fecha de Nacimiento:");
        txtFechaNacimiento = new JTextField();
        lblTelefono = new JLabel("Teléfono:");
        txtTelefono = new JTextField();
        lblEmail = new JLabel("Email:");
        txtEmail = new JTextField();
        lblDireccion = new JLabel("Dirección:");
        txtDireccion = new JTextField();
        guardarCambiosButton = new JButton("Guardar cambios");

        txtNombre.setEditable(false);
        txtApellido1.setEditable(false);
        txtApellido2.setEditable(false);
        txtDNI.setEditable(false);
        txtFechaNacimiento.setEditable(false);

        infoPanel.add(lblNombre);
        infoPanel.add(txtNombre);
        infoPanel.add(lblApellido1);
        infoPanel.add(txtApellido1);
        infoPanel.add(lblApellido2);
        infoPanel.add(txtApellido2);
        infoPanel.add(lblDNI);
        infoPanel.add(txtDNI);
        infoPanel.add(lblFechaNacimiento);
        infoPanel.add(txtFechaNacimiento);
        infoPanel.add(lblEmail);
        infoPanel.add(txtEmail);
        infoPanel.add(lblTelefono);
        infoPanel.add(txtTelefono);
        infoPanel.add(lblDireccion);
        infoPanel.add(txtDireccion);
        infoPanel.add(guardarCambiosButton);

        setInfoPanelTexfields();

        infoButton.addActionListener(e -> {
            deleteContent();
            getContentPane().add(infoPanel, BorderLayout.CENTER);
            selectedButton = "info";
            revalidate();
        });

        guardarCambiosButton.setEnabled(false);

        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                guardarCambiosButton.setEnabled(!prevEmail.equals(txtEmail.getText()));
            }
        });

        txtTelefono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                guardarCambiosButton.setEnabled(!prevTfn.equals(txtTelefono.getText()));
            }
        });

        txtDireccion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                guardarCambiosButton.setEnabled(!prevDirect.equals(txtDireccion.getText()));
            }
        });

        guardarCambiosButton.addActionListener(e -> {
            prevDirect = txtDireccion.getText();
            prevEmail = txtEmail.getText();
            prevTfn = txtTelefono.getText();

            PostgrestQuery query = postgrestClient.from("person")
                    .update(new Entry<>("email", prevEmail),
                            new Entry<>("phone", prevTfn),
                            new Entry<>("address", prevDirect))
                    .eq("id", patientId)
                    .select()
                    .getQuery();
            postgrestClient.sendQuery(query);
            guardarCambiosButton.setEnabled(false);
        });


        //--------------------------------------------------------------------------------------------------------------------

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.WEST);
        JPanel contentPanel = new JPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }
    //OTHER METHODS------------------------------------------------------------------------------------------------------

    /**
     * Removes all the contents of WindowPatient, setting the side menu
     */
    public void deleteContent() {
        getContentPane().removeAll();
        getContentPane().add(menuPanel, BorderLayout.WEST);
        revalidate();
        repaint();
    }

    /**
     * Set the infoPanel textfields to DB values
     */
    public void setInfoPanelTexfields() {

        PostgrestQuery query = postgrestClient
                .from("person")
                .select("*")
                .eq("id", patientId)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String surname1 = jsonObject.get("surname1").getAsString();
        String surname2 = jsonObject.get("surname2").getAsString();
        String dni = GsonUtils.getStringOrNull(jsonObject, "dni");
        String birthdate = jsonObject.get("birthdate").getAsString();
        String email = GsonUtils.getStringOrNull(jsonObject, "email");
        String phone = GsonUtils.getStringOrNull(jsonObject, "phone");
        String address = GsonUtils.getStringOrNull(jsonObject, "address");

        LocalDate date = LocalDate.parse(birthdate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formatted = date.format(formatter);

        txtNombre.setText(name);
        txtApellido1.setText(surname1);
        txtApellido2.setText(surname2);
        txtDNI.setText(dni);
        txtFechaNacimiento.setText(formatted);
        txtDireccion.setText(address);
        txtTelefono.setText(phone);
        txtEmail.setText(email);

        prevEmail = txtEmail.getText();
        prevDirect = txtDireccion.getText();
        prevTfn = txtTelefono.getText();
    }

    /**
     * Add all the appointments from the logged patient to the JTable at CalendarPanel using the DB
     */
    public void updateCalendarTable() {

        calendarTableModel.setRowCount(0);

        PostgrestQuery query = postgrestClient
                .from("appointment")
                .select("*")
                .eq("fk_patient_id", patientId)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        highlighter.clearDates();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject appointmentObject = jsonElement.getAsJsonObject();

            String datetime = appointmentObject.get("date").getAsString();
            String reason = appointmentObject.get("reason").getAsString();
            String fkDoctorId = appointmentObject.get("fk_doctor_id").getAsString();
            String chatCode = DoctorMsgCode.idToMsgCode(fkDoctorId);

            LocalDateTime dateTime = LocalDateTime.parse(datetime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy hh:mm a");
            String formatted = dateTime.format(formatter);

            PostgrestQuery query1 = postgrestClient
                    .from("person")
                    .select("name", "surname1", "surname2")
                    .eq("id", fkDoctorId)
                    .getQuery();

            JsonArray jsonArray1 = postgrestClient.sendQuery(query1).getAsJsonArray();
            JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String surname1 = jsonObject.get("surname1").getAsString();
            String surname2 = jsonObject.get("surname2").getAsString();
            String docname = String.format("%s %s %s", name, surname1, surname2);


            Object[] rowData = {formatted, reason, docname, chatCode};
            calendarTableModel.addRow(rowData);

            Date dateToHighlight = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
            highlighter.addDate(dateToHighlight);
        }

        calendar.getDayChooser().invalidate();
        calendar.getDayChooser().validate();
        calendar.repaint();
    }

    private void sendMessage() throws BadLocationException {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("fk_patient_id", patientId);
            jsonObject.addProperty("fk_doctor_id", docCode[0]);
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("patient_sent", true);
            jsonObject.addProperty("patient_read", false);
            jsonObject.addProperty("doctor_read", false);
            jsonObject.addProperty("date", LocalDateTime.now().toString());

            PostgrestQuery query = postgrestClient
                    .from("message")
                    .insert(jsonObject)
                    .select()
                    .getQuery();

            postgrestClient.sendQuery(query);

            messageField.setText("");
            lastMessage = message;
        }
    }

    public void recieveMessage(String message) throws BadLocationException {
        chatDoc.insertString(chatDoc.getLength(), "Doctor: " + message + "\n", null);
    }

    private void showDoctorCodeDialog() {

        JDialog dialog = new JDialog();
        dialog.setLayout(new BorderLayout());
        dialog.setSize(300, 120);
        dialog.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        dialog.setTitle("Abrir chat");

        JTextField doctorCodeField = new JTextField();
        doctorCodeField.setToolTipText("Código para chat proporcionado por el médico");

        ArrayList<ChatUser> opcionesList = new ArrayList<>();

        PostgrestQuery query = postgrestClient
                .from("message")
                .select("fk_doctor_id")
                .eq("fk_patient_id", patientId)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        HashSet<String> uniqueDoctorIds = new HashSet<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject messageObject = jsonElement.getAsJsonObject();
            String doctorId = messageObject.get("fk_doctor_id").getAsString();

            // Añadir al modelo solo si el ID del paciente es único
            if (uniqueDoctorIds.add(doctorId)) {
                PostgrestQuery query1 = postgrestClient
                        .from("person")
                        .select("id","name","surname1","surname2")
                        .eq("id", doctorId)
                        .getQuery();

                JsonArray jsonArray1 = postgrestClient.sendQuery(query1).getAsJsonArray();
                JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();

                String name = jsonObject.get("name").getAsString();
                String surname1 = jsonObject.get("surname1").getAsString();
                String surname2 = jsonObject.get("surname2").getAsString();
                String id = jsonObject.get("id").getAsString();

                ChatUser user = new ChatUser(name, surname1, surname2, id);
                opcionesList.add(user);
            }
        }

        JComboBox<ChatUser> comboBox = new JComboBox<>(new DefaultComboBoxModel<>(opcionesList.toArray(new ChatUser[0])));

        JButton confirmButton = new JButton("Confirmar");
        JButton cancelButton = new JButton("Cancelar");

        confirmButton.addActionListener(e -> {

            try {
                chatDoc.remove(0, chatDoc.getLength());
            } catch (BadLocationException ex) {
                logger.severe("Error al limpiar el chat: " + ex.getMessage() + "por el paciente: " + patientId);
            }

            if (!doctorCodeField.getText().isEmpty()) {
                docCode[0] = DoctorMsgCode.MsgCodeToId(doctorCodeField.getText());
            }else{
                ChatUser selectedUser = (ChatUser) comboBox.getSelectedItem();
                if (selectedUser != null) {
                    docCode[0] = selectedUser.getId();
                }
            }

            //get doctor and patient full names

            String docFullName = getDoctorName(docCode[0]);
            String patFullName = getPatientName(patientId);

            //Retrieve previous messages
            PostgrestQuery query12 = postgrestClient
                    .from("message")
                    .select("*")
                    .eq("fk_patient_id", patientId)
                    .eq("fk_doctor_id", docCode[0])
                    .order("date", true)
                    .getQuery();

            PostgrestQuery updateQuery = postgrestClient
                    .from("message")
                    .update(new Entry<>("patient_read", true))
                    .eq("fk_patient_id", patientId)
                    .eq("fk_doctor_id", docCode[0])
                    .getQuery();

            JsonArray jsonArray12 = postgrestClient.sendQuery(query12).getAsJsonArray();
            postgrestClient.sendQuery(updateQuery).getAsJsonArray();

            for (JsonElement jsonElement : jsonArray12) {
                JsonObject messageObject = jsonElement.getAsJsonObject();

                String message = messageObject.get("message").getAsString();
                String datetime = messageObject.get("date").getAsString();
                boolean patientSent = messageObject.get("patient_sent").getAsBoolean();
                LocalDateTime date = LocalDateTime.parse(datetime);
                String dateFormatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                SimpleAttributeSet bold = new SimpleAttributeSet();
                StyleConstants.setBold(bold, true);

                try {
                    if (patientSent) {
                        chatDoc.insertString(chatDoc.getLength(), patFullName + " " + dateFormatted + ":\n", bold);
                    } else {
                        chatDoc.insertString(chatDoc.getLength(), docFullName + " " + dateFormatted + ":\n", bold);
                    }
                    chatDoc.insertString(chatDoc.getLength(), message + "\n\n", null);
                } catch (BadLocationException ex) {
                    logger.severe("Error al mostrar el chat: " + ex.getMessage() + "por el paciente: " + patientId);
                }
            }

            dialog.dispose();
            messageCheckerStart();

        });


        cancelButton.addActionListener(e -> dialog.dispose());

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(confirmButton);
        buttonsPanel.add(cancelButton);

        dialog.add(doctorCodeField, BorderLayout.NORTH);
        dialog.add(comboBox, BorderLayout.CENTER);
        dialog.add(buttonsPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    /**
     * Returns the full name of a doctor(name + surname1 + surname2)
     * @param doctorId
     */

    public String getDoctorName(String doctorId) {
        PostgrestQuery query = postgrestClient
                .from("person")
                .select("name", "surname1", "surname2")
                .eq("id", doctorId)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String surname1 = jsonObject.get("surname1").getAsString();
        String surname2 = jsonObject.get("surname2").getAsString();

        return (name + " " + surname1 + " " + surname2);
    }

    /**
     * Returns the full name of a patient(name + surname1 + surname2)
     * @param patientId
     */

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

    /**
     * Stops the thread to update chat messages
     */

    public void messageThreadInterrupt(){
        if(msgThread != null && msgThread.isAlive()){
            msgThread.interrupt();
        }
    }

    /**
     * Starts the thread to update chat messages
     */

    public void messageCheckerStart(){

        messageThreadInterrupt();
        msgThread = new Thread(() -> {

            String patientName = getPatientName(patientId);
            String doctorName = getDoctorName(docCode[0]);

            while(!Thread.interrupted()){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    logger.severe("Error al interrumpir el hilo: " + e.getMessage() + "por el paciente: " + patientId);
                }

                PostgrestQuery query = postgrestClient
                        .from("message")
                        .select("*")
                        .eq("fk_patient_id", patientId)
                        .eq("fk_doctor_id", docCode[0])
                        .eq("patient_read", String.valueOf(false))
                        .getQuery();

                JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

                //Update patient_read to true (query 2)
                PostgrestQuery query2 = postgrestClient
                        .from("message")
                        .update(new Entry<>("patient_read", true))
                        .eq("fk_patient_id", patientId)
                        .eq("fk_doctor_id", docCode[0])
                        .getQuery();

                postgrestClient.sendQuery(query2);

                //Set the previously unread messages at chatArea
                for (JsonElement jsonElement : jsonArray) {
                    JsonObject messageObject = jsonElement.getAsJsonObject();

                    String message = messageObject.get("message").getAsString();
                    String datetime = messageObject.get("date").getAsString();
                    boolean patientSent = messageObject.get("patient_sent").getAsBoolean();
                    LocalDateTime date = LocalDateTime.parse(datetime);
                    String dateFormatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                    SimpleAttributeSet bold = new SimpleAttributeSet();
                    StyleConstants.setBold(bold, true);

                    try {
                        if (patientSent) {
                            chatDoc.insertString(chatDoc.getLength(), patientName + " " + dateFormatted + ":\n", bold);
                        } else {
                            chatDoc.insertString(chatDoc.getLength(), doctorName + " " + dateFormatted + ":\n", bold);
                        }
                        chatDoc.insertString(chatDoc.getLength(), message + "\n\n", null);
                    } catch (BadLocationException ex) {
                        logger.severe("Error al mostrar el chat: " + ex.getMessage() + "por el paciente: " + patientId);
                    }
                }

            }

        });
        msgThread.start();
    }

    /**
     * Saves the chat to a file.
     * @param filePath the path of the file to save the chat to.
     */

    public void saveChatToFile(String filePath) {
        try {
            String content = chatDoc.getText(0, chatDoc.getLength());
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(content);
            writer.close();
            JOptionPane.showMessageDialog(this, "Chat guardado en " + filePath);
            logger.info("Chat guardado en " + filePath + " por el paciente " + patientId + ".");
        } catch (BadLocationException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error al guardar el chat: " + ex.getMessage() + "por el paciente: " + patientId);
        }
    }

    /**
     * Adds a new highlighted date to the JCalendar
     * @param date date to highlight
     */
    public void addHighlightedDate(Date date) {
        highlighter.addDate(date);
        calendar.setCalendar(calendar.getCalendar()); // Actualizar el calendario
    }

    /**
     * Removes all the highlighed dates from JCalendar
     */
    public void clearHighlightedDates() {
        highlighter.clearDates();
        calendar.setCalendar(calendar.getCalendar()); // Actualizar el calendario
    }

    /**
     * Creates a date (used for JCalendar highlighting)
     * @param day
     * @param month
     * @param year
     */
    private static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    //MAIN(JUST TEST)------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();

        SwingUtilities.invokeLater(() -> {
            ConfigLoader configLoader = new ConfigLoader();
            new WindowPatient("00AAK", new AnonymousAuthenticationService(configLoader.getAnonymousToken()));
        });
    }
}