/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenValue;

/** @author myx */
final class TKV_LCONSTI extends TokenValue implements ModifierArgument {

	private final BasePrimitiveNumber value;

	/** @param value */
	TKV_LCONSTI(final BasePrimitiveNumber value) {
		
		assert value != null : "NULL java value";
		assert value.baseIsPrimitiveInteger();
		this.value = value;
	}

	@Override
	public final BasePrimitiveNumber argumentConstantValue() {

		return this.value;
	}

	@Override
	public double argumentDouble(final ExecProcess process) {

		return this.value.doubleValue();
	}

	@Override
	public boolean argumentHasSideEffects() {

		return false;
	}

	@Override
	public int argumentInt32(final ExecProcess process) {

		return this.value.baseToJavaInteger();
	}
	
	@Override
	public long argumentLong(final ExecProcess process) {

		return this.value.baseToJavaLong();
	}
	
	@Override
	public final String argumentNotation() {

		return Ecma.toEcmaSourceCompact(this.value);
	}
	
	@Override
	public final BasePrimitiveNumber argumentRead(final ExecProcess process) {

		return this.value;
	}
	
	@Override
	public CharSequence argumentString(final ExecProcess process) {

		return this.value.stringValue();
	}
	
	@Override
	public final String getNotation() {

		return this.value.toString();
	}

	@Override
	public final String getNotationValue() {

		return this.getNotation();
	}

	@Override
	public final InstructionResult getResultType() {

		return InstructionResult.INTEGER;
	}

	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {

		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;

		/** valid store */
		assert store != null;

		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(this.value, this, 0, store));
	}

	@Override
	public void toBooleanConditionalSkip(final ProgramAssembly assembly, final boolean compare, final int constant, final ResultHandler store) {

		assembly.addInstruction(
				(compare
					? OperationsA11.XESKIP1A_P
					: OperationsA11.XESKIP0A_P).instruction(this, constant, store));
	}

	@Override
	public InstructionEditable
			toBooleanConditionalSkip(final ProgramAssembly assembly, final int start, final boolean compare, final ResultHandler store) {

		final InstructionEditable editable = (compare
			? OperationsA11.XESKIP1A_P
			: OperationsA11.XESKIP0A_P).instructionCreate(this, 0, store);
		assembly.addInstruction(editable);
		return editable;
	}

	@Override
	public final String toCode() {

		return "LOADN\t1\tC  ->S\tCONST('" + this.value + "');";
	}

	@Override
	public final ModifierArgument toConstantModifier() {

		return this;
	}

	@Override
	public final BasePrimitiveNumber toConstantValue() {

		return this.value;
	}

	@Override
	public String toCreatePropertyName() {

		return this.value.baseToJavaString();
	}

	@Override
	public ModifierArgument toDirectModifier() {

		return this;
	}
}
