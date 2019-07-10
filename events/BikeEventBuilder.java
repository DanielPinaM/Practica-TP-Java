package es.ucm.fdi.events;

import es.ucm.fdi.Objects.VehicleType;
import es.ucm.fdi.ini.IniSection;

public class BikeEventBuilder extends VehicleEventBuilder {
	 private static String [] claves = new String[] { "time", "id" , "max_speed", "itinerary", "type"};
	 private static String [] defaultValues = new String[] { "", "" , "", "", "Bike"};
	
	public BikeEventBuilder() {
		super(claves, defaultValues);
	}
	@Override
	public Event parser(IniSection section) {
		 if (section.getKeys().size() != 5 || !section.getTag().equals(this.etiqueta) || section.getValue("type") == VehicleType.BIKE.toString().toLowerCase()) 
			 return null;
		 else {
			 String dummy = section.getValue("itinerary");
			 String dummyL[];
			 dummyL = dummy.split(",");
			 return new BikeEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				EventsBuilder.isValidId(section, "id"),
					 				EventsBuilder.parseaInt(section, "max_speed"),
					 				dummyL);
		 }
	}

}
