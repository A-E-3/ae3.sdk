/*
 * Code: Base64InputStream.java Originator: Java@Larsan.Net Address:
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
 * This class provides an InputStream for base 64 encoded input. It is
 * implemented as an FilterInputStream so that any InputStream can be wrapped in
 * it to provide the decoding.
 * <p>
 * 
 * The base 64 encoding is defined in RFC 1522. Basically it splits 3 bytes (3 *
 * 8 bits) into four groups (4 * 6 bits) which is represented by a character
 * value. This behavior makes sure that no bit of a higher order than 6 is sent
 * to a stream. This input stream reverses the behavior and merges 4 groups of
 * bytes (4 * 6) into three fully qualified bytes.
 * 
 * @author Lars J. Nilsson
 * @version 1.0 23/10/00
 */
public class Base64InputStream extends FilterInputStream {
	// base 64 character scheme
	private static final char[]	XLAT	= {
			'A',
			'B',
			'C',
			'D',
			'E',
			'F',
			'G',
			'H',
			'I',
			'J',
			'K',
			'L',
			'M',
			'N',
			'O',
			'P',
			'Q',
			'R',
			'S',
			'T',
			'U',
			'V',
			'W',
			'X',
			'Y',
			'Z',
			'a',
			'b',
			'c',
			'd',
			'e',
			'f',
			'g',
			'h',
			'i',
			'j',
			'k',
			'l',
			'm',
			'n',
			'o',
			'p',
			'q',
			'r',
			's',
			't',
			'u',
			'v',
			'w',
			'x',
			'y',
			'z',
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
			'+',
			'/'						};
	
	// this is the encoded character's values
	private static final byte[]	MATRIX	= new byte[256];
	static {
		// fill values so that the matrix holds every encoded characters
		// actual value at it's own position in the matrix
		for (int i = 0; i < Base64InputStream.XLAT.length; ++i) {
			Base64InputStream.MATRIX[Base64InputStream.XLAT[i]] = (byte) i;
		}
	}
	
	// instance data
	private final byte[]		readBuffer;
	
	private final char[]		decodeBuffer;
	
	private int					readBufferIndex;
	
	private int					readBufferSize;
	
	/**
	 * Create a new input stream for reading a base 64 encoded source.
	 * 
	 * @param in
	 */
	public Base64InputStream(final InputStream in) {
		super( in );
		this.readBuffer = new byte[3];
		this.decodeBuffer = new char[4];
		this.readBufferIndex = 0;
		this.readBufferSize = 0;
	}
	
	/**
	 * Not implemented! Will throw an IOException if used.
	 */
	@Override
	public int available() throws IOException {
		throw new IOException( "Method not suported: available()" );
	}
	
	/**
	 * This method decodes four encoded characters into three unencoded bytes in
	 * the read buffer and return the size of the buffer. (Ie: zero if we're at
	 * the end of the stream).
	 * 
	 * @return int
	 * @throws IOException
	 */
	private int fillBuffer() throws IOException {
		// load characters to a decoding character buffer
		// and count the characters to make sure we've got
		// an valid encoding on our hands
		for (int charCount = 0; charCount < 4; charCount++) {
			int tmp = -1;
			do {
				tmp = this.in.read();
				if (tmp == -1) {
					return 0;
				}
			} while (tmp == '\n' || tmp == '\r');
			this.decodeBuffer[charCount] = (char) tmp;
		}
		// get the byte representation of the two first characters
		final byte b_1 = Base64InputStream.MATRIX[this.decodeBuffer[0] & 0xff];
		final byte b_2 = Base64InputStream.MATRIX[this.decodeBuffer[1] & 0xff];
		// decode the first real byte by shifting the bytes from previous step
		// to their real position and then "merge" them (bitwise OR)
		this.readBuffer[0] = (byte) (b_1 << 2 & 0xfc | b_2 >>> 4 & 0x3);
		// character 3 and 4 can be '=' characters to mark the end of the file,
		// but if they are not - decode them too
		if (this.decodeBuffer[2] != '=') {
			final byte b_3 = Base64InputStream.MATRIX[this.decodeBuffer[2] & 0xff];
			this.readBuffer[1] = (byte) (b_2 << 4 & 0xf0 | b_3 >>> 2 & 0xf);
			if (this.decodeBuffer[3] != '=') {
				final byte b_4 = Base64InputStream.MATRIX[this.decodeBuffer[3] & 0xff];
				this.readBuffer[2] = (byte) (b_3 << 6 & 0xc0 | b_4 & 0x3f);
				return 3;
			}
			this.readBuffer[2] = -1;
			return 2;
		}
		this.readBuffer[1] = -1;
		this.readBuffer[2] = -1;
		return 1;
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
		// if we have decoded bytes ready, return them
		if (this.readBufferIndex < this.readBufferSize) {
			return this.readBuffer[this.readBufferIndex++] & 0xFF;
		}
		this.readBufferSize = this.fillBuffer();
		this.readBufferIndex = 0;
		if (this.readBufferSize == 0) {
			return -1;
		}
		return this.read();
	}
	
	/**
	 * Read bytes from stream into an array. Will return the number of bytes
	 * read or -1 if we're at the end of the stream.
	 */
	@Override
	public int read(final byte[] out) throws IOException {
		return this.read( out, 0, out.length );
	}
	
	/**
	 * Read bytes from stream into array starting at <code>startAt</code>,
	 * reading <code>length</code> number of bytes. Will return the number of
	 * bytes read or -1 if we're at the end of the stream.
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
