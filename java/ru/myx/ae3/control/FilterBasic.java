/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.base.BaseHostFilter;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * @param <T>
 * @param
 * 			<P>
 * 			
 */
public class FilterBasic<T extends FilterBasic<?, ?>, P extends ControlBasic<?>> extends BaseHostFilter<P> implements ControlBasic<T> {
	
	/**
	 * @param parent
	 */
	protected FilterBasic(final P parent) {
		super(parent);
	}
	
	@Override
	public BaseObject getAttributes() {
		
		return this.parent.getAttributes();
	}
	
	@Override
	public BaseObject getData() {
		
		return this.parent.getData();
	}
	
	@Override
	public String getIcon() {
		
		return this.parent.getIcon();
	}
	
	@Override
	public String getKey() {
		
		return this.parent.getKey();
	}
	
	@Override
	public String getTitle() {
		
		return this.parent.getTitle();
	}
	
	@Override
	public boolean hasAttributes() {
		
		return this.parent.hasAttributes();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final BaseObject value) {
		
		this.parent = (P) this.parent.setAttribute(name, value);
		return (T) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final boolean value) {
		
		this.parent = (P) this.parent.setAttribute(name, value);
		return (T) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final double value) {
		
		this.parent = (P) this.parent.setAttribute(name, value);
		return (T) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final long value) {
		
		this.parent = (P) this.parent.setAttribute(name, value);
		return (T) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final String value) {
		
		this.parent = (P) this.parent.setAttribute(name, value);
		return (T) this;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttributes(final BaseObject map) {
		
		this.parent = (P) this.parent.setAttributes(map);
		return (T) this;
	}
	
	/**
	 * @param parent
	 */
	protected final void setParent(final P parent) {
		
		this.parent = parent;
	}
}
