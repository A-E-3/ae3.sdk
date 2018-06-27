/*
 * Code: QuotedPrintableOutputStream.java Originator: Java@Larsan.Net Address:
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

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This class provides an OutputStream that encodes it's content to the Quoted
 * Printable encoding. It is implemented as an FilterOutputStream so that any
 * OutputStream can be wrapped in it to provide the encoding.
 * <p>
 * 
 * For email message body purposes the line length of the Quoted Printable
 * encoding is provided in the static DEFAULT_LINE_LENGTH.
 * <p>
 * 
 * This class separates the different lines by a CRLF sequence according to RFC
 * 1521.
 * <p>
 * 
 * @author Lars J. Nilsson
 * @version 1.0 23/10/00
 */

public class QuotedPrintableOutputStream extends FilterOutputStream {
	
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
	
	// instance data
	private final int			lineLength;
	
	private int					lineCount, lastChar, totalCount;
	
	/**
	 * Construct a new quoted printable output stream wrapped around an input
	 * stream using the default line length (76 characters).
	 * 
	 * @param out
	 */
	public QuotedPrintableOutputStream(final OutputStream out) {
		this( out, QuotedPrintableOutputStream.DEFAULT_LINE_LENGTH );
	}
	
	/**
	 * Construct a new quoted printable output stream wrapped around an input
	 * stream using a custom line length.
	 * 
	 * @param out
	 * @param lineLength
	 */
	public QuotedPrintableOutputStream(final OutputStream out, final int lineLength) {
		super( out );
		this.lineLength = lineLength - 1;
		this.lineCount = 0;
		this.lastChar = -1;
		this.totalCount = 0;
	}
	
	/**
	 * This method checks the line length.
	 * 
	 * @param i
	 * @throws IOException
	 */
	private void checkLineLength(final int i) throws IOException {
		if (this.lineLength > 0 && this.lineCount + i > this.lineLength) {
			this.out.write( '=' );
			this.out.write( '\r' );
			this.out.write( '\n' );
			this.lineCount = 0;
		}
		this.lineCount += i;
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
	 * Write a byte to the output stream.
	 */
	@Override
	public void write(int b) throws IOException {
		// Make sure the int is in range
		b &= 0xFF;
		// We use the last character as an indicator of what to do next,
		// if the last character was a space and it is followed by a line
		// break it must be encoded before we send the next
		if (this.lastChar == ' ') {
			if (b == '\r' || b == '\n') {
				// check for space on the line
				this.checkLineLength( 3 );
				// write hex characters
				this.out.write( '=' );
				this.out.write( QuotedPrintableOutputStream.XLAT[(this.lastChar & 0xF0) >> 4] );
				this.out.write( QuotedPrintableOutputStream.XLAT[this.lastChar & 0xF] );
				this.totalCount += 3;
			} else {
				this.checkLineLength( 1 );
				this.out.write( this.lastChar );
				this.totalCount += 1;
			}
		}
		try {
			if (b == ' ') {
				return; // ignore the space for now
			}
			if (b == '\r') {
				// send a line break here
				this.out.write( '\r' );
				this.out.write( '\n' );
				this.lineCount = 0;
			} else //
			if (b == '\n') {
				if (this.lastChar == '\r') {
					return; // we've already done this
				}
				this.out.write( '\r' );
				this.out.write( '\n' );
				
				this.lineCount = 0;
			} else //
			if (b < 0x20 || b > 0x7E || b == 0x3D) {
				// is the byte is outside the allowed range
				// it must be decoded
				// check for space on the line
				this.checkLineLength( 3 );
				// write hex characters
				this.out.write( '=' );
				this.out.write( QuotedPrintableOutputStream.XLAT[(b & 0xF0) >> 4] );
				this.out.write( QuotedPrintableOutputStream.XLAT[b & 0xF] );
				this.totalCount += 3;
			} else {
				this.checkLineLength( 1 );
				this.out.write( b );
				this.totalCount += 1; // allowed character, just send it
			}
		} finally {
			// the last character is buffered to make sure we
			// handle spaces and new lines correctly
			this.lastChar = b;
		}
	}
}
