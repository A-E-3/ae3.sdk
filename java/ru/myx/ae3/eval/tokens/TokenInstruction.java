/*
 * Created on 29.10.2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval.tokens;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.e4.parse.TokenType;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.parse.ParseConstants;
import ru.myx.ae3.eval.parse.TKV_WRAP_TOKENS2;
import ru.myx.ae3.eval.parse.TKV_WRAP_TOKENS3;
import ru.myx.ae3.eval.parse.TK_WRAP_TOKEN_ARGS1;
import ru.myx.ae3.eval.parse.TK_WRAP_TOKEN_ARGS2;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.Instruction;
import ru.myx.ae3.exec.InstructionEditable;
import ru.myx.ae3.exec.InstructionResult;
import ru.myx.ae3.exec.ModifierArgument;
import ru.myx.ae3.exec.ModifierArguments;
import ru.myx.ae3.exec.OperationsA01;
import ru.myx.ae3.exec.OperationsA11;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.exec.ResultHandlerDirect;

/** @author myx */
public interface TokenInstruction extends Instruction {
	
	/** @return */
	default boolean assertAccessReference() {
		
		assert this.isAccessReference() : "Expected to be access reference, tokenClass=" + this.getClass().getName() + ", token=" + this;
		return true;
	}
	
	/** @return */
	default boolean assertStackValue() {
		
		assert this.getOperandCount() == 0 : "Values expected to have exactly zero stack operands, but:" + this.getOperandCount() + "\r\n\t\tclass=" + this.getClass().getName()
				+ "\r\n\t\tthis=" + this;
		assert this.getResultCount() == 1 : "Values expected to have exactly one stack result, but:" + this.getResultCount() + "\r\n\t\tclass=" + this.getClass().getName()
				+ "\r\n\t\tthis=" + this;
		return true;
	}
	
	/** @return */
	default boolean assertZeroStackOperands() {
		
		assert this.getOperandCount() == 0 : "Expected to have exactly zero stack operands, but:" + this.getOperandCount() + "\r\n\t\tclass: " + this.getClass().getName()
				+ "\r\n\t\tthis: " + this + "\r\n\t\tnotation: " + this.getNotation() + "\r\n\t\tcode: " + this.toCode();
		return true;
	}
	
	@Override
	default ExecStateCode execCall(final ExecProcess ctx) {
		
		throw new UnsupportedOperationException("Tokens are not desined to be executed, class=" + this.getClass().getName() + "!");
	}
	
	/** @return */
	default String getNotation() {
		
		return this.getClass().getSimpleName();
	}
	
	/** @return */
	default String getNotationValue() {
		
		return '(' + this.getNotation() + ')';
	}
	
	/** @return int */
	abstract int getPriorityLeft();
	
	/** @return int */
	abstract int getPriorityRight();
	
	/** Result type when result count is 1. Null when result type is unknown. OBJECT is returned by
	 * default.
	 *
	 * @return type */
	InstructionResult getResultType();
	
	/** OPERATOR by default.
	 *
	 * @return type */
	TokenType getTokenType();
	
	/** should return true when toReferenceXXX methods are expected to work
	 *
	 * @return */
	default boolean isAccessReference() {
		
		return false;
	}
	
	/** @return */
	default boolean isConstantForArguments() {
		
		return false;
	}
	
	/** @return */
	default boolean isDirectSupported() {
		
		return true;
	}
	
	/** @return */
	default boolean isParseValueLeft() {
		
		return this.isStackValue();
	}
	
	/** @return */
	default boolean isParseValueRight() {
		
		return this.isStackValue();
	}
	
	/** @return */
	default boolean isStackOperator() {
		
		return this.getOperandCount() >= 0 && this.getResultCount() == 1;
	}
	
	/** @return */
	default boolean isStackStatement() {
		
		return this.getOperandCount() == 0 && this.getResultCount() == 0;
	}
	
	/** @return */
	default boolean isStackValue() {
		
		return this.getOperandCount() == 0 && this.getResultCount() == 1;
	}
	
	/** @param assembly
	 * @param argumentA
	 * @param argumentB
	 * @param store
	 * @throws Evaluate.CompilationException */
	void toAssembly(final ProgramAssembly assembly, final ModifierArgument argumentA, final ModifierArgument argumentB, final ResultHandlerBasic store)
			throws Evaluate.CompilationException;
	
