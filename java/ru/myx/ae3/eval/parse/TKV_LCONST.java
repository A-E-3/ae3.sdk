/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.ecma.Ecma;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

/** @author myx */
final class TKV_LCONST extends TokenValue implements ModifierArgument {

	private final BaseObject value;
	
	private final Object notation;
	
	InstructionResult resultType = null;
	
	/** @param value */
	TKV_LCONST(final BaseObject value) {
		
		assert value != null : "NULL java value";
		this.value = value;
		this.notation = value;
	}
	
	/** @param value
	 * @param notation */
	TKV_LCONST(final BaseObject value, final String notation) {
		
		assert value != null : "NULL java value";
		this.value = value;
		this.notation = notation;
	}
	
	/** CATCH METHOD
	 *
	 * @param value */
	@Deprecated
	TKV_LCONST(final BasePrimitiveNumber value) {
		
		assert false : "Use TKV_LCONSTN";
		this.value = value;
		this.notation = value;
	}
	
	/** CATCH METHOD
	 *
	 * @param value
	 * @param notation */
	@Deprecated
	TKV_LCONST(final BasePrimitiveNumber value, final String notation) {
		
		assert false : "Use TKV_LCONSTN";
		this.value = value;
		this.notation = notation;
	}
	
	/** CATCH METHOD
	 *
	 * @param value */
	@Deprecated
	TKV_LCONST(final BasePrimitiveString value) {
		
		assert false : "Use TKV_LCONSTS";
		this.value = value;
		this.notation = value;
	}
	
	/** CATCH METHOD
	 *
	 * @param value
	 * @param notation */
	@Deprecated
	TKV_LCONST(final BasePrimitiveString value, final String notation) {
		
		assert false : "Use TKV_LCONSTS";
		this.value = value;
		this.notation = notation;
	}
	
	@Override
	public final BaseObject argumentConstantValue() {

		return this.value;
	}
	
	@Override
	public boolean argumentHasSideEffects() {

		return false;
	}
	
	@Override
	public final String argumentNotation() {

		return Ecma.toEcmaSourceCompact(this.value);
	}
	
	@Override
	public final BaseObject argumentRead(final ExecProcess process) {

		return this.value;
	}
	
	@Override
	public final String getNotation() {

		return String.valueOf(this.notation);
	}
	
	@Override
	public final String getNotationValue() {

		return this.getNotation();
	}
	
	@Override
	public final InstructionResult getResultType() {

		if (this.resultType != null) {
			return this.resultType;
		}
		if (this.value.baseIsPrimitive()) {
			if (this.value == BaseObject.TRUE || this.value == BaseObject.FALSE) {
				return this.resultType = InstructionResult.BOOLEAN;
			}
			if (this.value == BaseObject.NULL) {
				return this.resultType = InstructionResult.NULL;
			}
			if (this.value == BaseObject.UNDEFINED) {
				return this.resultType = InstructionResult.UNDEFINED;
			}
			if (this.value.baseIsPrimitiveBoolean()) {
				return this.resultType = InstructionResult.BOOLEAN;
			}
			if (this.value.baseIsPrimitiveNumber()) {
				final double value = this.value.baseToNumber().doubleValue();
				if (value == (int) value) {
					return this.resultType = InstructionResult.INTEGER;
				}
				return this.resultType = InstructionResult.NUMBER;
			}
			if (this.value.baseIsPrimitiveString()) {
				return this.resultType = InstructionResult.STRING;
			}
		}
		if (this.value.baseArray() != null) {
			return this.resultType = InstructionResult.ARRAY;
		}
		return this.resultType = InstructionResult.OBJECT;
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

		return "LOAD\t1\tC  ->S\tCONST('" + this.value + "');";
	}
	
	@Override
	public final ModifierArgument toConstantModifier() {

		return this;
	}
	
	@Override
	public final BaseObject toConstantValue() {

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
