package main.java.ui;

import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.*;
/*
 * Created by JFormDesigner on Fri Oct 06 09:12:16 CEST 2023
 */



/**
 * @author Propietario
 */
public class WindowRegister extends JFrame {
    public WindowRegister() {
        initComponents();
    }

    public boolean emailValidation(String email){
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    public boolean passwordValidation(String password){
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Alvaro
        accountLabel = new JLabel();
        patientRadioButton = new JRadioButton();
        doctorRadioButton = new JRadioButton();
        doctCodeLabel = new JLabel();
        codePasswordField = new JPasswordField();
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        surnameLabel = new JLabel();
        surnameTextField = new JTextField();
        emailLabel = new JLabel();
        emailTextField = new JTextField();
        PasswordLabel = new JLabel();
        registerButton = new JButton();
        questionLabel = new JLabel();
        button2 = new JButton();
        errorLabel = new JLabel();
        paswordField = new JPasswordField();

        //======== this ========
        setTitle("Register");
        var contentPane = getContentPane();

        //---- accountLabel ----
        accountLabel.setText("New account");

        //---- patientRadioButton ----
        patientRadioButton.setText("Patient");

        //---- doctorRadioButton ----
        doctorRadioButton.setText("Doctor");

        //---- doctCodeLabel ----
        doctCodeLabel.setText("Doctor code:");

        //---- nameLabel ----
        nameLabel.setText("Name:");

        //---- surnameLabel ----
        surnameLabel.setText("Surname:");

        //---- emailLabel ----
        emailLabel.setText("Email:");

        //---- PasswordLabel ----
        PasswordLabel.setText("Password:");

        //---- registerButton ----
        registerButton.setText("Register");

        //---- questionLabel ----
        questionLabel.setText("Registered?");

        //---- button2 ----
        button2.setText("Log in");

        //---- errorLabel ----
        errorLabel.setForeground(Color.red);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(18, 18, 18)
                                    .addComponent(doctorRadioButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(patientRadioButton, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(doctCodeLabel))
                                .addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addGap(18, 18, 18)
                                            .addComponent(nameLabel))
                                        .addComponent(surnameLabel, GroupLayout.Alignment.TRAILING)
                                        .addComponent(emailLabel, GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(nameTextField)
                                        .addComponent(emailTextField)
                                        .addGroup(contentPaneLayout.createSequentialGroup()
                                            .addComponent(surnameTextField, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))))
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(PasswordLabel)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(paswordField, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(registerButton, GroupLayout.PREFERRED_SIZE, 88, GroupLayout.PREFERRED_SIZE)
                            .addGap(9, 9, 9)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(codePasswordField, GroupLayout.PREFERRED_SIZE, 93, GroupLayout.PREFERRED_SIZE)
                    .addGap(76, 76, 76))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(accountLabel))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(questionLabel)
                            .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(button2))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(69, 69, 69)
                            .addComponent(errorLabel, GroupLayout.PREFERRED_SIZE, 239, GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(accountLabel)
                    .addGap(26, 26, 26)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(doctorRadioButton)
                            .addComponent(patientRadioButton)
                            .addComponent(doctCodeLabel)
                            .addComponent(codePasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(45, 45, 45)
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                .addComponent(nameLabel)
                                .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))))
                    .addGap(21, 21, 21)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(surnameLabel)
                        .addComponent(surnameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(20, 20, 20)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(emailLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(emailTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(PasswordLabel)
                        .addComponent(paswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(errorLabel)
                    .addGap(10, 10, 10)
                    .addComponent(registerButton)
                    .addGap(35, 35, 35)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(questionLabel)
                        .addComponent(button2))
                    .addGap(58, 58, 58))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

       registerButton.addActionListener(new ActionListener() {
           @Override
           public void actionPerformed(ActionEvent e) {
               if (patientRadioButton.isSelected()){
                   if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || paswordField.getText().isEmpty()) {
                       errorLabel.setText("You must fill all the fields");
                   } else if (!emailValidation(emailTextField.getText())) {
                       errorLabel.setText("The email is not valid");
                   } else if (!passwordValidation(paswordField.getText())) {
                       errorLabel.setText("The password is not valid");
                   } else {
                       errorLabel.setText("You have been registered"); //TODO añadir jpane y mandar email, recoger los datos en database
                   }
               }else{
                   if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || paswordField.getText().isEmpty() || codePasswordField.getText().isEmpty()) {
                       errorLabel.setText("You must fill all the fields");
                   } else if (!emailValidation(emailTextField.getText())) {
                       errorLabel.setText("The email is not valid");
                   } else if (!passwordValidation(paswordField.getText())) {
                       errorLabel.setText("The password is not valid");
                   } else {
                       errorLabel.setText("You have been registered");
                   }
               }
           }
       });
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Alvaro
    private JLabel accountLabel;
    private JRadioButton patientRadioButton;
    private JRadioButton doctorRadioButton;
    private JLabel doctCodeLabel;
    private JPasswordField codePasswordField;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JLabel surnameLabel;
    private JTextField surnameTextField;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JLabel PasswordLabel;
    private JButton registerButton;
    private JLabel questionLabel;
    private JButton button2;
    private JLabel errorLabel;
    private JPasswordField paswordField;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on




}
