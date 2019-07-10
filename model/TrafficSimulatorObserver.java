package es.ucm.fdi.model;

import java.util.List;

import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.SimulationError;

public interface TrafficSimulatorObserver {
	
	public void registered(int time, RoadsMap map, List<Event> events);
	public void reset(int time, RoadsMap map, List<Event> events);
	public void eventAdded(int time, RoadsMap map, List<Event> events);
	public void advanced(int time, RoadsMap map, List<Event> events);
	public void simulatorError(int time, RoadsMap map, List<Event> events, SimulationError e);

}
