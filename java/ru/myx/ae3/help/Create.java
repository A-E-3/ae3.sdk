/**
 * Created on 10.11.2002
 * 
 */
package ru.myx.ae3.help;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.java.compare.ComparatorJava;

/**
 * @author myx
 * 		
 * 		
 */
public class Create {
	
	/**
	 * This class allows static access to some parameters whose values are
	 * defaults or explicitly specified by a user and should be considered if
	 * possible.
	 */
	public static final class LOCAL {
		
		private static final int TABLE_SMALL_MIN = 8;
		
		private static final int TABLE_SMALL_MAX = 512;
		
		/**
		 * Default small table size. This number guaranteed to be the power of
		 * 2.<br>
		 * Source: 2 ^ 'ae2.tune.table_small_factor' <br>
		 * Default: 32 <br>
		 * Min: 8 <br>
		 * Max: 512
		 */
		public static final int TABLE_SMALL = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.table_small_factor"), Engine.MODE_SIZE
			? 3
			: 5), LOCAL.TABLE_SMALL_MIN, LOCAL.TABLE_SMALL_MAX, 32);
			
		private static final int TABLE_MEDIUM_MIN = 16;
		
		private static final int TABLE_MEDIUM_MAX = 2048;
		
		/**
		 * Default medium table size. This number guaranteed to be the power of
		 * 2.<br>
		 * Source: 2 ^ 'ae2.tune.table_medium_factor' <br>
		 * Default: 256 <br>
		 * Min: 16 <br>
		 * Max: 2048
		 */
		public static final int TABLE_MEDIUM = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.table_medium_factor"), Engine.MODE_SIZE
			? 4
			: 8), LOCAL.TABLE_MEDIUM_MIN, LOCAL.TABLE_MEDIUM_MAX, 256);
			
		private static final int TABLE_LARGE_MIN = 32;
		
		private static final int TABLE_LARGE_MAX = 8192;
		
		/**
		 * Default large table size. This number guaranteed to be the power of
		 * 2.<br>
		 * Source: 2 ^ 'ae2.tune.table_large_factor' <br>
		 * Default: 2048 <br>
		 * Min: 32 <br>
		 * Max: 8192
		 */
		public static final int TABLE_LARGE = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.table_large_factor"), Engine.MODE_SIZE
			? 5
			: 11), LOCAL.TABLE_LARGE_MIN, LOCAL.TABLE_LARGE_MAX, 1024);
			
		private static final int TABLE_MAX_MIN = 8192;
		
		private static final int TABLE_MAX_MAX = 65536;
		
		/**
		 * Default maximal table size for validation use. This number guaranteed
		 * to be the power of 2.<br>
		 * Source: 2 ^ 'ae2.tune.table_max_factor' <br>
		 * Default: 32768 <br>
		 * Min: 8192 <br>
		 * Max: 65536
		 */
		public static final int TABLE_MAX = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.table_max_factor"), Engine.MODE_SIZE
			? 13
			: 15), LOCAL.TABLE_MAX_MIN, LOCAL.TABLE_MAX_MAX, 32768);
			
		private static final int LIST_SMALL_MIN = 1;
		
		private static final int LIST_SMALL_MAX = 64;
		
		/**
		 * Default small list initial size. This number guaranteed to be the
		 * power of 2.<br>
		 * Source: 2 ^ 'ae2.tune.list_small_factor' <br>
		 * Default: 2 <br>
		 * Min: 1 <br>
		 * Max: 64
		 */
		public static final int LIST_SMALL = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.list_small_factor"), Engine.MODE_SIZE
			? 0
			: 1), LOCAL.LIST_SMALL_MIN, LOCAL.LIST_SMALL_MAX, 2);
			
		private static final int LIST_MEDIUM_MIN = 2;
		
		private static final int LIST_MEDIUM_MAX = 512;
		
		/**
		 * Default medium list initial size. This number guaranteed to be the
		 * power of 2.<br>
		 * Source: 2 ^ 'ae2.tune.list_medium_factor' <br>
		 * Default: 16 <br>
		 * Min: 2 <br>
		 * Max: 512
		 */
		public static final int LIST_MEDIUM = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.list_medium_factor"), Engine.MODE_SIZE
			? 1
			: 3), LOCAL.LIST_MEDIUM_MIN, LOCAL.LIST_MEDIUM_MAX, 8);
			
		private static final int LIST_LARGE_MIN = 32;
		
		private static final int LIST_LARGE_MAX = 2048;
		
		/**
		 * Default large list initial size. This number guaranteed to be the
		 * power of 2.<br>
		 * Source: 2 ^ 'ae2.tune.list_large_factor' <br>
		 * Default: 256 <br>
		 * Min: 32 <br>
		 * Max: 2048
		 */
		public static final int LIST_LARGE = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.list_large_factor"), Engine.MODE_SIZE
			? 5
			: 8), LOCAL.LIST_LARGE_MIN, LOCAL.LIST_LARGE_MAX, 256);
			
		private static final int LIST_MAX_MIN = 2048;
		
		private static final int LIST_MAX_MAX = 65536;
		
		/**
		 * Default maximal table size for validation use. This number guaranteed
		 * to be the power of 2.<br>
		 * Source: 2 ^ 'ae2.tune.table_max_factor' <br>
		 * Default: 4096 <br>
		 * Min: 2048 <br>
		 * Max: 65536
		 */
		public static final int LIST_MAX = Validate.Fit.value(1 << Convert.Any.toInt(System.getProperty("ae2.tune.list_max_factor"), Engine.MODE_SIZE
			? 11
			: 14), LOCAL.LIST_MAX_MIN, LOCAL.LIST_MAX_MAX, 4096);
			
		private LOCAL() {
			// ignore
		}
	}
	
	/**
	 * 
	 * @param init
	 * @param <T>
	 * @return Unsynchronized list for per-thread/up-synchronized longlife
	 *         storage.
	 */
	public static final <T> List<T> privateList(final int init) {
		
		return new ArrayList<>(init * 2);
	}
	
	/**
	 * Unsynchronized map for per-thread/up-synchronized longlife storage. This
	 * map doesn't guarantee concurrent write access, but it is optimized for
	 * fast reads and writes.
	 * 
	 * Implementation may loose some creation/initialization speed in order to
	 * make best effort of fast reads and writes.
	 * 
	 * @param init
	 * @param <K>
	 * @param <V>
	 * @return map
	 */
	public static final <K, V> Map<K, V> privateMap(final int init) {
		
		return new HashMap<>(Validate.Fit.value(init, 16, 256, 32));
	}
	
	/**
	 * Unsynchronized set for per-thread/up-synchronized longlife storage.
	 * 
	 * @param init
	 * @param <T>
	 * @return set
	 */
	public static final <T> Set<T> privateSet(final int init) {
		
		return new HashSet<>(Validate.Fit.value(init, 16, 512, 32));
	}
	
	/**
	 * Unsynchronized map for per-thread/local shortlife processing. This map
	 * doesn't guarantee concurrent write access, but it is optimized for fast
	 * creation.
	 * 
	 * Implementation may loose some read/write speed on large amounts of data
	 * in order to make best effort of fast creation.
	 * 
	 * @param <K>
	 * @param <V>
	 * @return map
	 */
	public static final <K, V> Map<K, V> tempMap() {
		
		return new TreeMap<>(ComparatorJava.INSTANCE);
	}
	
	/**
	 * Unsynchronized map for per-thread/local shortlife processing. This map
	 * doesn't guarantee concurrent write access, but it is optimized for fast
	 * creation.
	 * 
	 * Implementation may loose some read/write speed on large amounts of data
	 * in order to make best effort of fast creation.
	 * 
	 * @param data
	 * @return map
	 */
	public static final Map<String, Object> tempMap(final BaseObject data) {
		
		final BaseMap map = new BaseNativeObject(BaseObject.PROTOTYPE);
		map.baseDefineImportAllEnumerable(data);
		return map;
	}
	
	/**
	 * Unsynchronized map for per-thread/local shortlife processing. This map
	 * doesn't guarantee concurrent write access, but it is optimized for fast
	 * creation.
	 * 
	 * Implementation may loose some read/write speed on large amounts of data
	 * in order to make best effort of fast creation.
	 * 
	 * @param data
	 * @param <K>
	 * @param <V>
	 * @return map
	 */
	public static final <K, V> Map<K, V> tempMap(final Map<K, V> data) {
		
		final Map<K, V> map = new TreeMap<>(ComparatorJava.INSTANCE);
		map.putAll(data);
		return map;
	}
	
	/**
	 * Unsynchronized collection for per-thread/local shortlife processing.
	 * 
	 * @param <T>
	 * @return set
	 */
	public static final <T> Set<T> tempSet() {
		
		return new TreeSet<>(ComparatorJava.INSTANCE);
	}
	
	/**
	 * Unsynchronized set for per-thread/local shortlife processing.
	 * 
	 * @param init
	 * @param <T>
	 * @return set
	 */
	public static final <T> Set<T> tempSet(final Collection<T> init) {
		
		if (init.size() > 64) {
			return new HashSet<>(init);
		}
		final Set<T> set = new TreeSet<>(ComparatorJava.INSTANCE);
		set.addAll(init);
		return set;
	}
	
	/**
	 * Unsynchronized set for per-thread/local shortlife processing.
	 * 
	 * @param init
	 * @param <T>
	 * @return set
	 */
	public static final <T> Set<T> tempSet(final int init) {
		
		return init > 64
			? (Set<T>) new HashSet<T>(init)
			: new TreeSet<T>(ComparatorJava.INSTANCE);
	}
	
	/**
	 * Unsynchronized set for per-thread/local shortlife processing.
	 * 
	 * @param init
	 * @param <T>
	 * @return set
	 */
	public static final <T> Set<T> tempSet(final Iterator<T> init) {
		
		final Set<T> set = new TreeSet<>(ComparatorJava.INSTANCE);
		for (; init.hasNext();) {
			set.add(init.next());
		}
		return set;
	}
	
	/**
	 * Unsynchronized map for per-thread/local shortlife processing. This map
	 * doesn't guarantee concurrent write access, but it is optimized for fast
	 * creation.
	 * 
	 * Implementation may loose some read/write speed on large amounts of data
	 * in order to make best effort of fast creation.
	 * 
	 * So it happens to be just nice little macro for new TreeMap() constructor
	 * which takes care about supplying automatic template types.
	 * 
	 * @param <K>
	 * @param <V>
	 * @return map
	 */
	public static final <K extends Comparable<?>, V> Map<K, V> treeMap() {
		
		return new TreeMap<>(ComparatorJava.INSTANCE);
	}
	
	private Create() {
		// empty
	}
}
