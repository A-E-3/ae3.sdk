package ru.myx.ae3.know;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.binary.TransferTarget;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.extra.External;
import ru.myx.ae3.produce.Reproducible;

/**
 * @author myx
 * 		
 */
public class GuidStream {
	
	private static final void toBinary(final BaseObject object, final TransferTarget target, final boolean detached) {
		
		{
			final Guid guid = Guid.forUnknown(object);
			if (guid != null) {
				/**
				 * Inline GUID - nothing to do anymore
				 */
				Guid.writeGuid(guid, target);
				return;
			}
		}
		if (object instanceof Reproducible) {
			final Reproducible reproducible = (Reproducible) object;
			
			final Guid factoryIdentity = Guid.forString(reproducible.restoreFactoryIdentity());
			assert factoryIdentity != null : "String: " + reproducible.restoreFactoryIdentity();
			
			final Guid factoryParameter = Guid.forString(reproducible.restoreFactoryParameter());
			assert factoryParameter != null : "String: " + reproducible.restoreFactoryParameter();
			
			Guid.writeGuid(Guid.GUID_STREAM_FACTORY, target);
			Guid.writeGuid(factoryIdentity, target);
			Guid.writeGuid(factoryParameter, target);
			
			return;
		}
		if (object instanceof External) {
			final External external = (External) object;
			
			if (detached) {
				final Object base = external.baseValue();
				if (base != object && base != null) {
					if (base instanceof BaseObject) {
						/**
						 * yes, there is a same check inside, but stack is one
						 * frame less this way.
						 */
						GuidStream.toBinary((BaseObject) base, target, detached);
						return;
					}
					GuidStream.toBinary(base, target, detached);
					return;
				}
			} else {
				final Guid guid = Guid.forString(external.getIdentity());
				assert guid != null : "String: " + external.getIdentity();
				Guid.writeGuid(Guid.GUID_STREAM_EXTERNAL, target);
				Guid.writeGuid(guid, target);
				return;
			}
		}
		{
			final BaseArray array = object.baseArray();
			if (array != null) {
				final int length = array.length();
				if (length == 0) {
					Guid.writeGuid(Guid.GUID_JS_ARRAY_EMPTY, target);
					return;
				}
				Guid.writeGuid(Guid.GUID_STREAM_ARRAY, target);
				for (int i = 0; i < length; ++i) {
					GuidStream.toBinary(array.baseGet(i, BaseObject.UNDEFINED), target, detached);
				}
				Guid.writeGuid(Guid.GUID_STREAM_STOP, target);
				return;
			}
		}
		if (object instanceof BaseMap || Base.hasKeys(object)) {
			/**
			 * TODO: add prototype!
			 */
			final Iterator<? extends BasePrimitive<?>> keys = object.baseKeysOwnPrimitive();
			if (!keys.hasNext()) {
				Guid.writeGuid(Guid.GUID_JS_OBJECT_EMPTY, target);
				return;
			}
			Guid.writeGuid(Guid.GUID_STREAM_OBJECT, target);
			do {
				final BasePrimitive<?> key = keys.next();
				GuidStream.toBinary(key, target, detached);
				GuidStream.toBinary(object.baseGet(key, BaseObject.UNDEFINED), target, detached);
			} while (keys.hasNext());
			Guid.writeGuid(Guid.GUID_STREAM_STOP, target);
			return;
		}
		{
			final Object base = object.baseValue();
			if (base != object && base != null) {
				if (base instanceof BaseObject) {
					/**
					 * yes, there is a same check inside, but stack is one frame
					 * less this way.
					 */
					GuidStream.toBinary((BaseObject) base, target, detached);
					return;
				}
				GuidStream.toBinary(base, target, detached);
				return;
			}
		}
		{
			assert false : "Incomplete: class: " + object.getClass().getName();
		}
	}
	
