package org.deustomed.ui;

import com.google.gson.JsonElement;
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
    private ArrayList<Medication> prescribedMedication;
    private ArrayList<Medication> retiredMedication;
    private ArrayList<Disease> diagnosedDiseases;
    private ArrayList<Disease> curedDiseases;

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

        JLabel title = new JLabel("NUEVO DIAGNÓSTICO para " + patient.getName() + " " + patient.getSurname1() + " " + patient.getSurname2() + " ");
        Font font = new Font(title.getFont().getName(), Font.BOLD, title.getFont().getSize());
        title.setFont(font);
        JLabel dateLabel = new JLabel(capitalizeFirstLetter(date.getDayOfWeek().toString()) + ", " + date.getDayOfMonth() + " " + capitalizeFirstLetter(date.getMonth().toString()) + " " + date.toLocalTime());
        dateLabel.setFont(font);
        dateLabel.setHorizontalAlignment(JLabel.RIGHT);

        panel1.add(title);
        panel1.add(dateLabel);
        title.setBorder(emptyBorder);
        dateLabel.setBorder(emptyBorder);

        // Panel para dar de alta enfermedades del paciente
        JPanel panelPatientDiseases = new JPanel(new BorderLayout());
        panelPatientDiseases.setBorder(emptyBorder);
        patientDiseases = new JList<>(patientDiseaseModel);
        patientDiseases.setBorder(BorderFactory.createTitledBorder("Enfermedades actuales del paciente"));
        ArrayList<Disease> list = loadPatientDiseases(patient.getId(), postgrestClient);
        list.forEach(disease -> patientDiseaseModel.addElement(disease));
        JScrollPane scrollPane1 = new JScrollPane(patientDiseases);

        prescribedMedication = new ArrayList<>();
        retiredMedication = new ArrayList<>();
        diagnosedDiseases = new ArrayList<>();
        curedDiseases = new ArrayList<>();

        JButton dischargeDisease = new JButton("Dar enfermedad de alta");
        dischargeDisease.addActionListener((ActionEvent e) -> {
            Disease selectedDisease;
            if(patientDiseases.getSelectedValue()==null){
                JOptionPane.showMessageDialog(this, "Debe seleccionar una enfermedad para darla de alta", "Enfermedad no seleccionada", JOptionPane.INFORMATION_MESSAGE);
            } else{
                selectedDisease = patientDiseases.getSelectedValue();
                //removePatientRelation(patient.getId(), selectedDisease.getId(), true);
                patientDiseaseModel.removeElement(selectedDisease);
                this.curedDiseases.add(selectedDisease);
            }
        });

        panelPatientDiseases.add(scrollPane1, BorderLayout.CENTER);
        panelPatientDiseases.add(dischargeDisease, BorderLayout.SOUTH);

        // Panel para retirar medicamentos al paciente
        JPanel panelPatientTreatments = new JPanel(new BorderLayout());
        panelPatientTreatments.setBorder(emptyBorder);
        patientTreatments = new JList<>(patientTreatmentsModel);
        patientTreatments.setBorder(BorderFactory.createTitledBorder("Medicación actuales del paciente"));
        ArrayList<Medication> list1 = loadPatientTreatments(patient.getId(), postgrestClient);
        list1.forEach(treat -> patientTreatmentsModel.addElement(treat));
        JScrollPane scrollPane2 = new JScrollPane(patientTreatments);

        JButton withdrawTreatment = new JButton("Retirar medicamento ");
        withdrawTreatment.addActionListener((ActionEvent e) -> {
            Medication selectedMedication;
            if(patientTreatments.getSelectedValue()==null){
                JOptionPane.showMessageDialog(this, "Debe seleccionar un medicamento para retirarlo", "Medicamento no seleccionada", JOptionPane.INFORMATION_MESSAGE);
            } else{
                selectedMedication = patientTreatments.getSelectedValue();
                //removePatientRelation(patient.getId(), selectedMedication.getId(), false);
                patientTreatmentsModel.removeElement(selectedMedication);
                this.retiredMedication.add(selectedMedication);
            }
        });

        panelPatientTreatments.add(scrollPane2, BorderLayout.CENTER);
        panelPatientTreatments.add(withdrawTreatment, BorderLayout.SOUTH);

        // Panel para diagnosticar nuevas enfermedades al paciente
        JPanel panelDiseaseRegistry = new JPanel(new BorderLayout());
        panelDiseaseRegistry.setBorder(emptyBorder);

        diseaseRegistry = new JList<>(diseaseRegistryModel);
        diseaseRegistry.setBorder(BorderFactory.createTitledBorder("Registro de enfermedades"));
        ArrayList<Disease> list2 = Disease.loadDiseaseRegistry(postgrestClient);
        list2.forEach(disease -> diseaseRegistryModel.addElement(disease));
        JScrollPane scrollPane3 = new JScrollPane(diseaseRegistry);

        JButton diagnoseDisease = new JButton("Diagnosticar enfermedad ");
        diagnoseDisease.addActionListener((ActionEvent e) -> {
            Disease selectedDisease;
            if(diseaseRegistry.getSelectedValue()==null){
                JOptionPane.showMessageDialog(this, "Debe seleccionar una enfermedad para diagnosticarla", "Enfermedad no seleccionada", JOptionPane.INFORMATION_MESSAGE);
            } else{
                selectedDisease = diseaseRegistry.getSelectedValue();
                //updatePatientRelation(patient.getId(), selectedDisease.getId(), true);
                patientDiseaseModel.addElement(selectedDisease);
                this.diagnosedDiseases.add(selectedDisease);
            }
        });

        panelDiseaseRegistry.add(scrollPane3, BorderLayout.CENTER);
        panelDiseaseRegistry.add(diagnoseDisease, BorderLayout.SOUTH);

        // Panel para recetar medicamentos al paciente
        JPanel panelMedicationRegisrtry = new JPanel(new BorderLayout());
        panelMedicationRegisrtry.setBorder(emptyBorder);
        medicationRegistry = new JList<>(medicationRegistryModel);
        medicationRegistry.setBorder(BorderFactory.createTitledBorder("Registro de medicamentos"));
        ArrayList<Medication> list3 = Medication.loadMedicationRegistry(postgrestClient);
        list3.forEach(treat -> medicationRegistryModel.addElement(treat));
        JScrollPane scrollPane4 = new JScrollPane(medicationRegistry);

        JButton prescribeTreatment = new JButton("Recetar medicamento ");
        prescribeTreatment.addActionListener((ActionEvent e) -> {
            Medication selectedMedication;
            if(medicationRegistry.getSelectedValue()==null){
                JOptionPane.showMessageDialog(this, "Debe seleccionar un medicamento para recetarlo", "Medicamento no seleccionada", JOptionPane.INFORMATION_MESSAGE);
            } else{
                selectedMedication = medicationRegistry.getSelectedValue();
                //updatePatientRelation(patient.getId(), selectedMedication.getId(), false);
                patientTreatmentsModel.addElement(selectedMedication);
                this.prescribedMedication.add(selectedMedication);
            }
        });

        panelMedicationRegisrtry.add(scrollPane4, BorderLayout.CENTER);
        panelMedicationRegisrtry.add(prescribeTreatment, BorderLayout.SOUTH);

        // Panel de resumen y registro del Diagnóstico
        JPanel panelDiagnosisSummary = new JPanel(new BorderLayout());
        panelDiagnosisSummary.setBorder(emptyBorder);
        JTextArea textAreaSummary = new JTextArea("'Notas del doctor'");
        textAreaSummary.setBorder(BorderFactory.createTitledBorder(" RESUMEN DEL DIAGNÓSTICO "));
        textAreaSummary.setPreferredSize(new Dimension(200,200));
        JButton registerDiagnosis = new JButton("Registrar Diagnóstico");

        registerDiagnosis.setBorder(emptyBorder);
        registerDiagnosis.addActionListener((ActionEvent e) -> {
            Diagnosis diagnosis = new Diagnosis(appointment, patient, appointment.getDoctorId(), textAreaSummary.getText(), prescribedMedication, retiredMedication, diagnosedDiseases, curedDiseases);
            checkLists();
            curedDiseases.forEach( d -> removePatientRelation(patient.getId(), d.getId(), true));
            diagnosedDiseases.forEach( d -> updatePatientRelation(patient.getId(), d.getId(), true));
            retiredMedication.forEach( m -> removePatientRelation(patient.getId(), m.getId(), false));
            prescribedMedication.forEach( m -> updatePatientRelation(patient.getId(), m.getId(), false));
            registerDiagnosis(diagnosis);
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

    public static void removePatientRelation (String patientID, int relationID, boolean disease) {
        String additionalFK = (disease) ? "fk_disease_id" : "medication_id";
        String table = (disease) ? "patient_suffers_disease" : "patient_undergoes_treatment";

        PostgrestQuery query = postgrestClient
                .from(table)
                .delete()
                .eq("fk_patient_id", patientID)
                .eq(additionalFK, String.valueOf(relationID))
                .getQuery();

        //postgrestClient.sendQuery(query);
        System.out.println("Remove anwser: "+String.valueOf(postgrestClient.sendQuery(query)));
    }

    public static void updatePatientRelation (String patientID, int relationID, boolean disease) {
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

        //postgrestClient.sendQuery(query);
        System.out.println(String.valueOf(postgrestClient.sendQuery(query)));
    }

    private void checkLists() {
        ArrayList<Disease> repetedDiseases = new ArrayList<>();
        ArrayList<Medication> repetedMedication = new ArrayList<>();
        diagnosedDiseases.forEach( d -> {
            if(curedDiseases.contains(d)){
                repetedDiseases.add(d);
            }
        });
        prescribedMedication.forEach(m -> {
            if(retiredMedication.contains(m)){
                repetedMedication.add(m);
            }
        });
        repetedDiseases.forEach( d -> {
            curedDiseases.remove(d);
            diagnosedDiseases.remove(d);
        });
        repetedMedication.forEach( m -> {
            retiredMedication.remove(m);
            prescribedMedication.remove(m);
        });
    }
    public static void registerDiagnosis (Diagnosis diagnosis) {
        JsonObject jsonObject = new JsonObject();
        System.out.println(diagnosis.getDoctor());
        jsonObject.addProperty("fk_doctor_id", diagnosis.getDoctor());
        jsonObject.addProperty("fk_patient_id", diagnosis.getPatient().getId());
        jsonObject.addProperty("summary", diagnosis.getSummary());

        PostgrestQuery query = postgrestClient
                .from("diagnosis")
                .insert(jsonObject)
                .select()
                .getQuery();

        //postgrestClient.sendQuery(query);
        System.out.println(String.valueOf(postgrestClient.sendQuery(query)));
    }
}
