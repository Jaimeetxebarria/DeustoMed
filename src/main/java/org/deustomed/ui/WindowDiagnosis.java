package org.deustomed.ui;

import com.google.gson.JsonObject;
import org.deustomed.Appointment;
import org.deustomed.Diagnosis;
import org.deustomed.Disease;
import org.deustomed.Medication;
import org.deustomed.Patient;
import org.deustomed.postgrest.Entry;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.deustomed.ui.ShowPatientWindow.loadPatientDiseases;
import static org.deustomed.ui.ShowPatientWindow.loadPatientTreatments;
import static org.deustomed.ui.WindowDoctor.capitalizeFirstLetter;

public class WindowDiagnosis extends JFrame {
    private JList<Disease> patientDiseases;
    private DefaultListModel<Disease> patientDiseaseModel = new DefaultListModel<>();
    private JList<Medication> patientTreatments;
    private DefaultListModel<Medication> patientTreatmentsModel = new DefaultListModel<>();
    private JList<Disease> diseaseRegistry;
    private DefaultListModel<Disease> diseaseRegistryModel = new DefaultListModel<>();
    private JList<Medication> medicationRegistry;
    private DefaultListModel<Medication> medicationRegistryModel = new DefaultListModel<>();
    private static PostgrestClient postgrestClient;

    public static void main(String[] args) {

    }

