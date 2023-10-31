package org.deustomed.ui;

import javax.swing.*;
import java.awt.*;

public class WindowAdmin extends JFrame {
    protected JTabbedPane tabAdmin;

    protected JPanel pnlUser;
    protected JTable tblUser;
    protected JScrollPane scrUser;
    protected JTextField tfUser;
    protected JButton btnAddUser;
    protected JButton btnLogout;
    protected JButton btnEditUser;
    protected JButton btnDeleteUser;

    protected JPanel pnlDoctor;
    protected JPanel pnlLogs;

    public WindowAdmin(){
        this.setTitle("DeustoMed");
        this.setSize(450, 600);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        tabAdmin = new JTabbedPane();

        pnlUser = new JPanel(new BorderLayout());
        tblUser = new JTable();
        scrUser = new JScrollPane(tblUser);
        tfUser = new JTextField();
        btnAddUser = new JButton("Add");
        btnLogout = new JButton("Logout");
        btnEditUser = new JButton("Edit");
        btnDeleteUser = new JButton("Delete");

        JPanel pnlUpper = new JPanel(new BorderLayout());
        pnlUpper.add(tfUser, BorderLayout.WEST);
        pnlUser.add(pnlUpper, BorderLayout.NORTH);

        pnlUser.add(scrUser, BorderLayout.CENTER);


        JPanel pnlBotton = new JPanel(new BorderLayout());
        pnlBotton.add(btnLogout, BorderLayout.WEST);
        pnlBotton.add(btnAddUser, BorderLayout.EAST);
        pnlUser.add(pnlBotton, BorderLayout.SOUTH);

        tabAdmin.addTab("Users", pnlUser);
        this.add(tabAdmin);

        this.setVisible(true);
    }


}
