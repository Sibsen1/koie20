package gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Core;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.ScrollPane;

import javax.swing.border.LineBorder;

import java.awt.Color;
import java.io.File;
import java.io.IOException;

public class AdminPane extends JPanel {
	JPanel cards;
	GUI g;
	private JTextField txtAdmin;
	private JTextPane labOutput;
	private JTable tblRapport;
	List<String> names;
	List<String> woods;
	private JTextPane labWarning;
	private JScrollPane jspW;
	ArrayList<List<Object>> rapport;
	private JList lstKoier;
	private JList lstWood;
	private JScrollPane jspL;
	private JPanel lists;
	private JLabel labImgWarning;
	private Map<String,Integer> warnings;
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
		warnings = new HashMap<String,Integer>();
		//---------------------- Start
		labWarning = new JTextPane();
		labWarning.setEditable(false);
		labWarning.setForeground(Color.RED);
		jspW = new JScrollPane(labWarning);
		jspW.setBounds(289, 385, 281, 50);
		jspW.setVisible(false);
		start.add(jspW);
		labImgWarning = new JLabel();
		labImgWarning.setBounds(263, 385, 26, 50);
		labImgWarning.setVisible(false);;
		try {
			labImgWarning.setIcon(new ImageIcon(ImageIO.read(new File("resources/warning.png"))));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		start.add(labImgWarning);
		
		names = new ArrayList<String>();
		woods = new ArrayList<String>();
		updateInfo();
		
		lstKoier.setCellRenderer(new IconListRenderer());
		lstWood.setCellRenderer(new IconListRenderer());
		lstWood.setSelectionModel(new DisabledItemSelectionModel());
		
		lists = new JPanel();
		lists.add(lstKoier);
		lists.add(lstWood);
		jspL = new JScrollPane(lists);
		jspL.setBorder(new LineBorder(new Color(0, 0, 0)));
		jspL.setBounds(45, 44, 146, 330);
		start.add(jspL);
		
		
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
		
		JLabel labKoier = new JLabel();
		labKoier.setText("Koier -");
		labKoier.setBounds(45, 13, 52, 20);
		labKoier.setBackground(this.getBackground());
		start.add(labKoier);
		JLabel labVed = new JLabel();
		labVed.setText(" - Ved");
		labVed.setBounds(120, 13, 52, 20);
		labVed.setBackground(this.getBackground());
		start.add(labVed);
		
		
		
		
		JLabel labNewAdmin = new JLabel();
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
					if (g.CoreClass.editUser(txtAdmin.getText(),true)){ 
						txtAdmin.setText("");
						labOutput.setText("OK!");
					}
				}
				else {
					labOutput.setText("Oppgi gyldig email.");
				}
			}
		});
		btnAddAdmin.setBounds(348, 75, 89, 23);
		start.add(btnAddAdmin);
		
		String column_names[]= {"Bruker:","Koie:","Rapport:"};
		DefaultTableModel table_model = new DefaultTableModel(column_names,0);//rapport.get(0).size()); //Lager headers og setter rowcount til antall rapporter
		
		rapport = new ArrayList<List<Object>>();
		rapport = g.CoreClass.getReports("rapport.user_mail","koie.koienavn","rapport.rapporttext");
		for (List<Object> o : rapport){
			table_model.addRow(o.toArray());
		} 
		tblRapport = new JTable(table_model);
		tblRapport.getTableHeader().setReorderingAllowed(false);
		
		JScrollPane jspB = new JScrollPane(tblRapport);
		jspB.setBounds(218, 200, 352, 175);
		start.add(jspB);
		
		JLabel labRapport = new JLabel();
		labRapport.setText("Liste over rapporter:");
		labRapport.setBounds(218, 169, 192, 20);
		labRapport.setBackground(this.getBackground());
		start.add(labRapport);
		
		cards.add(start,"Start");
		
		labOutput = new JTextPane();
		labOutput.setBackground(this.getBackground());
		labOutput.setBounds(447, 78, 126, 50);
		start.add(labOutput);
		
		JButton btnOppdater = new JButton("Oppdater");
		btnOppdater.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lstKoier.removeAll();
				lstWood.removeAll();
				updateInfo();
				jspL = new JScrollPane(lists);
			}
		});
		btnOppdater.setBounds(481, 166, 89, 23);
		start.add(btnOppdater);
		
		
		
		
		
		
		
		//------------------- Start end
		
		//------------------- Add
		cards.add(addNew,"Add");
		//------------------- Add end
		add(cards);
	}
	
	public void updateInfo(){
		names.clear();
		woods.clear();
		
		for (List<Object> o:g.CoreClass.getDataBaseColumns("koie", "koienavn")){
			names.add(o.get(0).toString());
		}
		
		woods = new ArrayList<String>();
		for (Object o: names){
			woods.add(Integer.toString(g.CoreClass.getWoodStatus(o.toString())));
			System.out.print(g.CoreClass.getWoodStatus(o.toString())+"-");
		}
		System.out.println("");
		labWarning.setText("");
		for (int i = 0;i < woods.size();i++){
			if (Integer.parseInt(woods.get(i)) < 3){
				warningWood(names.get(i));
				warnings.put(names.get(i), Integer.parseInt(woods.get(i)));
			}
		}
		g.displayWarnings(warnings);
		lstKoier = new JList(names.toArray());
		lstWood = new JList(woods.toArray());
	}
	
	
	public void warningWood(String name){
		jspW.setVisible(true);
		labWarning.setText(labWarning.getText()+ name + " har manglende ved! - ");
		labImgWarning.setVisible(true);
		//g.CoreClass.warningMail(name); Sender mail om manglende ved
	}
	
	public class IconListRenderer extends DefaultListCellRenderer {
	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
	        JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
	        Icon icon = this.getIcon(list, value, index, isSelected, cellHasFocus, label);
	        label.setIcon(icon);
	        return label;
	    }
	    protected Icon getIcon(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus, JLabel label) {
	    	try {
	    		if (value.toString().length() < 3){
	    			if (Integer.parseInt(value.toString()) < 3){
	    				label.setForeground(Color.RED);
	    				label.setBorder(new LineBorder(Color.RED));
	    				return new ImageIcon(ImageIO.read(new File("resources/warning.png")));}
	    			else return null;
	    		}
	    		else return null;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
	    }
	}
	class DisabledItemSelectionModel extends DefaultListSelectionModel {

	    @Override
	    public void setSelectionInterval(int index0, int index1) {
	        super.setSelectionInterval(-1, -1);
	    }
	}
}
