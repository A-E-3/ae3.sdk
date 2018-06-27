package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.renderer.ecma.AcmEcmaLanguageImpl;

/**
 * @author myx
 *
 */
public final class ExecFunctionImpl extends BaseFunctionAbstract implements ExecCallableStepper {
	
	
	private final BaseObject scope;

	private final String[] arguments;

	private final Instruction[] code;

	private final int start;

	private final int stop;

	ExecFunctionImpl(final BaseObject scope, final String[] arguments, final Instruction[] code, final int start, final int stop) {
		this.scope = scope;
		this.arguments = arguments;
		this.code = code;
		this.start = start;
		this.stop = stop;
	}

	/**
	 * @param scope
	 * @param context
	 * @param arguments
	 * @param ecmaSourceCode
	 * @throws Exception
	 */
	public ExecFunctionImpl(final BaseObject scope, final String context, final String[] arguments, final String ecmaSourceCode) throws Exception {
		
		final ProgramAssembly assembly = new ProgramAssembly();
		if (arguments != null && arguments.length > 0) {
			assembly.addInstruction(new IAVV_ARGS_SETUP(null, arguments));
			Evaluate.compileProgramInline(AcmEcmaLanguageImpl.INSTANCE, context, ecmaSourceCode, assembly);
		} else {
			Evaluate.compileProgramBlock(AcmEcmaLanguageImpl.INSTANCE, context, ecmaSourceCode, assembly);
		}

		/**
		 * TODO: need Exec.getGlobalProcess() - for acm domains
		 */
		this.scope = BaseMap.create(scope);

		this.arguments = arguments;
		final ProgramPart program = assembly.toProgram(0);
		// System.err.println(">>>>>>>>> " + context + " " +
		// Arrays.asList(arguments) + " " + program);
		this.code = program.getInstructions();
		this.start = 0;
		this.stop = this.code.length;
	}

	@Override
	public final int execArgumentsAcceptable() {
		
		
		return Integer.MAX_VALUE;
	}

	@Override
	public final int execArgumentsDeclared() {
		
		
		return this.arguments == null
			? 0
			: this.arguments.length;
	}

	@Override
	public final int execArgumentsMinimal() {
		
		
		return 0;
	}

	@Override
	public int execGetCodeStart() {
		
		
		return this.start;
	}

	@Override
	public int execGetCodeStop() {
		
		
		return this.stop;
	}

	@Override
	public Instruction[] execGetCode() {
		
		
		return this.code;
	}

	@Override
	public void execSetupContext(final ExecProcess ctx) {
		
		
		ctx.fldCode = this.code;
		ctx.ri0FI0 = ctx.ri08IP = this.start;
		ctx.ri09IL = this.stop;
	}

	@Override
	public BaseObject execScope() {
		
		
		return this.scope;
	}

	@Override
	public String[] execFormalParameters() {
		
		
		return this.arguments;
	}

	@Override
	public final String toString() {
		
		
		final String[] arguments = this.arguments;
		if (arguments == null || arguments.length == 0) {
			return "function()";
		}
		final StringBuilder builder = new StringBuilder(80);
		builder.append("function( ");
		builder.append(arguments[0]);
		for (int i = 1; i < arguments.length; ++i) {
			builder.append(", ");
			builder.append(arguments[i]);
		}
		builder.append(" )");
		return builder.toString();
	}
}
