package org.deustomed.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.deustomed.ConfigLoader;
import org.deustomed.authentication.AnonymousAuthenticationService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.deustomed.postgrest.PostgrestClient.gson;



public class DoctorChat extends JFrame {

    private JTextPane chatArea;
    private JTextField messageField;
    private JButton sendButton, leaveChatButton;
    private JList<String> conversationsList;
    private DefaultListModel<String> conversationsModel;
    private StyledDocument chatDocument;
    private String docCodeF = "";

    private static PostgrestClient postgrestClient;

    public DoctorChat(String docCode) {

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        String anonymousToken = configLoader.getAnonymousToken();
        postgrestClient = new PostgrestClient(hostname, endpoint, new AnonymousAuthenticationService(anonymousToken));

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
                loadConversation(conversationsList.getSelectedValue());
            }
        });
        JScrollPane scrollPaneConversations = new JScrollPane(conversationsList);
        conversationsPanel.add(scrollPaneConversations);

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
        leaveChatButton = new JButton("Salir");

        chatButtonsPanel.add(sendButton);
        chatButtonsPanel.add(leaveChatButton);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(chatButtonsPanel, BorderLayout.EAST);
        chatPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(conversationsPanel, BorderLayout.WEST);
        add(chatPanel, BorderLayout.CENTER);

        conversationsModel.addElement("Alejandro Hernandez");
        conversationsModel.addElement("Marta García");

        sendButton.addActionListener(e -> {
            try {
                sendMessage();
            } catch (BadLocationException ex) {
                throw new RuntimeException(ex);
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
    }

    private void sendMessage() throws BadLocationException {
        String message = messageField.getText();
        chatDocument.insertString(chatDocument.getLength(), message + "\n", null);
        messageField.setText("");
    }

    private void loadConversation(String patientId) {
        try {

            //EXAMPLE WITHOUT DB (DELETE)
            chatDocument.remove(0, chatDocument.getLength());

            if ("Alejandro Hernandez".equals(patientId)) {
                chatDocument.insertString(chatDocument.getLength(), "Usuario 1 (Paciente): Hola, he estado sintiendo dolor en mi muñeca desde ayer. ¿Podría ser algo serio?\n", null);
                chatDocument.insertString(chatDocument.getLength(), "Usuario 2 (Doctor): Hola, ¿puedes describir el dolor? ¿Hubo alguna lesión o movimiento inusual que lo haya provocado?\n", null);

            } else if ("Marta García".equals(patientId)) {
                chatDocument.insertString(chatDocument.getLength(), "Usuario 1 (Paciente): Buenos días, doctor. Solo quería informarle que he estado tomando la medicación como me indicó y me siento mucho mejor.\n", null);
                chatDocument.insertString(chatDocument.getLength(), "Usuario 2 (Doctor): Buenos días, me alegra escuchar eso. ¿Has experimentado algún efecto secundario?\n", null);

            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

        /*
        try {
            chatDocument.remove(0,chatDocument.getLength());
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
        */

        //get doctor and patient full names
        PostgrestQuery queryDocName = postgrestClient
                .from("person")
                .select("name","surname1","surname2")
                .eq("id",docCodeF)
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

        String docFullName = (docName+" "+docSurname1+" "+docSurname2);
        String patFullName = (patName+" "+patSurname1+" "+patSurname2);

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
                .update(new Entry("patient_read",true))
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
            }
        }

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DoctorChat("").setVisible(true));
    }
}
