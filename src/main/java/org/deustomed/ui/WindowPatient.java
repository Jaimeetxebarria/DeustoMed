package org.deustomed.ui;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class WindowPatient extends JFrame { //TODO: ARREGLAR LOS ICONOS
    public WindowPatient() {
        // Configura el estilo del botón
        UIManager.put("Button.font", new Font("Arial", Font.BOLD, 14));
        UIManager.put("Button.background", Color.WHITE);
        UIManager.put("Button.foreground", Color.BLACK);

        // Configura el diseño de la ventana
        setTitle("Ventana de Paciente");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Crea el panel para el menú vertical
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.DARK_GRAY);
        menuPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Crea iconos para los botones (reemplaza las rutas con tus propios archivos de iconos)
        ImageIcon infoIcon = new ImageIcon("src/main/java/ui/info.jpg");
        ImageIcon calendarIcon = new ImageIcon("calendar_icon.png");
        ImageIcon medicinesIcon = new ImageIcon("medicines_icon.png");
        ImageIcon chatIcon = new ImageIcon("chat_icon.png");

        // Agrega los botones al menú
        JButton infoButton = new JButton(infoIcon);
        JButton calendarButton = new JButton("Calendario", calendarIcon);
        JButton medicinesButton = new JButton("Medicinas", medicinesIcon);
        JButton chatButton = new JButton("Chat", chatIcon);

        // Configura el aspecto de los botones
        infoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        calendarButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        medicinesButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        chatButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Agrega los botones al panel del menú
        menuPanel.add(infoButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio entre botones
        menuPanel.add(calendarButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(medicinesButton);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        menuPanel.add(chatButton);

        // Agrega el panel del menú a la izquierda de la ventana
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(menuPanel, BorderLayout.WEST);

        // Configura el contenido principal en el centro de la ventana
        JPanel contentPanel = new JPanel();
        // Agrega aquí el contenido principal de la ventana

        // Agrega el contenido principal al centro de la ventana
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        // Hacer visible la ventana
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new WindowPatient();
        });
    }
}
