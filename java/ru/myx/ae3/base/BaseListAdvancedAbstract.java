package ru.myx.ae3.base;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import ru.myx.ae3.ecma.compare.ComparatorEcma;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ControlType;
import ru.myx.ae3.reflect.Reflect;

/**
 * Easy method to implement java.util.List 8-)
 * 
 * @author myx
 * 
 * @param <T>
 */
public abstract class BaseListAdvancedAbstract<T> implements BaseHost, List<T>, BaseArrayAdvanced<T> {
	
	
	/**
	 * To check abstract methods during compile
	 * 
	 * @author myx
	 * 
	 */
	static class Check extends BaseListAdvancedAbstract<Object> {
		
		
		@Override
		public BaseObject baseGet(final int index, final BaseObject defaultValue) {
			
			
			return defaultValue;
		}
		
		@Override
		public Object get(final int i) {
			
			
			return null;
		}
		
		@Override
		public int length() {
			
			
			return 0;
		}
	}
	
	@Override
	public void add(final int index, final T element) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean add(final T e) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean addAll(final Collection<? extends T> c) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean addAll(final int index, final Collection<? extends T> c) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean baseContains(final BaseObject value) {
		
		
		final int size = this.size();
		for (int i = 0; i < size; ++i) {
			final T item = this.get(i);
			if (ComparatorEcma.compareEQU(value, item)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		
		return false;
	}
	
	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		
		return false;
	}
	
	@Override
	public boolean baseDelete(final String name) {
		
		
		return false;
	}
	
	@Override
	public abstract BaseObject baseGet(final int index, final BaseObject defaultValue);
	
	@Override
	public ExecStateCode vmPropertyRead(final ExecProcess ctx, final int index, final BaseObject originalIfKnown, final BaseObject defaultValue, final ResultHandler store) {
		
		
		return store.execReturn(ctx, this.baseGet(index, defaultValue));
	}
	
	@Override
	public BaseObject baseGetFirst(final BaseObject defaultValue) {
		
		
		final int size = this.size();
		return size == 0
			? defaultValue
			: this.baseGet(0, defaultValue);
	}
	
	@Override
	public BaseObject baseGetLast(final BaseObject defaultValue) {
		
		
		final int size = this.size();
		return size == 0
			? defaultValue
			: this.baseGet(size - 1, defaultValue);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		
		return null;
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		
		return null;
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		
		return false;
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		
		return false;
	}
	
	@Override
	public Iterator<BaseObject> baseIterator() {
		
		
		final int size = this.size();
		if (size == 0) {
			return Collections.emptyIterator();
		}
		return new Iterator<BaseObject>() {
			
			
			int index = 0;
			
			@Override
			public boolean hasNext() {
				
				
				return this.index < size;
			}
			
			@Override
			public BaseObject next() {
				
				
				return BaseListAdvancedAbstract.this.baseGet(this.index++, null);
			}
			
			@Override
			public void remove() {
				
				
				throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
			}
		};
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		
		return BaseObject.ITERATOR_EMPTY;
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		
		return BaseObject.ITERATOR_EMPTY_PRIMITIVE;
	}
	
	@Override
	public BaseObject basePrototype() {
		
		
		return BaseArray.PROTOTYPE;
	}
	
	@Override
	public void clear() {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean contains(final Object o) {
		
		
		final int size = this.size();
		for (int i = 0; i < size; ++i) {
			final T item = this.get(i);
			if (ComparatorEcma.compareEQU(o, item)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean containsAll(final Collection<?> c) {
		
		
		final Iterator<?> e = c.iterator();
		while (e.hasNext()) {
			if (!this.contains(e.next())) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public abstract T get(final int i);
	
	@Override
	public int indexOf(final Object o) {
		
		
		final int size = this.size();
		for (int i = 0; i < size; ++i) {
			final T item = this.get(i);
			if (ComparatorEcma.compareEQU(o, item)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public boolean isEmpty() {
		
		
		return this.size() == 0;
	}
	
	@Override
	public Iterator<T> iterator() {
		
		
		final int size = this.size();
		if (size == 0) {
			return Collections.emptyIterator();
		}
		return new Iterator<T>() {
			
			
			int index = 0;
			
			@Override
			public boolean hasNext() {
				
				
				return this.index < size;
			}
			
			@Override
			public T next() {
				
				
				return BaseListAdvancedAbstract.this.get(this.index++);
			}
			
			@Override
			public void remove() {
				
				
				throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
			}
		};
	}
	
	@Override
	public int lastIndexOf(final Object o) {
		
		
		final int size = this.size();
		for (int i = size - 1; i >= 0; --i) {
			final T item = this.get(i);
			if (ComparatorEcma.compareEQU(o, item)) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public abstract int length();
	
	@Override
	public ListIterator<T> listIterator() {
		
		
		return this.listIterator(0);
	}
	
	@Override
	public ListIterator<T> listIterator(final int indexInitial) {
		
		
		final int size = this.size();
		if (size == 0) {
			return Collections.emptyListIterator();
		}
		return new ListIterator<T>() {
			
			
			int index = indexInitial;
			
			int last;
			
			@Override
			public void add(final T e) {
				
				
				throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
			}
			
			@Override
			public boolean hasNext() {
				
				
				return this.index < size;
			}
			
			@Override
			public boolean hasPrevious() {
				
				
				return this.index > 0;
			}
			
			@Override
			public T next() {
				
				
				return BaseListAdvancedAbstract.this.get(this.last = this.index++);
			}
			
			@Override
			public int nextIndex() {
				
				
				return this.index;
			}
			
			@Override
			public T previous() {
				
				
				return BaseListAdvancedAbstract.this.get(this.last = --this.index);
			}
			
			@Override
			public int previousIndex() {
				
				
				return this.index - 1;
			}
			
			@Override
			public void remove() {
				
				
				throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
			}
			
			@Override
			public void set(final T e) {
				
				
				BaseListAdvancedAbstract.this.set(this.last, e);
			}
		};
	}
	
	@Override
	public T remove(final int index) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean remove(final Object o) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean removeAll(final Collection<?> c) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public boolean retainAll(final Collection<?> c) {
		
		
		throw new UnsupportedOperationException("Non-Dynamic List, class=" + this.getClass().getName());
	}
	
	@Override
	public T set(final int index, final T element) {
		
		
		throw new UnsupportedOperationException("Non-Writable List, class=" + this.getClass().getName());
	}
	
	@Override
	public int size() {
		
		
		return this.length();
	}
	
	@Override
	public List<T> subList(final int fromIndex, final int toIndex) {
		
		
		return new BaseListAdvancedSubList<>(this, fromIndex, toIndex);
	}
	
	@Override
	public Object[] toArray() {
		
		
		final int size = this.size();
		final Object[] result = new Object[size];
		
		for (int i = 0; i < size; ++i) {
			result[i] = this.get(i);
		}
		
		return result;
	}
	
	@Override
	public <E> E[] toArray(final E[] a) {
		
		
		final int length = a.length;
		if (length == 0) {
			return a;
		}
		final int size = this.size();
		if (size == 0) {
			return a;
		}
		final Class<?> elementClass = a.getClass().getComponentType();
		final ControlType<?, ?> convertor = Reflect.getConverter(elementClass);
		
		@SuppressWarnings("unchecked")
		final E[] result = length < size
			? (E[]) Array.newInstance(elementClass, size)
			: a;
		
		for (int i = 0; i < size; ++i) {
			@SuppressWarnings("unchecked")
			final E converted = (E) convertor.convertAnyJavaToJava(this.get(i));
			result[i] = converted;
		}
		
		return result;
	}
	
	@Override
	public String toString() {
		
		
		final int length = this.size();
		if (length == 0) {
			return "";
		}
		final StringBuilder builder = new StringBuilder(length * 10);
		for (int i = 0; i < length; ++i) {
			final BaseObject element = this.baseGet(i, BaseObject.UNDEFINED);
			if (i != 0) {
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
