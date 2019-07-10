package es.ucm.fdi.events;

import es.ucm.fdi.Objects.RoadType;
import es.ucm.fdi.ini.IniSection;

public class HighwayEventBuilder extends RoadEventBuilder {
	
	
	HighwayEventBuilder(){
		
		super();
		this.etiqueta = "new_road";
		this.claves = new String [] {"time" , "id", "src", "dest", "max_speed", "length", "lanes"};
		this.defaultValues = new String [] {"", "", "", "", "", "", ""};
	}
	
	public Event parser(IniSection section) {
		 if (section.getKeys().size() != 8 || !section.getTag().equals(this.etiqueta) || section.getValue("type") == RoadType.LANES.toString().toLowerCase()) 
			 return null;
		 else
			 return new HighwayEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				  EventsBuilder.isValidId(section, "id"),
					 				  EventsBuilder.isValidId(section, "src"),
					 				  EventsBuilder.isValidId(section, "dest"),
					 				  EventsBuilder.parseaInt(section, "max_speed"),
					 				  EventsBuilder.parseaInt(section, "length"),
					 				  EventsBuilder.parseaInt(section, "lanes"));
	}
}
