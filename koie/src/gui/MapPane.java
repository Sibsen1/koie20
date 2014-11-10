package gui;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.Core;

import javax.swing.*;

import org.openstreetmap.gui.jmapviewer.*;

import java.awt.Font;

public class MapPane extends JPanel {
	GUI g;
	private JLabel labInfo;
	private JLabel labData;
	private JLabel labKoieInfo;
	public MapPane(GUI gui) {
		g = gui;
		this.setSize(600, 500);
		JMapViewer mapPanel = new JMapViewer(); 
		mapPanel.setBounds(10, 11, 400, 352);
		double lat = 63.13;
		double lon = 10.43;
		mapPanel.setDisplayPositionByLatLon(lat, lon, 8);

		DefaultMapController mapController = new DefaultMapController(mapPanel);
		mapController.setMovementMouseButton(MouseEvent.BUTTON1);
	
		this.setLayout(null);
		this.add(mapPanel);
		
		JLabel labKoier = new JLabel();
		labKoier.setFont(new Font("Tahoma", Font.PLAIN, 20));
		labKoier.setText("Koier:");
		labKoier.setBounds(438, 31, 113, 20);
		labKoier.setBackground(this.getBackground());
		add(labKoier);
		
		
		
		labKoieInfo = new JLabel();
		labKoieInfo.setFont(new Font("Tahoma", Font.BOLD, 14));
		labKoieInfo.setText("Velg en koie for informasjon:");
		labKoieInfo.setBounds(61, 370, 250, 20);
		labKoieInfo.setBackground(this.getBackground());
		add(labKoieInfo);
		

		List<String>names = new ArrayList<String>();
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		ArrayList<List<Object>> info = g.CoreClass.getDataBaseColumns("koie", "koienavn","sengeplasser","bordplasser","aar");
		for (List<Object> o:info){
			names.add(o.get(0).toString());	
			map.put(o.get(0).toString(), Arrays.asList(o.get(1).toString(),o.get(2).toString(),o.get(3).toString()));//o.get(1).toString());
		}
		
		System.out.println(names);
		
		JPanel pnlKoier = new JPanel(new GridLayout(0,1)); 
		ButtonGroup group = new ButtonGroup();
		ArrayList<JRadioButton> buttons = new ArrayList();
		
		for (Object o:names){
			buttons.add(new JRadioButton(o.toString()));
			group.add(buttons.get(buttons.size()-1));
			pnlKoier.add(buttons.get(buttons.size()-1));
			buttons.get(buttons.size()-1).addActionListener(new ActionListener(){ //Skal vise kort info om valgt koie
				@Override
				public void actionPerformed(ActionEvent arg0) {
					labKoieInfo.setText(o.toString());
					labInfo.setText("<html>Sengeplasser"+"<br>Bordplasser"+"<br>Bygget i"+"</html>");
					labData.setText("<html>"+map.get(o).get(0)+"<br>"+map.get(o).get(1)+"<br>"+map.get(o).get(2)+"</html>");
				}
			});
		}
		JScrollPane jspB = new JScrollPane(pnlKoier);
		jspB.setBounds(420, 59, 121, 352);
		pnlKoier.setBounds(420, 59, 121, 352);
		add(jspB);
		
		JButton btnReserver = new JButton("Reserver");
		btnReserver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				g.switchPane("reservePane", labKoieInfo.getText());
			}
		});
		btnReserver.setBounds(321, 381, 89, 23);
		add(btnReserver);
		
		JButton btnMerInfo = new JButton("Mer info");
		btnMerInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				g.switchPane("tablePane", labKoieInfo.getText());;
			}
		});
		btnMerInfo.setBounds(321, 415, 89, 23);
		add(btnMerInfo);
		
		labInfo = new JLabel();
		labInfo.setBounds(60, 381, 153, 57);
		add(labInfo);
		
		labData = new JLabel();
		labData.setBounds(186, 381, 134, 57);
		add(labData);
	
		ArrayList<List<Object>> koie_cords = g.CoreClass.getDataBaseColumns("koie","latitude","longitude");
		int d = 0;
		for (List<Object> k: koie_cords){
			mapPanel.addMapMarker(new MapMarkerDot(null, names.get(d), Float.valueOf(k.get(0).toString()), Float.valueOf(k.get(1).toString())));
			d++;
		}
		mapPanel.repaint();
	}
}
