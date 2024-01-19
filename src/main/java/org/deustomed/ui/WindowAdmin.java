package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.deustomed.*;
import org.deustomed.authentication.AnonymousAuthenticationService;
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
    protected  final int SEX_COLUMN = 4;
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
    static final Gson gson = new Gson();

    public WindowAdmin(PostgrestAuthenticationService authenticationService) {
        super(authenticationService instanceof UserAuthenticationService ? (UserAuthenticationService) authenticationService : null);

        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        postgrestClient = new PostgrestClient(hostname, endpoint, authenticationService);

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
                .from("patient_with_personal_data")
                .select("*")
                .getQuery();
        String jsonResponseP = String.valueOf(postgrestClient.sendQuery(queryPatient));
        jsonPatientData = gson.fromJson(jsonResponseP, JsonArray.class);
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
                filtrarDatos(tblPatient, tfFindPatient);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarDatos(tblPatient, tfFindPatient);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarDatos(tblPatient, tfFindPatient);
            }
        });
        tblPatient.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tblPatient.getTableHeader().columnAtPoint(e.getPoint());
                if (sorterPatients.getSortKeys().isEmpty() || sorterPatients.getSortKeys().get(0).getColumn() != column) {
                    sorterPatients.setSortKeys(null);
                } else {
                    // Toggle sorting order
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

        btnPatient.addActionListener(e -> new WindowAddUser(patients, tblPatient));

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
                    // Crea un nuevo objeto Patient con los valores editados
                    Patient editedPatient = new Patient(
                            originalPatient.getId(),                    //id
                            rowData[3].toString(),                      //name
                            rowData[1].toString(),                      //surname1
                            rowData[2].toString(),                      //surname2
                            LocalDate.parse(rowData[9].toString()),     //birthdate
                            originalPatient.getSex(),                   //sex
                            rowData[6].toString(),                      //dni
                            rowData[5].toString(),                      //email
                            rowData[8].toString(),                      //phone
                            rowData[9].toString(),                      //address
                            originalPatient.getMedicalRecord());        //medicalRecord


                    // Actualiza el objeto en la lista
                    patients.set(indexInListP, editedPatient);

                    // Actualiza la fila en la tabla
                    for (int i = 1; i < rowData.length; i++) {
                        tblPatient.getModel().setValueAt(rowData[i], modelRow, i);
                    }

                    //Actualizar los valores del paciente en la base de datos
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

                    System.out.println("Paciente con id " + originalPatient.getId() + " editado");
                }
            }
        });

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
                System.out.println("Paciente con id " + id + " eliminado");
                ((DefaultTableModel) tblPatient.getModel()).removeRow(modelRow);
            }
        });

        btnLogoutPatient.addActionListener(e -> dispose());

        //Doctor
        PostgrestQuery queryDoctor = postgrestClient
                .from("doctor_with_personal_data")
                .select("*")
                .getQuery();
        String jsonResponse = String.valueOf(postgrestClient.sendQuery(queryDoctor));
        jsonDoctorData = gson.fromJson(jsonResponse, JsonArray.class);
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

        tfFindDoctor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                filtrarDatos(tblDoctor, tfFindDoctor);
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                filtrarDatos(tblDoctor, tfFindDoctor);
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                filtrarDatos(tblDoctor, tfFindDoctor);
            }
        });
        tblDoctor.getTableHeader().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = tblDoctor.getTableHeader().columnAtPoint(e.getPoint());
                if (sorterDoctors.getSortKeys().isEmpty() || sorterDoctors.getSortKeys().get(0).getColumn() != column) {
                    sorterDoctors.setSortKeys(null); // No sorting, revert to original order
                } else {
                    // Toggle sorting order
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

        btnDoctor.addActionListener(e -> new WindowAddUser(doctors, tblDoctor));

        btnEditDoctor.addActionListener(e -> {
            int selectedRow = tblDoctor.getSelectedRow();
            if (selectedRow != -1) {
                // Obtén los datos asociados a la fila
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
                System.out.println(rowData[0]);
                System.out.println(originalDoctor);
                int indexInListD = doctors.indexOf(originalDoctor);
                if (indexInListD != -1 && validateData(tblDoctor, selectedRow)) {
                    // Crea un nuevo objeto Patient con los valores editados
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


                    // Actualiza el objeto en la lista
                    doctors.set(indexInListD, editedDoctor);

                    // Actualiza la fila en la tabla
                    for (int i = 1; i < rowData.length; i++) {
                        tblDoctor.getModel().setValueAt(rowData[i], selectedRow, i);
                    }

                    //Actualizar los valores del doctor en la base de datos
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
                    System.out.println("Doctor con id " + originalDoctor.getId() + " editado");
                }
            }
        });

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
                System.out.println("Doctor eliminado");
                ((DefaultTableModel) tblDoctor.getModel()).removeRow(modelRow);
                System.out.println(doctors);
            }
        });

        btnLogoutDoctor.addActionListener(e -> {
            dispose();
            new WindowLogin();
        });



        //Logs
        pnlLogs = new JPanel(new BorderLayout());

        this.add(tabAdmin);

        this.setVisible(true);
    }

    public void obtainPatients(JsonArray jsonPatientData){
        for (JsonElement jsonElement : jsonPatientData) {
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
            patients.add(new Patient(id, name, surname1, surname2, birthDate, sex, dni, email, phoneNumber, address, new ArrayList<>()));
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
        FlatLightLaf.setup();
        FlatInterFont.install();

        ConfigLoader configLoader = new ConfigLoader();
        SwingUtilities.invokeLater(() -> new WindowAdmin(new AnonymousAuthenticationService(configLoader.getAnonymousToken())));
    }
    private class CustomTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int row, int column) {
            return !(column == ID_COLUMN || column == SEX_COLUMN  || column == BIRTHDATE_COLUMN);
        }
    }
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

    private void filtrarDatos(JTable table, JTextField tfFinder) {
        String filter = tfFinder.getText().toLowerCase();
        DefaultTableModel model = (DefaultTableModel) table.getModel();

        TableRowSorter<TableModel> rowSorter = (TableRowSorter<TableModel>) table.getRowSorter();
        if (rowSorter == null) {
            rowSorter = new TableRowSorter<>(model);
            table.setRowSorter(rowSorter);
        }

        if (filter.trim().length() == 0) {
            rowSorter.setRowFilter(null);
        } else {
            rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + filter));
        }
    }

}


