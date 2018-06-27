/**
 * 
 */
package ru.myx.ae3.java.compare;

import java.util.Comparator;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.help.Convert;

/**
 * @author myx
 * 
 */
public final class CompareStringToObject implements Comparator<Object> {
	
	/**
	 * 
	 */
	public static final CompareStringToObject	INSTANCE	= new CompareStringToObject();
	
	private CompareStringToObject() {
		//
	}
	
	@Override
	public final int compare(final Object o1, final Object o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o2 == null) {
			return 1;
		}
		final Class<?> c2 = o2.getClass();
		if (c2 == String.class) {
			return ((String) o1).compareTo( (String) o2 );
		}
		if (c2 == Integer.class || c2 == int.class) {
			final double v1 = Convert.Any.toDouble( o1, Double.NaN );
			final int value = ((Integer) o2).intValue();
			return v1 == value
					? 0
					: v1 < value
							? -1
							: 1;
		}
		if (c2 == Long.class || c2 == long.class) {
			final double v1 = Convert.Any.toDouble( o1, Double.NaN );
			final long value = ((Long) o2).longValue();
			return v1 == value
					? 0
					: v1 < value
							? -1
							: 1;
		}
		if (c2 == Double.class || c2 == double.class) {
			final double v1 = Convert.Any.toDouble( o1, Double.NaN );
			final double value = ((Double) o2).doubleValue();
			return v1 == value
					? 0
					: v1 < value
							? -1
							: 1;
		}
		if (c2 == Boolean.class || c2 == boolean.class) {
			final double v1 = Convert.Any.toDouble( o1, Double.NaN );
			final int value = ((Boolean) o2).booleanValue()
					? 1
					: 0;
			return v1 == value
					? 0
					: v1 < value
							? -1
							: 1;
		}
		if (c2 == Character.class || c2 == char.class) {
			final String s1 = (String) o1;
			if (s1.length() == 0) {
				return -1;
			}
			final char value = ((Character) o2).charValue();
			final char v1 = s1.charAt( 0 );
			if (s1.length() == 1) {
				return v1 == value
						? 0
						: v1 < value
								? -1
								: 1;
			}
			if (v1 == value) {
				return 1;
			}
			if (v1 < value) {
				return -1;
			}
			return 1;
		}
		if (o2 instanceof Value<?>) {
			final Object baseO2 = ((Value<?>) o2).baseValue();
			if (baseO2 != o2) {
				return this.compare( o1, baseO2 );
			}
		}
		if (o2 instanceof Number) {
			final double v1 = Convert.Any.toDouble( o1, Double.NaN );
			final double value = ((Number) o2).doubleValue();
			return v1 == value
					? 0
					: v1 < value
							? -1
							: 1;
		}
		return ((String) o1).compareTo( o2.toString() );
	}
	
}
