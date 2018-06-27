/*
 * Created on 03.05.2005
 */
package ru.myx.ae3.base;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionIgnore;

/**
 * @author myx
 * 
 * @param <T>
 *            java array element type
 * 
 * 
 *            TODO: cache last access element by index in hope of normal forward
 *            loop speedup
 */
@ReflectionIgnore
public class BaseLinkedList<T> extends LinkedList<T> implements BaseListNoOwnProperties<T> {
	
	
	private static final BaseObject PROTOTYPE = Reflect.classToBasePrototype(LinkedList.class);
	
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
	public BaseObject baseDefaultPop() {
		
		
		return this.isEmpty()
			? BaseObject.UNDEFINED
			: Base.forUnknown(this.removeLast());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int baseDefaultPush(final BaseObject object) {
		
		
		this.addLast((T) object.baseValue());
		return this.size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int baseInsert(int index, BaseObject object) {
		
		
		this.add(index, (T) object.baseValue());
		return this.size();
	}
	
	@Override
	public BaseLinkedList<T> baseDefaultReverse() {
		
		
		Collections.reverse(this);
		return this;
	}
	
	@Override
	public BaseObject baseDefaultShift() {
		
		
		return this.isEmpty()
			? BaseObject.UNDEFINED
			: Base.forUnknown(this.removeFirst());
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int baseDefaultUnshift(final BaseObject object) {
		
		
		this.addFirst((T) object.baseValue());
		return this.size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public final int baseDefaultUnshift(final BaseObject[] values) {
		
		
		/**
		 * they end up being in the same order as in arguments
		 */
		for (int index = values.length - 1; index >= 0; --index) {
			this.addFirst((T) values[index].baseValue());
		}
		return this.size();
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
		
		
		final T value;
		try {
			value = this.get(index);
		} catch (final ArrayIndexOutOfBoundsException e) {
			/**
			 * EcmaScript way of dealing with such errors
			 */
			return store.execReturn(ctx, defaultValue);
		}
		return value != null
			? store.execReturnObject(ctx, value)
			: store.execReturn(ctx, defaultValue);
	}
	
	@Override
	public BaseObject baseGetFirst(final BaseObject defaultValue) {
		
		
		final T value;
		try {
			value = this.getFirst();
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
			value = this.getLast();
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
	public Iterator<BaseObject> baseIterator() {
		
		
		final Iterator<T> iterator = this.iterator();
		return new Iterator<BaseObject>() {
			
			
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
		
		
		return BaseLinkedList.PROTOTYPE;
	}
	
	@Override
	public BaseObject baseRemove(final int index) {
		
		
		try {
			return this.baseGet(index, BaseObject.UNDEFINED);
		} finally {
			this.remove(index);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean baseSet(final int index, final BaseObject value) {
		
		
		this.set(index, (T) value.baseValue());
		return true;
	}
	
	@Override
	public BasePrimitiveNumber baseToNumber() {
		
		
		return BasePrimitiveNumber.NAN;
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
	
	@Override
	public Object baseValue() {
		
		
		return this;
	}
	
	@Override
	public BaseLinkedList<T> clone() {
		
		
		final BaseLinkedList<T> result = new BaseLinkedList<>();
		result.addAll(this);
		return result;
	}
	
	@Override
	public int length() {
		
		
		return super.size();
	}
}
