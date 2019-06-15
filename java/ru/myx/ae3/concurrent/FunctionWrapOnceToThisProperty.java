package ru.myx.ae3.concurrent;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseEditable;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.exec.ExecCallable;
import ru.myx.ae3.exec.ExecCallableJava;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;

/** GETTER for 'once' property
 *
 * @author myx */
@ReflectionManual
public final class FunctionWrapOnceToThisProperty extends BaseFunctionAbstract implements ExecCallableJava.NativeJ0, ExecCallable.ForStore.UseStore0 {
	
	private final BasePrimitiveString propertyName;
	private final BaseFunction function;
	
	private final BaseObject checkEquals;
	private final BaseObject checkNotEquals;
	
	/** @param propertyName
	 * @param function
	 * @param checkEquals
	 * @param checkNotEquals
	 */
	@ReflectionExplicit
	public FunctionWrapOnceToThisProperty(final BasePrimitiveString propertyName, final BaseFunction function, final BaseObject checkEquals, final BaseObject checkNotEquals) {
		
		if (function == null) {
			throw new IllegalArgumentException("Function is NULL!");
		}
		if (function.execArgumentsDeclared() != 0) {
			throw new IllegalArgumentException("Function should have no arguments declared, use '.bind' to provide static arguments!");
		}
		this.propertyName = propertyName;
		this.function = function;
		this.checkEquals = checkEquals;
		this.checkNotEquals = checkNotEquals;
	}
	
	@Override
	public BaseArray baseArray() {
		
		return this.function.baseArray();
	}
	
	@Override
	public String baseClass() {
		
		return this.function.baseClass();
	}
	
	@Override
	public void baseClear() {
		
		this.function.baseClear();
	}
	
	@Override
	public BaseFunction baseConstruct() {
		
		return this.function;
	}
	
	//
	
	@Override
	public BaseObject baseConstructPrototype() {
		
		return this.function.baseConstructPrototype();
	}
	
	@Override
	public BaseObject callNE0(final ExecProcess ctx, final BaseObject instance) {
		
		final BaseObject result = this.function.callNE0(ctx, instance);
		if (this.checkEquals == null || this.checkEquals == instance) {
			if (this.checkNotEquals == null || this.checkNotEquals != instance) {
				if (instance instanceof BaseEditable) {
					((BaseEditable) instance).setOwnPropertyValue(this.propertyName, result, BaseProperty.ATTRS_MASK_NNN);
				} else {
					instance.baseDefine(this.propertyName, result, BaseProperty.ATTRS_MASK_NND);
				}
			}
		}
		return result;
	}
	
	@Override
	public BaseObject callNJ0(final BaseObject instance) {
		
		final BaseObject result = this.function.callNJ0(instance);
		if (this.checkEquals == null || this.checkEquals == instance) {
			if (this.checkNotEquals == null || this.checkNotEquals != instance) {
				if (instance instanceof BaseEditable) {
					((BaseEditable) instance).setOwnPropertyValue(this.propertyName, result, BaseProperty.ATTRS_MASK_NNN);
				} else {
					instance.baseDefine(this.propertyName, result, BaseProperty.ATTRS_MASK_NND);
				}
			}
		}
		return result;
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline) {
		
		final ExecStateCode code = this.function.execCallPrepare(ctx, instance, ResultHandler.FA_BNN_NXT, true);
		if (code == null) {
			if (this.checkEquals == null || this.checkEquals == instance) {
				if (this.checkNotEquals == null || this.checkNotEquals != instance) {
					if (instance instanceof BaseEditable) {
						((BaseEditable) instance).setOwnPropertyValue(this.propertyName, ctx.ra0RB, BaseProperty.ATTRS_MASK_NNN);
					} else {
						instance.baseDefine(this.propertyName, ctx.ra0RB, BaseProperty.ATTRS_MASK_NND);
					}
				}
			}
			return store.execReturn(ctx, ctx.ra0RB);
		}
		return code;
	}
	
	@Override
	public boolean execIsConstant() {
		
		return false;
	}
	
	@Override
	public Class<? extends Object> execResultClassJava() {
		
		return this.function.execResultClassJava();
	}
	
	@Override
	public BaseObject execScope() {
		
		return this.function.execScope();
	}
	
	@Override
	public String toString() {
		
		return "[function " + this.getClass().getSimpleName() + '(' + this.propertyName + ", " + this.function + ")]";
	}
}
