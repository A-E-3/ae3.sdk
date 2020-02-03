/**
 *
 */
package ru.myx.renderer.ecma;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.exec.ExecCallableBoth;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ResultHandler;

/** @author myx */
final class ModifierArgumentFunctionImport extends BaseFunctionAbstract implements ExecCallableBoth.ExecStore1, ModifierArgument {
	
	/**
	 *
	 */
	public static final BasePrimitiveString STR_IMPORT = Base.forString("import");

	/**
	 *
	 */
	public static final ModifierArgumentFunctionImport INSTANCE = new ModifierArgumentFunctionImport();
	
	private ModifierArgumentFunctionImport() {

		// prevent
	}
	
	@Override
	public final BasePrimitiveString argumentAccessFramePropertyName() {
		
		return ModifierArgumentFunctionImport.STR_IMPORT;
	}
	
	@Override
	public boolean argumentHasSideEffects() {
		
		return false;
	}
	
	@Override
	public final String argumentNotation() {
		
		return "SYSCALL::import";
	}
	
	@Override
	public final BaseObject argumentRead(final ExecProcess process) {
		
		return this;
	}
	
	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseObject x) {
		
		final Class<?> cls;
		try {
			cls = Class.forName(x.baseToJavaString());
		} catch (final ClassNotFoundException e) {
			return ctx.vmRaise(e);
		}
		// final BaseObject reflected = new BaseReflectedClass(cls);
		final BaseObject reflected = Base.forUnknown(cls);
		ctx.contextCreateMutableBinding(cls.getSimpleName(), reflected, false);
		return store.execReturn(ctx, reflected);
	}

	@Override
	public final boolean execIsConstant() {
		
		return false;
	}

	@Override
	public Class<? extends BaseObject> execResultClassJava() {
		
		return BaseObject.class;
	}

	@Override
	public BaseObject execScope() {
		
		/** executes in real current scope */
		return ExecProcess.GLOBAL;
	}

	@Override
	public final String toString() {

		return "SYSCALL::import";
	}
}
