/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control.command;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

/** @author myx
 *
 *         Adds commandsMap cache to ControlCommandSet interface */
public class SimpleCommandset extends ArrayList<ControlCommand<?>> implements ControlCommandset {
	
	/**
	 *
	 */
	private static final long serialVersionUID = 4444414220489055997L;
	
	private Map<String, ControlCommand<?>> commandsMap = null;
	
	@Override
	public boolean add(final ControlCommand<?> o) {
		
		this.commandsMap = null;
		return super.add(o);
	}
	
	@Override
	public void add(final int index, final ControlCommand<?> element) {
		
		this.commandsMap = null;
		super.add(index, element);
	}
	
	@Override
	public boolean addAll(final Collection<? extends ControlCommand<?>> c) {
		
		this.commandsMap = null;
		return super.addAll(c);
	}
	
	@Override
	public boolean addAll(final int index, final Collection<? extends ControlCommand<?>> c) {
		
		this.commandsMap = null;
		return super.addAll(index, c);
	}
	
	@Override
	public void clear() {
		
		this.commandsMap = null;
		super.clear();
	}
	
	@Override
	public ControlCommand<?> getByKey(final String name) {
		
		if (name == null || name.isBlank()) {
			return null;
		}
		if (this.commandsMap == null) {
			synchronized (this) {
				if (this.commandsMap == null) {
					this.commandsMap = new TreeMap<>();
					for (int i = this.size() - 1; i >= 0; --i) {
						final ControlCommand<?> command = this.get(i);
						this.commandsMap.put(command.getKey(), command);
					}
				}
			}
		}
		return this.commandsMap.get(name);
	}
	
	@Override
	public ControlCommand<?> remove(final int index) {
		
		this.commandsMap = null;
		return super.remove(index);
	}
	
	@Override
	protected void removeRange(final int fromIndex, final int toIndex) {
		
		this.commandsMap = null;
		super.removeRange(fromIndex, toIndex);
	}
	
	@Override
	public ControlCommand<?> set(final int index, final ControlCommand<?> element) {
		
		this.commandsMap = null;
		return super.set(index, element);
	}
}
