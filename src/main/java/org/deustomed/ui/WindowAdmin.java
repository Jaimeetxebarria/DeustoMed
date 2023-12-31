package org.deustomed.ui;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.deustomed.*;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WindowAdmin extends JFrame {

    protected List<User> patients;
    protected List<User> doctors;
    protected JsonArray jsonPatientIDs;

    protected JsonArray jsonDoctorData;

    protected JTabbedPane tabAdmin;

    protected JPanel pnlPatient;
    protected JTable tblPatient;
    protected DefaultTableModel mdlPatient;
    protected JScrollPane scrPatient;
    protected JTextField tfFindPatient;
    protected JButton btnPatient;
    protected JButton btnLogoutPatient;

    protected JPanel pnlDoctor;
    protected JTable tblDoctor;
    protected DefaultTableModel mdlDoctor;
    protected JScrollPane scrDoctor;
    protected JTextField tfFindDoctor;
    protected JButton btnDoctor;
    protected JButton btnLogoutDoctor;

    protected JPanel pnlLogs;

    private static PostgrestClient postgrestClient;
    static final Gson gson = new Gson();

    public WindowAdmin(){
        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        String anonymousToken = configLoader.getAnonymousToken();
        postgrestClient = new PostgrestClient(hostname, endpoint, new AnonymousAuthenticationService(anonymousToken));

        this.setTitle("DeustoMed");
        this.setSize(950, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        patients= new ArrayList<>();
        doctors= new ArrayList<>();

        tabAdmin = new JTabbedPane();

        //Patient
        PostgrestQuery queryPatient = postgrestClient
                .from("patient")
                .select("id")
                .getQuery();
        String jsonResponseP = String.valueOf(postgrestClient.sendQuery(queryPatient));
        jsonPatientIDs = gson.fromJson(jsonResponseP, JsonArray.class);
        obtainPatients(jsonPatientIDs);
        System.out.println(patients);

        pnlPatient = new JPanel(new BorderLayout());

        String[] columNamesPatients = {"ID", "Surnames", "Name", "Sex", "Email", "DNI", "Age", "Phone", "Address", "Birthdate"};
        mdlPatient = completeTable(columNamesPatients, patients);
        tblPatient = new JTable(mdlPatient);
        tblPatient.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(3).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(6).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(10).setPreferredWidth(150);
        tblPatient.setRowHeight(25);

        configureTable(tblPatient, new ButtonEditor(patients), new ButtonRenderer());

        scrPatient = new JScrollPane(tblPatient);
        tfFindPatient = new JTextField();
        tfFindPatient.setPreferredSize(new Dimension(200, 25));
        btnPatient = new JButton("Add");
        btnLogoutPatient = new JButton("Logout");

        JPanel pnlUpper = new JPanel(new BorderLayout());
        pnlUpper.add(tfFindPatient, BorderLayout.WEST);
        pnlPatient.add(pnlUpper, BorderLayout.NORTH);
        pnlPatient.add(scrPatient, BorderLayout.CENTER);

        JPanel pnlBotton = new JPanel(new BorderLayout());
        pnlBotton.add(btnLogoutPatient, BorderLayout.WEST);
        pnlBotton.add(btnPatient, BorderLayout.EAST);
        pnlPatient.add(pnlBotton, BorderLayout.SOUTH);

        tabAdmin.addTab("Users", pnlPatient);

        btnPatient.addActionListener(e -> new WindowAddUser(patients));

        btnLogoutPatient.addActionListener(e -> dispose());

        //Doctor
        PostgrestQuery queryDoctor = postgrestClient
                .from("doctor_with_personal_data")
                .select("*")
                .getQuery();
        String jsonResponse = String.valueOf(postgrestClient.sendQuery(queryDoctor));
        jsonDoctorData = gson.fromJson(jsonResponse, JsonArray.class);
        obtainDoctors(jsonDoctorData);
        System.out.println(doctors);

        pnlDoctor = new JPanel(new BorderLayout());
        String[] columNamesDoctor = {"ID", "Surname", "Name", "Sex", "Email", "DNI", "Speciality"};
        mdlDoctor = completeTable(columNamesDoctor, doctors);
        tblDoctor = new JTable(mdlDoctor);
        tblDoctor.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblDoctor.getColumnModel().getColumn(3).setPreferredWidth(25);
        tblDoctor.getColumnModel().getColumn(7).setPreferredWidth(150);
        tblDoctor.setRowHeight(25);

        configureTable(tblDoctor, new ButtonEditor(doctors), new ButtonRenderer());

        scrDoctor = new JScrollPane(tblDoctor);
        tfFindDoctor = new JTextField();
        tfFindDoctor.setPreferredSize(new Dimension(200, 25));
        btnDoctor = new JButton("Add");
        btnLogoutDoctor = new JButton("Logout");

        JPanel pnlUpperDoctor = new JPanel(new BorderLayout());
        pnlUpperDoctor.add(tfFindDoctor, BorderLayout.WEST);
        pnlDoctor.add(pnlUpperDoctor, BorderLayout.NORTH);

        pnlDoctor.add(scrDoctor, BorderLayout.CENTER);

        JPanel pnlBottonDoctor = new JPanel(new BorderLayout());
        pnlBottonDoctor.add(btnLogoutDoctor, BorderLayout.WEST);
        pnlBottonDoctor.add(btnDoctor, BorderLayout.EAST);
        pnlDoctor.add(pnlBottonDoctor, BorderLayout.SOUTH);

        tabAdmin.addTab("Doctors", pnlDoctor);

        btnDoctor.addActionListener(e -> new WindowAddUser(doctors));

        btnLogoutDoctor.addActionListener(e -> dispose());



        //Logs
        pnlLogs = new JPanel(new BorderLayout());

        this.add(tabAdmin);

        this.setVisible(true);
    }

    public void obtainPatients(JsonArray jsonPatientID){
        for (JsonElement jsonElement : jsonPatientID) {
            String jsonObjId = jsonElement.getAsString();
            PostgrestQuery queryPatient = postgrestClient
                    .from("person")
                    .select("*")
                    .eq("id", jsonObjId)
                    .getQuery();
            String jsonResponseP = String.valueOf(postgrestClient.sendQuery(queryPatient));
            JsonArray jsonPatient = gson.fromJson(jsonResponseP, JsonArray.class);
            for(JsonElement jsonPatientElement : jsonPatient){
                JsonObject jsonObject = jsonPatientElement.getAsJsonObject();
                String id = jsonObject.get("id").getAsString();
                String name = jsonObject.get("name").getAsString();
                String surname1 = jsonObject.get("surname1").getAsString();
                String surname2 = jsonObject.get("surname2").getAsString();
                String email = jsonObject.get("email").getAsString();
                String dni = jsonObject.get("dni").getAsString();
                String phoneNumber = jsonObject.get("phone").getAsString();
                String address = jsonObject.get("address").getAsString();
                Sex sex = Sex.valueOf(jsonObject.get("sex").getAsString().toUpperCase());
                LocalDate birthDate = LocalDate.parse(jsonObject.get("birthdate").getAsString());
                patients.add(new Patient(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address, new ArrayList<>()));
            }

        }
    }

    public void obtainDoctors(JsonArray jsonDoctorIDs){
        for (JsonElement jsonElement : jsonDoctorIDs) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String id = jsonObject.get("id").getAsString();
            String name = jsonObject.get("name").getAsString();
            String surname1 = jsonObject.get("surname1").getAsString();
            String surname2 = jsonObject.get("surname2").getAsString();
            String email = jsonObject.get("email").getAsString();
            String dni = jsonObject.get("dni").getAsString();
            String phoneNumber = jsonObject.get("phone").getAsString();
            String address = jsonObject.get("address").getAsString();
            Sex sex = Sex.valueOf(jsonObject.get("sex").getAsString().toUpperCase());
            LocalDate birthDate = LocalDate.parse(jsonObject.get("birthdate").getAsString());
            String speciality = jsonObject.get("speciality").getAsString();

            doctors.add(new Doctor(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address, speciality, new ArrayList<>()));
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(WindowAdmin::new);
    }

    public DefaultTableModel completeTable(String[] columNames, List<User> users) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columNames);
        if(!users.isEmpty()) {
            if (users.get(0) instanceof Patient) {
                for (User user : users) {
                    Patient patient = (Patient) user;
                    Object[] row = new String[columNames.length];
                    row[0] = String.valueOf(patient.getId());
                    row[1] = patient.getSurname1() + " " + user.getSurname2();
                    row[2] = patient.getName();
                    row[3] = patient.getSex().toString();
                    row[4] = patient.getEmail();
                    row[5] = patient.getDni();
                    row[6] = String.valueOf(patient.getAgeInYears());
                    row[7] = patient.getPhoneNumber();
                    row[8] = patient.getAddress();
                    row[9] = patient.getBirthDate().toString();
                    model.addRow(row);
                }

            } else if (users.get(0) instanceof Doctor) {
                for (User user : users) {
                    Doctor doctor = (Doctor) user;
                    Object[] row = new String[columNames.length];
                    row[0] = String.valueOf(doctor.getId());
                    row[1] = doctor.getSurname1() + " " + user.getSurname2();
                    row[2] = doctor.getName();
                    row[3] = doctor.getSex().toString();
                    row[4] = doctor.getEmail();
                    row[5] = doctor.getDni();
                    row[6] = doctor.getSpeciality();
                    model.addRow(row);
                }
            }
        }

        model.addColumn("Actions");
        return model;
    }

    private void configureTable(JTable table, ButtonEditor buttonEditor, ButtonRenderer buttonRenderer) {
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(buttonEditor);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(buttonRenderer);
    }


    class ButtonRenderer implements TableCellRenderer {
        private JPanel panel;
        private JButton btnEdit;
        private JButton btnDelete;

        public ButtonRenderer() {
            panel = new JPanel(new FlowLayout());
            btnEdit = new JButton("Edit");
            btnDelete = new JButton("Delete");

            JPanel pnlButtons = new JPanel(new GridLayout(1, 2));
            pnlButtons.add(btnEdit);
            pnlButtons.add(btnDelete);
            panel.add(pnlButtons);


            btnEdit.addActionListener(e -> {

                JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, btnEdit);

                    if (table != null) {
                        TableType tableType = getTableType(table, btnEdit);
                        int selectedRow = table.getSelectedRow();
                        System.out.println(selectedRow);
                        if (selectedRow != -1) {
                            int modelRow = table.convertRowIndexToModel(selectedRow);

                            // Obtén los datos asociados a la fila
                            Object[] rowData = new Object[table.getColumnCount()];
                            for (int i = 0; i < table.getColumnCount(); i++) {
                                rowData[i] = table.getModel().getValueAt(modelRow, i);
                            }

                            // Realiza la lógica de edición basada en los datos de la fila
                            switch (tableType) {
                                case PATIENTS:
                                    Patient originalPatient = (Patient) rowData[0];
                                    int indexInListP = patients.indexOf(originalPatient);

                                    if (indexInListP != -1) {
                                        // Crea un nuevo objeto Patient con los valores editados
                                        Patient editedPatient = new Patient(
                                                originalPatient.getId(),                    //id
                                                rowData[2].toString(),                      //name
                                                rowData[1].toString().split(" ")[0],  //surname1
                                                rowData[1].toString().split(" ")[1],  //surname2
                                                (LocalDate) rowData[8],                     //birthdate
                                                originalPatient.getSex(),                   //sex
                                                rowData[4].toString(),                      //dni
                                                rowData[3].toString(),                      //email
                                                rowData[6].toString(),                      //phone
                                                rowData[7].toString(),                      //address
                                                originalPatient.getMedicalRecord());        //medicalRecord


                                        // Actualiza el objeto en la lista
                                        patients.set(indexInListP, editedPatient);

                                        // Actualiza la fila en la tabla
                                        for (int i = 1; i < rowData.length; i++) {
                                            table.getModel().setValueAt(rowData[i], modelRow, i);
                                        }
                                    }
                                    System.out.println(patients);
                                    break;
                                case DOCTORS:
                                    Doctor originalDoctor = (Doctor) rowData[0];
                                    int indexInListD = doctors.indexOf(originalDoctor);
                                    if (indexInListD != -1) {
                                        // Crea un nuevo objeto Patient con los valores editados
                                        Doctor editedDoctor = new Doctor(
                                                originalDoctor.getId(),                       //id
                                                rowData[2].toString(),                        //name
                                                rowData[1].toString().split(" ")[0],    //surname1
                                                rowData[1].toString().split(" ")[1],    //surname2
                                                originalDoctor.getBirthDate(),                //birthdate
                                                originalDoctor.getSex(),                      //sex
                                                rowData[4].toString(),                        //dni
                                                rowData[3].toString(),                        //email
                                                originalDoctor.getPhoneNumber(),              //phone
                                                rowData[5].toString(),                        //address
                                                originalDoctor.getSpeciality(),               //speciality
                                                originalDoctor.getAppointments());


                                    // Actualiza el objeto en la lista
                                    doctors.set(indexInListD, editedDoctor);

                                    // Actualiza la fila en la tabla
                                    for (int i = 1; i < rowData.length; i++) {
                                        table.getModel().setValueAt(rowData[i], modelRow, i);
                                    }
                                }
                                System.out.println(doctors);
                                break;
                            case OTHER:
                                break;
                        }


                    }
                }
            });

            btnDelete.addActionListener(e -> {
                // Obtain Table
                JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, btnDelete);

                if (table != null) {
                    TableType tableType = getTableType(table, btnDelete);
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        String id = table.getModel().getValueAt(modelRow, 5).toString();
                        switch (tableType) {
                            case PATIENTS:
                                patients.remove(modelRow);
                                PostgrestQuery deletePatient = postgrestClient
                                        .from("person")
                                        .delete()
                                        .eq("dni", id)
                                        .getQuery();
                                postgrestClient.sendQuery(deletePatient);
                                System.out.println("Paciente eliminado");
                                break;
                            case DOCTORS:
                                doctors.remove(modelRow);
                                PostgrestQuery deleteDoctor = postgrestClient
                                        .from("person")
                                        .delete()
                                        .eq("dni", "87654321B")
                                        .getQuery();
                                postgrestClient.sendQuery(deleteDoctor);
                                System.out.println("Doctor eliminado");
                                break;
                            case OTHER:
                                break;
                        }

                        // Remueve la fila de la tabla
                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    }
                }
            });
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return panel;
        }
        public TableType getTableType(JTable table, JButton button){
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedRow);

                if (table.getModel() instanceof DefaultTableModel model) {

                    if (model.getRowCount() > modelRow) {
                        Object data = table.getModel();
                        if (data instanceof Patient) {
                            return TableType.PATIENTS;
                        } else if (data instanceof Doctor) {
                            return TableType.DOCTORS;
                        }

                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    }
                }


            }
            return TableType.OTHER;
        }
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private final ButtonRenderer renderer;
        private Object currentValue;

        public ButtonEditor(List<?> dataList) {
            renderer = new ButtonRenderer();
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            currentValue = value;
            return renderer.getTableCellRendererComponent(table, value, isSelected, true, row, column);
        }

        @Override
        public Object getCellEditorValue() {
            return currentValue;
        }
    }
}


