/**
 *
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveBoolean;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.common.FutureValue;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class ModifierArgumentFunctionTypeof extends BaseFunctionAbstract implements ExecCallableBoth.ExecStore1, ModifierArgument {
	
	/**
	 *
	 */
	public static final BasePrimitiveString STR_TYPEOF = Base.forString("typeof");
	
	/**
	 *
	 */
	public static final ModifierArgumentFunctionTypeof INSTANCE = new ModifierArgumentFunctionTypeof();

	private ModifierArgumentFunctionTypeof() {
		
		// prevent
	}

	@Override
	public final BasePrimitiveString argumentAccessFramePropertyName() {
		
		return ModifierArgumentFunctionTypeof.STR_TYPEOF;
	}
	
	@Override
	public boolean argumentHasSideEffects() {
		
		return false;
	}
	
	@Override
	public final String argumentNotation() {
		
		return "SYSCALL::typeof";
	}
	
	@Override
	public final BaseObject argumentRead(final ExecProcess process) {
		
		return this;
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseObject x) {

		{
			if (x.baseIsPrimitive()) {
				if (x == BaseObject.NULL) {
					return store.execReturn(
							ctx, //
							BaseString.STR_NULL);
				}
				if (x == BaseObject.UNDEFINED) {
					return store.execReturn(
							ctx, //
							BaseString.STR_UNDEFINED);
				}
				if (x instanceof BasePrimitiveString) {
					return store.execReturn(
							ctx, //
							BaseString.STR_STRING);
				}
				if (x instanceof BasePrimitiveBoolean) {
					return store.execReturn(
							ctx, //
							BaseString.STR_BOOLEAN);
				}
				if (x instanceof BasePrimitiveNumber) {
					return store.execReturn(
							ctx, //
							BaseString.STR_NUMBER);
				}
				{
					return store.execReturnString(
							ctx, //
							x.baseClass());
				}
			}

			if (x.baseCall() != null) {
				return store.execReturn(
						ctx, //
						BaseString.STR_FUNCTION);
			}
		}

		if (x instanceof FutureValue<?>) {
			final Object j = ((FutureValue<?>) x).baseValue();
			if (j == null) {
				return store.execReturn(
						ctx, //
						BaseString.STR_NULL);
			}
			final BaseObject b = Base.forUnknown(j);
			if (b.baseIsPrimitive()) {
				return store.execReturnString(
						ctx, //
						b.baseClass());
			}

			if (b.baseCall() != null) {
				return store.execReturn(
						ctx, //
						BaseString.STR_FUNCTION);
			}
		}

		return store.execReturn(
				ctx, //
				BaseString.STR_OBJECT);
	}

	@Override
	public final boolean execIsConstant() {

		return true;
	}

	@Override
	public Class<? extends BasePrimitiveString> execResultClassJava() {

		return BasePrimitiveString.class;
	}
	
	@Override
	public final String toString() {
		
		return "SYSCALL::typeof";
	}

}
