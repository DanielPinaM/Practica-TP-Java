package es.ucm.fdi.Objects;

public class CongestedJunction extends Junction {
	protected JunctionType type;
	
	public CongestedJunction(String id, JunctionType type) {
		super(id);
		this.type = type;
	}

}
