package ru.myx.ae3.base;

/**
 * 
 * @author myx
 * 		
 * @param <T>
 */
public class BaseListWritableSubList<T> extends BaseListWritableAbstract<T> {
	
	final BaseArrayWritable<T> array;
	
	final int start;
	
	final int end;
	
	BaseListWritableSubList(final BaseArrayWritable<T> array, final int start, final int end) {
		
		this.array = array;
		this.start = start;
		this.end = end;
	}
	
	//
	
	@Override
	public BaseObject baseGet(final int i, final BaseObject defaultValue) {
		
		final int index = i + this.start;
		if (i < 0 || index >= this.end) {
			return defaultValue;
		}
		return this.array.baseGet(index, defaultValue);
	}
	
	@Override
	public boolean baseSet(final int i, final BaseObject value) {
		
		final int index = i + this.start;
		if (i < 0 || index >= this.end) {
			return false;
		}
		return this.array.baseSet(index, value);
	}
	
	@Override
	public T get(final int i) {
		
		final int index = i + this.start;
		if (i < 0 || index >= this.end) {
			return null;
		}
		return this.array.get(index);
	}
	
	@Override
	public int length() {
		
		return this.end - this.start;
	}
	
	@Override
	public T set(final int i, final T value) {
		
		final int index = i + this.start;
		if (i < 0 || index >= this.end) {
			return null;
		}
		return this.array.set(index, value);
	}
}
