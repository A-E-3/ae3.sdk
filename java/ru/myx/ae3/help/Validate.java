/**
 * Created on 11.11.2002
 * 
 * myx - barachta */
package ru.myx.ae3.help;

import java.io.IOException;
import java.util.Iterator;
import java.util.zip.DeflaterOutputStream;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.java.compare.ComparatorJava;
import ru.myx.io.OutputStreamCounter;

/**
 * @author barachta
 * 
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class Validate {
	/**
	 * @author myx
	 * 
	 */
	public static class Analyse extends Validate {
		/**
		 * 
		 */
		public static final int	ENTROPY_MAX	= 100;
		
		/**
		 * Returns a value between 0 and 100.
		 * 
		 * @param array
		 * @return int
		 */
		public static final int entropy(final byte[] array) {
			final OutputStreamCounter counter = new OutputStreamCounter();
			try (final DeflaterOutputStream zos = new DeflaterOutputStream( counter )) {
				for (int i = array.length - 1; i >= 0; --i) {
					zos.write( array[i] & 0xFF );
				}
			} catch (final IOException e) {
				e.printStackTrace();
				return -1;
			}
			final int result = (int) (100.0 * counter.getTotal() / array.length);
			return result > 100
					? 100
					: result;
		}
		
		Analyse() {
			// empty
		}
	}
	
	/**
	 * @author myx
	 * 
	 */
	public static class Fit extends Validate {
		/**
		 * Fits given byte value to bounds specified. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? min : val &gt; max ? max : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return byte
		 */
		public static final byte value(final byte val, final byte min, final byte max) {
			return val < min
					? min
					: val > max
							? max
							: val;
		}
		
		/**
		 * Checks given byte value to fit in bounds specified returning value or
		 * default. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? def : val &gt; max ? def : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return byte
		 */
		public static final byte value(final byte val, final byte min, final byte max, final byte def) {
			return val < min
					? def
					: val > max
							? def
							: val;
		}
		
		/**
		 * Fits given char value to bounds specified. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? min : val &gt; max ? max : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return char
		 */
		public static final char value(final char val, final char min, final char max) {
			return val < min
					? min
					: val > max
							? max
							: val;
		}
		
		/**
		 * Checks given short value to fit in bounds specified returning value
		 * or default. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? def : val &gt; max ? def : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return char
		 */
		public static final char value(final char val, final char min, final char max, final char def) {
			return val < min
					? def
					: val > max
							? def
							: val;
		}
		
		/**
		 * Fits given double value to bounds specified. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? min : val &gt; max ? max : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return double
		 */
		public static final double value(final double val, final double min, final double max) {
			return val < min
					? min
					: val > max
							? max
							: val;
		}
		
		/**
		 * Checks given double value to fit in bounds specified returning value
		 * or default. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? def : val &gt; max ? def : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return double
		 */
		public static final double value(final double val, final double min, final double max, final double def) {
			return val < min
					? def
					: val > max
							? def
							: val;
		}
		
		/**
		 * Fits given float value to bounds specified. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? min : val &gt; max ? max : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return float
		 */
		public static final float value(final float val, final float min, final float max) {
			return val < min
					? min
					: val > max
							? max
							: val;
		}
		
		/**
		 * Checks given float value to fit in bounds specified returning value
		 * or default. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? def : val &gt; max ? def : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return float
		 */
		public static final float value(final float val, final float min, final float max, final float def) {
			return val < min
					? def
					: val > max
							? def
							: val;
		}
		
		/**
		 * Fits given integer value to bounds specified. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? min : val &gt; max ? max : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return int
		 */
		public static final int value(final int val, final int min, final int max) {
			return val < min
					? min
					: val > max
							? max
							: val;
		}
		
		/**
		 * Checks integer short value to fit in bounds specified returning value
		 * or default. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? def : val &gt; max ? def : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return int
		 */
		public static final int value(final int val, final int min, final int max, final int def) {
			return val < min
					? def
					: val > max
							? def
							: val;
		}
		
		/**
		 * Fits given long value to bounds specified. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? min : val &gt; max ? max : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return long
		 */
		public static final long value(final long val, final long min, final long max) {
			return val < min
					? min
					: val > max
							? max
							: val;
		}
		
		/**
		 * Checks given long value to fit in bounds specified returning value or
		 * default. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? def : val &gt; max ? def : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return long
		 */
		public static final long value(final long val, final long min, final long max, final long def) {
			return val < min
					? def
					: val > max
							? def
							: val;
		}
		
		/**
		 * Fits given short value to bounds specified. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? min : val &gt; max ? max : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return short
		 */
		public static final short value(final short val, final short min, final short max) {
			return val < min
					? min
					: val > max
							? max
							: val;
		}
		
		/**
		 * Checks given short value to fit in bounds specified returning value
		 * or default. <br>
		 * Seems that overhead of calling this method is equal to executing its
		 * body "return val &lt; min ? def : val &gt; max ? def : val;".
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations of your
		 * newly calculated values.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return short
		 */
		public static final short value(final short val, final short min, final short max, final short def) {
			return val < min
					? def
					: val > max
							? def
							: val;
		}
		
		/**
		 * Ensures that every entry of given array of byte values fits in bounds
		 * specified. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final byte current = val[i];
		 * 		if(current &lt; min) { val[i] = min; continue; }
		 * 		if(current &gt; max) val[i] = max;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return byte array
		 */
		public static final byte[] values(final byte[] val, final byte min, final byte max) {
			for (int i = val.length - 1; i >= 0; --i) {
				final byte current = val[i];
				if (current < min) {
					val[i] = min;
					continue;
				}
				if (current > max) {
					val[i] = max;
				}
			}
			return val;
		}
		
		/**
		 * Checks every entry of given array of byte values to fit in bounds
		 * specified padding with default value. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final byte current = val[i];
		 * 		if(current &lt; min || current &gt; max) val[i] = def;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return byte array
		 */
		public static final byte[] values(final byte[] val, final byte min, final byte max, final byte def) {
			for (int i = val.length - 1; i >= 0; --i) {
				final byte current = val[i];
				if (current < min || current > max) {
					val[i] = def;
				}
			}
			return val;
		}
		
		/**
		 * Ensures that every entry of given array of char values fits in bounds
		 * specified. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final char current = val[i];
		 * 		if(current &lt; min) { val[i] = min; continue; }
		 * 		if(current &gt; max) val[i] = max;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return char array
		 */
		public static final char[] values(final char[] val, final char min, final char max) {
			for (int i = val.length - 1; i >= 0; --i) {
				final char current = val[i];
				if (current < min) {
					val[i] = min;
					continue;
				}
				if (current > max) {
					val[i] = max;
				}
			}
			return val;
		}
		
		/**
		 * Checks every entry of given array of char values to fit in bounds
		 * specified padding with default value. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final char current = val[i];
		 * 		if(current &lt; min || current &gt; max) val[i] = def;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return char array
		 */
		public static final char[] values(final char[] val, final char min, final char max, final char def) {
			for (int i = val.length - 1; i >= 0; --i) {
				final char current = val[i];
				if (current < min || current > max) {
					val[i] = def;
				}
			}
			return val;
		}
		
		/**
		 * Ensures that every entry of given array of double values fits in
		 * bounds specified. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final double current = val[i];
		 * 		if(current &lt; min) { val[i] = min; continue; }
		 * 		if(current &gt; max) val[i] = max;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return double array
		 */
		public static final double[] values(final double[] val, final double min, final double max) {
			for (int i = val.length - 1; i >= 0; --i) {
				final double current = val[i];
				if (current < min) {
					val[i] = min;
					continue;
				}
				if (current > max) {
					val[i] = max;
				}
			}
			return val;
		}
		
		/**
		 * Checks every entry of given array of double values to fit in bounds
		 * specified padding with default value. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final double current = val[i];
		 * 		if(current &lt; min || current &gt; max) val[i] = def;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return double array
		 */
		public static final double[] values(final double[] val, final double min, final double max, final double def) {
			for (int i = val.length - 1; i >= 0; --i) {
				final double current = val[i];
				if (current < min || current > max) {
					val[i] = def;
				}
			}
			return val;
		}
		
		/**
		 * Ensures that every entry of given array of float values fits in
		 * bounds specified. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final float current = val[i];
		 * 		if(current &lt; min) { val[i] = min; continue; }
		 * 		if(current &gt; max) val[i] = max;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return float array
		 */
		public static final float[] values(final float[] val, final float min, final float max) {
			for (int i = val.length - 1; i >= 0; --i) {
				final float current = val[i];
				if (current < min) {
					val[i] = min;
					continue;
				}
				if (current > max) {
					val[i] = max;
				}
			}
			return val;
		}
		
		/**
		 * Checks every entry of given array of float values to fit in bounds
		 * specified padding with default value. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final float current = val[i];
		 * 		if(current &lt; min || current &gt; max) val[i] = def;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return float array
		 */
		public static final float[] values(final float[] val, final float min, final float max, final float def) {
			for (int i = val.length - 1; i >= 0; --i) {
				final float current = val[i];
				if (current < min || current > max) {
					val[i] = def;
				}
			}
			return val;
		}
		
		/**
		 * Ensures that every entry of given array of int values fits in bounds
		 * specified. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final int current = val[i];
		 * 		if(current &lt; min) { val[i] = min; continue; }
		 * 		if(current &gt; max) val[i] = max;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return int array
		 */
		public static final int[] values(final int[] val, final int min, final int max) {
			for (int i = val.length - 1; i >= 0; --i) {
				final int current = val[i];
				if (current < min) {
					val[i] = min;
					continue;
				}
				if (current > max) {
					val[i] = max;
				}
			}
			return val;
		}
		
		/**
		 * Checks every entry of given array of int values to fit in bounds
		 * specified padding with default value. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final int current = val[i];
		 * 		if(current &lt; min || current &gt; max) val[i] = def;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return int array
		 */
		public static final int[] values(final int[] val, final int min, final int max, final int def) {
			for (int i = val.length - 1; i >= 0; --i) {
				final int current = val[i];
				if (current < min || current > max) {
					val[i] = def;
				}
			}
			return val;
		}
		
		/**
		 * Ensures that every entry of given array of long values fits in bounds
		 * specified. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final long current = val[i];
		 * 		if(current &lt; min) { val[i] = min; continue; }
		 * 		if(current &gt; max) val[i] = max;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return long array
		 */
		public static final long[] values(final long[] val, final long min, final long max) {
			for (int i = val.length - 1; i >= 0; --i) {
				final long current = val[i];
				if (current < min) {
					val[i] = min;
					continue;
				}
				if (current > max) {
					val[i] = max;
				}
			}
			return val;
		}
		
		/**
		 * Checks every entry of given array of long values to fit in bounds
		 * specified padding with default value. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final long current = val[i];
		 * 		if(current &lt; min || current &gt; max) val[i] = def;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return long array
		 */
		public static final long[] values(final long[] val, final long min, final long max, final long def) {
			for (int i = val.length - 1; i >= 0; --i) {
				final long current = val[i];
				if (current < min || current > max) {
					val[i] = def;
				}
			}
			return val;
		}
		
		/**
		 * Ensures that every entry of given array of short values fits in
		 * bounds specified. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final short current = val[i];
		 * 		if(current &lt; min) { val[i] = min; continue; }
		 * 		if(current &gt; max) val[i] = max;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @return short array
		 */
		public static final short[] values(final short[] val, final short min, final short max) {
			for (int i = val.length - 1; i >= 0; --i) {
				final short current = val[i];
				if (current < min) {
					val[i] = min;
					continue;
				}
				if (current > max) {
					val[i] = max;
				}
			}
			return val;
		}
		
		/**
		 * Checks every entry of given array of short values to fit in bounds
		 * specified padding with default value. <br>
		 * Source code: <code>
		 * 	for(int i=val.length-1;i>=0;i--)
		 * 	{
		 * 		final short current = val[i];
		 * 		if(current &lt; min || current &gt; max) val[i] = def;
		 * 	}
		 * 	return val;
		 * </code>
		 * <p>
		 * So it seems to be useful just in case when you have not enough space
		 * on your screen to encode temporary variable declarations and a couple
		 * lines of code.
		 * <p>
		 * Written with respect to JIT inlining making this method as fast as
		 * possible.
		 * 
		 * @param val
		 * @param min
		 * @param max
		 * @param def
		 * @return short array
		 */
		public static final short[] values(final short[] val, final short min, final short max, final short def) {
			for (int i = val.length - 1; i >= 0; --i) {
				final short current = val[i];
				if (current < min || current > max) {
					val[i] = def;
				}
			}
			return val;
		}
		
		Fit() {
			// empty
		}
	}
	
	/**
	 * @param map1
	 * @param map2
	 * @return
	 */
	public static final boolean mapsEqual(final BaseObject map1, final BaseObject map2) {
		if (map1 != map2) {
			for (final Iterator<String> iterator = Base.keys( map1 ); iterator.hasNext();) {
				final String key = iterator.next();
				final BaseObject original = map1.baseGet( key, BaseObject.UNDEFINED );
				assert original != null : "NULL java value";
				final BaseObject effective = map2.baseGet( key, BaseObject.UNDEFINED );
				assert effective != null : "NULL java value";
				if (original != effective
						&& !original.equals( effective )
						&& !ComparatorJava.compareEQU( original, effective ) //
						/**
						 * FIXME: normally this would not be needed, but
						 * something is broken there, for Value<?> on left.
						 */
						&& !ComparatorJava.compareEQU( effective, original ) //
				) {
					return false;
				}
			}
			for (final Iterator<String> iterator = Base.keys( map2 ); iterator.hasNext();) {
				final String key = iterator.next();
				final BaseObject original = map1.baseGet( key, BaseObject.UNDEFINED );
				assert original != null : "NULL java value";
				final BaseObject effective = map2.baseGet( key, BaseObject.UNDEFINED );
				assert effective != null : "NULL java value";
				if (original != effective
						&& !original.equals( effective )
						&& !ComparatorJava.compareEQU( original, effective ) //
						/**
						 * FIXME: normally this would not be needed, but
						 * something is broken there, for Value<?> on left.
						 */
						&& !ComparatorJava.compareEQU( effective, original ) //
				) {
					return false;
				}
			}
		}
		return true;
	}
	
	Validate() {
		// empty
	}
}
