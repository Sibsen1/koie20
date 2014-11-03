package gui;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jdatepicker.impl.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReservePane extends JPanel{
	private JTextField txtDager;
	private JTextField txtDato;
	GUI g;
	private ArrayList<String> names;
	private JComboBox comboBox;
	private JTextPane labKname;
	public ReservePane(GUI gui) {
		g = gui;
		setLayout(null);
		
		txtDager = new JTextField();
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
		picLabel.setBounds(268, 59, 200, 200);
		add(picLabel);
		txtDato = new JTextField();
		txtDato.setBounds(107, 239, 86, 20);
		add(txtDato);
		txtDato.setColumns(10);
		
		/*UtilDateModel model = new UtilDateModel();
		JDatePanelImpl datePanel = new JDatePanelImpl(model);
		JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
		datePicker.setBounds(107, 239, 86, 20);
		add(datePicker);*/
		
		JButton btnBekreft = new JButton("BEKREFT");
		btnBekreft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Core.insertReservation(g.CoreClass.userEmail,);
			}
		});
		btnBekreft.setBounds(107, 322, 86, 23);
		add(btnBekreft);
		
		JTextPane labDager = new JTextPane();
		labDager.setEditable(false);
		labDager.setText("Antall dager:");
		labDager.setBounds(29, 200, 68, 20);
		labDager.setBackground(this.getBackground());
		add(labDager);
		
		JTextPane labDato = new JTextPane();
		labDato.setEditable(false);
		labDato.setText("Dato dd,mm,yyyy:");
		labDato.setBounds(10, 231, 87, 39);
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
           }
        };
        comboBox.addActionListener(cbActionListener);
		comboBox.setBounds(107, 281, 86, 20);
		add(comboBox);
		
		JLabel labReserve = new JLabel();
		//labReserve.setEditable(false);
		labReserve.setFont(new Font("Tahoma", Font.PLAIN, 26));
		labReserve.setText("Reserver");
		labReserve.setBounds(57, 77, 188, 39);
		labReserve.setBackground(this.getBackground());
		add(labReserve);
		
		labKname = new JTextPane();
		labKname.setText("temp");
		labKname.setBounds(345, 239, 86, 20);
		labKname.setBackground(this.getBackground());
		add(labKname);
	}
}
