package ca.usask.abm;

/**
 * A container class that stores a value and an associated time
 * @author isv452
 *
 * @param <T> the value type
 */
public class Timed<T> {
	private T value;
	private double time;
	
	public Timed(double t, T val){
		time = t;
		value = val;
	}
	
	public T getValue(){ return value;}
	public double getTime() { return time;}
}
