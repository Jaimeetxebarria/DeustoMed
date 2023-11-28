package org.deustomed.ui;

import com.toedter.calendar.JDateChooser;
import org.deustomed.Doctor;
import org.deustomed.Patient;

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

public class WindowAddUser extends JFrame {
    List<Patient> patients;
    List<Doctor> doctors;

    JLabel lblId;
    JLabel lblName;
    JLabel lblSurname1;
    JLabel lblSurname2;
    JLabel lblEmail;
    JLabel lblDni;
    JLabel lblAge;
    JLabel lblPhone;
    JLabel lblAddress;
    JLabel lblBirthDate;
    JLabel lblSpeciality;
    JLabel lblError;

    JTextField tfId;
    JTextField tfName;
    JTextField tfSurname1;
    JTextField tfSurname2;
    JTextField tfEmail;
    JTextField tfDni;
    JTextField tfAge;
    JTextField tfPhone;
    JTextField tfAddress;
    JTextField tfBirthDate;
    JTextField tfSpeciality;
    JButton btnSave;


    public WindowAddUser(List<Patient> patients) {
        this.patients = patients;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setTitle("New patient");
        setLocationRelativeTo(null);
        setSize(500, 750);

        lblId = new JLabel("ID:");
        lblName = new JLabel("Name:");
        lblSurname1 = new JLabel("Surname 1:");
        lblSurname2 = new JLabel("Surname 2:");
        lblEmail = new JLabel("Email:");
        lblDni = new JLabel("DNI:");
        lblAge = new JLabel("Age:");
        lblPhone = new JLabel("Phone:");
        lblAddress = new JLabel("Address:");
        lblBirthDate = new JLabel("Birth date:");
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
        tfEmail = new JTextField();
        tfDni = new JTextField();

        //JDateChooser
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatedDate = currentDate.format(format);
        tfBirthDate = new JTextField(formatedDate);
        tfBirthDate.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateAge();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateAge();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        /*
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");
        tfBirthDate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dateChooser.show();
            }
        });
        dateChooser.getDateEditor().addPropertyChangeListener(e -> {
            if ("date".equals(e.getPropertyName())) {
                updateDateTextField(dateChooser, tfBirthDate);
            }
        });

         */
        
        tfAge = new JTextField();
        tfAge.setEditable(false);
        //tfAge.setText();

        tfPhone = new JTextField();
        tfAddress = new JTextField();

        btnSave = new JButton("Save");

        JPanel pnlCenter = new JPanel(new BorderLayout());

        JPanel pnlPrimary = new JPanel(new GridLayout(10, 2));
        pnlPrimary.add(lblId);
        pnlPrimary.add(pnlId);
        pnlPrimary.add(lblName);
        pnlPrimary.add(tfName);
        pnlPrimary.add(lblSurname1);
        pnlPrimary.add(tfSurname1);
        pnlPrimary.add(lblSurname2);
        pnlPrimary.add(tfSurname2);
        pnlPrimary.add(lblEmail);
        pnlPrimary.add(tfEmail);
        pnlPrimary.add(lblDni);
        pnlPrimary.add(tfDni);
        pnlPrimary.add(lblBirthDate);
        pnlPrimary.add(tfBirthDate);
        pnlPrimary.add(lblAge);
        pnlPrimary.add(tfAge);
        pnlPrimary.add(lblPhone);
        pnlPrimary.add(tfPhone);
        pnlPrimary.add(lblAddress);
        pnlPrimary.add(tfAddress);


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
                String birthDate = tfBirthDate.getText();
                String age = tfAge.getText();
                String phone = tfPhone.getText();
                String address = tfAddress.getText();

                if (name.isEmpty() || surname1.isEmpty() || surname2.isEmpty() || email.isEmpty() || dni.isEmpty() || birthDate.isEmpty() || age.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                    lblError.setText("All fields are required");
                } else if (Integer.parseInt(tfAge.getText())<0) {
                    lblError.setText("Age must be positive");
                } else if (checkEmail(email)) {
                    lblError.setText("Email already exists");
                } else {
                    int id = patients.size() + 1;
                    Patient patient = new Patient(id, name, surname1, surname2, email, "", dni, Integer.parseInt(age), phone, address, new Date());
                    patients.add(patient);
                    JOptionPane.showMessageDialog(null, "Patient added successfully");
                    dispose();
                }
            }
        });


        setVisible(true);
    }

    private boolean checkEmail(String email){
        boolean result= true;
        for (Patient patient : patients) {
            if (patient.getEmail().equals(email)) {
                result = false;
            }
        }
        return result;
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
     * Updates the text of a textfield with the date selected in a JDateChooser
     * @param dateChooser   The JDateChooser
     * @param tfBirthDate   The textfield to update
     */
    private void updateDateTextField(JDateChooser dateChooser, JTextField tfBirthDate) {
        Date selectedDate = dateChooser.getDate();
        if (selectedDate != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = dateFormat.format(selectedDate);
            tfBirthDate.setText(formattedDate);
        } else {
            tfBirthDate.setText("");
        }
    }

    /**
     * Updates the age textfield with the age calculated from the birthdate textfield
     */
    private void updateAge() {
        try {

            String birthdayText = tfBirthDate.getText();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date birthday = dateFormat.parse(birthdayText);

            int age = getAge(birthday);
            if(age>=0 || age<=120){
                tfAge.setText(String.valueOf(age));
            }
        } catch (ParseException ex) {
            tfAge.setText("Error");
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
        List<Patient> patients= new ArrayList<>();
        patients.add(new Patient(1,"Pablo","Garcia","Iglesias","email1","1234","dni1", 20, "Phone1", "Adress1", new Date()));
        patients.add(new Patient(2,"Andoni","Hernández","Ruiz","email2","5678", "dni2", 17, "Phone2", "Adress2", new Date()));
        SwingUtilities.invokeLater(() -> {
            new WindowAddUser(patients);
        });

    }
}
