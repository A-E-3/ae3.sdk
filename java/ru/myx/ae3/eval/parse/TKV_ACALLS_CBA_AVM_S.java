/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.parse;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.eval.tokens.TokenValue;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA2X;
import ru.myx.ae3.exec.OperationsS2X;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;

final class TKV_ACALLS_CBA_AVM_S extends TokenValue {
	
	private final ModifierArgument accessObjectModifier;
	
	private final TokenInstruction accessProperty;
	
	private final TokenInstruction arguments;
	
	private final int constant;
	
	TKV_ACALLS_CBA_AVM_S(final ModifierArgument accessObjectModifier, final TokenInstruction accessProperty, final TokenInstruction arguments, final int constant) {
		
		assert accessObjectModifier != null;
		assert accessProperty.assertStackValue();
		assert accessObjectModifier != ModifierArguments.AE21POP && accessObjectModifier != ModifierArguments.AA0RB;
		assert accessObjectModifier.argumentStackRead() == 0 : "Not a stack argument!";
		assert arguments.assertZeroStackOperands();
		assert arguments.getResultCount() == constant;
		assert constant > 1 : constant == 0
			? "Use " + TKV_ACALLV_BA_VM_S.class.getSimpleName() + " then"
			: "Use " + TKV_ACALLO_CBA_AVM_S.class.getSimpleName() + " then";
		this.accessObjectModifier = accessObjectModifier;
		this.accessProperty = accessProperty;
		this.arguments = arguments;
		this.constant = constant;
	}
	
	@Override
	public final String getNotation() {
		
		return "" + this.accessObjectModifier + "." + this.accessProperty.getNotation() + "( " + this.arguments.getNotation() + " )";
	}
	
	@Override
	public final InstructionResult getResultType() {
		
		return InstructionResult.OBJECT;
	}
	
	@Override
	public void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store) {
		
		/** zero operands */
		assert argumentA == null;
		assert argumentB == null;
		
		final ModifierArgument modifierObject = this.accessObjectModifier;
		final boolean directObject = modifierObject == ModifierArguments.AA0RB;
		
		final ModifierArgument modifierProperty = this.accessProperty.toDirectModifier();
		final boolean directProperty = modifierProperty == ModifierArguments.AA0RB;
		
		/** we'll use XVCALLS, not XACALLS - first element of arguments is 'this', line
		 * fn.call(this) **/
		/** arguments must go first for ACALLS */
		if (directObject) {
			assembly.addInstruction(Instructions.INSTR_LOAD_1_R_SN_NEXT);
			
			if (directProperty) {
				this.accessProperty.toAssembly( //
						assembly,
						null,
						null,
						ResultHandler.FA_BNN_NXT //
				);
			}
			
			assembly.addInstruction(
					(this.accessProperty.getResultType() == InstructionResult.STRING
						? OperationsS2X.VACCESS_DS
						: OperationsA2X.XACCESS_D)//
								.instruction(
										directObject
											? directProperty
												? ModifierArguments.AE22PEEK
												: ModifierArguments.AA0RB
											: modifierObject, //
										directProperty
											? ModifierArguments.AA0RB
											: modifierProperty,
										0,
										ResultHandler.FB_BSN_NXT)//
			);
			
			/** Anyway in stack, constant is expected to be more than zero */
			this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			
			assembly.addInstruction(OperationsA01.XRCALLA.instruction(this.constant, store));
			
			/** <code> delete?
			assembly.addInstruction(
					(this.accessProperty.getResultType() == InstructionResult.STRING
						? OperationsS10.VACALLTS_XS
						: OperationsA10.XACALLTS)//
								.instruction(//
										modifierProperty,
										this.constant + 1,
										store) //
			);
			
			</code> **/
			return;
		}
		
		/** Anyway in stack, constant is expected to be more than zero */
		this.arguments.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
		
		if (directProperty) {
			this.accessProperty.toAssembly( //
					assembly,
					null,
					null,
					ResultHandler.FA_BNN_NXT //
			);
		}
		
		assembly.addInstruction(
				(this.accessProperty.getResultType() == InstructionResult.STRING
					? OperationsS2X.VACALLS_XS
					: OperationsA2X.XACALLS)//
							.instruction(//
									modifierObject,
									modifierProperty,
									this.constant,
									store) //
		);
	}
	
	@Override
	public final String toCode() {
		
		return (this.accessProperty.getResultType() == InstructionResult.STRING
			? OperationsS2X.VACALLS_XS
			: OperationsA2X.XACALLS)//
				+ "\t0+" + this.constant + "\tAVM->S;";
	}
}
