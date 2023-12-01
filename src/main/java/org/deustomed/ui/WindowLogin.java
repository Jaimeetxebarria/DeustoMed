package org.deustomed.ui;


import javax.swing.*;
import javax.swing.text.PasswordView;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;

public class WindowLogin extends JFrame {
    JLabel loginLbl;
    JLabel emailLbl;
    JLabel passwordLbl;
    JLabel errorLbl;
    JTextField emailTf;
    JPasswordField passwordTf;
    JButton loginBtn;

    public WindowLogin(){
        loginLbl = new JLabel("Login");
        emailLbl = new JLabel("Email:");
        passwordLbl = new JLabel("Clave:");
        emailTf = new JTextField();
        passwordTf = new JPasswordField();
        loginBtn = new JButton("Login");
        errorLbl = new JLabel("");

        loginLbl.setFont(loginLbl.getFont().deriveFont(20f));
        errorLbl.setForeground(java.awt.Color.RED);

        loginLbl.setBounds(115,20,100,30);
        emailLbl.setBounds(45,70,100,30);
        passwordLbl.setBounds(45,130,100,30);
        emailTf.setBounds(45,100,200,30);
        passwordTf.setBounds(45,160,200,30);
        loginBtn.setBounds(95,220,100,30);
        errorLbl.setBounds(15,270,250,30);

        add(loginLbl);
        add(emailLbl);
        add(passwordLbl);
        add(emailTf);
        add(passwordTf);
        add(loginBtn);
        add(errorLbl);

        setLayout(null);
        this.setResizable(false);
        this.setTitle("DeustoMed");
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,350);
        setVisible(true);


        loginBtn.addActionListener(e -> {
            String email = emailTf.getText();
            char[] passwordChars = passwordTf.getPassword();
            String password = new String(passwordChars);
            Arrays.fill(passwordChars, ' '); //Borrar la contraseña de la memoria
            if (loginChecker(email,password)) {
                //TODO: COMPROBAR LOGIN EN LA BASE DE DATOS Y ABRIR LA VENTANA PRINCIPAL
            }

        });
    }

    /**
     * Checks that the email and password are valid (not necessarily correct) and updates the error label.
     * @param email,password
     * @return boolean (true if the email and password are valid, false otherwise
     */
    public boolean loginChecker(String email, String password){
        if(email.isEmpty()){
            errorLbl.setText("Error: El email no puede estar vacio");
            return false;
        } else if(email.length() < 5){
            errorLbl.setText("Error: El email debe tener al menos 5 caracteres");
            return false;
        } else if(!email.contains("@")){
            errorLbl.setText("Error: El email debe contener un @");
            return false;
        } else if (password.isEmpty()){
            errorLbl.setText("Error: La contraseña no puede estar vacia");
            return false;
        } else if (password.length() < 8){
            errorLbl.setText("Error: La contraseña debe tener al menos 8 caracteres");
            return false;
        } else {
            errorLbl.setText("");
            return true;
        }
    }

}