package es.ucm.fdi.view;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


abstract public class TextAreaPanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	protected JTextArea areatexto;
	 
	 public TextAreaPanel(String titulo, boolean editable) {
		 this.setLayout(new GridLayout(1,1));
		 this.areatexto = new JTextArea(40, 30);
		 this.areatexto.setEditable(editable);
		 this.add(new JScrollPane(this.areatexto,
					JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));
		 this.setBorderWithTitle(titulo);
	 }
	 
	 public void setBorderWithTitle(String titulo){
		 this.setBorder(BorderFactory.createTitledBorder(titulo));
	 }
	 public String getAreaText() {
		 return this.areatexto.getText();
	 }
	 public void setAreaText(String text) {
		 this.areatexto.setText(text);
	 }
	 public void clean() {
		 this.areatexto.setText("");
	}
	 
	 public void insert(String valor) {
		 this.areatexto.insert(valor, this.areatexto.getCaretPosition());
	 }
}
