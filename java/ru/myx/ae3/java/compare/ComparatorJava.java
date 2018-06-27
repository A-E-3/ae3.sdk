/**
 * 
 */
package ru.myx.ae3.java.compare;

import java.util.Comparator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.help.Convert;

/**
 * These utilities follows Java conventions and will treat {a:5} equal to {a:5}
 * while JavaScript conventions will treat {a:5} not equal to {a:5}. See
 * ComparatorEcma for JavaScript conventions.
 * 
 * @author myx
 * 
 */
public final class ComparatorJava implements Comparator<Object> {
	
	
	/**
	 * 
	 */
	public static final ComparatorJava INSTANCE = new ComparatorJava();
	
	private static final Comparator<Object> COMPARE_NULL_TO_OBJECT = CompareNullToObject.INSTANCE;
	
	private static final Comparator<Object> COMPARE_INTEGER_TO_OBJECT = CompareIntegerToObject.INSTANCE;
	
	private static final Comparator<Object> COMPARE_LONG_TO_OBJECT = CompareLongToObject.INSTANCE;
	
	private static final Comparator<Object> COMPARE_DOUBLE_TO_OBJECT = CompareDoubleToObject.INSTANCE;
	
	private static final Comparator<Object> COMPARE_STRING_TO_OBJECT = CompareStringToObject.INSTANCE;
	
	private static final Comparator<Object> COMPARE_BOOLEAN_TO_OBJECT = CompareBooleanToObject.INSTANCE;
	
	private static final Comparator<Object> COMPARE_CHARACTER_TO_OBJECT = CompareCharacterToObject.INSTANCE;
	
	private static final Comparator<Object> COMPARE_OBJECT_TO_NULL = CompareObjectToNull.INSTANCE;
	
	private static final Comparator<Object> COMPARE_OBJECT_TO_INTEGER = CompareObjectToInteger.INSTANCE;
	
	private static final Comparator<Object> COMPARE_OBJECT_TO_LONG = CompareObjectToLong.INSTANCE;
	
	private static final Comparator<Object> COMPARE_OBJECT_TO_DOUBLE = CompareObjectToDouble.INSTANCE;
	
	private static final Comparator<Object> COMPARE_OBJECT_TO_STRING = CompareObjectToString.INSTANCE;
	
	private static final Comparator<Object> COMPARE_OBJECT_TO_BOOLEAN = CompareObjectToBoolean.INSTANCE;
	
	private static final Comparator<Object> COMPARE_OBJECT_TO_CHARACTER = CompareObjectToCharacter.INSTANCE;
	
	private static final byte R_leM = 1;
	
	private static final byte R_lEm = 2;
	
	private static final byte R_Lem = 4;
	
	// private static final byte R_lem = 0;
	
	private static final byte R_LeM = 5;
	
	private static final byte R_LEM = 7;
	
