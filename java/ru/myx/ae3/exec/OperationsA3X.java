/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Format;
import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.OperationA3X;

/** @author myx */
public enum OperationsA3X implements OperationA3X {

	/** ACCESS CALL */
	XACALLM {

		@Override

		public final ExecStateCode
				execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			/** CANNOT BE USED WITH GETTERS, execution order is invalid, make RCALL <code>
			final ExecStateCode access = argumentA.vmPropertyRead(ctx, argumentB, BaseObject.UNDEFINED, ResultHandler.FA_BNN_NXT);
			if (access != null) {
				// TODO: check if code should be analyzed
				return access;
			}

			final BaseObject candidate = ctx.ra0RB;
			</code> */
			final BaseObject candidate = argumentA.baseGet(argumentB, BaseObject.UNDEFINED);

			final BaseFunction callee = candidate.baseCall();
			if (callee == null) {
				if (candidate == BaseObject.UNDEFINED) {
					return ctx.vmRaise(
							(argumentA == ctx
								? "Context has no property called "
								: Format.Compact.baseObject(argumentA) + " has no property called ") //
									+ Format.Compact.baseObject(argumentB));
				}
				return ctx.vmRaise("Not a function: key=" + argumentB.baseToString() + ", class=" + candidate.getClass().getName());
			}

			if (argumentC instanceof final BaseArray baseArray) {
				return callee.execCallPrepare(ctx, argumentA, store, false, baseArray);
			}

			if (argumentC instanceof final NamedToIndexMapper mapper) {
				return ctx.vmCall_Generic_MapArgs(callee, argumentA, mapper, store);
			}
			return ctx.vmRaise(
					"Invalid arguments argument: key=" + argumentB.baseToString() + ", class=" + candidate.getClass().getName() + ", argumentsClass: "
							+ argumentC.getClass().getSimpleName());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}
	},
	/**
	 *
	 */
	XACALLO {

		@Override

		public final ExecStateCode
				execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			/** CANNOT BE USED WITH GETTERS, execution order is invalid, make RCALL <code>
			final ExecStateCode access = argumentA.vmPropertyRead(ctx, argumentB, BaseObject.UNDEFINED, ResultHandler.FA_BNN_NXT);
			if (access != null) {
				// TODO: check if code should be analyzed
				return access;
			}

			final BaseObject candidate = ctx.ra0RB;
			</code> */
			final BaseObject candidate = argumentA.baseGet(argumentB, BaseObject.UNDEFINED);

			final BaseFunction callee = candidate.baseCall();
			if (callee == null) {
				if (candidate == BaseObject.UNDEFINED) {
					return ctx.vmRaise(
							(argumentA == ctx
								? "Context has no property called "
								: Format.Compact.baseObject(argumentA) + " has no property called ") //
									+ Format.Compact.baseObject(argumentB));
				}
				return ctx.vmRaise("Not a function: key=" + argumentB.baseToString() + ", class=" + candidate.getClass().getName());
			}
			return callee.execCallPrepare(ctx, argumentA, store, false, argumentC);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

	},
	/**
	 *
	 */
	XASTORE_N {

		@Override

		public final ExecStateCode
				execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			return argumentA.vmPropertyDefine(ctx, argumentB, ExecProcess.vmEnsureNative(argumentC), store);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}

	},
	/** FUNCTION CALL */
	XOCALLM {

		@Override

		public final ExecStateCode
				execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			final BaseFunction callee = argumentA.baseCall();
			if (callee == null) {
				return ctx.vmRaise("Not a function: class=" + argumentA.getClass().getName());
			}

			if (argumentC instanceof final BaseArray baseArray) {
				return callee.execCallPrepare(ctx, argumentB, store, false, baseArray);
			}

			if (argumentC instanceof final NamedToIndexMapper mapper) {
				return ctx.vmCall_Generic_MapArgs(callee, argumentB, mapper, store);
			}
			return ctx.vmRaise("Invalid arguments argument: class=" + argumentA.getClass().getName() + ", argumentsClass: " + argumentC.getClass().getSimpleName());
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}
	},;

	@Override
	public OperationA3X execNativeResult() {

		return this;
	}

	@Override
	public OperationA3X execStackResult() {

		return this;
	}

	/** For ae3-vm-info script
	 *
	 * @return */
	public abstract InstructionResult getResultType();

	InstructionIA instructionCached(//
			final ModifierArgument modifierFilterA,
			final ModifierArgument modifierFilterB,
			final ModifierArgument modifierFilterC,
			final int constant,
			final ResultHandler store) {

		return InstructionA3X.instructionCached(this.instruction(modifierFilterA, modifierFilterB, modifierFilterC, constant, store));
	}
}
