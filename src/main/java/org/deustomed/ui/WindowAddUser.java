package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.toedter.calendar.JDateChooser;
import org.deustomed.*;
import org.deustomed.authentication.AnonymousAuthenticationService;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class WindowAddUser extends JFrame {
    // General
    JLabel lblName, lblSurname1, lblSurname2, lblSex, lblEmail, lblDni, lblError, lblAge, lblPhone, lblAddress, lblBirthDate, lblSpeciality;
    JTextField tfName, tfSurname1, tfSurname2, tfEmail, tfDni, tfAge, tfPhone, tfAddress;
    ButtonGroup group;
    JRadioButton radMale, radFemale;
    JButton btnSave;
    List<User> patients, doctors;
    JDateChooser dateChooser;
    Date previousDate;
    JComboBox<String> cbSpeciality;
    protected JsonArray jsonSpeciality;
    protected List<String> specialities = new ArrayList<>();

    private static PostgrestClient postgrestClient;
    private static final Gson gson = new Gson();

    public WindowAddUser(List<User> users) {
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

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        //setResizable(false);
        setLocationRelativeTo(null);
        setSize(300, 550);

        lblName = createCenteredLabel("*Nombre:");
        lblSurname1 = createCenteredLabel("*Apellido 1:");
        lblSurname2 = createCenteredLabel("*Apellido 2:");
        lblSex = createCenteredLabel("*Sexo:");
        lblEmail = createCenteredLabel("Email:");
        lblDni = createCenteredLabel("DNI:");
        lblError = createCenteredLabel("");
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
        lblAge = new JLabel("Edad:");
        lblPhone = new JLabel("Teléfono:");
        lblAddress = new JLabel("Dirección:");
        lblBirthDate = new JLabel("*Fecha de nacimiento:");

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
        tfAddress.setToolTipText("La dirección debe tener el siguiente formato: Calle número(3 digitos), ciudad\nEjemplo: Circle 678, " +
                "Town");

        if (doctors != null) {
            lblSpeciality = createCenteredLabel("Especialidad:");
            cbSpeciality = new JComboBox<>();
            PostgrestQuery specialityQuery = postgrestClient
                    .from("speciality")
                    .select("name")
                    .getQuery();
            String jsonResponse = String.valueOf(postgrestClient.sendQuery(specialityQuery));
            jsonSpeciality = gson.fromJson(jsonResponse, JsonArray.class);
            for (JsonElement speciality : jsonSpeciality) {
                JsonObject specialityObj = speciality.getAsJsonObject();
                specialities.add(specialityObj.get("name").getAsString());
            }
            cbSpeciality.setModel(new DefaultComboBoxModel<>(specialities.toArray(new String[0])));
        }

        //Add components to the window
        JPanel pnlCenter = new JPanel(new BorderLayout());

        JPanel pnlPrimary = new JPanel();
        if (patients != null) {
            pnlPrimary.setLayout(new GridLayout(10, 2));
        } else if (doctors != null) {
            pnlPrimary.setLayout(new GridLayout(11, 2));
        }

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

        if (doctors != null) {
            pnlPrimary.add(lblSpeciality);
            pnlPrimary.add(cbSpeciality);
        }

        pnlCenter.add(pnlPrimary, BorderLayout.CENTER);
        pnlCenter.add(lblError, BorderLayout.SOUTH);

        JPanel pnlSouth = new JPanel(new FlowLayout(FlowLayout.CENTER));
        pnlSouth.add(btnSave);

        this.add(pnlCenter, BorderLayout.CENTER);
        this.add(pnlSouth, BorderLayout.SOUTH);

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

                if (selectedSex.equals(radMale)) {
                    sex = Sex.MALE;
                } else {
                    sex = Sex.FEMALE;
                }

                if (validateData()) {
                    //Add user to person table (añadir id generado)
                    JsonObject person = new JsonObject();
                    person.addProperty("name", name);
                    person.addProperty("surname1", surname1);
                    person.addProperty("surname2", surname2);
                    person.addProperty("dni", dni);
                    person.addProperty("birthdate", String.valueOf(birthDate));
                    person.addProperty("email", email);
                    person.addProperty("phone", phone);
                    person.addProperty("address", address);
                    person.addProperty("sex", String.valueOf(sex));
                    person.addProperty("age", getAge(dateChooser.getDate()));
                    //PostgrestTransformBuilder qb1 = getBlankPostgrestQueryBuilder().insert(person);
                    //assertPathnameEquals("/table", qb1.getQuery());

                    if (patients != null) {
                        //Add user to patient table DB

                        //Add to the patient arraylist
                        String id = "" + (patients.size() + 1);
                        Patient patient = new Patient(id, name, surname1, surname2, birthDate, sex, dni, email, phone,
                                address, new ArrayList<>());
                        patients.add(patient);
                        new WindowConfirmNewUser(patients);
                        System.out.println("Nuevo paciente: " + patient);
                        dispose();
                    } else if (doctors != null) {
                        //Add user to doctor table DB

                        //Add to the doctor arraylist
                        String speciality = ((String) cbSpeciality.getSelectedItem()).toString();
                        String id = "" + doctors.size() + 1;
                        // TODO: 19/12/23 diferenciar entre médicos de familia y especialistas;
                        // TODO: 19/12/23 en este ejemplo es médico de familia, por los que la especialidad es predeterminada
                        Doctor doctor;
                        if (speciality.equals("Medicina Familiar")) {
                            doctor = new FamilyDoctor(id, name, surname1, surname2, birthDate, sex,
                                    dni, email, phone, address, new ArrayList<>(), new ArrayList<>());
                        } else {
                            doctor = new SpecialistDoctor(id, name, surname1, surname2, birthDate, sex, dni, email, phone, address,
                                    new ArrayList<>(), speciality, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
                        }
                        doctors.add(doctor);
                        new WindowConfirmNewUser(doctors);
                        System.out.println("Nuevo doctor: " + doctor);
                        dispose();
                    }
                }


            }
        });

        setVisible(true);
    }

    /**
     * If the gap is empty, returns null, else returns the value
     *
     * @param value
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
                tfSurname1.getText().isEmpty() ||
                tfSurname2.getText().isEmpty() ||
                group.getSelection() == null) {
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
            new WindowAddUser(doctors);
        });
    }
}
