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
import java.awt.ScrollPane;
import javax.swing.border.LineBorder;
import java.awt.Color;

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
		lstKoier.setBorder(new LineBorder(new Color(0, 0, 0)));
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
		labKoier.setBackground(this.getBackground());
		start.add(labKoier);
		
		
		
		
		
		JTextPane labNewAdmin = new JTextPane();
		labNewAdmin.setText("Legg til ny admin:");
		labNewAdmin.setBounds(348, 13, 112, 20);
		labNewAdmin.setBackground(this.getBackground());
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
		DefaultTableModel table_model = new DefaultTableModel(column_names,3);//rapport.get(0).size()); //Lager headers og setter rowcount til antall rapporter
		/*
		rapport = g.CoreClass.getDataBaseColumns("rapport", "user_mail","koie_idkoie","rapporttext");
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		for (int i = 0; i < rapport.size();i++){
			rows.set(i,new Object[rapport.size()]);
			for (int k = 0; k < rapport.get(0).size();k++){
				rows.get(k)[i] = rapport.get(i).get(k);
			}
		}          //using getdataBaseColums
		
		
		for (int i = 1; i < g.CoreClass.getDataBaseColumns("koie", "idkoie").get(0).size();i++){
			rapport.add(g.CoreClass.getRow("rapport",i));
		}
		
		for (List<String> ls : rapport){
			table_model.addRow(ls.toArray());
		}*/          //using getRow
		tblRapport = new JTable(table_model);
		
		JScrollPane jspB = new JScrollPane(tblRapport);
		jspB.setBounds(289, 200, 281, 175);
		start.add(jspB);
		
		JTextPane labRapport = new JTextPane();
		labRapport.setText("Liste over rapporter:");
		labRapport.setBounds(289, 169, 134, 20);
		labRapport.setBackground(this.getBackground());
		start.add(labRapport);
		
		cards.add(start,"Start");
		
		
		//------------------- Start end
		
		//------------------- Add
		cards.add(addNew,"Add");
		//------------------- Add end
		add(cards);
	}
}
