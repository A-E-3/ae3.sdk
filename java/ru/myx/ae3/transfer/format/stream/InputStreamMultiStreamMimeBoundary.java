/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.transfer.format.stream;

import java.io.IOException;
import java.io.InputStream;

/** @author myx */
public final class InputStreamMultiStreamMimeBoundary extends InputStreamMultiStream {

	private static final int ST_INIT = 0;

	private static final int ST_NEXT = 1;

	private static final int ST_READ = 2;

	private static final int ST_DONE = 3;

	private final InputStream is;

	private final byte[] boundary;

	private final byte[] arr;

	private int state = InputStreamMultiStreamMimeBoundary.ST_INIT;

	/** @param is
	 * @param boundary */
	public InputStreamMultiStreamMimeBoundary(final InputStream is, final String boundary) {

		this.is = is.markSupported()
			? is
			: new InputStreamMarkSupported(is);
		this.boundary = ("\r\n--" + boundary).getBytes();
		this.arr = new byte[this.boundary.length - 2];
	}

	@Override
	public void close() {

		// empty
	}

	@Override
	public boolean hasMoreStreams() throws IOException {

		switch (this.state) {
			case ST_DONE :
				return false;
			case ST_NEXT :
				return true;
			case ST_READ :
				if (this.in != null) {
					this.in.close();
					this.in = null;
				}
				break;
			case ST_INIT :
				this.is.mark(this.arr.length);
				for (int len = 0;;) {
					final int read = this.is.read(this.arr, len, this.arr.length - len);
					if (read <= 0) {
						this.is.reset();
						this.state = InputStreamMultiStreamMimeBoundary.ST_DONE;
						return false;
					}
					len += read;
					if (len == this.arr.length) {
						for (int i = 0; i < this.arr.length; ++i) {
							if (this.boundary[i + 2] != this.arr[i]) {
								this.is.reset();
								this.state = InputStreamMultiStreamMimeBoundary.ST_DONE;
								return false;
							}
						}
						break;
					}
				}
				break;
			default :
		}
		final int len = this.is.read();
		if (len == -1) {
			this.state = InputStreamMultiStreamMimeBoundary.ST_DONE;
			return false;
		}
		if (len == '\r') {
			if (this.is.read() == '\n') {
				this.state = InputStreamMultiStreamMimeBoundary.ST_NEXT;
				return true;
			}
		} else //
		if (len == '-') {
			if (this.is.read() == '-') {
				this.state = InputStreamMultiStreamMimeBoundary.ST_DONE;
				return false;
			}
		}
		throw new IOException("unexpected character: code=" + len);
	}

	@Override
	public void nextStream() throws IOException {

		if (this.hasMoreStreams()) {
			this.state = InputStreamMultiStreamMimeBoundary.ST_READ;
			this.in = new InputStreamEatBoundary(this.is, this.boundary);
		} else {
			this.state = InputStreamMultiStreamMimeBoundary.ST_DONE;
			this.in = null;
		}
	}

	@Override
	public String toString() {

		return "ISMSMB[ in=" + this.in + " ]";
	}
}
