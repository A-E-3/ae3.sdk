package ru.myx.ae3.eval.collection;

import ru.myx.ae3.eval.LanguageImpl;

/**
 * @author myx
 * 
 */
final class LanguageMapperSingle implements LanguageMapper {
	private final LanguageImpl	language;
	
	LanguageMapperSingle(final LanguageImpl language) {
		this.language = language;
	}
	
	/**
	 * @param name
	 * @return
	 */
	@Override
	public LanguageImpl getLanguage(final String name) {
		return this.language;
	}
}
