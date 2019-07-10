package es.ucm.fdi.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.exception.MapException;
import es.ucm.fdi.exception.SimulationError;


public class RoadsMap {
	private List<Road> roads;
	private List<Junction> junctions;
	private List<Vehicle> vehicles;
	
	private Map<String, Road> roadMap;
	private Map<String, Junction> junctionMap;
	private Map<String, Vehicle> vehiclesMap;
	
	public RoadsMap(){
		this.roads = new ArrayList<Road>();
		this.junctions = new ArrayList<Junction>();
		this.vehicles = new ArrayList<Vehicle>();
		
		this.roadMap = new HashMap<String, Road>();
		this.junctionMap = new HashMap<String, Junction>();
		this.vehiclesMap = new HashMap<String, Vehicle>();
	}
	
	public void addJunction(String junctionId, Junction j) throws MapException {
		if(!this.junctionMap.containsKey(junctionId)){
			this.junctions.add(j);
			this.junctionMap.put(junctionId, j); // put == add?
		}
		else {
			throw new MapException("Junction: '" + junctionId + "' is already on map");
		}
	}
	
	public void addVehicle(String vehicleId, Vehicle v) throws MapException {
		if(!this.vehiclesMap.containsKey(vehicleId)){
			this.vehicles.add(v);
			this.vehiclesMap.put(vehicleId, v);
			v.movetoNextRoad();
		}
		else {
			throw new MapException("Vehicle: '" + vehicleId + "' is already on map");
		}
	}
	
	public void addRoad(String roadId, Junction j1/*origen*/, Junction j2/*destino*/, Road road)
	throws MapException{
		if(!this.roadMap.containsKey(roadId)){
			this.roads.add(road);
			road.setDest(j2);
			road.setSrc(j1);
			this.roadMap.put(roadId, road);
			j1.addOutGoingRoadToJunction(j2, road);
			j2.addIncomingRoadToJunction(roadId, road);
		}
		else {
			throw new MapException("Road: '" + roadId + "' is already on map");
		}
	}
	
	public String generateReport(int time){
		 String report = "";
		 // genera informe para cruces
		 for (Junction j : this.junctions)
			 report += j.generateReport(time);
		 // genera informe para carreteras
		 for (Road r : this.roads)
			 report += r.generateReport(time);
		 // genera informe para vehiculos
		 for (Vehicle v : this.vehicles)
			 report += v.generateReport(time);
		return report;
		}
	
	public String getReports(List<Vehicle> vehicleList, List<Road> roadList, List<Junction> junctionList, int time) {
		String ret = "";
		for(Vehicle v : vehicleList) {
			ret+= v.generateReport(time);
		}
		
		for(Road r : roadList) {
			ret+= r.generateReport(time);
		}
		
		for(Junction j : junctionList)
			ret+= j.generateReport(time);
		
		return ret;
	}
	
	public void update() throws Exception {
	 // llama al método avanza de cada cruce
		for (Junction j : this.junctions)
			j.advance();
	 // llama al método avanza de cada carretera
		for (Road r : this.roads)
			r.advance();
	}
	
	public Junction getJunction(String id) throws MapException {
	 // devuelve el cruce con ese “id” utilizando el mapaDeCruces.
		 // sino existe el cruce lanza excepción.
		Junction j = this.junctionMap.get(id);
		if (j == null)
			throw new MapException("Junction: '" + id + "' is not on map");
		else return j;

	}
	
	public Vehicle getVehicle(String id) throws MapException{
	 // devuelve el vehículo con ese “id” utilizando el mapaDeVehiculos.
	 // sino existe el vehículo lanza excepción.
		Vehicle v = this.vehiclesMap.get(id);
		if (v == null)
			throw new MapException("Vehicle: '" + id + "' is not on map");
		else return v;
	}
	
	public Road getRoad(String id) throws MapException{
	 // devuelve la carretera con ese “id” utilizando el mapaDeCarreteras.
	 // sino existe la carretra lanza excepción.
		Road r = this.roadMap.get(id);
		if (r == null)
			throw new MapException("Road: '" + id + "' is not on map");
		else return r;
	}
	
	public void roadsAdvance(){
		for (Road r : this.roads) 
			r.advance();
	}
	
	public void junctionsAdvance() throws SimulationError{
		for (Junction j: this.junctions)
			j.advance();
	}

	public void setFirstGreenLight(){
		for(Junction j : this.junctions){
			j.setFirstGreenLight();
		}
		
	}

	public List<Junction> getCruces() {
		List<Junction> list = new ArrayList<>();
		for(Junction j : this.junctions)
			list.add(j);
		return list;
	}

	public List<Road> getCarreteras() {
		List<Road> list = new ArrayList<>();
		for(Road r : this.roads)
			list.add(r);
		return list;
	}

	public List<Vehicle> getVehiculos() {
		List<Vehicle> list = new ArrayList<>();
		for(Vehicle v : this.vehicles)
			list.add(v);
		return list;
	}

}
