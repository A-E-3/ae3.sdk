package ru.myx.ae3.eval.collection;

import java.util.function.Function;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.exec.ProgramPart;

/**
 * @author myx
 */
public interface RenderCollection {
	
	/**
	 * @return function
	 */
	Function<String, TransferBuffer> getBinarySource();
	
	/**
	 * @return function
	 */
	Function<String, String> getCharacterSource();
	
	/**
	 * @param name
	 * @return
	 * @throws Throwable
	 */
	ProgramPart prepare(final String name) throws Throwable;
}
