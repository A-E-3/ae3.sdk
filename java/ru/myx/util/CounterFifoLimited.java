/*
 * Created on 24.12.2004
 *
 * Window - Preferences - Java - Code Style - Code Templates
 */
package ru.myx.util;

import java.util.LinkedList;

/**
 * @author myx
 * 
 *         Window - Preferences - Java - Code Style - Code Templates
 */
public final class CounterFifoLimited extends Number {
	
	private final LinkedList<Number>	values				= new LinkedList<>();
	
	/**
	 * 
	 */
	private static final long			serialVersionUID	= 4683483335057438214L;
	
	private double						value;
	
	private int							count;
	
	private double						maximum;
	
	private double						minimum;
	
	private final int					historyMin;
	
	private final int					historyMax;
	
	/**
	 * @param initial
	 * @param historyMin
	 * @param historyMax
	 */
	public CounterFifoLimited(final double initial, final int historyMin, final int historyMax) {
		this( historyMin, historyMax );
		this.register( initial );
	}
	
	/**
	 * @param historyMin
	 * @param historyMax
	 */
	public CounterFifoLimited(final int historyMin, final int historyMax) {
		this.value = 0;
		this.count = 0;
		// MAX value, so any other value is new MIN
		this.minimum = Double.MAX_VALUE;
		// MIN value, so any other value is new MAX
		this.maximum = Double.MIN_VALUE;
		this.historyMin = historyMin;
		this.historyMax = historyMax;
	}
	
	@Override
	public double doubleValue() {
		if (this.count < this.historyMin) {
			return Double.NaN;
		}
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
		if (this.count < this.historyMin) {
			return Double.NaN;
		}
		return 1.0 * this.value / this.count;
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
		if (this.maximum == Double.NaN || this.maximum == Double.MIN_VALUE) {
			// MIN value, so any other value is new MAX
			double maximum = Double.MIN_VALUE;
			for (final Number number : this.values) {
				final double value = number.doubleValue();
				if (maximum < value) {
					maximum = value;
				}
			}
			return this.maximum = maximum;
		}
		return this.maximum;
	}
	
	/**
	 * @return double
	 */
	public double getMinimum() {
		if (this.minimum == Double.NaN || this.minimum == Double.MAX_VALUE) {
			// MAX value, so any other value is new MIN
			double minimum = Double.MAX_VALUE;
			for (final Number number : this.values) {
				final double value = number.doubleValue();
				if (minimum > value) {
					minimum = value;
				}
			}
			return this.minimum = minimum;
		}
		return this.minimum;
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
		this.values.addLast( new Double( value ) );
		this.value += value;
		++this.count;
		if (value > this.maximum) {
			this.maximum = (float) value;
		}
		if (value < this.minimum) {
			this.minimum = (float) value;
		}
		if (this.count > this.historyMax) {
			final double unregisterValue = this.values.removeFirst().doubleValue();
			this.value -= unregisterValue;
			--this.count;
			if (unregisterValue >= this.maximum) {
				this.maximum = Double.MIN_VALUE;
			}
			if (unregisterValue <= this.minimum) {
				this.minimum = Double.MAX_VALUE;
			}
		}
	}
	
	@Override
	public String toString() {
		return String.valueOf( this.doubleValue() );
	}
}
