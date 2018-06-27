package ru.myx.ae3.eval.parse;

import static ru.myx.ae3.exec.ModifierArguments.AA0RB;
import static ru.myx.ae3.exec.ModifierArguments.AE21POP;
import static ru.myx.ae3.exec.ModifierArguments.AE22PEEK;

import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.OperationsS3X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenValue;
import ru.myx.ae3.help.Format;

final class TKV_COBJECT_LITERAL extends TokenValue {
	
	BasePrimitiveString[] propertyNames;
	TokenInstruction[] propertyValues;

	TKV_COBJECT_LITERAL(final BasePrimitiveString[] propertyNames, final TokenInstruction[] propertyValues) {
		assert propertyNames != null : "NULL property names";
		assert propertyValues != null : "NULL property values";
		assert propertyNames.length > 0 : "Invalid property count: " + propertyNames.length;
		assert propertyNames.length == propertyValues.length : "Property count mismatch: " + propertyNames.length + ", " + propertyValues.length;

		this.propertyNames = propertyNames;
		this.propertyValues = propertyValues;
	}

	@Override
	public final String getNotation() {
		
		final StringBuilder builder = new StringBuilder();
		builder.append('{');

		final BasePrimitiveString[] names = this.propertyNames;
		final TokenInstruction[] values = this.propertyValues;
		final int length = names.length;
		for (int i = 0; i < length; ++i) {
			if (i > 0) {
				builder.append(',');
			}

			Format.Ecma.string(builder, names[i]);
			builder.append(values[i].getNotation());
		}

		builder.append('}');

		return builder.toString();
	}

	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}

	@Override
	public final boolean toPreferStackResult() {
		
		return true;
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

		assembly.addInstruction(Instructions.INSTR_COBJECT_0_0_SN_NEXT);

		final BasePrimitiveString[] names = this.propertyNames;
		final TokenInstruction[] values = this.propertyValues;
		final int length = names.length;
		for (int i = 0; i < length; ++i) {
			final TokenInstruction value = values[i];
			final ModifierArgument argumentC = value.toDirectModifier();
			final boolean directC = argumentC == AA0RB;
			if (directC) {
				value.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
			}
			assembly.addInstruction(OperationsS3X.VASTORE_NS.instruction(
					AE22PEEK, //
					names[i],
					null,
					argumentC,
					0));
		}

		/**
		 * TODO - other than NEXT conditions could be handled on last element!
		 */
		if (store == ResultHandler.FA_BNN_NXT) {
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_NN_NEXT);
			return;
		}
		if (store == ResultHandler.FB_BSN_NXT) {
			return;
		}
		if (store == ResultHandler.FB_BNO_NXT) {
			/**
			 * TODO last element
			 */
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_NO_NEXT);
			return;
		}
		if (store == ResultHandler.FB_BSO_NXT) {
			/**
			 * TODO last element
			 */
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_SO_NEXT);
			return;
		}

		/**
		 * state != StateCode.NEXT
		 */
		if (store == ResultHandler.FC_PNN_RET) {
			/**
			 * TODO last element
			 */
			assembly.addInstruction(Instructions.INSTR_LOAD_1_S_NN_RETURN);
			return;
		}

		assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(AE21POP, 0, store));
		return;
	}

	@Override
	public String toCode() {
		
		return "COBJECT_LITERAL [" + this.getNotation() + "];";
	}

}
