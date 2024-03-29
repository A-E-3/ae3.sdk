/*
 * Created on 19.05.2005
 */
package ru.myx.ae3.binary;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import ru.myx.ae3.Engine;
import ru.myx.io.DataInputByteArrayFast;

final class BufferCopy implements TransferBuffer {
	
	private final byte[] buffer;

	private int position;

	private int length;

	BufferCopy(final byte[] buffer) {
		
		this.buffer = new byte[this.length = buffer.length];
		System.arraycopy(buffer, 0, this.buffer, 0, this.length);
		this.position = 0;
	}

	BufferCopy(final byte[] buffer, final int offset, final int length) {
		
		this.buffer = new byte[this.length = length];
		System.arraycopy(buffer, offset, this.buffer, 0, length);
		this.position = 0;
	}

	@Override
	public final void destroy() {
		
		// ignore
	}

	@Override
	public final MessageDigest getMessageDigest() {
		
		final MessageDigest digest = Engine.getMessageDigestInstance();
		digest.update(this.buffer, this.position, this.length);
		return digest;
	}

	@Override
	public final boolean hasRemaining() {
		
		return this.length - this.position > 0;
	}

	@Override
	public final boolean isDirectAbsolutely() {
		
		return this.position == 0;
	}

	@Override
	public final boolean isSequence() {
		
		return false;
	}

	@Override
	public final int next() {
		
		return this.buffer[this.position++] & 0xFF;
	}

	@Override
	public final int next(final byte[] buffer, final int offset, final int length) {
		
		final int amount = Math.min(this.length - this.position, length);
		if (amount > 0) {
			System.arraycopy(this.buffer, this.position, buffer, offset, amount);
			this.position += amount;
		}
		return amount;
	}

	@Override
	public final TransferBuffer nextSequenceBuffer() {
		
		throw new UnsupportedOperationException("Not a sequence!");
	}

	@Override
	public final long remaining() {
		
		return this.length - this.position;
	}

	@Override
	public final TransferCopier toBinary() {
		
		return new WrapCopier(this.toDirectArray());
	}

	@Override
	public final byte[] toDirectArray() {
		
		if (this.position == 0) {
			this.position = this.length;
			return this.buffer;
		}
		final int remaining = this.length - this.position;
		final byte[] result = new byte[remaining];
		System.arraycopy(this.buffer, this.position, result, 0, remaining);
		this.position = this.length;
		return result;
	}

	@Override
	public final DataInputByteArrayFast toInputStream() {
		
		return new DataInputByteArrayFast(this.buffer, this.position, this.length);
	}

	@Override
	public final TransferBuffer toNioBuffer(final ByteBuffer target) {
		
		final int remaining = this.length - this.position;
		if (remaining <= 0) {
			return null;
		}
		final int writable = target.remaining();
		if (writable <= 0) {
			return this;
		}
		if (writable >= remaining) {
			target.put(this.buffer, this.position, remaining);
			this.position = this.length;
			return null;
		}
		target.put(this.buffer, this.position, writable);
		this.position += writable;
		return this;
	}

	@Override
	public final InputStreamReader toReaderUtf8() {
		
		return new InputStreamReader(this.toInputStream(), StandardCharsets.UTF_8);
	}

	@Override
	public final String toString() {
		
		return this.toString(Charset.defaultCharset());
	}

	@Override
	public final String toString(final Charset charset) {
		
		try {
			return this.position == this.length
				? ""
				: new String(this.buffer, this.position, this.length - this.position, charset);
		} finally {
			this.position = this.length;
		}
	}

	@Override
	public final String toString(final String encoding) throws UnsupportedEncodingException {
		
		try {
			return this.position == this.length
				? ""
				: new String(this.buffer, this.position, this.length - this.position, encoding);
		} finally {
			this.position = this.length;
		}
	}

	@Override
	public final TransferBuffer toSubBuffer(final long start, final long end) {
		
		final int remaining = this.length - this.position;
		if (start < 0 || start > end || end > remaining) {
			throw new IllegalArgumentException("Indexes are out of bounds: start=" + start + ", end=" + end + ", length=" + remaining);
		}
		this.length = (int) (this.position + end);
		this.position += start;
		return this;
	}

	@Override
	public final MessageDigest updateMessageDigest(final MessageDigest digest) {
		
		digest.update(this.buffer, this.position, this.length);
		return digest;
	}
}
