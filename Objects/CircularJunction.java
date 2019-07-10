package es.ucm.fdi.Objects;

import es.ucm.fdi.exception.SimulationError;

public class CircularJunction extends Junction {
	
	protected JunctionType type;
	protected int maxTimeSlice, minTimeSlice;
	
	@SuppressWarnings("unused")
	private int timeUsed;
	
	
	
	public CircularJunction(String id, JunctionType type, int max, int min) {
		super(id);
		this.type = type;
		this.maxTimeSlice = max;
		this.minTimeSlice = min;
		this.timeUsed = 0;
	}
	
	@Override
	public void updateTrafficLights() throws SimulationError{
		
	}

}
