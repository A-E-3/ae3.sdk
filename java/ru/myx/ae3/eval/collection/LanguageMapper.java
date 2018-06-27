package ru.myx.ae3.eval.collection;

import ru.myx.ae3.eval.LanguageImpl;

/**
 * @author myx
 * 
 */
public interface LanguageMapper {
	/**
	 * 
	 */
	LanguageMapper	DEFAULT	= new LanguageMapperAutoByExtension();
	
	/**
	 * @param name
	 * @return
	 */
	LanguageImpl getLanguage(final String name);
}
