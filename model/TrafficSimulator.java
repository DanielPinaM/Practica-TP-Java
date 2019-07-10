package es.ucm.fdi.model;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import es.ucm.fdi.Objects.RoadsMap;
import es.ucm.fdi.events.Event;
import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.utils.SortedArrayList;

public class TrafficSimulator implements Observer<TrafficSimulatorObserver>{
	private RoadsMap map;
	private List<Event> events;
	private int time;
	private List<TrafficSimulatorObserver> observers;

	public TrafficSimulator() {
		 this.map = new RoadsMap();
		 this.time = 0;
		 Comparator<Event> cmp = new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				return o1.getTime() - o2.getTime();
			}
		 };
		 this.events = new SortedArrayList<Event>(cmp); // estructura ordenada por “tiempo”
		 this.observers = new ArrayList<>();
	}
	
	public void addEvent(Event e) throws SimulationError{
		
		if(e.getTime() < this.time || e == null){
			SimulationError error = new SimulationError("Event time error");
			this.notifyError(error);
			throw error;
		}
			
		else{
			this.events.add(e);
			this.notifyEventAdded();
		}
	}
	
	public void execute(int pasosSimulacion, OutputStream s) throws MapException, SimulationError, IOException{

		BufferedOutputStream buffer = new BufferedOutputStream(s);
		String rep = "";
		int limiteTiempo = this.time + pasosSimulacion - 1;
		
		while (this.time <= limiteTiempo) {
			
			// 1. ejecutar los eventos correspondientes a ese tiempo
				for (Event e : events) //tal vez sería más óptimo con un while.
					if(e.getTime() == this.time) 
						e.run(map);	
				//Antes de nada, nos aseguramos de que la primera luz es verde.
				if(this.time==0)
					this.setFirstGreenLight();
			// 2. invocar al método avanzar de las carreteras
				this.map.roadsAdvance();
			// 3. invocar al método avanzar de los cruces
				this.map.junctionsAdvance();
			// 4. this.contadorTiempo++;
				this.time++;
			// 5. esciribir un informe en OutputStream
				rep = this.map.generateReport(this.time);
				this.notifyAdvanced();
				buffer.write(rep.getBytes());
		}
		buffer.flush();
		buffer.close();
		
	}
	
	public void reset(){
		 this.map = new RoadsMap();
		 this.time = 0;
		 Comparator<Event> cmp = new Comparator<Event>() {
			@Override
			public int compare(Event o1, Event o2) {
				return o1.getTime() - o2.getTime();
			}
		 };
		 this.events = new SortedArrayList<Event>(cmp);
		 this.notifyReset();
	}
	private void setFirstGreenLight() {
			this.map.setFirstGreenLight();//pone a verde todas las primeras carreteras de cada cruce.
	}

	@Override
	public void addObserver(TrafficSimulatorObserver o) {
		if(o != null && !this.observers.contains(o)){
			this.observers.add(o);
			this.notifyRegistered(o);
		}
			
	}

	@Override
	public void removeObserver(TrafficSimulatorObserver o) {
		if(o != null && this.observers.contains(o))
			this.observers.remove(o);
		
	}
	private void notifyRegistered(TrafficSimulatorObserver o){
		for(TrafficSimulatorObserver ob : this.observers)
			ob.registered(time, map, events);
	}
	private void notifyReset() {
		for(TrafficSimulatorObserver ob : this.observers)
			ob.reset(time, map, events);
	}
	private void notifyEventAdded() {
		for(TrafficSimulatorObserver ob : this.observers)
			ob.eventAdded(time, map, events);
	}
	private void notifyAdvanced() {
		for(TrafficSimulatorObserver ob : this.observers)
			ob.advanced(time, map, events);
	}
	private void notifyError(SimulationError e){
		for(TrafficSimulatorObserver ob : this.observers)
			ob.simulatorError(time, map, events, e);
	}

	public void stop() {
		
		
	}

}
