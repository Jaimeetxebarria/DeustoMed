package org.deustomed.ui;

import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import org.deustomed.Doctor;
import org.deustomed.Patient;
import org.deustomed.Sex;
import org.deustomed.User;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class WindowConfirmNewUser extends JFrame {
    public JLabel lblId, lblChatCode;
    public JTextField tfId, tfChatCode;
    public JButton btCopyId, btOk;

    public WindowConfirmNewUser(User user) {
        if (user instanceof Patient) {
            setTitle("Paciente creado con éxito");
            setSize(350, 120);
        } else {
            setTitle("Doctor creado con éxito");
            setSize(350, 150);
        }

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);


        lblId = new JLabel("Id:");
        tfId = new JTextField();
        tfId.setEditable(false);
        btCopyId = new JButton("Copiar código");
        btOk = new JButton("Confirmar");

        if (user instanceof Doctor) {
            lblChatCode = new JLabel("Código chat:");
            tfChatCode = new JTextField();
            tfChatCode.setEditable(false);
        }
        Border border = new TitledBorder("Aquí tiene sus datos identificativos:");

        JPanel pnlId = new JPanel(new GridLayout(1, 2));
        pnlId.add(tfId);
        pnlId.add(btCopyId);

        JPanel pnlMain = new JPanel();
        pnlMain.setBorder(border);
        if (user instanceof Patient) {
            pnlMain.setLayout(new GridLayout(1, 2));
            pnlMain.add(lblId);
            pnlMain.add(pnlId);
        } else {
            pnlMain.setLayout(new GridLayout(2, 2));
            pnlMain.add(lblId);
            pnlMain.add(pnlId);
            pnlMain.add(lblChatCode);
            pnlMain.add(tfChatCode);
        }
        add(pnlMain, BorderLayout.CENTER);
        add(btOk, BorderLayout.SOUTH);

        btCopyId.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                copyToClipboard(tfId.getText());
            }
        });

        btOk.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        setVisible(true);
    }

    /**
     * Copies a text of the id textfield to the clipboard
     *
     * @param id The id to copy
     */
    private void copyToClipboard(String id) {
        StringSelection stringSelection = new StringSelection(id);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
        JOptionPane.showMessageDialog(null, "ID copiado al portapapeles: " + id);
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();

        Patient patient = new Patient("00AAA", "Pablo", "Garcia", "Iglesias",
                LocalDate.of(1990, 8, 12), Sex.MALE, "dni1",
                "email1", "phone1", "address1", null);
        new WindowConfirmNewUser(patient);

//        Doctor doctor = new Doctor("00AAA", "Pablo", "Garcia", "Iglesias",
//                LocalDate.of(1990, 8, 12), Sex.MALE, "dni1",
//                "email1", "phone1", "address1", null, null);
//        new WindowConfirmNewUser(doctor);
    }
}