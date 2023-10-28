package org.deustomed.ui;

import javax.swing.*;

public class WindowLogin extends JFrame {
    JLabel loginLbl;
    JLabel emailLbl;
    JLabel passwordLbl;
    JTextField emailTf;
    JPasswordField passwordTf;
    JButton loginBtn;

    public WindowLogin(){
        loginLbl = new JLabel("Login");
        loginLbl.setFont(loginLbl.getFont().deriveFont(20f));
        emailLbl = new JLabel("Email:");
        passwordLbl = new JLabel("Clave:");
        emailTf = new JTextField();
        passwordTf = new JPasswordField();
        loginBtn = new JButton("Login");

        loginLbl.setBounds(170,20,100,30);
        emailLbl.setBounds(60,60,100,30);
        passwordLbl.setBounds(60,100,100,30);
        emailTf.setBounds(120,60,200,30);
        passwordTf.setBounds(120,100,200,30);
        loginBtn.setBounds(150,160,100,30);

        add(loginLbl);
        add(emailLbl);
        add(passwordLbl);
        add(emailTf);
        add(passwordTf);
        add(loginBtn);

        setLayout(null);
        this.setResizable(false);
        this.setTitle("DeustoMed");
        this.setLocation(600,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }


}
