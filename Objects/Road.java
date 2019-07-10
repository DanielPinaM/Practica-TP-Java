package es.ucm.fdi.Objects;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;

import es.ucm.fdi.ini.IniSection;
import es.ucm.fdi.utils.SortedArrayList;


public class Road extends SimulationObject{
	
	protected boolean trafficLight;//true = green, false = red;
	protected int length;
	protected int maxSpeed;
	protected ArrayList<Vehicle> vehicles; 
	protected RoadType type;
	protected int faultVehiclesCounter;
	Comparator<Vehicle> cmp = new Comparator<Vehicle>() {
		@Override
		public int compare(Vehicle o1, Vehicle o2) {
			return o2.compareTo(o1);
		}
	 };
	 protected Junction src;
		public Junction getCruceOrigen() {
			return this.src;
		}

	public void setSrc(Junction src) {
		this.src = src;
	}

	public Junction getCruceDestino() {
		return dest;
	}

	public void setDest(Junction dest) {
		this.dest = dest;
	}

	protected Junction dest;
	
	
	public Road(String id, int length, int maxSpeed/*sería interesante agregar un 3er parámetro, el cual sea una colección cualquiera de cars*/) {
		super(id);
		this.length = length;
		this.maxSpeed = maxSpeed;
		this.vehicles = new SortedArrayList<Vehicle>(cmp);
		this.faultVehiclesCounter = 0;
	}
	
	public Road(String id, int length, int maxSpeed, Collection<Vehicle> c) {
		super(id);
		this.length = length;
		this.maxSpeed = maxSpeed;
		this.vehicles = new SortedArrayList<Vehicle>(null);
		
		for(Vehicle v : c)
			this.addVehicle(v);
		this.faultVehiclesCounter = 0;
	}
	
	public Road(Road road) {
		this(road.id, road.length, road.maxSpeed, road.vehicles);
	}

	public ArrayList<Vehicle> getVehiclesList(){
		return this.vehicles;
	}
	
	public boolean addVehicle(Vehicle v)/*entraVehiculo()*/ {

		return this.vehicles.add(v);
	}
	
	public boolean removeVehicle(Vehicle v)/*saleVehiculo()*/{
		this.vehicles.remove(v);
		this.vehicles.sort(cmp);
		return true;
	}
	
	public void advance()/*avanza*/{
		//1.- Calcular velocidad base.
		int baseSpeed = this.calculateSpeed();
		
		//2.- Para cada vehículo
			//a) Se pone su velocidad a velocidadBase/factorReduccion
			//b) Se pide al vehículo que avance
		int i = 0;
		boolean faulty = false;
		while (i < this.vehicles.size() && !faulty) {
			Vehicle dummy = this.vehicles.get(i);
			this.vehicles.get(i).setSpeed(baseSpeed);
			this.vehicles.get(i).advance();
			if(this.vehicles.contains(dummy))
					faulty = this.vehicles.get(i).isFaulty();
			++i;
		}
		int reductionFactor = this.calcultaReductionFactor(); //1 por defecto
		while(i < this.vehicles.size()) {		//Si hay faulty se recorre el resto de v
			this.vehicles.get(i).setSpeed(baseSpeed/reductionFactor);
			this.vehicles.get(i).advance();
			i++;
		}
		this.vehicles.sort(cmp);
	}
		
	protected int calculateSpeed() {
		//VelocidadBase = mín (m, m / máx(n,1)) + 1
			//donde m es maxSpeed y n es el número total de coches vehicles.size().
		return this.min(this.maxSpeed, this.maxSpeed/this.max(this.vehicles.size(), 1) + 1);
	}
	
	protected int calcultaReductionFactor() {
		return this.faultVehiclesCounter > 0 ? 2 : 1;
	}
	
	protected int max(int a, int b) {
		return (a > b) ? a : b;
	}
	
	protected int min(int a, int b) {
		return (a < b) ? a : b;
	}

	public int getLength() {
		return this.length;
	}

	public String getId() {
		
		return this.id;
	}
	

	public String getTrafficLight() {
		return this.trafficLight ? "green" : "red";
	}

	public boolean getBooleanLight(){
		return this.trafficLight;
	}
	
	@Override
	public String getSectionName() {
		return "road_report";
	}

	@Override
	public void completeSectionDetails(IniSection is) {
		String ret = "";
		for (int i = 0; i < this.vehicles.size(); i++) {
			if(i==this.vehicles.size()-1)
				ret += "(" + this.vehicles.get(i).getId() + "," + this.vehicles.get(i).getLocation() + ")";
			else 
				ret += "(" + this.vehicles.get(i).getId() + "," + this.vehicles.get(i).getLocation() + "),";
		}
		is.setValue("state", ret);
		
	}
	
	public String toString(){
		return this.id;
	}

	public ArrayList<Vehicle> getVehiculos() {
		return new ArrayList<Vehicle>(this.vehicles);
	}

	public int getLongitud() {
		return this.length;
	}

	public int getMaxSpeed() {
		return this.maxSpeed;
	}

	@Override
	public String getTemplate() {

		return "[new_road]" + System.lineSeparator() +  
				"time = <NONEG-INTEGER>" + System.lineSeparator() +
		"id = <ROAD-ID>" + System.lineSeparator() +
		"src = <JUNC-ID>" + System.lineSeparator() +
		"dest = <JUNC-ID>" + System.lineSeparator() +
		"max_speed = <POSITIVE-INTEGER>" + System.lineSeparator() +
		"length = <POSITIVE-INTEGER>"  + System.lineSeparator();
	}


}
