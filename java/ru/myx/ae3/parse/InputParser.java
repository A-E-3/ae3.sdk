package ru.myx.ae3.parse;

import java.io.Reader;

/**
 * @author myx
 * 		
 * @param <V>
 */
public interface InputParser<V> {
	
	/**
	 * @param reader
	 * @return
	 * @throws Exception
	 */
	InputParser<V> parseChunk(Reader reader) throws Exception;
	
	/**
	 * @param string
	 * @return
	 * @throws Exception
	 */
	InputParser<V> parseChunk(String string) throws Exception;
	
	/**
	 * @param characters
	 * @return
	 * @throws Exception
	 */
	InputParser<V> parseChunk(CharSequence characters) throws Exception;
	
	/**
	 * Parser is reset and result object is returned.
	 * 
	 * @return the 'result object' (the meaning and possible values are
	 *         implementation dependent)
	 */
	V parseComplete();
}
