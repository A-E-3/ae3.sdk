package ru.myx.time;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

/** @author myx */
public class TimeParameters {

	/** @param callback
	 * @param start
	 * @param end
	 * @throws T */
	public static <T extends Throwable> void doCycleForIntervalHours(final TimeCycleCallback<T> callback, final Calendar start, final Calendar end) throws T {
		
		final Calendar c1 = (Calendar) start.clone();
		c1.setFirstDayOfWeek(Calendar.MONDAY);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		final int STEP_START, STEP_UNIT, STEP_SIZE;
		final TimePeriodType STEP_PERIOD;

		STEP_UNIT = Calendar.HOUR_OF_DAY;
		STEP_PERIOD = TimePeriodType.HOUR;
		STEP_START = 0;
		STEP_SIZE = 1;

		// this call required for calendar to recalculate it's internal
		// fields
		c1.getTimeInMillis();
		c1.set(STEP_UNIT, STEP_START);

		final long endMillis = end.getTimeInMillis();

		{
			for (;;) {
				final Calendar c2 = (Calendar) c1.clone();
				c1.add(STEP_UNIT, STEP_SIZE);
				if (c1.getTimeInMillis() > endMillis) {
					break;
				}
				c2.getTimeInMillis();
				callback.execute(c2, c1, STEP_PERIOD);
			}
		}
	}
	
	/** @param timezone
	 * @return */
	public final static TimeZone findTimezone(final String timezone) {
		
		if (timezone == null) {
			return TimeZone.getDefault();
		}
		final String name;
		final int shift;
		final int hash;
		{
			final int index = timezone.indexOf(',');
			if (index == -1) {
				shift = 0;
				name = timezone;
				hash = 0;
			} else {
				name = timezone.substring(0, index);
				final String right = timezone.substring(index + 1);
				final int index2 = right.indexOf(',');
				if (index2 == -1) {
					shift = Integer.parseInt(right);
					hash = 0;
				} else {
					shift = Integer.parseInt(right.substring(0, index2));
					hash = Integer.parseInt(right.substring(index2 + 1), 36);
				}
			}
		}
		for (final String id : TimeZone.getAvailableIDs()) {
			final TimeZone zone = TimeZone.getTimeZone(id);
			if (zone.getRawOffset() / 60_000L == shift && (hash == 0 || hash == zone.getID().hashCode()) && name.equals(zone.getDisplayName(false, TimeZone.SHORT))) {
				return zone;
			}
		}
		return TimeZone.getDefault();
	}
	
	/** @param timezone
	 * @return */
	public static final Calendar getCurrentCalendar(final TimeZone timezone) {
		
		// final Calendar result = Calendar.getInstance( this.timezone );
		final Calendar result = new GregorianCalendar(timezone, Locale.getDefault());
		result.setFirstDayOfWeek(Calendar.MONDAY);
		final String overrideDate = System.getProperty("timeControl.date", "");
		if (overrideDate.length() > 0) {
			final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			dateFormat.setTimeZone(timezone);
			try {
				result.setTimeInMillis(dateFormat.parse(overrideDate).getTime());
			} catch (final ParseException e) {
				throw new RuntimeException(e);
			}
			TimePeriodType.DAY.roundCalendar(result);
			TimePeriodType.DAY.shiftCalendar(result, 1);
			result.add(Calendar.MILLISECOND, -1);
		}
		return result;
	}
	
	/** @param string */
	public static void setStaticDate(final String string) {
		
		System.setProperty("timeControl.date", string);
	}

	private boolean beforeNow;

	private final int count;

	private final boolean detail;

	private Calendar endDate;

	private final Calendar startDate;

	private final TimeZone timezone;

	private final TimePeriodType type;

	private final TimeControlMode mode;
	
