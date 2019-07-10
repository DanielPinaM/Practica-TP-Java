package es.ucm.fdi.events;

import es.ucm.fdi.Objects.RoadType;
import es.ucm.fdi.ini.IniSection;

public class DirtEventBuilder extends RoadEventBuilder {
	
	DirtEventBuilder(){
		super();
	}
	
	public Event parser(IniSection section) {
		 if (section.getKeys().size() != 6 || !section.getTag().equals(this.etiqueta) || section.getValue("type") == RoadType.DIRT.toString()) 
			 return null;
		 else
			 return new DirtEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				  EventsBuilder.isValidId(section, "id"),
					 				  EventsBuilder.isValidId(section, "src"),
					 				  EventsBuilder.isValidId(section, "dest"),
					 				  EventsBuilder.parseaInt(section, "max_speed"),
					 				  EventsBuilder.parseaInt(section, "length"));
	}

}
