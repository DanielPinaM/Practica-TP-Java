package es.ucm.fdi.Objects;

import java.util.ArrayList;
import java.util.List;

public class IncomingRoad {

		 protected Road road;
		 protected List<Vehicle> vehicleQueue;
		 protected boolean trafficLight; // true=verde, false=rojo
		 
		 
		 public IncomingRoad(Road road) {
			 this.trafficLight = road.trafficLight;
			 this.vehicleQueue = new ArrayList<Vehicle>();
			 for(int i = 0; i < road.getVehiclesList().size(); i++) {
				 if(road.getVehiclesList().get(i).getLocation()== road.getLength())
					 this.vehicleQueue.add(road.getVehiclesList().get(i));
			 }
			 this.trafficLight = road.getBooleanLight();
			 this.road = road;
		 }
		 
		 void setTrafficLight(boolean color) {
			 this.trafficLight = road.trafficLight = color;
		 }

		 public void advanceFirstVehicle(){
			 if(!vehicleQueue.isEmpty()){
				 this.vehicleQueue.get(0).movetoNextRoad();
				 this.vehicleQueue.remove(0);
			 }
			 
		 // coge el primer vehiculo de la cola, lo elimina,
		 // y le manda que se mueva a su siguiente carretera.
		 }
		public String getTrafficLight() {
			return this.trafficLight ? "green" : "red";
		}
		
		public boolean getBooleanLight(){
			return this.trafficLight;
		}
		 @Override
		 public String toString() {
			 return this.road.id;
		 }

		public Road getCarretera() {
			Road ret = new Road(this.road);
			return ret;
		}

		public boolean tieneSemaforoVerde() {
			return this.trafficLight;
		}
	
}
