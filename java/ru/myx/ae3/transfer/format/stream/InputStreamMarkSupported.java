/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.transfer.format.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

final class InputStreamMarkSupported extends FilterInputStream {
	private byte[]			buffer;
	
	private int				storePosition	= 0;
	
	private int				readPosition	= 0;
	
	private byte[]			toSend;
	
	private int				sendPosition	= -1;
	
	private final byte[]	b				= new byte[1];
	
	InputStreamMarkSupported(final InputStream is) throws java.lang.IllegalArgumentException {
		super( is );
		if (is instanceof InputStreamMarkSupported) {
			throw new java.lang.IllegalArgumentException( "Stream does already support marks!" );
		}
	}
	
	@Override
	public int available() throws IOException {
		return this.buffer != null && this.readPosition != -1
				? this.storePosition - this.readPosition
				: this.in.available();
	}
	
	@Override
	public void close() throws IOException {
		this.toSend = null;
		this.buffer = null;
		super.close();
	}
	
	private int internRead() throws IOException {
		final int next;
		if (this.sendPosition == -1) {
			final int cc = this.in.read( this.b );
			if (cc < 1) {
				return -1;
			}
			next = this.b[0] & 0xFF;
		} else {
			next = this.toSend[this.sendPosition++] & 0xFF;
			if (this.sendPosition == this.toSend.length) {
				this.sendPosition = -1;
				this.toSend = null;
			}
		}
		return next;
	}
	
	@Override
	public void mark(final int limit) {
		if (this.buffer != null && this.readPosition != -1) {
			if (this.sendPosition == -1) {
				this.toSend = this.buffer;
				this.sendPosition = this.readPosition;
			} else {
				final int length = this.storePosition - this.readPosition;
				final int send = this.toSend.length - this.sendPosition;
				final byte[] newToSend = new byte[send + length];
				System.arraycopy( this.toSend, this.sendPosition, newToSend, 0, send );
				System.arraycopy( this.buffer, this.readPosition, newToSend, send, length );
				this.toSend = newToSend;
			}
			this.sendPosition = 0;
		}
		this.buffer = new byte[limit];
		this.storePosition = 0;
		this.readPosition = -1;
	}
	
	@Override
	public boolean markSupported() {
		return true;
	}
	
	@Override
	public int read() throws IOException {
		if (this.buffer != null) {
			if (this.readPosition == -1) {
				final int next = this.internRead();
				if (this.storePosition < this.buffer.length) {
					this.buffer[this.storePosition++] = (byte) next;
				} else {
					this.buffer = null;
					this.storePosition = 0;
					this.readPosition = 0;
				}
				return next;
			}
			try {
				return this.buffer[this.readPosition++] & 0xFF;
			} finally {
				if (this.readPosition == this.storePosition) {
					this.buffer = null;
					this.storePosition = 0;
					this.readPosition = 0;
				}
			}
		}
		return this.internRead();
	}
	
	@Override
	public int read(final byte[] buffer) throws IOException {
		return this.read( buffer, 0, buffer.length );
	}
	
	@Override
	public int read(final byte[] bytes, final int off, final int len) throws IOException {
		if (this.buffer == null && this.toSend == null) {
			return this.in.read( bytes, 0, len );
		}
		for (int i = 0; i < len; ++i) {
			final int next = this.read();
			if (next == -1) {
				return i;
			}
			bytes[i + off] = (byte) next;
		}
		return len;
	}
	
	@Override
	public void reset() throws IOException {
		if (this.buffer == null) {
			throw new IOException( "Not marked!" );
		}
		this.readPosition = 0;
	}
	
	@Override
	public String toString() {
		return " IMrkS[ " + this.in + ", blen=" + (this.buffer != null
				? this.buffer.length
				: 0) + ", r=" + this.readPosition + ", s=" + this.storePosition + ", snd=" + this.sendPosition + "] ";
	}
}
