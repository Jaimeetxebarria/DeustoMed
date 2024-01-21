package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.deustomed.ConfigLoader;
import org.deustomed.Doctor;
import org.deustomed.Patient;
import org.deustomed.User;
import org.deustomed.authentication.SuperuserAuthenticationService;
import org.deustomed.authentication.UserAuthenticationService;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.deustomed.postgrest.authentication.PostgrestAuthenticationService;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WindowAdmin extends UserAuthenticatedWindow {

    protected List<User> patients, doctors;
    protected JsonArray jsonPatientData, jsonDoctorData;

    protected JTabbedPane tabAdmin;

    protected JPanel pnlPatient, pnlDoctor;
    protected JTable tblPatient, tblDoctor;
    protected final String[] columNamesPatients = {"ID", "1º apellido", "2º apellido", "Nombre", "Sexo", "Email", "DNI", "Edad", "Teléfono", "Dirección", "Fecha de nacimiento"};
    protected final String[] columNamesDoctor = {"ID", "1º apellido", "2º apellido", "Nombre", "Sexo", "Email", "DNI", "Edad", "Teléfono", "Dirección", "Fecha de nacimiento", "Especialidad"};
    protected final int ID_COLUMN = 0;
    protected final int SURNAME1_COLUMN = 1;
    protected final int SURNAME2_COLUMN = 2;
    protected final int NAME_COLUMN = 3;
    protected final int SEX_COLUMN = 4;
    protected final int EMAIL_COLUMN = 5;
    protected final int DNI_COLUMN = 6;
    protected final int AGE_COLUMN = 7;
    protected final int PHONE_COLUMN = 8;
    protected final int ADDRESS_COLUMN = 9;
    protected final int BIRTHDATE_COLUMN = 10;
    protected final int SPECIALITY_COLUMN = 11;
    protected DefaultTableModel mdlPatient, mdlDoctor;
    protected JScrollPane scrPatient, scrDoctor;
    protected JTextField tfFindPatient, tfFindDoctor;
    protected JButton btnPatient, btnDoctor, btnEditPatient, btnEditDoctor, btnDeletePatient, btnDeleteDoctor;
    protected JButton btnLogoutPatient, btnLogoutDoctor;
    protected JPanel pnlLogs;
    private static PostgrestClient postgrestClient;

    public WindowAdmin(PostgrestAuthenticationService authenticationService) {
        super(authenticationService instanceof UserAuthenticationService ? (UserAuthenticationService) authenticationService : null);

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        postgrestClient = new PostgrestClient(hostname, endpoint, authenticationService);

        this.setTitle("DeustoMed");
        this.setSize(1050, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        patients= new ArrayList<>();
        doctors= new ArrayList<>();

        tabAdmin = new JTabbedPane();

        //Patient
        PostgrestQuery queryPatient = postgrestClient
                .from("patient_with_personal_data")
                .select("*")
                .getQuery();

        jsonPatientData = postgrestClient.sendQuery(queryPatient).getAsJsonArray();
        obtainPatients(jsonPatientData);

        pnlPatient = new JPanel(new BorderLayout());
        Border border = new TitledBorder("Tabla de pacientes:");
        pnlPatient.setBorder(border);

        mdlPatient = completeTable(columNamesPatients, patients);
        TableRowSorter<TableModel> sorterPatients = new TableRowSorter<>(mdlPatient);
        tblPatient = new JTable(mdlPatient);
        tblPatient.setRowSorter(sorterPatients);
        tblPatient.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(4).setPreferredWidth(30);
        tblPatient.getColumnModel().getColumn(7).setPreferredWidth(25);
        tblPatient.setRowHeight(25);

        scrPatient = new JScrollPane(tblPatient);
        tfFindPatient = new JTextField();
        tfFindPatient.setPreferredSize(new Dimension(200, 25));
        btnPatient = new JButton("Añadir");
        btnEditPatient = new JButton("Editar");
        btnDeletePatient = new JButton("Eliminar");
        btnLogoutPatient = new JButton("Cerrar sesión");

        JPanel pnlUpper = new JPanel(new BorderLayout());
        pnlUpper.add(tfFindPatient, BorderLayout.WEST);
        pnlPatient.add(pnlUpper, BorderLayout.NORTH);
        pnlPatient.add(scrPatient, BorderLayout.CENTER);

        JPanel pnlBotton = new JPanel(new BorderLayout());
        pnlBotton.add(btnLogoutPatient, BorderLayout.WEST);
        JPanel pnlButtons = new JPanel(new GridLayout(1, 3));
        pnlButtons.add(btnPatient);
        pnlButtons.add(btnEditPatient);
        pnlButtons.add(btnDeletePatient);
        pnlBotton.add(pnlButtons, BorderLayout.EAST);
        pnlPatient.add(pnlBotton, BorderLayout.SOUTH);

        tabAdmin.addTab("Paciente", pnlPatient);

        tfFindPatient.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterData(tblPatient, tfFindPatient);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterData(tblPatient, tfFindPatient);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterData(tblPatient, tfFindPatient);
            }
        });

        //Set rowSorter to headers
        tblPatient.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tblPatient.getTableHeader().columnAtPoint(e.getPoint());
                if (sorterPatients.getSortKeys().isEmpty() || sorterPatients.getSortKeys().get(0).getColumn() != column) {
                    sorterPatients.setSortKeys(null);
                } else {
                    List<RowSorter.SortKey> sortKeys = new ArrayList<>(sorterPatients.getSortKeys());
                    RowSorter.SortKey currentSortKey = sortKeys.get(0);
                    if (currentSortKey.getSortOrder() == SortOrder.ASCENDING) {
                        sortKeys.set(0, new RowSorter.SortKey(column, SortOrder.DESCENDING));
                    } else {
                        sortKeys.set(0, new RowSorter.SortKey(column, SortOrder.ASCENDING));
                    }
                    sorterPatients.setSortKeys(sortKeys);
                }
            }
        });

        //Create new Patient
        btnPatient.addActionListener(e -> new WindowAddUser(patients, tblPatient, postgrestClient));

        //Edit Patient
        btnEditPatient.addActionListener(e -> {
            int selectedRow = tblPatient.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = tblPatient.convertRowIndexToModel(selectedRow);

                Object[] rowData = new Object[tblPatient.getColumnCount()];
                for (int i = 0; i < tblPatient.getColumnCount(); i++) {
                    rowData[i] = tblPatient.getModel().getValueAt(modelRow, i);
                }
                Patient originalPatient = null;
                for (User patient : patients) {
                    if (patient.getId().equals(rowData[0].toString())) {
                        originalPatient = (Patient) patient;
                    }
                }
                int indexInListP = patients.indexOf(originalPatient);

                if (indexInListP != -1 && validateData(tblPatient, modelRow)) {
                    Patient editedPatient = new Patient(
                            originalPatient.getId(),                                //id
                            rowData[NAME_COLUMN].toString(),                        //name
                            rowData[SURNAME1_COLUMN].toString(),                    //surname1
                            rowData[SURNAME2_COLUMN].toString(),                    //surname2
                            LocalDate.parse(rowData[BIRTHDATE_COLUMN].toString()),  //birthdate
                            originalPatient.getSex(),                               //sex
                            rowData[DNI_COLUMN].toString(),                         //dni
                            rowData[EMAIL_COLUMN].toString(),                       //email
                            rowData[PHONE_COLUMN].toString(),                       //phone
                            rowData[ADDRESS_COLUMN].toString(),                     //address
                            originalPatient.getMedicalRecord());                    //medicalRecord

                    patients.set(indexInListP, editedPatient);

                    for (int i = 1; i < rowData.length; i++) {
                        tblPatient.getModel().setValueAt(rowData[i], modelRow, i);
                    }

                    JsonObject jsonObjEditPatient = new JsonObject();
                    jsonObjEditPatient.addProperty("name", editedPatient.getName());
                    jsonObjEditPatient.addProperty("surname1", editedPatient.getSurname1());
                    jsonObjEditPatient.addProperty("surname2", editedPatient.getSurname2());
                    jsonObjEditPatient.addProperty("birthdate", editedPatient.getBirthDate().toString());
                    jsonObjEditPatient.addProperty("dni", editedPatient.getDni());
                    jsonObjEditPatient.addProperty("email", editedPatient.getEmail());
                    jsonObjEditPatient.addProperty("phone", editedPatient.getPhoneNumber());
                    jsonObjEditPatient.addProperty("address", editedPatient.getAddress());

                    PostgrestQuery queryEditPatient = postgrestClient
                            .from("person")
                            .update(jsonObjEditPatient)
                            .eq("dni", editedPatient.getDni())
                            .getQuery();
                    postgrestClient.sendQuery(queryEditPatient);
                }
            }
        });

        //Delete patient
        btnDeletePatient.addActionListener(e -> {
            int selectedRow = tblPatient.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = tblPatient.convertRowIndexToModel(selectedRow);
                String id = tblPatient.getModel().getValueAt(modelRow, 0).toString();
                patients.remove(modelRow);
                PostgrestQuery deletePatient = postgrestClient
                        .from("person")
                        .delete()
                        .eq("id", id)
                        .getQuery();
                postgrestClient.sendQuery(deletePatient);
                ((DefaultTableModel) tblPatient.getModel()).removeRow(modelRow);
            }
        });

        btnLogoutPatient.addActionListener(e -> {
            dispose();
            new WindowLogin();
        });

        //Doctor
        PostgrestQuery queryDoctor = postgrestClient
                .from("doctor_with_personal_data")
                .select("*")
                .getQuery();

        jsonDoctorData = postgrestClient.sendQuery(queryDoctor).getAsJsonArray();
        obtainDoctors(jsonDoctorData);

        pnlDoctor = new JPanel(new BorderLayout());
        border = new TitledBorder("Tabla de doctores:");
        pnlDoctor.setBorder(border);

        mdlDoctor = completeTable(columNamesDoctor, doctors);
        TableRowSorter<TableModel> sorterDoctors = new TableRowSorter<>(mdlDoctor);
        tblDoctor = new JTable(mdlDoctor);
        tblDoctor.setRowSorter(sorterDoctors);
        tblDoctor.getColumnModel().getColumn(ID_COLUMN).setPreferredWidth(25);
        tblDoctor.getColumnModel().getColumn(SEX_COLUMN).setPreferredWidth(30);
        tblPatient.getColumnModel().getColumn(AGE_COLUMN).setPreferredWidth(25);
        tblDoctor.setRowHeight(25);

        scrDoctor = new JScrollPane(tblDoctor);
        tfFindDoctor = new JTextField();
        tfFindDoctor.setPreferredSize(new Dimension(200, 25));
        btnDoctor = new JButton("Añadir");
        btnEditDoctor = new JButton("Editar");
        btnDeleteDoctor = new JButton("Eliminar");
        btnLogoutDoctor = new JButton("Cerrar sesión");

        JPanel pnlUpperDoctor = new JPanel(new BorderLayout());
        pnlUpperDoctor.add(tfFindDoctor, BorderLayout.WEST);
        pnlDoctor.add(pnlUpperDoctor, BorderLayout.NORTH);

        pnlDoctor.add(scrDoctor, BorderLayout.CENTER);

        JPanel pnlBottonDoctor = new JPanel(new BorderLayout());
        pnlBottonDoctor.add(btnLogoutDoctor, BorderLayout.WEST);
        JPanel pnlButtonsDoctor = new JPanel(new GridLayout(1, 3));
        pnlButtonsDoctor.add(btnDoctor);
        pnlButtonsDoctor.add(btnEditDoctor);
        pnlButtonsDoctor.add(btnDeleteDoctor);
        pnlBottonDoctor.add(pnlButtonsDoctor, BorderLayout.EAST);
        pnlDoctor.add(pnlBottonDoctor, BorderLayout.SOUTH);

        tabAdmin.addTab("Doctores", pnlDoctor);

        //Add listener to the finder
        tfFindDoctor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filterData(tblDoctor, tfFindDoctor);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filterData(tblDoctor, tfFindDoctor);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filterData(tblDoctor, tfFindDoctor);
            }
        });

        //Add sorter to headers
        tblDoctor.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tblDoctor.getTableHeader().columnAtPoint(e.getPoint());
                if (sorterDoctors.getSortKeys().isEmpty() || sorterDoctors.getSortKeys().get(0).getColumn() != column) {
                    sorterDoctors.setSortKeys(null);
                } else {
                    List<RowSorter.SortKey> sortKeys = new ArrayList<>(sorterDoctors.getSortKeys());
                    RowSorter.SortKey currentSortKey = sortKeys.get(0);
                    if (currentSortKey.getSortOrder() == SortOrder.ASCENDING) {
                        sortKeys.set(0, new RowSorter.SortKey(column, SortOrder.DESCENDING));
                    } else {
                        sortKeys.set(0, new RowSorter.SortKey(column, SortOrder.ASCENDING));
                    }
                    sorterDoctors.setSortKeys(sortKeys);
                }
            }
        });

        //Create new Doctors
        btnDoctor.addActionListener(e -> new WindowAddUser(doctors, tblDoctor, postgrestClient));

        //Edit Doctors
        btnEditDoctor.addActionListener(e -> {
            int selectedRow = tblDoctor.getSelectedRow();
            if (selectedRow != -1) {
                Object[] rowData = new Object[tblDoctor.getColumnCount()];
                for (int i = 0; i < tblDoctor.getColumnCount(); i++) {
                    rowData[i] = tblDoctor.getModel().getValueAt(selectedRow, i);
                }
                Doctor originalDoctor = null;
                for (User doctor : doctors) {
                    if (doctor.getId().equals(rowData[0].toString())) {
                        originalDoctor = (Doctor) doctor;
                        break;
                    }
                }
                int indexInListD = doctors.indexOf(originalDoctor);
                if (indexInListD != -1 && validateData(tblDoctor, selectedRow)) {
                    Doctor editedDoctor = new Doctor(
                            originalDoctor.getId(),                       //id
                            rowData[NAME_COLUMN].toString(),              //name
                            rowData[SURNAME1_COLUMN].toString(),          //surname1
                            rowData[SURNAME2_COLUMN].toString(),          //surname2
                            originalDoctor.getBirthDate(),                //birthdate
                            originalDoctor.getSex(),                      //sex
                            rowData[DNI_COLUMN].toString(),               //dni
                            rowData[EMAIL_COLUMN].toString(),             //email
                            rowData[PHONE_COLUMN].toString(),             //phone
                            rowData[ADDRESS_COLUMN].toString(),           //address
                            originalDoctor.getSpeciality(),               //speciality
                            originalDoctor.getAppointments());

                    doctors.set(indexInListD, editedDoctor);

                    for (int i = 1; i < rowData.length; i++) {
                        tblDoctor.getModel().setValueAt(rowData[i], selectedRow, i);
                    }

                    JsonObject jsonObjEditDoctor = new JsonObject();
                    jsonObjEditDoctor.addProperty("name", editedDoctor.getName());
                    jsonObjEditDoctor.addProperty("surname1", editedDoctor.getSurname1());
                    jsonObjEditDoctor.addProperty("surname2", editedDoctor.getSurname2());
                    jsonObjEditDoctor.addProperty("birthdate", editedDoctor.getBirthDate().toString());
                    jsonObjEditDoctor.addProperty("dni", editedDoctor.getDni());
                    jsonObjEditDoctor.addProperty("email", editedDoctor.getEmail());
                    jsonObjEditDoctor.addProperty("phone", editedDoctor.getPhoneNumber());
                    jsonObjEditDoctor.addProperty("address", editedDoctor.getAddress());

                    PostgrestQuery queryEditDoctor = postgrestClient
                            .from("person")
                            .update(jsonObjEditDoctor)
                            .eq("dni", editedDoctor.getDni())
                            .getQuery();
                    postgrestClient.sendQuery(queryEditDoctor);
                }
            }
        });

        //Delete Doctors
        btnDeleteDoctor.addActionListener(e -> {
            int selectedRow = tblDoctor.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = tblDoctor.convertRowIndexToModel(selectedRow);
                String id = tblDoctor.getModel().getValueAt(modelRow, 0).toString();
                doctors.remove(modelRow);
                PostgrestQuery deleteDoctor = postgrestClient
                        .from("person")
                        .delete()
                        .eq("id", id)
                        .getQuery();
                postgrestClient.sendQuery(deleteDoctor);
                ((DefaultTableModel) tblDoctor.getModel()).removeRow(modelRow);
                System.out.println(doctors);
            }
        });

        btnLogoutDoctor.addActionListener(e -> {
            dispose();
            new WindowLogin();
        });

        this.add(tabAdmin);

        this.setVisible(true);
    }

    /**
     * Obtain patients from a jsonArray and add them to the patients list
     * @param jsonPatientData
     */
    public void obtainPatients(JsonArray jsonPatientData){
        for (JsonElement jsonElement : jsonPatientData) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            patients.add(new Patient(jsonObject));
        }
        mergeSort(patients);
    }

    /**
     * Obtain doctors from a jsonArray and add them to the doctors list
     * @param jsonDoctorIDs
     */
    public void obtainDoctors(JsonArray jsonDoctorIDs){
        for (JsonElement jsonElement : jsonDoctorIDs) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            doctors.add(new Doctor(jsonObject));
        }
        mergeSort(doctors);
    }

    /**
     * Customed DefaultTableModel to set editable columns
     */
    private class CustomTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return !(column == ID_COLUMN || column == SEX_COLUMN  || column == BIRTHDATE_COLUMN || column == SPECIALITY_COLUMN);
        }
    }

    /**
     * Create a complete DefaultTableModel with the list of users received as a parameter
     * @param columNames Header names
     * @param users Data that has to appear at the table
     * @return created model
     */
    public DefaultTableModel completeTable(String[] columNames, List<User> users) {
        DefaultTableModel model = new CustomTableModel();
        model.setColumnIdentifiers(columNames);
        if(!users.isEmpty()) {
            for (User user : users) {
                Object[] row = new String[columNames.length];
                row[ID_COLUMN] = user.getId();
                row[SURNAME1_COLUMN] = user.getSurname1();
                row[SURNAME2_COLUMN] = user.getSurname2();
                row[NAME_COLUMN] = user.getName();
                row[SEX_COLUMN] = user.getSex().toString();
                row[EMAIL_COLUMN] = user.getEmail();
                row[DNI_COLUMN] = user.getDni();
                row[AGE_COLUMN] = String.valueOf(user.getAgeInYears());
                row[PHONE_COLUMN] = user.getPhoneNumber();
                row[ADDRESS_COLUMN] = user.getAddress();
                row[BIRTHDATE_COLUMN] = user.getBirthDate().toString();
                if (users.get(0) instanceof Doctor){
                    Doctor doctor = (Doctor) user;
                    row[SPECIALITY_COLUMN] = doctor.getSpeciality();
                }
                model.addRow(row);
            }
        }
        return model;
    }

    /**
     * Validate data at the table at the moment of editing the table values
     * @param table Table edited
     * @param row   Number of row edited
     * @return true if the value is validate and if is not false
     */
    private boolean validateData(JTable table, int row) {
        String dni = table.getModel().getValueAt(row, DNI_COLUMN).toString();
        if (!dni.matches("\\d{8}[a-zA-Z]")) {
            JOptionPane.showMessageDialog(this, "El DNI introducido no es válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String phoneNumber = table.getModel().getValueAt(row, PHONE_COLUMN).toString();
        if (!phoneNumber.matches("(\\+34)?\\s?[6-9][0-9]{8}")) {
            JOptionPane.showMessageDialog(this, "El número de teléfono introducido no es válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String email = table.getModel().getValueAt(row, EMAIL_COLUMN).toString();
        if (!email.matches("(?!\\.)(?!.*\\.\\.)([\\w+-\\.]*)[\\w+-]@([a-zA-Z0-9][a-zA-Z0-9\\-]*\\.)+[a-zA-Z]{2,}")) {
            JOptionPane.showMessageDialog(this, "El email introducido no es válido", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * Filter data of a table by the text wrote at a JTextField
     * @param table Table that has to be filter
     * @param tfFinder TextField to obtein the text
     */
    private void filterData(JTable table, JTextField tfFinder) {
        String filter = tfFinder.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        TableRowSorter<? extends TableModel> rowSorter = (TableRowSorter<? extends TableModel>) table.getRowSorter();
        if (rowSorter == null) {
            rowSorter = new TableRowSorter<>(model);
            table.setRowSorter(rowSorter);
        }

        if (filter.trim().isEmpty()) {
            rowSorter.setRowFilter(null);
        } else {
            filter = filter.replace("+", "\\+");
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)^" + filter));
        }
    }

    /**
     * Sort a list of users recursively using an auxiliary method
     * @param users list of users that has to be sort
     */
    private void mergeSort(List<User> users) {
        if (users.size() <= 1) {
            return;
        }

        int midIndex = users.size() / 2;
        List<User> leftHalf = new ArrayList<>(users.subList(0, midIndex));
        List<User> rightHalf = new ArrayList<>(users.subList(midIndex, users.size()));

        mergeSort(leftHalf);
        mergeSort(rightHalf);

        merge(users, leftHalf, rightHalf);
    }

    /**
     * Auxiliary method to sort a list of users
     * @param users
     * @param leftHalf
     * @param rightHalf
     */
    private void merge(List<User> users, List<User> leftHalf, List<User> rightHalf) {
        int leftIndex = 0, rightIndex = 0, mergeIndex = 0;

        while (leftIndex < leftHalf.size() && rightIndex < rightHalf.size()) {
            if (leftHalf.get(leftIndex).getName().compareToIgnoreCase(rightHalf.get(rightIndex).getName()) < 0) {
                users.set(mergeIndex++, leftHalf.get(leftIndex++));
            } else {
                users.set(mergeIndex++, rightHalf.get(rightIndex++));
            }
        }

        while (leftIndex < leftHalf.size()) {
            users.set(mergeIndex++, leftHalf.get(leftIndex++));
        }

        while (rightIndex < rightHalf.size()) {
            users.set(mergeIndex++, rightHalf.get(rightIndex++));
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();

        ConfigLoader configLoader = new ConfigLoader();
        SwingUtilities.invokeLater(() -> new WindowAdmin(new SuperuserAuthenticationService(configLoader.getAnonymousToken(), configLoader.getSuperuserToken())));
    }
}


