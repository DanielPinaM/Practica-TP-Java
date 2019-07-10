package es.ucm.fdi.Objects;

import es.ucm.fdi.ini.IniSection;

public class Dirt extends Road {

	
	
	public Dirt(String id, int length, int maxSpeed) {
		super(id, length, maxSpeed);
		super.type = RoadType.DIRT;
	}

	protected int calcultaReductionFactor() {
		return 1 + this.faultVehiclesCounter;
	}
	
	protected int calculateSpeed() {
		return super.maxSpeed;
	}
	
	public void completeSectionDetails(IniSection is) {	//TODO NO SE SI SE HACE ASI
		super.completeSectionDetails(is);
		is.setValue("type", this.type.toString());
	}
}
