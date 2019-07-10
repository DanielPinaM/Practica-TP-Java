package es.ucm.fdi.control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import es.ucm.fdi.events.Event;
import es.ucm.fdi.events.EventsParser;
import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.ini.Ini;
import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.model.TrafficSimulator;
import es.ucm.fdi.model.TrafficSimulatorObserver;
import es.ucm.fdi.threads.DelayRunner;

public class Controller {
	private TrafficSimulator sim;
	private OutputStream outputFile;
	private InputStream inputFile;
	private int simSteps;
	
	
	public Controller(TrafficSimulator sim, Integer limiteTiempo/**/, InputStream is,
			OutputStream os) {
		this.sim = sim;
		this.outputFile = os;
		this.inputFile = is;
		this.simSteps = 10;
	}

	public void reset(){
		this.sim.reset();
	}
	
	public void execute() throws MapException, SimulationError, IOException {

		this.loadEvents(this.inputFile);
		this.sim.execute(this.simSteps, this.outputFile);
	}

	public void execute(int steps) throws MapException, SimulationError, IOException{
		this.sim.execute(steps, this.outputFile);
	}

	public void loadEvents(InputStream inStream) throws SimulationError{
		Ini ini; 
		try{
			ini = new Ini(inStream);
		}
		catch(IOException e){
			throw new SimulationError("Readings events error: " + e);
		}
		
		for(IniSection sec : ini.getSections()){
			Event e = EventsParser.parseEvent(sec);
			if (e!=null)
				this.sim.addEvent(e);
			else
				throw new SimulationError("Unknow event: " + sec.getTag());
		}
	}

	public void addObserver(TrafficSimulatorObserver o){
		this.sim.addObserver(o);
	}
	
	public void removeObserver(TrafficSimulatorObserver o){
		this.sim.removeObserver(o);
	}

	public void stop() {
		this.sim.stop();
		
	}
}
