package org.deustomed.ui;

import com.toedter.calendar.JDateChooser;
import org.deustomed.*;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class WindowAddUser extends JFrame {

    // General
    JLabel lblId, lblName, lblSurname1, lblSurname2, lblSex, lblEmail, lblDni, lblError;
    JTextField tfId, tfName, tfSurname1, tfSurname2, tfEmail, tfDni;
    JRadioButton radMale, radFemale;
    JButton btnSave;

    // For patient
    List<User> patients;
    JLabel lblAge, lblPhone, lblAddress, lblBirthDate;
    JTextField tfAge, tfPhone, tfAddress;
    JDateChooser dateChooser;
    Date previousDate;

    //For doctor
    List<User> doctors;
    JLabel lblSpeciality;
    JComboBox<String> cbSpeciality;
    List<String> specialities = Arrays.asList("Alergología", "Anestesiología", "Angiología", "Cardiología", "Endocrinología",
            "Gastroenterología", "Geriatría", "Hematología", "Infectología", "Medicina interna", "Nefrología", "Neumología", "Neurología"
            , "Obstetricia", "Oftalmología", "Oncología", "Pediatría", "Psiquiatría", "Reumatología", "Toxicología", "Urología");



    public WindowAddUser(List<User> users) {
        if(users.get(0) instanceof Patient){
            patients = users.stream().map(user -> (Patient) user).collect(Collectors.toList());
            setTitle("New patient");
        }else{
            doctors = users.stream().map(user -> (Doctor) user).collect(Collectors.toList());
            setTitle("New Doctor");
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        //setResizable(false);
        setLocationRelativeTo(null);
        setSize(300, 550);

        lblName = createCenteredLabel("Nombre:");
        lblSurname1 = createCenteredLabel("Apellido 1:");
        lblSurname2 = createCenteredLabel("Apellido 2:");
        lblSex = createCenteredLabel("Sexo:");
        lblEmail = createCenteredLabel("Email:");
        lblDni = createCenteredLabel("DNI:");
        lblError = createCenteredLabel("");
        lblError.setForeground(Color.RED);

        tfId.setEditable(false);


        tfName = new JTextField();
        tfSurname1 = new JTextField();
        tfSurname2 = new JTextField();
        radFemale = new JRadioButton("Mujer");
        radMale = new JRadioButton("Hombre");
        ButtonGroup group = new ButtonGroup();
        group.add(radFemale);
        group.add(radMale);
        tfEmail = new JTextField();
        tfDni = new JTextField();
        btnSave = new JButton("Crear");

        if (patients != null) {
            lblAge = new JLabel("Age:");
            lblPhone = new JLabel("Phone:");
            lblAddress = new JLabel("Address:");
            lblBirthDate = new JLabel("Birthdate:");

            //JDateChooser
            Date currentDate = new Date();
            dateChooser = new JDateChooser();
            dateChooser.setDateFormatString("dd MMMM yyyy");
            dateChooser.setDate(currentDate);
            dateChooser.setMaxSelectableDate(currentDate);
            dateChooser.setMinSelectableDate(new GregorianCalendar(1900, Calendar.JANUARY, 1).getTime());
            dateChooser.setToolTipText("Select birthdate");
            dateChooser.getDateEditor().addPropertyChangeListener(e -> {
                if ("date".equals(e.getPropertyName())) updateAge();
            });


            tfAge = new JTextField();
            tfAge.setEditable(false);
            tfAge.setText(String.valueOf(getAge(dateChooser.getDate())));
            tfPhone = new JTextField();
            tfPhone.setToolTipText("Phone number should have 9 digits");
            tfAddress = new JTextField();
            tfAddress.setToolTipText("Address should be in the format: Street, number, city, province, country\nExample: Sabino Arana, 15, 2A, Bilbao, Biscay, Spain");


        } else if (doctors != null) {
            lblSpeciality = createCenteredLabel("Specialidad:");
            cbSpeciality = new JComboBox<>();
            cbSpeciality.setModel(new DefaultComboBoxModel<>(specialities.toArray(new String[0])));

        }

        //Add components to the window
        JPanel pnlCenter = new JPanel(new BorderLayout());

        JPanel pnlPrimary = new JPanel();
        if (patients != null) {
            pnlPrimary.setLayout(new GridLayout(10, 2));
        } else if (doctors != null) {
            pnlPrimary.setLayout(new GridLayout(9, 2));
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
        if (patients != null) {
            pnlPrimary.add(lblBirthDate);
            pnlPrimary.add(dateChooser);
            pnlPrimary.add(lblAge);
            pnlPrimary.add(tfAge);
            pnlPrimary.add(lblPhone);
            pnlPrimary.add(tfPhone);
            pnlPrimary.add(lblAddress);
            pnlPrimary.add(tfAddress);
        } else if (doctors != null) {
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
                String email = tfEmail.getText();
                String dni = tfDni.getText();
                ButtonModel selectedSex = group.getSelection();
                Sex sex;
                if (selectedSex != null) {
                    if (selectedSex.equals(radMale)) {
                        sex = Sex.MALE;
                    } else {
                        sex = Sex.FEMALE;
                    }

                    if (patients != null) {
                        LocalDate birthDate = LocalDate.ofInstant(dateChooser.getDate().toInstant(), ZoneId.systemDefault());
                        String phone = tfPhone.getText();
                        String address = tfAddress.getText();
                        if (validateData()) {
                            String id = "" + (patients.size() + 1);
                            Patient patient = new Patient(id, name, surname1, surname2, birthDate, sex, dni, email, phone,
                                    address, new ArrayList<>());
                            patients.add(patient);
                            new WindowConfirmNewUser(patients);
                            System.out.println("Nuevo paciente: " + patient);
                            dispose();
                        }


                    } else if (doctors != null) {
                        String speciality = ((String) cbSpeciality.getSelectedItem()).toString();
                        LocalDate birthDate = LocalDate.ofInstant(dateChooser.getDate().toInstant(), ZoneId.systemDefault());
                        String phone = tfPhone.getText();
                        String address = tfAddress.getText();
                        if (validateData()) {
                            String id = "" + doctors.size() + 1;
                            // TODO: 19/12/23 diferenciar entre médicos de familia y especialistas;
                            // TODO: 19/12/23 en este ejemplo es médico de familia, por los que la especialidad es predeterminada
                            Doctor doctor = new FamilyDoctor(id, name, surname1, surname2, birthDate, sex,
                                    dni, email, phone, address, new ArrayList<>(), new ArrayList<>());
                            doctors.add(doctor);
                            JOptionPane.showMessageDialog(null, "Doctor added successfully");
                            System.out.println("Nuevo doctor: " + doctor);
                            dispose();
                        }
                    }
                } else {
                    lblError.setText("All fields are required");
                }

            }
        });

        setVisible(true);
    }

    /**
     * Creates centered JLabels
     * @param text JLabel string content
     */
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text);
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private boolean validateData() {
        // Verificar que ningún campo esté vacío
        if (tfName.getText().isEmpty() ||
                tfSurname1.getText().isEmpty() ||
                tfSurname2.getText().isEmpty() ||
                tfDni.getText().isEmpty()) {
            lblError.setText("All fields are required");
            return false;
        }
        // Validate dni
        String dni = tfDni.getText();
        if (!dni.matches("\\d{8}[A-Z]")) {
            lblError.setText("Invalid DNI");
            return false;
        }
        if (patients != null) {
            if (tfEmail.getText().isEmpty() ||
                    tfAge.getText().isEmpty() ||
                    tfPhone.getText().isEmpty() ||
                    tfAddress.getText().isEmpty()) {
                lblError.setText("All fields are required");
                return false;
            }


            // Validate email
            String email = tfEmail.getText();
            if (!email.matches(".+@gmail\\.com")) {
                lblError.setText("Invalid email");
                return false;
            }

            // Validate phone number
            String phoneNumber = tfPhone.getText();
            if (!phoneNumber.matches("\\+[0-9]{9}")) {
                lblError.setText("Invalid phone number");
                return false;
            }

            // Validate address
            String address = tfAddress.getText();
            if (!address.matches(".+ \\d{3}, .+")) {
                lblError.setText("Invalid address");
                return false;
            }
        } else if (doctors != null) {
            if (cbSpeciality.getSelectedItem().toString().isEmpty()) {
                lblError.setText("All fields are required");
                return false;
            }

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
            JOptionPane.showMessageDialog(null, "Invalid age");
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
        SwingUtilities.invokeLater(() -> {
            new WindowAddUser(patients);
            new WindowAddUser(doctors);
        });


    }
}
