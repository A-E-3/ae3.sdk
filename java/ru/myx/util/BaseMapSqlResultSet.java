/*
 * Created on 20.05.2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.util;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseDate;
import ru.myx.ae3.base.BaseHost;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;

/**
 * @author myx
 * 
 */
public final class BaseMapSqlResultSet implements BaseHost, BaseMap, BaseArray, BaseProperty {
	
	
	final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {
		
		
		final class EntrySetIterator implements Iterator<Map.Entry<String, Object>> {
			
			
			private int counter = 0;
			
			private final int size = BaseMapSqlResultSet.this.size();
			
			@Override
			public boolean hasNext() {
				
				
				return this.counter < this.size;
			}
			
			@Override
			public Map.Entry<String, Object> next() {
				
				
				final int index = this.counter++;
				final String key;
				try {
					key = BaseMapSqlResultSet.this.record.getMetaData().getColumnName(index + 1);
				} catch (final SQLException e) {
					e.printStackTrace();
					return null;
				}
				return new EntrySimple<>(key, BaseMapSqlResultSet.this.get(index));
			}
			
			@Override
			public void remove() {
				
				
				// empty
			}
		}
		
		@Override
		public Iterator<Map.Entry<String, Object>> iterator() {
			
			
			return new EntrySetIterator();
		}
		
		@Override
		public int size() {
			
			
			return BaseMapSqlResultSet.this.size();
		}
	}
	
	final class KeySet extends AbstractSet<String> {
		
		
		final class KeySetIterator implements Iterator<String> {
			
			
			private int counter = 0;
			
			private final int size = BaseMapSqlResultSet.this.size();
			
			@Override
			public boolean hasNext() {
				
				
				return this.counter < this.size;
			}
			
			@Override
			public String next() {
				
				
				try {
					return BaseMapSqlResultSet.this.record.getMetaData().getColumnName(++this.counter);
				} catch (final SQLException e) {
					return null;
				}
			}
			
			@Override
			public void remove() {
				
				
				// empty
			}
		}
		
		@Override
		public Iterator<String> iterator() {
			
			
			return new KeySetIterator();
		}
		
		@Override
		public int size() {
			
			
			return BaseMapSqlResultSet.this.size();
		}
	}
	
	final class ValueCollection extends AbstractCollection<Object> {
		
		
		final class ValueCollectionIterator implements Iterator<Object> {
			
			
			private int counter = 0;
			
			private final int size = BaseMapSqlResultSet.this.size();
			
			@Override
			public boolean hasNext() {
				
				
				return this.counter < this.size;
			}
			
			@Override
			public Object next() {
				
				
				/**
				 * zero-based
				 */
				return BaseMapSqlResultSet.this.get(this.counter++);
			}
			
			@Override
			public void remove() {
				
				
				// empty
			}
		}
		
		@Override
		public Iterator<Object> iterator() {
			
			
			return new ValueCollectionIterator();
		}
		
		@Override
		public int size() {
			
			
			return BaseMapSqlResultSet.this.size();
		}
	}
	
	private static final TransferCopier getBlob(final java.sql.Blob blob) throws java.sql.SQLException {
		
		
		if (blob == null) {
			return null;
		}
		final int length = (int) blob.length();
		if (length <= 0) {
			return TransferCopier.NUL_COPIER;
		}
		if (length <= 32768) {
			return Transfer.wrapCopier(blob.getBytes(1L, length));
		}
		final TransferCollector collector = Transfer.createCollector();
		try {
			Transfer.toStream(blob.getBinaryStream(), collector.getOutputStream(), true);
			/**
			 * collector is closed buy ^^^
			 */
			return collector.toCloneFactory();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private static final String getClob(final java.sql.Clob clob) throws java.sql.SQLException {
		
		
		assert clob != null;
		return clob.getSubString(1L, (int) clob.length());
	}
	
	final ResultSet record;
	
	private KeySet keySet;
	
	private ValueCollection valueCollection;
	
	private EntrySet entrySet;
	
	/**
	 * @param record
	 */
	public BaseMapSqlResultSet(final ResultSet record) {
		
		if (record == null) {
			throw new NullPointerException("resultset object is null!");
		}
		this.record = record;
	}
	
	@Override
	public String baseClass() {
		
		
		return "ResultSet";
	}
	
	@Override
	public boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		if (attributes != BaseProperty.ATTRS_MASK_WED) {
			return false;
		}
		this.put(name.toString(), value.baseValue());
		return true;
	}
	
	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		if (attributes != BaseProperty.ATTRS_MASK_WED) {
			return false;
		}
		this.put(name, value.baseValue());
		return true;
	}
	
