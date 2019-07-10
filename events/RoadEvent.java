package es.ucm.fdi.events;

import es.ucm.fdi.Objects.Road;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class RoadEvent extends Event {
	/*
		[new_road]
		time = <NONEG-INTEGER>
		id = <ROAD-ID>
		src = <JUNC-ID>
		dest = <JUNC-ID>
		max_speed = <POSITIVE-INTEGER>
		length = <POSITIVE-INTEGER>
	*/
	
	protected String id;
	protected Integer max_speed;
	protected Integer length;
	protected String src;
	protected String dest;
	
	public RoadEvent(Integer time, String id, String src, String dest, Integer max_speed, Integer length) {
		super(time);
		this.id = id;
		this.src = src;
		this.dest = dest;
		this.max_speed = max_speed;
		this.length = length;
	}
	
	@Override
	public void run(RoadsMap map) throws MapException {		// añade al mapa la carretera.
		map.addRoad(this.id, map.getJunction(src), map.getJunction(dest), new Road(this.id, length, max_speed));
	}
	
	public String toString() {
		return "new road";
	}
}
