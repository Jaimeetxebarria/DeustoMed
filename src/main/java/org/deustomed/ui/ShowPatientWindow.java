package org.deustomed.ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.deustomed.*;
import org.deustomed.authentication.SuperuserAuthenticationService;
import org.deustomed.postgrest.PostgrestClient;
import org.deustomed.postgrest.PostgrestQuery;
import org.jetbrains.annotations.Nls;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

class ShowPatientWindow extends JFrame {
    private JPanel panelWest;
    private JPanel pnlCenter;
    private JList<Disease> patientDiseases;
    private DefaultListModel<Disease> patientDiseaseModel;
    private JList<Medication> patientTreatments;
    private DefaultListModel<Medication> patientTreatmentsModel;
    private DiagnosesTableModel clinicalRecordModel;
    private JTable clinicalRecord;
    private static PostgrestClient postgrestClient;
    private Patient classPatient;

    public static void main(String[] args) {
        Patient patient = new Patient("00AAK", "Antonio", "Gonzalez", "Gonzalez", LocalDate.now(), Sex.MALE,
                "12345678A", "mail@gmail.vpm", "Calle Dirección Inventada", "Speciality", null);

        ConfigLoader configLoader = new ConfigLoader();
        PostgrestClient postgrestClient = new PostgrestClient(configLoader.getHostname(),
                configLoader.getEndpoint(), new SuperuserAuthenticationService(configLoader.getAnonymousToken(),
                configLoader.getSuperuserToken()));
        ShowPatientWindow spw = new ShowPatientWindow(patient, postgrestClient);
        spw.setVisible(true);
        spw.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    public ShowPatientWindow(Patient patient, PostgrestClient postgrestClient) {
        this.classPatient = patient;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setTitle("Ventana del paciente " + patient.getName() + " " + patient.getSurname1() + " " + patient.getSurname2());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth() / 4 - 110, (int) screenSize.getHeight() / 4 - 90, (int) (screenSize.getWidth() / 2 + 220), (int) (screenSize.getHeight() / 2 + 180));
        setLayout(new BorderLayout());
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        ShowPatientWindow.postgrestClient = postgrestClient;
        setResizable(false);

        // ----------------------- Nombre+Apellidos (west) -------------------
        panelWest = new JPanel();
        panelWest.setPreferredSize(new Dimension(200, 200));
        panelWest.setLayout(new BorderLayout());
        JButton btnPhoto = new JButton("");
        ImageIcon icon = new ImageIcon("src/main/java/ui/profileImg.png");
        Image png = icon.getImage().getScaledInstance(150, 150, DO_NOTHING_ON_CLOSE);
        btnPhoto.setIcon(new ImageIcon(png));
        btnPhoto.setEnabled(false);
        btnPhoto.setPreferredSize(new Dimension(150, 150));
        JPanel p = new JPanel();
        p.add(btnPhoto);
        panelWest.add(p, BorderLayout.NORTH);
        add(panelWest, BorderLayout.WEST);

        FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
        fl.setHgap(30);
        JPanel pnlTitle = new JPanel(fl);

        JLabel name = new JLabel(patient.getName());
        JLabel surname1 = new JLabel(patient.getSurname1());
        JLabel surname2 = new JLabel(patient.getSurname2());

        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));
        surname1.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));
        surname2.setFont(new Font(name.getFont().getName(), Font.BOLD, 20));

        pnlTitle.add(name);
        pnlTitle.add(surname1);
        pnlTitle.add(surname2);
        String doctorName = findDoctorName(patient);
        pnlTitle.add(new JLabel("<html>Paciente del doctor<html>"));
        pnlTitle.add(new JLabel("" + doctorName));
        panelWest.add(pnlTitle);

        // ----------------------- Mostrar información del paciente (CENTER) -------------------
        // ----------------------- Información general del paciente (CENTER + north) -------------------
        pnlCenter = new JPanel();
        Border emptyBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        pnlCenter.setBorder(emptyBorder);
        pnlCenter.setLayout(new BorderLayout());


        JPanel pnlInfo = new JPanel();
        pnlInfo.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JPanel pnlInfoPatient = new JPanel();
        pnlInfoPatient.setLayout(new GridLayout(4, 2));
        Border titleBorder = BorderFactory.createTitledBorder("Información del Paciente");
        pnlInfoPatient.setBorder(titleBorder);

        JPanel pnlContactPatient = new JPanel();
        pnlContactPatient.setLayout(new GridLayout(4, 2));
        Border titleBorder1 = BorderFactory.createTitledBorder("Información de contacto");
        pnlContactPatient.setBorder(titleBorder1);

        JLabel age = new JLabel("Age: ");
        JLabel sex = new JLabel("Sex: ");
        JLabel dni = new JLabel("DNI: ");
        JLabel birthdate = new JLabel("F. Nacimiento:      ");

        JLabel phone = new JLabel("Tlf.: ");
        JLabel email = new JLabel("Email: ");
        JLabel address = new JLabel("Dirección:             ");

        JTextField tfAge = new JTextField("Age");
        JTextField tfSex = new JTextField("Sex");
        JTextField tfDNI = new JTextField("DNI");
        JTextField tfBirthdate = new JTextField("Birthdate");

        JTextField tfPhone = new JTextField("Phone");
        JTextField tfEmail = new JTextField("Email");
        JTextField tfAddress = new JTextField("Address");

        tfAge.setEditable(false);
        tfAge.setText(classPatient.getAgeInYears() + "");
        tfSex.setEditable(false);
        String stringSex = (classPatient.getSex().equals(Sex.MALE) ? "Male" : "Female");
        tfSex.setText(stringSex);
        tfDNI.setEditable(false);
        tfDNI.setText(classPatient.getDni());
        tfBirthdate.setEditable(false);
        LocalDate date = classPatient.getBirthDate();
        tfBirthdate.setText(date.toString());

        tfPhone.setEditable(false);
        tfPhone.setText(classPatient.getPhoneNumber());
        tfEmail.setEditable(false);
        tfEmail.setText(classPatient.getEmail());
        tfAddress.setEditable(false);
        tfAddress.setText(classPatient.getAddress());

        pnlInfoPatient.add(age);
        pnlInfoPatient.add(tfAge);
        pnlInfoPatient.add(birthdate);
        pnlInfoPatient.add(tfBirthdate);
        pnlInfoPatient.add(sex);
        pnlInfoPatient.add(tfSex);
        pnlInfoPatient.add(dni);
        pnlInfoPatient.add(tfDNI);

        pnlContactPatient.add(phone);
        pnlContactPatient.add(tfPhone);
        pnlContactPatient.add(email);
        pnlContactPatient.add(tfEmail);
        pnlContactPatient.add(address);
        pnlContactPatient.add(tfAddress);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        pnlInfo.add(pnlInfoPatient, gbc);
        gbc.gridx++;
        pnlInfo.add(pnlContactPatient, gbc);
        pnlCenter.add(pnlInfo, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);

        // ----------------------- Información CLÍNICA del paciente (CENTER + center) -------------------
        JPanel pnlClinicalInfo = new JPanel();
        pnlClinicalInfo.setLayout(new BorderLayout());
        pnlClinicalInfo.setBorder(emptyBorder);
        JLabel clinicInfoTitle = new JLabel("INFORMACIÓN CLÍNICA DEL PACIENTE");
        JPanel subPnl = new JPanel();
        subPnl.setLayout(new GridBagLayout());

        JPanel pnlDiseases = new JPanel();
        Border titleBorder2 = BorderFactory.createTitledBorder("Enfermedades actuales");
        pnlDiseases.setBorder(titleBorder2);

        JPanel pnlTreatments = new JPanel();
        Border titleBorder3 = BorderFactory.createTitledBorder("Tratamientos actuales");
        pnlTreatments.setBorder(titleBorder3);

        patientDiseaseModel = new DefaultListModel<>();
        patientDiseases = new JList<>(patientDiseaseModel);
        patientDiseases.setPreferredSize(new Dimension(220, 100));
        patientDiseases.setCellRenderer(new ListCellRenderer());
        JScrollPane scpDiseases = new JScrollPane(patientDiseases);

        ArrayList<Disease> list = loadPatientDiseases(patient.getId(), postgrestClient);
        list.forEach( disease -> patientDiseaseModel.addElement(disease));

        patientTreatmentsModel = new DefaultListModel<>();
        patientTreatments = new JList<>(patientTreatmentsModel);
        patientTreatments.setPreferredSize(new Dimension(220, 100));
        patientTreatments.setCellRenderer(new ListCellRenderer());
        JScrollPane scpTreatments = new JScrollPane(patientTreatments);

        ArrayList<Medication> listTreatments = loadPatientTreatments(patient.getId(), postgrestClient);
        listTreatments.forEach( treatment -> patientTreatmentsModel.addElement(treatment));

        pnlDiseases.add(scpDiseases);
        pnlTreatments.add(scpTreatments);
        pnlClinicalInfo.add(clinicInfoTitle, BorderLayout.NORTH);
        subPnl.setBorder(emptyBorder);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        subPnl.add(pnlDiseases, gbc);
        gbc.gridx++;
        subPnl.add(pnlTreatments, gbc);
        pnlClinicalInfo.add(subPnl, BorderLayout.CENTER);
        pnlCenter.add(pnlClinicalInfo, BorderLayout.CENTER);

        // ----------------------- Información HISTORIAL MÉDICO del paciente (CENTER + south) -------------------
        ArrayList<Diagnosis> diagnoses = loadPatientMedicalRecord(patient.getId(), postgrestClient);
        clinicalRecordModel = new DiagnosesTableModel(diagnoses);
        clinicalRecord = new JTable(clinicalRecordModel);

        JScrollPane scpClinicalRecord = new JScrollPane(clinicalRecord);
        scpClinicalRecord.setPreferredSize(new Dimension(700, 150));
        JPanel subPanel = new JPanel();

        JPanel pnlClinicalRecord = new JPanel();
        pnlClinicalRecord.setLayout(new BorderLayout());
        JLabel clinicRecordTitle = new JLabel("HISTORIAL MÉDICO DEL PACIENTE");
        pnlClinicalRecord.add(clinicRecordTitle, BorderLayout.NORTH);
        subPanel.add(scpClinicalRecord);
        pnlClinicalRecord.add(subPanel, BorderLayout.CENTER);
        pnlCenter.add(pnlClinicalRecord, BorderLayout.SOUTH);
    }

    public static ArrayList<Disease> loadPatientDiseases(String id, PostgrestClient postgrestClient) {
        ArrayList<Disease> resultList = new ArrayList<>();
        PostgrestQuery query = postgrestClient
                .from("patient_suffers_disease")
                .select("fk_disease_id")
                .eq("fk_patient_id", id)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                String diseaseID = jsonObject1.get("fk_disease_id").getAsString();
                PostgrestQuery queryDisease = postgrestClient
                        .from("disease")
                        .select("*")
                        .eq("id", diseaseID)
                        .getQuery();

                JsonArray jsonArrayDisease = postgrestClient.sendQuery(queryDisease).getAsJsonArray();

                for (int j = 0; j < jsonArrayDisease.size(); j++) {
                    JsonObject jsonObject = jsonArrayDisease.get(j).getAsJsonObject();
                    int disease_id = jsonObject.get("id").getAsInt();
                    String name = jsonObject.get("name").getAsString();
                    String chronic = jsonObject.get("chronic").getAsString();
                    String hereditary = jsonObject.get("hereditary").getAsString();

                    boolean chronicB = chronic.equals("TRUE");
                    boolean hereditaryB = hereditary.equals("TRUE");

                    Disease newDisease = new Disease(disease_id, name, chronicB, hereditaryB);
                    resultList.add(newDisease);

                }
            }
        }
        return resultList;
    }

    public static ArrayList<Medication> loadPatientTreatments(String id, PostgrestClient postgrestClient) {
        ArrayList<Medication> resultList = new ArrayList<>();
        PostgrestQuery query = postgrestClient
                .from("patient_undergoes_treatment")
                .select("fk_medication_id")
                .eq("fk_patient_id", id)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                String treatmentID = jsonObject1.get("fk_medication_id").getAsString();
                PostgrestQuery queryTreatment = postgrestClient
                        .from("medication")
                        .select("*")
                        .eq("id", treatmentID)
                        .getQuery();

                JsonArray jsonArrayTreatment = postgrestClient.sendQuery(queryTreatment).getAsJsonArray();

                for (int j = 0; j < jsonArrayTreatment.size(); j++) {
                    JsonObject jsonObject = jsonArrayTreatment.get(j).getAsJsonObject();

                    int medication_id = jsonObject.get("id").getAsInt();
                    String activesubstance = jsonObject.get("activesubstance").getAsString();
                    String commercialName = jsonObject.get("commercialname").getAsString();
                    int stock = jsonObject.get("stock").getAsInt();
                    double dose = jsonObject.get("dose").getAsDouble();
                    String company = jsonObject.get("company").getAsString();
                    String shortDescription = jsonObject.get("shortdescription").getAsString();

                    Medication medication = new Medication(medication_id, activesubstance, commercialName, stock, dose, company, shortDescription);
                    resultList.add(medication);

                }
            }
        }
        return resultList;
    }

    public static String findDoctorName(Patient patient) {
        PostgrestQuery query = postgrestClient
                .from("patient")
                .select("fk_doctor_id")
                .eq("id", patient.getId())
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();
        JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();

        String doctorId = jsonObject.get("fk_doctor_id").getAsString();

        PostgrestQuery query1 = postgrestClient
                .from("doctor_with_personal_data")
                .select("name", "surname1", "surname2")
                .eq("id", doctorId)
                .getQuery();

        JsonArray jsonArray1 = postgrestClient.sendQuery(query1).getAsJsonArray();
        JsonObject jsonObject1 = jsonArray1.get(0).getAsJsonObject();

        String name = jsonObject1.get("name").getAsString();
        String surname1 = jsonObject1.get("surname1").getAsString();
        String surname2 = jsonObject1.get("surname2").getAsString();

        return name + " " + surname1 + " " + surname2;
    }

    /**
     * Loads the medial record of a given patient in the form of a list of diagnoses. For each diagnose it also loads
     * the corresponding appointment, which contains valuable information related to the diagnosis.
     *
     * @param id the id of the given patient whose medical record (diagnoses) are going to be loadded
     * @return An ArrayList<Diagnosis> with the diagnoses of the patient.
     */
    public static ArrayList<Diagnosis> loadPatientMedicalRecord (String id, PostgrestClient postgrestClient) {
        ArrayList<Diagnosis> resultList = new ArrayList<>();
        PostgrestQuery query = postgrestClient
                .from("diagnosis")
                .select("*")
                .eq("fk_patient_id", id)
                .getQuery();

        JsonArray jsonArray = postgrestClient.sendQuery(query).getAsJsonArray();

        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject1 = jsonArray.get(i).getAsJsonObject();
                int diagnosis_id = jsonObject1.get("id").getAsInt();
                String fk_doctor_id = jsonObject1.get("fk_doctor_id").getAsString();
                String fk_patient_id = jsonObject1.get("fk_patient_id").getAsString();
                String summary = jsonObject1.get("summary").getAsString();
                String dateString = jsonObject1.get("date").getAsString();
                LocalDateTime date = LocalDateTime.parse(dateString, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));

                PostgrestQuery queryAppointment = postgrestClient
                        .from("appointment_with_type")
                        .select("*")
                        .eq("fk_doctor_id", fk_doctor_id)
                        .eq("fk_patient_id",fk_patient_id)
                        .eq("date", dateString)
                        .getQuery();

                JsonArray jsonArrayAppointment = postgrestClient.sendQuery(queryAppointment).getAsJsonArray();
                JsonObject jsonObject2 = jsonArrayAppointment.get(0).getAsJsonObject();

                Appointment a = new Appointment(fk_patient_id, fk_doctor_id, date,
                        "Cita de carácter "+jsonObject2.get("appointment_type").getAsString(),
                        jsonObject2.get("reason").getAsString()

                );

                Diagnosis newDiagnosis = new Diagnosis( a, fk_patient_id, fk_doctor_id, summary,
                        Diagnosis.loadDiagnosisMedication(diagnosis_id, postgrestClient, true), Diagnosis.loadDiagnosisMedication(diagnosis_id, postgrestClient, false),
                        Diagnosis.loadDiagnosisDiseases(diagnosis_id, postgrestClient, true), Diagnosis.loadDiagnosisDiseases(diagnosis_id, postgrestClient, false));

                resultList.add(newDiagnosis);
            }
        }
        return resultList;
    }
} class DiagnosesTableModel extends AbstractTableModel {
    ArrayList<Diagnosis> diagnoses;
    String[] cNames = {"Doctor", "Fecha", "Diagnosticado", "Dado de alta", "Recetado", "Retirado", "Resumen"};
    DiagnosesTableModel(ArrayList<Diagnosis> diagnoses) {
        this.diagnoses = new ArrayList<>(diagnoses);
    }
    @Override
    public int getRowCount() {
        return diagnoses.size();
    }
    @Override
    public int getColumnCount() {
        return cNames.length;
    }
    @Nls
    @Override
    public String getColumnName(int columnIndex) {
        return cNames[columnIndex];
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Diagnosis d;
        try {
            d = this.diagnoses.get(rowIndex);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        StringBuilder diagnosed = new StringBuilder();
        for(Disease disease: d.getDiagnosedDiseases()) {diagnosed.append(disease.getName()).append("; ");};

        StringBuilder cured = new StringBuilder();
        for(Disease disease: d.getCuredDiseases()) {cured.append(disease.getName()).append("; ");};

        StringBuilder prescribed = new StringBuilder();
        for(Medication m: d.getPrescribedMedication()) {prescribed.append(m.getCommercialName()).append("; ");};

        StringBuilder retired = new StringBuilder();
        for(Medication m: d.getRetiredMedication()) {retired.append(m.getCommercialName()).append("; ");};

        return switch (columnIndex) {
            case 0 -> d.getDoctor();
            case 1 -> d.getAppointment().getDate()+"";
            case 2 -> diagnosed.toString();
            case 3 -> cured.toString();
            case 4 -> prescribed.toString();
            case 5 -> retired.toString();
            case 6 -> d.getSummary();

            default -> null;
        };
    }
}