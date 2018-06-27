/*
 * Created on 12.05.2006
 */
package ru.myx.ae3.control.value;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecNonMaskedException;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.report.Report;

/**
 *
 * @author myx
 *
 */
public final class ValueSourceStringEx extends ValueSource<BasePrimitiveString> {
	
	
	private final String attribute;
	
	private final ProgramPart function;
	
	/**
	 *
	 * @param attribute
	 */
	public ValueSourceStringEx(final String attribute) {
		this.attribute = attribute;
		this.function = Evaluate.prepareFunctionObjectForExpression(attribute.substring(1), null);
	}
	
	@Override
	public final BasePrimitiveString getObject(final Object argument) {
		
		
		final ExecProcess ctx = Exec.createProcess(Exec.currentProcess(), "Evaluation Context: " + this.attribute);
		ctx.vmFrameEntryExCode();
		ctx.vmScopeDeriveLocals();
		ctx.baseDefine("Record", Base.forUnknown(argument));
		try {
			return Act.run(ctx, function -> function.execCallPreparedInilne(ctx).baseToString(), this.function);
		} catch (final ExecNonMaskedException e) {
			throw e;
		} catch (final Throwable e) {
			Report.exception("FLD-INTEGER", "bad expression: " + this.attribute, e);
			return null;
		}
	}
}
