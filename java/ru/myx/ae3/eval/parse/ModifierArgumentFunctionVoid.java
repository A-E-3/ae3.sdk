/**
 *
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BasePrimitiveUndefined;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class ModifierArgumentFunctionVoid extends BaseFunctionAbstract implements ExecCallableBoth.ExecStore1, ModifierArgument {
	
	/**
	 *
	 */
	public static final BasePrimitiveString STR_VOID = Base.forString("void");
	
	/**
	 *
	 */
	public static final ModifierArgumentFunctionVoid INSTANCE = new ModifierArgumentFunctionVoid();

	private ModifierArgumentFunctionVoid() {
		
		// prevent
	}

	@Override
	public final BasePrimitiveString argumentAccessFramePropertyName() {
		
		return ModifierArgumentFunctionVoid.STR_VOID;
	}
	
	@Override
	public boolean argumentHasSideEffects() {
		
		return false;
	}
	
	@Override
	public final String argumentNotation() {
		
		return "SYSCALL::void";
	}
	
	@Override
	public final BaseObject argumentRead(final ExecProcess process) {
		
		return this;
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseObject x) {

		return store.execReturnUndefined(ctx);
	}

	@Override
	public final boolean execIsConstant() {

		return true;
	}

	@Override
	public Class<? extends BasePrimitiveUndefined> execResultClassJava() {

		return BasePrimitiveUndefined.class;
	}
	
	@Override
	public final String toString() {
		
		return "SYSCALL::void";
	}

}
