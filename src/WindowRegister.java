import java.awt.*;
import javax.swing.*;
/*
 * Created by JFormDesigner on Wed Oct 04 10:05:20 CEST 2023
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
        label1 = new JLabel();
        label2 = new JLabel();
        radioButton1 = new JRadioButton();
        radioButton2 = new JRadioButton();
        label3 = new JLabel();
        panel1 = new JPanel();
        panel2 = new JPanel();
        panel3 = new JPanel();
        panel4 = new JPanel();
        label10 = new JLabel();
        textField5 = new JTextField();
        textField1 = new JTextField();
        label5 = new JLabel();
        textField2 = new JTextField();
        label6 = new JLabel();
        textField3 = new JTextField();
        label7 = new JLabel();
        textField4 = new JTextField();
        label8 = new JLabel();
        passwordField1 = new JPasswordField();
        button1 = new JButton();
        label9 = new JLabel();
        button2 = new JButton();
        label11 = new JLabel();

        //======== this ========
        setTitle("Register");
        setUndecorated(true);
        var contentPane = getContentPane();
        contentPane.setLayout(null);
        contentPane.add(label1);
        label1.setBounds(new Rectangle(new Point(15, 25), label1.getPreferredSize()));

        //---- label2 ----
        label2.setText("New account");
        contentPane.add(label2);
        label2.setBounds(25, 25, 100, 20);

        //---- radioButton1 ----
        radioButton1.setText("Patient");
        contentPane.add(radioButton1);
        radioButton1.setBounds(25, 65, 75, radioButton1.getPreferredSize().height);

        //---- radioButton2 ----
        radioButton2.setText("Doctor");
        contentPane.add(radioButton2);
        radioButton2.setBounds(new Rectangle(new Point(120, 65), radioButton2.getPreferredSize()));
        contentPane.add(label3);
        label3.setBounds(new Rectangle(new Point(25, 115), label3.getPreferredSize()));

        //======== panel1 ========
        {
            panel1.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(new javax.swing.border.EmptyBorder(
            0,0,0,0), "JFor\u006dDesi\u0067ner \u0045valu\u0061tion",javax.swing.border.TitledBorder.CENTER,javax.swing.border.TitledBorder
            .BOTTOM,new java.awt.Font("Dia\u006cog",java.awt.Font.BOLD,12),java.awt.Color.
            red),panel1. getBorder()));panel1. addPropertyChangeListener(new java.beans.PropertyChangeListener(){@Override public void propertyChange(java.
            beans.PropertyChangeEvent e){if("bord\u0065r".equals(e.getPropertyName()))throw new RuntimeException();}});
            panel1.setLayout(new GridLayout(2, 1, 40, 20));
        }
        contentPane.add(panel1);
        panel1.setBounds(-15, 110, 0, panel1.getPreferredSize().height);

        //======== panel2 ========
        {
            panel2.setLayout(new GridLayout(2, 1, 200, 200));
        }
        contentPane.add(panel2);
        panel2.setBounds(-175, 110, 0, panel2.getPreferredSize().height);

        //======== panel3 ========
        {
            panel3.setLayout(new GridLayout(2, 1, 100, 20));
        }
        contentPane.add(panel3);
        panel3.setBounds(-75, 110, 0, panel3.getPreferredSize().height);

        //======== panel4 ========
        {
            panel4.setLayout(new GridLayout(1, 2));

            //---- label10 ----
            label10.setText("Doctor code:");
            panel4.add(label10);
            panel4.add(textField5);
        }
        contentPane.add(panel4);
        panel4.setBounds(220, 65, 145, 25);
        contentPane.add(textField1);
        textField1.setBounds(310, 90, 72, 0);

        //---- label5 ----
        label5.setText("Name:");
        contentPane.add(label5);
        label5.setBounds(new Rectangle(new Point(25, 105), label5.getPreferredSize()));
        contentPane.add(textField2);
        textField2.setBounds(25, 135, 130, textField2.getPreferredSize().height);

        //---- label6 ----
        label6.setText("Surname:");
        contentPane.add(label6);
        label6.setBounds(new Rectangle(new Point(180, 105), label6.getPreferredSize()));
        contentPane.add(textField3);
        textField3.setBounds(180, 135, 120, 25);

        //---- label7 ----
        label7.setText("Email:");
        contentPane.add(label7);
        label7.setBounds(new Rectangle(new Point(25, 205), label7.getPreferredSize()));
        contentPane.add(textField4);
        textField4.setBounds(25, 230, 130, textField4.getPreferredSize().height);

        //---- label8 ----
        label8.setText("Password:");
        contentPane.add(label8);
        label8.setBounds(new Rectangle(new Point(25, 285), label8.getPreferredSize()));
        contentPane.add(passwordField1);
        passwordField1.setBounds(25, 315, 130, passwordField1.getPreferredSize().height);

        //---- button1 ----
        button1.setText("Register");
        contentPane.add(button1);
        button1.setBounds(new Rectangle(new Point(160, 355), button1.getPreferredSize()));

        //---- label9 ----
        label9.setText("Registered ?");
        contentPane.add(label9);
        label9.setBounds(35, 400, label9.getPreferredSize().width, 20);

        //---- button2 ----
        button2.setText("Log in");
        contentPane.add(button2);
        button2.setBounds(135, 400, 105, 25);

        //---- label11 ----
        label11.setText("Error...");
        contentPane.add(label11);
        label11.setBounds(40, 435, 330, label11.getPreferredSize().height);

        {
            // compute preferred size
            Dimension preferredSize = new Dimension();
            for(int i = 0; i < contentPane.getComponentCount(); i++) {
                Rectangle bounds = contentPane.getComponent(i).getBounds();
                preferredSize.width = Math.max(bounds.x + bounds.width, preferredSize.width);
                preferredSize.height = Math.max(bounds.y + bounds.height, preferredSize.height);
            }
            Insets insets = contentPane.getInsets();
            preferredSize.width += insets.right;
            preferredSize.height += insets.bottom;
            contentPane.setMinimumSize(preferredSize);
            contentPane.setPreferredSize(preferredSize);
        }
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // Generated using JFormDesigner Evaluation license - Alvaro
    private JLabel label1;
    private JLabel label2;
    private JRadioButton radioButton1;
    private JRadioButton radioButton2;
    private JLabel label3;
    private JPanel panel1;
    private JPanel panel2;
    private JPanel panel3;
    private JPanel panel4;
    private JLabel label10;
    private JTextField textField5;
    private JTextField textField1;
    private JLabel label5;
    private JTextField textField2;
    private JLabel label6;
    private JTextField textField3;
    private JLabel label7;
    private JTextField textField4;
    private JLabel label8;
    private JPasswordField passwordField1;
    private JButton button1;
    private JLabel label9;
    private JButton button2;
    private JLabel label11;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
