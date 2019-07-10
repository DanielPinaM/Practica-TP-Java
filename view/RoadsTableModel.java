package es.ucm.fdi.view;

import java.util.List;

import es.ucm.fdi.Objects.Road;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.SimulationError;

@SuppressWarnings("serial")
public class RoadsTableModel extends TableModel<Road> {

	public RoadsTableModel(String[] columnIdEventos, Controller ctrl) {
		super(columnIdEventos, ctrl);
		// TODO Auto-generated constructor stub
	}
	
	 public Object getValueAt(int indiceFil, int indiceCol) {
		 Object s = null;
		 switch (indiceCol) {
			 case 0: s = this.lista.get(indiceFil).getId(); break;
			 case 1: s = this.lista.get(indiceFil).getCruceOrigen().toString(); break;
			 case 2: s = this.lista.get(indiceFil).getCruceDestino().toString(); break;
			 case 3: s = this.lista.get(indiceFil).getLength(); break;
			 case 4: s = this.lista.get(indiceFil).getMaxSpeed(); break;
			 case 5: s = this.lista.get(indiceFil).getVehiclesList().toString(); break;
		 }
		 return s;
	 }

	@Override
	public void registered(int time, RoadsMap map, List<Event> events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void reset(int time, RoadsMap map, List<Event> events) {
		this.lista = null;
		this.fireTableStructureChanged();
	}

	@Override
	public void eventAdded(int time, RoadsMap map, List<Event> events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void advanced(int time, RoadsMap map, List<Event> events) {
		this.lista = map.getCarreteras();
		this.fireTableStructureChanged();

	}

	@Override
	public void simulatorError(int time, RoadsMap map, List<Event> events,
			SimulationError e) {
		// TODO Auto-generated method stub

	}

}
