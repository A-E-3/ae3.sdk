//******************************************************************************
// Put.java
//******************************************************************************
package ru.myx.sapi;

import java.io.IOException;
import java.io.OutputStream;

//==============================================================================
/**
 * Just a couple of trivial output routines used by other classes in the
 * package. Normally this kind of stuff would be in a separate IO package, but I
 * wanted the present package to be self-contained for ease of distribution and
 * use by others.
 */
final class Gif89Util {
	
	// ----------------------------------------------------------------------------
	/**
	 * Write just the low bytes of a String. (This sucks, but the concept of an
	 * encoding seems inapplicable to a binary file ID string. I would think
	 * flexibility is just what we don't want - but then again, maybe I'm slow.)
	 * 
	 * @param s
	 * @param os
	 * @throws IOException
	 */
	static final void ascii(final String s, final OutputStream os) throws IOException {
		final byte[] bytes = new byte[s.length()];
		for (int i = 0; i < bytes.length; ++i) {
			bytes[i] = (byte) s.charAt( i ); // discard the high byte
		}
		os.write( bytes );
	}
	
	// ----------------------------------------------------------------------------
	/**
	 * Write a 16-bit integer in little endian byte order.
	 * 
	 * @param i16
	 * @param os
	 * @throws IOException
	 */
	static final void leShort(final int i16, final OutputStream os) throws IOException {
		os.write( i16 & 0xff );
		os.write( i16 >> 8 & 0xff );
	}
}
