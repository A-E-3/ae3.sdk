/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.eval;

import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.ProgramAssembly;

interface Precompiler {
	/**
	 * @param assembly
	 * @param expression
	 * @param balanceType
	 * @return
	 */
	TokenInstruction parse(ProgramAssembly assembly, String expression, BalanceType balanceType);
}
