package org.deustomed.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.deustomed.ConfigLoader;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.chat.MessageCheckerThread;
import org.deustomed.chat.chatUser;
import org.deustomed.logs.LogerMaker;
import org.deustomed.postgrest.Entry;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import static org.deustomed.postgrest.PostgrestClient.gson;



public class DoctorChat extends JFrame implements MessageCheckerThread {

    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton, saveChatButton;
    private JList<chatUser> conversationsList;
    private DefaultListModel<chatUser> conversationsModel;
    private StyledDocument chatDocument;
    private String docCodeF = "";
    private String patientId = "";

    private static PostgrestClient postgrestClient;
    protected Thread msgThread;
    private Logger logger;

    public DoctorChat(String docCode) {

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        String anonymousToken = configLoader.getAnonymousToken();
        postgrestClient = new PostgrestClient(hostname, endpoint, new AnonymousAuthenticationService(anonymousToken));

        LogerMaker.setLOG_FILE_PATH("src/main/java/org/deustomed/logs/DoctorChat.log");
        logger = LogerMaker.getLogger();
        logger.info("DoctorChat iniciado por el doctor " + docCode + ".");

        setTitle("DoctorChat");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        docCodeF = docCode;

        JPanel conversationsPanel = new JPanel();
        conversationsPanel.setLayout(new BoxLayout(conversationsPanel, BoxLayout.Y_AXIS));
        conversationsModel = new DefaultListModel<>();
        conversationsList = new JList<>(conversationsModel);
        conversationsList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chatUser selectedUser = conversationsList.getSelectedValue();
                patientId = selectedUser.getId();
                loadConversation(patientId);
                messageCheckerStart(patientId);
            }
        });
        JScrollPane scrollPaneConversations = new JScrollPane(conversationsList);
        conversationsPanel.add(scrollPaneConversations);
        conversationsPanel.setBackground(Color.DARK_GRAY);

        JPanel chatPanel = new JPanel(new BorderLayout());
        chatArea = new JTextPane();
        chatArea.setEditable(false);
        chatArea.setBackground(Color.lightGray);
        chatDocument = chatArea.getStyledDocument();
        JScrollPane scrollPaneChat = new JScrollPane(chatArea);
        chatPanel.add(scrollPaneChat, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel chatButtonsPanel = new JPanel(new FlowLayout());

        messageField = new JTextField();
        sendButton = new JButton("Enviar");
        saveChatButton = new JButton("Descargar");

        chatButtonsPanel.add(sendButton);
        chatButtonsPanel.add(saveChatButton);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(chatButtonsPanel, BorderLayout.EAST);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(conversationsPanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);

        //Load previous conversations
        PostgrestQuery query = postgrestClient
                .from("message")
                .select("fk_patient_id")
                .eq("fk_doctor_id", docCodeF)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        HashSet<String> uniquePatientIds = new HashSet<>();

        for (JsonElement jsonElement : jsonArray) {
            JsonObject messageObject = jsonElement.getAsJsonObject();
            String patientId = messageObject.get("fk_patient_id").getAsString();

            // Añadir al modelo solo si el ID del paciente es único
            if (uniquePatientIds.add(patientId)) {
                PostgrestQuery query1 = postgrestClient
                        .from("person")
                        .select("id","name","surname1","surname2")
                        .eq("id", patientId)
                        .getQuery();

                String jsonResponse1 = String.valueOf(postgrestClient.sendQuery(query1));
                JsonArray jsonArray1 = gson.fromJson(jsonResponse1, JsonArray.class);
                JsonObject jsonObject = jsonArray1.get(0).getAsJsonObject();

                String name = jsonObject.get("name").getAsString();
                String surname1 = jsonObject.get("surname1").getAsString();
                String surname2 = jsonObject.get("surname2").getAsString();
                String id = jsonObject.get("id").getAsString();

                chatUser user = new chatUser(name, surname1, surname2, id);
                conversationsModel.addElement(user);
            }
        }

        saveChatButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar chat como...");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();
                saveChatToFile(fileToSave.getAbsolutePath());
            }
        });

        sendButton.addActionListener(e -> {
            try {
                sendMessage();
            } catch (BadLocationException ex) {
                logger.severe("Error al enviar el mensaje: " + ex.getMessage() + " por el doctor " + docCodeF + ".");
            }
        });

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    try {
                        sendMessage();
                    } catch (BadLocationException ex) {
                        logger.severe("Error al enviar el mensaje: " + ex.getMessage() + " por el doctor " + docCodeF + ".");
                    }
                }
            }
        });
    }

    private void sendMessage() throws BadLocationException {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("fk_patient_id", patientId);
            jsonObject.addProperty("fk_doctor_id", docCodeF);
            jsonObject.addProperty("message", message);
            jsonObject.addProperty("patient_sent", false);
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

        }
    }
    
    /**
     * Saves the chat to a file.
     * @param filePath the path of the file to save the chat to.
     */

    public void saveChatToFile(String filePath) {
        try {
            String content = chatDocument.getText(0, chatDocument.getLength());
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            writer.write(content);
            writer.close();
            JOptionPane.showMessageDialog(this, "Chat guardado en " + filePath);
            logger.info("Chat guardado en " + filePath + " por el doctor " + docCodeF + ".");
        } catch (BadLocationException | IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error al guardar el archivo: " + ex.getMessage() + " por el doctor " + docCodeF + ".");
        }
    }


    @Override
    public void messageCheckerStart() {

    }

    public void messageThreadInterrupt(){
        if(msgThread != null && msgThread.isAlive()){
            msgThread.interrupt();
        }
    }

    public void messageCheckerStart(String patientId){

        messageThreadInterrupt();
        msgThread = new Thread(new Runnable() {
            @Override
            public void run() {

                String patientName = getPatientName(patientId);
                String doctorName = getDoctorName(docCodeF);

                while(!Thread.interrupted()){
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        logger.severe("Hilo de comprobación de mensajes interrumpido para el doctor " + docCodeF);
                    }

                    PostgrestQuery query = postgrestClient
                            .from("message")
                            .select("*")
                            .eq("fk_patient_id", patientId)
                            .eq("fk_doctor_id", docCodeF)
                            .is("doctor_read", false)
                            .getQuery();

                    String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));

                    //Update doctor_read to true (query 2)
                    PostgrestQuery query2 = postgrestClient
                            .from("message")
                            .update(new Entry("doctor_read", true))
                            .eq("fk_patient_id", patientId)
                            .eq("fk_doctor_id", docCodeF)
                            .getQuery();

                    postgrestClient.sendQuery(query2);

                    //Set the previously unread messages at chatArea
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
                                chatDocument.insertString(chatDocument.getLength(), patientName + " " + dateFormatted + ":\n", bold);
                            } else {
                                chatDocument.insertString(chatDocument.getLength(), doctorName + " " + dateFormatted + ":\n", bold);
                            }
                            chatDocument.insertString(chatDocument.getLength(), message + "\n\n", null);
                        } catch (BadLocationException ex) {
                            logger.severe("Error al insertar el mensaje en el chat: " + ex.getMessage() + " por el doctor " + docCodeF + ".");
                        }
                    }

                }

            }
        });
        msgThread.start();
    }



    public String getDoctorName(String doctorId) {
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

        String fullName = (name + " " + surname1 + " " + surname2);
        return fullName;
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

        String fullName = (name + " " + surname1 + " " + surname2);
        return fullName;
    }

    private void loadConversation(String patientId) {

        try {
            chatDocument.remove(0,chatDocument.getLength());
        } catch (BadLocationException ex) {
            logger.severe("Error al borrar el chat: " + ex.getMessage() + " por el doctor " + docCodeF + ".");
        }

        //get doctor and patient full names
        String docFullName = getDoctorName(docCodeF);
        String patFullName = getPatientName(patientId);

        //Retrieve previous messages
        PostgrestQuery query = postgrestClient
                .from("message")
                .select("*")
                .eq("fk_patient_id",patientId)
                .eq("fk_doctor_id",docCodeF)
                .order("date",true)
                .getQuery();

        PostgrestQuery updatequery = postgrestClient
                .from("message")
                .update(new Entry("doctor_read",true))
                .eq("fk_patient_id",patientId)
                .eq("fk_doctor_id",docCodeF)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        String jsonResponseUpdate = String.valueOf(postgrestClient.sendQuery(updatequery));
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for(JsonElement jsonElement : jsonArray){
            JsonObject messageObject = jsonElement.getAsJsonObject();

            String message = messageObject.get("message").getAsString();
            String datetime = messageObject.get("date").getAsString();
            Boolean patientSent = messageObject.get("patient_sent").getAsBoolean();
            LocalDateTime date = LocalDateTime.parse(datetime);
            String dateFormatted = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            SimpleAttributeSet bold = new SimpleAttributeSet();
            StyleConstants.setBold(bold, true);

            try {
                if(patientSent){
                    chatDocument.insertString(chatDocument.getLength(), patFullName + " " + dateFormatted + ":\n", bold);
                }else{
                    chatDocument.insertString(chatDocument.getLength(), docFullName + " " + dateFormatted + ":\n", bold);
                }
                chatDocument.insertString(chatDocument.getLength(), message + "\n\n", null);
            } catch (BadLocationException ex) {
                logger.severe("Error al insertar el mensaje en el chat: " + ex.getMessage() + " por el doctor " + docCodeF + ".");
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorChat("00AAA").setVisible(true));
    }
}
