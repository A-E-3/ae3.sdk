/*
 * Created on 11.09.2005
 */
package ru.myx.ae3.eval;

import java.util.function.Function;
import ru.myx.ae3.eval.parse.ExpressionParser;
import ru.myx.ae3.exec.Instructions;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.parse.expression.TokenInstruction;

final class AcmEvaluateLanguageImpl implements LanguageImpl {
	
	@Override
	public final void compile(final String identity, final Function<String, String> folder, final String name, final ProgramAssembly assembly, final CompileTargetMode mode)
			throws Evaluate.CompilationException {
		
		final String source = folder.apply(name);
		if (source == null) {
			return;
		}
		final TokenInstruction parsed = ExpressionParser.parseExpression(assembly, source.trim(), mode != CompileTargetMode.STANDALONE
			? BalanceType.STATEMENT
			: BalanceType.EXPRESSION);
		if (parsed != null) {
			parsed.toAssembly(assembly, null, null, mode == CompileTargetMode.INLINE
				? ResultHandler.FA_BNN_NXT
				: ResultHandler.FC_PNN_RET);
		} else //
		if (mode != CompileTargetMode.INLINE) {
			assembly.addInstruction(Instructions.INSTR_LOAD_UNDEFINED_NN_RETURN);
		}
	}
	
	@Override
	public String[] getAssociatedAliases() {
		
		return new String[]{
				//
				"ACM.CALC", //
				"ACM.EVAL", //
				"ACM.EVALUATE", //
				"CALC", //
				"EVALUATE", //
				"EXPRESSION", //
		};
	}
	
	@Override
	public String[] getAssociatedExtensions() {
		
		return new String[]{
				//
				".jsld", //
				".jso", //
				".jsob", //
				".jse", //
		};
	}
	
	@Override
	public String[] getAssociatedMimeTypes() {
		
		return new String[]{
				//
				"application/x-ae3-js-layout-data", //
				"application/x-ae3-js-object", //
				"application/x-ae3-js-expression", //
		};
	}
	
	@Override
	public String getKey() {
		
		return "ACM.CALC";
	}
}
