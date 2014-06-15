package ca.usask.abm;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * A class representing multi-dimensional arrays, and allowing
 * them to be viewed as one-dimensional arrays (using lexicographic order of coordinates)
 * @author isv452
 *
 */
public class MultiDimArray<T> extends AbstractList<T>{
	private ArrayList<T> array;
	private Collection<Integer> dims;
	
	/**
	 * Creates a new array given a sequence of dimensions
	 * @param ds a nonempty sequence of positive integers
	 */
	public MultiDimArray(Collection<Integer> ds){
		dims = ds;
		int totalLength = 1;
		for (int d : dims){
			totalLength *= d;
		}
		array = new ArrayList<T>(totalLength);
	}
	
	/**
	 * @param index coordinate vector
	 * @return true if this is a valid coordinate index
	 */
	public boolean valid(Collection<Integer> index){
		if (index.size() != dims.size()) return false;
		Iterator<Integer> ds = dims.iterator();
		for (int i : index){
			if (i < 0 || i >= ds.next()) return false;
		}
		return true;
	}
	
	/**
	 * PRECONDITION: valid(index)
	 * @param index a valid coordinate index
	 * @return the corresponding linear index
	 */
	public int toLinearIndex(Collection<Integer> index){
		int sum = 0;
		int product = size();
		Iterator<Integer> ds = dims.iterator();
		for (int i : index){
			// update product to be the number of elements in each subarray
			product /= ds.next();
			// update sum
			sum += product * i;
		}
		return sum;
	}
	
	/**
	 * PRECONDITION: index is between 0 and size()
	 * @param index a given linear index
	 * @return the corresponding co-ordinate index
	 * POSTCONDITION: toLinearIndex(fromLinearIndex(index)) == index
	 */
	public Collection<Integer> fromLinearIndex(int index){
		ArrayList<Integer> coord = new ArrayList<Integer>(dims.size());
		int product = size();
		for (int dim : dims){
			product /= dim;
			coord.add(index / product);
			index %= product;
		}
		return coord;
	}
	
	// list methods
	@Override
	public int size(){ return array.size();}
	@Override
	public T get(int idx){ return array.get(idx);}
	@Override
	public T set(int index, T value) { return array.set(index,  value);}

	
}
