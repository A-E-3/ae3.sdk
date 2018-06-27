/*
 *  Code: MailOutputStream.java
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
 * This OutputStream controls the formation of the output stream according to
 * the rules of the SMTP protocol described in RFC 821. It promise that:
 * 
 * 1) No bare line feed will occur. All new lines will be send in the canonical
 * form with a '\r' followed by a '\n'.
 * 
 * 2) All dots ('.') that start a new line will be presented transparent by
 * adding another dot before it. This procedure is described in RFC 821 to make
 * sure no messages are prematurely ended. To end a message or send a
 * non-transparent dot the <code>writeDot</code> method must be used.
 * 
 * @author Lars J. Nilsson
 * @version 1.0.1 29/10/00
 */
public class SmtpOutputStream extends FilterOutputStream {
	
	// char buffer
	private int	lastChar;
	
	/**
	 * Create a new MailOutputStream.
	 * 
	 * @param out
	 */
	public SmtpOutputStream(final OutputStream out) {
		super( out );
		this.lastChar = 0;
	}
	
	/**
	 * Write a byte array to the stream.
	 */
	@Override
	public void write(final byte[] b) throws IOException {
		this.write( b, 0, b.length );
	}
	
	/**
	 * Write a part of a byte array to the stream, starting at
	 * <code>startAt</code> and writing <code>length</code> bytes.
	 */
	@Override
	public void write(final byte[] b, final int startAt, final int length) throws IOException {
		for (int i = 0; i < length; ++i) {
			this.write( b[startAt + i] );
		}
	}
	
	/**
	 * Write a single byte to the stream. This method will search for new line
	 * characters and make sure they get printed as specified by the
	 * <code>getNewLine()</code> method.
	 */
	@Override
	public void write(final int b) throws IOException {
		if (b == '\r') {
			this.writeln();
		} else //
		if (b == '\n') {
			if (this.lastChar != '\r') {
				this.out.write( '\r' );
				this.out.write( '\n' );
			}
		} else //
		if (b == '.' && (this.lastChar == '\r' || this.lastChar == '\n')) {
			// last sequence was a line feed so make the dot transparent
			// by adding another dot before it: two dots at the start of a
			// line will be interpreted as one by the SMTP service
			this.out.write( '.' );
			this.out.write( '.' );
		} else {
			this.out.write( b );
		}
		this.lastChar = b;
	}
	
	/**
	 * Write an a dot ('.') to the output stream without making it transparent.
	 * This method can be used to end a SMTP DATA command.
	 * 
	 * @throws IOException
	 */
	public void writeDot() throws IOException {
		this.out.write( '.' );
	}
	
	/**
	 * Write a canonical new line sequence to the output stream.
	 * 
	 * @throws IOException
	 */
	public void writeln() throws IOException {
		this.out.write( '\r' );
		this.out.write( '\n' );
	}
}