    public WindowDiagnosis(Appointment appointment, PostgrestClient postgrestClient) {
        Patient patient = appointment.getPatient();
        LocalDateTime date = appointment.getDate();
        WindowDiagnosis.postgrestClient = postgrestClient;

        setTitle("Ventana de diagnóstico para " + patient.getName() + " " + patient.getSurname1() + " " + patient.getSurname2());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int h = (int) screenSize.getHeight()/ 2 + 270;
        int w = (int) screenSize.getWidth()/ 2 +20;
        setBounds((int) (screenSize.getWidth() - h)/2, (int) (screenSize.getHeight() - h)/2, h, w);

        Border emptyBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(new GridLayout(1, 2));
        panel2.setLayout(new GridLayout(2, 2));
        panel1.setBorder(emptyBorder);
        panel2.setBorder(emptyBorder);

        JLabel title = new JLabel("NUEVO DIAGNÓSTICO PARA " + patient.getName() + " " + patient.getSurname1() + " " + patient.getSurname2() + " ");
        JLabel dateLabel = new JLabel(capitalizeFirstLetter(date.getDayOfWeek().toString()) + ", " + date.getDayOfMonth() + " " + capitalizeFirstLetter(date.getMonth().toString()) + " " + date.toLocalTime());
        dateLabel.setHorizontalAlignment(JLabel.RIGHT);
        panel1.add(title);
        panel1.add(dateLabel);
        title.setBorder(emptyBorder);
        dateLabel.setBorder(emptyBorder);

        // Panel para dar de alta enfermedades del paciente
        JPanel panelPatientDiseases = new JPanel(new BorderLayout());
        panelPatientDiseases.setBorder(emptyBorder);
        patientDiseases = new JList<>(patientDiseaseModel);
        ArrayList<Disease> list = loadPatientDiseases(patient.getId(), postgrestClient);
        list.forEach(disease -> patientDiseaseModel.addElement(disease));
        JScrollPane scrollPane1 = new JScrollPane(patientDiseases);
        JButton dischargeDisease = new JButton("Dar enfermedad de alta");
        dischargeDisease.addActionListener((ActionEvent e) -> {
            Disease selectedDisease = patientDiseases.getSelectedValue();
            removePatientRelation(patient.getId(), selectedDisease.getId(), true);
            patientDiseaseModel.removeElement(selectedDisease);
        });
        panelPatientDiseases.add(scrollPane1, BorderLayout.CENTER);
        panelPatientDiseases.add(dischargeDisease, BorderLayout.SOUTH);

        // Panel para retirar medicamentos al paciente
        JPanel panelPatientTreatments = new JPanel(new BorderLayout());
        panelPatientTreatments.setBorder(emptyBorder);
        patientTreatments = new JList<>(patientTreatmentsModel);
        ArrayList<Medication> list1 = loadPatientTreatments(patient.getId(), postgrestClient);
        list1.forEach(treat -> patientTreatmentsModel.addElement(treat));
        JScrollPane scrollPane2 = new JScrollPane(patientTreatments);
        JButton withdrawTreatment = new JButton("Retirar medicamento ");
        withdrawTreatment.addActionListener((ActionEvent e) -> {
            Medication selectedMedication = patientTreatments.getSelectedValue();
            updatePatientRelation(patient.getId(), selectedMedication.getId(), true);
            patientTreatmentsModel.removeElement(selectedMedication);
        });
        panelPatientTreatments.add(scrollPane2, BorderLayout.CENTER);
        panelPatientTreatments.add(withdrawTreatment, BorderLayout.SOUTH);

        // Panel para diagnosticar nuevas enfermedades al paciente
        JPanel panelDiseaseRegistry = new JPanel(new BorderLayout());
        panelDiseaseRegistry.setBorder(emptyBorder);
        diseaseRegistryModel = new DefaultListModel<>();
        diseaseRegistry = new JList<>(diseaseRegistryModel);
        ArrayList<Disease> list2 = Disease.loadDiseaseRegistry(postgrestClient);
        list2.forEach(disease -> patientDiseaseModel.addElement(disease));
        JScrollPane scrollPane3 = new JScrollPane(patientDiseases);
        JButton diagnoseDisease = new JButton("Diagnosticar enfermedad ");
        diagnoseDisease.addActionListener((ActionEvent e) -> {
            Disease selectedDisease = diseaseRegistry.getSelectedValue();
            updatePatientRelation(patient.getId(), selectedDisease.getId(), true);
            patientDiseaseModel.addElement(selectedDisease);
        });
        panelDiseaseRegistry.add(scrollPane3, BorderLayout.CENTER);
        panelDiseaseRegistry.add(diagnoseDisease, BorderLayout.SOUTH);

        // Panel para recetar medicamentos al paciente
        JPanel panelMedicationRegisrtry = new JPanel(new BorderLayout());
        panelMedicationRegisrtry.setBorder(emptyBorder);
        medicationRegistry = new JList<>(medicationRegistryModel);
        ArrayList<Medication> list3 = Medication.loadMedicationRegistry(postgrestClient);
        list3.forEach(treat -> patientTreatmentsModel.addElement(treat));
        JScrollPane scrollPane4 = new JScrollPane(patientTreatments);
        JButton prescribeTreatment = new JButton("Recetar medicamento ");
        prescribeTreatment.addActionListener((ActionEvent e) -> {
            Medication selectedMedication = patientTreatments.getSelectedValue();
            updatePatientRelation(patient.getId(), selectedMedication.getId(), true);
            patientTreatmentsModel.removeElement(selectedMedication);
        });
        panelMedicationRegisrtry.add(scrollPane4, BorderLayout.CENTER);
        panelMedicationRegisrtry.add(prescribeTreatment, BorderLayout.SOUTH);

        // Panel de resumen y registro del Diagnóstico
        JPanel panelDiagnosisSummary = new JPanel(new BorderLayout());
        panelDiagnosisSummary.setBorder(emptyBorder);
        JTextArea textAreaSummary = new JTextArea();
        textAreaSummary.setBorder(BorderFactory.createTitledBorder(" RESUMEN DEL DIAGNÓSTICO "));
        textAreaSummary.setPreferredSize(new Dimension(200,200));
        JButton registerDiagnosis = new JButton("Registrar Diagnóstico");
        registerDiagnosis.setBorder(emptyBorder);
        registerDiagnosis.addActionListener((ActionEvent e) -> {
            Diagnosis diagnosis = new Diagnosis(appointment, patient, appointment.getDoctor(), textAreaSummary.getText());
            patient.getMedicalRecord().add(diagnosis);
        });

        panelDiagnosisSummary.add(textAreaSummary, BorderLayout.CENTER);
        panelDiagnosisSummary.add(registerDiagnosis, BorderLayout.SOUTH);

        panel2.add(panelPatientDiseases);
        panel2.add(panelPatientTreatments);
        panel2.add(panelDiseaseRegistry);
        panel2.add(panelMedicationRegisrtry);
        this.add(panel1, BorderLayout.NORTH);
        this.add(panel2, BorderLayout.CENTER);
        this.add(panelDiagnosisSummary, BorderLayout.SOUTH);
    }

    public static void removePatientRelation (String patientID, String relationID, boolean disease) {
        String additionalFK = (disease) ? "fk_disease_id" : "medication_id";
        String table = (disease) ? "patient_suffers_disease" : "patient_undergoes_treatment";

        PostgrestQuery query = postgrestClient
                .from(table)
                .delete()
                .eq("fk_patient_id", patientID)
                .eq(additionalFK, relationID)
                .getQuery();
        postgrestClient.sendQuery(query);
    }

    public static void updatePatientRelation (String patientID, String relationID, boolean disease) {
        String additionalFK = (disease) ? "fk_disease_id" : "medication_id";
        String table = (disease) ? "patient_suffers_disease" : "patient_undergoes_treatment";

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fk_patient_id", patientID);
        jsonObject.addProperty(additionalFK, relationID);

        PostgrestQuery query = postgrestClient
                .from(table)
                .insert(jsonObject)
                .select()
                .getQuery();
        query.addHeader("Prefer", "resolution=merge-duplicates");
        postgrestClient.sendQuery(query);
    }
}