	@Override
	public boolean baseDelete(final String name) {
		
		
		try {
			this.record.updateNull(name);
		} catch (final java.sql.SQLException e) {
			throw new Error("ParamsCollectionSqlResultSet.Remove(Key): cannot delete fields from table :)");
		}
		return true;
	}
	
	@Override
	public BaseObject baseGet(final int index, final BaseObject defaultValue) {
		
		
		if (index < 0) {
			return defaultValue;
		}
		if (index >= this.length()) {
			return defaultValue;
		}
		try {
			/**
			 * first column is at index 1
			 */
			final int dtype = this.record.getMetaData().getColumnType(index + 1);
			switch (dtype) {
				case java.sql.Types.NULL : {
					return BaseObject.NULL;
				}
				case java.sql.Types.CLOB : {
					return Base.forString(BaseMapSqlResultSet.getClob(this.record.getClob(index + 1)));
				}
				case java.sql.Types.LONGVARCHAR :
				case java.sql.Types.VARCHAR : {
					return Base.forString(this.record.getString(index + 1));
				}
				case java.sql.Types.BLOB :
				case java.sql.Types.LONGVARBINARY : {
					return BaseMapSqlResultSet.getBlob(this.record.getBlob(index + 1));
				}
				case java.sql.Types.BINARY :
				case java.sql.Types.VARBINARY : {
					return Base.forUnknown(this.record.getBytes(index + 1));
				}
				case java.sql.Types.NUMERIC : {
					return Base.forDouble(this.record.getDouble(index + 1));
				}
				case java.sql.Types.DOUBLE : {
					return Base.forDouble(this.record.getDouble(index + 1));
				}
				case java.sql.Types.FLOAT : {
					return Base.forDouble(this.record.getFloat(index + 1));
				}
				case java.sql.Types.SMALLINT :
				case java.sql.Types.INTEGER : {
					return Base.forInteger(this.record.getInt(index + 1));
				}
				case java.sql.Types.BIGINT : {
					return Base.forLong(this.record.getLong(index + 1));
				}
				case java.sql.Types.BIT : {
					return this.record.getBoolean(index + 1)
						? BaseObject.TRUE
						: BaseObject.FALSE;
				}
				case java.sql.Types.TIME : {
					final java.util.Date date = this.record.getTime(index + 1);
					return date == null
						? BaseDate.UNKNOWN
						: new BaseDate(date);
				}
				case java.sql.Types.TIMESTAMP : {
					final java.util.Date date = this.record.getTimestamp(index + 1);
					return date == null
						? BaseDate.UNKNOWN
						: new BaseDate(date);
				}
				case java.sql.Types.DATE : {
					final java.util.Date date = this.record.getDate(index + 1);
					return date == null
						? BaseDate.UNKNOWN
						: new BaseDate(date);
				}
				default : {
					final Object object = this.record.getObject(index + 1);
					if (object == null) {
						return BaseObject.NULL;
					}
					if (object instanceof java.sql.Blob) {
						return BaseMapSqlResultSet.getBlob((java.sql.Blob) object);
					}
					if (object instanceof java.sql.Clob) {
						return Base.forString(BaseMapSqlResultSet.getClob((java.sql.Clob) object));
					}
					return Base.forUnknown(object);
				}
			}
		} catch (final SQLException e) {
			return BaseObject.UNDEFINED;
		}
	}
	
	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {
		
		
		return store.execReturn(ctx, this.baseGet(index, defaultValue));
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		return this.containsKey(name.toString())
			? this
			: null;
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		
		return this.containsKey(name)
			? this
			: null;
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		
		return !this.isEmpty();
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		
		return false;
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		
		return this.keySet().iterator();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		return Base.iteratorPrimitiveSafe(this.baseKeysOwn());
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return BaseArray.PROTOTYPE;
	}
	
	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		return BasePrimitiveNumber.NAN;
	}
	
	@Override
	public void clear() {
		
		
		// empty
	}
	
	@Override
	public boolean containsKey(final Object key) {
		
		
		try {
			return key == null
				? false
				: this.record.findColumn(key.toString()) > 0;
		} catch (final SQLException e) {
			return false;
		}
	}
	
	@Override
	public boolean containsValue(final Object value) {
		
		
		for (final Object val : this.values()) {
			if (val == value || val != null && val.equals(value)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public final Set<Map.Entry<String, Object>> entrySet() {
		
		
		return this.entrySet == null
			? this.entrySet = new EntrySet()
			: this.entrySet;
	}
	
	/**
	 * zero-based
	 */
	@Override
	public Object get(final int i) {
		
		
		try {
			/**
			 * first column is at index 1
			 */
			final int index = i + 1;
			final int dtype = this.record.getMetaData().getColumnType(index);
			switch (dtype) {
				case java.sql.Types.NULL : {
					return null;
				}
				case java.sql.Types.CLOB : {
					return BaseMapSqlResultSet.getClob(this.record.getClob(index));
				}
				case java.sql.Types.LONGVARCHAR :
				case java.sql.Types.VARCHAR : {
					return this.record.getString(index);
				}
				case java.sql.Types.BLOB :
				case java.sql.Types.LONGVARBINARY : {
					return BaseMapSqlResultSet.getBlob(this.record.getBlob(index));
				}
				case java.sql.Types.BINARY :
				case java.sql.Types.VARBINARY : {
					return this.record.getBytes(index);
				}
				case java.sql.Types.NUMERIC : {
					final long l = this.record.getLong(index);
					final double d = this.record.getDouble(index);
					return l == d
						? (Number) Long.valueOf(l)
						: (Number) Double.valueOf(d);
				}
				case java.sql.Types.DOUBLE : {
					return Double.valueOf(this.record.getDouble(index));
				}
				case java.sql.Types.FLOAT : {
					return Float.valueOf(this.record.getFloat(index));
				}
				case java.sql.Types.INTEGER : {
					return Integer.valueOf(this.record.getInt(index));
				}
				case java.sql.Types.BIGINT : {
					return Long.valueOf(this.record.getLong(index));
				}
				case java.sql.Types.SMALLINT : {
					return Integer.valueOf(this.record.getInt(index));
				}
				case java.sql.Types.BIT : {
					return this.record.getBoolean(index)
						? Boolean.TRUE
						: Boolean.FALSE;
				}
				case java.sql.Types.TIME : {
					final java.util.Date date = this.record.getTime(index);
					return date == null
						? null
						: date;
				}
				case java.sql.Types.TIMESTAMP : {
					final java.util.Date date = this.record.getTimestamp(index);
					return date == null
						? null
						: date;
				}
				case java.sql.Types.DATE : {
					final java.util.Date date = this.record.getDate(index);
					return date == null
						? null
						: date;
				}
				default : {
					final Object object = this.record.getObject(index);
					if (object == null) {
						return null;
					}
					if (object instanceof java.sql.Blob) {
						return BaseMapSqlResultSet.getBlob((java.sql.Blob) object);
					}
					if (object instanceof java.sql.Clob) {
						return BaseMapSqlResultSet.getClob((java.sql.Clob) object);
					}
					return object;
				}
			}
		} catch (final SQLException e) {
			return null;
		}
	}
	
	@Override
	public Object get(final Object key) {
		
		
		if (key == null) {
			return null;
		}
		final int index;
		try {
			index = key instanceof Number && ((Number) key).intValue() == ((Number) key).doubleValue()
				? ((Number) key).intValue()
				/**
				 * first column is at index 1
				 */
				: this.record.findColumn(key.toString()) - 1;
		} catch (final SQLException e) {
			return null;
		}
		return this.get(index);
	}
	
	@Override
	public boolean isEmpty() {
		
		
		return this.size() == 0;
	}
	
	@Override
	public final Set<String> keySet() {
		
		
		return this.keySet == null
			? this.keySet = new KeySet()
			: this.keySet;
	}
	
	@Override
	public int length() {
		
		
		return this.size();
	}
	
	/**
	 * actually non-dynamic, but we set NULLs for baseDelete/remove methods
	 */
	@Override
	public short propertyAttributes(final CharSequence name) {
		
		
		return BaseProperty.ATTRS_MASK_WED_NPK;
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString key) {
		
		
		return Base.forUnknown(this.get(key));
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {
		
		
		return Base.forUnknown(this.get(name));
	}
	
	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {
		
		
		return Base.forUnknown(this.put(name, value.baseValue()));
	}
	
	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {
		
		
		return store.execReturn(ctx, this.propertyGet(instance, name.stringValue()));
	}
	
	@Override
	public boolean propertySet(final BaseObject instance, final CharSequence name, final BaseObject value, final short attributes) {
		
		
		if (attributes != BaseProperty.ATTRS_MASK_WED) {
			return false;
		}
		this.put(name.toString(), value.baseValue());
		return true;
	}
	
	@Override
	public Object put(final String key, final Object value) {
		
		
		if (key == null) {
			return null;
		}
		try {
			return this.get(key);
		} finally {
			try {
				this.record.moveToCurrentRow();
				if (value instanceof TransferBuffer) {
					final TransferBuffer buff = (TransferBuffer) value;
					if (buff.isDirectAbsolutely()) {
						this.record.updateBytes(key, buff.toDirectArray());
					} else {
						final long length = buff.remaining();
						if (length > Integer.MAX_VALUE) {
							throw new RuntimeException("Bigger than maximum byte array size, size=" + length + "!");
						}
						this.record.updateBinaryStream(key, buff.toInputStream(), (int) length);
					}
				} else {
					this.record.updateObject(key, value);
				}
				this.record.updateRow();
			} catch (final SQLException e) {
				// ignore
			}
		}
	}
	
	@Override
	public void putAll(final Map<? extends String, ? extends Object> map) {
		
		
		if (!map.isEmpty()) {
			for (final Map.Entry<? extends String, ? extends Object> current : map.entrySet()) {
				this.put(current.getKey(), current.getValue());
			}
		}
	}
	
	@Override
	public Object remove(final Object key) {
		
		
		if (key == null) {
			return null;
		}
		try {
			return this.get(key);
		} finally {
			try {
				this.record.updateNull(key.toString());
			} catch (final java.sql.SQLException e) {
				throw new Error("ParamsCollectionSqlResultSet.Remove(Key): cannot delete fields from table :)");
			}
		}
	}
	
	@Override
	public int size() {
		
		
		try {
			return this.record.getMetaData().getColumnCount();
		} catch (final SQLException e) {
			return 0;
		}
	}
	
	@Override
	public String toString() {
		
		
		return "[object " + this.baseClass() + "(" + Ecma.toEcmaSourceCompact(this) + ")]";
	}
	
	@Override
	public final Collection<Object> values() {
		
		
		return this.valueCollection == null
			? this.valueCollection = new ValueCollection()
			: this.valueCollection;
	}
}
