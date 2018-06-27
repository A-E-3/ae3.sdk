/*
 * Created on 05.05.2006
 */
package ru.myx.ae3.eval;

import java.util.function.Function;
import ru.myx.ae3.exec.ProgramAssembly;

/**
 * @author myx
 *
 */
public interface LanguageImpl {
	
	/**
	 *
	 */
	public static final LanguageImpl DEFAULT = new LanguageImplDefault();
	
	/**
	 * @param identity
	 *            - compilation identity
	 * @param folder
	 *            - folder with scripts
	 * @param name
	 *            - script name in folder
	 * @param target
	 *            - target assembly for compiled code
	 * @param mode
	 *            - inline scripts should not contain program prolog and epilog
	 *            parts
	 * @throws Evaluate.CompilationException
	 */
	public void compile(final String identity, final Function<String, String> folder, final String name, final ProgramAssembly target, final CompileTargetMode mode)
			throws Evaluate.CompilationException;

	/**
	 * return set of aliases used (and have to) be supported for factory names
	 *
	 * NULL is ok when nothing to return.
	 *
	 * kinda 'ACM.ECMA', 'EVALUATE'
	 *
	 * @return
	 */
	public String[] getAssociatedAliases();
	
	/**
	 * return set of file extensions with leading '.'
	 *
	 * NULL is ok when nothing to return.
	 *
	 * kinda '.js', '.jso'
	 *
	 * @return
	 */
	public String[] getAssociatedExtensions();
	
	/**
	 * return set of mime-types
	 *
	 * NULL is ok when nothing to return.
	 *
	 * kinda 'text/javascript', 'application/x-ae3-js-object'
	 *
	 * @return
	 */
	public String[] getAssociatedMimeTypes();
	
	/**
	 * return common primary unique name
	 *
	 * 'evaluate', 'javascript', 'tpl' are already taken!
	 *
	 * @return
	 */
	public String getKey();
}
