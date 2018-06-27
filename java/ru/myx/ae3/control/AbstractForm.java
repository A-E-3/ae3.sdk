/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 * @param <T>
 * 			
 */
public abstract class AbstractForm<T extends AbstractForm<?>> extends AbstractActor<T> implements ControlForm<T> {
	
	private BaseObject data = null;
	
	@Override
	public BaseObject getData() {
		
		return this.data == null
			? this.data = new BaseNativeObject()
			: this.data;
	}
	
	@Override
	public BaseObject setData(final BaseObject data) {
		
		return this.data = data;
	}
}
