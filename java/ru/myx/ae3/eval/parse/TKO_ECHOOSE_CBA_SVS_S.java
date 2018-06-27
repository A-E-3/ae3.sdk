/*
 * Created on 29.10.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;
import ru.myx.ae3.exec.parse.expression.TokenOperator;

final class TKO_ECHOOSE_CBA_SVS_S extends TokenOperator {
	
	
	private final TokenInstruction argumentB;
	
	TKO_ECHOOSE_CBA_SVS_S(final TokenInstruction argumentB) {
		this.argumentB = argumentB;
	}
	
	@Override
	public final String getNotation() {
		
		
		return " ? " + this.argumentB.getNotation() + " : ";
	}
	
	@Override
	public final int getOperandCount() {
		
		
		return 2;
	}
	
	@Override
	public final int getPriorityLeft() {
		
		
		return 166;
	}
	
	@Override
	public final int getPriorityRight() {
		
		
		return 162;
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ResultHandlerBasic store) {
		
		
		/**
		 * TODO: can actually handle constant argumentB
		 */
		throw new IllegalStateException("Incomplete!");
	}
	
	@Override
	public final String toCode() {
		
		
		return "ECHOOSE\t3\tSVS ->S;";
	}
	
	@Override
	public TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentC, final boolean sideEffectsOnly) {
		
		
		final BaseObject constantIF = argumentA.toConstantValue();
		final BaseObject constantThen = this.argumentB.toConstantValue();
		final BaseObject constantElse = argumentC.toConstantValue();
		if (constantIF != null) {
			return constantIF.baseToBoolean() == BaseObject.TRUE
				? sideEffectsOnly && constantThen != null
					? argumentA
					: this.argumentB
				: sideEffectsOnly && constantElse != null
					? argumentA
					: argumentC;
		}
		
		if (constantThen != null) {
			if (constantElse != null) {
				if (sideEffectsOnly) {
					/**
					 * other arguments are known to be constants
					 */
					return argumentA;
				}
				/**
				 * <code>xxx ? true : false</code> --> <code>!!xxx</code>
				 */
				if (constantThen == BaseObject.TRUE && constantElse == BaseObject.FALSE) {
					return new TKV_BCVT(argumentA);
				}
				/**
				 * <code>xxx ? false : true</code> --> <code>!xxx</code>
				 */
				if (constantThen == BaseObject.FALSE && constantElse == BaseObject.TRUE) {
					return new TKV_BNOT(argumentA);
				}
			}
			if (sideEffectsOnly) {
				return new TKV_EBOR(argumentA, argumentC);
			}
		} else //
		if (constantElse != null) {
			if (sideEffectsOnly) {
				return new TKV_EBAND(argumentA, this.argumentB);
			}
		}
		
		final BasePrimitiveString frameNameA = argumentA.toContextPropertyName();
		if (frameNameA != null && frameNameA == this.argumentB.toContextPropertyName()) {
			return new TKV_EBOR(argumentA, argumentC);
		}
		
		return new TKV_ECHOOSE(argumentA, this.argumentB, argumentC);
	}
}
