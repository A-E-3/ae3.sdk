package ru.myx.ae3.base;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionDisable;

/** @author myx */
@ReflectionDisable
public class BasePropertyGetterNoSetter implements BaseProperty, BaseObjectNominal {

	/** PROCEDURAL | INSTANCE */
	public static final short ATTRIBUTES = BaseProperty.ATTR_PROCEDURAL_GET | BaseProperty.ATTR_INSTANCE;
	
	private final BaseFunction getter;
	
	/** @param getter
	 */
	public BasePropertyGetterNoSetter(final BaseFunction getter) {
		
		this.getter = getter;
	}
	
	@Override
	public short propertyAttributes(final CharSequence name) {

		return BasePropertyGetterNoSetter.ATTRIBUTES;
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString name) {

		return this.getter.callNJ0(instance);
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {

		return this.getter.callNJ0(instance);
	}
	
	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {

		return this.getter.callNJ0(instance);
	}
	
	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {

		return this.getter.execCallPrepare(ctx, instance, store, false);
	}
	
}
