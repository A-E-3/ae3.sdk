/**
 * Created on 10.11.2002
 * 
 * myx - barachta */
package ru.myx.ae3.help;

/**
 * @author barachta
 * 
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
public class Search {
	/**
	 * @author myx
	 * 
	 */
	public static class Reflection extends Search {
		/**
		 * @param c1
		 * @param c2
		 * @return common superclass
		 */
		public static final Class<?> commonSuperclass(final Class<?> c1, final Class<?> c2) {
			final Class<?>[] list = new Class[3];
			int listSize = 0;
			if (c1 == c2) {
				return c1;
			}
			{
				final Class<?> common = Reflection.internSuperclass( c1, c2 );
				if (common != null) {
					return common;
				}
			}
			{
				final Class<?> parent = c1.getSuperclass();
				if (parent != null) {
					final Class<?> common = Reflection.internSuperclass( parent, c2 );
					if (common != null) {
						list[listSize++] = common;
					}
				}
			}
			{
				final Class<?> parent = c2.getSuperclass();
				if (parent != null) {
					final Class<?> common = Reflection.internSuperclass( c1, parent );
					if (common != null) {
						list[listSize++] = common;
					}
				}
			}
			{
				final Class<?> common = Reflection.internSuperclass( c2.getInterfaces(), c1 );
				if (common != null) {
					list[listSize++] = common;
				}
			}
			if (listSize == 3) {
				final boolean ia1 = list[1].isAssignableFrom( list[2] );
				final boolean ia2 = list[2].isAssignableFrom( list[1] );
				if (ia1 || ia2) {
					list[1] = ia1
							? list[2]
							: list[1];
				}
			}
			if (listSize > 1) {
				final boolean ia1 = list[0].isAssignableFrom( list[1] );
				final boolean ia2 = list[1].isAssignableFrom( list[0] );
				if (ia1 || ia2) {
					return ia1
							? list[1]
							: list[0];
				}
			}
			return null;
		}
		
		/**
		 * @param classes
		 * @return common superclass
		 */
		public static final Class<?> commonSuperclass(final Class<?>[] classes) {
			Class<?> result = classes[0];
			for (int i = classes.length - 1; i >= 1; --i) {
				result = Reflection.commonSuperclass( result, classes[i] );
			}
			return result;
		}
		
		private static final Class<?> internSuperclass(final Class<?> c1, final Class<?> c2) {
			return c1.isAssignableFrom( c2 )
					? c1
					: c2.isAssignableFrom( c1 )
							? c2
							: null;
		}
		
		private static final Class<?> internSuperclass(final Class<?>[] classes, final Class<?> cls) {
			{
				for (int i = classes.length - 1; i >= 0; --i) {
					final Class<?> current = classes[i];
					if (current.isAssignableFrom( cls )) {
						return current;
					}
				}
			}
			{
				final Class<?>[] other = cls.getInterfaces();
				if (other == null || other.length == 0) {
					return null;
				}
				for (final Class<?> current : classes) {
					final Class<?> common = Reflection.internSuperclass( other, current );
					if (common != null) {
						return common;
					}
				}
			}
			return null;
		}
		
		private Reflection() {
			// empty
		}
	}
	
	Search() {
		// empty
	}
}
