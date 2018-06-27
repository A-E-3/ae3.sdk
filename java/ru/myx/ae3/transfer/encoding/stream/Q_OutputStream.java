/**
 *  Code: Q_OutputStream.java
 *  Originator: Java@Larsan.Net
 *  Address: www.larsan.net/java/
 *  Contact: webmaster@larsan.net
 *
 *  Copyright (C) 2000 Lars J. Nilsson
 *
 *       This program is free software; you can redistribute it and/or
 *       modify it under the terms of the GNU General Public License
 *       as published by the Free Software Foundation; either version 2
 *       of the License, or (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful,
 *       but WITHOUT ANY WARRANTY; without even the implied warranty of
 *       MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *       GNU General Public License for more details.
 *
 *       You should have received a copy of the GNU General Public License
 *       along with this program; if not, write to the Free Software
 *       Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA. 
 */

package ru.myx.ae3.transfer.encoding.stream;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class provides an OutputStream that encodes it's content to the "Q"
 * encoding for email headers. It extends the QuotedPrintableOutputStream.
 * <p>
 * 
 * @author Lars J. Nilsson
 * @version 1.0.1 28/03/2001
 */
public class Q_OutputStream extends FilterOutputStream {
	
	/** Default line length for must implementations */
	public static final int		DEFAULT_LINE_LENGTH	= 76;
	
	// this is the hex notation characters to use
	private static final char[]	XLAT				= {
			'0',
			'1',
			'2',
			'3',
			'4',
			'5',
			'6',
			'7',
			'8',
			'9',
			'A',
			'B',
			'C',
			'D',
			'E',
			'F'									};
	
	/**
	 * This method checks for special characters in a byte array which should be
	 * encoded in in a Quoted Printable Header and calculates the encoded length
	 * of the array.
	 * <p>
	 * 
	 * @param input
	 * @return int
	 */
	public static int encodedLength(final byte[] input) {
		int count = 0;
		for (final byte element : input) {
			final int ch = element & 0xFF;
			if (ch < 0x20 || ch > 0x7E || Q_OutputStream.isSpecial( ch )) {
				count += 3;
			} else {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * This method checks for special characters in a char array which should be
	 * encoded in in a Quoted Printable Header and calculates the encoded length
	 * of the array.
	 * <p>
	 * 
	 * @param input
	 * @return int
	 */
	public static int encodedLength(final char[] input) {
		int count = 0;
		for (final char element : input) {
			final int ch = element & 0xFF;
			if (ch < 0x20 || ch > 0x7E || Q_OutputStream.isSpecial( ch )) {
				count += 3;
			} else {
				count++;
			}
		}
		return count;
	}
	
	/**
	 * This is where we check every character to see if it is special enough to
	 * be encoded. Usually the specials differs between different parts or
	 * "tokens" of the message to be encoded, but since every character MAY be
	 * encoded and the restriction is linear in the case that it only adds
	 * characters to the forbidden list we can simply encode every special
	 * character and be done with it.
	 * 
	 * @param ch
	 * @return boolean
	 */
	protected static boolean isSpecial(final int ch) {
		switch (ch) {
		case '=':
		case '_':
		case '?':
		case '(':
		case ')':
		case '<':
		case '>':
		case '@':
		case '"':
		case '\'':
		case ',':
		case ';':
		case ':':
		case '\\':
		case '.':
		case '[':
		case ']':
			return true;
			
		default:
			return false;
		}
	}
	
	// instance data
	private int	totalCount;
	
	/**
	 * Construct a new Q_OutputStream wrapped around the provided output stream.
	 * 
	 * @param out
	 */
	public Q_OutputStream(final OutputStream out) {
		super( out );
		this.totalCount = 0;
	}
	
	/**
	 * Get the number of encoded bytes written to the stream.
	 * 
	 * @return int
	 */
	public int getWrittenLength() {
		return this.totalCount;
	}
	
	/**
	 * Write a byte array to the stream.
	 */
	@Override
	public void write(final byte[] in) throws IOException {
		this.write( in, 0, in.length );
	}
	
	/**
	 * Write a part of a byte array to the stream, starting at
	 * <code>startAt</code> and writing <code>length</code> bytes.
	 */
	@Override
	public void write(final byte[] in, final int startAt, final int length) throws IOException {
		for (int i = 0; i < length; ++i) {
			this.write( in[startAt + i] );
		}
	}
	
	/**
	 * Write and encode a byte to the output stream.
	 */
	@Override
	public void write(int b) throws IOException {
		// check the byte range
		b &= 0xFF;
		// we'll send the space character as an underscore
		// which is permitted to make the encoded line easier to
		// understand for mail readers who can't decode it
		if (b == ' ') {
			this.out.write( '_' );
			this.totalCount += 1;
		} else //
		if (b < 0x20 || b > 0x7E || Q_OutputStream.isSpecial( b )) {
			// write hex characters
			this.out.write( '=' );
			this.out.write( Q_OutputStream.XLAT[(b & 0xF0) >> 4] );
			this.out.write( Q_OutputStream.XLAT[b & 0xF] );
			this.totalCount += 3; // special character or out of ASCII range
		} else {
			this.out.write( b );
			this.totalCount += 1; // just send
		}
	}
}
