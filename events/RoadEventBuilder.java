package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public class RoadEventBuilder extends EventsBuilder {

/*
	[new_road]
	time = <NONEG-INTEGER>
	id = <ROAD-ID>
	src = <JUNC-ID>
	dest = <JUNC-ID>
	max_speed = <POSITIVE-INTEGER>
	length = <POSITIVE-INTEGER>
*/
	
	RoadEventBuilder(){
		this.etiqueta = "new_road";
		this.claves = new String [] {"time" , "id", "src", "dest", "max_speed", "length"};
		this.defaultValues = new String [] {"", "", "", "", "", ""};
	}
	
	@Override
	public Event parser(IniSection section) {
		 if (section.getKeys().size() != 6 || !section.getTag().equals(this.etiqueta) || section.getValue("type") != null) 
			 return null;
		 else
			 return new RoadEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				  EventsBuilder.isValidId(section, "id"),
					 				  EventsBuilder.isValidId(section, "src"),
					 				  EventsBuilder.isValidId(section, "dest"),
					 				  EventsBuilder.parseaInt(section, "max_speed"),
					 				  EventsBuilder.parseaInt(section, "length"));
	}
	
	public String toString(){
		return "new road";
	}

}
