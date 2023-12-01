package org.deustomed.ui;

import com.toedter.calendar.JCalendar;
import org.deustomed.chat.Client;
import org.deustomed.chat.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class WindowPatient extends JFrame {
    protected String selectedButton = ""; //info, calendar, medicines, chat
    protected String prevDirect, prevTfn, prevEmail;

    protected JButton infoButton, calendarButton, medicinesButton, chatButton, logoutButton, pedirCitaButton, guardarCambiosButton;
    protected JPanel menuPanel, infoPanel, infoPanel2, calendarPanel, medicinesPanel, chatPanel;

    protected JCalendar calendar;
    protected JTable calendarTable;
    protected DefaultTableModel calendarTableModel;

    protected JLabel lblNombre, lblApellido1, lblApellido2, lblEmail, lblDNI, lblTelefono, lblFechaNacimiento, lblDireccion;
    protected  JTextField txtNombre, txtApellido1, txtApellido2, txtEmail, txtDNI, txtTelefono, txtFechaNacimiento, txtDireccion;

    protected JTextArea chatArea;
    protected JTextField messageField;
    protected JButton sendButton;
    protected JButton leaveChatButton;
    protected String lastMessage = "";

    public WindowPatient() {
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
        String[] calendarColumns = {"Día","Hora","Motivo cita","Médico","Codigo Médico"};
        calendarTableModel.setColumnIdentifiers(calendarColumns);
        Object[] rowData = {"Lunes", "10:00 AM", "Revisión médica", "Dr. Smith", "12345"};
        calendarTableModel.addRow(rowData);
        calendarTable = new JTable(calendarTableModel);
        JScrollPane scrollPane = new JScrollPane(calendarTable);
        calendarPanel.add(scrollPane, BorderLayout.CENTER);

        pedirCitaButton = new JButton("Pedir Cita");
        calendarPanel.add(pedirCitaButton, BorderLayout.SOUTH);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(selectedButton.equals("calendar")){
                    deleteContent();
                    getContentPane().add(calendarPanel, BorderLayout.CENTER);
                }else if(selectedButton.equals("info")){
                    deleteContent();
                    getContentPane().add(infoPanel, BorderLayout.CENTER);
                } else if (selectedButton.equals("chat")) {
                    deleteContent();
                    getContentPane().add(chatPanel,BorderLayout.CENTER);
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
                        "Confirmar Cierre de Sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, atentionIcon, options, options[0]);

                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                    new WindowLogin();
                }
                //TODO: CERRAR SESION EN LA BD
            }
        });

        //CHAT PANEL---------------------------------------------------------------------------------------------------------
        chatPanel = new JPanel();
        chatPanel.setLayout(new BorderLayout());
        JPanel bottomPanel = new JPanel(new BorderLayout());
        JPanel chatButtonsPanel = new JPanel(new FlowLayout());

        chatArea = new JTextArea();
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
            }
        });
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        messageField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    sendMessage();
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
                if(!prevEmail.equals(txtEmail.getText())){
                    guardarCambiosButton.setEnabled(true);
                }else{
                    guardarCambiosButton.setEnabled(false);
                }
            }
        });

        txtTelefono.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(!prevTfn.equals(txtTelefono.getText())){
                    guardarCambiosButton.setEnabled(true);
                }else{
                    guardarCambiosButton.setEnabled(false);
                }
            }
        });

        txtDireccion.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(!prevDirect.equals(txtDireccion.getText())){
                    guardarCambiosButton.setEnabled(true);
                }else{
                    guardarCambiosButton.setEnabled(false);
                }
            }
        });

        guardarCambiosButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //TODO: ACTUALIZAR EMAIL,DIRECCION Y TELEFONO EN LA BD
                prevDirect = txtDireccion.getText();
                prevEmail = txtEmail.getText();
                prevTfn = txtTelefono.getText();
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
    public void deleteContent(){
        getContentPane().removeAll();
        getContentPane().add(menuPanel, BorderLayout.WEST);
        revalidate();
        repaint();
    }

    /**
     * Set the infoPanel textfields to DB values
     */
    public void setInfoPanelTexfields(){
        txtNombre.setText("");
        txtApellido1.setText("");
        txtApellido2.setText("");
        txtDNI.setText("");
        txtFechaNacimiento.setText("");
        txtDireccion.setText("");
        txtEmail.setText("");

        prevEmail = txtEmail.getText();
        prevDirect = txtDireccion.getText();
        prevTfn = txtTelefono.getText();
    }

    private void sendMessage() {
        String message = messageField.getText().trim();
        if (!message.isEmpty()) {
            chatArea.append("Paciente: " + message + "\n");
            messageField.setText("");
            lastMessage = message;
        }
    }
    public void recieveMessage(String message){
        chatArea.append("Doctor: " + message + "\n");
    }
    public String getLastMessage(){
        return lastMessage;
    }

    public JCalendar getCalendar() {
        return calendar;
    }

    //MAIN(JUST TEST)------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WindowPatient();
        });
    }
}