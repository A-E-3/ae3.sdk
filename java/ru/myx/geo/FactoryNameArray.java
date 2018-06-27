package ru.myx.geo;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.produce.ObjectFactory;

/*
 * Created on 19.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
final class FactoryNameArray implements ObjectFactory<Object, String[]> {

	private static final Class<?>[] TARGETS = {
			String[].class
	};

	private static final String[] VARIETY = {
			"GEOGRAPHY/COUNTRY_NAMES"
	};

	@Override
	public final boolean accepts(final String variant, final BaseObject attributes, final Class<?> source) {
		
		return true;
	}

	@Override
	public final String[] produce(final String variant, final BaseObject attributes, final Object source) {
		
		return Geography.getCountryNames();
	}

	@Override
	public final Class<?>[] sources() {
		
		return null;
	}

	@Override
	public final Class<?>[] targets() {
		
		return FactoryNameArray.TARGETS;
	}

	@Override
	public final String[] variety() {
		
		return FactoryNameArray.VARIETY;
	}

}