	/** Trend constructor
	 *
	 * @param timezone
	 * @param count
	 * @param type
	 * @param day
	 * @param month
	 * @param year */
	public TimeParameters(final String timezone, final int count, final TimePeriodType type, final int day, final int month, final int year) {
		
		this.timezone = TimeParameters.findTimezone(timezone);
		this.startDate = this.getCurrentCalendar();
		this.detail = false;
		this.type = type;
		this.count = count;
		this.mode = TimeControlMode.TIME_TREND;

		boolean needShift = false;
		if (year == -1) {
			needShift = needShift || type == TimePeriodType.YEAR;
		} else {
			this.startDate.set(Calendar.YEAR, year);
		}
		if (month == -1) {
			needShift = needShift || type == TimePeriodType.MONTH;
		} else {
			this.startDate.set(Calendar.MONTH, month);
		}
		if (day == -1) {
			needShift = needShift || type == TimePeriodType.WEEK || type == TimePeriodType.DAY;
		} else {
			this.startDate.set(Calendar.DAY_OF_MONTH, day);
		}
		if (needShift) {
			// this call required for calendar to recalculate it's internal
			// fields
			this.startDate.getTimeInMillis();
			type.shiftCalendar(this.startDate, -(count - 1));
		}
		{
			// this call required for calendar to recalculate it's internal
			// fields
			this.startDate.getTimeInMillis();
			type.roundCalendar(this.startDate);
			// this call required for calendar to recalculate it's internal
			// fields
			this.startDate.getTimeInMillis();
		}

		this.endDate = (Calendar) this.startDate.clone();
		type.shiftCalendar(this.endDate, count);
	}
	
	/** Detail constructor
	 *
	 * @param timezone
	 * @param count
	 * @param type
	 * @param hour
	 * @param minute
	 * @param day
	 * @param month
	 * @param year */
	public TimeParameters(final String timezone, final int count, final TimePeriodType type, final int hour, final int minute, final int day, final int month, final int year) {
		
		this.timezone = TimeParameters.findTimezone(timezone);
		this.startDate = this.getCurrentCalendar();
		this.detail = true;
		this.type = type;
		this.count = count;
		this.mode = TimeControlMode.TIME_DETAIL;

		boolean needShift = false;
		if (year == -1) {
			needShift = needShift || type == TimePeriodType.YEAR;
		} else {
			this.startDate.set(Calendar.YEAR, year);
		}
		if (month == -1) {
			needShift = needShift || type == TimePeriodType.MONTH;
		} else {
			this.startDate.set(Calendar.MONTH, month);
		}
		if (day == -1) {
			needShift = needShift || type == TimePeriodType.WEEK || type == TimePeriodType.DAY;
		} else {
			this.startDate.set(Calendar.DAY_OF_MONTH, day);
		}
		if (hour == -1) {
			needShift = needShift || type == TimePeriodType.HOUR;
		} else {
			this.startDate.set(Calendar.HOUR_OF_DAY, hour);
		}
		if (minute == -1) {
			needShift = needShift || type == TimePeriodType.MINUTE;
			this.startDate.set(Calendar.SECOND, 59);
			this.startDate.set(Calendar.MILLISECOND, 999);
		} else {
			this.startDate.set(Calendar.MINUTE, minute);
		}
		if (needShift) {
			// this call required for calendar to recalculate it's internal
			// fields
			this.startDate.getTimeInMillis();
			type.shiftCalendar(this.startDate, -count);
		}
		{
			// this call required for calendar to recalculate it's internal
			// fields
			this.startDate.getTimeInMillis();
			TimePeriodType.MINUTE.roundCalendar(this.startDate);
			// this call required for calendar to recalculate it's internal
			// fields
			this.startDate.getTimeInMillis();
		}

		this.endDate = (Calendar) this.startDate.clone();
		type.shiftCalendar(this.endDate, count);
	}
	
	/** Exact interval constructor
	 *
	 * @param timezone
	 * @param dateStart
	 * @param dateEnd */
	public TimeParameters(final TimeZone timezone, final long dateStart, final long dateEnd) {
		
		this.timezone = timezone;
		this.startDate = this.getCurrentCalendar();
		this.startDate.setTimeInMillis(dateStart);
		if (dateEnd == dateStart) {
			this.endDate = this.startDate;
		} else {
			this.endDate = (Calendar) this.startDate.clone();
			this.endDate.setTimeInMillis(dateEnd);
		}
		this.detail = true;
		this.count = 1;
		this.type = null;
		this.mode = TimeControlMode.TIME_POINT;
	}
	
