package es.ucm.fdi.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

@SuppressWarnings("serial")
public class SimObjectPanel<T> extends JPanel{
	
	private ListModel<T> listModel;
	private JList<T> objList;
	
	public SimObjectPanel(String titulo) {
		this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), titulo));
		this.listModel = new ListModel<T>();
		this.objList = new JList<T>(this.listModel);
		addCleanSelectionListner(objList);
		this.add(new JScrollPane(objList,
				JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.EAST);
	}
	
	private void addCleanSelectionListner(JList<?> list) {
		list.addKeyListener(new KeyListener() {
			// limpiar la seleccion de items pulsando “c”
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == KeyEvent.VK_C)
					list.clearSelection();
			}

			@Override
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub

			}
		});	
	}

	public List<T> getSelectedItems() {
		List<T> l = new ArrayList<>();
		for (int i : this.objList.getSelectedIndices()) {
			l.add(listModel.getElementAt(i));
		}
		return l;
	}
	public void setList(List<T> lista) { 
		this.listModel.setList(lista);
	}
}

