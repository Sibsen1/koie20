package gui;
import javax.imageio.ImageIO;
import javax.swing.*;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;
import org.jdatepicker.impl.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ReservePane extends JPanel{
	private JTextField txtEpost;
	private JTextField txtDato;
	public ReservePane() {
		setLayout(null);
		
		txtEpost = new JTextField();
		txtEpost.setBounds(107, 200, 86, 20);
		add(txtEpost);
		txtEpost.setColumns(10);
		
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
				//Core.reserveKoie(args);
			}
		});
		btnBekreft.setBounds(107, 322, 86, 23);
		add(btnBekreft);
		
		JButton btnTilbake = new JButton("TILBAKE");
		btnTilbake.setBounds(345, 322, 89, 23);
		add(btnTilbake);
		
		JTextPane labEpost = new JTextPane();
		labEpost.setEditable(false);
		labEpost.setText("Epost:");
		labEpost.setBounds(54, 200, 43, 20);
		labEpost.setBackground(this.getBackground());
		add(labEpost);
		
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
		
		JList lstKoie = new JList();
		lstKoie.setBounds(107, 281, 86, 20);
		//ArrayList<String> names = Core.getKoieNames();
		//for s in names lstKoie.add(s)
		add(lstKoie);
		
		JTextPane labReserve = new JTextPane();
		labReserve.setFont(new Font("Tahoma", Font.PLAIN, 26));
		labReserve.setText("Reserver");
		labReserve.setBounds(57, 77, 188, 39);
		labReserve.setBackground(this.getBackground());
		add(labReserve);
		
		JTextPane labKname = new JTextPane();
		labKname.setText("temp");
		labKname.setBounds(345, 239, 86, 20);
		labKname.setBackground(this.getBackground());
		add(labKname);
	}
}
