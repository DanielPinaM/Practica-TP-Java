package es.ucm.fdi.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ucm.fdi.exception.SimulationError;
import es.ucm.fdi.ini.IniSection;

public class Junction extends SimulationObject{
	
	private int GreenTrafficLightIndex;	//Indice del semaforo en verde:
	protected List<IncomingRoad> incomingRoads; //carreteras entrantes
	protected Map<String, IncomingRoad> IncomingRoadsMap;
	protected Map<Junction, Road> OutgoingRoad;		//Contiene: (j1, r1), (j2, r2)
	protected boolean first;
	protected JunctionType type;
	
	public Junction(String id) {
		super(id);
		this.GreenTrafficLightIndex = 0;
		this.incomingRoads = new ArrayList<IncomingRoad>();
		this.OutgoingRoad = new HashMap<Junction, Road>();
		this.IncomingRoadsMap = new HashMap<String, IncomingRoad>();
		first = true;
	}
	
	public Road roadToJunction(Junction junction) {
		return this.OutgoingRoad.get(junction);
	}
	
	public void addIncomingRoadToJunction(String roadId, Road road) {
		// añade una carretera entrante al “mapaCarreterasEntrantes” y a las “carreterasEntrantes”
		if(this.incomingRoads.size()==0)
			road.trafficLight = true;
		IncomingRoad iR = new IncomingRoad(road);
		this.incomingRoads.add(iR);
		this.IncomingRoadsMap.put(roadId, iR);
	}
	
	public void addOutGoingRoadToJunction(Junction dest, Road road) {
		//añade una carretera saliente al cruce
		this.OutgoingRoad.put(dest, road);
	}
	
	public void vehicleEntersJunction(String roadId, Vehicle vehicle) {
		// añade el “vehiculo” a la carretera entrante “idCarretera”
		this.IncomingRoadsMap.get(roadId).vehicleQueue.add(vehicle);
	}
	
	public void updateTrafficLights() throws SimulationError {
		if(this.incomingRoads.size()==0) throw new SimulationError("No hay carreteras entrantes.");
		
		if(this.incomingRoads.size() > 1 && !first){
			boolean color = this.incomingRoads.get(GreenTrafficLightIndex).getBooleanLight();
			this.incomingRoads.get(this.GreenTrafficLightIndex).setTrafficLight(!color);
			
			this.GreenTrafficLightIndex = (this.GreenTrafficLightIndex + 1) % this.incomingRoads.size();

			if(this.GreenTrafficLightIndex < this.incomingRoads.size() && this.incomingRoads.size()!=1)
				this.incomingRoads.get(this.GreenTrafficLightIndex).setTrafficLight(true);
		}
	}
	
	@Override
	public void advance() throws SimulationError {
		if(!this.incomingRoads.isEmpty()) {
			// Si “carreterasEntrantes” es vacío, no hace nada.
				 // en otro caso “avanzaPrimerVehiculo” de la carretera con el semáforo verde.
				 // Posteriormente actualiza los semáforos.ç
			
			this.incomingRoads.get(this.GreenTrafficLightIndex).advanceFirstVehicle();
			this.updateTrafficLights();
			if(first)
				 first = false;
		}
	}
	
	@Override 
	public void completeSectionDetails(IniSection is){
		String ret = "";

		for(int j = 0; j < this.incomingRoads.size(); ++j){
			
			String vehicleListReport ="";
			ArrayList<Vehicle> vehicleList = (ArrayList<Vehicle>) this.incomingRoads.get(j).vehicleQueue;
			
 			for(int i = 0; i < vehicleList.size(); i++){
				if(i == vehicleList.size() - 1)
					vehicleListReport += vehicleList.get(i).getId();
				else 
					vehicleListReport += vehicleList.get(i).getId()+ ",";
			}
 			if(j!= this.incomingRoads.size()-1)
 				ret += "("+this.incomingRoads.get(j).road.getId()+","+this.incomingRoads.get(j).getTrafficLight()+",["+vehicleListReport+"]),";
 			else
 				ret += "("+this.incomingRoads.get(j).road.getId()+","+this.incomingRoads.get(j).getTrafficLight()+",["+vehicleListReport+"])";
		}
		is.setValue("queues", ret);
	}

	@Override
	public String getSectionName() {
		return "junction_report";
	}

	public void setFirstGreenLight() {
		if(!(this.incomingRoads.size()==0))
			this.incomingRoads.get(0).setTrafficLight(true);
		
	}

	public List<IncomingRoad> getCarreteras() {
		ArrayList<IncomingRoad> viewList = new ArrayList<IncomingRoad>(this.incomingRoads);
		return viewList;
	}

	public String getGreenRoads() {
		String ret = "[";
		for(IncomingRoad r : this.incomingRoads){
			if(r.road.trafficLight)
				ret+= "(" + r.road.id + ", " + r.road.getTrafficLight() + ", " + "[" + r.road.getVehiclesList().toString() + "]";
		}
		ret += "]";
		
		return ret;
	}

	public String getRedRoads() {
		String ret = "[";
		for(IncomingRoad r : this.incomingRoads){
			if(!r.road.trafficLight)
				ret+= "(" + r.road.id + ", " + r.road.getTrafficLight() + ", " + "[" + r.road.getVehiclesList().toString() + "]";
		}
		ret += "]";
		
		return ret;
	}

	@Override
	public String getTemplate() {

		return "[new_junction]" + System.lineSeparator() +
				"time = <NONEG-INTEGER>"+ System.lineSeparator() +
		 "id = <JUNC-ID>"+ System.lineSeparator();
	}
}
