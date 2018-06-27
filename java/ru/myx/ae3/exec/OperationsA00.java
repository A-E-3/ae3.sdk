/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseNativeArray;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;

/** @author myx */
public enum OperationsA00 implements OperationA00 {
	/**
	 *
	 */
	XCARRAY_N {
		
		@Override
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			if (constant == 0) {
				ctx.ra0RB = new BaseNativeArray(0);
				return null;
			}
			final BaseNativeArray array = new BaseNativeArray(constant);
			final BaseObject[] stack = ctx.fldStack;
			final int rASP = ctx.ri0ASP;
			for (int i = constant; i > 0; --i) {
				array.putAppend(stack[rASP - i]);
				stack[rASP - i] = null;
			}
			ctx.ri0ASP -= constant;
			ctx.ra0RB = array;
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			if (constant == 0) {
				ctx.ra0RB = BaseObject.createObject();
				return null;
			}
			final BaseObject object = BaseObject.createObject();
			final BaseObject[] stack = ctx.fldStack;
			final int rASP = ctx.ri0ASP;
			for (int i = constant; i > 0; --i) {
				final BaseObject key = stack[rASP - i * 2 - 1];
				final BaseObject value = stack[rASP - i * 2 - 0];
				stack[rASP - i * 2 - 1] = null;
				stack[rASP - i * 2 - 0] = null;
				object.baseDefine(key.baseToString(), value, BaseProperty.ATTRS_MASK_WED);
			}
			ctx.ri0ASP -= constant * 2;
			ctx.ra0RB = object;
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			if (constant == 0) {
				ctx.ra0RB = ctx.vmScopeCreateSandbox(ExecProcess.GLOBAL);
				return null;
			}
			final BaseObject object = ctx.vmScopeCreateSandbox(ExecProcess.GLOBAL);
			final BaseObject[] stack = ctx.fldStack;
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
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			if (constant == 0) {
				ctx.ra0RB = ctx.vmScopeDeriveLocals();
				return null;
			}
			final BaseObject object = ctx.vmScopeDeriveLocals();
			final BaseObject[] stack = ctx.fldStack;
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
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			if (constant == 0) {
				ctx.ra0RB = ctx.vmScopeDeriveContextFromFV();
				return null;
			}
			final BaseObject object = ctx.vmScopeDeriveContextFromFV();
			final BaseObject[] stack = ctx.fldStack;
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
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			if (constant == 0) {
				return null;
			}
			final BaseObject[] stack = ctx.fldStack;
			final int rASP = ctx.ri0ASP;
			for (int i = constant; i > 0; --i) {
				stack[rASP - i] = null;
			}
			ctx.ri0ASP -= constant;
			return null;
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
		public final ExecStateCode execute(final ExecProcess process, final int constant) {
			
			return process.vmRaise("Oops, virtual!");
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
		public final ExecStateCode execute(final ExecProcess process, final int constant) {
			
			final BaseFunction previous = process.execOutputReplace(new ExecOutputBuilder(new StringBuilder(256)));
			process.ra0RB = previous == null
				? BaseObject.NULL
				: previous;
			return null;
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
		public final ExecStateCode execute(final ExecProcess process, final int constant) {
			
			final BaseFunction previous = process.execOutputReplace(null);
			process.ra0RB = previous == null
				? BaseObject.NULL
				: previous;
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			if (constant == 0) {
				final BaseObject r7RR = ctx.ri10GV = ctx.rb7FV;
				ctx.ra0RB = r7RR;
				return null;
			}
			final BaseObject object = ctx.ri10GV = ctx.rb7FV;
			final BaseObject[] stack = ctx.fldStack;
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
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			ctx.ra0RB = ctx.fldStack[ctx.ri0ASP - constant];
			return null;
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
		public final ExecStateCode execute(final ExecProcess ctx, final int constant) {
			
			ctx.ra0RB = ctx.fldStack[ctx.ri0ASP - constant];
			return null;
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
	final Instruction instructionCached(final int constant, final ResultHandler store) {
		
		return InstructionA00.instructionCached(this.instruction(constant, store));
	}
	
}
