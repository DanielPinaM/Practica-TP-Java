package es.ucm.fdi.Objects;

import es.ucm.fdi.ini.IniSection;

public class Highway extends Road {

	private int lanes;
	
	public Highway(String id, int length, int maxSpeed, int lanes) {
		super(id, length, maxSpeed);
		this.lanes = lanes;
		super.type = RoadType.LANES;
	}

	protected int calcultaReductionFactor() {
		return (lanes > this.faultVehiclesCounter) ? 1 : 2;
	}
	
	
	protected int calculateSpeed() {
	/*La velocidad base (velocidadBase) de una autopista también considera el número de
	carriles y se define como min(m, m*l /max(n,1) + 1), 
	donde m es la velocidad máxima, 
	n es el	número de vehículos en la carretera y 
	l es el número de carriles. 
	La división es entera
	 * */	
		int m = super.maxSpeed;
		int l = this.lanes;
		int n = this.vehicles.size();
		return super.min(m, m*l/super.max(n, 1) + 1);
	}
	
	
	public void completeSectionDetails(IniSection is) {
		String ret = "";
		is.setValue("type", this.type.toString().toLowerCase());
		for (int i = 0; i < this.vehicles.size(); i++) {
			if(i==this.vehicles.size()-1)
				ret += "(" + this.vehicles.get(i).getId() + "," + this.vehicles.get(i).getLocation() + ")";
			else 
				ret += "(" + this.vehicles.get(i).getId() + "," + this.vehicles.get(i).getLocation() + "),";
		}
		is.setValue("state", ret);
		
	}
}
