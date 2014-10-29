package gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class TablePane extends JPanel{
	JTable tblKoier;
	public TablePane(GUI g) {
		setLayout(null);
		
		this.setSize(600, 500);

		String column_names[]= {"<html>Koie<br>navn:</html>","<html>Senge<br>plasser:</html>","<html>Bord<br>plasser:</html>", "År:", "Terreng:", "<html>Topp<br>tur:</html>", "<html>Jakt<br>fiske:</html>", "Spesialiteter:", "<html>Ved<br>status:</html>"};
		//ArrayList<List<String>> koieInfo = g.CoreClass.getDataBaseColumns("koie", null);
		DefaultTableModel table_model = new DefaultTableModel(column_names,3);//rapport.get(0).size()); //Lager headers og setter rowcount til antall rapporter
		tblKoier = new JTable(table_model);
		tblKoier.enable(false);
		JScrollPane jspB = new JScrollPane(tblKoier);
		jspB.setBounds(29, 26, 540, 399);
		add(jspB);
		
		
	}
	
	

}