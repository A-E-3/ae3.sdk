/**
 * 
 */
package ru.myx.plist;

import java.io.IOException;

import ru.myx.ae3.AbstractSAPI;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.help.Format;

/**
 * This is a helper API class for EcmaScript (ECMA-252 standard, revision 5.
 * Also known as JavaScript, JScript, etc.).
 * 
 * ECMA-262 standard defines syntax, notation, data types, formats, etc. Some
 * java methods with typical tasks while working with ECMA-262-related stuff are
 * defined in this helper class.
 * 
 * @author myx
 */
public class Plist extends AbstractSAPI {
	
	private static final AbstractPlistImpl ECMA_IMPL;
	
	/**
	 * this block should go last
	 */
	static {
		ECMA_IMPL = AbstractSAPI.createObject("ru.myx.ae3.ecma.ImplementEcma");
	}
	
	static final <T extends Appendable> T ident(final T builder, final int count) throws IOException {
		
		for (int i = count; i > 0; --i) {
			builder.append("  ");
		}
		return builder;
	}
	
	/**
	 * Fragment
	 * 
	 * @param o
	 * @return
	 */
	public static final String toPlistSource(final BaseObject o) {
		
		if (o == null) {
			return "undefined";
		}
		if (o == BaseObject.TRUE) {
			return "<true/>";
		}
		if (o.baseIsPrimitive()) {
			if (o.baseIsPrimitiveString()) {
				return Format.Ecma.string(o.baseToJavaString());
			}
			return o.baseToJavaString();
		}
		try {
			return Plist.toPlistSource(new StringBuilder(), o, 0, 256).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Readable source
	 * 
	 * @param o
	 * @param idented
	 * @param ident
	 * @return
	 */
	public static final String toPlistSource(final BaseObject o, final boolean idented, final int ident) {
		
		if (o == null) {
			return "undefined";
		}
		try {
			if (o.baseIsPrimitive()) {
				if (o.baseIsPrimitiveString()) {
					return ident > 0 && !idented
						? Format.Ecma.string(Plist.ident(new StringBuilder(), ident), o.baseToJavaString()).toString()
						: Format.Ecma.string(o.baseToJavaString());
				}
				return ident > 0 && !idented
					? Plist.ident(new StringBuilder(), ident).append(o.baseToJavaString()).toString()
					: o.baseToJavaString();
			}
			return Plist.toPlistSource(idented
				? Plist.ident(new StringBuilder(), ident)
				: new StringBuilder(), o, ident, 256).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Readable source
	 * 
	 * @param o
	 * @param idented
	 * @param ident
	 * @param limit
	 * @return
	 */
	public static final String toPlistSource(final BaseObject o, final boolean idented, final int ident, final int limit) {
		
		assert o != null : "NULL java value";
		try {
			if (o.baseIsPrimitive()) {
				if (o.baseIsPrimitiveString()) {
					return ident > 0 && !idented
						? Format.Ecma.string(Plist.ident(new StringBuilder(), ident), o.baseToJavaString()).toString()
						: Format.Ecma.string(o.baseToJavaString());
				}
				return ident > 0 && !idented
					? Plist.ident(new StringBuilder(), ident).append(o.baseToJavaString()).toString()
					: o.baseToJavaString();
			}
			return Plist.toPlistSource(idented
				? Plist.ident(new StringBuilder(), ident)
				: new StringBuilder(), o, ident, limit).toString();
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
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
	public static final <T extends Appendable> T toPlistSource(final T builder, final BaseObject o, final int ident, final int limit) throws IOException {
		
		return Plist.ECMA_IMPL.toPlistSource(builder, o, ident, limit);
	}
	
	/**
	 * Complete
	 * 
	 * @param o
	 * @return
	 */
	public static final String toPlistSourceComplete(final BaseObject o) {
		
		final StringBuilder builder = new StringBuilder();
		builder.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n");
		builder.append("<!DOCTYPE plist PUBLIC \"-//Apple//DTD PLIST 1.0//EN\" \"http://www.apple.com/DTDs/PropertyList-1.0.dtd\">\r\n");
		builder.append("<plist version=\"1.0\">");
		
		try {
			Plist.toPlistSource(builder, o, 0, 256);
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		builder.append("</plist>");
		return builder.toString();
	}
	
	private Plist() {
		// ignore
	}
}
