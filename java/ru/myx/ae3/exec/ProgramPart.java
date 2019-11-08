/*
 * Created on 26.08.2005
 */
package ru.myx.ae3.exec;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.reflect.ControlType;
import ru.myx.ae3.report.Report;

/** @author myx */
public class ProgramPart implements Instruction, /* Function<ExecProcess, BaseObject>, */ ExecFunctionUncheckedResultCode, ExecCallableStepper {

	/** @param instructions
	 * @param buffer
	 * @param ctx
	 * @return */
	public static final StringBuilder dumpCode(final Instruction[] instructions, final StringBuilder buffer, final ExecProcess ctx) {

		final boolean full = ctx == null || Report.MODE_DEVEL;
		final int highlight = ctx == null
			? -1
			: ctx.ri08IP;
		final int start = ctx == null
			? -1
			: ctx.ri0FI0;
		final int limit = ctx == null
			? -1
			: ctx.ri09IL;
		final int breakTarget = ctx == null
			? -1
			: ctx.ri0CBT;
		final int continueTarget = ctx == null
			? -1
			: ctx.ri0DCT;
		final int errorTarget = ctx == null
			? -1
			: ctx.ri0EET;
		final int dumpPrefixSize = Report.MODE_DEVEL
			? Integer.MAX_VALUE
			: Report.MODE_DEBUG
				? 64
				: Report.MODE_ASSERT
					? 32
					: 16;
		final int dumpSufixSize = Report.MODE_DEVEL
			? Integer.MAX_VALUE
			: Report.MODE_DEBUG
				? 32
				: Report.MODE_ASSERT
					? 16
					: 8;
		final int dumpSufixCutSize = Report.MODE_DEVEL
			? Integer.MAX_VALUE
			: Report.MODE_DEBUG
				? 32
				: Report.MODE_ASSERT
					? 16
					: 8;
		final Map<BasePrimitiveNumber, List<String>> comments = new IdentityHashMap<>();
		for (int j = 0; j < instructions.length; j++) {
			final Instruction instr = instructions[j];
			if (instr.isRelativeAddressInConstant()) {
				/** TODO make same info for 'after the last' instruction */
				final int position = buffer.length();
				final int reference = j + instr.getConstant() + 1;
				buffer.append("\t     // referenced from instruction # ");
				ProgramPart.printNumber(buffer, 10, j, 4);
				buffer.append('\n');
				final String comment = buffer.substring(position);
				buffer.setLength(position);
				final BasePrimitiveNumber key = Base.forInteger(reference);
				{
					final List<String> list = comments.get(key);
					if (list != null) {
						list.add(comment);
						continue;
					}
				}
				{
					final List<String> list = new ArrayList<>(4);
					list.add(comment);
					comments.put(key, list);
				}
			}
		}
		for (int j = 0; j < instructions.length; j++) {
			final Instruction instr = instructions[j];
			if (instr instanceof ProgramPart) {
				buffer.append("\t PART\n");
				continue;
			}
			final String code = instr.toCode();
			if (j == start) {
				buffer.append("\t     // **** executable block start (according to I0 register)\n");
			}
			if (j == limit) {
				buffer.append("\t     // **** executable block limit (according to IL register)\n");
			}
			if (j == breakTarget) {
				buffer.append("\t     // **** break target (according to BT register)\n");
			}
			if (j == continueTarget) {
				buffer.append("\t     // **** continue target (according to CT register)\n");
			}
			if (j == errorTarget) {
				buffer.append("\t     // **** error target (according to ET register)\n");
			}
			if (j == highlight) {
				buffer.append("\t     // **** current instruction (according to IP register)\n");
			}
			{
				final BasePrimitiveNumber key = Base.forInteger(j);
				final List<String> list = comments.get(key);
				if (list != null) {
					for (final String comment : list) {
						buffer.append(comment);
					}
				}
			}
			if (instr instanceof IAVV_VFDEBUG_C) {
				if (!full) {
					if (j > highlight + dumpSufixSize && instructions.length - j > dumpSufixCutSize) {
						buffer.append("\t     // **** ").append(instructions.length - j).append(" instructions skipped...\n");
						break;
					}
				}
				buffer.append("\t     // ");
				ProgramPart.printNumber(buffer, 10, j, 4);
				buffer.append(" debug: ");
				buffer.append(((IAVV_VFDEBUG_C) instr).getDebug());
				buffer.append('\n');
				continue;
			}
			if (instr.isRelativeAddressInConstant()) {
				buffer.append("\t     // references instruction # ");
				ProgramPart.printNumber(buffer, 10, j + instr.getConstant() + 1, 4);
				buffer.append('\n');
			}
			buffer.append("\t     ");
			buffer.append(
					j == highlight
						? ">> "
						: "   ");
			ProgramPart.printNumber(buffer, 10, j, 4);
			buffer.append("\t" + code + "\n");
			if (!full) {
				if (j < highlight - dumpPrefixSize) {
					buffer.append("\t     // **** ").append(highlight - dumpPrefixSize - j).append(" instructions skipped...\n");
					j = highlight - dumpPrefixSize;
					continue;
				}
			}
		}
		return buffer;
	}

	private static final void printNumber(final StringBuilder builder, final int radix, final int number, final int length) {

		final String decimal = Integer.toString(number, radix);
		for (int i = length - decimal.length(); i > 0; --i) {
			builder.append('0');
		}
		builder.append(decimal);
	}

	private final Instruction[] instructions;

	int balance = Integer.MIN_VALUE;

	int operands = 0;

	/** @param block */
	public ProgramPart(final Instruction[] block) {

		this.instructions = block;
	}

