package org.deustomed.ui;

import com.toedter.calendar.JMonthChooser;
import com.toedter.calendar.JYearChooser;
import org.deustomed.Appoinment;
import org.deustomed.Doctor;
import org.deustomed.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class WindowDoctor extends JFrame {
    private Doctor doctor;
    private JPanel pnlInfo;
    private JPanel pnlCentral;
    private JPanel pnlAppoinments;
    private Dimension screenSize;
    private JPanel pnlDisplayAppoinments;

    public static void main(String[] args) {
        Patient patient1 = new Patient(1001, "Paciente1", "Surname1", "Surname2", "paciente1@gmail.com", "password", 24);
        ArrayList<Appoinment> appoinments = new ArrayList<>();
        appoinments.add( new Appoinment(patient1, LocalDateTime.of(2023, 1, 1, 12, 0), "Cita consulta", "Cita consulta con paciente"));
        appoinments.add( new Appoinment(patient1, LocalDateTime.of(2023, 1, 1, 12, 0), "Cita consulta", "Cita consulta con paciente"));
        appoinments.add( new Appoinment(patient1, LocalDateTime.of(2023, 1, 1, 12, 0), "Cita consulta", "Cita consulta con paciente"));
        appoinments.add( new Appoinment(patient1, LocalDateTime.of(2023, 1, 1, 12, 0), "Cita consulta", "Cita consulta con paciente"));
        Doctor doctor1 = new Doctor(1000, "Carlos", "Rodriguez", "Martinez", "carlosrodri@gmail.com", "carlosrodriguez", "Medicina Familiar", appoinments);
        WindowDoctor win = new WindowDoctor(doctor1);
        win.setVisible(true);
    }

    public WindowDoctor(Doctor doctor){
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((int) screenSize.getWidth()/4, (int) screenSize.getHeight()/4, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        setLayout(new BorderLayout());
        this.doctor=doctor;



        // ------------------ pnlInfo ------------------
        pnlInfo = new JPanel();
        pnlInfo.setPreferredSize(new Dimension(300,300));
        pnlInfo.setLayout(new BorderLayout());
        JButton btnPhoto = new JButton("");
        ImageIcon icon = new ImageIcon("src/main/java/ui/profileImg.png");
        Image png = icon.getImage().getScaledInstance(200,200, DO_NOTHING_ON_CLOSE);
        btnPhoto.setIcon(new ImageIcon(png));
        btnPhoto.setEnabled(false);
        btnPhoto.setPreferredSize(new Dimension(250,250));
        JPanel p = new JPanel();
        p.add(btnPhoto);
        pnlInfo.add(p, BorderLayout.NORTH);

        FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
        fl.setHgap(30);
        JPanel pnlc = new JPanel(fl);

        JButton info = new JButton("Infomación Personal");
        info.setPreferredSize(new Dimension(200,30));

        JButton info2 = new JButton("Infomación Personal");
        info.setPreferredSize(new Dimension(200,30));

        JButton info3 = new JButton("Infomación Personal");
        info.setPreferredSize(new Dimension(200,30));

        JLabel name = new JLabel("Dr. "+doctor.getName());
        JLabel surname1 = new JLabel(doctor.getSurname1());
        JLabel surname2 = new JLabel(doctor.getSurname2());

        name.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));
        surname1.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));
        surname2.setFont(new Font(name.getFont().getName(), Font.BOLD, 30));

        pnlc.add(name);
        pnlc.add(surname1);
        pnlc.add(surname2);
        pnlc.add(new JLabel("Doctor especialista en "+doctor.getSpeciality()));
        pnlc.add(Box.createVerticalStrut(200));
        pnlc.add(info);
        pnlc.add(info2);
        pnlc.add(info3);

        pnlInfo.add(pnlc, BorderLayout.CENTER);
        JButton singOut = new JButton("Cerrar Sesión");
        pnlInfo.add(singOut, BorderLayout.SOUTH);
        add(pnlInfo, BorderLayout.WEST);

        //--------------------- Panel EAST: Citas (Appointments) --------------------------------
        pnlAppoinments = new JPanel();
        add(pnlAppoinments, BorderLayout.EAST);
        pnlAppoinments.setLayout(new BorderLayout());
        pnlAppoinments.setPreferredSize(new Dimension((int) (screenSize.width/4.1),(int) (screenSize.height*0.75)));
        TitledBorder bordeEast = BorderFactory.createTitledBorder("Citas");
        pnlAppoinments.setBorder(bordeEast);
        JMonthChooser monthChooser = new JMonthChooser();
        pnlDisplayAppoinments = new JPanel();
        JYearChooser yearChooser = new JYearChooser();
        pnlAppoinments.add(pnlDisplayAppoinments, BorderLayout.CENTER);
        JScrollPane sbarPedidos = new JScrollPane(pnlDisplayAppoinments);
        pnlAppoinments.add(sbarPedidos, BorderLayout.CENTER);

        JPanel pnlDateChooser =  new JPanel();
        pnlDateChooser.add(monthChooser);
        pnlDateChooser.add(yearChooser);
        pnlDisplayAppoinments.add(pnlDateChooser, BorderLayout.NORTH);

        JPanel panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(2,2));

        JButton visualizarPedido = new JButton("Visualizar Pedido");
        panelBotones.add(visualizarPedido);
        visualiseAppoinments();

        /*anadirPedido = new JButton("Añadir Pedido");
        anadirPedido.setPreferredSize(new Dimension(80,50));
        panelBotones.add(anadirPedido);
        anadirPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                try {
                    ImageIcon icono= new ImageIcon("src/Proyecto/Imagenes/pedido.png");
                    Image imagen = icono.getImage().getScaledInstance(50, 50, DO_NOTHING_ON_CLOSE);
                    Pedido nuevoPedido = new Pedido(null,null, Integer.parseInt((String) JOptionPane.showInputDialog(null, "Introduce número de mesa: ", "Creando nuevo pedido...", 1, new ImageIcon(imagen), null, null)));
                    pedidos.addItem(nuevoPedido);
                    nuevoPedido.productos = new HashMap<String, Integer>();
                    nuevoPedido.spinners = new ArrayList<JSpinner>();
                }catch(Exception e1) {};}});


        //---------------------------------------------
        finalizarPedido = new JButton("Finalizar Pedido");
        finalizarPedido.setPreferredSize(new Dimension(130,30));
        panelBotones.add(finalizarPedido);
        pnlAppoinments.add(panelBotones, BorderLayout.SOUTH);
        finalizarPedido.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub
                FinalizarPedido finPed = new FinalizarPedido((Pedido) pedidos.getSelectedItem(), usuario, sesion);
                finPed.setVisible(true);
            }
        });

    }*/

}
class CreateRoundButton extends JButton {
    public CreateRoundButton(String label) {
        super(label);
        Dimension size = getPreferredSize();
        size.width = size.height = Math.max(size.width,size.height);
        setPreferredSize(size);

        setContentAreaFilled(false);
    }

    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(Color.lightGray);
        } else {
            g.setColor(getBackground());
        }
        g.fillOval(0, 0, getSize().width-1,getSize().height-1);

        super.paintComponent(g);
    }

    protected void paintBorder(Graphics g) {
        g.setColor(getForeground());
        g.drawOval(0, 0, getSize().width-1,     getSize().height-1);
    }

    Shape shape;
    public boolean contains(int x, int y) {
        if (shape == null ||
                !shape.getBounds().equals(getBounds())) {
            shape = new Ellipse2D.Float(0, 0, getWidth(), getHeight());
        }
        return shape.contains(x, y);
    }

    /*public static void main(String[] args) {
        JButton button = new CreateRoundButton("Click");
        button.setBackground(Color.gray);

        JFrame frame = new JFrame();
        frame.getContentPane().add(button);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setSize(150, 150);
        frame.setVisible(true);
    }*/
}
    public void visualiseAppoinments() {
        pnlDisplayAppoinments.removeAll();
        pnlDisplayAppoinments.updateUI();
        JPanel pnlModApp = new JPanel();
        pnlDisplayAppoinments.add(pnlModApp, BorderLayout.CENTER);
        pnlModApp.setLayout(new GridLayout(40,1));
        for(Appoinment appointment : doctor.getAppointments()) {

                TitledBorder borde = new TitledBorder("");
                JPanel panel = new JPanel();
                panel.setLayout(new BorderLayout(3, 3));
                panel.setPreferredSize(new Dimension(300,150));
                JPanel nombre = new JPanel();
                JPanel pnlButton = new JPanel();

                panel.setBorder(borde);
                Patient pat = appointment.getPatient();
                nombre.setLayout(new BoxLayout(nombre, BoxLayout.Y_AXIS));
                JLabel name = new JLabel(pat.getName()+" "+pat.getSurname1()+" "+pat.getSurname2());
                name.setHorizontalAlignment(SwingConstants.LEFT);
                name.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
                nombre.add(name);

                JPanel pnTa = new JPanel();

                pnTa.setBorder(BorderFactory.createEmptyBorder(0,5,5,0));
                JTextArea ta = new JTextArea(appointment.getShortDesciption());
                ta.setEditable(false);
                ta.setPreferredSize(new Dimension(200,106));
                ta.setBorder(BorderFactory.createTitledBorder(""));
                pnTa.add(ta);
                panel.add(pnTa, BorderLayout.CENTER);

                JButton btn = new JButton("+Info");
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                    }
                });
                pnlButton.add(btn);
                panel.add(nombre, BorderLayout.NORTH);
                panel.add(pnlButton, BorderLayout.EAST);
                pnlModApp.add(panel);
                pnlModApp.updateUI();
                pnlDisplayAppoinments.updateUI();
        }
    }
}