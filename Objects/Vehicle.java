package es.ucm.fdi.Objects;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.ini.IniSection;

public class Vehicle  extends SimulationObject implements Comparable<Vehicle>{

	protected Road road;
	protected int maxSpeed;
	protected int currentSpeed;
	protected int location;
	protected int kilometrage;
	protected int faultyTimeLeft; //>0 -> está averiado;
	protected List<Junction> itinerary;
	protected Junction src;
	protected Junction dest;
	protected int currentJuctionIndex;
	protected boolean isInAJunction;
	protected String currentJunctionId;
	protected boolean hasArrived;
	protected VehicleType type;
	
	
	
	public Vehicle(String id, int maxSpeed, List<Junction> iti) throws MapException {
		super(id);
		if(iti.size() < 2)
			throw new MapException("Less than two junctions in Vehicle creation");
		src = iti.get(0);
		dest = iti.get(1);
		this.road = src.roadToJunction(dest);
		if(maxSpeed < 0)
			throw new MapException("Invalid speed");
		this.maxSpeed = maxSpeed;
		this.itinerary = new ArrayList<Junction>(iti);
		this.currentJuctionIndex = 0;
		this.currentJunctionId = this.itinerary.get(this.currentJuctionIndex).getId();
	}
	
	public void movetoNextRoad(){
		/*
		Si la carretera no es null, sacar el vehículo de la carretera.
	 	Si hemos llegado al último cruce del itinerario, entonces:
		 	1. Se marca que el vehículo ha llegado (this.haLlegado = true).
			 2. Se pone su carretera a null.
			 3. Se pone su “velocidadActual” y “localizacion” a 0.
			 	y se marca que el vehículo está en un cruce (this.estaEnCruce = true).
		En otro caso:
			 1. Se calcula la siguiente carretera a la que tiene que ir.
			 2. Si dicha carretera no existe, se lanza excepción.
			 3. En otro caso, se introduce el vehículo en la carretera.
			 4. Se inicializa su localización.
		 */
		if(this.road != null) {
			this.road.removeVehicle(this);
			this.currentSpeed = this.location = 0;
			if(this.currentJuctionIndex == this.itinerary.size()-1){
				this.hasArrived = true;
				this.isInAJunction = true;
			}
			else {
				Junction j = this.itinerary.get(this.currentJuctionIndex);
				Road nextRoad;
				++this.currentJuctionIndex;	
				nextRoad = j.roadToJunction(this.itinerary.get(this.currentJuctionIndex));
				if(nextRoad != null) {
					nextRoad.addVehicle(this);
					this.road = nextRoad;
				}
   				this.isInAJunction = false;	

			}

		}
		
	}
	
	public void setSpeed(int speed) {
		if(speed < 0)
			this.currentSpeed = 0;
		else if(speed > this.maxSpeed)
			this.currentSpeed = this.maxSpeed;
		else
			this.currentSpeed = speed;
	}
	
	public void setFaultyTime(int faultyTime){
		if(faultyTime > 0)
			this.faultyTimeLeft += faultyTime;
		
		if(this.isFaulty()) {
			this.currentSpeed = 0;
			this.road.faultVehiclesCounter++;
			}				
		}
	
	public int getFaultyTime() {
		return this.faultyTimeLeft;
	}
	
	public boolean isFaulty() {
		return this.faultyTimeLeft > 0;
	}
	
	public int compareTo(Vehicle v) {
		return this.location - v.location;
	}
	
	public String getId() {
		return this.id;
	}
	
	public int getLocation() {
		return this.location;
	}

	@Override
	public void advance(){
		if(this.faultyTimeLeft > 0) {
			this.faultyTimeLeft--;
			if(this.faultyTimeLeft == 0)
				this.road.faultVehiclesCounter--;
			this.currentSpeed = 0;
		}
		else if(this.isInAJunction) {
			this.setSpeed(0); 	
		}
		else {
			this.location += this.currentSpeed;
			this.kilometrage += this.currentSpeed;;
			
			if(this.location >= this.road.getLength()) {
				
				this.kilometrage -= this.location - this.road.getLength();;	
				this.location = this.road.getLength();
				this.itinerary.get(currentJuctionIndex).vehicleEntersJunction(this.road.id, this);
				this.isInAJunction = true;
				this.currentSpeed = 0;
				
			}
		}
	}

	@Override
	public String getSectionName() {
		return "vehicle_report";
	}

	@Override
	public void completeSectionDetails(IniSection is) {	//TODO NO SE SI SE HACE ASI
		String ret = "";
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

	public double getTiempoDeInfraccion() {
		
		return this.faultyTimeLeft;
	}

	public Road getRoad() {
		return new Road(this.road);
	}

	public int getSpeed() {
		return this.currentSpeed;
	}

	public int getKm() {
		return this.kilometrage;
	}

	public ArrayList<Junction> getItinerary() {
		return new ArrayList<Junction>(this.itinerary);
	}

	@Override
	public String getTemplate() {
		return "[new_vehicle]" + System.lineSeparator() +
		"time = <NONEG-INTEGER>" + System.lineSeparator() +
		"id = <VEHICLE-ID>" + System.lineSeparator() +
		"max_speed = <POSITIVE-INTEGER>"  + System.lineSeparator() +
		"itinerary = <JUNC-ID>,<JUNC-ID>(,<JUNC-ID>)*"  + System.lineSeparator();
	}
	
}
