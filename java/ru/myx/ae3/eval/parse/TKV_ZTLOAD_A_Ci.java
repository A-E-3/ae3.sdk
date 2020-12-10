/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/** @author myx */
public final class TKV_ZTLOAD_A_Ci extends TokenValue implements ModifierArgument {

	private final BasePrimitiveString argumentB;

	private final int intB;

	/** @param name */
	public TKV_ZTLOAD_A_Ci(final BasePrimitiveNumber name) {
		
		assert name.baseIsPrimitiveInteger() : "Integers only!";
		this.argumentB = name.baseToString();
		this.intB = name.intValue();
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
		
		final BaseArray array = process.rb4CT.baseArray();
		return array != null
			? array.baseGet(this.intB, BaseObject.UNDEFINED)
			: process.rb4CT.baseGet(this.argumentB, BaseObject.UNDEFINED);
	}

	@Override
	public final String getNotation() {
		
		return "this[" + Ecma.toEcmaSourceCompact(this.argumentB) + "]";
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
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store)
			throws Evaluate.CompilationException {
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(this, 0, store));
	}

	@Override
	public void toBooleanConditionalSkip(final ProgramAssembly assembly, final boolean compare, final int constant, final ResultHandler store)
			throws Evaluate.CompilationException {
		
		assembly.addInstruction(
				(compare
					? OperationsA11.XESKIP1A_P
					: OperationsA11.XESKIP0A_P).instruction(this, constant, store));
	}

	@Override
	public InstructionEditable toBooleanConditionalSkip(final ProgramAssembly assembly, final int start, final boolean compare, final ResultHandler store)
			throws Evaluate.CompilationException {
		
		final InstructionEditable editable = (compare
			? OperationsA11.XESKIP1A_P
			: OperationsA11.XESKIP0A_P).instructionCreate(this, 0, store);
		assembly.addInstruction(editable);
		return editable;
	}

	@Override
	public final String toCode() {
		
		return "ACCESS\t2\tTC ->S\tCONST('" + this.argumentB + "');";
	}

	@Override
	public ModifierArgument toDirectModifier() {
		
		return this;
	}

	@Override
	public TokenInstruction toReferenceDelete() {
		
		return new TKV_ZTDELETE_A_Cb(this.argumentB);
	}

	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) {
		
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
			final ResultHandler store) throws Evaluate.CompilationException {
		
		assert argumentA == null;
		assert argumentB == null;
		assert modifierValue != null;
		assembly.addInstruction(OperationsA2X.ZTSTORE.instruction(this.argumentB, null, modifierValue, 0, store));
	}
}
