/*
 * Created on 11.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.IAVV_ARGS_SETUP;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.help.Text;
import ru.myx.renderer.ecma.AcmEcmaLanguageImpl;

/**
 * @author myx
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
final class TKV_CFUNCTION extends TokenValue {
	
	private static final BasePrimitiveString[] EMPTY_ARGUMENTS = new BasePrimitiveString[0];

	private ProgramPart code;

	private final String name;

	private final String[] arguments;

	private final String body;

	/**
	 * title is like: "function(" + Text.join( this.arguments, ", " ) + ")
	 *
	 * @param name
	 * @param arguments
	 * @param body
	 */
	public TKV_CFUNCTION(final String name, final String[] arguments, final String body) {
		this.name = name;
		this.arguments = arguments;
		this.body = body.trim();
		this.code = null;
	}

	@Override
	public final String getNotation() {
		
		return (this.name == null
			? "function("
			: "function " + this.name + '(') + Text.join(this.arguments, ", ") + ") { " //
				+ "..." + this.body.split("\n").length + " lines" + "..." + " }";
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/**
		 * zero operands
		 */
		assert argumentA == null;
		assert argumentB == null;

		/**
		 * valid store
		 */
		assert store != null;

		final boolean block = this.name == null && this.arguments == null;
		if (this.code == null) {
			final int codeStart = assembly.size();
			if (block) {
				Evaluate.compileProgramBlock(AcmEcmaLanguageImpl.INSTANCE, this.getNotation(), this.body, assembly);
			} else {
				assembly.addInstruction(this.arguments == null
					? new IAVV_ARGS_SETUP(this.name, TKV_CFUNCTION.EMPTY_ARGUMENTS)
					: new IAVV_ARGS_SETUP(this.name, this.arguments));
				Evaluate.compileProgramInline(AcmEcmaLanguageImpl.INSTANCE, this.getNotation(), this.body, assembly);
			}
			this.code = assembly.toProgram(codeStart);

		}

		final int codeLength = this.code.length();
		if (codeLength == 0) {
			assembly.addInstruction(OperationsA11.XCFUNCTION_N.instruction(
					new ModifierArgumentA30IMM(this.arguments), //
					1,
					store));
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
			return;
		}

		assembly.addInstruction(OperationsA11.XCFUNCTION_N.instruction(
				new ModifierArgumentA30IMM(this.arguments), //
				codeLength + 1,
				store));
		assembly.addInstruction(this.code);
		assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
	}

	@Override
	public String toCode() {
		
		return "CFUNCTION [" + Text.join(this.arguments, ", ") + ", " + this.code + "];";
	}
}