	@Override
	public BaseObject baseConstructPrototype() {

		return null;
	}

	@Override
	public boolean baseHasInstance(final BaseObject value) {

		return false;
	}

	@Override
	public BaseObject basePrototype() {

		return BaseFunction.PROTOTYPE;
	}

	@Override
	public int execArgumentsAcceptable() {

		return Integer.MAX_VALUE;
	}

	@Override
	public int execArgumentsDeclared() {

		return 0;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) throws Exception {

		// ctx.vmFrameEntryExCallStepper(ctx, this, ctx, null);
		// ctx.vmFrameEntryExFull();
		// ctx.vmFrameEntryExCodeSave();
		ctx.fldCode = this.instructions;
		ctx.ri0FI0 = ctx.ri08IP = 0;
		ctx.ri09IL = this.instructions.length;
		return ExecStateCode.REPEAT;
		// return ctx.vmSetResultCallRepeat(this.instructions, 0,
		// this.instructions.length);
	}

	/** ATTENTION: this method expects disbalanced context state on entry: the new frame should be
	 * entered and SB register should point to the stack entry exactly after this frame's position.
	 * On exit, that frame will be cleared by the callee!
	 *
	 * Returned value is RB register value.
	 *
	 * The frame should be CALL_XXX or CODE_XXX, cause the code instructions and code block Start
	 * and Limit positions will be changed, as well as an Instruction Pointer.
	 *
	 *
	 *
	 * <code>
		ctx.vmFrameEntryExCode();
		return program.execCallInilnePrepared(ctx);
	</code> OR <code>
		ctx.vmFrameEntryExCall(true, entry, program, ExecArgumentsEmpty.INSTANCE, ResultHandler.FA_BNN_NXT);
		ctx.vmScopeDeriveLocals(server.getRootContext());
		ctx.baseDefine("entry", entry);
	 	return program.execCallInilnePrepared(ctx);
	</code>
	 *
	 * @param ctx
	 * @return */
	public final BaseObject execCallPreparedInilne(final ExecProcess ctx) {

		this.execSetupContext(ctx);

		/** <code>
		if (!inline) {
			return ExecStateCode.REPEAT;
		}
		</code> */

		final BaseFunction riOutput = ctx.riOutput;
		try {
			ctx.vmStateToErrorOrSilence(ctx.vmStateFinalizeFrames(ExecStateCode.REPEAT, ctx.ri0BSB - 1, true));
		} finally {
			ctx.riOutput = riOutput;
		}
		return ctx.ra0RB;
		/** FA_BNN_NXT - no need to detach? */
		// return ctx.vmGetResultDetachable();
	}

	@Override
	public Instruction[] execGetCode() {

		return this.instructions;
	}

	@Override
	public int execGetCodeStart() {

		return 0;
	}

	@Override
	public int execGetCodeStop() {

		return this.instructions.length;
	}

	@Override
	public BaseObject execScope() {

		/** executes in real current scope */
		return ExecProcess.GLOBAL;
	}

	@Override
	public void execSetupContext(final ExecProcess ctx) {

		ctx.fldCode = this.instructions;
		ctx.ri0FI0 = ctx.ri08IP = 0;
		ctx.ri09IL = this.instructions.length;
	}

	/** Does executeRaw and replaces RETURN and NEXT with null and creates an ERROR on
	 *
	 * @param ctx
	 * @param result
	 * @param store
	 * @return */
	@SuppressWarnings("unchecked")
	public <T> ExecStateCode executeCall(final ExecProcess ctx, final ControlType<T, ?> result, final ResultHandler store) {

		final ExecStateCode code = this.execCallPrepare(ctx, null, ResultHandler.FA_BNN_NXT, true);
		if (code == null || code == ExecStateCode.RETURN) {
			if (result != null) {
				/** have converter */
				return result.convertJavaToCtxResult(ctx, (T) ctx.ra0RB, store);
			}
			return store.execReturn(ctx, ctx.ra0RB);
		}
		if (code == ExecStateCode.ERROR) {
			final BaseObject error = ctx.vmGetResultImmediate();
			/** TODO: check that it is needed, it is already raised. */
			return error instanceof Throwable
				? ctx.vmRaise((Throwable) error)
				: code;
		}
		if (code == ExecStateCode.NEXT) {
			if (result != null) {
				/** have converter */
				return result.convertJavaToCtxResult(ctx, (T) ctx.ra0RB, store);
			}
			return store.execReturn(ctx, ctx.ra0RB);
		}
		return ctx.vmRaise("Illegal result code for method: code=" + code);
	}

	/** @return code */
	public final Instruction[] getInstructions() {

		return this.instructions;
	}

	@Override
	public final int getOperandCount() {

		return this.operands;
	}

	@Override
	public final int getResultCount() {

		if (this.balance != Integer.MIN_VALUE) {
			return this.balance;
		}
		int balance = 0;
		final Instruction[] code = this.instructions;
		/** FIXME: have to make recursive search really */
		for (int i = code.length - 1; i >= 0; --i) {
			final Instruction current = code[i];
			balance += current.getResultCount() - current.getOperandCount();
		}
		return this.balance = balance;
	}

	/** @return int */
	public final int length() {

		return this.instructions.length;
	}

	@Override
	public final String toCode() {

		final StringBuilder result = new StringBuilder("BLOCK(").append(this.getClass().getSimpleName()).append(")\n");
		return ProgramPart.dumpCode(this.instructions, result, null).toString();
	}

	@Override
	public final String toString() {

		return this.toCode();
	}
}
