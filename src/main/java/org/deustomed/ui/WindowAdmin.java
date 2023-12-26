package org.deustomed.ui;

import org.deustomed.Doctor;
import org.deustomed.Patient;
import org.deustomed.Sex;
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
    protected JTextField tfFindPatient;
    protected JButton btnPatient;
    protected JButton btnLogoutPatient;

    protected JPanel pnlDoctor;
    protected JTable tblDoctor;
    protected DefaultTableModel mdlDoctor;
    protected JScrollPane scrDoctor;
    protected JTextField tfFindDoctor;
    protected JButton btnDoctor;
    protected JButton btnLogoutDoctor;

    protected JPanel pnlLogs;

    public WindowAdmin(){
        this.setTitle("DeustoMed");
        this.setSize(950, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        //pending data
        patients= new ArrayList<>();
        patients.add(new Patient("1", "Pablo", "Garcia", "Iglesias", "email1", "dni1", Sex.MALE, 20, "Phone1", "Adress1", new Date()));
        patients.add(new Patient("2", "Andoni", "Hernández", "Ruiz", "email2", "dni2", Sex.MALE, 17, "Phone2", "Adress2", new Date()));
        doctors= new ArrayList<>();
        //doctors.add(new Doctor(1,"Jaime","Eguskisa","Gascon","email1","4562","dni1", Sex.MALE, "Ophthalmologist", new ArrayList<>(), new ArrayList<>()));
        //doctors.add(new Doctor(2,"Irene","Garcia","Iglesias","email2","1234","dni2", Sex.FEMALE, "Ophthalmologist", new ArrayList<>(), new ArrayList<>()));

        tabAdmin = new JTabbedPane();

        //Patient
        pnlPatient = new JPanel(new BorderLayout());

        String[] columNamesPatients = {"ID", "Surnames", "Name", "Sex", "Email", "DNI", "Age", "Phone", "Address", "Birthdate"};
        mdlPatient = completeTable(columNamesPatients, patients);
        tblPatient = new JTable(mdlPatient);
        tblPatient.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(3).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(6).setPreferredWidth(25);
        tblPatient.getColumnModel().getColumn(10).setPreferredWidth(150);
        tblPatient.setRowHeight(25);

        configureTable(tblPatient, new ButtonEditor(patients), new ButtonRenderer());

        scrPatient = new JScrollPane(tblPatient);
        tfFindPatient = new JTextField();
        tfFindPatient.setPreferredSize(new Dimension(200, 25));
        btnPatient = new JButton("Add");
        btnLogoutPatient = new JButton("Logout");

        JPanel pnlUpper = new JPanel(new BorderLayout());
        pnlUpper.add(tfFindPatient, BorderLayout.WEST);
        pnlPatient.add(pnlUpper, BorderLayout.NORTH);
        pnlPatient.add(scrPatient, BorderLayout.CENTER);

        JPanel pnlBotton = new JPanel(new BorderLayout());
        pnlBotton.add(btnLogoutPatient, BorderLayout.WEST);
        pnlBotton.add(btnPatient, BorderLayout.EAST);
        pnlPatient.add(pnlBotton, BorderLayout.SOUTH);

        tabAdmin.addTab("Users", pnlPatient);

        btnPatient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WindowAddUser(patients);
            }

        });

        btnLogoutPatient.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        //Doctor
        pnlDoctor = new JPanel(new BorderLayout());
        String[] columNamesDoctor = {"ID", "Surname", "Name", "Sex", "Email", "DNI", "Speciality"};
        mdlDoctor = completeTable(columNamesDoctor, doctors);
        tblDoctor = new JTable(mdlDoctor);
        tblDoctor.getColumnModel().getColumn(0).setPreferredWidth(25);
        tblDoctor.getColumnModel().getColumn(3).setPreferredWidth(25);
        tblDoctor.getColumnModel().getColumn(7).setPreferredWidth(150);
        tblDoctor.setRowHeight(25);

        configureTable(tblDoctor, new ButtonEditor(doctors), new ButtonRenderer());

        scrDoctor = new JScrollPane(tblDoctor);
        tfFindDoctor = new JTextField();
        tfFindDoctor.setPreferredSize(new Dimension(200, 25));
        btnDoctor = new JButton("Add");
        btnLogoutDoctor = new JButton("Logout");

        JPanel pnlUpperDoctor = new JPanel(new BorderLayout());
        pnlUpperDoctor.add(tfFindDoctor, BorderLayout.WEST);
        pnlDoctor.add(pnlUpperDoctor, BorderLayout.NORTH);

        pnlDoctor.add(scrDoctor, BorderLayout.CENTER);

        JPanel pnlBottonDoctor = new JPanel(new BorderLayout());
        pnlBottonDoctor.add(btnLogoutDoctor, BorderLayout.WEST);
        pnlBottonDoctor.add(btnDoctor, BorderLayout.EAST);
        pnlDoctor.add(pnlBottonDoctor, BorderLayout.SOUTH);

        tabAdmin.addTab("Doctors", pnlDoctor);

        btnDoctor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new WindowAddUser(doctors);
            }
        });

        btnLogoutDoctor.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });



        //Logs
        pnlLogs = new JPanel(new BorderLayout());

        this.add(tabAdmin);

        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WindowAdmin::new);
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
                row[3] = patient.getSex().toString();
                row[4] = patient.getEmail();
                row[5] = patient.getDni();
                row[6] = String.valueOf(patient.getAge());
                row[7] = patient.getPhoneNumer();
                row[8] = patient.getAddress();
                row[9] = patient.getBirthDate().toString();
                model.addRow(row);
            }

        } else if (users.get(0) instanceof Doctor) {
            for (User user : users) {
                Doctor doctor = (Doctor) user;
                Object[] row = new String[columNames.length];
                row[0] = String.valueOf(doctor.getId());
                row[1] = doctor.getSurname1() + " " + user.getSurname2();
                row[2] = doctor.getName();
                row[3] = doctor.getSex().toString();
                row[4] = doctor.getEmail();
                row[5] = doctor.getDni();
                row[6] = doctor.getSpeciality();
                model.addRow(row);
            }
        }

        model.addColumn("Actions");
        return model;
    }

    private void configureTable(JTable table, ButtonEditor buttonEditor, ButtonRenderer buttonRenderer) {
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellEditor(buttonEditor);
        table.getColumnModel().getColumn(table.getColumnCount() - 1).setCellRenderer(buttonRenderer);
    }
    /*
    private User getUserFromTable(JTable table, int row) {
        if (table.getModel() instanceof DefaultTableModel) {
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            List<?> dataList = (List<?>) model.getDataVector().get(row);
            if (dataList != null && dataList.size() > 0) {
                Object data = dataList.get(0);
                if (data instanceof Patient) {
                    return (Patient) data;
                } else if (data instanceof Doctor) {
                    return (Doctor) data;
                }
            }
        }
        return null;
    }

    private void updatePatientData(List<User> patients, int index, Object data){
        Patient patient = (Patient) getUserFromTable(tblPatient, index);

    }

     */

    class ButtonRenderer implements TableCellRenderer {
        private JPanel panel;
        private JButton btnEdit;
        private JButton btnDelete;

        public ButtonRenderer() {
            panel = new JPanel(new FlowLayout());
            btnEdit = new JButton("Edit");
            btnDelete = new JButton("Delete");

            JPanel pnlButtons = new JPanel(new GridLayout(1, 2));
            pnlButtons.add(btnEdit);
            pnlButtons.add(btnDelete);
            panel.add(pnlButtons);


            btnEdit.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    JOptionPane.showMessageDialog(null, "Botón Edit clickeado");
                    JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, btnEdit);

                    if (table != null) {
                        TableType tableType = getTableType(table, btnEdit);
                        int selectedRow = table.getSelectedRow();
                        System.out.println(selectedRow);
                        if (selectedRow != -1) {
                            int modelRow = table.convertRowIndexToModel(selectedRow);

                            // Obtén los datos asociados a la fila
                            Object[] rowData = new Object[table.getColumnCount()];
                            for (int i = 0; i < table.getColumnCount(); i++) {
                                rowData[i] = table.getModel().getValueAt(modelRow, i);
                            }

                            // Realiza la lógica de edición basada en los datos de la fila
                            switch (tableType) {
                                case PATIENTS:
                                    Patient originalPatient = (Patient) rowData[0];
                                    int indexInListP = patients.indexOf(originalPatient);

                                    if (indexInListP != -1) {
                                        // Crea un nuevo objeto Patient con los valores editados
                                        Patient editedPatient = new Patient();
                                        editedPatient.setName(rowData[2].toString());
                                        editedPatient.setSurname1(rowData[1].toString().split(" ")[0]);
                                        editedPatient.setSurname2(rowData[1].toString().split(" ")[1]);
                                        editedPatient.setEmail(rowData[3].toString());
                                        editedPatient.setDni(rowData[4].toString());
                                        editedPatient.setAge(Integer.parseInt(rowData[5].toString()));
                                        editedPatient.setPhoneNumer(rowData[6].toString());
                                        editedPatient.setAddress(rowData[7].toString());
                                        editedPatient.setBirthDate((Date) rowData[8]);


                                        // Actualiza el objeto en la lista
                                        patients.set(indexInListP, editedPatient);

                                        // Actualiza la fila en la tabla
                                        for (int i = 1; i < rowData.length; i++) {
                                            table.getModel().setValueAt(rowData[i], modelRow, i);
                                        }
                                    }
                                    System.out.println(patients);
                                    break;
                                case DOCTORS:
                                    Doctor originalDoctor = (Doctor) rowData[0];
                                    int indexInListD = doctors.indexOf(originalDoctor);
                                    if (indexInListD != -1) {
                                        // Crea un nuevo objeto Patient con los valores editados
                                        Doctor editedDoctor = new Doctor();
                                        editedDoctor.setName(rowData[2].toString());
                                        editedDoctor.setSurname1(rowData[1].toString().split(" ")[0]);
                                        editedDoctor.setSurname2(rowData[1].toString().split(" ")[1]);
                                        editedDoctor.setEmail(rowData[3].toString());
                                        editedDoctor.setDni(rowData[4].toString());
                                        editedDoctor.setSpeciality(rowData[5].toString());


                                        // Actualiza el objeto en la lista
                                        doctors.set(indexInListD, editedDoctor);

                                        // Actualiza la fila en la tabla
                                        for (int i = 1; i < rowData.length; i++) {
                                            table.getModel().setValueAt(rowData[i], modelRow, i);
                                        }
                                    }
                                    System.out.println(doctors);
                                    break;
                                case OTHER:
                                    break;
                            }


                        }
                    }
                }
            });

            btnDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Obtain Table
                    JTable table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, btnDelete);

                    if (table != null) {
                        TableType tableType = getTableType(table, btnDelete);
                        int selectedRow = table.getSelectedRow();
                        if (selectedRow != -1) {
                            int modelRow = table.convertRowIndexToModel(selectedRow);

                            switch (tableType) {
                                case PATIENTS:
                                    patients.remove(modelRow);
                                    break;
                                case DOCTORS:
                                    doctors.remove(modelRow);
                                    break;
                                case OTHER:
                                    break;
                            }

                            // Remueve la fila de la tabla
                            ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                        }
                    }
                }
            });
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return panel;
        }
        public TableType getTableType(JTable table, JButton button){
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int modelRow = table.convertRowIndexToModel(selectedRow);

                if (table.getModel() instanceof DefaultTableModel) {
                    DefaultTableModel model = (DefaultTableModel) table.getModel();

                    if (model.getRowCount() > modelRow) {
                        Object data = (DefaultTableModel) table.getModel();
                        if (data instanceof Patient) {
                            return TableType.PATIENTS;
                        } else if (data instanceof Doctor) {
                            return TableType.DOCTORS;
                        }

                        ((DefaultTableModel) table.getModel()).removeRow(modelRow);
                    }
                }


            }
            return TableType.OTHER;
        }
    }
    public enum TableType {
        PATIENTS,
        DOCTORS,
        OTHER
    }

    class ButtonEditor extends AbstractCellEditor implements TableCellEditor {
        private ButtonRenderer renderer;
        private Object currentValue;

        public ButtonEditor(List<?> dataList) {
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
}


