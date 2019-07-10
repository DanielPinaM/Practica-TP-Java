package es.ucm.fdi.Objects;

import java.util.List;
import java.util.Random;

import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.ini.IniSection;

public class Car extends Vehicle {
	
	private int kmSinceLastFault;
	private int resistance;
	private int fault_probability;
	private int max_fault_duration;
	Random r;
	

	public Car(String id, int maxSpeed, List<Junction> iti, int kmSinceLastFault,  int resistance, 
			int max_fault_duration, int fault_probability, long seed) throws MapException {
		super(id, maxSpeed, iti);
		this.type = VehicleType.CAR;
		this.kmSinceLastFault = kmSinceLastFault;
		this.resistance = resistance;
		this.fault_probability = fault_probability;
		this.r = new Random(seed);
	}
	
	public void advance() {
		if(!this.isFaulty() && resistance >= kmSinceLastFault && r.nextDouble() < this.fault_probability) {
			try {
				this.setFaultyTime(r.nextInt(this.max_fault_duration) + 1);
			} catch (Exception e) {}
			
			super.advance();
		}
	}
	
	public void completeSectionDetails(IniSection is) {
		String ret = "";
		is.setValue("type", this.type.toString().toLowerCase());
		is.setValue("speed", currentSpeed);
		is.setValue("kilometrage", kilometrage);
		is.setValue("faulty", faultyTimeLeft);
		if(this.road != null){

			if(this.hasArrived)
				is.setValue("location", "arrived");
			else {
				ret = "(" + this.road.toString() +"," +this.location + ")";
				is.setValue("location", ret);
			}
			
		}
		else if (this.hasArrived)
			is.setValue("location", "arrived");

	}
	
}
