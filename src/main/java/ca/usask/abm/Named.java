package ca.usask.abm;

/**
 * A container class that stores a value and an associated name
 * @author isv452
 *
 * @param <T>
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
}
