package es.ucm.fdi.events;

import es.ucm.fdi.Objects.JunctionType;
import es.ucm.fdi.ini.IniSection;

public class CircularJunctionEventBuilder 
	extends JunctionEventBuilder{
	
	protected JunctionType type;
	
	CircularJunctionEventBuilder(){
		super();
		 this.claves = new String[] { "time", "id", "type", "max_time_slice", "min_time_slice" };
		 this.defaultValues = new String[] { "", "","", "","" };
	}
	
	 @Override
	 public Event parser(IniSection section) {
		 if (section.getKeys().size() != 5 || !section.getTag().equals(this.etiqueta) || section.getValue("type") != null) 
			 return null;
		 else
			 return new CircularJunctionEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				  EventsBuilder.isValidId(section, "id"),
					 				 EventsBuilder.parserNoNegInt(section, "max_time_slice", 0),
					 				EventsBuilder.parserNoNegInt(section, "min_time_slice", 0));
	 }
	 @Override
	 public String toString() { 
		 return "New CircularJunction"; 
		 }
}
