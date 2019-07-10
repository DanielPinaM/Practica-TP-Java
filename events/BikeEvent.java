package es.ucm.fdi.events;

import java.util.List;

import es.ucm.fdi.Objects.Bike;
import es.ucm.fdi.Objects.Junction;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.Objects.VehicleType;
import es.ucm.fdi.exception.MapException;

public class BikeEvent extends VehicleEvent {
	protected VehicleType type;
	
	public BikeEvent(int time, String id, int maxSpeed, String[] itinerary) {
		super(time, id, maxSpeed, itinerary);
		this.type = VehicleType.BIKE;
	}
	
	public void run(RoadsMap map) throws MapException {
		List<Junction> iti = ParserCarreteras.parseaListaCruces(this.itinerary, map);
			 // si iti es null o tiene menos de dos cruces lanzar excepción
			 // en otro caso crear el vehículo y añadirlo al mapa.
		if(iti.equals(null)||iti.size()<2)
			throw new MapException("ERROR");
		else {
			map.addVehicle(id, new Bike(id, maxSpeed, iti));
		}
	}

}
