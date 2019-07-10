package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public class VehicleEventBuilder extends EventsBuilder {
	
	public VehicleEventBuilder() {
		 this.etiqueta = "new_vehicle";
		 this.claves = new String[] { "time", "id" , "max_speed", "itinerary"};
		 this.defaultValues = new String[] { "", "" , "", ""};
	}
	
	public VehicleEventBuilder(String[] claves, String[] defaultValues) {
		 this.etiqueta = "new_vehicle";
		 this.claves = claves;
		 this.defaultValues = defaultValues;
	}
	
	@Override
	 public Event parser(IniSection section) {
		 if (section.getKeys().size() != 4 || !section.getTag().equals(this.etiqueta) || section.getValue("type") != null) 
			 return null;
		 else {
			 String dummy = section.getValue("itinerary");
			 String dummyL[];
			 dummyL = dummy.split(",");
			 return new VehicleEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				 EventsBuilder.isValidId(section, "id"),
					 				 EventsBuilder.parseaInt(section, "max_speed"),
					 				 dummyL);
		 }		 
	}
}
