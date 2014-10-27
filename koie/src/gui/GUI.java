package gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import core.Core;

public class GUI extends JFrame{

	public Core CoreClass; 		// All kommunikasjon med resten av programmet skjer med denne. -Sindre
	JTabbedPane contentPanel = new JTabbedPane();
	//ReservePane reservePane = new ReservePane();
	//AdminPane adminPane;
	//MapPane mapPane;
	
	private boolean userIsAdmin;
	
	public GUI(Core CoreClass) { // GUI() tar i mot all informasjon som er nødvendig ved start for hvert GUI-element -Sindre
		this.CoreClass = CoreClass;
		this.setResizable(false);
		getContentPane().add(contentPanel);
		//mapPane	= new MapPane(this);
		//adminPane = new AdminPane(this);
		
		//contentPanel.add("Reserver",reservePane);
		//contentPanel.add("Kart",mapPane);
		//if (userIsAdmin){
			//contentPanel.add("Admin",adminPane);
		//}
		
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
		
	public void login(String email) {
		CoreClass.login(email);
		userIsAdmin = CoreClass.isAdmin;
	}
}

