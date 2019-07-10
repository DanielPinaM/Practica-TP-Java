package es.ucm.fdi.events;

import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public abstract class Event implements Comparable<Event>{

	
	protected Integer time;
	
	public Event (Integer time){
		this.time = time;
	}
	
	public int getTime() {
		return this.time;
	}
	
	
	public int compareTo(Event e) {
		return this.time - e.time;
	}

	public abstract void run(RoadsMap map) throws MapException;

}
