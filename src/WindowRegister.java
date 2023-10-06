import java.awt.*;
import javax.swing.*;
import javax.swing.GroupLayout;
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
        label8 = new JLabel();
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
        surnameLabel.setText("Surname");

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

        //---- label8 ----
        label8.setText("Error");

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(emailLabel)
                            .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addComponent(PasswordLabel)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(emailTextField, GroupLayout.Alignment.LEADING)
                                .addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
                                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                        .addComponent(accountLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(patientRadioButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addGap(18, 18, 18)
                                    .addComponent(doctorRadioButton, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                    .addGap(39, 39, 39)
                                    .addComponent(doctCodeLabel)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(codePasswordField, GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE))
                                .addGroup(GroupLayout.Alignment.LEADING, contentPaneLayout.createSequentialGroup()
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(nameLabel)
                                        .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, 137, GroupLayout.PREFERRED_SIZE))
                                    .addGap(25, 25, 25)
                                    .addGroup(contentPaneLayout.createParallelGroup()
                                        .addComponent(surnameLabel)
                                        .addComponent(surnameTextField))))
                            .addGap(44, 44, 44))))
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(143, 143, 143)
                            .addComponent(registerButton))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addComponent(label8)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addComponent(questionLabel)
                                    .addGap(18, 18, 18)
                                    .addComponent(button2))))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(paswordField, GroupLayout.DEFAULT_SIZE, 383, Short.MAX_VALUE)))
                    .addGap(44, 44, 44))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(accountLabel)
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(patientRadioButton)
                        .addComponent(doctorRadioButton)
                        .addComponent(doctCodeLabel)
                        .addComponent(codePasswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addComponent(nameLabel)
                        .addComponent(surnameLabel))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(nameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(surnameTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(emailLabel)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(emailTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(PasswordLabel)
                    .addGap(18, 18, 18)
                    .addComponent(paswordField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addGap(38, 38, 38)
                    .addComponent(registerButton)
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(questionLabel)
                        .addComponent(button2))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(label8)
                    .addContainerGap(13, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
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
    private JLabel label8;
    private JPasswordField paswordField;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
