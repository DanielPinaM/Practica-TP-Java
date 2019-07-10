package es.ucm.fdi.view;

import java.util.List;

import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.SimulationError;

@SuppressWarnings("serial")
public class EventsTableModel extends TableModel<Event> {

	public EventsTableModel(String[] columnIdEventos, Controller ctrl) {		
		super(columnIdEventos, ctrl);
	}
	 @Override // necesario para que se visualicen los datos
	 public Object getValueAt(int indiceFil, int indiceCol) {
		 Object s = null;
		 switch (indiceCol) {
			 case 0: s = indiceFil; break;
			 case 1: s = this.lista.get(indiceFil).getTime(); break;
			 case 2: s = this.lista.get(indiceFil).toString(); break;
		 }
		 return s;
	 }
	 // ...

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
		this.lista = events; 
		this.fireTableStructureChanged();
	}

	@Override
	public void advanced(int time, RoadsMap map, List<Event> events) {
		// TODO Auto-generated method stub

	}

	@Override
	public void simulatorError(int time, RoadsMap map, List<Event> events,
			SimulationError e) {
		// TODO Auto-generated method stub

	}

}
