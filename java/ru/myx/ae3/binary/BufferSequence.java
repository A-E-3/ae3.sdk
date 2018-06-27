/*
 * Created on 19.05.2005
 */
package ru.myx.ae3.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.SequenceInputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ru.myx.ae3.Engine;

final class BufferSequence implements TransferBuffer {
	private final TransferBuffer[]	buffers;
	
	private int						index		= 0;
	
	private int						remaining	= 0;
	
	BufferSequence(final TransferBuffer[] buffers) {
		this.buffers = buffers;
		for (int i = buffers.length - 1; i >= 0; --i) {
			this.remaining += this.buffers[i].remaining();
		}
	}
	
	@Override
	public final void destroy() {
		if (this.remaining == 0) {
			return;
		}
		for (;;) {
			if (this.index == this.buffers.length) {
				break;
			}
			this.buffers[this.index].destroy();
			this.buffers[this.index++] = null;
		}
		this.remaining = 0;
	}
	
	@Override
	public final MessageDigest getMessageDigest() {
		return this.updateMessageDigest( Engine.getMessageDigestInstance() );
	}
	
	@Override
	public final boolean hasRemaining() {
		return this.remaining > 0;
	}
	
	@Override
	public final boolean isDirectAbsolutely() {
		return false;
	}
	
	@Override
	public final boolean isSequence() {
		return true;
	}
	
	@Override
	public final int next() {
		if (this.remaining == 0) {
			throw new ArrayIndexOutOfBoundsException( "No more data!" );
		}
		this.remaining--;
		while (!this.buffers[this.index].hasRemaining()) {
			this.index++;
		}
		return this.buffers[this.index].next();
	}
	
	@Override
	public final int next(final byte[] buffer, final int offset, final int length) {
		final int amount = Math.min( this.remaining, length );
		if (amount > 0) {
			for (int left = amount, written = 0; left > 0;) {
				while (!this.buffers[this.index].hasRemaining()) {
					this.index++;
				}
				final int last = this.buffers[this.index].next( buffer, offset + written, left );
				left -= last;
				this.remaining -= last;
				written += last;
			}
		}
		return amount;
	}
	
	@Override
	public final TransferBuffer nextSequenceBuffer() {
		if (this.remaining == 0) {
			return null;
		}
		final TransferBuffer result = this.buffers[this.index];
		this.buffers[this.index] = null;
		this.remaining -= result.remaining();
		this.index++;
		return result;
	}
	
	@Override
	public final long remaining() {
		return this.remaining;
	}
	
	@Override
	public final TransferCopier toBinary() {
		if (this.remaining == 0) {
			return TransferCopier.NUL_COPIER;
		}
		final int index = this.index;
		final TransferCopier[] result = new TransferCopier[this.buffers.length - index];
		for (int i = index; i < this.buffers.length; ++i) {
			result[i - index] = this.buffers[i].toBinary();
		}
		return new CopierSequence( result );
	}
	
	@Override
	public final byte[] toDirectArray() {
		final long remaining = this.remaining();
		if (remaining > Integer.MAX_VALUE) {
			throw new RuntimeException( "Bigger than maximum byte array size, size=" + remaining + "!" );
		}
		final byte[] result = new byte[(int) remaining];
		this.next( result, 0, (int) remaining );
		return result;
	}
	
	@Override
	public final SequenceInputStream toInputStream() {
		if (this.remaining <= 0) {
			return null;
		}
		final List<InputStream> list = new ArrayList<>( this.buffers.length - this.index );
		for (;;) {
			final TransferBuffer next = this.nextSequenceBuffer();
			if (next == null) {
				break;
			}
			if (next.hasRemaining()) {
				list.add( next.toInputStream() );
			}
		}
		return new SequenceInputStream( Collections.enumeration( list ) );
	}
	
