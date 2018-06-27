/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control;

import java.util.Iterator;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostDataSubstitution;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Format;

/** @author myx
 * @param <T>
 */
public abstract class AbstractBasic<T extends AbstractBasic<?>> implements BaseHostDataSubstitution<BaseObject>, ControlBasic<T> {
	
	private BaseNativeObject attributes = null;

	/**
	 *
	 */
	protected String key = null;

	@Override
	public BaseObject baseGetSubstitution() {
		
		return this.getData();
	}

	@Override
	public final BaseObject getAttributes() {
		
		return this.attributes;
	}

	@Override
	public BaseObject getData() {
		
		return BaseObject.UNDEFINED;
	}

	@Override
	public String getIcon() {
		
		/** attributes could be null */
		return this.attributes == null
			? null
			: Base.getString(this.attributes, "icon", null);
	}

	@Override
	public String getKey() {
		
		if (this.key == null) {
			this.key = this.attributes == null
				? null
				: Base.getString(this.attributes, "id", null);
			if (this.key == null) {
				this.key = "rnd" + Engine.createGuid();
			}
		}
		return this.key;
	}

	@Override
	public String getTitle() {
		
		/** attributes could be null */
		final String title = this.attributes == null
			? null
			: Base.getString(this.attributes, "title", null);
		return title == null
			? this.getKey()
			: title;
	}

	/** @throws IllegalArgumentException
	 */
	protected void recalculate() throws IllegalArgumentException {
		
		// empty
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final BaseObject value) {
		
		if (value == null) {
			if (this.attributes != null) {
				this.attributes.baseDelete(name);
				if ("id".equals(name)) {
					this.key = null;
				}
				this.recalculate();
			}
		} else {
			if (this.attributes == null) {
				this.attributes = new BaseNativeObject();
			}
			this.attributes.putAppend(name, value);
			if ("id".equals(name)) {
				this.key = null;
			}
			this.recalculate();
		}
		return (T) this;
	}

	/** Call this method to prevent recalculation. Don't forget to call recalculate() explicitly.
	 *
	 * @param name
	 * @param value
	 */
	protected final void setAttributeIntern(final String name, final BaseObject value) {
		
		if (this.attributes == null) {
			this.attributes = new BaseNativeObject();
		}
		this.attributes.putAppend(name, value);
		if ("id".equals(name)) {
			this.key = null;
		}
	}

	/** Call this method to prevent recalculation. Don't forget to call recalculate() explicitly.
	 *
	 * @param name
	 * @param value
	 */
	protected final void setAttributeIntern(final String name, final boolean value) {
		
		if (this.attributes == null) {
			this.attributes = new BaseNativeObject();
		}
		this.attributes.putAppend(name, value);
		if ("id".equals(name)) {
			this.key = null;
		}
	}

	/** Call this method to prevent recalculation. Don't forget to call recalculate() explicitly.
	 *
	 * @param name
	 * @param value
	 */
	protected final void setAttributeIntern(final String name, final double value) {
		
		if (this.attributes == null) {
			this.attributes = new BaseNativeObject();
		}
		this.attributes.putAppend(name, value);
		if ("id".equals(name)) {
			this.key = null;
		}
	}

	/** Call this method to prevent recalculation. Don't forget to call recalculate() explicitly.
	 *
	 * @param name
	 * @param value
	 */
	protected final void setAttributeIntern(final String name, final long value) {
		
		if (this.attributes == null) {
			this.attributes = new BaseNativeObject();
		}
		this.attributes.putAppend(name, value);
		if ("id".equals(name)) {
			this.key = null;
		}
	}

	/** Call this method to prevent recalculation. Don't forget to call recalculate() explicitly.
	 *
	 * @param name
	 * @param value
	 */
	protected final void setAttributeIntern(final String name, final String value) {
		
		if (this.attributes == null) {
			this.attributes = new BaseNativeObject();
		}
		this.attributes.putAppend(name, value);
		if ("id".equals(name)) {
			this.key = null;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public final T setAttributes(final BaseObject map) {
		
		if (map != null && Base.hasKeys(map)) {
			for (final Iterator<String> keys = map.baseKeysOwn(); keys.hasNext();) {
				final String key = keys.next();
				this.setAttributeIntern(key, map.baseGet(key, BaseObject.UNDEFINED));
			}
		}
		this.recalculate();
		return (T) this;
	}

	@Override
	public String toString() {
		
		return "[object " + this.baseClass() + "(" + this.toStringDetails() + ")]";
	}

	protected String toStringDetails() {
		
		return this.key == null
			? null
			: "key=" + Format.Ecma.string(this.key);
	}
}
