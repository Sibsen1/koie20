package pages;
import javax.imageio.ImageIO;
import javax.swing.*;

import com.alee.laf.WebLookAndFeel;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
//testfork

public class iniPage extends JFrame{
	public iniPage() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel contentPane = new JPanel();
		getContentPane().add(contentPane, BorderLayout.CENTER);
		contentPane.setLayout(null);
		
		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				/*if (core.validateEmail(txtEpos.text)){
					
				}*/
				disposeThis();
				pageHub hub = new pageHub();
				hub.show();
			}
		});
		btnOk.setBounds(150, 177, 89, 23);
		contentPane.add(btnOk);
		
		JTextField txtEmail = new JTextField();
		txtEmail.setBounds(10, 143, 364, 23);
		contentPane.add(txtEmail);
		
		JTextPane txtEpos = new JTextPane();
		txtEpos.setEditable(false);
		txtEpos.setBackground(contentPane.getBackground());
		txtEpos.setText("Epost adresse:");
		txtEpos.setBounds(10, 123, 176, 20);
		contentPane.add(txtEpos);
		
		JPanel imgPanel = new JPanel();
		imgPanel.setBounds(72, 11, 208, 101);
		contentPane.add(imgPanel);
		
		BufferedImage myPicture;
		//myPicture = ImageIO.read(new File("../resources/koie_login.jpg"));
		JLabel picLabel = new JLabel(new ImageIcon("C:\\Users\\admin\\git\\koie20\\koie.ui\\resources\\koie_login.jpg"));
		imgPanel.add(picLabel);
		

		
		
		
	}	
	public static void main(String[] args) {  
		try {
			UIManager.setLookAndFeel ( WebLookAndFeel.class.getCanonicalName () );
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		iniPage initer = new iniPage();
		initer.show();
		initer.setSize(400, 250);
		initer.setResizable(false);
	}
	public void disposeThis(){
		this.dispose();
	}
}
