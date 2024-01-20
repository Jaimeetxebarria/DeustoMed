package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.toedter.calendar.JDateChooser;
import org.deustomed.*;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.authentication.BypassTrustManager;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class WindowAddUser extends JFrame {
    // General
    JLabel lblName, lblSurname1, lblSurname2, lblSex, lblEmail, lblDni, lblError, lblAge, lblPhone, lblAddress, lblBirthDate, lblPassword
            , lblSpeciality;
    JTextField tfName, tfSurname1, tfSurname2, tfEmail, tfDni, tfAge, tfPhone, tfAddress;
    JPasswordField pfPassword;
    ButtonGroup group;
    JRadioButton radMale, radFemale;
    JButton btnSave, btnCancel;
    List<User> patients, doctors;
    JDateChooser dateChooser;
    Date previousDate;
    JComboBox<String> cbSpeciality;
    protected List<String> specialities = new ArrayList<>();

    private static PostgrestClient postgrestClient;
    private static final Gson gson = new Gson();

    public WindowAddUser(List<? extends User> users, JTable tblUsers) {
        ConfigLoader configLoader = new ConfigLoader();
        String hostname = configLoader.getHostname();
        String endpoint = configLoader.getEndpoint();
        String anonymousToken = configLoader.getAnonymousToken();
        postgrestClient = new PostgrestClient(hostname, endpoint, new AnonymousAuthenticationService(anonymousToken));

        if (users.get(0) instanceof Patient) {
            patients = users.stream().map(user -> (Patient) user).collect(Collectors.toList());
            setTitle("New patient");
        } else {
            doctors = users.stream().map(user -> (Doctor) user).collect(Collectors.toList());
            setTitle("New Doctor");
        }

        setLayout(new BorderLayout());
        //setResizable(false);
        setSize(400, 390);
        setLocationRelativeTo(null);

        lblName = new JLabel("*Nombre:");
        lblSurname1 = new JLabel("*Apellido 1:");
        lblSurname2 = new JLabel("*Apellido 2:");
        lblSex = new JLabel("*Sexo:");
        lblEmail = new JLabel("Email:");
        lblDni = new JLabel("DNI:");
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);

        tfName = new JTextField();
        tfSurname1 = new JTextField();
        tfSurname2 = new JTextField();
        radFemale = new JRadioButton("Mujer");
        radMale = new JRadioButton("Hombre");
        group = new ButtonGroup();
        group.add(radFemale);
        group.add(radMale);
        tfEmail = new JTextField();
        tfDni = new JTextField();
        btnSave = new JButton("Crear");
        btnCancel = new JButton("Cancelar");
        lblAge = new JLabel("Edad:");
        lblPhone = new JLabel("Teléfono:");
        lblAddress = new JLabel("Dirección:");
        lblBirthDate = new JLabel("*Fecha de nacimiento:");
        lblPassword = new JLabel("*Contraseña:");

        //JDateChooser
        Date currentDate = new Date();
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy MMMM dd");
        dateChooser.setDate(currentDate);
        dateChooser.setMaxSelectableDate(currentDate);
        dateChooser.setMinSelectableDate(new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime());
        dateChooser.setToolTipText("Seleciona una fecha de nacimiento");
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) updateAge();
        });


        tfAge = new JTextField();
        tfAge.setEditable(false);
        tfAge.setText(String.valueOf(getAge(dateChooser.getDate())));
        tfPhone = new JTextField();
        tfPhone.setToolTipText("El teléfono debe tener el siguiente formato: +123456789");
        tfAddress = new JTextField();
        pfPassword = new JPasswordField();

        if (doctors != null) {
            lblSpeciality = new JLabel("Especialidad:");
            cbSpeciality = new JComboBox<>();

            PostgrestQuery specialityQuery = postgrestClient
                    .from("speciality")
                    .select("name")
                    .getQuery();

            List<String> specialitiesFromDb = postgrestClient.sendQuery(specialityQuery)
                    .getAsJsonArray()
                    .asList()
                    .stream()
                    .map(jsonElement -> jsonElement.getAsJsonObject().get("name").getAsString())
                    .toList();

            specialities.addAll(specialitiesFromDb);
            cbSpeciality.setModel(new DefaultComboBoxModel<>(specialities.toArray(new String[0])));
        }

        //Add components to the window
        JPanel pnlCenter = new JPanel(new BorderLayout());

        JPanel pnlPrimary = new JPanel();
        if (patients != null) {
            pnlPrimary.setLayout(new GridLayout(11, 2, 5, 5));
        } else if (doctors != null) {
            pnlPrimary.setLayout(new GridLayout(12, 2, 5, 5));
        }
        pnlPrimary.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        pnlPrimary.add(lblName);
        pnlPrimary.add(tfName);
        pnlPrimary.add(lblSurname1);
        pnlPrimary.add(tfSurname1);
        pnlPrimary.add(lblSurname2);
        pnlPrimary.add(tfSurname2);
        pnlPrimary.add(lblSex);
        Panel pnlSex = new Panel(new GridLayout(1, 2));
        pnlSex.add(radMale);
        pnlSex.add(radFemale);
        pnlPrimary.add(pnlSex);
        pnlPrimary.add(lblEmail);
        pnlPrimary.add(tfEmail);
        pnlPrimary.add(lblDni);
        pnlPrimary.add(tfDni);
        pnlPrimary.add(lblBirthDate);
        pnlPrimary.add(dateChooser);
        pnlPrimary.add(lblAge);
        pnlPrimary.add(tfAge);
        pnlPrimary.add(lblPhone);
        pnlPrimary.add(tfPhone);
        pnlPrimary.add(lblAddress);
        pnlPrimary.add(tfAddress);
        pnlPrimary.add(lblPassword);
        pnlPrimary.add(pfPassword);

        if (doctors != null) {
            pnlPrimary.add(lblSpeciality);
            pnlPrimary.add(cbSpeciality);
        }

        pnlCenter.add(pnlPrimary, BorderLayout.CENTER);
        pnlCenter.add(lblError, BorderLayout.SOUTH);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));
        pnlSouth.add(btnSave, BorderLayout.EAST);
        pnlSouth.add(btnCancel, BorderLayout.WEST);

        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnSave.addActionListener(e -> {
            String name = tfName.getText();
            String surname1 = tfSurname1.getText();
            String surname2 = tfSurname2.getText();
            String email = obtainValueOrNull(tfEmail.getText());
            String dni = obtainValueOrNull(tfDni.getText());
            ButtonModel selectedSex = group.getSelection();
            LocalDate birthDate = LocalDate.ofInstant(dateChooser.getDate().toInstant(), ZoneId.systemDefault());
            birthDate.format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String phone = obtainValueOrNull(tfPhone.getText());
            String address = obtainValueOrNull(tfAddress.getText());
            Sex sex;

            if (selectedSex.equals(radMale.getModel())) {
                sex = Sex.MALE;
            } else {
                sex = Sex.FEMALE;
            }

            if (!validateData()) return;

            JsonObject person = new JsonObject();
            person.addProperty("userType", patients != null ? "patient" : "doctor");
            person.addProperty("name", name);
            person.addProperty("surname1", surname1);
            person.addProperty("surname2", surname2);
            person.addProperty("birthDate", String.valueOf(birthDate));
            person.addProperty("sex", String.valueOf(sex));
            if (!new String(pfPassword.getPassword()).isBlank()) person.addProperty("password", new String(pfPassword.getPassword()));
            if (!tfDni.getText().isBlank()) person.addProperty("dni", dni);
            if (!tfEmail.getText().isBlank()) person.addProperty("email", email);
            if (!tfPhone.getText().isBlank()) person.addProperty("phone", phone);
            if (!tfAddress.getText().isBlank()) person.addProperty("address", address);
            if (doctors != null) person.addProperty("speciality", cbSpeciality.getSelectedItem().toString());

            SSLContext sslContext;
            try {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{new BypassTrustManager()}, new SecureRandom());
            } catch (NoSuchAlgorithmException | KeyManagementException err) {
                throw new RuntimeException(err);
            }

            HttpClient authServerClient = HttpClient.newBuilder()
                    .sslContext(sslContext)
                    .build();

            HttpRequest signUpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(configLoader.getAuthServerBaseUrl() + "/signup"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(person.toString()))
                    .build();

            HttpResponse<String> response;
            try {
                response = authServerClient.send(signUpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException ex) {
                lblError.setText("Error de conexión con el servidor");
                return;
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }

            String userId;
            switch (response.statusCode()) {
                case 200 -> {
                    JsonObject responseJson = gson.fromJson(response.body(), JsonObject.class);
                    userId = responseJson.get("id").getAsString();
                }
                case 400 -> {
                    if (response.body().equals("User already exists")) lblError.setText("El usuario ya existe");
                    else lblError.setText("Error de formato en los datos");
                    return;
                }
                case 500 -> {
                    lblError.setText("Error del servidor");
                    return;
                }
                default -> throw new RuntimeException("Unexpected response code: " + response.statusCode());
            }

            DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
            String[] rowData;
            if (patients != null) {
                //Add to the patient arraylist
                Patient patient = new Patient(userId, name, surname1, surname2, birthDate, sex, dni, email, phone,
                        address, new ArrayList<>());
                patients.add(patient);
                //Update table
                model.addRow(new String[]{userId, surname1, surname2, name, sex.toString(), email, dni, tfAge.getText(), phone, address, String.valueOf(birthDate)});
                new WindowConfirmNewUser(patient);
                System.out.println("Nuevo paciente: " + patient);
                dispose();
            } else if (doctors != null) {
                //Add to the doctor arraylist
                String speciality = ((String) cbSpeciality.getSelectedItem()).toString();
                Doctor doctor;
                doctor = new Doctor(userId, name, surname1, surname2, birthDate, sex, dni, email, phone,
                        address, speciality, new ArrayList<>());
                doctors.add(doctor);
                //Update table
                model.addRow(new String[]{userId, surname1, surname2, name, sex.toString(), email, dni, tfAge.getText(), phone, address, String.valueOf(birthDate), speciality});
                new WindowConfirmNewUser(doctor);
                System.out.println("Nuevo doctor: " + doctor);
                dispose();
            }
        });

        btnCancel.addActionListener(e -> dispose());

        setVisible(true);
    }

    /**
     * If the gap is empty, returns null, else returns the value
     *
     * @param value
     *
     * @return null if the value is empty, else the value
     */
    private String obtainValueOrNull(String value) {
        if (value.isEmpty()) {
            return null;
        } else {
            return value;
        }
    }

    /**
     * Creates centered JLabels
     *
     * @param text JLabel string content
     */
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    /**
     * Validates the data introduced by the user
     *
     * @return boolean indicating if the data is valid or not
     */
    private boolean validateData() {
        if (tfName.getText().isEmpty() ||
                tfSurname1.getText().isBlank() ||
                tfSurname2.getText().isBlank() ||
                group.getSelection() == null ||
                dateChooser.getDate() == null ||
                new String(pfPassword.getPassword()).isBlank()) {
            lblError.setText("Hay campos obligatorios sin rellenar. Recuerde que los que tienen un '*' son obligatorios");
            return false;
        }

        // Validate dni
        String dni = tfDni.getText().trim();
        if (!dni.isEmpty() && !dni.matches("\\d{8}[a-zA-Z]")) {
            lblError.setText("DNI inválido");
            return false;
        }

        // Validate email
        String email = tfEmail.getText().trim();
        if (!email.isEmpty() &&
                !email.matches("(?!\\.)(?!.*\\.\\.)([\\w+-\\.]*)[\\w+-]@([a-zA-Z0-9][a-zA-Z0-9\\-]*\\.)+[a-zA-Z]{2,}")) {
            lblError.setText("Email inválido");
            return false;
        }

        // Validate phone number (only spanish numbers)
        String phoneNumber = tfPhone.getText().trim();
        if (!phoneNumber.isEmpty() && !phoneNumber.matches("(\\+34)?\\s?[6-9][0-9]{8}")) {
            lblError.setText("Numero de teléfono inválido");
            return false;
        }

        // Validate password
        String password = new String(pfPassword.getPassword()).trim();
        boolean isCorrectSize = password.length() >= 8 && password.length() <= 20;
        boolean hasLetters = password.matches(".*[a-zA-Z]+.*");
        boolean hasNumbers = password.matches(".*[0-9]+.*");
        boolean hasSpecialCharacters = password.matches(".*[^a-zA-Z0-9]+.*");
        if (!isCorrectSize || !hasLetters || !hasNumbers || !hasSpecialCharacters) {
            lblError.setText("Contraseña inválida");
            return false;
        }

        if (doctors != null && cbSpeciality.getSelectedItem().toString().isEmpty()) {
            lblError.setText("Se requiere definir una especialidad");
            return false;
        }

        return true;
    }

    /**
     * Updates the age textfield with the age calculated from the birthdate textfield
     */
    private void updateAge() {
        Date birthday = dateChooser.getDate();

        int age = getAge(birthday);
        if (age >= 0 && age <= 120) {
            tfAge.setText(String.valueOf(age));
            previousDate = birthday;
        } else {
            JOptionPane.showMessageDialog(null, "Fecha de nacimiento inválida");
            SwingUtilities.invokeLater(() -> {
                dateChooser.setDate(previousDate);
            });
        }
    }

    /**
     * Calculates the age of a user given the birthdate and the current date
     *
     * @param birthday The birthday of the user
     *
     * @return
     */
    private int getAge(Date birthday) {
        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();
        birthDate.setTime(birthday);

        int age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        return age;
    }

    public static void main(String[] args) {
        List<User> patients = new ArrayList<>();
        patients.add(new Patient("00AAA", "Pablo", "Garcia", "Iglesias",
                LocalDate.of(1990, 8, 12), Sex.MALE, "dni1",
                "email1", "phone1", "address1", null));
        patients.add(new Patient("00AAB", "Andoni", "Hernández", "Ruiz",
                LocalDate.of(1975, 3, 1), Sex.MALE, "email2", "dni2",
                "phone2", "address2", null));

        List<User> doctors = new ArrayList<>();
        doctors.add(new Doctor("00AAA", "Pablo", "Garcia", "Iglesias",
                LocalDate.of(1990, 8, 12), Sex.MALE, "dni1",
                "email1", "phone1", "address1", null, null));
        doctors.add(new Doctor("00AAB", "Andoni", "Hernández", "Ruiz",
                LocalDate.of(1975, 3, 1), Sex.MALE, "email2", "dni2",
                "phone2", "address2", null, null));

        FlatLightLaf.setup();
        FlatInterFont.install();

        SwingUtilities.invokeLater(() -> {
            //new WindowAddUser(patients);
            //new WindowAddUser(doctors);
        });
    }
}