	/** @param assembly
	 * @param compare
	 * @param constant
	 * @param store
	 * @throws Evaluate.CompilationException */
	default void toBooleanConditionalSkip(final ProgramAssembly assembly, final boolean compare, final int constant, final ResultHandler store)
			throws Evaluate.CompilationException {
		
		final ResultHandlerDirect direct = store.execDirectTransportType().transportForBooleanCheck().handlerForStoreNext();
		this.toAssembly(assembly, null, null, direct);
		
		if (direct == ResultHandler.FA_BNN_NXT) {
			assembly.addInstruction(
					(compare
						? OperationsA01.XESKIPRB1_P
						: OperationsA01.XESKIPRB0_P).instruction(constant, store));
			return;
		}
		
		assembly.addInstruction(
				(compare
					? OperationsA11.XESKIP1A_P
					: OperationsA11.XESKIP0A_P).instruction(ModifierArgument.forStore(direct), constant, store));
		
	}
	
	/** @param assembly
	 * @param start
	 * @param compare
	 * @param store
	 * @return
	 * @throws Evaluate.CompilationException */
	default InstructionEditable toBooleanConditionalSkip(final ProgramAssembly assembly, final int start, final boolean compare, final ResultHandler store)
			throws Evaluate.CompilationException {
		
		final ResultHandlerDirect direct = store.execDirectTransportType().transportForBooleanCheck().handlerForStoreNext();
		this.toAssembly(assembly, null, null, direct);
		
		if (direct == ResultHandler.FA_BNN_NXT) {
			final InstructionEditable editable = (compare
				? OperationsA01.XESKIPRB1_P
				: OperationsA01.XESKIPRB0_P).instructionCreate(0, store);
			assembly.addInstruction(editable);
			return editable;
		}
		final InstructionEditable editable = (compare
			? OperationsA11.XESKIP1A_P
			: OperationsA11.XESKIP0A_P).instructionCreate(ModifierArgument.forStore(direct), 0, store);
		assembly.addInstruction(editable);
		return editable;
	}
	
	/** If this token represents constant value, returns that value, otherwise returns NULL.
	 *
	 * Have none or constant arguments already and doesn't produce side-effects.
	 *
	 * @return NULL by default */
	default ModifierArgument toConstantModifier() {
		
		/** Check code (do not uncomment all such code at once, could cause deadly
		 * recursions):<code>
		
		assert this.toDirectModifier() == ModifierArguments.A07RR : //
		"Re-implement this method to handle this case, class=" + this.getClass().getName();
		
		</code> */
		return null;
	}
	
	/** If this token represents constant value, returns that value, otherwise returns NULL.
	 *
	 * Have none or constant arguments already and doesn't produce side-effects.
	 *
	 * @return NULL by default */
	default BaseObject toConstantValue() {
		
		/** Check code (do not uncomment all such code at once, could cause deadly
		 * recursions):<code>
		
		assert this.toConstantModifier() == null : //
		"Re-implement this method to handle this case, class=" + this.getClass().getName();
		
		</code> */
		return null;
	}
	
	/** @return */
	default BasePrimitiveString toContextPropertyName() {
		
		return null;
	}
	
	/** @return name or null */
	default String toCreatePropertyName() {
		
		return null;
	}
	
	/** @return */
	default ModifierArgument toDirectModifier() {
		
		/** Check code (do not uncomment all such code at once, could cause deadly
		 * recursions):<code>
		
		assert this.toConstantModifier() == null : //
		"Re-implement this method to handle this case, class=" + this.getClass().getName();
		
		</code> */
		return ModifierArguments.AA0RB;
	}
	
	/** Should return instruction producing same results from same arguments but definitely
	 * providing detach-able (not depending on context state) result in r7RR register (if any).
	 *
	 * Without calling this there is no guarantee that results would be valid for the next
	 * instruction.
	 *
	 * Every instruction using internal context's accumulators for results should override this
	 * method and provide means for receiving actually detach-able results.
	 *
	 * @return */
	default TokenInstruction toExecDetachableResult() {
		
		return this;
	}
	
	/** Should return instruction producing same results from same arguments but definitely
	 * providing native (not depending on context state, not wrapped) result in r7RR register (if
	 * any).
	 *
	 * Without calling this there is no guarantee that results would be valid for the next
	 * instruction.
	 *
	 * Every instruction using internal context's accumulators for results should override this
	 * method and provide means for receiving actually native results.
	 *
	 * @return */
	default TokenInstruction toExecNativeResult() {
		
		return this.toExecDetachableResult();
	}
	
