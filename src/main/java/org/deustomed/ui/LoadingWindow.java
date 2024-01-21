package org.deustomed.ui;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

class LoadingWindow extends JFrame {
    private static Thread t;

    public LoadingWindow(String name) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth() / 2 - 200, (int) screenSize.getHeight() / 2 - 60, 400, 120);
        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        Border emptyBorder = BorderFactory.createEmptyBorder(30, 40, 0, 0);
        labelPanel.setBorder(emptyBorder);
        String[] states = {"Cargando datos del registro de pacientes .", "Cargando datos del registro de pacientes ..", "Cargando datos del registro de pacientes ...",
                "Cargando datos del registro de pacientes ....", "Cargando datos del registro de pacientes ....."};
        setTitle("Cargando ventana de Dr./Dra. " + name);
        JLabel loadingData = new JLabel();
        loadingData.setHorizontalAlignment(SwingConstants.LEFT);
        loadingData.setVerticalAlignment(SwingConstants.CENTER);
        loadingData.setFont(new Font(loadingData.getFont().getName(), 3, 15));
        labelPanel.add(loadingData);
        loadingData.getAlignmentX();
        this.add(labelPanel);
        t = new Thread(() -> {
            int counter = 0;
            while (counter < 5) {
                loadingData.setText(states[counter]);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
                counter++;
                if (counter == 5) counter = 0;
            }
        }
        );
        t.start();
    }

    public static void stopThread() {
        t.interrupt();
    };
}
