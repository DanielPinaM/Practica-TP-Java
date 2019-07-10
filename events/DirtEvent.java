package es.ucm.fdi.events;

import es.ucm.fdi.Objects.Dirt;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class DirtEvent extends RoadEvent {


	public DirtEvent(Integer time, String id, String src, String dest, Integer max_speed, Integer length) {
		super(time, id, src, dest, max_speed, length);
	}

	
	public void run(RoadsMap map) throws MapException {

		map.addRoad(this.id, map.getJunction(src), map.getJunction(dest), new Dirt(this.id, length, max_speed));
	}

}