	private static final void toBinary(final Object object, final TransferTarget target, final boolean detached) {
		
		if (object instanceof BaseObject) {
			GuidStream.toBinary((BaseObject) object, target, detached);
			return;
		}
		{
			final Guid guid = Guid.forUnknown(object);
			if (guid != null) {
				/**
				 * Inline GUID - nothing to do anymore
				 */
				Guid.writeGuid(guid, target);
				return;
			}
		}
		if (object instanceof Reproducible) {
			final Reproducible reproducible = (Reproducible) object;
			
			final Guid factoryIdentity = Guid.forString(reproducible.restoreFactoryIdentity());
			assert factoryIdentity != null : "String: " + reproducible.restoreFactoryIdentity();
			
			final Guid factoryParameter = Guid.forString(reproducible.restoreFactoryParameter());
			assert factoryParameter != null : "String: " + reproducible.restoreFactoryParameter();
			
			Guid.writeGuid(Guid.GUID_STREAM_FACTORY, target);
			Guid.writeGuid(factoryIdentity, target);
			Guid.writeGuid(factoryParameter, target);
			
			return;
		}
		if (object instanceof External) {
			final External external = (External) object;
			
			if (detached) {
				final Object base = external.baseValue();
				if (base != object && base != null) {
					if (base instanceof BaseObject) {
						/**
						 * yes, there is a same check inside, but stack is one
						 * frame less this way.
						 */
						GuidStream.toBinary((BaseObject) base, target, detached);
						return;
					}
					GuidStream.toBinary(base, target, detached);
					return;
				}
			} else {
				final Guid guid = Guid.forString(external.getIdentity());
				assert guid != null : "String: " + external.getIdentity();
				Guid.writeGuid(Guid.GUID_STREAM_EXTERNAL, target);
				Guid.writeGuid(guid, target);
				return;
			}
		}
		if (object instanceof List<?>) {
			final List<?> array = (List<?>) object;
			final int length = array.size();
			if (length == 0) {
				Guid.writeGuid(Guid.GUID_JS_ARRAY_EMPTY, target);
				return;
			}
			Guid.writeGuid(Guid.GUID_STREAM_ARRAY, target);
			for (int i = 0; i < length; ++i) {
				GuidStream.toBinary(array.get(i), target, detached);
			}
			Guid.writeGuid(Guid.GUID_STREAM_STOP, target);
			return;
		}
		if (object instanceof Object[]) {
			final Object[] array = (Object[]) object;
			final int length = array.length;
			if (length == 0) {
				Guid.writeGuid(Guid.GUID_JS_ARRAY_EMPTY, target);
				return;
			}
			Guid.writeGuid(Guid.GUID_STREAM_ARRAY, target);
			for (int i = 0; i < length; ++i) {
				GuidStream.toBinary(array[i], target, detached);
			}
			Guid.writeGuid(Guid.GUID_STREAM_STOP, target);
			return;
		}
		if (object instanceof Map<?, ?>) {
			@SuppressWarnings("unchecked")
			final Map<Object, Object> map = (Map<Object, Object>) object;
			final Set<Map.Entry<Object, Object>> entrySet = map.entrySet();
			if (entrySet.isEmpty()) {
				Guid.writeGuid(Guid.GUID_JS_OBJECT_EMPTY, target);
				return;
			}
			Guid.writeGuid(Guid.GUID_STREAM_OBJECT, target);
			for (final Map.Entry<Object, Object> entry : entrySet) {
				final Object key = entry.getKey();
				GuidStream.toBinary(key, target, detached);
				GuidStream.toBinary(map.get(key), target, detached);
			}
			Guid.writeGuid(Guid.GUID_STREAM_STOP, target);
			return;
		}
		if (object instanceof Value<?>) {
			final Object base = ((Value<?>) object).baseValue();
			if (base != object && base != null) {
				if (base instanceof BaseObject) {
					/**
					 * yes, there is a same check inside, but stack is one frame
					 * less this way.
					 */
					GuidStream.toBinary((BaseObject) base, target, detached);
					return;
				}
				GuidStream.toBinary(base, target, detached);
				return;
			}
		}
		{
			assert false : "Incomplete: class: " + object.getClass().getName();
		}
	}
	
	/**
	 * Calculates GUID reflecting current state (value represented) on an object
	 * given.
	 * 
	 * @param object
	 * @param detached
	 * @return
	 */
	public static final Guid toUniqueIdentifier(final BaseObject object, final boolean detached) {
		
		assert object != null : "NULL java object!";
		{
			final Guid guid = Guid.forUnknown(object);
			if (guid != null) {
				/**
				 * Inline GUID - nothing to do anymore
				 */
				return guid;
			}
		}
		{
			final WhirlpoolTarget target = new WhirlpoolTarget();
			GuidStream.toBinary(object, target, detached);
			return new Guid(GuidType.createCRC(target.getDigest(), target.getLength()));
		}
	}
}
