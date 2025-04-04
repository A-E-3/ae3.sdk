/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.Evaluate.CompilationException;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArgumentA30IMM;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/** @author myx */
public final class TKV_ZTLOAD_A_Cs extends TokenValue implements ModifierArgument {

	private final BasePrimitiveString argumentB;

	/** @param name */
	public TKV_ZTLOAD_A_Cs(final BasePrimitiveString name) {

		this.argumentB = name;
	}

	@Override
	public boolean argumentHasSideEffects() {

		return true;
	}

	@Override
	public final String argumentNotation() {

		return "RT['" + this.argumentB + "']";
	}

	@Override
	public final BaseObject argumentRead(final ExecProcess process) {

		assert process.rb4CT != null : "'this' is not available in current context";
		return process.rb4CT.baseGet(this.argumentB, BaseObject.UNDEFINED);
	}

	@Override
	public final String getNotation() {

		return "this." + this.argumentB;
	}

	@Override
	public final String getNotationValue() {

		return this.getNotation();
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.OBJECT;
	}

	@Override
	public final boolean isAccessReference() {

		return true;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(this, 0, store));
	}

	@Override
	public final String toCode() {

		return "ACCESS\t2\tTC ->S\tCONST('" + this.argumentB + "');";
	}

	@Override
	public InstructionEditable toConditionalSkipEditable(final ProgramAssembly assembly, final int start, final TokenInstruction.ConditionType compare, final ResultHandler store) {

		final InstructionEditable editable = compare.createEditable(this, store);
		assembly.addInstruction(editable);
		return editable;
	}

	@Override
	public void toConditionalSkipSingleton(final ProgramAssembly assembly, final TokenInstruction.ConditionType compare, final int constant, final ResultHandler store) {

		assembly.addInstruction(compare.createSingleton(this, constant, store));
	}

	@Override
	public ModifierArgument toDirectModifier() {

		return this;
	}

	@Override
	public TokenInstruction toReferenceDelete() {

		return new TKV_ZTDELETE_A_Cs(this.argumentB);
	}

	@Override
	public TokenInstruction toReferenceObject() {

		return ParseConstants.TKV_THIS;
	}

	@Override
	public TokenInstruction toReferenceProperty() {

		return ParseConstants.getConstantValue(this.argumentB);
	}

	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directReadAllowed,
			final boolean directWriteFollows) {

		/** inlined<code>
		assembly.addInstruction( start, new InstructionA10( OperationsA10.LOAD,
				null,
				new ModifierArgumentAccessContext( this.argumentB ),
				0,
				ModifierStore.SN,
				ExecStateCode.NEXT ) );
		 </code> */
		assert argumentA == null;
		assert argumentB == null;
		return needRead
			? this
			: null;
	}

	@Override
	public void toReferenceWriteAfterRead(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument modifierValue,
			final boolean directWrite,
			final ResultHandler store) {

		assert argumentA == null;
		assert argumentB == null;
		assert modifierValue != null;
		assembly.addInstruction(
				OperationsS2X.VTSTORE_S.instruction(
						new ModifierArgumentA30IMM(this.argumentB), //
						modifierValue,
						0,
						store));
	}

	@Override
	public Instruction toReferenceWriteSkipAfterRead(//
			final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean directWrite,
			final ResultHandler store//
	) throws CompilationException {

		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		return null;
	}
}
