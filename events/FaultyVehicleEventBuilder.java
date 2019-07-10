package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public class FaultyVehicleEventBuilder extends EventsBuilder {

	
	 public FaultyVehicleEventBuilder() {
		 this.etiqueta = "make_vehicle_faulty";
		 this.claves = new String[] { "time", "id", "vehicles"};
		 this.defaultValues = new String[] { "", "", };
	}
	@Override
	 public Event parser(IniSection section) {
		 if (section.getKeys().size() != 3  || !section.getTag().equals(this.etiqueta) || section.getValue("type") != null) 
			 return null;
		 else {
			 String dummy = section.getValue("vehicles");
			 String dummyL[];
			 dummyL = dummy.split(",");
			 return new FaultyVehicleEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 						parseaInt(section, "duration"),
					 						dummyL);
		 }
	}
	
	 @Override
	 public String toString() { 
		 return "Make vehicle faulty"; 
		 }

}