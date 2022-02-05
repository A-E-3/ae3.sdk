package ru.myx.ae3.eval;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import java.util.function.Function;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.eval.tokens.TokenInstruction;
import ru.myx.ae3.exec.ExecNonMaskedException;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.OperationsA10;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.exec.ResultHandlerBasic;
import ru.myx.ae3.report.Report;
import ru.myx.renderer.ecma.AcmEcmaLanguageImpl;

/**
 * Title: Base Implementations Description: Copyright: Copyright (c) 2001
 * Company: -= MyX =-
 *
 * @author Alexander I. Kharitchev
 * @version 1.0
 */
public final class Evaluate {
	
	private static final StackTraceElement[] EMPTY_STACK_TRACE = new StackTraceElement[0];
	
	private static final Precompiler PRECOMPILER;
	
	private static final Map<String, LanguageImpl> RENDERERS_BY_KEY;
	
	private static final Map<String, LanguageImpl> RENDERERS_ALL;
	
	/**
	 *
	 */
	public static final LanguageImpl EVALUATE_LANGUAGE_IMPL;
	
	static {
		RENDERERS_BY_KEY = new TreeMap<>();
		RENDERERS_ALL = new HashMap<>(32, 0.1f);
		EVALUATE_LANGUAGE_IMPL = new AcmEvaluateLanguageImpl();
		PRECOMPILER = Report.MODE_DEVEL
			? new CompilerImplDevel()
			: new CompilerImpl();
		
		Evaluate.registerLanguage(Evaluate.EVALUATE_LANGUAGE_IMPL);
		
		/**
		 * TODO: move to SDK later Same code is defined: RendererEcmaMain
		 */
		Evaluate.registerLanguage(AcmEcmaLanguageImpl.INSTANCE);
	}
	
