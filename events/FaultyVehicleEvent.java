package es.ucm.fdi.events;

import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class FaultyVehicleEvent extends Event {

	private int duration;
	String[] vehicles;
	
			
	public FaultyVehicleEvent(int time, int duration, String[] vehiclesId) {
		super(time);
		this.duration = duration;
		vehicles = vehiclesId;
	}

	public void run(RoadsMap map) throws MapException{
		for(String v : vehicles){
			map.getVehicle(v).setFaultyTime(this.duration);
		}

	}

}
