/*
 * Created on 13.12.2005
 */
package ru.myx.ae3.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostLookup;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;

/** @author myx
 *
 * @param <E> */
public final class ControlLookupEnum<E extends Enum<E>> extends BaseHostLookup {
	
	private final E[] universe;

	/** @param enumeration */
	public ControlLookupEnum(final Class<E> enumeration) {

		this.universe = enumeration.getEnumConstants();
	}

	@Override
	public final BaseObject baseGetLookupValue(final BaseObject key) {
		
		for (final E element : this.universe) {
			if (element == key || element == key.baseValue()) {
				return Base.forUnknown(element);
			}
		}
		for (final E element : this.universe) {
			if (key.baseToJavaString().equals(element.name())) {
				return Base.forUnknown(element);
			}
		}
		return null;
	}

	@Override
	public boolean baseHasKeysOwn() {
		
		return this.universe.length > 0;
	}

	@Override
	public final Iterator<String> baseKeysOwn() {
		
		final List<String> keys = new ArrayList<>();
		for (final E element : this.universe) {
			keys.add(element.name());
		}
		return keys.iterator();
	}

	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		return this.baseKeysOwn();
	}

	@Override
	public final Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		final List<BasePrimitive<?>> keys = new ArrayList<>();
		for (final E element : this.universe) {
			keys.add(Base.forString(element.name()));
		}
		return keys.iterator();
	}

	@Override
	public String toString() {
		
		return "[Lookup: Java Enum: " + this.universe[0].getClass().getName() + "]";
	}
}
