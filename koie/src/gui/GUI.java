package gui;

import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import core.Core;

public class GUI extends JFrame{

	public Core CoreClass; 		// All kommunikasjon med resten av programmet skjer med denne. -Sindre
	JTabbedPane contentPanel = new JTabbedPane();
	ReservePane reservePane;
	AdminPane adminPane;
	MapPane mapPane;
	TablePane tablePane;
	
	private boolean userIsAdmin;
	
	public GUI(Core CoreClass) { // GUI() tar i mot all informasjon som er nødvendig ved start for hvert GUI-element -Sindre
		this.CoreClass = CoreClass;
		this.setResizable(false);
		getContentPane().add(contentPanel);
		reservePane	= new ReservePane(this);
		mapPane	= new MapPane(this);
		
		tablePane = new TablePane(this);
		
		contentPanel.add("Reserver",reservePane);
		contentPanel.add("Kart",mapPane);
		contentPanel.add("Mer info",tablePane);
		
		
		this.setSize(600, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
	}
	
	public void switchPane(String pane, String arg){
		if (pane == "tablePane"){
			contentPanel.setSelectedComponent(tablePane);
			tablePane.showKoie(arg);
		}
		if (pane == "reservePane"){
			contentPanel.setSelectedComponent(reservePane);
			reservePane.setReserve(arg);
		}
	}
		
	public void login(String email) {
		CoreClass.login(email);
		userIsAdmin = CoreClass.isAdmin;
		if (userIsAdmin){
			adminPane = new AdminPane(this);
			contentPanel.add("Admin",adminPane);
		}
	}
	public void displayWarnings(Map<String,Integer> koiewood){
		reservePane.setWarnings(koiewood);
	}

}

