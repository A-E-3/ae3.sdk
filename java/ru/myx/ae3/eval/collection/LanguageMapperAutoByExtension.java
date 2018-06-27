package ru.myx.ae3.eval.collection;

import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.help.FileName;
import ru.myx.ae3.mime.MimeType;

/**
 * @author myx
 * 
 */
final class LanguageMapperAutoByExtension implements LanguageMapper {
	LanguageMapperAutoByExtension() {
		//
	}
	
	/**
	 * @param name
	 * @return
	 */
	@Override
	public LanguageImpl getLanguage(final String name) {
		{
			final String extension = FileName.extensionExact( name );
			final LanguageImpl language = Evaluate.getLanguageImpl( extension );
			if (language != null) {
				return language;
			}
		}
		{
			final String type = MimeType.forName( name, "" ).trim();
			if (type.length() > 0) {
				final LanguageImpl language = Evaluate.getLanguageImpl( type );
				if (language != null) {
					return language;
				}
			}
		}
		{
			return null;
		}
	}
}