	/** Prefer stack when toDirectModifier returns 7RR
	 *
	 * @return */
	default boolean toPreferStackResult() {
		
		return false;
	}
	
	/** @return */
	default TokenInstruction toReferenceDelete() {
		
		assert !this.isAccessReference() : "this method must be overridden for every class claims to support access reference interface, class=" + this.getClass().getName();
		throw new UnsupportedOperationException("Not an access reference");
	}
	
	/** when isAccessReference, could be NULL when object is not known within a token and read from
	 * operand stack
	 *
	 * @return */
	default TokenInstruction toReferenceObject() {
		
		assert !this.isAccessReference() : "this method must be overridden for every class claims to support access reference interface, class=" + this.getClass().getName();
		throw new UnsupportedOperationException("Not an access reference");
	}
	
	/** when isAccessReference, could be NULL when property is not known within a token and read
	 * from operand stack
	 *
	 * @return */
	default TokenInstruction toReferenceProperty() {
		
		assert !this.isAccessReference() : "this method must be overridden for every class claims to support access reference interface, class=" + this.getClass().getName();
		throw new UnsupportedOperationException("Not an access reference");
	}
	
	/** @param assembly
	 * @param argumentA
	 * @param argumentB
	 * @param needRead
	 * @param directAllowed
	 * @return
	 * @throws Evaluate.CompilationException */
	default ModifierArgument toReferenceReadBeforeWrite(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final boolean needRead,
			final boolean directAllowed) throws Evaluate.CompilationException {
		
		assert !this.isAccessReference() : "this method must be overridden for every class claims to support access reference interface, class=" + this.getClass().getName();
		throw new UnsupportedOperationException("Not an access reference");
	}
	
	/** Always gets store result from direct RR
	 *
	 * @param assembly
	 * @param argumentA
	 * @param argumentB
	 * @param modifierValue
	 * @param store
	 * @throws Evaluate.CompilationException */
	default void toReferenceWriteAfterRead(final ProgramAssembly assembly,
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final ModifierArgument modifierValue,
			final ResultHandler store) throws Evaluate.CompilationException {
		
		assert !this.isAccessReference() : "this method must be overridden for every class claims to support access reference interface, class=" + this.getClass().getName();
		throw new UnsupportedOperationException("Not an access reference");
	}
	
	/** @param programAssembly
	 * @param sideEffectsOnly
	 *            - true if only code producing side effects is required (optional), do not return
	 *            NULL, return constant instead.
	 * @return
	 * @throws Evaluate.CompilationException */
	default TokenInstruction toStackValue(final ProgramAssembly programAssembly, final boolean sideEffectsOnly) throws Evaluate.CompilationException {
		
		assert this.getOperandCount() + this.getResultCount() == 0 : "Only for syntax token, class=" + this.getClass().getName() + ", this=" + this;
		throw new UnsupportedOperationException("Not a syntax token");
	}
	
