package ca.usask.abm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A container class that stores a value and an associated name
 * @author isv452
 *
 * @param <T> the value type
 */
public class Named<T> {
	private T value;
	private String name;
	
	public Named(String n, T val){
		name = n;
		value = val;
	}
	
	public T getValue(){ return value;}
	public String getName() { return name;}
	
	/**
	 * @param ts a list of named values
	 * @return a list of their names, in the same order
	 * 		   = map (_.getName())
	 * @param <T> the value type
	 */
	public static <T> List<String> getNames(Collection<Named<T>> ts){
		ArrayList<String> names = new ArrayList<String>();
		for (Named<T> t : ts){
			names.add(t.getName());
		}
		return names;
	}
	
	/**
	 * A factory method that eliminates the necessity to specify the type parameter
	 * @param n the name
	 * @param val the value
	 * @return a resulting named object
	 */
	public static <T> Named<T> create(String n, T val) { return new Named<T>(n, val);}
}
