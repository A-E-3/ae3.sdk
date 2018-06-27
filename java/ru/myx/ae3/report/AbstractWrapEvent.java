package ru.myx.ae3.report;

import ru.myx.ae3.base.BaseAbstract;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.common.Value;

/**
 * @author myx
 * 
 * @param <V>
 */
public abstract class AbstractWrapEvent<V extends Event> extends BaseAbstract implements Event, BaseObjectNoOwnProperties, Value<V> {
	
	
	/**
	 * 
	 */
	protected V wrapped;
	
	/**
	 * 
	 * @param wrapped
	 */
	protected AbstractWrapEvent(final V wrapped) {
		
		assert wrapped != null;
		this.wrapped = wrapped;
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		
		return this.wrapped.baseToString();
	}
	
	@Override
	public String toString() {
		
		
		return this.wrapped.toString();
	}
	
	@Override
	public V baseValue() {
		
		
		return this.wrapped;
	}
	
	//
	
	@Override
	public long getDate() {
		
		
		return this.wrapped.getDate();
	}
	
	@Override
	public String getEventTypeId() {
		
		
		return this.wrapped.getEventTypeId();
	}
	
	@Override
	public long getProcess() {
		
		
		return this.wrapped.getProcess();
	}
	
	@Override
	public String getSubject() {
		
		
		return this.wrapped.getSubject();
	}
	
	@Override
	public String getTitle() {
		
		
		return this.wrapped.getTitle();
	}
}
