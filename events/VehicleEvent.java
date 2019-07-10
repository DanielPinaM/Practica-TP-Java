package es.ucm.fdi.events;

import java.util.List;

import es.ucm.fdi.Objects.Junction;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.Objects.Vehicle;
import es.ucm.fdi.exception.MapException;

public class VehicleEvent extends Event {

	protected String id;
	protected Integer maxSpeed;
	protected String[] itinerary;
	
	public VehicleEvent(int time, String id, int maxSpeed, String[] itinerary) {
		super(time);
		this.id = id;
		this.maxSpeed = maxSpeed;
		this.itinerary = itinerary;
	
	}
	@Override
	public void run(RoadsMap map) throws MapException{
		List<Junction> iti = ParserCarreteras.parseaListaCruces(this.itinerary, map);
		if(iti.equals(null)||iti.size()<2)
			throw new MapException("ERROR");
		else {
			map.addVehicle(id, new Vehicle(id, maxSpeed, iti));
		}
	}
	
	public String toString(){
		return "new vehicle";
	}

}
