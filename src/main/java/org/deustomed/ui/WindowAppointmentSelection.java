package org.deustomed.ui;

import org.deustomed.Appoinment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.TreeSet;

public class WindowAppointmentSelection extends JFrame {
    JComboBox<String> comboBox;
    JTable table;
    JButton cancelButton;
    JButton confirmButton;

    public WindowAppointmentSelection(TreeSet<Appoinment> appointments) {
        setLayout(new BorderLayout());


        comboBox = new JComboBox<>();
        add(comboBox, BorderLayout.NORTH);

        String[] columnNames = {"Médico", "Código", "Fecha"};
        DefaultTableModel model = new DefaultTableModel(null, columnNames);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy hh:mm a");

        for(Appoinment ap:appointments){
            String[] row = {ap.getDoctor().getName() +" "+ ap.getDoctor().getSurname1()+" "+ap.getDoctor().getSurname2(), String.valueOf(ap.getDoctor().getId()), dateFormat.format(ap.getDateAsDate())};
            model.addRow(row);
            comboBox.addItem(ap.getDoctor().getName() +" "+ ap.getDoctor().getSurname1()+" "+ap.getDoctor().getSurname2()+" - "+dateFormat.format(ap.getDateAsDate()));
        }

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));

        cancelButton = new JButton("Cancelar");
        confirmButton = new JButton("Confirmar");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] options = {"Sí", "No"};
                int confirmResult = JOptionPane.showOptionDialog(
                        null,
                        "¿Estás seguro de confirmar la cita?",
                        "Confirmación",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        new ImageIcon("src/main/java/ui/tick.png"),
                        options,
                        options[0]);

                if (confirmResult == JOptionPane.YES_OPTION) {
                    // TODO: PUT PATIENT IN THE APPOINTMENT
                    dispose();
                }

            }
        });

        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(cancelButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(confirmButton);
        buttonPanel.add(Box.createHorizontalGlue());
        add(buttonPanel, BorderLayout.SOUTH);

        setTitle("Selección de cita");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new WindowAppointmentSelection(new TreeSet());
    }
}
