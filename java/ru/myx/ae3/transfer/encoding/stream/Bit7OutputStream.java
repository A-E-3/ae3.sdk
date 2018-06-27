/*
 * Code: Bit7OutputStream.java Originator: Java@Larsan.Net Address:
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
 * This class confines an output stream to only send the seven first bits in
 * every byte.
 * 
 * @author Lars J. Nilsson
 * @version 1.0 23/10/00
 */

public class Bit7OutputStream extends FilterOutputStream {
	
	/**
	 * Checks if a character is regarded as special and needs hiding.
	 * 
	 * @param ch
	 * @return boolean
	 */
	protected static final boolean isSpecial(final int ch) {
		/**
		 * Here is the specials we want to look for. Usually the specials
		 * differs between different part or "tokens" of the message to be
		 * encoded, but since every character MAY be quoted we can simply quot
		 * every special character and be done with it.
		 */
		switch (ch) {
		case '(':
		case ')':
		case '<':
		case '>':
		case '@':
		case ',':
		case ';':
		case ':':
		case '.':
		case '[':
		case ']':
		case '\"':
		case '\'':
		case '\\':
			return true;
		default:
			return false;
		}
	}
	
	private final boolean	quoteSpecials;
	
	/**
	 * Construct a new Bit7OutputStream.
	 * 
	 * @param out
	 * @param quoteSpecials
	 */
	public Bit7OutputStream(final OutputStream out, final boolean quoteSpecials) {
		super( out );
		this.quoteSpecials = quoteSpecials;
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
			final int o = in[startAt + i] & 0x7F;
			if (this.quoteSpecials && Bit7OutputStream.isSpecial( o )) {
				this.out.write( '\\' );
			}
			this.out.write( o );
		}
	}
	
	/**
	 * Write a the seven first bits of the provided byte to the output stream.
	 */
	@Override
	public void write(final int b) throws IOException {
		final int o = b & 0x7F;
		if (this.quoteSpecials && Bit7OutputStream.isSpecial( o )) {
			this.out.write( '\\' );
		}
		this.out.write( o );
	}
}
