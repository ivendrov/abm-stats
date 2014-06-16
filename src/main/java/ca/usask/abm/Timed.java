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
	
	/**
	 * @return a function that extracts the value from a timed object
	 */
	public static <T> Function<Timed<T>, T> valueFunction() {
		return new Function<Timed<T>, T>(){

			@Override
			public T apply(Timed<T> argument) {
				return argument.getValue();
			}
			
		};
	}
	
	/**
	 * @return a function that extracts the time from a timed object
	 */
	public static <T> Function<Timed<T>, Double> timeFunction() {
		return new Function<Timed<T>, Double>(){

			@Override
			public Double apply(Timed<T> argument) {
				return argument.getTime();
			}
			
		};
	}
}
