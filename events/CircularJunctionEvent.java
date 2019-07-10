package es.ucm.fdi.events;

import es.ucm.fdi.Objects.CircularJunction;
import es.ucm.fdi.Objects.JunctionType;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class CircularJunctionEvent extends JunctionEvent {

	/*[new_junction]
time = <NONEG-INTEGER>
id = <JUNC-ID>
type = rr
max_time_slice = <POSITIVE-INTEGER>
min_time_slice = <POSITIVE-INTEGER>
*/
	protected JunctionType type;
	protected int maxTimeSlice, minTimeSlice;
	public CircularJunctionEvent(int time, String id,int max, int min) {
		super(time, id);
		this.maxTimeSlice = max;
		this.minTimeSlice = min;
		this.type = JunctionType.CIRCULAR;
	}
	
	@Override
	public void run(RoadsMap map) throws MapException {
		CircularJunction j = new CircularJunction(this.id, this.type, this.maxTimeSlice, this.minTimeSlice);
		map.addJunction(this.id, j);
			//Crea el cruce y se lo añade al mapa
		
	}
}
