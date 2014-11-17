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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
	private Map<String,List<String>> utstyr;
	private ArrayList<List<Object>> reservationsList;
	private ArrayList<List<Object>> ki;
	private ArrayList<String> sengeplassr;
	private HashMap<String, String> fraktes;
	private JScrollPane jspR;
	public ReservePane(GUI gui) {
		g = gui;
		setLayout(null);
		
		txtDager = new JTextField();
		txtDager.setText("1");
		txtDager.setBounds(123, 200, 86, 20);
		add(txtDager);
		txtDager.setColumns(10);
		
		BufferedImage myPicture = null;
		try {
			myPicture = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("koie_login.jpg")); //Bildet skal oppdateres når man velger mellom
		} catch (IOException e) {											//forskjellige koier
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JLabel picLabel = new JLabel(new ImageIcon(myPicture));
		picLabel.setBounds(269, 11, 200, 158);
		add(picLabel);
		
		this.warnings = new HashMap<String,Integer>();
		
		UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
		datePicker.setBounds(123, 239, 151, 23);
		add(datePicker);
		
		reservationsList = g.CoreClass.getDataBaseColumns("reservations", "koie_idkoie", "date","antall_personer");
		
		
		
		JButton btnBekreft = new JButton("BEKREFT");
		btnBekreft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (datePicker.getModel().getValue() != null && Integer.parseInt(txtDager.getText())!= 0){
					if (g.CoreClass.insertReservation(comboBox.getSelectedItem().toString(),g.CoreClass.userEmail,(Date) datePicker.getModel().getValue(),Integer.parseInt(txtDager.getText()))){
						labOutput.setText("Reservasjon regestrert.");
						
						if (warnings.get(comboBox.getSelectedItem().toString()) != null){
							JOptionPane.showMessageDialog(null, comboBox.getSelectedItem().toString() + " har bare " + warnings.get(comboBox.getSelectedItem().toString()) + " sekker ved igjen! Husk å ta med mer!" );
						}
						
						if (!fraktes.get(comboBox.getSelectedItem().toString()).trim().equals(",") && fraktes.get(comboBox.getSelectedItem().toString()).length() != 0) {
							JOptionPane.showMessageDialog(null, "<html><body><p style='width: 200px;'>Det trengs å fraktes nytt utstyr til "+comboBox.getSelectedItem().toString()+" koien: "+fraktes.get(comboBox.getSelectedItem().toString())+"</body></html>");
							
							String koieNavn = comboBox.getSelectedItem().toString();
							
							Calendar cal = Calendar.getInstance();
							cal.setTime((Date) datePicker.getModel().getValue());
							int resYear = cal.get(Calendar.YEAR);
							int resMonth = cal.get(Calendar.MONTH)+1;
							int resDay = cal.get(Calendar.DAY_OF_MONTH);
							
							g.CoreClass.SendEmailToReservations(koieNavn, 
									resYear, resMonth, resDay, 
									"Utstyr må fraktes til " + koieNavn + " [Automatisk e-mail]", 
									"Hei,\n\n"
											+ "Dette er en e-mail som er sent for å minne deg på at du har en reservasjon på NTNUI sin koie "
											+ "'" + koieNavn+ "'. Denne koien har nå utstyr som må fraktes opp, og vi ber deg vennligst om å "
											+ "ta dette med deg når du reiser opp "+ resDay+"."+resMonth+"."+resYear+".\n\n"
											+ "Hilsen,\n koie-avdelingen på NTNUI");
						}
					}
					else labOutput.setText("Koien er ikke ledig den dagen/de dagene.");
				}
				else labOutput.setText("Velg dato.");
			}
		});
		btnBekreft.setBounds(123, 322, 86, 23);
		add(btnBekreft);
		
		JTextPane labDager = new JTextPane();
		labDager.setEditable(false);
		labDager.setText("Antall personer:");
		labDager.setBounds(10, 200, 125, 20);
		labDager.setBackground(this.getBackground());
		add(labDager);
		
		JTextPane labDato = new JTextPane();
		labDato.setEditable(false);
		labDato.setText("Dato:");
		labDato.setBounds(70, 239, 43, 20);
		labDato.setBackground(this.getBackground());
		add(labDato);
		
		JTextPane labKoie = new JTextPane();
		labKoie.setEditable(false);
		labKoie.setText("Koie:");
		labKoie.setBounds(70, 281, 43, 20);
		labKoie.setBackground(this.getBackground());
		add(labKoie);
		
		names = new ArrayList<String>();
		sengeplassr = new ArrayList<String>();
		fraktes = new HashMap<String,String>();
		ki = g.CoreClass.getDataBaseColumns("koie", "koienavn","sengeplasser","fraktes");
		for (List<Object> o:ki){
			names.add(o.get(0).toString());
			sengeplassr.add(o.get(1).toString());
			fraktes.put(o.get(0).toString(),o.get(2).toString());
		}
		comboBox = new JComboBox();
		for (Object o: names){
			comboBox.addItem(o);
		}
		ActionListener cbActionListener = new ActionListener() {//add actionlistner to listen for change
            @Override
            public void actionPerformed(ActionEvent e) {
            	labKname.setText(names.get(comboBox.getSelectedIndex())+" - maks "+sengeplassr.get(comboBox.getSelectedIndex())+" sengeplasser.");
            	labReservers.setText("<html>"+comboBox.getSelectedItem().toString()+"<br>");
            	Map<String,Integer> rsrvs = new HashMap<String,Integer>();
            	for (int i = 0; i < reservationsList.size(); i++) {
        			
        			if ((int) reservationsList.get(i).get(0) == g.CoreClass.getKoieID(comboBox.getSelectedItem().toString())) {
        				Calendar cal = new GregorianCalendar();
        				cal.setTime((Date) reservationsList.get(i).get(1));
        				if (rsrvs.get(cal.getTime().toString())==null){
        					rsrvs.put(cal.getTime().toString(),Integer.parseInt(reservationsList.get(i).get(2).toString()));
        				}
        				else rsrvs.put(cal.getTime().toString(),rsrvs.get(cal.getTime().toString())+Integer.parseInt(reservationsList.get(i).get(2).toString()));
        			}
        		}
            	
            	for(Entry<String, Integer> entry : rsrvs.entrySet()) {
            	    String key = entry.getKey();
            	    Integer value = entry.getValue();
            	    labReservers.setText(labReservers.getText()+" er reservert den "+ key.substring(0,key.length()-17)+" med "+value+" personer.<br>");

            	}
            	labReservers.setText(labReservers.getText()+"</html>");
           }
        };
        comboBox.addActionListener(cbActionListener);
		comboBox.setBounds(123, 281, 86, 20);
		add(comboBox);
		
		JLabel labReserve = new JLabel();
		//labReserve.setEditable(false);
		labReserve.setFont(new Font("Tahoma", Font.PLAIN, 26));
		labReserve.setText("Reserver");
		labReserve.setBounds(10, 130, 188, 39);
		labReserve.setBackground(this.getBackground());
		add(labReserve);
		
		labKname = new JTextPane();
		labKname.setBounds(255, 180, 236, 20);
		labKname.setBackground(this.getBackground());
		add(labKname);
		
		labOutput = new JLabel();
		labOutput.setBounds(123, 356, 206, 19);
		add(labOutput);
		
		labReservers = new JLabel();
		jspR = new JScrollPane(labReservers);
		jspR.setBounds(283, 200, 283, 142);
		add(jspR);
	}
	public void setWarnings(Map<String, Integer> koiewood) {
		warnings = koiewood;
	}
	public void setReserve(String name){
		comboBox.setSelectedItem(name);
		labKname.setText(names.get(comboBox.getSelectedIndex()));
    	labReservers.setText("<html>"+comboBox.getSelectedItem().toString()+"<br>");
    	for (int i = 0; i < reservationsList.size(); i++) {
			
			if ((int) reservationsList.get(i).get(0) == g.CoreClass.getKoieID(comboBox.getSelectedItem().toString())) {
				Calendar cal = new GregorianCalendar();
				cal.setTime((Date) reservationsList.get(i).get(1));
				String t =  cal.getTime().toString().substring(cal.getTime().toString().length()-17);
				labReservers.setText(labReservers.getText() + " er reservert den "+ t+"<br>");
			}
		}
    	labReservers.setText(labReservers.getText()+"</html>");
   }
}
