/**
 * 
 */
package ru.myx.ae3.ecma.compare;

import java.util.Comparator;

import ru.myx.ae3.common.Value;
import ru.myx.ae3.help.Convert;

/**
 * @author myx
 * 
 */
public final class CompareObjectToCharacter implements Comparator<Object> {
	
	/**
	 * 
	 */
	public static final CompareObjectToCharacter	INSTANCE	= new CompareObjectToCharacter();
	
	private CompareObjectToCharacter() {
		//
	}
	
	@Override
	public final int compare(final Object o2, final Object o1) {
		if (o1 == o2) {
			return 0;
		}
		if (o2 == null) {
			return -1;
		}
		final char v1 = ((Character) o1).charValue();
		if (v1 == 1 && o2 == Boolean.TRUE) {
			return 0;
		}
		if (v1 == 0 && o2 == Boolean.FALSE) {
			return 0;
		}
		final Class<?> c2 = o2.getClass();
		if (c2 == Integer.class || c2 == int.class) {
			final int value = ((Integer) o2).intValue();
			return v1 == value
					? 0
					: v1 < value
							? 1
							: -1;
		}
		if (c2 == Long.class || c2 == long.class) {
			final long value = ((Long) o2).longValue();
			return v1 == value
					? 0
					: v1 < value
							? 1
							: -1;
		}
		if (c2 == Double.class || c2 == double.class) {
			final double value = ((Double) o2).doubleValue();
			return v1 == value
					? 0
					: v1 < value
							? 1
							: -1;
		}
		if (c2 == String.class) {
			final String s2 = (String) o2;
			if (s2.length() == 0) {
				return -1;
			}
			final char value = s2.charAt( 0 );
			if (s2.length() == 1) {
				return v1 == value
						? 0
						: v1 < value
								? 1
								: -1;
			}
			return v1 == value
					? 1
					: v1 < value
							? 1
							: -1;
		}
		if (c2 == Boolean.class || c2 == boolean.class) {
			return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
							? 0
							: v1 < 1.0d
									? 1
									: -1
					: v1 == 0.0d
							? 0
							: v1 < 0.0d
									? 1
									: -1;
		}
		if (c2 == Character.class || c2 == char.class) {
			final char value = ((Character) o2).charValue();
			return v1 == value
					? 0
					: v1 < value
							? 1
							: -1;
		}
		if (o2 instanceof Value<?>) {
			final Object baseO2 = ((Value<?>) o2).baseValue();
			if (baseO2 != o2) {
				return this.compare( o1, baseO2 );
			}
		}
		if (o2 instanceof Number) {
			final double value = ((Number) o2).doubleValue();
			return v1 == value
					? 0
					: v1 < value
							? 1
							: -1;
		}
		final double value = Convert.Any.toDouble( o2, Double.NaN );
		return v1 == value
				? 0
				: v1 < value
						? 1
						: -1;
	}
	
}
