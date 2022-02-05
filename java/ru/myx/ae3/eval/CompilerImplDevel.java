/*
 * Created on 29.10.2003
 * 
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.eval;

import java.util.Set;

import ru.myx.ae3.eval.parse.ExpressionParser;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.help.Create;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.report.ReportReceiver;

/**
 * @author myx
 * 
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
final class CompilerImplDevel implements Precompiler {
	private static final String	OWNER		= "PRECOMPILER";
	
	private static final int	MASK_SETS	= 31;
	
	private static final Set<String>[] createSets() {
		final Set<String>[] result = Convert.Array.toAny( new Set[CompilerImplDevel.MASK_SETS + 1] );
		for (int i = CompilerImplDevel.MASK_SETS; i >= 0; --i) {
			result[i] = Create.privateSet( 64 );
		}
		return result;
	}
	
	private final Set<String>[]	known	= CompilerImplDevel.createSets();
	
	private ReportReceiver		logger	= null;
	
	@Override
	public TokenInstruction parse(final ProgramAssembly assembly, final String expr, final BalanceType balanceType) {
		if (this.logger == null) {
			this.logger = Report.createReceiver( "ae3.evaluate" );
		}
		final String expression = expr.trim();
		final boolean contains;
		{
			final int index = expression.hashCode() & CompilerImplDevel.MASK_SETS;
			final Set<String> checkSet = this.known[index];
			synchronized (checkSet) {
				contains = !checkSet.add( expression );
			}
		}
		final int size = assembly.size();
		final TokenInstruction result = ExpressionParser.parseExpression( assembly, expression, balanceType );
		if (!contains) {
			this.logger.event( CompilerImplDevel.OWNER,
					"prepare",
					Report.MODE_DEVEL
							? "expr: "
									+ expression
									+ "\r\ncode:\r\n"
									+ assembly.dumpCode( new StringBuilder( 1024 ), size, assembly.size() )
							: "expr: " + expression );
		}
		return result;
	}
}
