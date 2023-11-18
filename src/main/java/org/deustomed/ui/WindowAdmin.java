package org.deustomed.ui;

import org.deustomed.Doctor;
import org.deustomed.Patient;
import org.deustomed.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    protected JButton btnEditPatient;
    protected JButton btnDeletePatient;

    protected JPanel pnlDoctor;
    protected JTable tblDoctor;
    protected DefaultTableModel mdlDoctor;
    protected JScrollPane scrDoctor;
    protected JTextField tfDoctor;
    protected JButton btnDoctor;
    protected JButton btnLogoutDoctor;
    protected JButton btnEditDoctor;
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
        patients.add(new Patient(1,"Pablo","Garcia","Iglesias","email1","1234","dni1", 20, "Phone1", "Adress1", new Date()));
        patients.add(new Patient(2,"Andoni","Hernández","Ruiz","email2","5678", "dni2", 17, "Phone2", "Adress2", new Date()));
        doctors= new ArrayList<>();
        doctors.add(new Doctor(1,"Jaime","Eguskisa","Gascon","email1","4562","dni1", "Ophthalmologist", new ArrayList<>()));
        doctors.add(new Doctor(2,"Iñaki","Garcia","Iglesias","email2","1234","dni2", "Ophthalmologist", new ArrayList<>()));

        tabAdmin = new JTabbedPane();

        //Patient
        pnlPatient = new JPanel(new BorderLayout());

        String[] columNamesPatients = {"ID", "Surname", "Name", "Email", "DNI", "Age", "Phone", "Address", "Birthdate"};
        mdlPatient = completeTable(columNamesPatients, patients);
        tblPatient = new JTable(mdlPatient);

        tblPatient.getColumnModel().getColumn(mdlPatient.getColumnCount() - 1).setCellEditor(new ButtonEditor());
        tblPatient.getColumnModel().getColumn(mdlPatient.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());

        scrPatient = new JScrollPane(tblPatient);
        tfPatient = new JTextField();
        btnPatient = new JButton("Add");
        btnLogout = new JButton("Logout");
        btnEditPatient = new JButton("Edit");
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
        String[] columNamesDoctor = {"ID", "Surname", "Name", "Email", "DNI", "Speciality"};
        mdlDoctor = completeTable(columNamesDoctor, doctors);
        tblDoctor = new JTable(mdlDoctor);

        tblDoctor.getColumnModel().getColumn(mdlDoctor.getColumnCount() - 1).setCellEditor(new ButtonEditor());
        tblDoctor.getColumnModel().getColumn(mdlDoctor.getColumnCount() - 1).setCellRenderer(new ButtonRenderer());

        scrDoctor = new JScrollPane(tblDoctor);
        tfDoctor = new JTextField();
        btnDoctor = new JButton("Add");
        btnLogoutDoctor = new JButton("Logout");
        btnEditDoctor = new JButton("Edit");
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



        //Logs
        pnlLogs = new JPanel(new BorderLayout());

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

        model.addColumn("Acciones");
        return model;
    }

}

class ButtonRenderer implements TableCellRenderer {
    private JPanel panel;
    private JButton btnEdit;
    private JButton btnDelete;

    public ButtonRenderer() {
        panel = new JPanel(new FlowLayout());
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");

        panel.add(btnEdit);
        panel.add(btnDelete);

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, "Botón Edit clickeado");
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JOptionPane.showMessageDialog(null, "Botón Delete clickeado");
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        return panel;
    }
}

class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
    private final ButtonRenderer renderer;
    private Object currentValue;

    public ButtonEditor() {
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
