package es.ucm.fdi.view;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class TablePanel<T> extends JPanel {
	
	 private TableModel<T> modelo;

	public TablePanel(String bordeId, TableModel<T> modelo){
		 this.setLayout(new GridLayout(1,1));
		 this.setBorde(bordeId);
		 this.modelo = modelo;
		 JTable tabla = new JTable(this.modelo);
		 this.add(new JScrollPane(tabla, 
				 	JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
	 }
	
	 public void setBorde(String titulo){
		 this.setBorder(BorderFactory.createTitledBorder(titulo));
	 }
	}
