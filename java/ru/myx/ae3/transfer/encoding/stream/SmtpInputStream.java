/**
 * Code: MailInputStream.java Originator: Java@Larsan.Net Address:
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

/** This input stream performs two general email specific conversions. First, it makes sure that
 * email new line character sequence - i.e.: the canonical form "\r\n" - is returned as a platform
 * specific new line character sequence. Secondly, it checks for transparent dots - i.e.: if a dot
 * at the beginning of the line is directly followed by another dot only one of them are returned.
 *
 * @author Lars J. Nilsson
 * @version 1.0 06/11/00 */

public class SmtpInputStream extends FilterInputStream {

	private int pos;

	private byte last;

	private boolean haveDot;

	/** Construct a new SmtpInputStream.
	 *
	 * @param in */
	public SmtpInputStream(final InputStream in) {

		super(in);
		this.last = -1;
		this.haveDot = false;
		this.pos = 2;
	}

	/** Not implemented! Will throw an IOException if used. */
	@Override
	public int available() throws IOException {

		throw new IOException("Method not suported: available()");
	}

	/** Not implemented! */
	public void mark() {

		// empty
	}

	/** Checks if the stream supports marks, which it does not. */
	@Override
	public boolean markSupported() {

		return false;
	}

	/** Read a single byte from the stream. This method will return a byte value as an integer and
	 * mark the end of the stream with -1. */
	@Override
	public int read() throws IOException {

		switch (this.pos) {
			case 0 :
				return '\r';
			case 1 :
				return '\n';
			default :
		}
		final int tmp = this.in.read();
		if (tmp == '\r') {
			this.last = (byte) '\r';
			this.pos = 0;
			return this.read();
		}
		if (tmp == '\n') {
			if (this.last != '\r') {
				this.last = (byte) '\r';
				this.pos = 0;
			}
			return this.read();
		}
		if (tmp == '.') {
			if (this.last == '.' && this.haveDot) {
				this.haveDot = false;
				return this.read();
			}
			this.haveDot = this.last == '\r';
			this.last = (byte) '.';
			return '.';
		}
		this.last = (byte) tmp;
		return tmp;
	}

	/** Read bytes from stream into array. Will return -1 if the stream is ended or otherwise the
	 * number of bytes read. */
	@Override
	public int read(final byte[] out) throws IOException {

		return this.read(out, 0, out.length);
	}

	/** Read bytes from stream into array starting at <code>startAt</code>, reading
	 * <code>length</code> number of bytes. Will return -1 if the stream is ended or otherwise the
	 * number of bytes read. */
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

	/** Not implemented! */
	@Override
	public void reset() {

		// empty
	}

	/** Not implemented! Will throw an IOException if used. */
	@Override
	public long skip(final long skip) throws IOException {

		throw new IOException("Method not suported: skip()");
	}
}
