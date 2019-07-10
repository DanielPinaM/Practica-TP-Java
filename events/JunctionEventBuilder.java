package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public class JunctionEventBuilder extends EventsBuilder {
	
	/*
	 * Ejemplo de Junction:
	 * [new_junction]
	 * time = 0
	 * id = j4
	 * */
	
	
	 public JunctionEventBuilder() {
		 this.etiqueta = "new_junction";
		 this.claves = new String[] { "time", "id" };
		 this.defaultValues = new String[] { "", "" };
	}
	 
	 @Override
	 public Event parser(IniSection section) {
		 if (section.getKeys().size() != 2 || !section.getTag().equals(this.etiqueta) || section.getValue("type") != null) 
			 return null;
		 else
			 return new JunctionEvent(EventsBuilder.parserNoNegInt(section, "time", 0),
					 				  EventsBuilder.isValidId(section, "id"));
	 }
	 
	 @Override
	 public String toString() { 
		 return "New Junction"; 
		 }
}
