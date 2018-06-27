package ru.myx.ae3.flow;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseMessageEditable;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.reflect.ReflectionIgnore;

/**
 * @author barachta
 *
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 * @param <T>
 */
@ReflectionIgnore
public abstract class AbstractMessageMutable<T extends BaseMessageEditable<T>> extends AbstractMessage<T> {
	
	
	/**
	 *
	 */
	protected BaseObject attributes = null;

	/**
	 *
	 * @param attributes
	 */
	protected AbstractMessageMutable(final BaseObject attributes) {
		
		this.attributes = attributes;
	}

	/**
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T addAttribute(final String name, final BaseObject value) {
		
		
		if (value == null) {
			throw new NullPointerException("Attribute value cannot be null, name=" + name);
		}
		if (this.attributes == null) {
			this.attributes = new BaseNativeObject();
			this.attributes.baseDefine("Date", Base.forDateMillis(Engine.fastTime()));
			this.attributes.baseDefine(name, value);
		} else {
			final BaseObject o = this.attributes.baseGet(name, BaseObject.UNDEFINED);
			assert o != null : "NULL java object";
			if (o == BaseObject.UNDEFINED) {
				this.attributes.baseDefine(name, value);
			} else {
				if (o instanceof MultipleList) {
					final MultipleList list = (MultipleList) o;
					if (!list.contains(value)) {
						list.add(value);
					}
				} else {
					if (!o.equals(value)) {
						final MultipleList list = new MultipleList();
						list.add(o);
						list.add(value);
						this.attributes.baseDefine(name, list);
					}
				}
			}
		}
		return (T) this;
	}

	/**
	 * @param name
	 * @param value
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T addAttribute(final String name, final Object value) {
		
		
		if (value == null) {
			throw new NullPointerException("Attribute value cannot be null, name=" + name);
		}
		if (this.attributes == null) {
			this.attributes = new BaseNativeObject();
			this.attributes.baseDefine("Date", Base.forDateMillis(Engine.fastTime()));
			this.attributes.baseDefine(name, Base.forUnknown(value));
		} else {
			final BaseObject o = this.attributes.baseGet(name, BaseObject.UNDEFINED);
			assert o != null : "NULL java object";
			if (o == BaseObject.UNDEFINED) {
				this.attributes.baseDefine(name, Base.forUnknown(value));
			} else {
				if (o instanceof MultipleList) {
					final MultipleList list = (MultipleList) o;
					if (!list.contains(value)) {
						list.add(Base.forUnknown(value));
					}
				} else {
					if (!o.equals(value)) {
						final MultipleList list = new MultipleList();
						list.add(o);
						list.add(Base.forUnknown(value));
						this.attributes.baseDefine(name, list);
					}
				}
			}
		}
		return (T) this;
	}

	@Override
	public final BaseObject getAttributes() {
		
		
		return this.attributes;
		/**
		 * <code>
		return this.attributes == null
				? this.attributes = new BaseNativeObject()
				: this.attributes;
		</code>
		 */
	}

	/**
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final BaseObject value) {
		
		
		if (value == null) {
			if (this.attributes != null) {
				this.attributes.baseDelete(name);
			}
		} else {
			if (this.attributes == null) {
				this.attributes = new BaseNativeObject("Date", Base.forDateMillis(Engine.fastTime()));
			}
			this.attributes.baseDefine(name, value);
		}
		return (T) this;
	}

	/**
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttribute(final String name, final Object value) {
		
		
		if (value == null || value == BaseObject.UNDEFINED) {
			if (this.attributes != null) {
				this.attributes.baseDelete(name);
			}
		} else {
			if (this.attributes == null) {
				this.attributes = new BaseNativeObject("Date", Base.forDateMillis(Engine.fastTime()));
			}
			this.attributes.baseDefine(name, Base.forUnknown(value));
		}
		return (T) this;
	}

	/**
	 *
	 * @param attributes
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T setAttributes(final BaseObject attributes) {
		
		
		if (attributes != null && !attributes.baseIsPrimitive()) {
			if (this.attributes == null) {
				this.attributes = new BaseNativeObject();
			}
			this.attributes.baseDefineImportAllEnumerable(attributes);
		}
		return (T) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public final T toEditable() {
		
		
		return (T) this;
	}

	/**
	 * @param attributes
	 * @return
	 */
	@Override
	@SuppressWarnings("unchecked")
	public final T useAttributes(final BaseObject attributes) {
		
		
		this.attributes = attributes == BaseObject.UNDEFINED
			? null
			: attributes;
		return (T) this;
	}
}
