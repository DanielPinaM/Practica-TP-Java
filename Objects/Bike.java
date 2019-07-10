package es.ucm.fdi.Objects;

import java.util.List;

import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.ini.IniSection;
/*
 * [new_vehicle]
time = <NONEG-INTEGER>
id = <VEHICLE-ID>
itinerary = <JUNC-ID>,<JUNC-ID>(,<JUNC-ID>)*
max_speed = <POSITIVE-INTEGER>
type = bike

 * */
public class Bike extends Vehicle {
	
	public Bike(String id, int maxSpeed, List<Junction> iti) throws MapException {
		super(id, maxSpeed, iti);
		this.type = VehicleType.BIKE;
	}

	
	public void completeSectionDetails(IniSection is) {
		String ret = "";
		is.setValue("type", this.type.toString().toLowerCase());
		is.setValue("speed", currentSpeed);
		is.setValue("kilometrage", kilometrage);
		is.setValue("faulty", faultyTimeLeft);
		if(this.road != null){

			if(this.hasArrived)
				is.setValue("location", "arrived");
			else {
				ret = "(" + this.road.toString() +"," +this.location + ")";
				is.setValue("location", ret);
			}
			
		}
		else if (this.hasArrived)
			is.setValue("location", "arrived");

	}
}
