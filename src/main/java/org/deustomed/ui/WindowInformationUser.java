package org.deustomed.ui;

import javax.swing.*;

public class WindowInformationUser extends JFrame {
    JLabel lblname;
    JLabel surname1Lbl;
    JLabel surname2Lbl;
    JLabel emailLbl;
    JLabel dniLbl;
    JLabel ageLbl;
    JLabel phoneLbl;
    JLabel addressLbl;
    JLabel birthDateLbl;
    JLabel specialityLbl;
    JLabel errorLbl;
    JTextField nameTf;
    JTextField surname1Tf;
    JTextField surname2Tf;
    JTextField emailTf;
    JTextField dniTf;
    JTextField ageTf;
    JTextField phoneTf;
    JTextField addressTf;
    JTextField birthDateTf;
    JTextField specialityTf;
    JButton saveBtn;

    public WindowInformationUser() {
        lblname = new JLabel("Nombre:");
        surname1Lbl = new JLabel("Primer apellido:");
        surname2Lbl = new JLabel("Segundo apellido:");
        emailLbl = new JLabel("Email:");
        dniLbl = new JLabel("DNI:");
        ageLbl = new JLabel("Edad:");
        phoneLbl = new JLabel("Teléfono:");
        addressLbl = new JLabel("Dirección:");
        birthDateLbl = new JLabel("Fecha de nacimiento:");
        specialityLbl = new JLabel("Especialidad:");
        nameTf = new JTextField();
        surname1Tf = new JTextField();
        surname2Tf = new JTextField();
        emailTf = new JTextField();
        dniTf = new JTextField();
        ageTf = new JTextField();
        phoneTf = new JTextField();
        addressTf = new JTextField();
        birthDateTf = new JTextField();
        specialityTf = new JTextField();
        saveBtn = new JButton("Guardar");
        errorLbl = new JLabel("");
    }
}
