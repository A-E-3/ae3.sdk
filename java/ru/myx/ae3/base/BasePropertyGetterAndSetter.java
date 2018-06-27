package ru.myx.ae3.base;

import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionDisable;

/**
 *
 * @author myx
 *
 */
@ReflectionDisable
public class BasePropertyGetterAndSetter implements BaseProperty, BaseObjectNominal {
	
	/**
	 * INSTANCE | WRITABLE | PROCEDURAL
	 */
	public static final short ATTRIBUTES = BaseProperty.ATTR_PROCEDURAL | BaseProperty.ATTR_WRITABLE | BaseProperty.ATTR_INSTANCE;
	
	private final BaseFunction getter;
	
	private final BaseFunction setter;
	
	/**
	 *
	 * @param getter
	 * @param setter
	 */
	public BasePropertyGetterAndSetter(final BaseFunction getter, final BaseFunction setter) {
		this.getter = getter;
		this.setter = setter;
	}
	
	@Override
	public short propertyAttributes(final CharSequence name) {
		
		return BasePropertyGetterAndSetter.ATTRIBUTES;
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
		
		final ExecProcess ctx = Exec.currentProcess();
		final BaseObject previous = this.getter.callNE0(ctx, instance);
		this.setter.callVE1(ctx, instance, value);
		return previous;
	}
	
	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {
		
		return this.getter.execCallPrepare(ctx, instance, store, false);
	}
	
	@Override
	public boolean propertySet(final BaseObject instance, final CharSequence name, final BaseObject value, final short attributes) {
		
		this.setter.callVJ1(instance, value);
		return true;
	}
	
}
