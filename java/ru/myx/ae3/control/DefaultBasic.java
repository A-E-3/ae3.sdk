/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control;

import ru.myx.ae3.base.BaseObject;

/**
 * TODO: move to sys?
 *
 * @author myx
 *
 */
public final class DefaultBasic extends AbstractBasic<DefaultBasic> {
	
	final Object title;

	final BaseObject data;

	/**
	 *
	 * @param key
	 * @param title
	 * @param data
	 */
	public DefaultBasic(final String key, final Object title, final BaseObject data) {
		this.key = key;
		this.title = title == null
			? key
			: title;
		this.data = data;
	}

	@Override
	public BaseObject baseGetSubstitution() {
		
		return this.data;
	}

	@Override
	public BaseObject getData() {
		
		return this.data;
	}

	@Override
	public String getIcon() {
		
		return null;
	}

	@Override
	public String getTitle() {
		
		return String.valueOf(this.title);
	}
}