	// private static final byte R_lEM = 3;
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareEQU(final BaseObject o1, final BaseObject o2) {
		
		return ComparatorJava.compareImpl(o1, o2) == ComparatorJava.R_lEm;
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareEQU(final Object o1, final Object o2) {
		
		return ComparatorJava.compareImpl(o1, o2) == ComparatorJava.R_lEm;
	}
	
	// private static final byte R_LEm = 6;
	
	private static final byte compareImpl(final BaseObject o1, final BaseObject o2) {
		
		if (o1 == BasePrimitiveNumber.NAN || o2 == BasePrimitiveNumber.NAN) {
			return o1 == o2
				? ComparatorJava.R_LEM
				: ComparatorJava.R_LeM;
		}
		if (o1 == o2) {
			return ComparatorJava.R_lEm;
		}
		final boolean o1falseExact = o1 == null || o1 == BaseObject.UNDEFINED || o1 == BaseObject.NULL || o1 == BaseObject.FALSE;
		
		final boolean o2falseExact = o2 == null || o2 == BaseObject.UNDEFINED || o2 == BaseObject.NULL || o2 == BaseObject.FALSE;
		
		if (o1falseExact && o2falseExact) {
			return ComparatorJava.R_lEm;
		}
		
		if (o1falseExact) {
			return o2 != null && o2.baseToJavaBoolean()
				? ComparatorJava.R_Lem
				: ComparatorJava.R_lEm;
		}
		if (o2falseExact) {
			return o1 != null && o1.baseToJavaBoolean()
				? ComparatorJava.R_leM
				: ComparatorJava.R_lEm;
		}
		
		final boolean o1trueExact = o1 == BaseObject.TRUE;
		
		final boolean o2trueExact = o2 == BaseObject.TRUE;
		
		if (o1trueExact && o2trueExact) {
			return ComparatorJava.R_lEm;
		}
		
		if (o1trueExact) {
			return o2 != null && o2.baseToJavaBoolean()
				? ComparatorJava.R_lEm
				: ComparatorJava.R_leM;
		}
		if (o2trueExact) {
			return o1 != null && o1.baseToJavaBoolean()
				? ComparatorJava.R_lEm
				: ComparatorJava.R_Lem;
		}
		
		final boolean o1falseAlike = o1falseExact || o1 == BasePrimitiveNumber.ZERO || o1 == BaseString.EMPTY;
		final boolean o2falseAlike = o2falseExact || o2 == BasePrimitiveNumber.ZERO || o2 == BaseString.EMPTY;
		
		if (o1falseAlike && o2falseAlike) {
			return ComparatorJava.R_lEm;
		}
		assert o1 != null && o2 != null : "should not get there ^^^^^";
		final Class<?> c1 = o1.getClass();
		final Class<?> c2 = o2.getClass();
		// System.out
		// .println( ">>>>> cmp-r: " + c1.getName() + " vs. " + c2.getName() +
		// ", objects: " + o1 + " vs. " + o2 );
		if (c1 == c2 && o1.equals(o2)) {
			return ComparatorJava.R_lEm;
		}
		final Object baseO1 = ((Value<?>) o1).baseValue();
		if (o2 == baseO1) {
			return ComparatorJava.R_lEm;
		}
		final Object baseO2 = ((Value<?>) o2).baseValue();
		if (o1 == baseO2) {
			return ComparatorJava.R_lEm;
		}
		return ComparatorJava.compareImpl(baseO1, baseO2);
	}
	
	private static final byte compareImpl(final Object o1, final Object o2) {
		
		if (o1 == BasePrimitiveNumber.NAN || o2 == BasePrimitiveNumber.NAN) {
			return o1 == o2
				? ComparatorJava.R_LEM
				: ComparatorJava.R_LeM;
		}
		if (o1 == o2) {
			return ComparatorJava.R_lEm;
		}
		final boolean o1falseExact = o1 == null || o1 == BaseObject.UNDEFINED || o1 == BaseObject.NULL || o1 == Boolean.FALSE || o1 == BaseObject.FALSE;
		
		final boolean o2falseExact = o2 == null || o2 == BaseObject.UNDEFINED || o2 == BaseObject.NULL || o2 == Boolean.FALSE || o2 == BaseObject.FALSE;
		
		if (o1falseExact && o2falseExact) {
			return ComparatorJava.R_lEm;
		}
		
		if (o1falseExact) {
			return Ecma.ecmaToBoolean(o2)
				? ComparatorJava.R_Lem
				: ComparatorJava.R_lEm;
		}
		if (o2falseExact) {
			return Ecma.ecmaToBoolean(o1)
				? ComparatorJava.R_leM
				: ComparatorJava.R_lEm;
		}
		
		final boolean o1trueExact = o1 == Boolean.TRUE || o1 == BaseObject.TRUE;
		
		final boolean o2trueExact = o2 == Boolean.TRUE || o2 == BaseObject.TRUE;
		
		if (o1trueExact && o2trueExact) {
			return ComparatorJava.R_lEm;
		}
		
		if (o1trueExact) {
			return Ecma.ecmaToBoolean(o2)
				? ComparatorJava.R_lEm
				: ComparatorJava.R_leM;
		}
		if (o2trueExact) {
			return Ecma.ecmaToBoolean(o1)
				? ComparatorJava.R_lEm
				: ComparatorJava.R_Lem;
		}
		
		final boolean o1falseAlike = o1falseExact || o1 == BasePrimitiveNumber.ZERO || o1 == BaseString.EMPTY;
		final boolean o2falseAlike = o2falseExact || o2 == BasePrimitiveNumber.ZERO || o2 == BaseString.EMPTY;
		
		if (o1falseAlike && o2falseAlike) {
			return ComparatorJava.R_lEm;
		}
		assert o1 != null && o2 != null : "should not get there ^^^^^";
		final Class<?> c1 = o1.getClass();
		final Class<?> c2 = o2.getClass();
		// System.out
		// .println( ">>>>> cmp-r: " + c1.getName() + " vs. " + c2.getName() +
		// ", objects: " + o1 + " vs. " + o2 );
		if (c1 == Integer.class || c1 == int.class) {
			final int v1 = ((Integer) o1).intValue();
			/**
			 * falseExact trueExact are already done!
			 */
			//
			if (c2 == Integer.class || c2 == int.class) {
				final int value = ((Integer) o2).intValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Long.class || c2 == long.class) {
				final long value = ((Long) o2).longValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == String.class) {
				if (v1 == 0) {
					if (((String) o2).length() == 0) {
						return ComparatorJava.R_lEm;
					}
					if ("false".equals(o2)) {
						return ComparatorJava.R_Lem;
					}
				}
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1
						? ComparatorJava.R_lEm
						: v1 < 1
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM
					: v1 == 0
						? ComparatorJava.R_lEm
						: v1 < 0
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof BaseObject) {
				final double value = ((BaseObject) o2).baseToNumber().doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return ComparatorJava.compareImpl(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? ComparatorJava.R_lEm
				: v1 < value
					? ComparatorJava.R_Lem
					: ComparatorJava.R_leM;
		}
		if (c1 == Long.class || c1 == long.class) {
			final long v1 = ((Long) o1).longValue();
			/**
			 * falseExact trueExact are already done!
			 */
			//
			if (c2 == Long.class || c2 == long.class || c2 == Integer.class || c2 == int.class) {
				final long value = ((Number) o2).longValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == String.class) {
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1L
						? ComparatorJava.R_lEm
						: v1 < 1L
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM
					: v1 == 0L
						? ComparatorJava.R_lEm
						: v1 < 0L
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof BaseObject) {
				final double value = ((BaseObject) o2).baseToNumber().doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return ComparatorJava.compareImpl(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? ComparatorJava.R_lEm
				: v1 < value
					? ComparatorJava.R_Lem
					: ComparatorJava.R_leM;
		}
		if (c1 == String.class) {
			if (c2 == String.class) {
				final int result = ((String) o1).compareTo((String) o2);
				return result == 0
					? ComparatorJava.R_lEm
					: result > 0
						? ComparatorJava.R_leM
						: ComparatorJava.R_Lem;
			}
			if (c2 == Integer.class || c2 == int.class) {
				final int value = ((Integer) o2).intValue();
				if (value == 0 && o1falseAlike) {
					return ComparatorJava.R_lEm;
				}
				final double v1 = Base.forString((String) o1).baseToNumber().doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Long.class || c2 == long.class) {
				final long value = ((Long) o2).longValue();
				if (value == 0L && o1falseAlike) {
					return ComparatorJava.R_lEm;
				}
				final double v1 = Base.forString((String) o1).baseToNumber().doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				if (value == 0.0 && o1falseAlike) {
					return ComparatorJava.R_lEm;
				}
				final double v1 = Base.forString((String) o1).baseToNumber().doubleValue();
				if (value == 0.0 && Double.isNaN(v1)) {
					/**
					 * less than not a number string
					 */
					return ComparatorJava.R_Lem;
				}
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				final boolean value = ((Boolean) o2).booleanValue();
				if (!value && o1falseAlike) {
					return ComparatorJava.R_lEm;
				}
				final boolean v1 = Ecma.ecmaToBoolean(o1);
				return v1 == value
					? ComparatorJava.R_lEm
					: v1
						? ComparatorJava.R_leM
						: ComparatorJava.R_Lem;
			}
			if (c2 == Character.class || c2 == char.class) {
				final String s1 = (String) o1;
				if (s1.length() == 0) {
					return ComparatorJava.R_Lem;
				}
				final char value = ((Character) o2).charValue();
				final char v1 = s1.charAt(0);
				if (s1.length() == 1) {
					return v1 == value
						? ComparatorJava.R_lEm
						: v1 < value
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
				}
				if (v1 == value) {
					return ComparatorJava.R_leM;
				}
				if (v1 < value) {
					return ComparatorJava.R_Lem;
				}
				return ComparatorJava.R_leM;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return ComparatorJava.compareImpl(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double v1 = Base.forString((String) o1).baseToNumber().doubleValue();
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			final int result = ((String) o1).compareTo(o2.toString());
			return result == 0
				? ComparatorJava.R_lEm
				: result > 0
					? ComparatorJava.R_leM
					: ComparatorJava.R_Lem;
		}
		if (c1 == Double.class || c1 == double.class) {
			final double v1 = ((Double) o1).doubleValue();
			/**
			 * falseExact trueExact are already done!
			 */
			//
			if (c2 == Long.class || c2 == long.class || c2 == Integer.class || c2 == int.class) {
				final long value = ((Number) o2).longValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == String.class) {
				if (v1 == 0.0 && o2falseAlike) {
					return ComparatorJava.R_lEm;
				}
				final double value = Base.forString((String) o2).baseToNumber().doubleValue();
				if (v1 == 0.0 && Double.isNaN(value)) {
					/**
					 * less than not a number string
					 */
					return ComparatorJava.R_Lem;
				}
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? ComparatorJava.R_lEm
						: v1 < 1.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM
					: v1 == 0.0d
						? ComparatorJava.R_lEm
						: v1 < 0.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return ComparatorJava.compareImpl(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? ComparatorJava.R_lEm
				: v1 < value
					? ComparatorJava.R_Lem
					: ComparatorJava.R_leM;
		}
		if (c1 == Boolean.class || c1 == boolean.class) {
			final int v1 = ((Boolean) o1).booleanValue()
				? 1
				: 0;
			/**
			 * falseExact trueExact are already done!
			 */
			//
			if (c2 == Integer.class || c2 == int.class) {
				final int value = ((Integer) o2).intValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Long.class || c2 == long.class) {
				final long value = ((Long) o2).longValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == String.class) {
				final int value = Ecma.ecmaToBoolean(o2)
					? 1
					: 0;
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? ComparatorJava.R_lEm
						: v1 < 1.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM
					: v1 == 0.0d
						? ComparatorJava.R_lEm
						: v1 < 0.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return ComparatorJava.compareImpl(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? ComparatorJava.R_lEm
				: v1 < value
					? ComparatorJava.R_Lem
					: ComparatorJava.R_leM;
		}
		if (o1 instanceof Value<?>) {
			if (o2.getClass() == o1.getClass() && o1.equals(o2)) {
				return ComparatorJava.R_lEm;
			}
			final Object baseO1 = ((Value<?>) o1).baseValue();
			if (baseO1 != o1) {
				return ComparatorJava.compareImpl(baseO1, o2);
			}
		}
		if (o1 instanceof Number) {
			final double v1 = ((Number) o1).doubleValue();
			/**
			 * falseExact trueExact are already done!
			 */
			//
			if (c2 == Long.class || c2 == long.class || c2 == Integer.class || c2 == int.class) {
				final long value = ((Number) o2).longValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == String.class) {
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? ComparatorJava.R_lEm
						: v1 < 1.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM
					: v1 == 0.0d
						? ComparatorJava.R_lEm
						: v1 < 0.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return ComparatorJava.compareImpl(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? ComparatorJava.R_lEm
				: v1 < value
					? ComparatorJava.R_Lem
					: ComparatorJava.R_leM;
		}
		if (c1 == Character.class || c1 == char.class) {
			final char v1 = ((Character) o1).charValue();
			/**
			 * falseExact trueExact are already done!
			 */
			//
			if (c2 == Integer.class || c2 == int.class) {
				final int value = ((Integer) o2).intValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Long.class || c2 == long.class) {
				final long value = ((Long) o2).longValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == String.class) {
				final String s2 = (String) o2;
				if (s2.length() == 0) {
					return ComparatorJava.R_leM;
				}
				final char value = s2.charAt(0);
				if (s2.length() == 1) {
					return v1 == value
						? ComparatorJava.R_lEm
						: v1 < value
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
				}
				return v1 == value
					? ComparatorJava.R_Lem
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? ComparatorJava.R_lEm
						: v1 < 1.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM
					: v1 == 0.0d
						? ComparatorJava.R_lEm
						: v1 < 0.0d
							? ComparatorJava.R_Lem
							: ComparatorJava.R_leM;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return ComparatorJava.compareImpl(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? ComparatorJava.R_lEm
					: v1 < value
						? ComparatorJava.R_Lem
						: ComparatorJava.R_leM;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? ComparatorJava.R_lEm
				: v1 < value
					? ComparatorJava.R_Lem
					: ComparatorJava.R_leM;
		}
		if (c1 == c2 && o1 instanceof Comparable<?>) {
			@SuppressWarnings("unchecked")
			final Comparable<Object> a = (Comparable<Object>) o1;
			final int result = a.compareTo(o2);
			return result == 0
				? ComparatorJava.R_lEm
				: result > 0
					? ComparatorJava.R_leM
					: ComparatorJava.R_Lem;
		}
		return ComparatorJava.R_Lem;
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareLESS(final BaseObject o1, final BaseObject o2) {
		
		return ComparatorJava.compareImpl(o1, o2) == ComparatorJava.R_Lem;
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareLESS(final Object o1, final Object o2) {
		
		return ComparatorJava.compareImpl(o1, o2) == ComparatorJava.R_Lem;
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareMORE(final BaseObject o1, final BaseObject o2) {
		
		return ComparatorJava.compareImpl(o1, o2) == ComparatorJava.R_leM;
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareMORE(final Object o1, final Object o2) {
		
		return ComparatorJava.compareImpl(o1, o2) == ComparatorJava.R_leM;
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareNLESS(final BaseObject o1, final BaseObject o2) {
		
		switch (ComparatorJava.compareImpl(o1, o2)) {
			case R_lEm :
			case R_leM :
				return true;
			default :
				return false;
		}
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareNLESS(final Object o1, final Object o2) {
		
		switch (ComparatorJava.compareImpl(o1, o2)) {
			case R_lEm :
			case R_leM :
				return true;
			default :
				return false;
		}
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareNMORE(final BaseObject o1, final BaseObject o2) {
		
		switch (ComparatorJava.compareImpl(o1, o2)) {
			case R_lEm :
			case R_Lem :
				return true;
			default :
				return false;
		}
	}
	
	/**
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static final boolean compareNMORE(final Object o1, final Object o2) {
		
		switch (ComparatorJava.compareImpl(o1, o2)) {
			case R_lEm :
			case R_Lem :
				return true;
			default :
				return false;
		}
	}
	
	/**
	 * Gives best matching comparator for known argument type.
	 * 
	 * @param o
	 * @return comparator optimized for given first argument
	 */
	public static final Comparator<Object> ecmaComparatorForConstantArgumentA(final Object o) {
		
		if (o == null) {
			return ComparatorJava.COMPARE_NULL_TO_OBJECT;
		}
		final Class<?> cls = o.getClass();
		if (cls == Integer.class || cls == int.class) {
			return ComparatorJava.COMPARE_INTEGER_TO_OBJECT;
		}
		if (cls == Long.class || cls == long.class) {
			return ComparatorJava.COMPARE_LONG_TO_OBJECT;
		}
		if (cls == Double.class || cls == double.class) {
			return ComparatorJava.COMPARE_DOUBLE_TO_OBJECT;
		}
		if (cls == String.class) {
			return ComparatorJava.COMPARE_STRING_TO_OBJECT;
		}
		if (cls == Boolean.class || cls == boolean.class) {
			return ComparatorJava.COMPARE_BOOLEAN_TO_OBJECT;
		}
		if (cls == Character.class || cls == char.class) {
			return ComparatorJava.COMPARE_CHARACTER_TO_OBJECT;
		}
		if (o instanceof Number) {
			return ComparatorJava.COMPARE_DOUBLE_TO_OBJECT;
		}
		return ComparatorJava.INSTANCE;
	}
	
	/**
	 * Gives best matching comparator for known argument type.
	 * 
	 * @param o
	 * @return comparator optimized for given second argument
	 */
	public static final Comparator<Object> ecmaComparatorForConstantArgumentB(final Object o) {
		
		if (o == null) {
			return ComparatorJava.COMPARE_OBJECT_TO_NULL;
		}
		final Class<?> cls = o.getClass();
		if (cls == Integer.class || cls == int.class) {
			return ComparatorJava.COMPARE_OBJECT_TO_INTEGER;
		}
		if (cls == Long.class || cls == long.class) {
			return ComparatorJava.COMPARE_OBJECT_TO_LONG;
		}
		if (cls == Double.class || cls == double.class) {
			return ComparatorJava.COMPARE_OBJECT_TO_DOUBLE;
		}
		if (cls == String.class) {
			return ComparatorJava.COMPARE_OBJECT_TO_STRING;
		}
		if (cls == Boolean.class || cls == boolean.class) {
			return ComparatorJava.COMPARE_OBJECT_TO_BOOLEAN;
		}
		if (cls == Character.class || cls == char.class) {
			return ComparatorJava.COMPARE_OBJECT_TO_CHARACTER;
		}
		if (o instanceof Number) {
			return ComparatorJava.COMPARE_OBJECT_TO_DOUBLE;
		}
		return ComparatorJava.INSTANCE;
	}
	
	private ComparatorJava() {
		//
	}
	
	@Override
	public final int compare(final Object o1, final Object o2) {
		
		if (o1 == o2) {
			return 0;
		}
		if (o1 == null || o1 == BaseObject.UNDEFINED || o1 == BaseObject.NULL) {
			return o2 != null && o2 != BaseObject.UNDEFINED && o2 != BaseObject.NULL
				? -1
				: 0;
		}
		if (o2 == null || o2 == BaseObject.UNDEFINED || o2 == BaseObject.NULL) {
			return o1 != BaseObject.UNDEFINED && o1 != BaseObject.NULL
				? 1
				: 0;
		}
		final Class<?> c1 = o1.getClass();
		final Class<?> c2 = o2.getClass();
		if (c1 == Integer.class || c1 == int.class) {
			final int v1 = ((Integer) o1).intValue();
			if (c2 == Integer.class || c2 == int.class) {
				final int value = ((Integer) o2).intValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Long.class || c2 == long.class) {
				final long value = ((Long) o2).longValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == String.class) {
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1
						? 0
						: v1 < 1
							? -1
							: 1
					: v1 == 0
						? 0
						: v1 < 0
							? -1
							: 1;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof BaseObject) {
				final double value = ((BaseObject) o2).baseToNumber().doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return this.compare(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? 0
				: v1 < value
					? -1
					: 1;
		}
		if (c1 == Long.class || c1 == long.class) {
			final long v1 = ((Long) o1).longValue();
			if (c2 == Long.class || c2 == long.class || c2 == Integer.class || c2 == int.class) {
				final long value = ((Number) o2).longValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == String.class) {
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1L
						? 0
						: v1 < 1L
							? -1
							: 1
					: v1 == 0L
						? 0
						: v1 < 0L
							? -1
							: 1;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof BaseObject) {
				final double value = ((BaseObject) o2).baseToNumber().doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return this.compare(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? 0
				: v1 < value
					? -1
					: 1;
		}
		if (c1 == String.class) {
			if (c2 == String.class) {
				return ((String) o1).compareTo((String) o2);
			}
			if (c2 == Integer.class || c2 == int.class) {
				final double v1 = Convert.Any.toDouble(o1, Double.NaN);
				final int value = ((Integer) o2).intValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Long.class || c2 == long.class) {
				final double v1 = Convert.Any.toDouble(o1, Double.NaN);
				final long value = ((Long) o2).longValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double v1 = Convert.Any.toDouble(o1, Double.NaN);
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				final double v1 = Convert.Any.toDouble(o1, Double.NaN);
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
				final char v1 = s1.charAt(0);
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
					return this.compare(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double v1 = Convert.Any.toDouble(o1, Double.NaN);
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			return ((String) o1).compareTo(o2.toString());
		}
		if (c1 == Double.class || c1 == double.class) {
			final double v1 = ((Double) o1).doubleValue();
			if (c2 == Long.class || c2 == long.class || c2 == Integer.class || c2 == int.class) {
				final long value = ((Number) o2).longValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == String.class) {
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? 0
						: v1 < 1.0d
							? -1
							: 1
					: v1 == 0.0d
						? 0
						: v1 < 0.0d
							? -1
							: 1;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return this.compare(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? 0
				: v1 < value
					? -1
					: 1;
		}
		if (c1 == Boolean.class || c1 == boolean.class) {
			final int v1 = ((Boolean) o1).booleanValue()
				? 1
				: 0;
			if (c2 == Integer.class || c2 == int.class) {
				final int value = ((Integer) o2).intValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Long.class || c2 == long.class) {
				final long value = ((Long) o2).longValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == String.class) {
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? 0
						: v1 < 1.0d
							? -1
							: 1
					: v1 == 0.0d
						? 0
						: v1 < 0.0d
							? -1
							: 1;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return this.compare(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? 0
				: v1 < value
					? -1
					: 1;
		}
		if (o1 instanceof Value<?>) {
			if (o2.getClass() == o1.getClass() && o1.equals(o2)) {
				return 0;
			}
			final Object baseO1 = ((Value<?>) o1).baseValue();
			if (baseO1 != o1) {
				return this.compare(baseO1, o2);
			}
		}
		if (o1 instanceof Number) {
			final double v1 = ((Number) o1).doubleValue();
			if (v1 == 1.0d && (o2 == Boolean.TRUE || o2 == BaseObject.TRUE)) {
				return 0;
			}
			if (c2 == Long.class || c2 == long.class || c2 == Integer.class || c2 == int.class) {
				final long value = ((Number) o2).longValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == String.class) {
				final double value = Convert.Any.toDouble(o2, Double.NaN);
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? 0
						: v1 < 1.0d
							? -1
							: 1
					: v1 == 0.0d
						? 0
						: v1 < 0.0d
							? -1
							: 1;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return this.compare(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? 0
				: v1 < value
					? -1
					: 1;
		}
		if (c1 == Character.class || c1 == char.class) {
			final char v1 = ((Character) o1).charValue();
			if (c2 == Integer.class || c2 == int.class) {
				final int value = ((Integer) o2).intValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Long.class || c2 == long.class) {
				final long value = ((Long) o2).longValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Double.class || c2 == double.class) {
				final double value = ((Double) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == String.class) {
				final String s2 = (String) o2;
				if (s2.length() == 0) {
					return 1;
				}
				final char value = s2.charAt(0);
				if (s2.length() == 1) {
					return v1 == value
						? 0
						: v1 < value
							? -1
							: 1;
				}
				return v1 == value
					? -1
					: v1 < value
						? -1
						: 1;
			}
			if (c2 == Boolean.class || c2 == boolean.class) {
				return ((Boolean) o2).booleanValue()
					? v1 == 1.0d
						? 0
						: v1 < 1.0d
							? -1
							: 1
					: v1 == 0.0d
						? 0
						: v1 < 0.0d
							? -1
							: 1;
			}
			if (c2 == Character.class || c2 == char.class) {
				final char value = ((Character) o2).charValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			if (o2 instanceof Value<?>) {
				final Object baseO2 = ((Value<?>) o2).baseValue();
				if (baseO2 != o2) {
					return this.compare(o1, baseO2);
				}
			}
			if (o2 instanceof Number) {
				final double value = ((Number) o2).doubleValue();
				return v1 == value
					? 0
					: v1 < value
						? -1
						: 1;
			}
			final double value = Convert.Any.toDouble(o2, Double.NaN);
			return v1 == value
				? 0
				: v1 < value
					? -1
					: 1;
		}
		if (c1 == c2 && o1 instanceof Comparable<?>) {
			@SuppressWarnings("unchecked")
			final Comparable<Object> a = (Comparable<Object>) o1;
			return a.compareTo(o2);
		}
		return -1;
	}
}