	/**
	 * The result is: NN / NEXT
	 *
	 * @param assembly
	 * @param startOffset
	 *            - program start point, lowest address to optimize till.
	 * @param expression
	 * @throws Evaluate.CompilationException
	 */
	public static final void compileDeclaration(final ProgramAssembly assembly, final int startOffset, final String expression) throws Evaluate.CompilationException {
		
		final TokenInstruction token = Evaluate.PRECOMPILER.parse(assembly, expression, BalanceType.DECLARATION);
		if (token != null) {
			assert token.assertStackValue();
			token.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
	}
	
	/**
	 * @param assembly
	 * @param expression
	 * @param store
	 * @throws Evaluate.CompilationException
	 */
	public static final void compileExpression(final ProgramAssembly assembly, final String expression, final ResultHandlerBasic store) throws Evaluate.CompilationException {
		
		final TokenInstruction token = Evaluate.compileToken(assembly, expression, BalanceType.EXPRESSION);
		assert token.assertStackValue();
		token.toAssembly(assembly, null, null, store);
	}
	
	/**
	 * @param language
	 * @param identity
	 * @param folder
	 * @param name
	 * @return
	 * @throws Evaluate.CompilationException
	 */
	public static final ProgramPart compileProgram(final LanguageImpl language, final String identity, final Function<String, String> folder, final String name)
			throws Evaluate.CompilationException {
		
		assert language != null : "Language is NULL";
		final ProgramAssembly assembly = new ProgramAssembly();
		assembly.addDebug(language.getKey() + ", identity=" + identity);
		language.compile(identity, folder, name, assembly, CompileTargetMode.STANDALONE);
		return assembly.toProgram(0);
	}
	
	/**
	 *
	 * @author myx
	 *
	 */
	@SuppressWarnings("serial")
	public static class CompilationException extends RuntimeException {
		
		/**
		 *
		 * @param message
		 * @param parent
		 */
		public CompilationException(final String message, final Throwable parent) {
			super(message, parent);
		}
		
		/**
		 *
		 * @param parent
		 */
		public CompilationException(final Throwable parent) {
			super(parent);
		}
	}
	
	/**
	 * @param language
	 * @param identity
	 * @param source
	 * @return
	 * @throws Evaluate.CompilationException
	 */
	public static final ProgramPart compileProgram(final LanguageImpl language, final String identity, final String source) //
			throws Evaluate.CompilationException {
		
		return Evaluate.compileProgram(language, identity, new FolderOneSource("anonymous", source), "anonymous");
	}
	
	/**
	 * @param language
	 * @param identity
	 * @param source
	 * @param assembly
	 * @throws Evaluate.CompilationException
	 */
	public static final void compileProgramInline(final LanguageImpl language, final String identity, final String source, final ProgramAssembly assembly) //
			throws Evaluate.CompilationException {
		
		assert language != null : "Language is NULL";
		language.compile(identity, new FolderOneSource("anonymous", source), "anonymous", assembly, CompileTargetMode.INLINE);
	}
	
	/**
	 * @param language
	 * @param identity
	 * @param source
	 * @param assembly
	 * @throws Evaluate.CompilationException
	 */
	public static final void compileProgramBlock(final LanguageImpl language, final String identity, final String source, final ProgramAssembly assembly) //
			throws Evaluate.CompilationException {
		
		assert language != null : "Language is NULL";
		language.compile(identity, new FolderOneSource("anonymous", source), "anonymous", assembly, CompileTargetMode.BLOCK);
	}
	
	/**
	 * @param language
	 * @param identity
	 * @param source
	 * @return
	 */
	public static ProgramPart compileProgramSilent(final LanguageImpl language, final String identity, final String source) {
		
		try {
			return Evaluate.compileProgram(language, identity, source);
		} catch (final Throwable t) {
			final String text = "Error compiling script at " + identity;
			Report.exception("LazyCompilation", text, t);
			final Error e = new Error(text, t);
			e.setStackTrace(Evaluate.EMPTY_STACK_TRACE);
			final ProgramAssembly assembly = new ProgramAssembly();
			try {
				assembly.addDebug("LazyCompilation: identity=" + identity + ", compilation error");
				assembly.addInstruction(OperationsA10.XFLOAD_P.instruction(Base.forThrowable(e), null, 0, ResultHandler.FB_BNN_ERR));
			} catch (final Exception e1) {
				throw new RuntimeException("Cannot generate embedded error script on previous error!", t);
			}
			return assembly.toProgram(0);
		}
	}
	
	/**
	 * Returns: NN / NEXT
	 *
	 * @param assembly
	 * @param startOffset
	 *            - program start point, lowest address to optimize till.
	 * @param expression
	 * @throws Evaluate.CompilationException
	 */
	public static final void compileStatement(final ProgramAssembly assembly, final int startOffset, final String expression) //
			throws Evaluate.CompilationException {
		
		final TokenInstruction token = Evaluate.PRECOMPILER.parse(assembly, expression, BalanceType.STATEMENT);
		if (token != null) {
			assert token.assertStackValue();
			token.toAssembly(assembly, null, null, ResultHandler.FA_BNN_NXT);
		}
	}
	
	/**
	 * @param assembly
	 * @param expression
	 * @param balanceType
	 * @return
	 */
	public static final TokenInstruction compileToken(final ProgramAssembly assembly, final String expression, final BalanceType balanceType) {
		
		return Evaluate.PRECOMPILER.parse(assembly, expression, balanceType);
	}
	
	/**
	 * @param expression
	 * @param ctx
	 * @param constants
	 * @return boolean
	 */
	public static final boolean evaluateBoolean(final String expression, final ExecProcess ctx, final List<Object> constants) {
		
		final ProgramAssembly assembly = new ProgramAssembly(ctx);
		if (constants != null) {
			for (final Object constant : constants) {
				assembly.constantRegister(Base.forUnknown(constant));
			}
		}
		try {
			/**
			 * TODO add BOOLEAN balance type, reason: to skip creation of {} or
			 * [].
			 */
			final TokenInstruction token = Evaluate.PRECOMPILER.parse(assembly, expression, BalanceType.EXPRESSION);
			assert token.assertStackValue();
			final BaseObject constant = token.toConstantValue();
			if (constant != null) {
				return constant.baseToJavaBoolean();
			}
			
			token.toAssembly(assembly, null, null, ResultHandler.FB_BNN_RET);
			ctx.vmFrameEntryExCode();
			return assembly.toProgram(0).execCallPreparedInilne(ctx).baseToJavaBoolean();
			
			// return assembly.toProgram(0).callN(ctx,
			// ctx.rb4CT).baseToJavaBoolean();
		} catch (final ExecNonMaskedException e) {
			throw e;
		} catch (final Error e) {
			throw e;
		} catch (final Throwable e) {
			throw new RuntimeException("Expression: " + expression, e);
		}
	}
	
	/**
	 * @param expression
	 * @param ctx
	 * @param constants
	 * @return object array
	 */
	public static final BaseList<?> evaluateList(final String expression, final ExecProcess ctx, final List<Object> constants) {
		
		final ProgramAssembly assembly = new ProgramAssembly(ctx);
		if (constants != null) {
			for (final Object constant : constants) {
				assembly.constantRegister(Base.forUnknown(constant));
			}
		}
		try {
			final TokenInstruction token = Evaluate.PRECOMPILER.parse(assembly, expression, BalanceType.ARRAY_LITERAL);
			assert token.assertStackValue();
			
			token.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			ctx.vmFrameEntryExCode();
			final BaseObject object = assembly.toProgram(0).execCallPreparedInilne(ctx);
			
			// final BaseObject object = assembly.toProgram(0).callN(ctx,
			// ctx.rb4CT);
			
			assert object instanceof BaseList : "ARRAY balance type assumes that result will be an instance of BaseNativeArray, but: " + (object == null
				? null
				: object.getClass().getName());
			return (BaseList<?>) object;
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Error e) {
			throw e;
		} catch (final Throwable e) {
			throw new RuntimeException("Expression: " + expression, e);
		}
	}
	
	/**
	 * @param expression
	 * @param ctx
	 * @param constants
	 * @return object
	 */
	public static final BaseObject evaluateObject(final String expression, final ExecProcess ctx, final List<Object> constants) {
		
		final ProgramAssembly assembly = new ProgramAssembly(ctx);
		if (constants != null) {
			for (final Object constant : constants) {
				assembly.constantRegister(Base.forUnknown(constant));
			}
		}
		try {
			final TokenInstruction token = Evaluate.PRECOMPILER.parse(assembly, expression, BalanceType.EXPRESSION);
			assert token.assertStackValue();
			final BaseObject constant = token.toConstantValue();
			if (constant != null) {
				return constant;
			}
			
			token.toAssembly(assembly, null, null, ResultHandler.FB_BNN_RET);
			ctx.vmFrameEntryExCode();
			return assembly.toProgram(0).execCallPreparedInilne(ctx);
			
			// token.toAssembly(assembly, null, null, ResultHandler.FC_PNN_RET);
			
			// return assembly.toProgram(0).callN(ctx, ctx.rb4CT);
		} catch (final ExecNonMaskedException e) {
			throw e;
		} catch (final Error e) {
			throw e;
		} catch (final Throwable e) {
			throw new RuntimeException("Expression: " + expression, e);
		}
	}
	
	/**
	 * @param expression
	 * @param ctx
	 * @param constants
	 */
	public static final void evaluateVoid(final String expression, final ExecProcess ctx, final List<Object> constants) {
		
		final ProgramAssembly assembly = new ProgramAssembly(ctx);
		if (constants != null) {
			for (final Object constant : constants) {
				assembly.constantRegister(Base.forUnknown(constant));
			}
		}
		try {
			final TokenInstruction token = Evaluate.PRECOMPILER.parse(assembly, expression, BalanceType.STATEMENT);
			if (token == null) {
				return;
			}
			assert token.assertStackValue();
			final BaseObject constant = token.toConstantValue();
			if (constant != null) {
				return;
			}
			
			token.toAssembly(assembly, null, null, ResultHandler.FU_BNN_RET);
			ctx.vmFrameEntryExCode();
			assembly.toProgram(0).execCallPreparedInilne(ctx);
			return;
			
			// TODO: FU_VNN_RET maybe?
			
			// token.toAssembly(assembly, null, null, ResultHandler.FC_PNN_RET);
			
			// assembly.toProgram(0).callV(ctx, ctx.rb4CT);
		} catch (final ExecNonMaskedException e) {
			throw e;
		} catch (final Error e) {
			throw e;
		} catch (final Throwable e) {
			throw new RuntimeException("Expression: " + expression, e);
		}
	}
	
	/**
	 * @param alias
	 * @return
	 */
	public static final LanguageImpl getLanguageImpl(final String alias) {
		
		return Evaluate.RENDERERS_ALL.get(alias);
	}
	
	/**
	 * @return renderers map
	 */
	public static final Set<Map.Entry<String, LanguageImpl>> getRenderers() {
		
		return Collections.unmodifiableSet(Evaluate.RENDERERS_BY_KEY.entrySet());
	}
	
	/**
	 * @param expression
	 * @param constants
	 * @return instruction
	 */
	public static final ProgramPart prepareFunctionObjectForExpression(final String expression, final List<Object> constants) {
		
		final ProgramAssembly assembly = new ProgramAssembly();
		if (constants != null) {
			for (final Object constant : constants) {
				assembly.constantRegister(Base.forUnknown(constant));
			}
		}
		try {
			final TokenInstruction token = Evaluate.PRECOMPILER.parse(assembly, expression, BalanceType.EXPRESSION);
			assert token.assertStackValue();
			token.toAssembly(assembly, null, null, ResultHandler.FB_BSN_NXT);
			return assembly.toProgram(0);
		} catch (final Throwable e) {
			throw new RuntimeException("EXPR: " + expression, e);
		}
	}
	
	/**
	 * @param language
	 */
	public static final void registerLanguage(final LanguageImpl language) {
		
		assert language != null;
		{
			final String key = language.getKey();
			assert key != null;
			assert !Evaluate.RENDERERS_BY_KEY.containsKey(key) : "language already registered, key=" + key + ", language=" + language;
			Evaluate.RENDERERS_BY_KEY.put(key, language);
			Evaluate.RENDERERS_ALL.put(key, language);
		}
		for (final String[] keys : new String[][]{
				null, //
				language.getAssociatedAliases(), //
				language.getAssociatedExtensions(), //
				language.getAssociatedMimeTypes(), //
		}) {
			if (keys == null || keys.length == 0) {
				continue;
			}
			for (final String key : keys) {
				if (key == null || key.length() == 0) {
					assert false : "null or empty key: " + Arrays.asList(keys);
					continue;
				}
				Evaluate.RENDERERS_ALL.put(key, language);
			}
		}
	}
	
	private Evaluate() {
		// empty
	}
	
}
