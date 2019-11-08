/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseNumber;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.base.ToPrimitiveHint;
import ru.myx.vm_vliw32_2010.OperationA10;

/** @author myx */
public enum OperationsS10 implements OperationA10 {
	/** macro for: MADD 0, a */
	VCVTN_D {

		@Override
		public OperationA10 execNativeResult() {

			return OperationsS10.VCVTN_N;
		}

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			if (argumentA instanceof BaseNumber) {
				return store.execReturn(ctx, argumentA);
			}
			{

				final BasePrimitive<?> primitive = argumentA.baseToPrimitive(ToPrimitiveHint.NUMBER);

				return primitive instanceof BaseNumber
					? store.execReturn(ctx, primitive)
					: store.execReturnNumeric(ctx, primitive.doubleValue());
			}
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public boolean isConstantForArguments() {

			return true;
		}
	},
	/** macro for: MADD 0, a */
	VCVTN_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			return store.execReturn(ctx, argumentA.baseToNumber());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.NUMBER;
		}

		@Override
		public boolean isConstantForArguments() {

			return true;
		}
	},
	/** macro for: MADD '', a */
	VCVTS_D {

		@Override
		public OperationA10 execNativeResult() {

			return OperationsS10.VCVTS_N;
		}

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			if (argumentA instanceof BaseString) {
				return store.execReturn(ctx, argumentA);
			}
			{

				final BasePrimitive<?> primitive = argumentA.baseToPrimitive(ToPrimitiveHint.STRING);
				return primitive instanceof BaseString
					? store.execReturn(ctx, primitive)
					: store.execReturnString(ctx, primitive.stringValue());
			}
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.STRING;
		}

		@Override
		public boolean isConstantForArguments() {

			return true;
		}
	},
	/** macro for: MADD '', a */
	VCVTS_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			return store.execReturn(ctx, argumentA.baseToString());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.STRING;
		}

		@Override
		public boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	VFLOAD_D {

		@Override
		public OperationA10 execNativeResult() {

			return OperationsS10.VFLOAD_N;
		}

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			return store.execReturn(ctx, ExecProcess.vmEnsureDetached(ctx, argumentA));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public boolean isConstantForArguments() {

			return true;
		}
	},
	/**
	 *
	 */
	VFLOAD_N {

		@Override

		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			return store.execReturn(ctx, ExecProcess.vmEnsureNative(argumentA));
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public boolean isConstantForArguments() {

			return true;
		}
	},
	/** detach-able */
	VMBAND_D {

		@Override
		public OperationA10 execNativeResult() {

			return OperationsS10.VMBAND_N;
		}

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int valueA = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA & constant);
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
	VMBAND_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int valueA = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, valueA & constant);
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
		public OperationA10 execNativeResult() {

			return OperationsS10.VMBSHL_N;
		}

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift << (constant & 0x1F));
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
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift << (constant & 0x1F));
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
		public OperationA10 execNativeResult() {

			return OperationsS10.VMBSHRS_N;
		}

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >> (constant & 0x1F));
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
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >> (constant & 0x1F));
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
		public OperationA10 execNativeResult() {

			return OperationsS10.VMBSHRU_N;
		}

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >>> (constant & 0x1F));
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
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final int shift = argumentA.baseToJavaInteger();
			return store.execReturnNumeric(ctx, shift >>> (constant & 0x1F));
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

	/**
	 *
	 */
	;

	@Override
	public OperationA10 execNativeResult() {

		return this;
	}

	@Override
	public OperationA10 execStackResult() {

		return this;
	}

	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();

	Instruction instructionCached(//
			final ModifierArgument argumentA,
			final int constant,
			final ResultHandler store) {

		return InstructionA10.instructionCached(this.instruction(argumentA, constant, store));
	}

	@Override
	public boolean isConstantForArguments() {

		return false;
	}
}
