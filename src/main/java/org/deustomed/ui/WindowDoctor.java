package org.deustomed.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JDayChooser;
import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import org.deustomed.*;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.logs.LoggerMaker;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.PostgrestClient;
//import org.jetbrains.annotations.Nls;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.EventObject;
import java.util.logging.Logger;

public class WindowDoctor extends JFrame {
    private static Doctor doctor;
    private JPanel pnlInfo;
    private JPanel pnlCentral;
    private JPanel pnlAppointments;
    private static Dimension screenSize;
    private JPanel pnlDisplayAppoinments;
    private JTable tableOwnPatient;
    private JTable tableTreatedPatient;
    private JTable tableInTreatmentPatient;
    private JTable tableToTreatPatient;
    private JTable tableMedication;
    private JTabbedPane tabbedPaneCenter;
    private ArrayList<Patient> ownPatients;
    private Date selectedDate = new Date();
    private static PostgrestClient postgrestClient;
    private Logger logger;
    public WindowDoctor(Doctor doctor){
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth()/4, (int) screenSize.getHeight()/4, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        setLayout(new BorderLayout());
        this.doctor=doctor;

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        String anonymousToken = configLoader.getAnonymousToken();
        postgrestClient = new PostgrestClient(hostname, endpoint, new AnonymousAuthenticationService(anonymousToken));

        LoggerMaker.setlogFilePath("src/main/java/org/deustomed/logs/WindowPatient.log");
        logger = LoggerMaker.getLogger();


        // ------------------ pnlInfo ------------------
        pnlInfo = new JPanel();
        pnlInfo.setPreferredSize(new Dimension(300,300));
        pnlInfo.setLayout(new BorderLayout());
        JButton btnPhoto = new JButton("");
        ImageIcon icon = new ImageIcon("src/main/java/ui/profileImg.png");
        Image png = icon.getImage().getScaledInstance(200,200, DO_NOTHING_ON_CLOSE);
        btnPhoto.setIcon(new ImageIcon(png));
        btnPhoto.setEnabled(false);
        btnPhoto.setPreferredSize(new Dimension(250,250));
        JPanel p = new JPanel();
        p.add(btnPhoto);
        pnlInfo.add(p, BorderLayout.NORTH);

        FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
        fl.setHgap(30);
        JPanel pnlc = new JPanel(fl);
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(3,1));

        JButton info = new JButton("Infomación Personal");
        info.setPreferredSize(new Dimension(200,30));
        info.setHorizontalAlignment(SwingConstants.CENTER);

        JButton info2 = new JButton("Visualizaciñon Calendario");
        info2.setPreferredSize(new Dimension(200,30));

        JButton info3 = new JButton("Servicio Técnico");
        info3.setPreferredSize(new Dimension(200,30));

