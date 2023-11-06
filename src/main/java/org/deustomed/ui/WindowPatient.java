package org.deustomed.ui;

import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.border.EmptyBorder;

public class WindowPatient extends JFrame {
    protected String selectedButton = ""; //info, calendar, medicines, chat

    protected JButton infoButton;
    protected JButton calendarButton;
    protected JButton medicinesButton;
    protected JButton chatButton;
    protected JButton logoutButton;

    protected JPanel menuPanel;
    protected JPanel infoPanel;
    protected JPanel calendarPanel;
    protected JPanel medicinesPanel;
    protected JPanel chatPanel;

    protected JCalendar calendar;

    public void deleteContent(){
        getContentPane().removeAll();
        getContentPane().add(menuPanel, BorderLayout.CENTER);
        revalidate();
    }

    public WindowPatient() {
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Button.foreground", Color.BLACK);

        setTitle("Ventana de Paciente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        Color colorizq = Color.DARK_GRAY;
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(colorizq);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        ImageIcon infoIcon = new ImageIcon("src/main/java/ui/info.png");
        ImageIcon calendarIcon = new ImageIcon("src/main/java/ui/calendario.png");
        ImageIcon medicinesIcon = new ImageIcon("src/main/java/ui/medicina.png");
        ImageIcon chatIcon = new ImageIcon("src/main/java/ui/mensaje.png");
        ImageIcon logoutIcon = new ImageIcon("src/main/java/ui/logout.png");

        infoButton = new JButton(infoIcon);
        infoButton.setBackground(colorizq);
        infoButton.setBorder(null);
        calendarButton = new JButton(calendarIcon);
        calendarButton.setBackground(colorizq);
        calendarButton.setBorder(null);
        medicinesButton = new JButton(medicinesIcon);
        medicinesButton.setBackground(colorizq);
        medicinesButton.setBorder(null);
        chatButton = new JButton(chatIcon);
        chatButton.setBackground(colorizq);
        chatButton.setBorder(null);
        logoutButton = new JButton(logoutIcon);
        logoutButton.setBackground(colorizq);
        logoutButton.setBorder(null);

        infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicinesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        logoutButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuPanel.add(infoButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(calendarButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(medicinesButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(chatButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 80)));
        menuPanel.add(logoutButton);

        calendarPanel = new JPanel(new BorderLayout());
        calendar = new JCalendar();
        calendar.setWeekOfYearVisible(false);
        calendarPanel.add(calendar, BorderLayout.WEST);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if(selectedButton.equals("calendar")){
                    deleteContent();
                    getContentPane().add(calendarPanel, BorderLayout.CENTER);

                }
            }
        });

        calendarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteContent();
                getContentPane().add(calendarPanel, BorderLayout.CENTER);
                selectedButton = "calendar";
                revalidate();
            }
        });



        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.WEST);
        JPanel contentPanel = new JPanel();
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WindowPatient();
        });
    }
}
