package es.ucm.fdi.model;

public interface Observer<T> {
	public void addObserver(T o);
	public void removeObserver(T o);
}
