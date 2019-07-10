package es.ucm.fdi.events;

import es.ucm.fdi.Objects.Highway;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class HighwayEvent extends RoadEvent {

	private int lanes;
	

	public HighwayEvent(Integer time, String id, String src, String dest, Integer max_speed, Integer length, int lanes) {
		super(time, id, src, dest, max_speed, length);
		this.lanes = lanes;
	}

	
	public void run(RoadsMap map) throws MapException {

		map.addRoad(this.id, map.getJunction(src), map.getJunction(dest), new Highway(this.id, length, max_speed, this.lanes));
	}
}
