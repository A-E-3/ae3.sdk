package ru.myx.time;

import java.util.Calendar;

/**
 * 
 * @author myx
 * 
 * @param <T>
 */
public interface TimeCycleCallback<T extends Throwable> {
	/**
	 * 
	 * @param start
	 * @param end
	 * @param period
	 * @throws T
	 */
	public void execute(final Calendar start, final Calendar end, final TimePeriodType period) throws T;
}
