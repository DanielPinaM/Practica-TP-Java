package es.ucm.fdi.view;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import es.ucm.fdi.control.Controller;
import es.ucm.fdi.model.TrafficSimulatorObserver;

//EL MODELO SE REGISTRA COMO OBSERVADOR
@SuppressWarnings("serial")
public abstract class TableModel<T> extends DefaultTableModel implements TrafficSimulatorObserver {
	
	protected String[] columnIds;
	protected List<T> lista;

	public TableModel(String[] columnIdEventos, Controller ctrl) {
		this.lista = null;
		this.columnIds = new String[columnIdEventos.length];
		for(int i = 0; i < columnIdEventos.length; i++)
			this.columnIds[i] = columnIdEventos[i];
		ctrl.addObserver(this);
}

	@Override
	public String getColumnName(int col) {
		return this.columnIds[col];
	}

	@Override
	public int getColumnCount() {
		return this.columnIds.length;
	}

	@Override
	public int getRowCount() {
		return this.lista == null ? 0 : this.lista.size();
	}
}