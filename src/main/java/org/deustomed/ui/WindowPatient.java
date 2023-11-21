package org.deustomed.ui;

import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class WindowPatient extends JFrame {
    protected String selectedButton = ""; //info, calendar, medicines, chat

    protected JButton infoButton, calendarButton, medicinesButton, chatButton, logoutButton, pedirCitaButton;
    protected JPanel menuPanel, infoPanel, calendarPanel, medicinesPanel, chatPanel;

    protected JCalendar calendar;
    protected JTable calendarTable;
    protected DefaultTableModel calendarTableModel;

    protected JLabel lblNombre, lblApellido1, lblApellido2, lblEmail, lblDNI, lblTelefono, lblFechaNacimiento, lblDireccion;
    protected  JTextField txtNombre, txtApellido1, txtApellido2, txtEmail, txtDNI, txtTelefono, txtFechaNacimiento, txtDireccion;

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
                    getContentPane().add(infoPanel, BorderLayout.CENTER);}
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
                //TODO: Crear y abrir una ventana de cita
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

        //INFO MENU PANEL-----------------------------------------------------------------------------------------------------

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));

        lblNombre = new JLabel("Nombre:");
        txtNombre = new JTextField();
        lblApellido1 = new JLabel("Primer Apellido:");
        txtApellido1 = new JTextField();
        lblApellido2 = new JLabel("Segundo Apellido:");
        txtApellido2 = new JTextField();
        lblEmail = new JLabel("Email:");
        txtEmail = new JTextField();
        lblDNI = new JLabel("DNI:");
        txtDNI = new JTextField();
        lblTelefono = new JLabel("Teléfono:");
        txtTelefono = new JTextField();
        lblFechaNacimiento = new JLabel("Fecha de Nacimiento:");
        txtFechaNacimiento = new JTextField();
        lblDireccion = new JLabel("Dirección:");
        txtDireccion = new JTextField();

        infoPanel.add(lblNombre);
        infoPanel.add(txtNombre);
        infoPanel.add(lblApellido1);
        infoPanel.add(txtApellido1);
        infoPanel.add(lblApellido2);
        infoPanel.add(txtApellido2);
        infoPanel.add(lblEmail);
        infoPanel.add(txtEmail);
        infoPanel.add(lblDNI);
        infoPanel.add(txtDNI);
        infoPanel.add(lblTelefono);
        infoPanel.add(txtTelefono);
        infoPanel.add(lblFechaNacimiento);
        infoPanel.add(txtFechaNacimiento);
        infoPanel.add(lblDireccion);
        infoPanel.add(txtDireccion);

        infoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContent();
                getContentPane().add(infoPanel, BorderLayout.CENTER);
                selectedButton = "info";
                revalidate();
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



    //MAIN(JUST TEST)------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WindowPatient();
        });
    }
}
