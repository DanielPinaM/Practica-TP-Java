package es.ucm.fdi.view;

import java.awt.Color;
import java.util.List;

import javax.swing.BorderFactory;

import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.control.Controller;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.model.TrafficSimulatorObserver;

@SuppressWarnings("serial")
public class ReportsPanel extends TextAreaPanel implements TrafficSimulatorObserver {
	
	public ReportsPanel(String titulo, boolean editable, Controller ctrl) {
	super(titulo, editable);
	this.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Reports"));
	ctrl.addObserver(this); // se añade como observador
	}
	
	@Override
	public void registered(int time, RoadsMap map, List<Event> events) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void reset(int time, RoadsMap map, List<Event> events) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void eventAdded(int time, RoadsMap map, List<Event> events) {
		// TODO Auto-generated method stub
		
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