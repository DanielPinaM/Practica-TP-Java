package es.ucm.fdi.events;

import java.util.List;

import es.ucm.fdi.Objects.Car;
import es.ucm.fdi.Objects.Junction;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.Objects.VehicleType;
import es.ucm.fdi.exception.MapException;

public class CarEvent extends VehicleEvent {
	protected VehicleType type;
	protected int kmSinceLastFault;
	protected int resistance;
	protected int max_fault_duration;
	protected int fault_probability;
	protected long seed;
	
	public CarEvent(int time, String id, int maxSpeed, String[] itinerary, int kmSinceLastFault,  int resistance, 
			int max_fault_duration, int fault_probability, long seed) {
		super(time, id, maxSpeed, itinerary);
		this.type = VehicleType.CAR;
		this.kmSinceLastFault = kmSinceLastFault;
		this.resistance = resistance;
		this.max_fault_duration = max_fault_duration;
		this.fault_probability = fault_probability;
		this.seed = seed;
	}
	
	public void run(RoadsMap map) throws MapException {
		List<Junction> iti = ParserCarreteras.parseaListaCruces(this.itinerary, map);
			 // si iti es null o tiene menos de dos cruces lanzar excepción
			 // en otro caso crear el vehículo y añadirlo al mapa.
		if(iti.equals(null)||iti.size()<2)
			throw new MapException("ERROR");
		else {
			map.addVehicle(id, new Car(id, maxSpeed, iti, this.kmSinceLastFault, this.resistance, this.max_fault_duration, 
					this.fault_probability, this.seed));
		}
	}
}
