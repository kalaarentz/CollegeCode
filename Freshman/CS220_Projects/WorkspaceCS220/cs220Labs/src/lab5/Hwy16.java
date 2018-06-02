/*
Author: Kala Arentz

Date: Apr 21, 2015

Purpose: implementing the traffic jam and traffic jam factory

*/
package lab5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Random;

public class Hwy16<E> implements TrafficJam<E>
{
	private ArrayList< E > one;
	private ArrayList< E > two;
	
	public Hwy16()
	{
		one = new ArrayList<>();
		two = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Hwy16<Integer> practice = new Hwy16<>();
		
		practice.add( 5, 1);
		practice.add( 10, 1 );
		practice.add( 7, 1 );
		practice.add( 1, 1 );
		practice.add( 3, 2 );
		practice.add( 11, 2 );
		
		Iterator< Integer > hwy16Cars = practice.iterator();
		
		//practice.remove();
		//practice.remove();
		
		for( Integer num : practice )
		{
			System.out.println( num );
		}

	}

	/**
	 * 
	 */
	@Override
	public Iterator<E> iterator() {
		// private inner class implements iterator
		return new Hwy16Iterator();
	}

	/**
	 * How many lanes are in this traffic jam. 
	 * 
	 */
	@Override
	public int getLaneCount() {
		
		// hard coded the number of lanes since I am assuming there
		// is only two lanes on Hwy 16
		
		return 2;
	}

	/**
	 * A car can be added to the traffic jam. When they are added, they must 
	 * specify which lane they are joining. The lane numbers begin at 1, and
	 * go up to the number of lanes available for that particular traffic jam.
	 *  
	 */
	
	@Override
	public void add(E element, int lane) {
		// Check for which lane is getting the element added to
		// ArrayList element is added at the end if no position is given
		
		if( lane == 1 )
		{
			one.add( (E) element );
		}
		else
		{
			two.add( (E) element );
		}
		
	}

	/**
	 * Cars may be removed from the traffic jam. Only cars at the front of 
	 * their lanes can be removed. If more than one lane is occupied, the 
	 * car that is removed is selected uniformly at random.
	 */
	@Override
	public E remove() {
		// Pick a random non-empty lane 
		// remove element form position 0
		Random r = new Random();
		
		boolean lane = r.nextBoolean();
		
		if( lane && one.size() > 0 )
		{
			return one.remove( 0 );
		}
		else if( two.size() > 0 )
		{
			return two.remove( 0 );
		}
		else
		{
			return one.remove( 0 );
		}
		
	}

	/**
	 * Cars have a 40% chance of switching lanes. Car can  switch lanes if the
	 * same position in an adjacent lane is clear. The car will always go to
	 * the lane with the lowest amount of cars.
	 * 
	 */
	@Override
	public void shuffle() {
		// Find which lane has less cars
		// lane one has less cars then lane two
		
		// For every car in lane 1
		for( int idx = one.size() - 1; idx >= 0; idx-- )
		{
			if( idx >= two.size() && shuffleTheCar( ) )
			{
				two.add( one.remove( idx ) );
			}
		}
		// For every car in lane 2
		for( int idx = two.size() - 1; idx >= 0; idx-- )
		{
			if( idx >= one.size() && shuffleTheCar( ) )
			{
				one.add( two.remove( idx ) );
			}
		}
		
		
		
	}
	
	/**
	 * Will determine if the car will shuffle or not
	 * @return true: car is below 40%
	 * 		   false: car is above 40%
	 */
	private boolean shuffleTheCar()
	{
		Random rand = new Random();
		int chance = rand.nextInt(10);
		if( chance < 4 )
		{
			return true;
		}
		return false;
		
	}
	
	/**
	 * This is the private inner class iterator.
	 * 
	 * @author kalaarentz
	 */
	private class Hwy16Iterator implements Iterator<E>
	{
		private int listIndex;
		private ArrayList<E> lanes = new ArrayList<>();
		
		/**
		 *  In this constructor the new array list lanes will addAll
		 *  the elements from lane one and lane two so it can iterate 
		 *  over all the elements.
		 */
		public Hwy16Iterator()
		{
			lanes.addAll( one );
			lanes.addAll( two );
		}
		
		@Override
		public boolean hasNext() {

			return listIndex < lanes.size() ;
		}

		@Override
		public E next() {
			
			if( !hasNext() )
			{
				throw new NoSuchElementException( 
						"No more cars in traffic jam" );
			}
			
			return (E) lanes.get( listIndex++ );
		}
		
		@Override
		public void remove()
		{
			throw new UnsupportedOperationException();
		}
		
	}

}