        JLabel name = new JLabel("Dr. "+doctor.getName());
        JLabel surname1 = new JLabel(doctor.getSurname1());
        JLabel surname2 = new JLabel(doctor.getSurname2());

        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));
        surname1.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));
        surname2.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));

        pnlc.add(name);
        pnlc.add(surname1);
        pnlc.add(surname2);
        pnlc.add(new JLabel("Doctor especialista en "+doctor.getSpeciality()));
        pnlc.add(Box.createVerticalStrut(200));
        panelButtons.add(info);
        panelButtons.add(info2);
        panelButtons.add(info3);
        pnlc.add(panelButtons);

        pnlInfo.add(pnlc, BorderLayout.CENTER);
        JButton singOut = new JButton("Cerrar Sesión");
        pnlInfo.add(singOut, BorderLayout.SOUTH);
        add(pnlInfo, BorderLayout.WEST);


        //--------------------- Panel CENTER: TabbedPane: Pacientes, Medicamentos, Char Médico-Paciente... --------------------------------

        JScrollPane spTablePatient = new JScrollPane(tableOwnPatient);
        tableMedication = new JTable();

        tabbedPaneCenter = new JTabbedPane();
        tabbedPaneCenter.setVisible(true);
        Icon iconPatients = new ImageIcon("src/main/java/ui/patient.png");
        Icon iconMedication = new ImageIcon("src/main/java/ui/medication.png");

        tabbedPaneCenter.addTab("Registro Completo Pacientes", null, spTablePatient);
        tabbedPaneCenter.addTab("Table Medicamentos", null, tableMedication);
        tabbedPaneCenter.addTab("Calendario", null, null);
        tabbedPaneCenter.addTab("Chats en Curso", null, null);
        add(tabbedPaneCenter, BorderLayout.CENTER);

        // Para Doctor de Medicina Familiar:
        if(doctor instanceof FamilyDoctor){
            //System.out.println(((FamilyDoctor) doctor).getOwnPatients());
            ArrayList<Patient> pat = ((FamilyDoctor) doctor).getOwnPatients();
            System.out.println("This are the patients: "+pat);
            ownPatients = pat;
            if(pat != null){
                TableModelPatient tmp1 = new TableModelPatient(pat);
                tableOwnPatient = new JTable(tmp1);
                tableOwnPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
                tableOwnPatient.setDefaultEditor(Patient.class, new TablePatienteEditor(new JTextField()));
                JScrollPane spTableOwnPatient = new JScrollPane(tableOwnPatient);

                tabbedPaneCenter.addTab("Registro Pacientes Propios", iconPatients, spTableOwnPatient);
            }

        } else {
            TableModelPatient tmp2 = new TableModelPatient(((SpecialistDoctor) doctor).getTreatedPatients());
            tableTreatedPatient = new JTable(tmp2);
            tableTreatedPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
            tableTreatedPatient.setDefaultEditor(Patient.class, new TablePatienteEditor(new JTextField()));
            JScrollPane spTableTreatedPatients = new JScrollPane(tableTreatedPatient);

            TableModelPatient tmp3 = new TableModelPatient(((SpecialistDoctor) doctor).getInTreatmentPatients());
            tableInTreatmentPatient = new JTable(tmp3);
            tableInTreatmentPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
            tableInTreatmentPatient.setDefaultEditor(Patient.class, new TablePatienteEditor(new JTextField()));
            JScrollPane spTableInTreatmentPatients = new JScrollPane(tableInTreatmentPatient);

            TableModelPatient tmp4 = new TableModelPatient(((SpecialistDoctor) doctor).getToTreatPatients());
            tableToTreatPatient = new JTable(tmp4);
            tableToTreatPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
            tableToTreatPatient.setDefaultEditor(Patient.class, new TablePatienteEditor(new JTextField()));
            JScrollPane spTableToTreatPatients = new JScrollPane(tableToTreatPatient);

            tabbedPaneCenter.addTab("Registro Pacientes Tratados", iconPatients, spTableTreatedPatients);
            tabbedPaneCenter.addTab("Registro Pacientes Tratamiento", iconPatients, spTableInTreatmentPatients);
            tabbedPaneCenter.addTab("Registro Futuros Pacientes", iconPatients, spTableToTreatPatients);
        }

        //--------------------- Panel EAST: Citas (Appointments) --------------------------------
        pnlAppointments = new JPanel();
        add(pnlAppointments, BorderLayout.EAST);
        pnlAppointments.setLayout(new BorderLayout());
        pnlAppointments.setPreferredSize(new Dimension((int) (screenSize.width/4.1),(int) (screenSize.height*0.75)));
        TitledBorder bordeEast = BorderFactory.createTitledBorder("Citas");
        pnlAppointments.setBorder(bordeEast);
        JMonthChooser monthChooser = new JMonthChooser();
        pnlDisplayAppoinments = new JPanel();
        JYearChooser yearChooser = new JYearChooser();

        pnlAppointments.add(pnlDisplayAppoinments, BorderLayout.CENTER);
        JScrollPane sbarPedidos = new JScrollPane(pnlDisplayAppoinments);
        pnlAppointments.add(sbarPedidos, BorderLayout.CENTER);

        JPanel pnlDateChooser =  new JPanel();
        pnlDateChooser.setLayout(new FlowLayout());
        pnlDateChooser.add(new JLabel("Select date: "));
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                Date newDate = (Date) e.getNewValue();
                System.out.println("Selected date changed to: " + newDate);
                selectedDate = newDate;
                visualiseAppoinments(newDate);
            }
        });
        dateChooser.setPreferredSize(new Dimension(200,25));
        pnlDateChooser.add(dateChooser);
        pnlAppointments.add(pnlDateChooser, BorderLayout.NORTH);


        /*JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2,2));

        JButton visualizarPedido = new JButton("Visualizar Pedido");
        panelBotones.add(visualizarPedido);*/
        visualiseAppoinments(new Date());

        /*anadirPedido = new JButton("Añadir Pedido");
        anadirPedido.setPreferredSize(new Dimension(80,50));
        panelBotones.add(anadirPedido);
        anadirPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    ImageIcon icono= new ImageIcon("src/Proyecto/Imagenes/pedido.png");
                    Image imagen = icono.getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
                    Pedido nuevoPedido = new Pedido(null,null, Integer.parseInt((String) JOptionPane.showInputDialog(null, "Introduce número de mesa: ", "Creando nuevo pedido...", 1, new ImageIcon(imagen), null, null)));
                    pedidos.addItem(nuevoPedido);
                    nuevoPedido.productos = new HashMap<String, Integer>();
                    nuevoPedido.spinners = new ArrayList<JSpinner>();
                }catch(Exception e1) {};}});


        //---------------------------------------------
        finalizarPedido = new JButton("Finalizar Pedido");
        finalizarPedido.setPreferredSize(new Dimension(130,30));
        panelBotones.add(finalizarPedido);
        pnlAppoinments.add(panelBotones, BorderLayout.SOUTH);
        finalizarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                FinalizarPedido finPed = new FinalizarPedido((Pedido) pedidos.getSelectedItem(), usuario, sesion);
                finPed.setVisible(true);
            }
        });

    }*/

}

    public static void main(String[] args) {
        org.deustomed.Patient patient1 = new org.deustomed.Patient("1001", "Paciente1", "Surname1", "Surname2", "paciente1@gmail.com", "password", 24);
        ArrayList<Appointment> appoinments = new ArrayList<>();
        appoinments.add( new Appointment(patient1, doctor, LocalDateTime.of(2023, 12, 24, 12, 0), "Cita consulta", "Cita consulta con paciente"));
        appoinments.add( new Appointment(patient1, doctor, LocalDateTime.of(2023, 1, 1, 12, 0), "Cita consulta", "Cita consulta con paciente"));
        appoinments.add( new Appointment(patient1, doctor, LocalDateTime.of(2023, 1, 1, 12, 0), "Cita consulta", "Cita consulta con paciente"));
        ArrayList<Patient> patients = new ArrayList<>();
        patients.add(patient1);
        // TODO: 19/12/23 ajustar instancia a constructor
        Doctor doctor1 = new FamilyDoctor("1000", "Carlos", "Rodriguez", "Martinez", "carlosrodri@gmail.com", "carlosrodriguez", "", Sex.MALE, appoinments, patients);
        WindowDoctor win = new WindowDoctor(doctor1);
        win.setVisible(true);
    }

    public void visualiseAppoinments(Date date) {
        pnlDisplayAppoinments.removeAll();
        pnlDisplayAppoinments.updateUI();
        JPanel pnlModApp = new JPanel();
        pnlDisplayAppoinments.add(pnlModApp, BorderLayout.CENTER);
        pnlModApp.setLayout(new GridLayout(40,1));
        for(Appointment appointment : doctor.getAppointments()) {
            LocalDateTime ldt = appointment.getDate();
            Date asDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
            //System.out.println(truncateTime(asDate));
            //System.out.println("Date: "+truncateTime(date));


            if(compareDateParts(asDate, date)){
                    TitledBorder border = new TitledBorder("");
                    JPanel panel = new JPanel();
                    panel.setLayout(new BorderLayout(3, 3));
                    panel.setPreferredSize(new Dimension(300,200));
                    JPanel nombre = new JPanel();
                    //panel.setLayout(new GridLayout(2, 1));
                    JPanel pnlButton = new JPanel();
                    pnlButton.setLayout(new GridLayout(4,1));

                    panel.setBorder(border);
                    org.deustomed.Patient pat = appointment.getPatient();
                    nombre.setLayout(new BoxLayout(nombre, BoxLayout.Y_AXIS));
                    JLabel appointmentDate = new JLabel("                            "+capitalizeFirstLetter(ldt.getDayOfWeek().toString())+", "+ldt.getDayOfMonth()+" "+capitalizeFirstLetter(ldt.getMonth().toString())+" "+ldt.toLocalTime());
                    JLabel name = new JLabel(pat.getName()+" "+pat.getSurname1()+" "+pat.getSurname2());
                    name.setHorizontalAlignment(SwingConstants.LEFT);
                    name.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                    nombre.add(appointmentDate);
                    nombre.add(name);

                    JPanel pnTa = new JPanel();

                    pnTa.setBorder(BorderFactory.createEmptyBorder(0,5,5,0));
                    JTextArea ta = new JTextArea(appointment.getShortDesciption());
                    ta.setEditable(false);
                    ta.setPreferredSize(new Dimension(200,140));
                    ta.setBorder(BorderFactory.createTitledBorder(""));
                    pnTa.add(ta);
                    panel.add(pnTa, BorderLayout.CENTER);

                    JButton btn = new JButton("Más Info");
                    btn.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });

                    JButton btn2 = new JButton("Iniciar");
                    btn2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });

                    JButton btn3 = new JButton("Cancelar");
                    btn2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                        }
                    });

                    pnlButton.add(btn);
                    pnlButton.add(btn2);
                    pnlButton.add(btn3);

                    panel.add(nombre, BorderLayout.NORTH);
                    panel.add(pnlButton, BorderLayout.EAST);
                    pnlModApp.add(panel);
                    pnlModApp.updateUI();
                    pnlDisplayAppoinments.updateUI();
                }

        }
    }

    private static boolean compareDateParts(Date date1, Date date2) {
        Date truncatedDate1 = truncateTime(date1);
        Date truncatedDate2 = truncateTime(date2);

        return truncatedDate1.equals(truncatedDate2);
    }

    // Set the time components of a Date object to midnight
    private static Date truncateTime(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }
    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }

        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }


    class TablePatientRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return new PatientShowButton((Patient) value);
        }

    }
    class PatientShowButton extends JButton{
        PatientShowButton(Patient patient) {
            super("Mostrar");
            this.setFont(new Font("Arial", 0, 12));
            this.setEnabled(true);
            this.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ShowPatientWindow spw = new ShowPatientWindow(patient);
                    spw.setVisible(true);
                    spw.setDefaultCloseOperation(EXIT_ON_CLOSE);
                }
            });
        }
    }
    class TablePatienteEditor extends DefaultCellEditor {

        public TablePatienteEditor(JTextField textField) {
            super(textField);
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            return new PatientShowButton((Patient) value);
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }

        @Override
        public boolean stopCellEditing() {
            return false;
        }

        @Override
        public void cancelCellEditing() {

        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {

        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {

        }
    }
    public class TableModelPatient extends AbstractTableModel {
        private ArrayList<Patient> patients;

        public TableModelPatient(ArrayList<Patient> data){
            this.patients = new ArrayList<Patient>(data);
        }

        String[] columns = {"Nombre", "1º Apellido", "2º Apellido", "DNI", "NSS", "Correo", "Día de Nacimiento", "Edad", "nº Teléfono", "Dirección", ""};
        Class[] columnClass = {String.class, String.class, String.class, String.class, String.class, String.class, Date.class, Integer.class, String.class, String.class, Patient.class};
        @Override
        public int getRowCount() {
            return this.patients.size();
        }

        @Override
        public int getColumnCount() {
            return 11;
        }

        @Override
        public String getColumnName(int columnIndex) {
            return columns[columnIndex];
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return columnClass[columnIndex];
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            if(columnIndex==10){
                return true;
            }
            return false;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            //System.out.println(doctor.getPatients().size());
            org.deustomed.Patient patient = null;
            try {
                patient = this.patients.get(rowIndex);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            switch (columnIndex) {
                case 0:
                    return patient.getName();
                case 1:
                    return patient.getSurname1();
                case 2:
                    return patient.getSurname2();
                case 3:
                    return patient.getDni();
                case 4:
                    return patient.getNSS();
                case 5:
                    return patient.getEmail();
                case 6:
                    return patient.getBirthDate();
                case 7:
                    return patient.getAge();
                case 8:
                    return patient.getPhoneNumer();
                case 9:
                    return patient.getAddress();
                case 10:
                    return patient;
                default:
                    return null;
            }
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            Patient patient = this.patients.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    patient.setName((String) aValue);
                    break;
                case 1:
                    patient.setSurname1((String) aValue);
                    break;
                case 2:
                    patient.setSurname2((String) aValue);
                    break;
                case 3:
                    patient.setDni((String) aValue);
                    break;
                case 4:
                    patient.setNSS((String) aValue);
                    break;
                case 5:
                    patient.setEmail((String) aValue);
                    break;
                case 6:
                    patient.setBirthDate((Date) aValue);
                    break;
                case 7:
                    patient.setAge((int) aValue);
                    break;
                case 8:
                    patient.setPhoneNumer((String) aValue);
                    break;
                case 9:
                    patient.setAddress((String) aValue);
                    break;
            }
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }
    static class ShowPatientWindow extends JFrame {
        private final JPanel panelWest;

        public static void main(String[] args) {
            Patient patient = new Patient("1", "Antonio", "Gonzalez", "Gonzalez", "mail@gmail.vpm", "antonio", "12345678A", 50, "612345678", "Calle Dirección Inventada", new Date(), "NSS123456");
            ShowPatientWindow spw = new ShowPatientWindow(patient);
            spw.setVisible(true);
            spw.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        public ShowPatientWindow(Patient patient){
            setTitle(patient.getName()+" "+patient.getSurname1()+" "+patient.getSurname2());
            screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setBounds((int) screenSize.getWidth()/4, (int) screenSize.getHeight()/4, (int) screenSize.getWidth()/2, (int) screenSize.getHeight()/2);
            setLayout(new BorderLayout());

            // ----------------------- mostrando la información -------------------
            panelWest = new JPanel();
            panelWest.setPreferredSize(new Dimension(200,200));
            panelWest.setLayout(new BorderLayout());
            JButton btnPhoto = new JButton("");
            ImageIcon icon = new ImageIcon("src/main/java/ui/profileImg.png");
            Image png = icon.getImage().getScaledInstance(150,150, DO_NOTHING_ON_CLOSE);
            btnPhoto.setIcon(new ImageIcon(png));
            btnPhoto.setEnabled(false);
            btnPhoto.setPreferredSize(new Dimension(150,150));
            JPanel p = new JPanel();
            p.add(btnPhoto);
            panelWest.add(p, BorderLayout.NORTH);
            add(panelWest, BorderLayout.WEST);

            FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
            fl.setHgap(30);
            JPanel pnlTitle = new JPanel(fl);

            JLabel name = new JLabel(patient.getName());
            JLabel surname1 = new JLabel(patient.getSurname1());
            JLabel surname2 = new JLabel(patient.getSurname2());

            name.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));
            surname1.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));
            surname2.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));

            pnlTitle.add(name);
            pnlTitle.add(surname1);
            pnlTitle.add(surname2);
            //pnlTitle.add(new JLabel("Paciente del doctor: "));
            panelWest.add(pnlTitle);
    }}

