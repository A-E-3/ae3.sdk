/*
 * Created on 24.12.2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.util;

/**
 * @author myx
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public final class Counter extends Number {
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 4683483335057438214L;
	
	private double				value;
	
	private int					count;
	
	private double				maximum;
	
	private double				minimum;
	
	/**
	 */
	public Counter() {
		this.value = 0;
		this.count = 0;
		// MAX value, so any other value is new MIN
		this.minimum = Double.MAX_VALUE;
		// MIN value, so any other value is new MAX
		this.maximum = Double.MIN_VALUE;
	}
	
	/**
	 * @param initial
	 */
	public Counter(final double initial) {
		this.value = initial;
		this.count = 1;
		this.minimum = initial;
		this.maximum = initial;
	}
	
	/**
	 */
	public void decrement() {
		this.register( -1 );
	}
	
	@Override
	public double doubleValue() {
		return this.value;
	}
	
	@Override
	public float floatValue() {
		return (float) this.value;
	}
	
	/**
	 * @return double
	 */
	public double getAverage() {
		return this.value / this.count;
	}
	
	/**
	 * @return int
	 */
	public int getCount() {
		return this.count;
	}
	
	/**
	 * @return double
	 */
	public double getMaximum() {
		return this.maximum;
	}
	
	/**
	 * @return double
	 */
	public double getMinimum() {
		return this.minimum;
	}
	
	/**
	 * @return double
	 */
	public double getValue() {
		return this.value;
	}
	
	/**
	 */
	public void increment() {
		this.register( 1 );
	}
	
	@Override
	public int intValue() {
		return (int) this.value;
	}
	
	@Override
	public long longValue() {
		return (long) this.value;
	}
	
	/**
	 * @param value
	 */
	public void register(final double value) {
		this.value += value;
		this.count++;
		if (value > this.maximum) {
			this.maximum = value;
		}
		if (value < this.minimum) {
			this.minimum = value;
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf( this.value );
	}
}
