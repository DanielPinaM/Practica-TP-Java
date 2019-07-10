package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public abstract class EventsBuilder {
	
	protected String etiqueta; // etiqueta de la entrada (“new_road”, etc..)
	protected String[] claves; // campos de la entrada (“time”, “vehicles”, etc.)
	protected String[] defaultValues;
	EventsBuilder() {
	this.etiqueta = null;	//new_road, new_vehicle, ...
	this.claves = null;		//id, time, ...
	}
	
	public abstract Event parser(IniSection section);
	
	protected static int parseaInt(IniSection seccion, String clave) {
		String v = seccion.getValue(clave);
		if (v == null)
			throw new IllegalArgumentException("Could not get value from key: " + clave);
		else 
			return Integer.parseInt(seccion.getValue(clave));
		}
	
	protected static int parseaInt(IniSection seccion, String clave, int valorPorDefecto) {
		 String v = seccion.getValue(clave);
		 return (v != null) ? Integer.parseInt(seccion.getValue(clave)) :
		 valorPorDefecto;
	}
	
	public static int parserNoNegInt(IniSection section, String string, int defaultValue) {
	 
		 int i = EventsBuilder.parseaInt(section, string, defaultValue);
		 if (i < 0)
			 throw new IllegalArgumentException("El valor " + i + " para " + string + " no es un ID valido");
		 else 
			 return i;
	}
	
	protected static String isValidId(IniSection seccion, String clave) {
		 String s = seccion.getValue(clave);
		 if (!isValidId(s))
			 throw new IllegalArgumentException("Value " + s + " for " + clave + " is not a valid ID");
		 return s;
}
	private static boolean isValidId(String id) {
		return !id.equals(null) && id.matches("[a-z0-9]+");
	}
}