class CreateRoundButton extends JButton {
    Shape shape;

    public CreateRoundButton(String label) {
        super(label);
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width,size.height);
        setPreferredSize(size);

        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillOval(0, 0, getSize().width-1,getSize().height-1);

        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width-1,     getSize().height-1);
    }

    public boolean contains(int x, int y) {
        if (shape == null ||
                !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }

    /*public static void main(String[] args) {
        JButton button = new CreateRoundButton("Click");
        button.setBackground(Color.gray);

        JFrame frame = new JFrame();
        frame.getContentPane().add(button);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setSize(150, 150);
        frame.setVisible(true);
    }*/
}

    public void loadDoctors() {

        PostgrestQuery query = postgrestClient
                .from("doctor_with_personal_data")
                .select("*")
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String id = jsonObject.get("id").getAsString();
        String name = jsonObject.get("name").getAsString();
        String surname1 = jsonObject.get("surname1").getAsString();
        String surname2 = jsonObject.get("surname2").getAsString();
        String dni = jsonObject.get("dni").getAsString();
        String birthdate = jsonObject.get("birthdate").getAsString();
        String email = jsonObject.get("email").getAsString();
        String phone = jsonObject.get("phone").getAsString();
        String address = jsonObject.get("address").getAsString();
        String speciality = jsonObject.get("speciality").getAsString();
        String sexString = jsonObject.get("sex").getAsString();
        Sex sex;
        if (sexString.equals("MALE")) {
            sex = Sex.MALE;
        } else {
            sex = Sex.FEMALE;
        }

        LocalDate date = LocalDate.parse(birthdate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
        String formatted = date.format(formatter);

        if (speciality.equals("Medicina Familiar")) {
            ArrayList<Patient> ownPatients = loadOwnPatients(id);
            ArrayList<Appointment> appointments = loadDoctorAppointments(id);

            FamilyDoctor newFamilyDoctor = new FamilyDoctor(id, name, surname1, surname2, email, "password", dni, sex, appointments, ownPatients);
        } else {
            // TODO: 26/12/23 Create new SpecialistDoctor: load patients Treated, OnTreatment and ToBeTreated 
            //SpecialistDoctor newSpecialistDoctor = new SpecialistDoctor();
        }

    }

    public ArrayList<Patient> loadOwnPatients(String doctorID) {

        ArrayList<Patient> resultArrayList = new ArrayList<>();

        PostgrestQuery query = postgrestClient
                .from("patient_with_personal_data")
                .select("*")
                .eq("doctor_id", doctorID)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            String id = jsonObject.get("id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String surname1 = jsonObject.get("surname1").getAsString();
            String surname2 = jsonObject.get("surname2").getAsString();
            String dni = jsonObject.get("dni").getAsString();
            String birthdate = jsonObject.get("birthdate").getAsString();
            String email = jsonObject.get("email").getAsString();
            String phone = jsonObject.get("phone").getAsString();
            String address = jsonObject.get("address").getAsString();
            String sexString = jsonObject.get("sex").getAsString();
            int age = Integer.parseInt(jsonObject.get("age").getAsString());
            Sex sex;
            if (sexString.equals("MALE")) {
                sex = Sex.MALE;
            } else {
                sex = Sex.FEMALE;
            }

            LocalDate localDate = LocalDate.parse(birthdate);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            String formatted = localDate.format(formatter);
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());


            Patient newPatient = new Patient(id, name, surname1, surname2, email, "password", dni, sex, age, phone, address, date);
            resultArrayList.add(newPatient);
        }

        return resultArrayList;
    }

    public ArrayList<Appointment> loadDoctorAppointments(String doctorID) {
        ArrayList<Appointment> resultArrayList = new ArrayList<>();

        PostgrestQuery query = postgrestClient
                .from("appointment")
                .select("*")
                .eq("fk_doctor_id", doctorID)
                .getQuery();

        String jsonResponse = String.valueOf(postgrestClient.sendQuery(query));
        Gson gson = new Gson();
        JsonArray jsonArray = gson.fromJson(jsonResponse, JsonArray.class);

        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();

            String reason = jsonObject.get("reason").getAsString();
            String dateString = jsonObject.get("date").getAsString();
            Patient appointmentPatient = getPatientWithThisID("");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime localDateTime = LocalDateTime.parse(dateString, formatter);


            Appointment newAppointment = new Appointment(appointmentPatient, null, localDateTime, reason, "");
            resultArrayList.add(newAppointment);
        }


        return resultArrayList;
    }

    public Patient getPatientWithThisID(String id){
        return new Patient();
    }

}