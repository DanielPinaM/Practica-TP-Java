package es.ucm.fdi.events;

import es.ucm.fdi.Objects.JunctionType;
import es.ucm.fdi.ini.IniSection;

public class CongestedJunctionEventBuilder 
extends JunctionEventBuilder {
	protected JunctionType type;
	
	 @Override
	 public String toString() { 
		 return "New Congested Junction"; 
		 }
	 
	 @Override
	 public Event parser(IniSection section) {
		 if (section.getKeys().size() != 3 || !section.getTag().equals(this.etiqueta) || section.getValue("type") != this.type.toString().toLowerCase()) 
			 return null;
		 else
			 return new CongestedJunctionEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				  EventsBuilder.isValidId(section, "id"));
	 }
}
