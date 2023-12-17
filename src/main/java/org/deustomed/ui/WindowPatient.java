package org.deustomed.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.toedter.calendar.JCalendar;
import org.deustomed.DoctorMsgCode;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.chat.Server;
import org.deustomed.postgrest.Entry;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.deustomed.postgrest.PostgrestClient.gson;

public class WindowPatient extends JFrame {
    protected String selectedButton = ""; //info, calendar, medicines, chat
    protected String prevDirect, prevTfn, prevEmail;

    protected JButton infoButton, calendarButton, medicinesButton, chatButton, logoutButton, pedirCitaButton, guardarCambiosButton;
    protected JPanel menuPanel, infoPanel, infoPanel2, calendarPanel, medicinesPanel, chatPanel;

    protected JCalendar calendar;
    protected JTable calendarTable;
    protected DefaultTableModel calendarTableModel;

    protected JLabel lblNombre, lblApellido1, lblApellido2, lblEmail, lblDNI, lblTelefono, lblFechaNacimiento, lblDireccion;
    protected JTextField txtNombre, txtApellido1, txtApellido2, txtEmail, txtDNI, txtTelefono, txtFechaNacimiento, txtDireccion;

    protected JTextPane chatArea = new JTextPane();
    protected StyledDocument chatDoc = chatArea.getStyledDocument();
    protected JTextField messageField;
    protected JButton sendButton;
    protected JButton leaveChatButton;
    protected String lastMessage = "";
    protected String patientId;
    final String[] docCode = {""};

    static final String HOSTNAME = "hppqxyzzghzomojqpddp.supabase.co";
    static final String ENDPOINT = "/rest/v1";
    static final PostgrestClient postgrestClient = new PostgrestClient(HOSTNAME, ENDPOINT,
            new AnonymousAuthenticationService("""
                    eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImhwcHF4eXp6Z2h6b21vanFwZGRwIiwicm9s\
                    ZSI6ImFub24iLCJpYXQiOjE2OTg2NzE5MjksImV4cCI6MjAxNDI0NzkyOX0.m5uDlUdMaDBXBSoDzRx0BScQfF3AweNGopruakwxais"""));

