import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.GroupLayout;
/*
 * Created by JFormDesigner on Tue Oct 03 18:27:04 CEST 2023
 */



/**
 * @author Alvaro
 */
public class WindowLogin extends JFrame {
    public WindowLogin() {
        initComponents();
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                WindowRegister windowRegister = new WindowRegister();
                windowRegister.setSize(300, 300);
                windowRegister.setVisible(true);
            }
        });
    }

    private void thisKeyPressed(KeyEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        // Generated using JFormDesigner Evaluation license - Alvaro
        labelLogin = new JLabel();
        emailLabel = new JLabel();
        emailTf = new JTextField();
        passwordLabel = new JLabel();
        passwordField1 = new JPasswordField();
        loginButton = new JButton();
        accountLabel = new JLabel();
        registerButton = new JButton();
        labelError = new JLabel();

        //======== this ========
        setBackground(Color.green);
        setTitle("DeustoMed ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        var contentPane = getContentPane();

        //---- labelLogin ----
        labelLogin.setText("Login");
        labelLogin.setFont(new Font("Inconsolata", Font.BOLD, 18));

        //---- emailLabel ----
        emailLabel.setText("Email:");

        //---- passwordLabel ----
        passwordLabel.setText("Password:");

        //---- loginButton ----
        loginButton.setText("Login");

        //---- accountLabel ----
        accountLabel.setText("Have an account? ");

        //---- registerButton ----
        registerButton.setText("Register");

        //---- labelError ----
        labelError.setForeground(Color.red);

        GroupLayout contentPaneLayout = new GroupLayout(contentPane);
        contentPane.setLayout(contentPaneLayout);
        contentPaneLayout.setHorizontalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addGap(14, 14, 14)
                    .addGroup(contentPaneLayout.createParallelGroup()
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGap(10, 10, 10)
                            .addComponent(accountLabel)
                            .addGap(18, 18, 18)
                            .addComponent(registerButton))
                        .addGroup(contentPaneLayout.createSequentialGroup()
                            .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                .addComponent(passwordLabel)
                                .addComponent(emailLabel))
                            .addGap(24, 24, 24)
                            .addGroup(contentPaneLayout.createParallelGroup()
                                .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                    .addComponent(passwordField1)
                                    .addComponent(emailTf, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE))
                                .addComponent(loginButton)
                                .addGroup(contentPaneLayout.createSequentialGroup()
                                    .addGap(8, 8, 8)
                                    .addComponent(labelLogin))))
                        .addComponent(labelError, GroupLayout.PREFERRED_SIZE, 253, GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(16, Short.MAX_VALUE))
        );
        contentPaneLayout.setVerticalGroup(
            contentPaneLayout.createParallelGroup()
                .addGroup(contentPaneLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(labelLogin, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(emailTf, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addComponent(emailLabel))
                    .addGap(18, 18, 18)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(passwordLabel)
                        .addComponent(passwordField1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                    .addGap(16, 16, 16)
                    .addComponent(loginButton)
                    .addGap(24, 24, 24)
                    .addGroup(contentPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                        .addComponent(accountLabel)
                        .addComponent(registerButton))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(labelError)
                    .addContainerGap(22, Short.MAX_VALUE))
        );
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Alvaro
    private JLabel labelLogin;
    private JLabel emailLabel;
    private JTextField emailTf;
    private JLabel passwordLabel;
    private JPasswordField passwordField1;
    private JButton loginButton;
    private JLabel accountLabel;
    private JButton registerButton;
    private JLabel labelError;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
