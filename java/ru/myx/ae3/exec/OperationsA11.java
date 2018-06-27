/**
 *
 */
package ru.myx.ae3.exec;

import static ru.myx.ae3.exec.ExecStateCode.NEXT;
import static ru.myx.ae3.exec.ModifierArguments.AA0RB;

import ru.myx.ae3.base.BaseObject;

/** @author myx */
public enum OperationsA11 implements OperationA11 {
	/**
	 *
	 */
	XCFUNCTION_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			final String[] arguments;
			{
				final Object baseValue = argumentA.baseValue();
				if (baseValue == null) {
					arguments = null;
				} else //
				if (baseValue instanceof String[]) {
					arguments = (String[]) baseValue;
				} else {
					return ctx.vmRaise("CFUNCTION: invalid argument type: class=" + baseValue.getClass().getName());
				}
			}
			final int limit = ctx.ri08IP + constant;
			final ExecFunctionImpl function = new ExecFunctionImpl(ctx.rb7FV, arguments, ctx.fldCode, ctx.ri08IP + 1, limit + 1);
			ctx.ri08IP = limit;
			return store.execReturn(ctx, function);
		}

		@Override
		public final InstructionResult getResultType() {

			return null;
		}

		@Override
		public final boolean isRelativeAddressInConstant() {

			return true;
		}
	},
	/**
	 *
	 */
	XESKIP0A_P {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			if (!argumentA.baseToJavaBoolean()) {
				ctx.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return NEXT;
			}
			return store.execReturn(ctx, argumentA);
		}

		@Override
		public final InstructionResult getResultType() {

			return null;
		}

		@Override
		public final Instruction instruction(final ModifierArgument argumentA, final int constant, final ResultHandler store) {

			if (store == ResultHandler.FA_BNN_NXT) {
				return new IA11_XESKIP0A_A_C_NN_NXT(argumentA, constant);
			}
			return super.instruction(argumentA, constant, store);
		}

		@Override
		public final boolean isRelativeAddressInConstant() {

			return true;
		}
	},
	/**
	 *
	 */
	XESKIP1A_P {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			if (argumentA.baseToJavaBoolean()) {
				ctx.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return NEXT;
			}
			return store.execReturn(ctx, argumentA);
		}

		@Override
		public final InstructionResult getResultType() {

			return null;
		}

		@Override
		public final Instruction instruction(final ModifierArgument argumentA, final int constant, final ResultHandler store) {

			if (store == ResultHandler.FA_BNN_NXT) {
				return new IA11_XESKIP1A_A_C_NN_NXT(argumentA, constant);
			}
			return super.instruction(argumentA, constant, store);
		}

		@Override
		public final boolean isRelativeAddressInConstant() {

			return true;
		}
	},
	/**
	 *
	 */
	XESKIPRB0XA_P {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			if (!BaseObject.equalsNative(ctx.ra0RB, argumentA)) {
				ctx.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return NEXT;
			}
			return store.execReturn(ctx);
		}

		@Override
		public final InstructionResult getResultType() {

			return null;
		}

		@Override
		public final boolean isRelativeAddressInConstant() {

			return true;
		}
	},
	/**
	 *
	 */
	XESKIPRB1XA_P {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final BaseObject argumentA, final int constant, final ResultHandler store) {

			if (BaseObject.equalsNative(ctx.ra0RB, argumentA)) {
				ctx.ri08IP += constant;
				/** return NEXT - skip other VLIW command parts */
				return NEXT;
			}
			return store.execReturn(ctx);
		}

		@Override
		public final InstructionResult getResultType() {

			return null;
		}

		@Override
		public final boolean isRelativeAddressInConstant() {

			return true;
		}
	},;

	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();

	InstructionA11 instructionCached(final ModifierArgument argumentA, final int constant, final ResultHandler store) {

		if (store == ResultHandler.FA_BNN_NXT) {
			// that way is required, so re-implemented method will be called
			return InstructionA11.instructionCached(argumentA == AA0RB
				? new IA11_AXXX_R_C_NN_NXT(this, constant)
				: new IA11_AXXX_A_C_NN_NXT(this, argumentA, constant));
		}

		return InstructionA11.instructionCached(argumentA == AA0RB
			? new IA11_AXXX_R_C_XX_XXX(this, constant, store)
			: new IA11_AXXX_A_C_XX_XXX(this, argumentA, constant, store));
	}
}
