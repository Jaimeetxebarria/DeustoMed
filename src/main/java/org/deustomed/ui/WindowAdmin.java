package org.deustomed.ui;

import org.deustomed.Doctor;
import org.deustomed.Patient;
import org.deustomed.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class WindowAdmin extends JFrame {

    protected List<User> patients;
    protected List<User> doctors;

    protected JTabbedPane tabAdmin;

    protected JPanel pnlPatient;
    protected JTable tblPatient;
    protected DefaultTableModel mdlPatient;
    protected JScrollPane scrPatient;
    protected JTextField tfPatient;
    protected JButton btnPatient;
    protected JButton btnLogout;
    protected JButton btnDeletePatient;

    protected JPanel pnlDoctor;
    protected JTable tblDoctor;
    protected DefaultTableModel mdlDoctor;
    protected JScrollPane scrDoctor;
    protected JTextField tfDoctor;
    protected JButton btnDoctor;
    protected JButton btnLogoutDoctor;
    protected JButton btnDeleteDoctor;

    protected JPanel pnlLogs;

    public WindowAdmin(){
        this.setTitle("DeustoMed");
        this.setSize(950, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //pending data
        patients= new ArrayList<>();
        patients.add(new Patient(1,"Pablo","Garcia","Iglesias","","1234","dni1", 20, "Phone1", "Adress1", new Date()));
        patients.add(new Patient(2,"Andoni","Hernández","Ruiz","","5678", "dni2", 17, "Phone2", "Adress2", new Date()));
        doctors= new ArrayList<>();
        doctors.add(new Doctor(1,"Jaime","Eguskisa","Gascon","","4562","", "Ophthalmologist", new ArrayList<>()));
        doctors.add(new Doctor(2,"Iñaki","Garcia","Iglesias","","1234","", "Ophthalmologist", new ArrayList<>()));

        tabAdmin = new JTabbedPane();

        //Patient
        pnlPatient = new JPanel(new BorderLayout());
        tblPatient = new JTable();
        String[] columNamesPatients = {"ID", "Surname", "Name", "Email", "DNI", "Age", "Phone", "Address", "Birthdate"};
        mdlPatient = completeTable(columNamesPatients, patients);
        tblPatient.setModel(mdlPatient);
        scrPatient = new JScrollPane(tblPatient);
        tfPatient = new JTextField();
        btnPatient = new JButton("Add");
        btnLogout = new JButton("Logout");
        btnDeletePatient = new JButton("Delete");

        JPanel pnlUpper = new JPanel(new BorderLayout());
        pnlUpper.add(tfPatient, BorderLayout.WEST);
        pnlPatient.add(pnlUpper, BorderLayout.NORTH);

        pnlPatient.add(scrPatient, BorderLayout.CENTER);


        JPanel pnlBotton = new JPanel(new BorderLayout());
        pnlBotton.add(btnLogout, BorderLayout.WEST);
        pnlBotton.add(btnPatient, BorderLayout.EAST);
        pnlPatient.add(pnlBotton, BorderLayout.SOUTH);

        tabAdmin.addTab("Users", pnlPatient);

        //Doctor
        pnlDoctor = new JPanel(new BorderLayout());
        tblDoctor = new JTable();
        String[] columNamesDoctor = {"ID", "Surname", "Name", "Email", "DNI", "Speciality"};
        mdlDoctor = completeTable(columNamesDoctor, doctors);
        tblDoctor.setModel(mdlDoctor);
        scrDoctor = new JScrollPane(tblDoctor);
        tfDoctor = new JTextField();
        btnDoctor = new JButton("Add");
        btnLogoutDoctor = new JButton("Logout");
        btnDeleteDoctor = new JButton("Delete");

        JPanel pnlUpperDoctor = new JPanel(new BorderLayout());
        pnlUpperDoctor.add(tfDoctor, BorderLayout.WEST);
        pnlDoctor.add(pnlUpperDoctor, BorderLayout.NORTH);

        pnlDoctor.add(scrDoctor, BorderLayout.CENTER);


        JPanel pnlBottonDoctor = new JPanel(new BorderLayout());
        pnlBottonDoctor.add(btnLogoutDoctor, BorderLayout.WEST);
        pnlBottonDoctor.add(btnDoctor, BorderLayout.EAST);
        pnlDoctor.add(pnlBottonDoctor, BorderLayout.SOUTH);

        tabAdmin.addTab("Doctors", pnlDoctor);

        this.add(tabAdmin);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        new WindowAdmin();
    }

    public DefaultTableModel completeTable(String[] columNames, List<User> users) {
        DefaultTableModel model = new DefaultTableModel();
        model.setColumnIdentifiers(columNames);
        if (users.get(0) instanceof Patient) {
            for (User user : users) {
                Patient patient = (Patient) user;
                Object[] row = new String[columNames.length];
                row[0] = String.valueOf(patient.getId());
                row[1] = patient.getSurname1() + " " +  user.getSurname2();
                row[2] = patient.getName();
                row[3] = patient.getEmail();
                row[4] = patient.getDni();
                row[5] = String.valueOf(patient.getAge());
                row[6] = patient.getPhoneNumer();
                row[7] = patient.getAddress();
                row[8] = patient.getBirthDate().toString();
                model.addRow(row);
            }
        } else if (users.get(0) instanceof Doctor) {
            for (User user : users) {
                Doctor doctor = (Doctor) user;
                Object[] row = new String[columNames.length];
                row[0] = String.valueOf(doctor.getId());
                row[1] = doctor.getSurname1() + " " + user.getSurname2();
                row[2] = doctor.getName();
                row[3] = doctor.getEmail();
                row[4] = doctor.getDni();
                row[5] = doctor.getSpeciality();
                model.addRow(row);
            }
        }
        return model;
    }

}