	/** @param assembly
	 * @param argumentA
	 * @param sideEffectsOnly
	 *            - true if only code producing side effects is required (optional), do not return
	 *            NULL, return constant instead.
	 * @return
	 * @throws Evaluate.CompilationException */
	default TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final boolean sideEffectsOnly) throws Evaluate.CompilationException {
		
		assert this.getOperandCount() == 1 : "Need complete set of arguments at once, not 1 but: " + this.getOperandCount();
		// System.out.println( ">>> >>> 1: " + token + " " + argumentA );
		final ModifierArgument modifierA = argumentA.toDirectModifier();
		assert modifierA != ModifierArguments.AE21POP : "asking for direct";
		if (modifierA == ModifierArguments.AA0RB) {
			return new TKV_WRAP_TOKENS2(argumentA, this);
		}
		if (this.isConstantForArguments() && modifierA.argumentConstantValue() != null) {
			if (sideEffectsOnly) {
				/** known to be a constant already */
				return argumentA;
			}
			final int size = assembly.size();
			this.toAssembly(assembly, modifierA, null, ResultHandler.FA_BNN_NXT);
			
			// assembly.ctx.vmScopeCreateSandbox();
			// assembly.toProgram(size).execSetupContext(assembly.ctx);
			
			return ParseConstants.getConstantValue(assembly.toProgram(size).callNE0(assembly.ctx, BaseObject.UNDEFINED));
			
			// final ExecStateCode code = assembly.ctx.runImpl();
			
			// final ExecStateCode code =
			// assembly.toProgram(size).executeRaw(assembly.ctx);
			/** FIXME: check if ExitCode check required here! */
			/** <code>
			if (code == ExecStateCode.ERROR) {
				final BaseObject result = assembly.ctx.vmGetResultImmediate();
				if (result instanceof RuntimeException) {
					throw (RuntimeException) result;
				}
			}
			assert code == ExecStateCode.RETURN : "code=" + code;
			return ParseConstants.getConstantValue(assembly.ctx.vmGetResultDetachable());
			</code> */
		}
		{
			final TokenInstruction replacement = this.toToken(modifierA, null);
			assert replacement != null;
			return replacement;
		}
	}
	
	/** @param assembly
	 * @param argumentA
	 * @param argumentB
	 * @param sideEffectsOnly
	 *            - true if only code producing side effects is required (optional), do not return
	 *            NULL, return constant instead.
	 * @return
	 * @throws Evaluate.CompilationException */
	default TokenInstruction toStackValue(final ProgramAssembly assembly, final TokenInstruction argumentA, final TokenInstruction argumentB, final boolean sideEffectsOnly)
			throws Evaluate.CompilationException {
		
		assert this.getOperandCount() == 2 : "Need complete set of arguments at once, not 2 but: " + this.getOperandCount();
		// System.out.println( ">>> >>> 2: " + token + " " + argumentA +
		// " " + argumentB );
		final ModifierArgument modifierA = argumentA.toDirectModifier();
		assert modifierA != ModifierArguments.AE21POP : "asking for default";
		final boolean directA = modifierA == ModifierArguments.AA0RB;
		final ModifierArgument modifierB = argumentB.toDirectModifier();
		assert modifierB != ModifierArguments.AE21POP : "asking for default";
		final boolean directB = modifierB == ModifierArguments.AA0RB;
		/** both of them? */
		if (directA && directB) {
			return new TKV_WRAP_TOKENS3(argumentA, argumentB, this);
		}
		if (this.isConstantForArguments()) {
			final BaseObject constantA = modifierA.argumentConstantValue();
			final BaseObject constantB = modifierB.argumentConstantValue();
			if (constantA != null && constantB != null) {
				if (sideEffectsOnly) {
					/** Known to be constant already */
					return argumentA;
				}
				final int size = assembly.size();
				this.toAssembly(assembly, modifierA, modifierB, ResultHandler.FA_BNN_NXT);
				final BaseObject result = assembly.toProgram(size).callNE0(assembly.ctx, BaseObject.UNDEFINED);
				return ParseConstants.getConstantValue(result);
			}
			/** side effects */
			if (sideEffectsOnly) {
				if (constantA != null) {
					return argumentB;
				}
				if (constantB != null) {
					return argumentA;
				}
			}
		}
		/** any of them? */
		if (directA) {
			final TokenInstruction replacement = this.toToken(null, modifierB);
			if (replacement != null) {
				return new TKV_WRAP_TOKENS2(argumentA, replacement);
			}
			assert false;
		}
		if (directB) {
			final TokenInstruction replacement = this.toToken(modifierA, null);
			if (replacement != null) {
				return new TKV_WRAP_TOKENS2(argumentB, replacement);
			}
			assert false;
		}
		/** ok - none */
		{
			final TokenInstruction replacement = this.toToken(modifierA, modifierB);
			assert replacement != null;
			return replacement;
		}
	}
	
	/** Should not use POP and RR_READ. Use null instead.
	 *
	 * @param modifierA
	 * @param modifierB
	 * @return
	 * @throws Evaluate.CompilationException */
	default TokenInstruction toToken(final ModifierArgument modifierA, final ModifierArgument modifierB) throws Evaluate.CompilationException {
		
		assert modifierA != ModifierArguments.AE21POP && modifierA != ModifierArguments.AA0RB : "no POP nor RR_READ allowed here, use NULL";
		assert modifierB != ModifierArguments.AE21POP && modifierB != ModifierArguments.AA0RB : "no POP nor RR_READ allowed here, use NULL";
		final int operandCount = this.getOperandCount();
		assert operandCount > 0 : "Must have operands to be reduceable";
		assert operandCount < 3 : "Must have less than 3 operands to be reduceable by this method";
		assert modifierB == null || operandCount > 1 : "Extra arguments: this=" + this;
		return operandCount == 2
			? new TK_WRAP_TOKEN_ARGS2(this, modifierA, modifierB)
			: /* operandCount == 1 */
			new TK_WRAP_TOKEN_ARGS1(this, modifierA);
	}
	
}
