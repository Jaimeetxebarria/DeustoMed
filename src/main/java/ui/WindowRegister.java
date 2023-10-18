package main.java.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.border.*;
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

    public boolean emailValidation(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean passwordValidation(String password) {
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }


    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Alvaro
        accountLabel = new JLabel();
        questionLabel = new JLabel();
        button2 = new JButton();
        panel1 = new JPanel();
        doctorRadioButton = new JRadioButton();
        patientRadioButton = new JRadioButton();
        doctCodeLabel = new JLabel();
        codePasswordField = new JPasswordField();
        panel2 = new JPanel();
        nameLabel = new JLabel();
        nameTextField = new JTextField();
        surnameTextField = new JTextField();
        surnameLabel = new JLabel();
        emailLabel = new JLabel();
        emailTextField = new JTextField();
        passwordField = new JPasswordField();
        PasswordLabel = new JLabel();
        errorLabel = new JLabel();
        registerButton = new JButton();

        //======== this ========
        setTitle("Register");
        setMaximumSize(new Dimension(-300000000, 300000000));
        var contentPane = getContentPane();

        //---- accountLabel ----
        accountLabel.setText("Creating new account");

        //---- questionLabel ----
        questionLabel.setText("Already registered?");

        //---- button2 ----
        button2.setText("Log in");

        //======== panel1 ========
        {
            panel1.setBorder(new TitledBorder("Identify yourselve:"));
            panel1.setBorder ( new javax . swing. border .CompoundBorder ( new javax . swing. border .TitledBorder ( new javax .
            swing. border .EmptyBorder ( 0, 0 ,0 , 0) ,  "JF\u006frmD\u0065sig\u006eer \u0045val\u0075ati\u006fn" , javax. swing .border
            . TitledBorder. CENTER ,javax . swing. border .TitledBorder . BOTTOM, new java. awt .Font ( "Dia\u006cog"
            , java .awt . Font. BOLD ,12 ) ,java . awt. Color .red ) ,panel1. getBorder
            () ) ); panel1. addPropertyChangeListener( new java. beans .PropertyChangeListener ( ){ @Override public void propertyChange (java
            . beans. PropertyChangeEvent e) { if( "\u0062ord\u0065r" .equals ( e. getPropertyName () ) )throw new RuntimeException
            ( ) ;} } );
            panel1.setLayout(null);

            //---- doctorRadioButton ----
            doctorRadioButton.setText("Doctor");
            doctorRadioButton.setBorder(null);
            doctorRadioButton.setBorderPainted(true);
            panel1.add(doctorRadioButton);
            doctorRadioButton.setBounds(new Rectangle(new Point(145, 15), doctorRadioButton.getPreferredSize()));

            //---- patientRadioButton ----
            patientRadioButton.setText("Patient");
            patientRadioButton.setBorder(null);
            patientRadioButton.setBorderPainted(true);
            panel1.add(patientRadioButton);
            patientRadioButton.setBounds(new Rectangle(new Point(225, 15), patientRadioButton.getPreferredSize()));

            //---- doctCodeLabel ----
            doctCodeLabel.setText("Introduce your doctor code: ");
            panel1.add(doctCodeLabel);
            doctCodeLabel.setBounds(new Rectangle(new Point(40, 50), doctCodeLabel.getPreferredSize()));
            panel1.add(codePasswordField);
            codePasswordField.setBounds(230, 45, 165, codePasswordField.getPreferredSize().height);

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel1.getComponentCount(); i++) {
                    Rectangle bounds = panel1.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel1.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel1.setMinimumSize(preferredSize);
                panel1.setPreferredSize(preferredSize);
            }
        }

        //======== panel2 ========
        {
            panel2.setBorder(new TitledBorder("Introduce your information"));
            panel2.setLayout(null);

            //---- nameLabel ----
            nameLabel.setText("Name:");
            nameLabel.setFont(new Font("Inter", Font.PLAIN, 15));
            panel2.add(nameLabel);
            nameLabel.setBounds(new Rectangle(new Point(40, 35), nameLabel.getPreferredSize()));
            panel2.add(nameTextField);
            nameTextField.setBounds(120, 30, 275, nameTextField.getPreferredSize().height);
            panel2.add(surnameTextField);
            surnameTextField.setBounds(120, 80, 275, 30);

            //---- surnameLabel ----
            surnameLabel.setText("Surname:");
            surnameLabel.setFont(new Font("Inter", Font.PLAIN, 15));
            panel2.add(surnameLabel);
            surnameLabel.setBounds(new Rectangle(new Point(40, 80), surnameLabel.getPreferredSize()));

            //---- emailLabel ----
            emailLabel.setText("Email:");
            emailLabel.setFont(new Font("Inter", Font.PLAIN, 15));
            panel2.add(emailLabel);
            emailLabel.setBounds(new Rectangle(new Point(40, 130), emailLabel.getPreferredSize()));
            panel2.add(emailTextField);
            emailTextField.setBounds(120, 125, 275, 30);
            panel2.add(passwordField);
            passwordField.setBounds(140, 170, 255, 30);

            //---- PasswordLabel ----
            PasswordLabel.setText("Password:");
            PasswordLabel.setFont(new Font("Inter", Font.PLAIN, 15));
            panel2.add(PasswordLabel);
            PasswordLabel.setBounds(new Rectangle(new Point(40, 175), PasswordLabel.getPreferredSize()));

            //---- errorLabel ----
            errorLabel.setText("not valid");
            panel2.add(errorLabel);
            errorLabel.setBounds(new Rectangle(new Point(195, 220), errorLabel.getPreferredSize()));

            {
                // compute preferred size
                Dimension preferredSize = new Dimension();
                for(int i = 0; i < panel2.getComponentCount(); i++) {
                    Rectangle bounds = panel2.getComponent(i).getBounds();
                    preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                    preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
                }
                Insets insets = panel2.getInsets();
                preferredSize.width += insets.right;
                preferredSize.height += insets.bottom;
                panel2.setMinimumSize(preferredSize);
                panel2.setPreferredSize(preferredSize);
            }
        }

        //---- registerButton ----
        registerButton.setText("Register");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(43, 43, 43)
                    .addComponent(questionLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(button2)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap(24, Short.MAX_VALUE)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                                    .addComponent(accountLabel)
                                    .addGap(154, 154, 154))
                                .addComponent(panel1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 435, GroupLayout.PREFERRED_SIZE)
                                .addComponent(panel2, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 435, GroupLayout.PREFERRED_SIZE))
                            .addGap(19, 19, 19))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addComponent(registerButton)
                            .addGap(192, 192, 192))))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(accountLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(panel1, GroupLayout.PREFERRED_SIZE, 87, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(panel2, GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                    .addGap(12, 12, 12)
                    .addComponent(registerButton)
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(questionLabel)
                        .addComponent(button2))
                    .addGap(9, 9, 9))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (patientRadioButton.isSelected()) {
                    if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || passwordField.getText().isEmpty()) {
                        errorLabel.setText("You must fill all the fields");
                    } else if (!emailValidation(emailTextField.getText())) {
                        errorLabel.setText("The email is not valid");
                    } else if (!passwordValidation(passwordField.getText())) {
                        errorLabel.setText("The password is not valid");
                    } else {
                        errorLabel.setText("You have been registered"); //TODO añadir jpane y mandar email, recoger los datos en database
                    }
                } else {
                    if (nameTextField.getText().isEmpty() || surnameTextField.getText().isEmpty() || emailTextField.getText().isEmpty() || passwordField.getText().isEmpty() || codePasswordField.getText().isEmpty()) {
                        errorLabel.setText("You must fill all the fields");
                    } else if (!emailValidation(emailTextField.getText())) {
                        errorLabel.setText("The email is not valid");
                    } else if (!passwordValidation(passwordField.getText())) {
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
    private JLabel questionLabel;
    private JButton button2;
    private JPanel panel1;
    private JRadioButton doctorRadioButton;
    private JRadioButton patientRadioButton;
    private JLabel doctCodeLabel;
    private JPasswordField codePasswordField;
    private JPanel panel2;
    private JLabel nameLabel;
    private JTextField nameTextField;
    private JTextField surnameTextField;
    private JLabel surnameLabel;
    private JLabel emailLabel;
    private JTextField emailTextField;
    private JPasswordField passwordField;
    private JLabel PasswordLabel;
    private JLabel errorLabel;
    private JButton registerButton;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on


}
