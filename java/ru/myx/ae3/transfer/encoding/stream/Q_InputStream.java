/*
 * Code: Q_InputStream.java Originator: Java@Larsan.Net Address:
 * www.larsan.net/java/ Contact: webmaster@larsan.net
 * 
 * Copyright (C) 2000 Lars J. Nilsson
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 */

package ru.myx.ae3.transfer.encoding.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides an InputStream for reading "Q" encoded data in mail
 * headers. It extends the QuotedPrintableInputStream.
 * <p>
 * 
 * @author Lars J. Nilsson
 * @version 1.0 23/10/00
 */

public class Q_InputStream extends FilterInputStream {
	
	// the hex notation characters
	private static final char[]	THEX	= {
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
			'F'						};
	
	// the hex character values
	private static final byte[]	MATRIX	= new byte[256];
	
	static {
		// fill the matrix so that matrix[hexCharacter] equals real value
		for (int i = 0; i < Q_InputStream.THEX.length; ++i) {
			Q_InputStream.MATRIX[Q_InputStream.THEX[i]] = (byte) i;
		}
	}
	
	/**
	 * Construct a new Q_InputStream wrapped around the parameter input stream.
	 * 
	 * @param in
	 */
	public Q_InputStream(final InputStream in) {
		super( in );
	}
	
	/**
	 * Not implemented! Will throw an IOException if used.
	 */
	@Override
	public int available() throws IOException {
		throw new IOException( "Method not suported: available()" );
	}
	
	/**
	 * Not implemented!
	 */
	public void mark() {
		// empty
	}
	
	/**
	 * Checks if the stream supports marks, which it does not.
	 */
	@Override
	public boolean markSupported() {
		return false;
	}
	
	/**
	 * Read a single byte from the stream. This method will return a byte value
	 * as an integer or mark the end of the stream with -1.
	 */
	@Override
	public int read() throws IOException {
		for (;;) {
			final int a = this.in.read();
			// underscore represents spaces in this decoding
			// and new lines or returns shouldn't exist, but if they
			// do we'll simply ignore them
			if (a == '_') {
				return ' ';
			}
			if (a == '\n' || a == '\r') {
				continue;
			}
			if (a == '=') {
				final int b = this.in.read();
				if (b == -1) {
					return -1;
				}
				final int c = this.in.read();
				if (c == -1) {
					return -1;
				}
				return Q_InputStream.MATRIX[b] << 4 & 0xF0 | Q_InputStream.MATRIX[c] & 0xF;
			}
			return a;
		}
	}
	
	/**
	 * Read bytes from stream into array. Will return -1 if the stream is ended
	 * or the number of bytes read.
	 */
	@Override
	public int read(final byte[] out) throws IOException {
		return this.read( out, 0, out.length );
	}
	
	/**
	 * Read bytes from stream into array starting at <code>startAt</code>,
	 * reading <code>length</code> number of bytes. Will return -1 if the stream
	 * is ended or the number of bytes read.
	 */
	@Override
	public int read(final byte[] out, final int startAt, final int length) throws IOException {
		int answer = 0;
		for (int i = 0; i < length; ++i) {
			final int tmp = this.read();
			if (tmp == -1) {
				if (i == 0) {
					answer = -1;
				}
				break;
			}
			out[startAt + i] = (byte) tmp;
			answer++;
		}
		return answer;
	}
	
	/**
	 * Not implemented!
	 */
	@Override
	public void reset() {
		// empty
	}
	
	/**
	 * Not implemented! Will throw an IOException if used.
	 */
	@Override
	public long skip(final long skip) throws IOException {
		throw new IOException( "Method not suported: skip()" );
	}
}
