/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenSyntax;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.ProgramAssembly;

/** @author myx */
final class TKS_ACALL_OCO extends TokenSyntax implements TokenSyntax.ConditionalStackValuable {
	
	private final TokenInstruction arguments;
	
	private final int constant;
	
	private final Instruction carguments;
	
	TKS_ACALL_OCO(final TokenInstruction arguments, final int constant, final Instruction carguments) {
		
		assert arguments == null || arguments.assertZeroStackOperands();
		assert arguments == null
			? 0 == constant
			: arguments.getResultCount() == constant;
		
		this.arguments = arguments;
		this.constant = constant;
		this.carguments = carguments;
	}
	
	@Override
	public final String getNotation() {
		
		if (this.arguments == null) {
			return "?.()";
		}
		return "?.(" + this.arguments.getNotation() + ")";
	}
	
	@Override
	public final int getPriorityLeft() {
		
		return 950;
	}
	
	@Override
	public final int getPriorityRight() {
		
		return 950;
	}
	
	@Override
	public ConditionType getSkipCondition() {
		
		return TokenInstruction.ConditionType.NULLISH_YES;
	}
	
	@Override
	public final boolean isConstantForArguments() {
		
		return false;
	}
	
	@Override
	public boolean isParseValueLeft() {
		
		return true;
	}
	
	@Override
	public final TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly)
			throws Evaluate.CompilationException {
		
		assert argumentA.isAccessReference();
		
		final TokenInstruction accessObject = argumentA.toReferenceObject();

		if (accessObject == ParseConstants.TKV_FRAME) {
			if (this.constant == 0) {
				return new TKV_FCALLV_EOCO_S(argumentA, argumentB);
			}
			return new TKV_FCALLA_EOCO_S(argumentA, this.arguments, this.constant, this.carguments, argumentB);
		}
		
		final TokenInstruction accessProperty = argumentA.toReferenceProperty();

		return new TKV_ACALLA_EOCO_S(accessObject, accessProperty, this.arguments, this.constant, this.carguments, argumentB);
	}
}
