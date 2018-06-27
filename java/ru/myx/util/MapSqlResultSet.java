/*
 * Created on 20.05.2003
 * 
 * To change the template for this generated file go to
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

import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferCopier;

/**
 * @author myx
 * 
 */
public final class MapSqlResultSet implements Map<String, Object> {
	final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {
		final class EntrySetIterator implements Iterator<Map.Entry<String, Object>> {
			private int	counter	= MapSqlResultSet.this.size();
			
			@Override
			public boolean hasNext() {
				return this.counter > 0;
			}
			
			@Override
			public Map.Entry<String, Object> next() {
				final String key;
				try {
					key = MapSqlResultSet.this.record.getMetaData().getColumnName( this.counter-- );
				} catch (final SQLException e) {
					e.printStackTrace();
					return null;
				}
				return new EntrySimple<>( key, MapSqlResultSet.this.get( key ) );
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
			return MapSqlResultSet.this.size();
		}
	}
	
	final class KeySet extends AbstractSet<String> {
		final class KeySetIterator implements Iterator<String> {
			private int	counter	= MapSqlResultSet.this.size();
			
			@Override
			public boolean hasNext() {
				return this.counter > 0;
			}
			
			@Override
			public String next() {
				try {
					return MapSqlResultSet.this.record.getMetaData().getColumnName( this.counter-- );
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
			return MapSqlResultSet.this.size();
		}
	}
	
	final class ValueCollection extends AbstractCollection<Object> {
		final class ValueCollectionIterator implements Iterator<Object> {
			private int	counter	= MapSqlResultSet.this.size();
			
			@Override
			public boolean hasNext() {
				return this.counter > 0;
			}
			
			@Override
			public Object next() {
				final String key;
				try {
					key = MapSqlResultSet.this.record.getMetaData().getColumnName( this.counter-- );
				} catch (final SQLException e) {
					e.printStackTrace();
					return null;
				}
				return MapSqlResultSet.this.get( key );
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
			return MapSqlResultSet.this.size();
		}
	}
	
	private static final Object getBlob(final java.sql.Blob blob) throws java.sql.SQLException {
		if (blob == null) {
			return null;
		}
		final int length = (int) blob.length();
		if (length <= 0) {
			return TransferCopier.NUL_COPIER;
		}
		if (length <= 32768) {
			return Transfer.wrapCopier( blob.getBytes( 1L, length ) );
		}
		final TransferCollector collector = Transfer.createCollector();
		try {
			Transfer.toStream( blob.getBinaryStream(), collector.getOutputStream(), true );
			/**
			 * collector is closed buy ^^^
			 */
			return collector.toCloneFactory();
		} catch (final IOException e) {
			throw new RuntimeException( e );
		}
	}
	
	private static final Object getClob(final java.sql.Clob clob) throws java.sql.SQLException {
		assert clob != null;
		return clob.getSubString( 1L, (int) clob.length() );
	}
	
	final ResultSet			record;
	
	private KeySet			keySet;
	
	private ValueCollection	valueCollection;
	
	private EntrySet		entrySet;
	
	/**
	 * @param record
	 */
	public MapSqlResultSet(final ResultSet record) {
		if (record == null) {
			throw new NullPointerException( "resultset object is null!" );
		}
		this.record = record;
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
					: this.record.findColumn( key.toString() ) > 0;
		} catch (final SQLException e) {
			return false;
		}
	}
	
	@Override
	public boolean containsValue(final Object value) {
		for (final Object val : this.values()) {
			if (val == value || val != null && val.equals( value )) {
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
	
	@Override
	public Object get(final Object key) {
		if (key == null) {
			return null;
		}
		try {
			final int index = this.record.findColumn( key.toString() );
			final int dtype = this.record.getMetaData().getColumnType( index );
			switch (dtype) {
			case java.sql.Types.NULL: {
				return null;
			}
			case java.sql.Types.CLOB: {
				return MapSqlResultSet.getClob( this.record.getClob( index ) );
			}
			case java.sql.Types.LONGVARCHAR:
			case java.sql.Types.VARCHAR: {
				return this.record.getString( index );
			}
			case java.sql.Types.BLOB:
			case java.sql.Types.BINARY:
			case java.sql.Types.VARBINARY:
			case java.sql.Types.LONGVARBINARY: {
				return MapSqlResultSet.getBlob( this.record.getBlob( index ) );
			}
			case java.sql.Types.NUMERIC: {
				final long l = this.record.getLong( index );
				final double d = this.record.getDouble( index );
				return l == d
						? (Number) new Long( l )
						: (Number) new Double( d );
			}
			case java.sql.Types.DOUBLE: {
				return new Double( this.record.getDouble( index ) );
			}
			case java.sql.Types.FLOAT: {
				return new Float( this.record.getFloat( index ) );
			}
			case java.sql.Types.INTEGER: {
				return new Integer( this.record.getInt( index ) );
			}
			case java.sql.Types.BIGINT: {
				return new Long( this.record.getLong( index ) );
			}
			case java.sql.Types.SMALLINT: {
				return new Integer( this.record.getInt( index ) );
			}
			case java.sql.Types.BIT: {
				return this.record.getBoolean( index )
						? Boolean.TRUE
						: Boolean.FALSE;
			}
			case java.sql.Types.TIME: {
				final java.util.Date date = this.record.getTime( index );
				return date == null
						? null
						: date;
			}
			case java.sql.Types.TIMESTAMP: {
				final java.util.Date date = this.record.getTimestamp( index );
				return date == null
						? null
						: date;
			}
			case java.sql.Types.DATE: {
				final java.util.Date date = this.record.getDate( index );
				return date == null
						? null
						: date;
			}
			default: {
				final Object object = this.record.getObject( index );
				if (object == null) {
					return null;
				}
				if (object instanceof java.sql.Blob) {
					return MapSqlResultSet.getBlob( (java.sql.Blob) object );
				}
				if (object instanceof java.sql.Clob) {
					return MapSqlResultSet.getClob( (java.sql.Clob) object );
				}
				return object;
			}
			}
		} catch (final SQLException e) {
			return null;
		}
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
	public Object put(final String key, final Object value) {
		if (key == null) {
			return null;
		}
		try {
			return this.get( key );
		} finally {
			try {
				this.record.moveToCurrentRow();
				if (value instanceof TransferBuffer) {
					final TransferBuffer buff = (TransferBuffer) value;
					if (buff.isDirectAbsolutely()) {
						this.record.updateBytes( key, buff.toDirectArray() );
					} else {
						final long length = buff.remaining();
						if (length > Integer.MAX_VALUE) {
							throw new RuntimeException( "Bigger than maximum byte array size, size=" + length + "!" );
						}
						this.record.updateBinaryStream( key, buff.toInputStream(), (int) length );
					}
				} else {
					this.record.updateObject( key, value );
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
				this.put( current.getKey(), current.getValue() );
			}
		}
	}
	
	@Override
	public Object remove(final Object key) {
		if (key == null) {
			return null;
		}
		try {
			return this.get( key );
		} finally {
			try {
				this.record.updateNull( key.toString() );
			} catch (final java.sql.SQLException e) {
				throw new Error( "ParamsCollectionSqlResultSet.Remove(Key): cannot delete fields from table :)" );
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
	public final Collection<Object> values() {
		return this.valueCollection == null
				? this.valueCollection = new ValueCollection()
				: this.valueCollection;
	}
}
