/*
 * Code: Base64OutputStream.java Originator: Java@Larsan.Net Address:
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

/** This class provides an OutputStream that encodes it's content to the Base 64 encoding. It is
 * implemented as an FilterOutputStream so any OutputStream can be wrapped in it to provide the
 * encoding.
 * <p>
 *
 * The base 64 encoding is defined in RFC 1522. Basically it splits 3 bytes (3 * 8 bits) into four
 * groups (4 * 6 bits) which is represented by a character value. This behavior makes sure that no
 * bit of a higher order than 6 is sent to a stream.
 * <p>
 *
 * For email message body purposes the line length of the base 64 encoding is provided in the static
 * variable DEFAULT_LINE_LENGTH.
 *
 * @author Lars J. Nilsson
 * @version 1.0 23/10/00 */
public class Base64OutputStream extends FilterOutputStream {
	
	/** Default line length, ie: 76. */
	public static final int DEFAULT_LINE_LENGTH = 76;
	
	// instance data
	private static final char[] XLAT = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
	};
	
	private final int lineLength;
	
	private int bufferSize, lineCount, totalCount;
	
	// base 64 character scheme
	private final byte[] buffer;
	
	/** Construct a new Base64OutputStream with default line length (76).
	 *
	 * @param out */
	public Base64OutputStream(final OutputStream out) {
		
		this(out, Base64OutputStream.DEFAULT_LINE_LENGTH);
	}
	
	/** Contruct a new Base64OutputStream. To enable unlimited line length set the
	 * <code>lineLength</code> parameter to -1.
	 *
	 * @param out
	 * @param lineLength */
	public Base64OutputStream(final OutputStream out, final int lineLength) {
		
		super(out);
		this.lineLength = lineLength;
		this.bufferSize = 0;
		this.lineCount = 0;
		this.totalCount = 0;
		this.buffer = new byte[3];
	}
	
	/** Close the stream. This method will force bytes left in the buffer to be encoded to the
	 * stream. */
	@Override
	public void close() throws IOException {
		
		this.flush();
		super.close();
	}
	
	/** This method performs the actual encoding upon the received buffer.
	 *
	 * @throws IOException */
	private void encodeBuffer() throws IOException {
		
		// check line length -> add newLine and reset count
		if (this.lineLength > 0 && this.lineCount + 4 > this.lineLength) {
			this.out.write('\r');
			this.out.write('\n');
			this.lineCount = 0;
		}
		
		// zero fill unused buffer slots
		if (this.bufferSize < 3) {
			switch (this.bufferSize) {
				// если второй пуст
				case 1 :
					this.buffer[1] = 0x00;
					// если третий пуст
					// $FALL-THROUGH$
				case 2 :
					this.buffer[2] = 0x00;
					// $FALL-THROUGH$
				default :
			}
		}
		
		// encode the first byte into two characters
		this.out.write(Base64OutputStream.XLAT[this.buffer[0] >>> 2 & 0x3f]);
		this.out.write(Base64OutputStream.XLAT[this.buffer[0] << 4 & 0x30 | this.buffer[1] >>> 4 & 0x0f]);
		
		// then check the number of bytes to encode and
		// fill with '=' characters if we're at the end of the file
		// and have empty buffer slots
		
		switch (this.bufferSize) {
			case 3 :
				this.out.write(Base64OutputStream.XLAT[this.buffer[1] << 2 & 0x3c | this.buffer[2] >>> 6 & 0x03]);
				this.out.write(Base64OutputStream.XLAT[this.buffer[2] & 0x3f]);
				break;
			
			case 2 :
				this.out.write(Base64OutputStream.XLAT[this.buffer[1] << 2 & 0x3c | this.buffer[2] >>> 6 & 0x03]);
				this.out.write('=');
				break;
			
			case 1 :
				this.out.write('=');
				this.out.write('=');
				break;
			
			default :
		}
		
		// update counts and set buffer size to zero
		this.lineCount += 4;
		this.bufferSize = 0;
		this.totalCount += 4;
	}
	
	/** Flush the stream. This method will force bytes in the buffer to be encoded to the stream. */
	@Override
	public void flush() throws IOException {
		
		if (this.bufferSize > 0) {
			this.encodeBuffer();
		}
	}
	
	/** Get the number of encoded bytes written to the stream. If this method is called before the
	 * stream is closed or flushed there might be trailing bytes left in the buffer.
	 *
	 * @return int */
	public int getWrittenLength() {
		
		return this.totalCount;
	}
	
	/** Write and encode byte array to the stream. This stream is buffered to create the encoded
	 * content, flush or close to empty the buffer. */
	@Override
	public void write(final byte[] in) throws IOException {
		
		for (int pos = 0, left = in.length; left > 0; left--, pos++) {
			this.buffer[this.bufferSize] = in[pos];
			if (++this.bufferSize > 2) {
				this.encodeBuffer();
			}
		}
	}
	
	/** Write and encode a part of a byte array to the stream, starting at <code>startAt</code> and
	 * writing <code>length</code> bytes. This stream is buffered to create the encoded content,
	 * flush or close to empty the buffer. */
	@Override
	public void write(final byte[] in, final int startAt, final int length) throws IOException {
		
		for (int pos = startAt, left = length; left > 0; left--, pos++) {
			this.buffer[this.bufferSize] = in[pos];
			if (++this.bufferSize > 2) {
				this.encodeBuffer();
			}
		}
	}
	
	/** Write a byte to the output stream. The byte may be kept in a buffer for encoding purposes,
	 * the buffer can be cleared by flushing or closing the stream.
	 *
	 * @param b
	 * @throws IOException */
	@Override
	public void write(final int b) throws IOException {
		
		this.buffer[this.bufferSize] = (byte) b;
		if (++this.bufferSize > 2) {
			this.encodeBuffer();
		}
	}
}
