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
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WindowAdmin extends UserAuthenticatedWindow {

    protected List<User> patients, doctors;
    protected JsonArray jsonPatientData, jsonDoctorData;

    protected JTabbedPane tabAdmin;

    protected JPanel pnlPatient, pnlDoctor;
    protected JTable tblPatient, tblDoctor;
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
        System.out.println(patients);

        pnlPatient = new JPanel(new BorderLayout());
        Border border = new TitledBorder("Tabla de pacientes:");
        pnlPatient.setBorder(border);

        String[] columNamesPatients = {"ID", "Apellidos", "Nombre", "Sexo", "Email", "DNI", "Edad", "Teléfono", "Dirección", "Fecha de nacimiento"};
        mdlPatient = completeTable(columNamesPatients, patients);
        tblPatient = new JTable(mdlPatient);
        tblPatient.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(3).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(6).setPreferredWidth(25);
        tblPatient.setRowHeight(25);

        scrPatient = new JScrollPane(tblPatient);
        tfFindPatient = new JTextField();
        tfFindPatient.setPreferredSize(new Dimension(200, 25));
        btnPatient = new JButton("Add");
        btnEditPatient = new JButton("Edit");
        btnDeletePatient = new JButton("Delete");
        btnLogoutPatient = new JButton("Logout");

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

        tabAdmin.addTab("Usuarios", pnlPatient);

        btnPatient.addActionListener(e -> new WindowAddUser(patients));

        btnEditPatient.addActionListener(e -> { //Error con el valor birthdate
            int selectedRow = tblPatient.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = tblPatient.convertRowIndexToModel(selectedRow);

                // Obtén los datos asociados a la fila
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

                if (indexInListP != -1) {
                    // Crea un nuevo objeto Patient con los valores editados
                    Patient editedPatient = new Patient(
                            originalPatient.getId(),                    //id
                            rowData[2].toString(),                      //name
                            rowData[1].toString().split(" ")[0],  //surname1
                            rowData[1].toString().split(" ")[1],  //surname2
                            (LocalDate) rowData[9],                     //birthdate
                            originalPatient.getSex(),                   //sex
                            rowData[5].toString(),                      //dni
                            rowData[4].toString(),                      //email
                            rowData[7].toString(),                      //phone
                            rowData[8].toString(),                      //address
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
                System.out.println("Paciente eliminado");
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
        System.out.println(doctors);

        pnlDoctor = new JPanel(new BorderLayout());
        border = new TitledBorder("Tabla de doctores:");
        pnlDoctor.setBorder(border);

        String[] columNamesDoctor = {"ID", "Apellidos", "Nombre", "Sexo", "Email", "DNI", "Edad", "Teléfono", "Dirección", "Fecha de nacimiento", "Especialidad"};
        mdlDoctor = completeTable(columNamesDoctor, doctors);
        tblDoctor = new JTable(mdlDoctor);
        tblDoctor.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblDoctor.getColumnModel().getColumn(3).setPreferredWidth(25);
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

        btnDoctor.addActionListener(e -> new WindowAddUser(doctors));

        btnEditDoctor.addActionListener(e -> { //Arreglar detecta linea erronea
            int selectedRow = tblDoctor.getSelectedRow();
            if (selectedRow != -1) {
                System.out.println(selectedRow);
                int modelRow = tblDoctor.convertRowIndexToModel(selectedRow );
                // Obtén los datos asociados a la fila
                Object[] rowData = new Object[tblPatient.getColumnCount()];
                for (int i = 0; i < tblPatient.getColumnCount(); i++) {
                    rowData[i] = tblPatient.getModel().getValueAt(selectedRow, i);
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
                if (indexInListD != -1) {
                    // Crea un nuevo objeto Patient con los valores editados
                    Doctor editedDoctor = new Doctor(
                            originalDoctor.getId(),                       //id
                            rowData[2].toString(),                        //name
                            rowData[1].toString().split(" ")[0],    //surname1
                            rowData[1].toString().split(" ")[1],    //surname2
                            originalDoctor.getBirthDate(),                //birthdate
                            originalDoctor.getSex(),                      //sex
                            rowData[5].toString(),                        //dni
                            rowData[6].toString(),                        //email
                            originalDoctor.getPhoneNumber(),              //phone
                            rowData[8].toString(),                        //address
                            originalDoctor.getSpeciality(),               //speciality
                            originalDoctor.getAppointments());


                    // Actualiza el objeto en la lista
                    doctors.set(indexInListD, editedDoctor);

                    // Actualiza la fila en la tabla
                    for (int i = 1; i < rowData.length; i++) {
                        tblDoctor.getModel().setValueAt(rowData[i], modelRow, i);
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

        btnLogoutDoctor.addActionListener(e -> dispose());



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

    public DefaultTableModel completeTable(String[] columNames, List<User> users) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columNames);
        if(!users.isEmpty()) {
            for (User user : users) {
                Object[] row = new String[columNames.length];
                row[0] = String.valueOf(user.getId());
                row[1] = user.getSurname1() + " " + user.getSurname2();
                row[2] = user.getName();
                row[3] = user.getSex().toString();
                row[4] = user.getEmail();
                row[5] = user.getDni();
                row[6] = String.valueOf(user.getAgeInYears());
                row[7] = user.getPhoneNumber();
                row[8] = user.getAddress();
                row[9] = user.getBirthDate().toString();
                if (users.get(0) instanceof Doctor){
                    Doctor doctor = (Doctor) user;
                    row[10] = doctor.getSpeciality();
                }
                model.addRow(row);
            }
        }
        return model;
    }
}


