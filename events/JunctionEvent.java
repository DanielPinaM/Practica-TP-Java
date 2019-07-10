package es.ucm.fdi.events;

import es.ucm.fdi.Objects.Junction;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class JunctionEvent extends Event {
	protected String id;
	
	public JunctionEvent(int time, String id){
		super(time);
		this.id = id;
	}
	
	@Override
	public void run(RoadsMap map) throws MapException {
		Junction j = new Junction(this.id);
		map.addJunction(this.id, j);
			//Crea el cruce y se lo añade al mapa
		
	}

	@Override
	public String toString(){
		return "new junction";
	}
}
