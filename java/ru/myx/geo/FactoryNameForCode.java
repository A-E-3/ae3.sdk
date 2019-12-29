package ru.myx.geo;

import java.util.function.Function;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.produce.ObjectFactory;

/*
 * Created on 19.12.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/** @author myx
 *
 *         To change the template for this generated type comment go to Window>Preferences>Java>Code
 *         Generation>Code and Comments */
final class FactoryNameForCode implements ObjectFactory<Object, Function<String, String>> {

	private static final Class<?>[] TARGETS = {
			Function.class
	};

	private static final String[] VARIETY = {
			"GEOGRAPHY/COUNTRY_NAME_FOR_CODE"
	};

	private static final Function<String, String> FN_NAME_FOR_CODE = new Function<>() {

		@Override
		public String apply(String argument) {

			return Geography.getCountryNameForCode(argument);
		}
	};

	@Override
	public final boolean accepts(final String variant, final BaseObject attributes, final Class<?> source) {
		
		return true;
	}

	@Override
	public final Function<String, String> produce(final String variant, final BaseObject attributes, final Object source) {
		
		return FactoryNameForCode.FN_NAME_FOR_CODE;
	}

	@Override
	public final Class<?>[] sources() {
		
		return null;
	}

	@Override
	public final Class<?>[] targets() {
		
		return FactoryNameForCode.TARGETS;
	}

	@Override
	public final String[] variety() {
		
		return FactoryNameForCode.VARIETY;
	}

}
