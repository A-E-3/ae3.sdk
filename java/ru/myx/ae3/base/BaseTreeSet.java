/*
 * Created on 03.05.2005
 */
package ru.myx.ae3.base;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionIgnore;

/**
 * FIXME: unfinished
 * 
 * @author myx
 * 
 * @param <T>
 *            java array element type
 */
@ReflectionIgnore
public class BaseTreeSet<T extends Object> extends TreeSet<T> implements BaseObjectNoOwnProperties, BaseArrayAdvanced<T> {
	
	
	private static final BaseObject PROTOTYPE = Reflect.classToBasePrototype(Set.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1978981599142417504L;
	
	@Override
	public String baseClass() {
		
		
		return "FIFO";
	}
	
	@Override
	public boolean baseContains(final BaseObject value) {
		
		
		return this.contains(value.baseValue()) || this.contains(value);
	}
	
	@Override
	public BaseObject baseGet(final int index, final BaseObject defaultValue) {
		
		
		final T value;
		try {
			value = this.get(index);
		} catch (final ArrayIndexOutOfBoundsException e) {
			/**
			 * EcmaScript way of dealing with such errors
			 */
			return defaultValue;
		}
		return value != null
			? Base.forUnknown(value)
			: defaultValue;
	}
	
	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {
		
		
		return store.execReturn(ctx, this.baseGet(index, defaultValue));
	}
	
	@Override
	public BaseObject baseGetFirst(final BaseObject defaultValue) {
		
		
		final T value;
		try {
			value = this.first();
		} catch (final ArrayIndexOutOfBoundsException e) {
			/**
			 * EcmaScript way of dealing with such errors
			 */
			return defaultValue;
		}
		return value != null
			? Base.forUnknown(value)
			: defaultValue;
	}
	
	@Override
	public BaseObject baseGetLast(final BaseObject defaultValue) {
		
		
		final T value;
		try {
			value = this.last();
		} catch (final ArrayIndexOutOfBoundsException e) {
			/**
			 * EcmaScript way of dealing with such errors
			 */
			return defaultValue;
		}
		return value != null
			? Base.forUnknown(value)
			: defaultValue;
	}
	
	@Override
	public Iterator<? extends BaseObject> baseIterator() {
		
		
		final Iterator<T> iterator = this.iterator();
		return new Iterator<>() {
			
			
			@Override
			public boolean hasNext() {
				
				
				return iterator.hasNext();
			}
			
			@Override
			public BaseObject next() {
				
				
				return Base.forUnknown(iterator.next());
			}
			
			@Override
			public void remove() {
				
				
				iterator.remove();
			}
		};
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return BaseTreeSet.PROTOTYPE;
	}
	
	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		return BasePrimitiveNumber.NAN;
	}
	
	@Override
	public BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {
		
		
		return Base.defaultToPrimitive(this, hint);
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		
		final int length = this.size();
		if (length == 0) {
			return BaseString.EMPTY;
		}
		final StringBuilder builder = new StringBuilder(length * 10);
		for (final Object object : this) {
			final BaseObject element = Base.forUnknown(object);
			assert element != null : "NULL java value!";
			if (builder.length() > 0) {
				builder.append(',');
			}
			if (element == BaseObject.UNDEFINED || element == BaseObject.NULL) {
				// ignore
			} else {
				builder.append(element.baseToJavaString());
			}
		}
		return Base.forString(builder.toString());
	}
	
	@Override
	public Object baseValue() {
		
		
		return this;
	}
	
	@Override
	public BaseTreeSet<T> clone() {
		
		
		final BaseTreeSet<T> result = new BaseTreeSet<>();
		result.addAll(this);
		return result;
	}
	
	@Override
	public T get(final int i) {
		
		
		final Iterator<T> iterator = this.iterator();
		for (int left = i; left > 0; --left) {
			if (!iterator.hasNext()) {
				throw new ArrayIndexOutOfBoundsException(i - left);
			}
			iterator.next();
		}
		if (!iterator.hasNext()) {
			throw new ArrayIndexOutOfBoundsException(i);
		}
		return iterator.next();
	}
	
	@Override
	public int length() {
		
		
		return super.size();
	}
	
	@Override
	public String toString() {
		
		
		final int length = this.size();
		if (length == 0) {
			return "";
		}
		final StringBuilder builder = new StringBuilder(length * 10);
		for (final Object object : this) {
			final BaseObject element = Base.forUnknown(object);
			assert element != null : "NULL java value!";
			if (builder.length() > 0) {
				builder.append(',');
			}
			if (element == BaseObject.UNDEFINED || element == BaseObject.NULL) {
				// ignore
			} else {
				builder.append(element.baseToJavaString());
			}
		}
		return builder.toString();
	}
}
