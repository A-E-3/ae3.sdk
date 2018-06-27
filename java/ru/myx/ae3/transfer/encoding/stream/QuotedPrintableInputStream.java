/*
 *  Code: QuotedPrintableInputStream.java
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

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class provides an InputStream for Quoted Printable encoded streams. It
 * is implemented as an FilterInputStream so that any InputStream can be wrapped
 * in it to provide the decoding.
 * <p>
 * 
 * @author Lars J. Nilsson
 * @version 1.0 23/10/00
 */

public class QuotedPrintableInputStream extends FilterInputStream {
	
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
		for (int i = 0; i < QuotedPrintableInputStream.THEX.length; ++i) {
			QuotedPrintableInputStream.MATRIX[QuotedPrintableInputStream.THEX[i]] = (byte) i;
		}
	}
	
	// instance data
	private int					validBlanks, bufferByte;
	
	/**
	 * Create a new Quoted Printable decoding input stream.
	 * 
	 * @param in
	 */
	public QuotedPrintableInputStream(final InputStream in) {
		super( in );
		this.bufferByte = -1;
		this.validBlanks = 0;
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
	 * as an integer and mark the end of the stream with -1.
	 */
	@Override
	public int read() throws IOException {
		// first count down any valid space characters
		if (this.validBlanks > 0) {
			this.validBlanks--;
			return ' ';
		}
		int a = 0;
		if (this.bufferByte != -1) {
			a = this.bufferByte;
			this.bufferByte = -1;
		} else {
			a = this.in.read();
		}
		// let's see what we got here
		if (a == ' ') {
			// if the space is followed by more spaces just count them
			// but make sure the following none space character makes them
			// valid - if not, dump them - if yes, keep them and buffer the
			// none space character for later use
			while ((a = this.in.read()) == ' ') {
				this.validBlanks++;
			}
			
			if (a == '\n' || a == '\r' || a == -1) {
				this.validBlanks = 0;
			} else {
				this.bufferByte = a;
				a = ' ';
			}
			return a;
		} else //
		if (a == '=') {
			// This is a Quoted Printable encded sequence so get the next byte
			final int b = this.in.read();
			// The line directly below is the correct one... But since anyone
			// can make
			// mistakes, let's see '\n' as a complete soft line break and accept
			// minus one as the end of the stream
			// if(b == '\n' || b == -1) throw new IOException("Invalid syntax in
			// Quoted Printable Stream.");
			
			if (b == '\n') {
				return this.read();
			}
			if (b == -1) {
				return -1;
			}
			if (b == '\r') {
				// This is a soft line break... probably. It should come a '\n'
				// after this byte, so...
				final int c = this.in.read();
				// Again the correct line below has been put as a comment...
				// if(c != '\n') throw new IOException("Invalid syntax in Quoted
				// Printable Stream.");
				if (c != '\n') {
					this.bufferByte = c;
				}
				return this.read(); // soft line break - skip and continue
			}
			// This is a hex sequence so read the next byte and
			// decode them
			final int c = this.in.read();
			return QuotedPrintableInputStream.MATRIX[b] << 4 & 0xF0 | QuotedPrintableInputStream.MATRIX[c] & 0xF;
		} else {
			return a; // nothing special here, just return the
			// character
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
