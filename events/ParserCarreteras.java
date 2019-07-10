package es.ucm.fdi.events;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.Objects.Junction;
import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.exception.MapException;

public class ParserCarreteras {

	public static List<Junction> parseaListaCruces(String[] itinerary, RoadsMap map) {
		List<Junction> l = new ArrayList<Junction>();
		for (String s : itinerary){
			try {
				l.add(map.getJunction(s));
			} catch (MapException e) {
				return null;
			}
		}
		return l;
	}
	
	
}
