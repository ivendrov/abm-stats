package ca.usask.abm;

/**
 * Models a function between two types; is a Java 8 Functional Interface
 * @author isv452
 *
 */
public interface Function<T, U> {
	/**
	 * @param argument the argument to the function
	 * @return the result of applying the function to the argument
	 */
	U apply(T argument);
}
