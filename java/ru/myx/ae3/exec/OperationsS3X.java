/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.vm_vliw32_2010.InstructionIA;
import ru.myx.vm_vliw32_2010.OperationA3X;

/** @author myx */
public enum OperationsS3X implements OperationA3X {
	/** INTEGER KEY */
	VASTORE_NI {

		@Override

		public final ExecStateCode
				execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			final BaseObject value = ExecProcess.vmEnsureNative(argumentC);
			return argumentA.vmPropertyDefine(ctx, argumentB.baseToJavaInteger(), argumentB, value, store);
		}

		@Override
		public final InstructionResult getResultType() {

			return InstructionResult.OBJECT;
		}
	},
	/** STRING KEY */
	VASTORE_NS {

		@Override

		public final ExecStateCode
				execute(final ExecProcess ctx, final BaseObject argumentA, final BaseObject argumentB, final BaseObject argumentC, final int constant, final ResultHandler store) {

			final BaseObject value = ExecProcess.vmEnsureNative(argumentC);

			/** Known to have character propertyName, do not check for array access (implicitly) */
			// return argumentA.vmPropertyDefine(ctx, argumentB, value);

			argumentA.baseDefine((CharSequence) argumentB, value, BaseProperty.ATTRS_MASK_WED);
			return store.execReturn(ctx, value);
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