    public WindowPatient(String patientId) {
        this.patientId = patientId;

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
        ImageIcon medicinesIcon = new ImageIcon("src/main/java/ui/medicina.png");
        ImageIcon chatIcon = new ImageIcon("src/main/java/ui/mensaje.png");
        ImageIcon logoutIcon = new ImageIcon("src/main/java/ui/logout.png");

        infoButton = new JButton(infoIcon);
        infoButton.setBackground(colorizq);
        infoButton.setBorder(null);
        calendarButton = new JButton(calendarIcon);
        calendarButton.setBackground(colorizq);
        calendarButton.setBorder(null);
        medicinesButton = new JButton(medicinesIcon);
        medicinesButton.setBackground(colorizq);
        medicinesButton.setBorder(null);
        chatButton = new JButton(chatIcon);
        chatButton.setBackground(colorizq);
        chatButton.setBorder(null);
        logoutButton = new JButton(logoutIcon);
        logoutButton.setBackground(colorizq);
        logoutButton.setBorder(null);

        infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicinesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(infoButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(calendarButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(medicinesButton);
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
        String[] calendarColumns = {"Fecha", "Hora", "Motivo cita", "Médico", "Codigo Médico"};
        calendarTableModel.setColumnIdentifiers(calendarColumns);
        updateCalendarTable();
        calendarTable = new JTable(calendarTableModel);
        JScrollPane scrollPane = new JScrollPane(calendarTable);
        calendarPanel.add(scrollPane, BorderLayout.CENTER);
        calendarTable.setDefaultEditor(Object.class, null);

        pedirCitaButton = new JButton("Pedir Cita");
        calendarPanel.add(pedirCitaButton, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (selectedButton.equals("calendar")) {
                    deleteContent();
                    getContentPane().add(calendarPanel, BorderLayout.CENTER);
                } else if (selectedButton.equals("info")) {
                    deleteContent();
                    getContentPane().add(infoPanel, BorderLayout.CENTER);
                } else if (selectedButton.equals("chat")) {
                    deleteContent();
                    getContentPane().add(chatPanel, BorderLayout.CENTER);
                }
            }
        });

        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContent();
                getContentPane().add(calendarPanel, BorderLayout.CENTER);
                selectedButton = "calendar";
                revalidate();
            }
        });

        pedirCitaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowAppointment windowAppointment = new WindowAppointment(calendar.getDate());
            }
        });


        //LOGOUT------------------------------------------------------------------------------------------------------------

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Object[] options = {"Sí", "No"};
                ImageIcon atentionIcon = new ImageIcon("src/main/java/ui/atencion.png");
                int option = JOptionPane.showOptionDialog(null, "¿Estás seguro de que quieres cerrar sesión?",
                        "Confirmar Cierre de Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, atentionIcon, options,
                        options[0]);

                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                    new WindowLogin();
                }

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
        leaveChatButton = new JButton("Salir");
        chatButtonsPanel.add(sendButton);
        chatButtonsPanel.add(leaveChatButton);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(chatButtonsPanel, BorderLayout.EAST);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        Server server = new Server();
        //server.initServer(this);

        chatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContent();
                getContentPane().add(chatPanel, BorderLayout.CENTER);
                selectedButton = "chat";
                revalidate();
                showDoctorCodeDialog();
            }
        });

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    sendMessage();
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        sendMessage();
                    } catch (BadLocationException ex) {
                        throw new RuntimeException(ex);
                    }
                }
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

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContent();
                getContentPane().add(infoPanel, BorderLayout.CENTER);
                selectedButton = "info";
                revalidate();
            }
        });

        guardarCambiosButton.setEnabled(false);

        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!prevEmail.equals(txtEmail.getText())) {
                    guardarCambiosButton.setEnabled(true);
                } else {
                    guardarCambiosButton.setEnabled(false);
                }
            }
        });

        txtTelefono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!prevTfn.equals(txtTelefono.getText())) {
                    guardarCambiosButton.setEnabled(true);
                } else {
                    guardarCambiosButton.setEnabled(false);
                }
            }
        });

        txtDireccion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!prevDirect.equals(txtDireccion.getText())) {
                    guardarCambiosButton.setEnabled(true);
                } else {
                    guardarCambiosButton.setEnabled(false);
                }
            }
        });

        guardarCambiosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                prevDirect = txtDireccion.getText();
                prevEmail = txtEmail.getText();
                prevTfn = txtTelefono.getText();

                PostgrestQuery query = postgrestClient.from("person")
                        .update(new Entry("email", prevEmail),
                                new Entry("phone", prevTfn),
                                new Entry("address", prevDirect))
                        .eq("id", patientId)
                        .select()
                        .getQuery();
                postgrestClient.sendQuery(query);
                guardarCambiosButton.setEnabled(false);
            }
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

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String name = jsonObject.get("name").getAsString();
        String surname1 = jsonObject.get("surname1").getAsString();
        String surname2 = jsonObject.get("surname2").getAsString();
        String dni = jsonObject.get("dni").getAsString();
        String birthdate = jsonObject.get("birthdate").getAsString();
        String email = jsonObject.get("email").getAsString();
        String phone = jsonObject.get("phone").getAsString();
        String address = jsonObject.get("address").getAsString();

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
        PostgrestQuery query = postgrestClient
                .from("appointment")
                .select("*")
                .eq("fk_patient_id", patientId)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (JsonElement jsonElement : jsonArray) {
            JsonObject appointmentObject = jsonElement.getAsJsonObject();

            String datetime = appointmentObject.get("datetime").getAsString();
            String reason = appointmentObject.get("reason").getAsString();
            String fkDoctorId = appointmentObject.get("fk_doctor_id").getAsString();
            String hour = appointmentObject.get("hour").getAsString();

            LocalDate date = LocalDate.parse(datetime);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String formatted = date.format(formatter);

            PostgrestQuery query1 = postgrestClient
                    .from("person")
                    .select("name", "surname1", "surname2")
                    .eq("id", fkDoctorId)
                    .getQuery();

            String jsonResponse1 = String.valueOf(postgrestClient.sendQuery(query1));
            JsonArray jsonArray1 = gson.fromJson(jsonResponse1, JsonArray.class);
            JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();

            String name = jsonObject.get("name").getAsString();
            String surname1 = jsonObject.get("surname1").getAsString();
            String surname2 = jsonObject.get("surname2").getAsString();
            String docname = String.format("%s %s %s", name, surname1, surname2);

            Object[] rowData = {formatted, hour, reason, docname, fkDoctorId};
            calendarTableModel.addRow(rowData);

        }
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

    public String getLastMessage() {
        return lastMessage;
    }

    public JCalendar getCalendar() {
        return calendar;
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
        String[] opciones = {"Opción 1", "Opción 2", "Opción 3"};
        JComboBox<String> comboBox = new JComboBox<>(opciones);

        JButton confirmButton = new JButton("Confirmar");
        JButton cancelButton = new JButton("Cancelar");

        DoctorMsgCode codeTransformator = new DoctorMsgCode();


        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try {
                    chatDoc.remove(0, chatDoc.getLength());
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }

                if (!doctorCodeField.getText().equals("")) {
                    docCode[0] = codeTransformator.MsgCodeToId(doctorCodeField.getText());
                }

                //get doctor and patient full names
                PostgrestQuery queryDocName = postgrestClient
                        .from("person")
                        .select("name", "surname1", "surname2")
                        .eq("id", docCode[0])
                        .getQuery();

                String jsonResponse1 = String.valueOf(postgrestClient.sendQuery(queryDocName));
                JsonArray jsonArray1 = gson.fromJson(jsonResponse1, JsonArray.class);
                JsonObject jsonObject1 = jsonArray1.get(0).getAsJsonObject();
                String docName = jsonObject1.get("name").getAsString();
                String docSurname1 = jsonObject1.get("surname1").getAsString();
                String docSurname2 = jsonObject1.get("surname2").getAsString();

                PostgrestQuery query2 = postgrestClient
                        .from("person")
                        .select("*")
                        .eq("id", patientId)
                        .getQuery();

                String jsonResponse2 = String.valueOf(postgrestClient.sendQuery(query2));
                Gson gson = new Gson();
                JsonArray jsonArray2 = gson.fromJson(jsonResponse2, JsonArray.class);
                JsonObject jsonObject2 = jsonArray2.get(0).getAsJsonObject();

                String patName = jsonObject2.get("name").getAsString();
                String patSurname1 = jsonObject2.get("surname1").getAsString();
                String patSurname2 = jsonObject2.get("surname2").getAsString();

                String docFullName = (docName + " " + docSurname1 + " " + docSurname2);
                String patFullName = (patName + " " + patSurname1 + " " + patSurname2);

                //Retrieve previous messages
                PostgrestQuery query = postgrestClient
                        .from("message")
                        .select("*")
                        .eq("fk_patient_id", patientId)
                        .eq("fk_doctor_id", docCode[0])
                        .order("date", true)
                        .getQuery();

                PostgrestQuery updatequery = postgrestClient
                        .from("message")
                        .update(new Entry("patient_read", true))
                        .eq("fk_patient_id", patientId)
                        .eq("fk_doctor_id", docCode[0])
                        .getQuery();

                String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
                String jsonResponseUpdate = String.valueOf(postgrestClient.sendQuery(updatequery));
                JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

                for (JsonElement jsonElement : jsonArray) {
                    JsonObject messageObject = jsonElement.getAsJsonObject();

                    String message = messageObject.get("message").getAsString();
                    String datetime = messageObject.get("date").getAsString();
                    Boolean patientSent = messageObject.get("patient_sent").getAsBoolean();
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
                    }
                }

                dialog.dispose();

            }
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


    //MAIN(JUST TEST)------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WindowPatient("00AAK");
        });
    }
}