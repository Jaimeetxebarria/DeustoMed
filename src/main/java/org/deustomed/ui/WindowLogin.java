package org.deustomed.ui;


import javax.swing.*;
import java.util.Arrays;

public class WindowLogin extends JFrame {
    JLabel lblLogin;
    JLabel lblEmail;
    JLabel lblPassword;
    JLabel lblError;
    JTextField txtEmail;
    JPasswordField txtPassword;
    JButton btnLogin;

    public WindowLogin(){
        lblLogin = new JLabel("Login");
        lblEmail = new JLabel("Email:");
        lblPassword = new JLabel("Clave:");
        txtEmail = new JTextField();
        txtPassword = new JPasswordField();
        btnLogin = new JButton("Login");
        lblError = new JLabel("");

        lblLogin.setFont(lblLogin.getFont().deriveFont(20f));
        lblError.setForeground(java.awt.Color.RED);

        lblLogin.setBounds(115, 20, 100, 30);
        lblEmail.setBounds(45, 70, 100, 30);
        lblPassword.setBounds(45, 130, 100, 30);
        txtEmail.setBounds(45, 100, 200, 30);
        txtPassword.setBounds(45, 160, 200, 30);
        btnLogin.setBounds(95, 220, 100, 30);
        lblError.setBounds(15, 270, 250, 30);

        add(lblLogin);
        add(lblEmail);
        add(lblPassword);
        add(txtEmail);
        add(txtPassword);
        add(btnLogin);
        add(lblError);

        setLayout(null);
        this.setResizable(false);
        this.setTitle("DeustoMed");
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300,350);
        setVisible(true);


        btnLogin.addActionListener(e -> {
            String email = txtEmail.getText();
            char[] passwordChars = txtPassword.getPassword();
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
            lblError.setText("Error: El email no puede estar vacio");
            return false;
        } else if(email.length() < 5){
            lblError.setText("Error: El email debe tener al menos 5 caracteres");
            return false;
        } else if(!email.contains("@")){
            lblError.setText("Error: El email debe contener un @");
            return false;
        } else if (password.isEmpty()){
            lblError.setText("Error: La contraseña no puede estar vacia");
            return false;
        } else if (password.length() < 8){
            lblError.setText("Error: La contraseña debe tener al menos 8 caracteres");
            return false;
        } else {
            lblError.setText("");
            return true;
        }
    }

}