	@Override
	public final TransferBuffer toNioBuffer(final ByteBuffer target) throws IOException {
		if (this.remaining <= 0) {
			return null;
		}
		int writable = target.remaining();
		if (writable <= 0) {
			return this;
		}
		final TransferBuffer[] buffers = this.buffers;
		do {
			if (this.remaining <= 0 || this.index >= buffers.length) {
				return null;
			}
			final TransferBuffer buffer = buffers[this.index];
			final long before = buffer.remaining();
			if (before == 0) {
				buffers[this.index].destroy();
				buffers[this.index++] = null;
				continue;
			}
			final TransferBuffer replacement = buffer.toNioBuffer( target );
			if (replacement == null || !replacement.hasRemaining()) {
				buffers[this.index].destroy();
				buffers[this.index++] = null;
				this.remaining -= before;
				continue;
			}
			{
				final long after = replacement.remaining();
				final long written = before - after;
				this.remaining -= written;
				writable = target.remaining();
				if (replacement != buffer) {
					buffers[this.index] = replacement;
				}
			}
		} while (writable > 0);
		if (this.index == buffers.length - 1) {
			try {
				return buffers[this.index];
			} finally {
				buffers[this.index] = null;
			}
		}
		return this;
	}
	
	@Override
	public final InputStreamReader toReaderUtf8() {
		return new InputStreamReader( this.toInputStream(), Engine.CHARSET_UTF8 );
	}
	
	@Override
	public final String toString() {
		return this.toString( Engine.CHARSET_DEFAULT );
	}
	
	@Override
	public final String toString(final Charset charset) {
		final byte[] bytes = this.toDirectArray();
		return bytes == null
				? null
				: bytes.length == 0
						? ""
						: new String( bytes, charset );
	}
	
	@Override
	public final String toString(final String encoding) throws UnsupportedEncodingException {
		final byte[] bytes = this.toDirectArray();
		return bytes == null
				? null
				: bytes.length == 0
						? ""
						: new String( bytes, encoding );
	}
	
	@Override
	public final TransferBuffer toSubBuffer(final long start, final long end) {
		final int remaining = this.remaining;
		if (start < 0 || start > end || end > remaining) {
			throw new IllegalArgumentException( "Indexes are out of bounds: start="
					+ start
					+ ", end="
					+ end
					+ ", length="
					+ remaining );
		}
		if (start > 0) {
			int skipped = 0;
			while (start > skipped) {
				if (this.index == this.buffers.length) {
					throw new IllegalArgumentException( "Nothing to skip!" );
				}
				final long currentRemaining = this.buffers[this.index].remaining();
				if (currentRemaining == 0) {
					this.buffers[this.index].destroy();
					this.buffers[this.index++] = null;
					continue;
				}
				final long skip = start - skipped;
				if (currentRemaining <= skip) {
					skipped += currentRemaining;
					this.buffers[this.index].destroy();
					this.buffers[this.index++] = null;
					continue;
				}
				this.buffers[this.index] = this.buffers[this.index].toSubBuffer( skip, currentRemaining );
				skipped += skip;
			}
			this.remaining -= skipped;
		}
		if (end < remaining) {
			final long skip = remaining - end;
			int targetIndex = this.buffers.length - 1;
			while (skip > 0) {
				if (this.index > targetIndex) {
					throw new IllegalArgumentException( "Nothing to skip!" );
				}
				final long currentRemaining = this.buffers[targetIndex].remaining();
				if (currentRemaining == 0) {
					this.buffers[targetIndex].destroy();
					this.buffers[targetIndex--] = null;
					continue;
				}
				if (skip > currentRemaining) {
					this.remaining -= currentRemaining;
					this.buffers[targetIndex].destroy();
					this.buffers[targetIndex--] = null;
					continue;
				}
				this.buffers[targetIndex] = this.buffers[targetIndex].toSubBuffer( 0, currentRemaining - skip );
				this.remaining -= skip;
			}
			final int shift = this.buffers.length - targetIndex - 1;
			if (shift > 0) {
				System.arraycopy( this.buffers, this.index, this.buffers, this.index + shift, targetIndex - this.index );
				for (int i = shift; i > 0; --i) {
					this.buffers[this.index++] = null;
				}
			}
		}
		return this;
	}
	
	@Override
	public final MessageDigest updateMessageDigest(final MessageDigest digest) {
		if (this.remaining == 0) {
			return digest;
		}
		int index = this.index;
		for (;;) {
			if (index == this.buffers.length) {
				break;
			}
			this.buffers[index++].updateMessageDigest( digest );
		}
		return digest;
	}
}
