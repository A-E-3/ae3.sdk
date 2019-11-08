/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseNativeArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.vm_vliw32_2010.OperationA00;

/** @author myx */
public enum OperationsA00 implements OperationA00 {
	/**
	 *
	 */
	XCARRAY_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			if (constant == 0) {
				return store.execReturn(ctx, new BaseNativeArray(0));
			}
			final BaseNativeArray array = new BaseNativeArray(constant);
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant; i > 0; --i) {
				array.putAppend(stack[rASP - i]);
				stack[rASP - i] = null;
			}
			ctx.ri0ASP -= constant;
			return store.execReturn(ctx, array);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.ARRAY;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant;
		}
	},
	/**
	 *
	 */
	XCOBJECT_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			if (constant == 0) {
				return store.execReturn(ctx, BaseObject.createObject());
			}
			final BaseObject object = BaseObject.createObject();
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant; i > 0; --i) {
				final BaseObject key = stack[rASP - i * 2 - 1];
				final BaseObject value = stack[rASP - i * 2 - 0];
				stack[rASP - i * 2 - 1] = null;
				stack[rASP - i * 2 - 0] = null;
				object.baseDefine(key.baseToString(), value, BaseProperty.ATTRS_MASK_WED);
			}
			ctx.ri0ASP -= constant * 2;
			return store.execReturn(ctx, object);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant * 2;
		}
	},
	/**
	 *
	 */
	XCSANDBOX_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			if (constant == 0) {
				return store.execReturn(ctx, ctx.vmScopeCreateSandbox(ExecProcess.GLOBAL));
			}
			final BaseObject object = ctx.vmScopeCreateSandbox(ExecProcess.GLOBAL);
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant * 2; i > 0; i -= 2) {
				final BaseObject key = stack[rASP - i - 1];
				final BaseObject value = stack[rASP - i - 0];
				object.baseDefine(key.baseToString(), value, BaseProperty.ATTRS_MASK_WED);
				stack[rASP - i - 1] = null;
				stack[rASP - i - 0] = null;
			}
			ctx.ri0ASP -= constant * 2;
			return store.execReturn(ctx, object);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant * 2;
		}
	},
	/**
	 *
	 */
	XCSCOPE_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			if (constant == 0) {
				return store.execReturn(ctx, ctx.vmScopeDeriveLocals());
			}
			final BaseObject object = ctx.vmScopeDeriveLocals();
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant * 2; i > 0; i -= 2) {
				final BaseObject key = stack[rASP - i - 1];
				final BaseObject value = stack[rASP - i - 0];
				object.baseDefine(key.baseToString(), value, BaseProperty.ATTRS_MASK_WED);
				stack[rASP - i - 1] = null;
				stack[rASP - i - 0] = null;
			}
			ctx.ri0ASP -= constant * 2;
			ctx.ra0RB = object;
			return store.execReturn(ctx, object);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant * 2;
		}
	},
	/**
	 *
	 */
	XCSCOPECTX_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			if (constant == 0) {
				return store.execReturn(ctx, ctx.vmScopeDeriveContextFromFV());
			}
			final BaseObject object = ctx.vmScopeDeriveContextFromFV();
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant * 2; i > 0; i -= 2) {
				final BaseObject key = stack[rASP - i - 1];
				final BaseObject value = stack[rASP - i - 0];
				object.baseDefine(key.baseToString(), value, BaseProperty.ATTRS_MASK_WED);
				stack[rASP - i - 1] = null;
				stack[rASP - i - 0] = null;
			}
			ctx.ri0ASP -= constant * 2;
			return store.execReturn(ctx, object);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant * 2;
		}
	},
	/**
	 *
	 */
	XCVOID_P {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			if (constant == 0) {
				return store.execReturnUndefined(ctx);
			}
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant; i > 0; --i) {
				stack[rASP - i] = null;
			}
			ctx.ri0ASP -= constant;
			return store.execReturnUndefined(ctx);
		}

		@Override
		public final InstructionResult getResultType() {

			return null;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant;
		}
	},
	/**
	 *
	 */
	XFDEBUG_P {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			return ctx.vmRaise("Oops, virtual!");
		}

		@Override
		public final InstructionResult getResultType() {

			return null;
		}
	},
	/**
	 *
	 */
	XFOTBLDR_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			final BaseFunction previous = ctx.execOutputReplace(new ExecOutputBuilder(new StringBuilder(256)));
			return store.execReturn(
					ctx,
					previous == null
						? BaseObject.NULL
						: previous);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

	},
	/**
	 *
	 */
	XFOTNULL_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			final BaseFunction previous = ctx.execOutputReplace(null);
			return store.execReturn(
					ctx,
					previous == null
						? BaseObject.NULL
						: previous);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

	},
	/**
	 *
	 */
	XFPULLGV_N {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			if (constant == 0) {
				final BaseObject r7RR = ctx.ri10GV = ctx.rb7FV;
				return store.execReturn(ctx, r7RR);
			}
			final BaseObject object = ctx.ri10GV = ctx.rb7FV;
			final BaseObject[] stack = ctx.stackRaw();
			final int rASP = ctx.ri0ASP;
			for (int i = constant * 2; i > 0; i -= 2) {
				final BaseObject key = stack[rASP - i - 1];
				final BaseObject value = stack[rASP - i - 0];
				object.baseDefine(key.baseToString(), value, BaseProperty.ATTRS_MASK_WED);
				stack[rASP - i - 1] = null;
				stack[rASP - i - 0] = null;
			}
			ctx.ri0ASP -= constant * 2;
			return store.execReturn(ctx, object);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return constant * 2;
		}
	},

	/** STACK LOAD: Relative to SB (Stack Base) */
	XFSB_LOAD_D {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			return store.execReturn(ctx, ctx.fldStack[ctx.ri0ASP - constant]);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return 0;
			/** TODO: should have had: <code>
			return constant;
			 * </code>
			 *
			 * for assembly checks. But there is no reverse part yet (getStackOutputCount) for
			 * correct balance. */
		}
	},
	/** STACK LOAD: Relative to SP (Stack Pointer) */
	XFSP_LOAD_D {

		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant, final ResultHandler store) {

			return store.execReturn(ctx, ctx.fldStack[ctx.ri0ASP - constant]);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

		@Override
		public final int getStackInputCount(final int constant) {

			return 0;
			/** TODO: should have had: <code>
			return constant;
			 * </code>
			 *
			 * for assembly checks. But there is no reverse part yet (getStackOutputCount) for
			 * correct balance. */
		}
	},

	/**
	 *
	 */
	;

	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();

	/** @param constant
	 * @param store
	 * @param state
	 * @return */
	final Instruction instructionCached(//
			final int constant,
			final ResultHandler store) {

		return InstructionA00.instructionCached(this.instruction(constant, store));
	}

}
