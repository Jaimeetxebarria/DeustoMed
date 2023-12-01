package org.deustomed.ui;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WindowAppointment extends JFrame {

    private JTextField patientNameField;
    private JComboBox reasonCombo;
    private JLabel dateLabel;
    private JLabel nameLabel;
    private JLabel reasonLabel;
    private JDateChooser dateChooser;


    public WindowAppointment(Date date) {
        System.out.println(date);

        setTitle("Solicitud de Cita Médica");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        dateLabel = new JLabel("Fecha de la cita:");
        nameLabel = new JLabel("Nombre del paciente:");
        reasonLabel = new JLabel("Motivo de la cita:");

        dateChooser = new JDateChooser();
        dateChooser.setMinSelectableDate(new Date());
        dateChooser.setDate(date);

        //TODO: OBTENER EL NOMBRE DEL PACIENTE
        patientNameField = new JTextField("Nombre");
        patientNameField.setEditable(false);

        reasonCombo = new JComboBox();
        reasonCombo.addItem("General");
        reasonCombo.addItem("Revisión");
        reasonCombo.addItem("Urgencia");


        JButton submitButton = new JButton("Enviar Solicitud");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String patientName = patientNameField.getText();

                Date selectedDate = dateChooser.getDate();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
                String formated = dateFormat.format(selectedDate);

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.HOUR_OF_DAY, -24);
                Date dateminus = calendar.getTime();

                if (selectedDate != null && selectedDate.before(dateminus)) {
                    JOptionPane.showMessageDialog(WindowAppointment.this,
                            "Error: La fecha seleccionada ya ha pasado.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }else{
                    //TODO: Algoritmo que genere HashSet de citas libres (ordenadas por fecha)

                    new WindowAppointmentSelection();
                }

                /*else{

                JOptionPane.showMessageDialog(WindowAppointment.this,
                        "Solicitud de cita enviada:\nFecha: " + formated + "\nNombre: " + patientName + "\nMotivo: " + reasonCombo.getSelectedItem().toString(),
                        "Confirmación", JOptionPane.INFORMATION_MESSAGE);}
                 */
            }

        });

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(dateLabel)
                        .addComponent(nameLabel)
                        .addComponent(reasonLabel))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(dateChooser)
                        .addComponent(patientNameField)
                        .addComponent(reasonCombo)
                        .addComponent(submitButton))
        );

        layout.setVerticalGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(dateLabel)
                        .addComponent(dateChooser))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameLabel)
                        .addComponent(patientNameField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(reasonLabel)
                        .addComponent(reasonCombo))
                .addComponent(submitButton)
        );

        pack();
        setLocationRelativeTo(null);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new WindowAppointment(new Date());
    }
}
