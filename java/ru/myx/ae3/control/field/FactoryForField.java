/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.control.field;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.produce.ObjectFactory;

final class FactoryForField implements ObjectFactory<Object, ControlField> {
	
	private static final Class<?>[] TARGETS = {
			ControlField.class
	};

	private final ControlField field;

	private final String[] variety;

	/**
	 * @param factoryIdentity
	 * @param field
	 */
	FactoryForField(final String factoryIdentity, final ControlField field) {
		this.field = field;
		this.variety = new String[]{
				factoryIdentity
		};
	}

	@Override
	public final boolean accepts(final String variant, final BaseObject attributes, final Class<?> source) {
		
		return true;
	}

	@Override
	public final ControlField produce(final String variant, final BaseObject attributes, final Object source) {
		
		return this.field.cloneField().setAttributes(attributes);
	}

	@Override
	public final Class<?>[] sources() {
		
		return null;
	}

	@Override
	public final Class<?>[] targets() {
		
		return FactoryForField.TARGETS;
	}

	@Override
	public final String[] variety() {
		
		return this.variety;
	}
}