	/** @param callback
	 * @throws T */
	public <T extends Throwable> void doCycleForInterval(final TimeCycleCallback<T> callback) throws T {
		
		final Calendar c1 = (Calendar) this.startDate.clone();
		c1.setFirstDayOfWeek(Calendar.MONDAY);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		final int STEP_START, STEP_UNIT, STEP_SIZE, TYPE_UNIT;
		final TimePeriodType STEP_PERIOD;

		switch (this.type) {
			case DAY : {
				TYPE_UNIT = Calendar.DAY_OF_MONTH;
				STEP_SIZE = 1;
				if (this.count == 1) {
					STEP_UNIT = Calendar.HOUR_OF_DAY;
					STEP_PERIOD = TimePeriodType.HOUR;
					STEP_START = 0;
				} else {
					STEP_UNIT = Calendar.DAY_OF_MONTH;
					STEP_PERIOD = TimePeriodType.DAY;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.HOUR_OF_DAY, 0);
				}
				break;
			}
			case WEEK : {
				TYPE_UNIT = Calendar.WEEK_OF_YEAR;
				if (this.count == 1) {
					STEP_SIZE = 1;
					STEP_UNIT = Calendar.DAY_OF_WEEK;
					STEP_PERIOD = TimePeriodType.DAY;
					STEP_START = Calendar.MONDAY;
					c1.set(Calendar.HOUR_OF_DAY, 0);
				} else {
					c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					STEP_SIZE = 7;
					// JAVA BUG WORKAROUND: 2008.12.29 - getWeekOfYear == 1 which is
					// totally wrong and when you
					// set the value that you just read you'll get a date in 2007.
					// Same shit will happen on any date that is in a week which
					// overlaps with two years
					STEP_UNIT = Calendar.DAY_OF_MONTH; // Calendar.WEEK_OF_YEAR;
					STEP_PERIOD = TimePeriodType.WEEK;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.HOUR_OF_DAY, 0);
				}
				break;
			}
			case MONTH : {
				TYPE_UNIT = Calendar.MONTH;
				STEP_SIZE = 1;
				if (this.count == 1) {
					STEP_UNIT = Calendar.DAY_OF_MONTH;
					STEP_PERIOD = TimePeriodType.DAY;
					STEP_START = 1;
					c1.set(Calendar.HOUR_OF_DAY, 0);
				} else {
					STEP_UNIT = Calendar.MONTH;
					STEP_PERIOD = TimePeriodType.MONTH;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.HOUR_OF_DAY, 0);
					c1.set(Calendar.DAY_OF_MONTH, 1);
				}
				break;
			}
			case YEAR : {
				TYPE_UNIT = Calendar.YEAR;
				STEP_SIZE = 1;
				if (this.count == 1) {
					STEP_UNIT = Calendar.MONTH;
					STEP_PERIOD = TimePeriodType.MONTH;
					STEP_START = 0;
					c1.set(Calendar.HOUR_OF_DAY, 0);
					c1.set(Calendar.DAY_OF_MONTH, 1);
				} else {
					STEP_UNIT = Calendar.YEAR;
					STEP_PERIOD = TimePeriodType.YEAR;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.HOUR_OF_DAY, 0);
					c1.set(Calendar.DAY_OF_MONTH, 1);
					c1.set(Calendar.MONTH, 0);
				}
				break;
			}
			default :
				return;
		}

		// this call required for calendar to recalculate it's internal
		// fields
		c1.getTimeInMillis();
		c1.set(STEP_UNIT, STEP_START);

		final long endMillis;
		{
			final Calendar c2 = (Calendar) c1.clone();
			c2.add(TYPE_UNIT, this.count);
			endMillis = c2.getTimeInMillis();
		}

		{
			for (;;) {
				final Calendar c2 = (Calendar) c1.clone();
				c1.add(STEP_UNIT, STEP_SIZE);
				if (c1.getTimeInMillis() > endMillis) {
					break;
				}
				c2.getTimeInMillis();
				callback.execute(c2, c1, STEP_PERIOD);
			}
		}
	}
	
	/** @return */
	public final Calendar getCurrentCalendar() {
		
		return TimeParameters.getCurrentCalendar(this.timezone);
	}
	
	/** @return */
	public final Date getCurrentDate() {
		
		return this.getCurrentCalendar().getTime();
	}
	
	/** @return */
	public int getCycleCountForInterval() {
		
		final Calendar c1 = (Calendar) this.startDate.clone();
		c1.setFirstDayOfWeek(Calendar.MONDAY);
		c1.set(Calendar.MINUTE, 0);
		c1.set(Calendar.SECOND, 0);
		c1.set(Calendar.MILLISECOND, 0);

		final int STEP_START;
		final int TYPE_UNIT;
		final int STEP_UNIT;

		switch (this.type) {
			case DAY : {
				TYPE_UNIT = Calendar.DAY_OF_MONTH;
				if (this.count == 1) {
					STEP_UNIT = Calendar.HOUR_OF_DAY;
					STEP_START = 0;
				} else {
					STEP_UNIT = Calendar.DAY_OF_MONTH;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.HOUR_OF_DAY, 0);
				}
				break;
			}
			case WEEK : {
				TYPE_UNIT = Calendar.WEEK_OF_YEAR;
				if (this.count == 1) {
					STEP_UNIT = Calendar.DAY_OF_WEEK;
					STEP_START = Calendar.MONDAY;
					c1.set(Calendar.HOUR_OF_DAY, 0);
				} else {
					STEP_UNIT = Calendar.WEEK_OF_YEAR;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
					c1.set(Calendar.HOUR_OF_DAY, 0);
				}
				break;
			}
			case MONTH : {
				TYPE_UNIT = Calendar.MONTH;
				if (this.count == 1) {
					STEP_UNIT = Calendar.DAY_OF_MONTH;
					STEP_START = 1;
					c1.set(Calendar.HOUR_OF_DAY, 0);
				} else {
					STEP_UNIT = Calendar.MONTH;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.HOUR_OF_DAY, 0);
					c1.set(Calendar.DAY_OF_MONTH, 1);
				}
				break;
			}
			case YEAR : {
				TYPE_UNIT = Calendar.YEAR;
				if (this.count == 1) {
					STEP_UNIT = Calendar.MONTH;
					STEP_START = 0;
					c1.set(Calendar.HOUR_OF_DAY, 0);
					c1.set(Calendar.DAY_OF_MONTH, 1);
				} else {
					STEP_UNIT = Calendar.YEAR;
					STEP_START = c1.get(STEP_UNIT);
					c1.set(Calendar.HOUR_OF_DAY, 0);
					c1.set(Calendar.DAY_OF_MONTH, 1);
					c1.set(Calendar.MONTH, 0);
				}
				break;
			}
			default :
				return 0;
		}

		// this call required for calendar to recalculate it's internal
		// fields
		c1.getTimeInMillis();
		c1.set(STEP_UNIT, STEP_START);

		final long endMillis;
		{
			final Calendar c2 = (Calendar) c1.clone();
			c2.add(TYPE_UNIT, this.count);
			endMillis = c2.getTimeInMillis();
		}

		int i = 0;
		{
			for (;;) {
				c1.add(STEP_UNIT, 1);
				if (c1.getTimeInMillis() > endMillis) {
					break;
				}
				i++;
			}
		}
		return i;
	}
	
	/** @param locale
	 * @return */
	public SimpleDateFormat getCycleLabelFormat(final Locale locale) {
		
		final SimpleDateFormat df;
		{
			final String format;
			switch (this.type) {
				case DAY :
					format = this.count == 1
						? "HH:mm"
						: "E dd MMM";
					break;
				case WEEK :
					format = this.count == 1
						? "E dd MMM"
						: "dd MMM";
					break;
				case MONTH :
					format = this.count == 1
						? "E dd"
						: "MMM yyyy";
					break;
				case YEAR :
					format = this.count == 1
						? "MMMM"
						: "yyyy";
					break;
				default :
					throw new IllegalArgumentException("Incorrect period type: " + this.type + "!");
			}
			df = new SimpleDateFormat(format, locale);
		}
		df.setTimeZone(this.startDate.getTimeZone());
		return df;
	}
	
	/** @return */
	public Calendar getEndDate() {
		
		return this.endDate;
	}
	
	/** @return */
	public long getEndDateMillis() {
		
		return this.endDate.getTimeInMillis();
	}
	
	/** @return */
	public Calendar getEndDateMinusOneMillisecond() {
		
		final Calendar clone = (Calendar) this.endDate.clone();
		clone.setTimeInMillis(clone.getTimeInMillis() - 1);
		// clone.add( Calendar.MILLISECOND, -1 );
		return clone;
	}
	
	/** @return */
	public Calendar getEndDateStart() {
		
		final Calendar clone = (Calendar) this.endDate.clone();
		this.type.shiftCalendar(clone, -1);
		return clone;
	}
	
	/** @param locale
	 * @return */
	public final String getIntervalAxisLabel(final Locale locale) {
		
		if (this.count == 1) {
			final SimpleDateFormat df;
			switch (this.type) {
				case DAY :
					df = new SimpleDateFormat("EEEE, dd MMMM yyyy", locale);
					break;
				case WEEK :
					df = new SimpleDateFormat("E, dd MMMM yyyy", locale);
					break;
				case MONTH :
					df = new SimpleDateFormat("MMMM yyyy", locale);
					break;
				case YEAR :
					df = new SimpleDateFormat("yyyy", locale);
					break;
				default :
					throw new IllegalArgumentException("Incorrect period type!");
			}
			df.setTimeZone(this.timezone);
			return df.format(this.startDate.getTime()) + " (" + this.timezone.getDisplayName() + ")";
		}
		if (this.type == TimePeriodType.WEEK) {
			this.getEndDate().set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		}
		final String f1;
		final String f2;
		{
			switch (this.type) {
				case DAY :
				case WEEK :
					if (this.getEndDate().get(Calendar.YEAR) == this.startDate.get(Calendar.YEAR)) {
						if (this.getEndDate().get(Calendar.MONTH) == this.startDate.get(Calendar.MONTH)) {
							f1 = "E, dd";
						} else {
							f1 = "E, dd MMMM";
						}
					} else {
						f1 = "E, dd MMMM yyyy";
					}
					f2 = "E, dd MMMM yyyy";
					break;
				case MONTH :
					if (this.getEndDate().get(Calendar.YEAR) == this.startDate.get(Calendar.YEAR)) {
						f1 = "MMMM";
					} else {
						f1 = "MMMM yyyy";
					}
					f2 = "MMMM yyyy";
					break;
				case YEAR :
					f1 = "yyyy";
					f2 = "yyyy";
					break;
				default :
					throw new IllegalArgumentException("Incorrect period type!");
			}
		}
		final SimpleDateFormat df1 = new SimpleDateFormat(f1, locale);
		final SimpleDateFormat df2 = new SimpleDateFormat(f2, locale);
		df1.setTimeZone(this.timezone);
		df2.setTimeZone(this.timezone);
		return df1.format(this.startDate.getTime()) + " \u2014 " + df2.format(this.getEndDateStart().getTime()) + " (" + this.timezone.getDisplayName() + ")";
	}
	
	/** @return */
	public TimeControlMode getMode() {
		
		return this.mode;
	}
	
	/** @return */
	public int getPeriodCount() {
		
		return this.count;
	}
	
	/** @return */
	public TimePeriodType getPeriodType() {
		
		return this.type;
	}
	
	/** @return */
	public Calendar getStartDate() {
		
		return this.startDate;
	}
	
	/** @return */
	public long getStartDateMillis() {
		
		return this.startDate.getTimeInMillis();
	}
	
	/** @return */
	public TimeZone getTimezone() {
		
		return this.timezone;
	}
	
	/** @return */
	public long getTotalMillis() {
		
		return this.getEndDateMillis() - this.getStartDateMillis();
	}
	
	/** @return */
	public boolean isBeforeNow() {
		
		return this.beforeNow;
	}
	
	/** @param type
	 * @return */
	public boolean isComplete(final TimePeriodType type) {
		
		if (this.mode != TimeControlMode.TIME_TREND) {
			return true;
		}
		final Calendar calendar = this.getCurrentCalendar();
		type.roundCalendar(calendar);
		type.shiftCalendar(calendar, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime().getTime() < System.currentTimeMillis();
	}
	
	/** @return */
	public boolean isDetail() {
		
		return this.detail;
	}
	
	/** @return */
	public boolean isPoint() {
		
		return this.startDate.getTimeInMillis() == this.endDate.getTimeInMillis();
	}
	
	/** @return */
	public boolean isTrend() {
		
		return !this.detail;
	}
	
	/** @param relative
	 * @return */
	public TimeParameters setBeforeNow(final boolean relative) {
		
		this.beforeNow = relative;
		return this;
	}
	
	@Override
	public String toString() {
		
		final DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return this.mode + "\t " + this.type + "\t x" + this.count + "\t " + format.format(this.getStartDate().getTime()) + " - " + format.format(this.getEndDate().getTime());
	}
}
