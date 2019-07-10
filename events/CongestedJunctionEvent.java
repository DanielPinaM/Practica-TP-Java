package es.ucm.fdi.events;

import es.ucm.fdi.Objects.CongestedJunction;
import es.ucm.fdi.Objects.JunctionType;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class CongestedJunctionEvent extends JunctionEvent {
	protected JunctionType type;
	
	public CongestedJunctionEvent(int time, String id) {
		super(time, id);
		 this.type = JunctionType.CONGESTED;
	}

	@Override
	public void run(RoadsMap map) throws MapException {
		CongestedJunction j = new CongestedJunction(this.id, this.type);
		map.addJunction(this.id, j);
		
	}
}
