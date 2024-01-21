package org.deustomed.ui;

import org.deustomed.Disease;
import org.deustomed.Medication;

import javax.swing.*;
import java.awt.*;

public class ListCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        if (value instanceof Disease) {
            Disease disease = (Disease) value;

            label.setText(disease.getName());
        }
        if (value instanceof Medication) {
            Medication medication = (Medication) value;

            label.setText(medication.getCommercialName());
        }
        return label;
    }
}
