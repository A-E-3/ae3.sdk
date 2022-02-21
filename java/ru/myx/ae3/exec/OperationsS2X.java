/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.base.ToPrimitiveHint;
import ru.myx.ae3.help.Format;
import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.OperationA2X;

/** @author myx
 *
 *         Specials */
public enum OperationsS2X implements OperationA2X {
	
	/** direct string */
	VACALLS_XS {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			assert argumentA != ctx || ctx == ctx.rb4CT : "Use FCALLS then";
			
			/** CANNOT BE USED WITH GETTERS, execution order is invalid, make RCALL <code>
			final ExecStateCode access = argumentA.vmPropertyRead(ctx, (CharSequence) argumentB, BaseObject.UNDEFINED, ResultHandler.FA_BNN_NXT);
			if (access != null) {
				// TODO: check if code should be analyzed
				return access;
			}
			
			final BaseObject candidate = ctx.ra0RB;
			</code> */
			final BaseObject candidate = argumentA.baseGet((CharSequence) argumentB, BaseObject.UNDEFINED);
			
			final BaseFunction callee = candidate.baseCall();
			if (callee == null) {
				if (candidate == BaseObject.UNDEFINED) {
					return ctx.vmRaise(
							Format.Compact.baseObject(argumentA) + " has no property called " //
									+ Format.Compact.baseObject(argumentB));
				}
				return ctx.vmRaise("Not a function: key=" + argumentB.baseToJavaString() + ", class=" + candidate.getClass().getName());
			}
			
			return ctx.vmCallS(callee, argumentA, constant, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final int getStackInputCount(final int constant) {
			
			return constant;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},
	/** detach-able, INTEGER */
	VACCESS_DI {
		
		@Override
		public OperationA2X execDirectResult() {
			
			return OperationsS2X.VACCESS_TI;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			assert argumentA != ctx : "Use LOAD";
			
			return argumentA.vmPropertyRead(ctx, argumentB.baseToJavaInteger(), argumentB, BaseObject.UNDEFINED, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},
	/** detach-able, STRING */
	VACCESS_DS {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VACCESS_NS;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			assert argumentA != ctx : "Use LOAD";
			
			return argumentA.vmPropertyRead(ctx, (CharSequence) argumentB, argumentB, BaseObject.UNDEFINED, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public InstructionIA instruction(final ModifierArgument argumentA, final BaseObject constantArgumentB, final ModifierArgument defaultArgumentB, final int constant) {
			
			if (constant == 0) {
				return new IA2_VACCESSDS_AS_0_NN_NXT(argumentA, constantArgumentB);
			}
			return super.instruction(argumentA, constantArgumentB, defaultArgumentB, constant);
		}
		
		@Override
		public InstructionIA instruction(final ModifierArgument argumentA,
				final BaseObject constantArgumentB,
				final ModifierArgument defaultArgumentB,
				final int constant,
				final ResultHandler store) {
			
			if (constant == 0 && store == ResultHandler.FB_BSN_NXT) {
				return new IA2_VACCESSDS_AS_0_SN_NXT(argumentA, constantArgumentB);
			}
			return super.instruction(argumentA, constantArgumentB, defaultArgumentB, constant, store);
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
		
	},
	/** native, any */
	VACCESS_NA {
		
		@Override
		public OperationA2X execDirectResult() {
			
			return OperationsA2X.XACCESS_D;
		}
		
		@Override
		public OperationA2X execStackResult() {
			
			return OperationsA2X.XACCESS_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			assert argumentA != ctx : "Use LOAD";
			
			return argumentA.vmPropertyRead(ctx, argumentB, BaseObject.UNDEFINED, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},
	/** native, string */
	VACCESS_NS {
		
		@Override
		public OperationA2X execDirectResult() {
			
			return OperationsS2X.VACCESS_DS;
		}
		
		@Override
		public OperationA2X execStackResult() {
			
			return OperationsS2X.VACCESS_DS;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			assert argumentA != ctx : "Use LOAD";
			
			return argumentA.vmPropertyRead(ctx, (CharSequence) argumentB, argumentB, BaseObject.UNDEFINED, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},
	/** temp, INTEGER */
	VACCESS_TI {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VACCESS_DI;
		}
		
		@Override
		public OperationA2X execStackResult() {
			
			return OperationsS2X.VACCESS_DI;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			assert argumentA != ctx : "Use LOAD";
			
			return argumentA.vmPropertyRead(ctx, argumentB.baseToJavaInteger(), argumentB, BaseObject.UNDEFINED, store);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},
	/** detach-able
	 *
	 * should be implemented cause we pass value but detachable-ness is up to topmost token */
	VEBOR_D {
		
		@Override
		public OperationA2X execDirectResult() {
			
			return OperationsA2X.XEBOR_T;
		}
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VEBOR_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			switch (constant) {
				case 0 :
					return store.execReturn(
							ctx,
							ExecProcess.vmEnsureDetached(
									ctx,
									argumentA.baseToJavaBoolean()
										? argumentA
										: argumentB));
				case 1 :
					return store.execReturn(
							ctx,
							ExecProcess.vmEnsureDetached(
									ctx,
									argumentA != BaseObject.UNDEFINED
										? argumentA
										: argumentB));
				case 2 :
					return store.execReturn(
							ctx,
							ExecProcess.vmEnsureDetached(
									ctx,
									argumentA.baseValue() != null
										? argumentA
										: argumentB));
				default :
					return ctx.vmRaise("Invalid constant value: " + constant);
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** variation of BOR
	 *
	 * detach-able
	 *
	 * should be implemented cause we pass value but detachable-ness is up to topmost token */
	VENCO_D {
		
		@Override
		public OperationA2X execDirectResult() {
			
			return OperationsA2X.XENCO_T;
		}
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VENCO_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			return store.execReturn(
					ctx,
					ExecProcess.vmEnsureDetached(
							ctx,
							argumentA.baseValue() != null
								? argumentA
								: argumentB));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native
	 *
	 * should be implemented cause we pass value but native-ness is up to topmost token */
	VEBOR_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			switch (constant) {
				case 0 :
					return store.execReturn(
							ctx,
							ExecProcess.vmEnsureNative(
									argumentA.baseToJavaBoolean()
										? argumentA
										: argumentB));
				case 1 :
					return store.execReturn(
							ctx,
							ExecProcess.vmEnsureNative(
									argumentA != BaseObject.UNDEFINED
										? argumentA
										: argumentB));
				case 2 :
					return store.execReturn(
							ctx,
							ExecProcess.vmEnsureNative(
									argumentA.baseValue() != null
										? argumentA
										: argumentB));
				default :
					return ctx.vmRaise("Invalid constant value: " + constant);
			}
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** variation of BOR
	 *
	 * native
	 *
	 * should be implemented cause we pass value but native-ness is up to topmost token */
	VENCO_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			return store.execReturn(
					ctx,
					ExecProcess.vmEnsureNative(
							argumentA.baseValue() != null
								? argumentA
								: argumentB));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VFDECLARE_N {
		
		@Override
		public OperationA2X execDirectResult() {
			
			return OperationsA2X.XFDECLARE_D;
		}
		
		@Override
		public OperationA2X execStackResult() {
			
			return OperationsA2X.XFDECLARE_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BaseObject value = ExecProcess.vmEnsureNative(argumentB);
			
			ctx.contextCreateMutableBinding(argumentA, value, false);
			
			return store.execReturn(ctx, value);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},
	/** native */
	VFSTORE_N {
		
		@Override
		public OperationA2X execDirectResult() {
			
			return OperationsA2X.XFSTORE_D;
		}
		
		@Override
		public OperationA2X execStackResult() {
			
			return OperationsA2X.XFSTORE_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BaseObject value = ExecProcess.vmEnsureNative(argumentB);
			
			ctx.contextSetMutableBinding(argumentA, value, false);
			
			return store.execReturn(ctx, value);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},
	/** stack */
	VMADD_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMADD_N;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			if (argumentA instanceof CharSequence) {
				return store.execReturnString(ctx, argumentA.baseToJavaString() + argumentB.baseToJavaString());
			}
			
			final BasePrimitive<?> additive = argumentA.baseToPrimitive(null);
			
			if (argumentB instanceof CharSequence) {
				return store.execReturnString(ctx, additive.stringValue() + argumentB.toString());
			}
			
			final BasePrimitive<?> multiplicative = argumentB.baseToPrimitive(null);
			
			return additive instanceof CharSequence || multiplicative instanceof CharSequence
				? store.execReturnString(ctx, additive.stringValue() + multiplicative.stringValue())
				: additive == BasePrimitiveNumber.NAN
					? store.execReturn(ctx, multiplicative)
					: multiplicative == BasePrimitiveNumber.NAN
						? store.execReturn(ctx, additive)
						: store.execReturnNumeric(ctx, additive.doubleValue() + multiplicative.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMADD_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			if (argumentA instanceof CharSequence) {
				return store.execReturnString(ctx, argumentA.baseToJavaString() + argumentB.baseToJavaString());
			}
			
			final BasePrimitive<?> additive = argumentA.baseToPrimitive(null);
			
			if (argumentB instanceof CharSequence) {
				return store.execReturnString(ctx, additive.stringValue() + argumentB.toString());
			}
			
			final BasePrimitive<?> multiplicative = argumentB.baseToPrimitive(null);
			
			return additive instanceof CharSequence || multiplicative instanceof CharSequence
				? store.execReturnString(ctx, additive.stringValue() + multiplicative.stringValue())
				: additive == BasePrimitiveNumber.NAN
					? store.execReturn(ctx, ExecProcess.vmEnsureNative(multiplicative))
					: multiplicative == BasePrimitiveNumber.NAN
						? store.execReturn(ctx, ExecProcess.vmEnsureNative(additive))
						: store.execReturnNumeric(ctx, additive.doubleValue() + multiplicative.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** numeric detachable */
	VMADDN_D {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitiveNumber additive = argumentA.baseToNumber();
			
			final BasePrimitiveNumber multiplicative = argumentB.baseToNumber();
			
			return additive == BasePrimitiveNumber.NAN
				? store.execReturn(ctx, ExecProcess.vmEnsureNative(multiplicative))
				: multiplicative == BasePrimitiveNumber.NAN
					? store.execReturn(ctx, ExecProcess.vmEnsureNative(additive))
					: store.execReturnNumeric(ctx, additive.doubleValue() + multiplicative.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** numeric native */
	VMADDN_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitiveNumber additive = argumentA.baseToNumber();
			
			final BasePrimitiveNumber multiplicative = argumentB.baseToNumber();
			
			return additive == BasePrimitiveNumber.NAN
				? store.execReturn(ctx, ExecProcess.vmEnsureNative(multiplicative))
				: multiplicative == BasePrimitiveNumber.NAN
					? store.execReturn(ctx, ExecProcess.vmEnsureNative(additive))
					: store.execReturnNumeric(ctx, additive.doubleValue() + multiplicative.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** numeric direct */
	VMADDN_T {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMADDN_N;
		}
		
		@Override
		public OperationA2X execStackResult() {
			
			return OperationsS2X.VMADDN_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitiveNumber additive = argumentA.baseToNumber();
			
			final BasePrimitiveNumber multiplicative = argumentB.baseToNumber();
			
			return additive == BasePrimitiveNumber.NAN
				? store.execReturn(ctx, multiplicative)
				: multiplicative == BasePrimitiveNumber.NAN
					? store.execReturn(ctx, additive)
					: store.execReturnNumeric(ctx, additive.doubleValue() + multiplicative.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** string detachable */
	VMADDS_D {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			return store.execReturnString(ctx, argumentA.baseToJavaString() + argumentB.baseToJavaString());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.STRING;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** string native */
	VMADDS_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			return store.execReturnString(ctx, argumentA.baseToJavaString() + argumentB.baseToJavaString());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.STRING;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** string direct */
	VMADDS_T {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMADDS_N;
		}
		
		@Override
		public OperationA2X execStackResult() {
			
			return OperationsS2X.VMADDS_D;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			return store.execReturnString(ctx, argumentA.baseToJavaString() + argumentB.baseToJavaString());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.STRING;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMBAND_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMBAND_N;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA & valueB);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMBAND_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA & valueB);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMBOR_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMBOR_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA | valueB);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMBOR_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA | valueB);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMBSHL_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMBSHL_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift << (additive & 0x1F));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMBSHL_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift << (additive & 0x1F));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMBSHRS_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMBSHRS_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >> (additive & 0x1F));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMBSHRS_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >> (additive & 0x1F));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMBSHRU_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMBSHRU_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >>> (additive & 0x1F));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMBSHRU_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int shift = argumentA.baseToJavaInteger();
			final int additive = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >>> (additive & 0x1F));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMBXOR_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMBXOR_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA ^ valueB);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMBXOR_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final int valueA = argumentA.baseToJavaInteger();
			final int valueB = argumentB.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA ^ valueB);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.INTEGER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMDIV_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMDIV_N;
		}
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> dividend = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (dividend == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, dividend);
			}
			
			final BasePrimitive<?> divisor = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (divisor == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, divisor);
			}
			
			return store.execReturnNumeric(ctx, dividend.doubleValue() / divisor.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMDIV_N {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> dividend = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (dividend == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, dividend);
				
			}
			
			final BasePrimitive<?> divisor = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (divisor == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, divisor);
			}
			
			return store.execReturnNumeric(ctx, dividend.doubleValue() / divisor.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMMOD_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMMOD_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> dividend = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (dividend == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, dividend);
			}
			final BasePrimitive<?> divisor = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (divisor == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, divisor);
			}
			return store.execReturnNumeric(ctx, dividend.doubleValue() % divisor.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMMOD_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> dividend = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (dividend == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, dividend);
			}
			final BasePrimitive<?> divisor = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (divisor == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, divisor);
			}
			return store.execReturnNumeric(ctx, dividend.doubleValue() % divisor.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMMUL_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMMUL_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> multiplicative = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}
			final BasePrimitive<?> unary = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (unary == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, unary);
			}
			return store.execReturnNumeric(ctx, multiplicative.doubleValue() * unary.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMPOW_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMPOW_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> multiplicative = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}
			final BasePrimitive<?> unary = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (unary == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, unary);
			}
			return store.execReturnNumeric(ctx, Math.pow(multiplicative.doubleValue(), unary.doubleValue()));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMMUL_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> multiplicative = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}
			final BasePrimitive<?> unary = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (unary == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, unary);
			}
			return store.execReturnNumeric(ctx, multiplicative.doubleValue() * unary.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMPOW_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> multiplicative = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}
			final BasePrimitive<?> unary = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (unary == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, unary);
			}
			return store.execReturnNumeric(ctx, Math.pow(multiplicative.doubleValue(), unary.doubleValue()));
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** detach-able */
	VMSUB_D {
		
		@Override
		public OperationA2X execNativeResult() {
			
			return OperationsS2X.VMSUB_N;
		}
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> additive = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (additive == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, additive);
			}
			final BasePrimitive<?> multiplicative = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}
			return store.execReturnNumeric(ctx, additive.doubleValue() - multiplicative.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** native */
	VMSUB_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final int constant, final ResultHandler store) {
			
			final BasePrimitive<?> additive = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (additive == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, additive);
			}
			final BasePrimitive<?> multiplicative = argumentB.baseToPrimitive(ToPrimitiveHint.NUMBER);
			if (multiplicative == BasePrimitiveNumber.NAN) {
				return store.execReturn(ctx, multiplicative);
			}
			return store.execReturnNumeric(ctx, additive.doubleValue() - multiplicative.doubleValue());
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.NUMBER;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return true;
		}
	},
	/** STRING
	 *
	 * macro ASTORE RT, a, b */
	VTSTORE_S {
		
		@Override
		
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {
			
			final BaseObject value = ExecProcess.vmEnsureNative(argumentC);
			
			ctx.rb4CT.baseDefine(argumentB.baseToString(), value, BaseProperty.ATTRS_MASK_WED);
			return store.execReturn(ctx, value);
		}
		
		@Override
		public final InstructionResult getResultType() {
			
			return InstructionResult.OBJECT;
		}
		
		@Override
		public final boolean isConstantForArguments() {
			
			return false;
		}
	},;
	
	@Override
	public OperationA2X execDirectResult() {
		
		return this;
	}
	
	@Override
	public OperationA2X execNativeResult() {
		
		return this;
	}
	
	@Override
	public OperationA2X execStackResult() {
		
		return this;
	}
	
	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();
	
	Instruction instructionCached(//
			final ModifierArgument argumentA,
			final ModifierArgument argumentB,
			final int constant,
			final ResultHandler store) {
		
		return InstructionA2X.instructionCached(this.instruction(argumentA, argumentB, constant, store));
	}
}
