/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.control.command.ControlCommandset;

/**
 * @author myx
 * @param <T>
 * 			
 */
public abstract class AbstractContainer<T extends AbstractContainer<?>> extends AbstractActor<T> implements ControlContainer<T> {
	
	@Override
	public ControlCommandset getContentCommands(final String key) {
		
		return null;
	}
	
	@Override
	public ControlCommandset getContentMultipleCommands(final BaseArray keys) {
		
		return null;
	}
}
