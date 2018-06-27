package ru.myx.ae3.base;

import ru.myx.ae3.eval.LanguageImpl;

/**
 * Source code is kept by
 * 
 * @author myx
 * 
 */
public final class BaseLazyCompilationString extends BaseLazyCompilationAbstract<BasePrimitiveString> {
	
	/**
	 * @param language
	 * @param identity
	 * @param source
	 */
	public BaseLazyCompilationString(final LanguageImpl language,
			final String identity,
			final BasePrimitiveString source) {
		super( language, identity, source );
	}
	
	/**
	 * @param language
	 * @param identity
	 * @param source
	 */
	public BaseLazyCompilationString(final LanguageImpl language, final String identity, final String source) {
		super( language, identity, Base.forString( source ) );
	}
	
	@Override
	protected final String getSourceAsJavaString(final BasePrimitiveString source) {
		return source.stringValue();
	}
	
	@Override
	protected final BasePrimitiveString getSourceAsString(final BasePrimitiveString source) {
		return source;
	}
	
}
