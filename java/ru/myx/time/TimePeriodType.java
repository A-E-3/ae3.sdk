/**
 * 
 */
package ru.myx.time;

import java.util.Calendar;

/**
 * 
 * @author myx
 * 
 */
public enum TimePeriodType {
	/**
	 * 
	 */
	MINUTE {
		@Override
		public void roundCalendar(final Calendar calendar) {
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
		}
		
		@Override
		public void shiftCalendar(final Calendar calendar, final int count) {
			calendar.add( Calendar.MINUTE, count );
		}
	},
	/**
	 * 
	 */
	HOUR {
		@Override
		public void roundCalendar(final Calendar calendar) {
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
		}
		
		@Override
		public void shiftCalendar(final Calendar calendar, final int count) {
			calendar.add( Calendar.HOUR, count );
		}
	},
	/**
	 * 
	 */
	DAY {
		@Override
		public void roundCalendar(final Calendar calendar) {
			calendar.set( Calendar.HOUR_OF_DAY, 0 );
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
		}
		
		@Override
		public void shiftCalendar(final Calendar calendar, final int count) {
			calendar.add( Calendar.DATE, count );
		}
	},
	/**
	 * 
	 */
	WEEK {
		@Override
		public void roundCalendar(final Calendar calendar) {
			calendar.set( Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek() );
			calendar.set( Calendar.HOUR_OF_DAY, 0 );
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
		}
		
		@Override
		public void shiftCalendar(final Calendar calendar, final int count) {
			calendar.add( Calendar.DATE, count * 7 );
		}
	},
	/**
	 * 
	 */
	MONTH {
		@Override
		public void roundCalendar(final Calendar calendar) {
			calendar.set( Calendar.DAY_OF_MONTH, 1 );
			calendar.set( Calendar.HOUR_OF_DAY, 0 );
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
		}
		
		@Override
		public void shiftCalendar(final Calendar calendar, final int count) {
			calendar.add( Calendar.MONTH, count );
		}
	},
	/**
	 * 
	 */
	YEAR {
		@Override
		public void roundCalendar(final Calendar calendar) {
			calendar.set( Calendar.DAY_OF_YEAR, 1 );
			calendar.set( Calendar.HOUR_OF_DAY, 0 );
			calendar.set( Calendar.MINUTE, 0 );
			calendar.set( Calendar.SECOND, 0 );
			calendar.set( Calendar.MILLISECOND, 0 );
		}
		
		@Override
		public void shiftCalendar(final Calendar calendar, final int count) {
			calendar.add( Calendar.YEAR, count );
		}
	};
	
	/**
	 * 
	 * @param calendar
	 */
	public abstract void roundCalendar(final Calendar calendar);
	
	/**
	 * 
	 * @param calendar
	 * @param count
	 */
	public abstract void shiftCalendar(final Calendar calendar, final int count);
}
