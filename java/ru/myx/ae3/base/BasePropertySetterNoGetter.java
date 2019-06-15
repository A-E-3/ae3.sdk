package ru.myx.ae3.base;

import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionDisable;

/** @author myx */
@ReflectionDisable
public class BasePropertySetterNoGetter implements BaseProperty, BaseObjectNominal {
	
	/** PROCEDURAL | WRITABLE | INSTANCE */
	public static final short ATTRIBUTES = BaseProperty.ATTR_PROCEDURAL_SET | BaseProperty.ATTR_WRITABLE | BaseProperty.ATTR_INSTANCE;
	
	private final BaseFunction setter;
	
	/** @param setter
	 */
	public BasePropertySetterNoGetter(final BaseFunction setter) {
		
		this.setter = setter;
	}
	
	@Override
	public short propertyAttributes(final CharSequence name) {
		
		return BasePropertySetterNoGetter.ATTRIBUTES;
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final BasePrimitiveString name) {
		
		return BaseObject.UNDEFINED;
	}
	
	@Override
	public BaseObject propertyGet(final BaseObject instance, final String name) {
		
		return BaseObject.UNDEFINED;
	}
	
	@Override
	public BaseObject propertyGetAndSet(final BaseObject instance, final String name, final BaseObject value) {
		
		this.setter.callVJ1(instance, value);
		return BaseObject.UNDEFINED;
	}
	
	@Override
	public ExecStateCode propertyGetCtxResult(final ExecProcess ctx, final BaseObject instance, final BasePrimitive<?> name, final ResultHandler store) {
		
		return store.execReturnUndefined(ctx);
	}
	
	@Override
	public boolean propertySet(final BaseObject instance, final CharSequence name, final BaseObject value, final short attributes) {
		
		this.setter.callVJ1(instance, value);
		return true;
	}
	
}
