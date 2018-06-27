package ru.myx.ae3.serve;

import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Message;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.transform.Transform;

/**
 * @author myx
 * 
 */
public class Serve {
	
	/**
	 * 
	 * @param query
	 */
	public static final void checkParsePostParameters(final ServeRequest query) {
		if (!query.isEmpty() && "POST".equalsIgnoreCase( query.getVerb() )) {
			final String contentType = Message.cleanAttributeValue( query, "Content-Type", "" ).trim();
			if (contentType.length() > 0) {
				try {
					final BaseObject data = Transform.materialize( BaseObject.class,
							contentType,
							query.getAttributes(),
							query.toBinary().getBinary() );
					if (data != null) {
						for (final Iterator<String> iterator = Base.keys( data ); iterator.hasNext();) {
							final String key = iterator.next();
							query.addParameter( key, data.baseGet( key, BaseObject.UNDEFINED ) );
						}
					}
				} catch (final Throwable t) {
					Report.exception( "SERVE-HELPER", "Error while parsing a query", t );
				}
			}
		}
	}
	
	private Serve() {
		// prevent
	}
}
