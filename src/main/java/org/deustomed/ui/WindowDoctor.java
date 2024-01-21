package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.toedter.calendar.JDateChooser;
import org.deustomed.*;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.authentication.UserAuthenticationService;
import org.deustomed.logs.LoggerMaker;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.CellEditorListener;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Logger;

public class WindowDoctor extends UserAuthenticatedWindow {
    private Doctor doctor;
    private final JPanel pnlInfo;
    private final JPanel pnlAppointments;
    private static Dimension screenSize;
    private final JPanel pnlDisplayAppoinments;
    private JTable tableOwnPatient;
    private JTable tableTreatedPatient;
    private JTable tableInTreatmentPatient;
    private ArrayList<Patient> ownPatients;
    private ArrayList<Patient> treatedPatients;
    private ArrayList<Patient> inTreatmentPatients;
    private final JTabbedPane tabbedPaneCenter;
    private Date selectedDate;
    private static PostgrestClient postgrestClient;
    private LoadingWindow loadingWindow;

    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();

        ConfigLoader configLoader = new ConfigLoader();
        WindowDoctor winFamilyDoctor = new WindowDoctor("00AAG", new AnonymousAuthenticationService(configLoader.getAnonymousToken()));
        winFamilyDoctor.setVisible(true);

        // WindowDoctor winSpecialistDoctor = new WindowDoctor("00AAA", new AnonymousAuthenticationService(configLoader.getAnonymousToken()));
        // winSpecialistDoctor.setVisible(true);
    }

    public WindowDoctor(String doctorID, PostgrestAuthenticationService authenticationService) {
        super(authenticationService instanceof UserAuthenticationService ? (UserAuthenticationService) authenticationService : null);

        ConfigLoader configLoader = new ConfigLoader();
        postgrestClient = new PostgrestClient(configLoader.getHostname(), configLoader.getEndpoint(), authenticationService);
        PostgrestQuery query = postgrestClient
                .from("doctor_with_personal_data")
                .select("*")
                .eq("id", doctorID)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.doctor = new Doctor(jsonObject);
        this.doctor.setAppointments(Doctor.loadDoctorAppointments(postgrestClient, doctorID));

        loadingWindow = new LoadingWindow(doctor.getName() + " " + doctor.getSurname1() + " " + doctor.getSurname2() + " ");
        loadingWindow.setVisible(true);

        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth() / 4, (int) screenSize.getHeight() / 4, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        setLayout(new BorderLayout());

        Comparator<Appointment> appointmentComparator = new Comparator<Appointment>() {
            @Override
            public int compare(Appointment o1, Appointment o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        };

        doctor.getAppointments().sort(appointmentComparator);


        // ------------------ pnlInfo ------------------
        pnlInfo = new JPanel();
        pnlInfo.setPreferredSize(new Dimension(300, 300));
        pnlInfo.setLayout(new BorderLayout());
        JButton btnPhoto = new JButton("");
        ImageIcon icon = new ImageIcon("src/main/java/ui/profileImg.png");
        Image png = icon.getImage().getScaledInstance(200, 200, DO_NOTHING_ON_CLOSE);
        btnPhoto.setIcon(new ImageIcon(png));
        btnPhoto.setEnabled(false);
        btnPhoto.setPreferredSize(new Dimension(250, 250));
        JPanel p = new JPanel();
        p.add(btnPhoto);
        pnlInfo.add(p, BorderLayout.NORTH);

        FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
        fl.setHgap(30);
        JPanel pnlc = new JPanel(fl);
        JPanel panelButtons = new JPanel();
        panelButtons.setLayout(new GridLayout(3, 1));

        JButton info = new JButton("Chats en curso");
        info.setPreferredSize(new Dimension(200, 30));
        info.setHorizontalAlignment(SwingConstants.CENTER);
        info.addActionListener((ActionEvent e) -> {
            DoctorChat doctorChat = new DoctorChat(doctorID);
            doctorChat.setVisible(true);
        });


        JLabel name = new JLabel("Dr. " + doctor.getName());
        JLabel surname1 = new JLabel(doctor.getSurname1());
        JLabel surname2 = new JLabel(doctor.getSurname2());

        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));
        surname1.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));
        surname2.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));

        pnlc.add(name);
        pnlc.add(surname1);
        pnlc.add(surname2);
        pnlc.add(new JLabel("Doctor especialista en " + doctor.getSpeciality()));
        pnlc.add(Box.createVerticalStrut(200));
        panelButtons.add(info);
        pnlc.add(panelButtons);

        pnlInfo.add(pnlc, BorderLayout.CENTER);
        JButton singOut = new JButton("Cerrar Sesión");
        singOut.addActionListener((ActionEvent e) -> {
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            this.dispose();
        });

        pnlInfo.add(singOut, BorderLayout.SOUTH);
        add(pnlInfo, BorderLayout.WEST);


        //--------------------- Panel CENTER: TabbedPane: Pacientes, Medicamentos, Char Médico-Paciente... --------------------------------

        tabbedPaneCenter = new JTabbedPane();
        tabbedPaneCenter.setVisible(true);

        doctor.setRegistryOfPatients(Doctor.loadPatientIDs(doctor.getId(), postgrestClient, true));
        ArrayList<Patient> fullRegistry = doctor.loadPatients(postgrestClient, 0);

        TableModelPatient tmp0 = new TableModelPatient(fullRegistry);
        tableTreatedPatient = new JTable(tmp0);
        tableTreatedPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
        tableTreatedPatient.setDefaultEditor(Patient.class, new TablePatientEditor());
        JScrollPane spTableFullRegistry = new JScrollPane(tableTreatedPatient);
        tabbedPaneCenter.addTab("Registro Completo Pacientes", null, spTableFullRegistry);

        add(tabbedPaneCenter, BorderLayout.CENTER);

        // Para Doctor de Medicina Familiar:
        if (doctor.getSpeciality().equals("Medicina Familiar")) {

            doctor.setOwnPatients(Doctor.loadPatientIDs(doctor.getId(), postgrestClient, false));
            ownPatients = doctor.loadPatients(postgrestClient, 1);
            System.out.println("These are the patients: " + ownPatients);

            if (ownPatients != null) {
                TableModelPatient tmp1 = new TableModelPatient(ownPatients);
                tableOwnPatient = new JTable(tmp1);
                tableOwnPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
                tableOwnPatient.setDefaultEditor(Patient.class, new TablePatientEditor());
                JScrollPane spTableOwnPatient = new JScrollPane(tableOwnPatient);

                tabbedPaneCenter.addTab("Registro Pacientes Propios", null, spTableOwnPatient);
            }

        } else {
            ArrayList<String> treatedPatientsIDs = Doctor.loadSpecialistPatientIDs(doctor.getId(), postgrestClient, true);
            doctor.setTreatedPatients(treatedPatientsIDs);

            treatedPatients = doctor.loadPatients(postgrestClient, 2);

            TableModelPatient tmp2 = new TableModelPatient(treatedPatients);
            tableTreatedPatient = new JTable(tmp2);
            tableTreatedPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
            tableTreatedPatient.setDefaultEditor(Patient.class, new TablePatientEditor());
            JScrollPane spTableTreatedPatients = new JScrollPane(tableTreatedPatient);

            ArrayList<String> inTreatmentPatientsIDs = Doctor.loadSpecialistPatientIDs(doctor.getId(), postgrestClient, false);
            doctor.setTreatedPatients(inTreatmentPatientsIDs);
            inTreatmentPatients = doctor.loadPatients(postgrestClient, 3);
            TableModelPatient tmp3 = new TableModelPatient(inTreatmentPatients);
            tableInTreatmentPatient = new JTable(tmp3);
            tableInTreatmentPatient.setDefaultRenderer(Patient.class, new TablePatientRenderer());
            tableInTreatmentPatient.setDefaultEditor(Patient.class, new TablePatientEditor());
            JScrollPane spTableInTreatmentPatients = new JScrollPane(tableInTreatmentPatient);

            tabbedPaneCenter.addTab("Registro Pacientes Tratados", null, spTableTreatedPatients);
            tabbedPaneCenter.addTab("Registro Pacientes Tratamiento", null, spTableInTreatmentPatients);

        }

        //--------------------- Panel EAST: Citas (Appointments) --------------------------------
        pnlAppointments = new JPanel();
        add(pnlAppointments, BorderLayout.EAST);
        pnlAppointments.setLayout(new BorderLayout());
        pnlAppointments.setPreferredSize(new Dimension((int) (screenSize.width / 4.1), (int) (screenSize.height * 0.75)));
        TitledBorder bordeEast = BorderFactory.createTitledBorder("Citas");
        pnlAppointments.setBorder(bordeEast);
        pnlDisplayAppoinments = new JPanel();

        pnlAppointments.add(pnlDisplayAppoinments, BorderLayout.CENTER);
        JScrollPane sbarPedidos = new JScrollPane(pnlDisplayAppoinments);
        pnlAppointments.add(sbarPedidos, BorderLayout.CENTER);

        JPanel pnlDateChooser = new JPanel();
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
        dateChooser.setPreferredSize(new Dimension(200, 25));
        pnlDateChooser.add(dateChooser);
        pnlAppointments.add(pnlDateChooser, BorderLayout.NORTH);

        visualiseAppoinments(new Date());

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                loadingWindow.dispose();
                LoadingWindow.stopThread();
                setVisible(true);
            }
        });
    }

    public void visualiseAppoinments(Date date) {
        pnlDisplayAppoinments.removeAll();
        pnlDisplayAppoinments.updateUI();
        JPanel pnlModApp = new JPanel();
        pnlDisplayAppoinments.add(pnlModApp, BorderLayout.CENTER);
        pnlModApp.setLayout(new GridLayout(40, 1));
        for (Appointment appointment : doctor.getAppointments()) {
            LocalDateTime ldt = appointment.getDate();
            Date asDate = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());

            if (compareDateParts(asDate, date)) {
                TitledBorder border = new TitledBorder("");
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout(3, 3));
                panel.setPreferredSize(new Dimension(300, 200));
                JPanel nombre = new JPanel();

                JPanel pnlButton = new JPanel();
                pnlButton.setLayout(new GridLayout(4, 1));

                panel.setBorder(border);
                Patient pat = appointment.getPatient(postgrestClient);
                nombre.setLayout(new BoxLayout(nombre, BoxLayout.Y_AXIS));
                JLabel appointmentDate = new JLabel("                            " + capitalizeFirstLetter(ldt.getDayOfWeek().toString()) + ", " + ldt.getDayOfMonth() + " " + capitalizeFirstLetter(ldt.getMonth().toString()) + " " + ldt.toLocalTime());
                JLabel name = new JLabel(pat.getName() + " " + pat.getSurname1() + " " + pat.getSurname2());
                name.setHorizontalAlignment(SwingConstants.LEFT);
                name.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
                nombre.add(appointmentDate);
                nombre.add(name);

                JPanel pnTa = new JPanel();
                pnTa.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 0));
                JTextArea ta = new JTextArea(appointment.getShortDescription());
                ta.setEditable(false);
                ta.setPreferredSize(new Dimension(200, 140));
                ta.setBorder(BorderFactory.createTitledBorder(""));
                pnTa.add(ta);
                panel.add(pnTa, BorderLayout.CENTER);

                JButton btn2 = new JButton("Iniciar");
                btn2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        WindowDiagnosis windowDiagnosis = new WindowDiagnosis(appointment, postgrestClient);
                        windowDiagnosis.setVisible(true);
                    }
                });

                pnlButton.add(btn2);

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

    public static String capitalizeFirstLetter(String input) {
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

    class PatientShowButton extends JButton {
        private Patient patient;

        public Patient getButtonPatient() {
            return patient;
        }

        PatientShowButton(Patient patient) {
            super("Mostrar");
            this.patient = patient;
            this.setFont(new Font("Arial", 0, 12));
            this.setEnabled(true);

        }
    }

    class TablePatientEditor extends AbstractCellEditor implements TableCellEditor {
        private PatientShowButton patientShowButton;

        public TablePatientEditor() {
            super();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            patientShowButton = new PatientShowButton((Patient) value);
            patientShowButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ShowPatientWindow spw = new ShowPatientWindow(patientShowButton.getButtonPatient());
                    spw.setVisible(true);
                    fireEditingStopped();
                }
            });

            return patientShowButton;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
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
            fireEditingStopped();
            return true;
        }

        @Override
        public void cancelCellEditing() {
            fireEditingCanceled();
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
            listenerList.add(CellEditorListener.class, l);
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
            listenerList.remove(CellEditorListener.class, l);
        }
    }

    public class TableModelPatient extends AbstractTableModel {
        private final ArrayList<Patient> patients;

        public TableModelPatient(ArrayList<Patient> data) {
            this.patients = new ArrayList<Patient>(data);
        }

        String[] columns = {"Nombre", "1º Apellido", "2º Apellido", "DNI", "Correo", "Día de Nacimiento", "Edad", "nº Teléfono", "Direcci" +
                "ón", ""};
        Class[] columnClass = {String.class, String.class, String.class, String.class, String.class, LocalDate.class, Integer.class,
                String.class, String.class, Patient.class};

        @Override
        public int getRowCount() {
            return this.patients.size();
        }

        @Override
        public int getColumnCount() {
            return 10;
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
            return columnIndex == 9;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {

            org.deustomed.Patient patient = null;
            try {
                patient = this.patients.get(rowIndex);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return switch (columnIndex) {
                case 0 -> patient.getName();
                case 1 -> patient.getSurname1();
                case 2 -> patient.getSurname2();
                case 3 -> patient.getDni();
                case 4 -> patient.getEmail();
                case 5 -> patient.getBirthDate();
                case 6 -> patient.getAgeInYears();
                case 7 -> patient.getPhoneNumber();
                case 8 -> patient.getAddress();
                case 9 -> patient;
                default -> null;
            };
        }

        @Override
        public void addTableModelListener(TableModelListener l) {

        }

        @Override
        public void removeTableModelListener(TableModelListener l) {

        }
    }

}