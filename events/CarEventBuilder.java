package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;
/** 
[new_vehicle]
time = <NONEG-INTEGER>
id = <VEHICLE-ID>
itinerary = <JUNC-ID>,<JUNC-ID>(,<JUNC-ID>)*
max_speed = <POSITIVE-INTEGER>
type = bike
**/

public class CarEventBuilder extends VehicleEventBuilder {
	 private static String [] claves = new String[] { "time", "id" , "max_speed", "itinerary", "type"};
	 private static String [] defaultValues = new String[] { "", "" , "", "", "Car"};
	
	public CarEventBuilder() {
		super(claves, defaultValues);
	}
	
	@Override
	public Event parser(IniSection section) {
		 if (section.getKeys().size() != 5 || !section.getTag().equals(this.etiqueta) || section.getValue("type") != null) 
			 return null;
		 else {
			 String dummy = section.getValue("itinerary");
			 String dummyL[];
			 dummyL = dummy.split(",");
			 return new CarEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				EventsBuilder.isValidId(section, "id"),
					 				EventsBuilder.parseaInt(section, "max_speed"),
					 				dummyL,
					 				0,
					 				EventsBuilder.parseaInt(section, "resistance"),
					 				EventsBuilder.parseaInt(section, "fault_probability"),
					 				EventsBuilder.parseaInt(section, "max_fault_duration"),
					 				EventsBuilder.parseaInt(section, "seed"));
		 }
	}

}
