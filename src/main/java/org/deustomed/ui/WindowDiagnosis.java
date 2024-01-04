package org.deustomed.ui;

import org.deustomed.Appointment;
import org.deustomed.Disease;
import org.deustomed.Medication;
import org.deustomed.Patient;
import org.deustomed.postgrest.PostgrestClient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import static org.deustomed.ui.ShowPatientWindow.loadPatientDiseases;
import static org.deustomed.ui.ShowPatientWindow.loadPatientTreatments;
import static org.deustomed.ui.WindowDoctor.capitalizeFirstLetter;

public class WindowDiagnosis extends JFrame {
    private JList<Disease> patientDiseases ;
    private DefaultListModel<Disease> patientDiseaseModel = new DefaultListModel<>();
    private JList<Medication> patientTreatments;
    private DefaultListModel<Medication> patientTreatmentsModel = new DefaultListModel<>();
    private JList<Disease> diseaseRegistry;
    private DefaultListModel<Disease> diseaseRegistryModel = new DefaultListModel<>();
    private JList<Medication> medicationRegistry;
    private DefaultListModel<Medication> medicationRegistryModel = new DefaultListModel<>();
    public static void main(String[] args) {

    }
    public WindowDiagnosis(Appointment appointment, PostgrestClient postgrestClient) {
        Patient patient = appointment.getPatient();
        LocalDateTime date = appointment.getDate();

        setLayout(new BorderLayout());
        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        panel1.setLayout(new GridLayout(1,2));
        panel2.setLayout(new GridLayout(2,2));

        JLabel title = new JLabel("NUEVO DIAGNÓSTICO PARA"+patient.getName()+" "+patient.getSurname1()+" "+patient.getSurname2()+" ");
        JLabel dateLabel = new JLabel(capitalizeFirstLetter(date.getDayOfWeek().toString()) + ", " + date.getDayOfMonth() + " " + capitalizeFirstLetter(date.getMonth().toString()) + " " + date.toLocalTime());
        panel1.add(title);
        panel1.add(dateLabel);

        patientDiseases = new JList<>(patientDiseaseModel);;
        ArrayList<Disease> list = loadPatientDiseases(patient.getId());
        list.forEach( disease -> patientDiseaseModel.addElement(disease));
        JScrollPane scrollPane1 = new JScrollPane(patientDiseases);

        patientTreatments = new JList<>(patientTreatmentsModel);
        ArrayList<Medication> list1 = loadPatientTreatments(patient.getId());
        list1.forEach( treat -> patientTreatmentsModel.addElement(treat));
        JScrollPane scrollPane2 = new JScrollPane(patientTreatments);

        diseaseRegistry = new JList<>(diseaseRegistryModel);
        ArrayList<Disease> list2 = Disease.loadDiseaseRegistry(postgrestClient);
        list2.forEach( disease -> patientDiseaseModel.addElement(disease));
        JScrollPane scrollPane3 = new JScrollPane(patientDiseases);

        medicationRegistry = new JList<>(medicationRegistryModel);
        ArrayList<Medication> list3 = Medication.loadMedicationRegistry(postgrestClient);
        list3.forEach( treat -> patientTreatmentsModel.addElement(treat));
        JScrollPane scrollPane4 = new JScrollPane(patientTreatments);


    }
}
