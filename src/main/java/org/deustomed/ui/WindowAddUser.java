package org.deustomed.ui;

import com.toedter.calendar.JDateChooser;
import org.deustomed.*;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class WindowAddUser extends JFrame {

    //General
    JLabel lblId;
    JLabel lblName;
    JLabel lblSurname1;
    JLabel lblSurname2;
    JLabel lblSex;
    JLabel lblEmail;
    JLabel lblDni;
    JTextField tfId;
    JTextField tfName;
    JTextField tfSurname1;
    JTextField tfSurname2;
    JRadioButton radMale;
    JRadioButton radFemale;
    JTextField tfEmail;
    JTextField tfDni;
    JLabel lblError;
    JButton btnSave;

    //For patient
    List<Patient> patients;
    JLabel lblAge;
    JLabel lblPhone;
    JLabel lblAddress;
    JLabel lblBirthDate;
    JTextField tfAge;
    JTextField tfPhone;
    JTextField tfAddress;
    JDateChooser dateChooser;
    private Date previousDate;

    //For doctor
    List<Doctor> doctors;
    JLabel lblSpeciality;
    JComboBox<String> cbSpeciality;
    protected List<String> specialities = Arrays.asList("Alergología", "Anestesiología", "Angiología", "Cardiología", "Endocrinología", "Gastroenterología", "Geriatría", "Hematología", "Infectología", "Medicina interna", "Nefrología", "Neumología", "Neurología", "Obstetricia", "Oftalmología", "Oncología", "Pediatría", "Psiquiatría", "Reumatología", "Toxicología", "Urología");


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
        setResizable(false);
        setLocationRelativeTo(null);
        setSize(300, 550);

        lblId = new JLabel("ID:");
        lblName = new JLabel("Name:");
        lblSurname1 = new JLabel("Surname 1:");
        lblSurname2 = new JLabel("Surname 2:");
        lblSex = new JLabel("Sex:");
        lblEmail = new JLabel("Email:");
        lblDni = new JLabel("DNI:");
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);

        tfId = new JTextField("Id generado automáticamente");
        JButton btnCopyId = new JButton("Copy");
        btnCopyId.addActionListener(e -> copyToClipboard(tfId.getText()));
        tfId.setEditable(false);
        JPanel pnlId = new JPanel(new BorderLayout());
        pnlId.add(tfId, BorderLayout.CENTER);
        pnlId.add(btnCopyId, BorderLayout.EAST);

        tfName = new JTextField();
        tfSurname1 = new JTextField();
        tfSurname2 = new JTextField();
        radFemale = new JRadioButton("Female");
        radMale = new JRadioButton("Male");
        ButtonGroup group = new ButtonGroup();
        group.add(radFemale);
        group.add(radMale);
        tfEmail = new JTextField();
        tfDni = new JTextField();
        btnSave = new JButton("Save");

        if(patients!=null) {
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
            tfPhone.setToolTipText("Phone should start with 6 or 9 and have 9 digits");
            tfAddress = new JTextField();
            tfAddress.setToolTipText("Address should be in the format: Street, number, city, province, country\nExample: Sabino Arana, 15, 2A, Bilbao, Biscay, Spain");


        }else if (doctors!=null) {
            lblSpeciality = new JLabel("Speciality:");
            cbSpeciality = new JComboBox<>();
            cbSpeciality.setModel(new DefaultComboBoxModel<>(specialities.toArray(new String[0])));

        }

        //Add components to the window
        JPanel pnlCenter = new JPanel(new BorderLayout());

        JPanel pnlPrimary = new JPanel();
        if (patients != null) {
            pnlPrimary.setLayout(new GridLayout(11, 2));
        } else if (doctors != null) {
            pnlPrimary.setLayout(new GridLayout(8, 2));
        }
        pnlPrimary.add(lblId);
        pnlPrimary.add(pnlId);
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
        if(patients!=null) {
            pnlPrimary.add(lblBirthDate);
            pnlPrimary.add(dateChooser);
            pnlPrimary.add(lblAge);
            pnlPrimary.add(tfAge);
            pnlPrimary.add(lblPhone);
            pnlPrimary.add(tfPhone);
            pnlPrimary.add(lblAddress);
            pnlPrimary.add(tfAddress);
        }else if (doctors!=null) {
            pnlPrimary.add(lblSpeciality);
            pnlPrimary.add(cbSpeciality);

        }
        pnlCenter.add(pnlPrimary, BorderLayout.CENTER);
        pnlCenter.add(lblError, BorderLayout.SOUTH);

        JPanel pnlSouth = new JPanel(new BorderLayout());
        pnlSouth.add(btnSave, BorderLayout.EAST);

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
                if(selectedSex != null) {
                    if (selectedSex.equals(radMale)) {
                        sex = Sex.MALE;
                    } else{
                        sex = Sex.FEMALE;
                    }

                    if (patients != null) {
                        Date birthDate = dateChooser.getDate();
                        String age = tfAge.getText();
                        String phone = tfPhone.getText();
                        String address = tfAddress.getText();
                        if (validateData()) {
                            String id = ""+(patients.size() + 1);
                            Patient patient = new Patient(id, name, surname1, surname2, email, "", dni, sex, Integer.parseInt(age), phone, address, birthDate);
                            patients.add(patient);
                            JOptionPane.showMessageDialog(null, "Patient added successfully");
                            System.out.println("Nuevo paciente: " + patient);
                            dispose();
                        }


                    } else if (doctors != null) {
                        String speciality = ((String) cbSpeciality.getSelectedItem()).toString();
                        if (validateData()) {
                            String id = ""+doctors.size() + 1;
                            // TODO: 19/12/23 diferenciar entre médicos de familia y especialistas;
                            // TODO: 19/12/23 en este ejemplo es médico de familia, por los que la especialidad es predeterminada
                            Doctor doctor = new FamilyDoctor(id, name, surname1, surname2, email, "", dni, sex, new ArrayList<>(), new ArrayList<>());
                            doctors.add(doctor);
                            JOptionPane.showMessageDialog(null, "Doctor added successfully");
                            System.out.println("Nuevo doctor: " + doctor);
                            dispose();
                        }
                    }
                }else{
                    lblError.setText("All fields are required");
                }

            }
        });

        setVisible(true);
    }

    private boolean validateData() {
        // Verificar que ningún campo esté vacío
        if (tfId.getText().isEmpty() ||
                tfName.getText().isEmpty() ||
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
        if(patients!=null) {
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
        } else if (doctors!=null) {
            if (cbSpeciality.getSelectedItem().toString().isEmpty()) {
                lblError.setText("All fields are required");
                return false;
            }

        }
        return true;
    }
    /**
     * Copies a text of the id textfield to the clipboard
     * @param id   The id to copy
     */
    private void copyToClipboard(String id) {
        StringSelection stringSelection = new StringSelection(id);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(null, "ID copiado al portapapeles: " + id);
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
     * @param birthday The birthday of the user
     * @return
     */
    private int getAge(Date birthday){
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
        List<User> patients= new ArrayList<>();
        patients.add(new Patient("1","Pablo","Garcia","Iglesias","email1","1234","dni1", 20, "Phone1", "Adress1", new Date()));
        patients.add(new Patient("2","Andoni","Hernández","Ruiz","email2","5678", "dni2", 17, "Phone2", "Adress2", new Date()));

        List<User> doctors= new ArrayList<>();
        doctors.add(new Doctor("1","Pablo","Garcia","Iglesias","email1","1234","dni1", Sex.MALE,"speciality1", new ArrayList<>()));
        doctors.add(new Doctor("2","Andoni","Hernández","Ruiz","email2","5678", "dni2", Sex.MALE, "speciality2", new ArrayList<>()));
        SwingUtilities.invokeLater(() -> {
            new WindowAddUser(patients);
            new WindowAddUser(doctors);
        });



    }
}
