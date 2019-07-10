package es.ucm.fdi.events;

import es.ucm.fdi.ini.IniSection;

public class EventsParser {
	 private static EventsBuilder[] events = {
	 new JunctionEventBuilder(),
	 new RoadEventBuilder(),
	 new VehicleEventBuilder(),
	 new FaultyVehicleEventBuilder(),
	 new CarEventBuilder(),
	 new BikeEventBuilder(),
	 new HighwayEventBuilder(),
	 new DirtEventBuilder()
	 };
	 
	 public static Event parseEvent(IniSection sec) {
		 Event e = null;
		 for (int i = 0; i < events.length && e==null; i++){
			 e = events[i].parser(sec);
		 }
		 
	 return e;
	}
}