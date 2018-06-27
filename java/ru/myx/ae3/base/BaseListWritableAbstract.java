package ru.myx.ae3.base;

import java.util.Collections;
import java.util.List;

/**
 * 
 * @author myx
 * 		
 * @param <T>
 */
public abstract class BaseListWritableAbstract<T> extends BaseListAdvancedAbstract<T> implements BaseArrayWritable<T> {
	
	/**
	 * To check abstract methods during compile
	 * 
	 * @author myx
	 * 		
	 */
	static class Check extends BaseListWritableAbstract<Object> {
		
		@Override
		public BaseObject baseGet(final int index, final BaseObject defaultValue) {
			
			return defaultValue;
		}
		
		@Override
		public boolean baseSet(final int index, final BaseObject value) {
			
			return false;
		}
		
		@Override
		public Object get(final int i) {
			
			return null;
		}
		
		@Override
		public int length() {
			
			return 0;
		}
		
		@Override
		public Object set(final int index, final Object value) {
			
			return null;
		}
	}
	
	@Override
	public BaseListWritableAbstract<T> baseDefaultReverse() {
		
		Collections.reverse(this);
		return this;
	}
	
	@Override
	public abstract boolean baseSet(int index, BaseObject value);
	
	@Override
	public abstract T set(int index, T value);
	
	@Override
	public List<T> subList(final int fromIndex, final int toIndex) {
		
		return new BaseListWritableSubList<>(this, fromIndex, toIndex);
	}
	
}
