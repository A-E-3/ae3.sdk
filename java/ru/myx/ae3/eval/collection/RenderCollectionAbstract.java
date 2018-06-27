package ru.myx.ae3.eval.collection;

import ru.myx.ae3.base.BaseHostEmpty;
import ru.myx.ae3.eval.LanguageImpl;

/**
 * BaseObject compatible
 * 
 * @author myx
 */
public abstract class RenderCollectionAbstract extends BaseHostEmpty implements RenderCollection {
	
	/**
	 * 
	 */
	protected LanguageMapper languageMapper;
	
	/**
	 * @param singleLanguage
	 * 			
	 */
	protected RenderCollectionAbstract(final LanguageImpl singleLanguage) {
		this.languageMapper = singleLanguage == null
			? LanguageMapper.DEFAULT
			: new LanguageMapperSingle(singleLanguage);
	}
	
	/**
	 * @param mapper
	 * 			
	 */
	protected RenderCollectionAbstract(final LanguageMapper mapper) {
		assert mapper != null;
		this.languageMapper = mapper;
	}
	
}
