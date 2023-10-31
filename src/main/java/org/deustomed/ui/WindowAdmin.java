package org.deustomed.ui;

import javax.swing.*;
import java.awt.*;

public class WindowAdmin extends JFrame {
    protected JTabbedPane tabAdmin;

    JPanel pnlUser;
    JPanel pnlDoctor;
    JPanel pnlLogs;

    public WindowAdmin(){
        tabAdmin.add("User", pnlUser);
        tabAdmin.add("Doctor", pnlDoctor);

        this.setLayout(new BorderLayout());
        this.add(tabAdmin, BorderLayout.SOUTH);

        setLayout(null);
        this.setResizable(false);
        this.setTitle("DeustoMed");
        this.setLocation(600,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }


}
