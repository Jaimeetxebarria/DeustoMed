package org.deustomed.ui;


import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.fonts.inter.FlatInterFont;
import org.deustomed.ConfigLoader;
import org.deustomed.UserType;
import org.deustomed.authentication.BypassTrustManager;
import org.deustomed.authentication.UserAuthenticationService;
import org.deustomed.postgrest.authentication.exceptions.AuthenticationServerInternalErrorException;
import org.deustomed.postgrest.authentication.exceptions.AuthenticationServerUnavailableException;
import org.deustomed.postgrest.authentication.exceptions.InexistentUserException;
import org.deustomed.postgrest.authentication.exceptions.InvalidCredentialsException;

import javax.swing.*;
import java.util.Arrays;

public class WindowLogin extends JFrame {
    JLabel lblLogin;
    JLabel lblId;
    JLabel lblPassword;
    JLabel lblError;
    JTextField txtId;
    JPasswordField txtPassword;
    JRadioButton rdbtnPatient;
    JRadioButton rdbtnDoctor;
    JRadioButton rdbtnAdmin;
    JButton btnLogin;

    public WindowLogin() {
        //Login label
        lblLogin = new JLabel("Iniciar Sesión");
        lblLogin.setFont(lblLogin.getFont().deriveFont(23f).deriveFont(java.awt.Font.BOLD));
        lblLogin.setBounds(45, 30, 300, 30);
        add(lblLogin);

        //ID label and text field
        lblId = new JLabel("ID:");
        lblId.setBounds(45, 70, 100, 30);
        add(lblId);

        txtId = new JTextField();
        txtId.setBounds(45, 100, 200, 30);
        add(txtId);

        //Password label and text field
        lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(lblPassword.getFont().deriveFont(13f));
        lblPassword.setBounds(45, 140, 100, 30);
        add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setBounds(45, 170, 200, 30);
        add(txtPassword);

        //Radio buttons
        ButtonGroup buttonGroup = new ButtonGroup();
        rdbtnPatient = new JRadioButton("Paciente");
        rdbtnDoctor = new JRadioButton("Doctor");
        rdbtnAdmin = new JRadioButton("Administrador");

        rdbtnPatient.setMnemonic('P');
        rdbtnDoctor.setMnemonic('D');
        rdbtnAdmin.setMnemonic('A');

        rdbtnPatient.setBounds(45, 210, 200, 25);
        rdbtnDoctor.setBounds(45, 230, 200, 25);
        rdbtnAdmin.setBounds(45, 250, 200, 25);

        buttonGroup.add(rdbtnPatient);
        buttonGroup.add(rdbtnDoctor);
        buttonGroup.add(rdbtnAdmin);

        rdbtnPatient.setSelected(true);

        add(rdbtnPatient);
        add(rdbtnDoctor);
        add(rdbtnAdmin);

        //Login button and error label
        btnLogin = new JButton("Iniciar Sesión");
        btnLogin.setBounds(45, 300, 200, 30);
        add(btnLogin);

        lblError = new JLabel("");
        lblError.setHorizontalAlignment(SwingConstants.CENTER);
        lblError.setVerticalAlignment(SwingConstants.TOP);
        lblError.setForeground(java.awt.Color.RED);
        lblError.setBounds(25, 340, 240, 50);
        add(lblError);

        setLayout(null);
        setResizable(false);
        setTitle("DeustoMed");
        setSize(300, 425);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        btnLogin.addActionListener(e -> {
            String id = txtId.getText().trim();
            char[] passwordChars = txtPassword.getPassword();
            String password = new String(passwordChars).trim();
            Arrays.fill(passwordChars, ' '); //Borrar la contraseña de la memoria

            if (!loginChecker(id, password)) return;

            ConfigLoader configLoader = new ConfigLoader();
            UserAuthenticationService userAuthenticationService = new UserAuthenticationService(configLoader.getAuthServerBaseUrl(),
                    new BypassTrustManager(), configLoader.getAnonymousToken());

            try {
                switch (buttonGroup.getSelection().getMnemonic()) {
                    case 'P':
                        userAuthenticationService.login(id, password, UserType.PATIENT);
                        new WindowPatient(id, userAuthenticationService);
                        dispose();
                        break;
                    case 'D':
                        userAuthenticationService.login(id, password, UserType.DOCTOR);
                        SwingWorker<Void, Void> worker = new SwingWorker<>() {
                            @Override
                            protected Void doInBackground() {
                                new WindowDoctor(id, userAuthenticationService);
                                return null;
                            }
                        };
                        worker.execute();

                        dispose();
                        break;
                    case 'A':
                        userAuthenticationService.login(id, password, UserType.ADMIN);
                        new WindowAdmin(userAuthenticationService);
                        dispose();
                        break;
                }
            } catch (InvalidCredentialsException exception) {
                lblError.setText("El ID o la contraseña son incorrectos");
            } catch (InexistentUserException exception) {
                lblError.setText("El usuario no existe");
            } catch (AuthenticationServerUnavailableException | AuthenticationServerInternalErrorException exception) {
                lblError.setText("<html>Error del sistema. Por favor, inténtelo más<br/>tarde o póngase en contacto con " +
                        "soporte</html>");
            } catch (Exception exception) {
                exception.printStackTrace();
                lblError.setText("¡Enhorabuena! Ha descubierto un bug");
            }
        });
    }

    /**
     * Checks that the id and password are valid (not necessarily correct) and updates the error label.
     *
     * @param id, password
     *
     * @return True if the id and password are valid, false otherwise.
     */
    public boolean loginChecker(String id, String password) {
        if (id.isEmpty()) {
            lblError.setText("El ID no puede estar vacío");
            return false;
        } else if (!id.matches("^\\d{2}[a-zA-Z]{3}$")) {
            lblError.setText("El ID no está en el formato correcto");
            return false;
        } else if (password.isEmpty()) {
            lblError.setText("La contraseña no puede estar vacía");
            return false;
        } else {
            lblError.setText("");
            return true;
        }
    }

    public static void main(String[] args) {
        FlatLightLaf.setup();
        FlatInterFont.install();
        new WindowLogin();
    }

}