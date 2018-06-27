package ru.myx.plist;

import java.io.IOException;

import ru.myx.ae3.base.BaseObject;

abstract class AbstractPlistImpl {
	/**
	 * Readable source
	 * 
	 * @param builder
	 * @param o
	 * @param ident
	 * @param limit
	 *            - max depth level
	 * @return
	 * @throws IOException
	 */
	public abstract <T extends Appendable> T toPlistSource(
			final T builder,
			final BaseObject o,
			final int ident,
			final int limit) throws IOException;
}
