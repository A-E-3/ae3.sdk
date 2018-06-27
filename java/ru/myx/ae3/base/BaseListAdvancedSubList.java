package ru.myx.ae3.base;

/**
 * 
 * @author myx
 * 		
 * @param <T>
 */
public class BaseListAdvancedSubList<T> extends BaseListAdvancedAbstract<T> {
	
	final BaseArrayAdvanced<T> array;
	
	final int start;
	
	final int end;
	
	BaseListAdvancedSubList(final BaseArrayAdvanced<T> array, final int start, final int end) {
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
}
