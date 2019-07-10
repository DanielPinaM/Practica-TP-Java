package es.ucm.fdi.Objects;

import es.ucm.fdi.ini.IniSection;

public abstract class SimulationObject {
	protected String id;
	
	public SimulationObject(String id){
		this.id = id;
	}
	
	@Override
	public String toString(){
		return this.id;
	}
	
	public String generateReport(int tiempo){
		IniSection is = new IniSection(this.getSectionName());
		is.setValue("id", this.id);
		is.setValue("time", tiempo);
		this.completeSectionDetails(is);
		return is.toString() + System.lineSeparator();
	}
		// los métodos getNombreSeccion y completaDetallesSeccion
		// tendrán que implementarlos cada subclase de ObjetoSimulacion
	public abstract void advance() throws Exception;
	
	public abstract String getSectionName();
	
	public abstract void completeSectionDetails(IniSection is);
	
	public abstract String getTemplate();
	
	public String getId(){
		return this.id;
	}
	
}
