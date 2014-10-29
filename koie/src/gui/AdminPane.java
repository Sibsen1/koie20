package gui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
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
	GUI g;
	private JTextField txtAdmin;
	private JTable tblRapport;
	List<String> names;
	public AdminPane(GUI gui) {
		this.g = gui;
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
		//List<Object> names = new ArrayList<Object>();
		//names.add("sdagaxxs");
		//names.add("sda222s");
		//names.add("sdaqqs");
		//names.add("sdsdas");
		names = new ArrayList<String>();
		for (List<Object> o:g.CoreClass.getDataBaseColumns("koie", "koienavn")){
			names.add(o.get(0).toString());
		}
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
		
		String column_names[]= {"Bruker:","Koie:","Rapport:"};
		DefaultTableModel table_model = new DefaultTableModel(column_names,0);//rapport.get(0).size()); //Lager headers og setter rowcount til antall rapporter
		
		ArrayList<List<Object>> rapport = new ArrayList<List<Object>>();
		/*rapport.addAll(g.CoreClass.getDataBaseColumns("rapport", "user_mail","koie_idkoie","rapporttext"));
		ArrayList<Object[]> rows = new ArrayList<Object[]>();
		for (int i = 0; i < rapport.size();i++){
			rows.set(i,new Object[rapport.size()]);
			for (int k = 0; k < rapport.get(0).size();k++){
				rows.get(k)[i] = rapport.get(i).get(k);
			}
		}          //using getdataBaseColums
		
		
		for (int i = 0; i < g.CoreClass.getDataBaseColumns("rapport", "idrapport").get(0).size();i++){
			rapport.add(g.CoreClass.getDataBaseRow("rapport",Integer.toString(i),"user_mail","koie_idkoie","rapporttext"));
		}*/
		rapport = g.CoreClass.getReports("rapport.user_mail","koie.koienavn","rapport.rapporttext");
		for (List<Object> o : rapport){
			table_model.addRow(o.toArray());
		} 
		tblRapport = new JTable(table_model);
		tblRapport.getTableHeader().setReorderingAllowed(false);
		
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
	public static void main(String[] args) throws SQLException {
		Core core = new Core();
		System.out.println(core.getDataBaseColumns("koie", "koienavn"));
		
		
	}
}
