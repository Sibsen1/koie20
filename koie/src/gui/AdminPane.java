package gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import core.Core;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminPane extends JPanel {
	JPanel cards;
	private JTextField txtAdmin;
	private JTable tblRapport;
	public AdminPane(GUI g) {
		cards = new JPanel(new CardLayout());
		CardLayout cardLayout = (CardLayout) cards.getLayout();
		JPanel start = new JPanel();
		JPanel addNew = new JPanel();
		addNew.setLayout(null);
		start.setLayout(null);
		setLayout(null);
		this.setSize(600, 500);
		cards.setSize(600, 500);
		start.setSize(600,500);
		addNew.setSize(600,500);
		
		//---------------------- Start
		ArrayList<String> names = new ArrayList<String>();
		names.add("sdagaxxs");
		names.add("sda222s");
		names.add("sdaqqs");
		names.add("sdsdas");
		//names.addAll(g.CoreClass.getDataBaseColumns("koie", "koienavn").get(0));
		JList lstKoier = new JList(names.toArray());
		lstKoier.setBounds(45, 44, 146, 330);
		start.add(lstKoier);
		
		JButton btnEdit = new JButton("Rediger");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!lstKoier.isSelectionEmpty()){
					
				}
			}
		});
		btnEdit.setBounds(201, 41, 89, 23);
		start.add(btnEdit);
		
		JButton btnDelete = new JButton("- Slett");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!lstKoier.isSelectionEmpty()){
					
				}
			}
		});
		btnDelete.setBounds(201, 75, 89, 23);
		start.add(btnDelete);
		
		JButton btnAdd = new JButton("+ Legg til ");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cardLayout.show(cards,"Add");
			}
		});
		btnAdd.setBounds(45, 385, 81, 23);
		start.add(btnAdd);
		
		JTextPane labKoier = new JTextPane();
		labKoier.setText("Koier:");
		labKoier.setBounds(45, 13, 112, 20);
		start.add(labKoier);
		
		
		
		
		cards.add(start,"Start");
		
		JTextPane labNewAdmin = new JTextPane();
		labNewAdmin.setText("Legg til ny admin:");
		labNewAdmin.setBounds(348, 13, 112, 20);
		start.add(labNewAdmin);
		
		txtAdmin = new JTextField();
		txtAdmin.setBounds(348, 42, 160, 22);
		start.add(txtAdmin);
		txtAdmin.setColumns(10);
		
		JButton btnAddAdmin = new JButton("+ Legg til");
		btnAddAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String[] parts = txtAdmin.getText().split("@");
				if (parts.length == 2 && parts[1].indexOf(".")!=-1){
					//g.coreClass.setAdmin(txtAdmin.getText()); //Skal sette admin til true for denne brukeren
					txtAdmin.setText("");
				}
			}
		});
		btnAddAdmin.setBounds(348, 75, 89, 23);
		start.add(btnAddAdmin);
		
		ArrayList<List<String>> rapport = new ArrayList<List<String>>();
		String column_names[]= {"Bruker:","Koie:","Rapport:"};
		//rapport = g.CoreClass.getDataBaseColumns("rapport", "user_mail","koie_idkoie","rapporttext");
		DefaultTableModel table_model = new DefaultTableModel(column_names,3);//rapport.get(0).size()); //Lager headers og setter rowcount til antall rapporter
		tblRapport = new JTable(table_model);
		tblRapport.setBounds(289, 200, 281, 175);
		start.add(new JScrollPane(tblRapport));
		
		JTextPane labRapport = new JTextPane();
		labRapport.setText("Liste over rapporter:");
		labRapport.setBounds(289, 169, 119, 20);
		start.add(labRapport);
		
		//------------------- Start end
		
		//------------------- Add
		cards.add(addNew,"Add");
		//------------------- Add end
		add(cards);
	}
}
