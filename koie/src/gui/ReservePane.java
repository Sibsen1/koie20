package gui;
import javax.imageio.ImageIO;
import javax.swing.*;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

public class ReservePane extends JPanel{
	private JTextField txtDager;
	private JTextField txtDato;
	GUI g;
	private ArrayList<String> names;
	private JComboBox comboBox;
	private JTextPane labKname;
	private JLabel labOutput;
	private JLabel labReservers;
	private Map<String,Integer> warnings;
	public ReservePane(GUI gui) {
		g = gui;
		setLayout(null);
		
		txtDager = new JTextField();
		txtDager.setText("1");
		txtDager.setBounds(107, 200, 86, 20);
		add(txtDager);
		txtDager.setColumns(10);
		
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(new File("resources/koie_login.jpg")); //Bildet skal oppdateres når man velger mellom
		} catch (IOException e) {											//forskjellige koier
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		picLabel.setBounds(269, 11, 200, 158);
		add(picLabel);
		/*txtDato = new JTextField();
		txtDato.setBounds(107, 239, 86, 20);
		add(txtDato);
		txtDato.setColumns(10);*/
		
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
		datePicker.setBounds(107, 239, 151, 23);
		add(datePicker);
		
		ArrayList<List<Object>> reservationsList = g.CoreClass.getDataBaseColumns("reservations", "koie_idkoie", "date");
		
		
		
		JButton btnBekreft = new JButton("BEKREFT");
		btnBekreft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (g.CoreClass.insertReservation(comboBox.getSelectedItem().toString(),g.CoreClass.userEmail,(Date) datePicker.getModel().getValue())){
					labOutput.setText("Reservasjon regestrert.");
					if (warnings.get(comboBox.getSelectedItem().toString()) != null){
						JOptionPane.showMessageDialog(null, comboBox.getSelectedItem().toString() + " har bare " + warnings.get(comboBox.getSelectedItem().toString()) + " sekker ved igjen! Husk å ta med mer!" );
					}
				}
				else labOutput.setText("Koien er ikke ledig den dagen/de dagene.");
			}
		});
		btnBekreft.setBounds(107, 322, 86, 23);
		add(btnBekreft);
		
		JTextPane labDager = new JTextPane();
		labDager.setEditable(false);
		labDager.setText("Antall dager:");
		labDager.setBounds(21, 200, 86, 20);
		labDager.setBackground(this.getBackground());
		add(labDager);
		
		JTextPane labDato = new JTextPane();
		labDato.setEditable(false);
		labDato.setText("Dato:");
		labDato.setBounds(54, 239, 43, 20);
		labDato.setBackground(this.getBackground());
		add(labDato);
		
		JTextPane labKoie = new JTextPane();
		labKoie.setEditable(false);
		labKoie.setText("Koie:");
		labKoie.setBounds(54, 281, 43, 20);
		labKoie.setBackground(this.getBackground());
		add(labKoie);
		
		names = new ArrayList<String>();
		for (List<Object> o:g.CoreClass.getDataBaseColumns("koie", "koienavn")){
			names.add(o.get(0).toString());
		}
		comboBox = new JComboBox();
		for (Object o: names){
			comboBox.addItem(o);
		}
		ActionListener cbActionListener = new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {
            	labKname.setText(names.get(comboBox.getSelectedIndex()));
            	labReservers.setText("<html>"+comboBox.getSelectedItem().toString()+"<br>");
            	for (int i = 0; i < reservationsList.size(); i++) {
        			
        			if ((int) reservationsList.get(i).get(0) == g.CoreClass.getKoieID(comboBox.getSelectedItem().toString())) {
        				Calendar cal = new GregorianCalendar();
        				cal.setTime((Date) reservationsList.get(i).get(1));
        				labReservers.setText(labReservers.getText() + " er reservert den "+ cal.getTime().toString()+"<br>");
        			}
        		}
            	labReservers.setText(labReservers.getText()+"</html>");
           }
        };
        comboBox.addActionListener(cbActionListener);
		comboBox.setBounds(107, 281, 86, 20);
		add(comboBox);
		
		JLabel labReserve = new JLabel();
		//labReserve.setEditable(false);
		labReserve.setFont(new Font("Tahoma", Font.PLAIN, 26));
		labReserve.setText("Reserver");
		labReserve.setBounds(10, 130, 188, 39);
		labReserve.setBackground(this.getBackground());
		add(labReserve);
		
		labKname = new JTextPane();
		labKname.setBounds(392, 162, 86, 20);
		labKname.setBackground(this.getBackground());
		add(labKname);
		
		labOutput = new JLabel();
		labOutput.setBounds(203, 326, 161, 19);
		add(labOutput);
		
		labReservers = new JLabel();
		labReservers.setBounds(273, 203, 218, 142);
		add(labReservers);
	}
	public void setWarnings(Map<String, Integer> koiewood) {
		warnings = koiewood;
	}
}
