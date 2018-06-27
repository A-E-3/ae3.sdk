/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.transfer.format.stream;

import java.io.IOException;
import java.io.InputStream;

final class InputStreamEatBoundary extends InputStream {
	private final InputStream	in;
	
	private final byte[]		boundary;
	
	private int					position	= 0;
	
	private int					search;
	
	InputStreamEatBoundary(final InputStream in, final byte[] boundary) {
		this.in = in.markSupported()
				? in
				: new InputStreamMarkSupported( in );
		this.boundary = boundary;
		this.search = this.boundary[0];
	}
	
	@Override
	public void close() throws IOException {
		for (;;) {
			if (this.read() == -1) {
				break;
			}
		}
	}
	
	@Override
	public int read() throws IOException {
		if (this.search == -1) {
			return -1;
		}
		for (;;) {
			final int next = this.in.read();
			if (next == this.search) {
				if (this.position == 0) {
					this.in.mark( this.boundary.length );
				}
				if (this.position < this.boundary.length - 1) {
					this.search = this.boundary[++this.position] & 0xFF;
				} else {
					return this.search = -1;
				}
			} else //
			if (this.position > 0) {
				this.in.reset();
				this.position = 0;
				this.search = this.boundary[0] & 0xFF;
				return this.search;
			} else {
				if (next == -1) {
					this.search = -1;
				}
				return next;
			}
		}
	}
	
	@Override
	public int read(final byte[] buffer) throws IOException {
		return this.read( buffer, 0, buffer.length );
	}
	
	@Override
	public int read(final byte[] buffer, final int off, final int len) throws IOException {
		for (int i = 0; i < len; ++i) {
			if (this.search == -1) {
				return -1;
			}
			final int next = this.read();
			if (next == -1) {
				return i;
			}
			buffer[i + off] = (byte) next;
		}
		return len;
	}
	
	@Override
	public long skip(final long skip) throws IOException {
		long skipped = 0;
		while (skipped < skip) {
			final int next = this.read();
			if (next == -1) {
				return skipped;
			}
			skipped++;
		}
		return skipped;
	}
	
	@Override
	public String toString() {
		return "IEB[ "
				+ this.in
				+ ", len="
				+ this.boundary.length
				+ ", p="
				+ this.position
				+ ", s="
				+ this.search
				+ " ]";
	}
}
