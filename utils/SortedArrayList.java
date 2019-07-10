package es.ucm.fdi.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;

public class SortedArrayList<E> extends ArrayList<E> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private Comparator<E> cmp;
	 public SortedArrayList(Comparator<E> cmp) {
		 this.cmp = cmp;
	 }
	 
		public boolean add(E e)
		{
			if (!this.contains(e)) {
				super.add(e);
				super.sort(cmp);
				return true;
			}
			return false;
		}
	 
	 @Override
	 public boolean addAll(Collection<? extends E> c) {
		 //programar inserción ordenada (invocando a add)
		 Iterator<? extends E> i =  c.iterator();
		 boolean ok = true;
		 while(i.hasNext() && ok){
			ok = this.add(i.next());
		}
		 return ok;
	 }
	 
	 // sobreescribir los métodos que realizan operaciones de
	 // inserción basados en un índice para que lancen excepcion.
	 @Override
	 public void add(int index, E element){
		super.add(index, element);
	 }
	}