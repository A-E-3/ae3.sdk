/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
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
public final class TKV_FLOAD_A_Cs_S extends TokenValue implements ModifierArgument {

	/** @param key
	 * @param keywords
	 * @return */
	public static final TokenValue getInstance(final BasePrimitiveString key, final Map<String, TokenValue> keywords) {

		final TokenValue known = keywords.get(key.toString());
		return known != null
			? known
			: new TKV_FLOAD_A_Cs_S(key);
	}
	
	/** @param key
	 * @param keywords
	 * @return */
	public static final TokenValue getInstance(final String key, final Map<String, TokenValue> keywords) {

		final TokenValue known = keywords.get(key);
		return known != null
			? known
			: new TKV_FLOAD_A_Cs_S(Base.forString(key));
	}
	
	private final BasePrimitiveString argumentA;
	
	/** @param name */
	TKV_FLOAD_A_Cs_S(final BasePrimitiveString name) {
		
		this.argumentA = name;
	}
	
	@Override
	public final BasePrimitiveString argumentAccessFramePropertyName() {

		return this.argumentA;
	}
	
	@Override
	public boolean argumentHasSideEffects() {

		return false;
	}
	
	@Override
	public final String argumentNotation() {

		return "FV['" + this.argumentA + "']";
	}
	
	@Override
	public final BaseObject argumentRead(final ExecProcess process) {

		return process.contextGetBindingValue(this.argumentA, false);
	}
	
	@Override
	public final String getNotation() {

		return this.argumentA.toString();
	}
	
	@Override
	public final String getNotationValue() {

		return this.argumentA.toString();
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

		return "ACCESS\t2\tFC ->S\tCONST('" + this.argumentA + "');";
	}
	
	@Override
	public BasePrimitiveString toContextPropertyName() {

		return this.argumentA;
	}

	@Override
	public String toCreatePropertyName() {

		return this.argumentA.toString();
	}
	
	@Override
	public ModifierArgument toDirectModifier() {

		return this;
	}
	
	@Override
	public TokenInstruction toReferenceDelete() {

		return new TKV_DELETE_BA_CF_S(this.argumentA.toString());
	}
	
	@Override
	public TokenInstruction toReferenceObject() {

		assert false : "Can't do this!";
		return null;
	}
	
	@Override
	public TokenInstruction toReferenceProperty() {

		/** same FLOAD (cause frame access needs source, not a property name) */
		return this;
	}
	
	@Override
	public ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) {

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
			final ResultHandler store) {

		assert argumentA == null;
		assert argumentB == null;
		assert modifierValue != null;
		assembly.addInstruction(OperationsA2X.XFSTORE_D.instruction(this.argumentA, null, modifierValue, 0, store));
	}
}
