/*
 * Created on 19.05.2005
 */
package ru.myx.ae3.binary;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import ru.myx.ae3.Engine;
import ru.myx.io.DataInputByteArrayFast;

final class CollBufBinary extends CollBuf {
	private static final byte[]	EMPTY_ARRAY	= TransferCopier.NUL_COPIER.nextDirectArray();
	
	private Collector			collector;
	
	private byte[]				buffer;
	
	private int					bufferCapacity;
	
	private int					bufferTail;
	
	private int					bufferHead;
	
	private int					bufferSize;
	
	CollBufBinary(final Collector collector) {
		this.collector = collector;
		final int capacity = Transfer.BUFFER_MEDIUM;
		this.buffer = new byte[capacity];
		this.bufferCapacity = capacity;
	}
	
	CollBufBinary(final Collector collector, final byte[] data) {
		this.collector = collector;
		final int length = data.length;
		final int capacity = length > Transfer.BUFFER_MEDIUM
				? length
				: Transfer.BUFFER_MEDIUM;
		this.buffer = new byte[capacity];
		this.bufferCapacity = capacity;
		System.arraycopy( data, 0, this.buffer, 0, this.bufferSize = this.bufferHead = length );
	}
	
	CollBufBinary(final Collector collector, final byte[] data, final int off, final int length) {
		this.collector = collector;
		final int capacity = length > Transfer.BUFFER_MEDIUM
				? length
				: Transfer.BUFFER_MEDIUM;
		this.buffer = new byte[capacity];
		this.bufferCapacity = capacity;
		System.arraycopy( data, off, this.buffer, 0, this.bufferSize = this.bufferHead = length );
	}
	
	CollBufBinary(final Collector collector, final ByteBuffer data) {
		this.collector = collector;
		final int length = data.remaining();
		final int capacity = length > Transfer.BUFFER_MEDIUM
				? length
				: Transfer.BUFFER_MEDIUM;
		this.buffer = new byte[capacity];
		this.bufferCapacity = capacity;
		data.get( this.buffer, 0, length );
		this.bufferSize = this.bufferHead = length;
	}
	
	@Override
	public void close() {
		// empty
	}
	
	private String describe() {
		return this.getClass()
				+ ": bufferCapacity="
				+ this.bufferCapacity
				+ ", bufferHead="
				+ this.bufferHead
				+ ", bufferTail="
				+ this.bufferTail
				+ ", bufferSize="
				+ this.bufferSize;
	}
	
	@Override
	public final void destroy() {
		this.collector = null;
		this.buffer = null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.collector = null;
		this.buffer = null;
		super.finalize();
	}
	
	@Override
	public MessageDigest getMessageDigest() {
		return this.updateMessageDigest( Engine.getMessageDigestInstance() );
	}
	
	// /////////////////////////////////////////////////////////////////////
	@Override
	public final boolean hasRemaining() {
		return this.bufferSize > 0;
	}
	
	@Override
	public final boolean isDirectAbsolutely() {
		return this.bufferSize == this.bufferCapacity && this.bufferTail % this.bufferCapacity == 0;
	}
	
	@Override
	public boolean isSequence() {
		return false;
	}
	
	@Override
	public int next() {
		if (this.bufferSize == 0) {
			return -1;
		}
		--this.bufferSize;
		return this.buffer[this.bufferTail++ % this.bufferCapacity] & 0xFF;
	}
	
	@Override
	public final int next(final byte[] buffer, final int offset, final int length) {
		final int amount = Math.min( this.bufferSize, length );
		if (amount > 0) {
			final int head = this.bufferHead % this.bufferCapacity;
			final int tail = this.bufferTail % this.bufferCapacity;
			if (tail < head || head == 0) {
				System.arraycopy( this.buffer, tail, buffer, 0, amount );
				this.bufferTail += amount;
			} else {
				final int firstLength = this.bufferCapacity - tail;
				if (firstLength >= amount) {
					System.arraycopy( this.buffer, tail, buffer, 0, amount );
					this.bufferTail += amount;
				} else {
					System.arraycopy( this.buffer, tail, buffer, 0, firstLength );
					this.bufferTail += firstLength;
					final int left = amount - firstLength;
					System.arraycopy( this.buffer, 0, buffer, firstLength, left );
					this.bufferTail += left;
				}
			}
			this.bufferSize -= amount;
			if (this.bufferSize <= 0) {
				this.collector = null;
				this.buffer = null;
				this.bufferHead = 0;
				this.bufferTail = 0;
				this.bufferCapacity = 0;
			}
		}
		return amount;
	}
	
	@Override
	public final TransferBuffer nextSequenceBuffer() {
		throw new UnsupportedOperationException( "Not a sequence!" );
	}
	
	@Override
	public final void queued() {
		// empty
	}
	
	@Override
	public final long remaining() {
		return this.bufferSize;
	}
	
	@Override
	public final void reset() {
		this.bufferHead = this.bufferTail = this.bufferSize = 0;
		/**
		 * reduce the buffer if it is stretched quite a bit
		 */
		if (this.bufferCapacity > Transfer.BUFFER_LARGE) {
			// behavior.returnBuffer(buffer);
			final int capacity = Transfer.BUFFER_MEDIUM;
			this.buffer = new byte[capacity];
			this.bufferCapacity = capacity;
		}
	}
	
	@Override
	public TransferCopier toBinary() {
		return this.bufferSize == 0
				? TransferCopier.NUL_COPIER
				: new WrapCopier( this.toDirectArray() );
	}
	
	@Override
	public final byte[] toDirectArray() {
		final byte[] result;
		if (this.bufferSize == 0) {
			result = CollBufBinary.EMPTY_ARRAY;
		} else {
			final int head = this.bufferHead % this.bufferCapacity;
			final int tail = this.bufferTail % this.bufferCapacity;
			final int size = this.bufferSize;
			this.bufferSize = 0;
			if (size == this.bufferCapacity && tail == 0) {
				this.bufferHead = 0;
				result = this.buffer;
				this.buffer = null;
				return result;
			}
			result = new byte[size];
			if (tail < head || head == 0) {
				System.arraycopy( this.buffer, tail, result, 0, size );
			} else {
				final int firstLength = this.bufferCapacity - tail;
				System.arraycopy( this.buffer, tail, result, 0, firstLength );
				System.arraycopy( this.buffer, 0, result, firstLength, head );
			}
			this.bufferTail = this.bufferHead;
		}
		if (this != this.collector.binary) {
			// behavior.returnBuffer(buffer);
			this.buffer = null;
		}
		return result;
	}
	
	@Override
	public DataInputByteArrayFast toInputStream() {
		return new DataInputByteArrayFast( this.toDirectArray() );
	}
	
	@Override
	public final TransferBuffer toNioBuffer(final ByteBuffer target) {
		final int bufferSize = this.bufferSize;
		if (bufferSize <= 0) {
			this.collector = null;
			this.buffer = null;
			return null;
		}
		final int writable = target.remaining();
		if (writable <= 0) {
			return this;
		}
		final int block = Math.min( writable, bufferSize );
		for (int i = block; i > 0; --i) {
			target.put( this.buffer[this.bufferTail++ % this.bufferCapacity] );
		}
		this.bufferSize -= block;
		if (this.bufferSize <= 0) {
			this.collector = null;
			this.buffer = null;
			return null;
		}
		return this;
	}
	
	@Override
	public final InputStreamReader toReaderUtf8() {
		return new InputStreamReader( this.toInputStream(), Engine.CHARSET_UTF8 );
	}
	
	@Override
	public final TransferBuffer toSubBuffer(final long start, final long end) {
		final int remaining = this.bufferSize;
		if (start < 0 || start > end || end > remaining) {
			throw new IllegalArgumentException( "Indexes are out of bounds: start="
					+ start
					+ ", end="
					+ end
					+ ", length="
					+ remaining );
		}
		this.bufferSize = (int) (end - start);
		this.bufferTail += start;
		return this;
	}
	
	@Override
	public MessageDigest updateMessageDigest(final MessageDigest digest) {
		if (this.bufferSize == 0) {
			return digest;
		}
		final int head = this.bufferHead % this.bufferCapacity;
		final int tail = this.bufferTail % this.bufferCapacity;
		final int size = this.bufferSize;
		if (size == this.bufferCapacity && tail == 0) {
			digest.update( this.buffer );
			return digest;
		}
		if (tail < head || head == 0) {
			digest.update( this.buffer, tail, size );
		} else {
			final int firstLength = this.bufferCapacity - tail;
			digest.update( this.buffer, tail, firstLength );
			digest.update( this.buffer, 0, head );
		}
		return digest;
	}
	
	@Override
	public final void write(final byte[] buff, final int off, final int len) throws IOException {
		final int targetSize = this.bufferSize + len;
		final int head = this.bufferHead % this.bufferCapacity;
		/**
		 * fits in current buffer
		 */
		if (targetSize <= this.bufferCapacity) {
			final int firstLimit = this.bufferCapacity - head;
			if (firstLimit >= len) {
				System.arraycopy( buff, off, this.buffer, head, len );
			} else {
				System.arraycopy( buff, off, this.buffer, head, firstLimit );
				System.arraycopy( buff, off + firstLimit, this.buffer, 0, len - firstLimit );
			}
			this.bufferHead = head + len;
			this.bufferSize = targetSize;
			return;
		}
		/**
		 * gonna be used in any case
		 */
		final int tail = this.bufferTail % this.bufferCapacity;
		/**
		 * doesn't fit and bigger that biggest buffer allowed
		 */
		if (targetSize > Transfer.BUFFER_MAX) {
			final Collector collector = this.collector;
			final CollBufTemp result;
			if (tail < head || head == 0) {
				result = new CollBufTemp( collector, this.buffer, tail, this.bufferSize );
			} else {
				final int firstLength = this.bufferCapacity - tail;
				result = new CollBufTemp( collector, this.buffer, tail, firstLength );
				result.write( this.buffer, 0, head );
			}
			// behavior.returnBuffer(buffer);
			result.write( buff, off, len );
			collector.sequence.replaceLast( collector.binary = result, this );
			return;
		}
		/**
		 * doesn't fit and we're going to expand our buffer
		 */
		{
			final int tgExpansion1 = this.bufferCapacity += len;
			final int tgExpansion2 = this.bufferCapacity << 1;
			final int tgExpansion = tgExpansion1 < tgExpansion2
					? tgExpansion2
					: tgExpansion1;
			final int targetCapacity = tgExpansion > Transfer.BUFFER_MAX
					? Transfer.BUFFER_MAX
					: tgExpansion;
			final byte[] newBuffer = new byte[targetCapacity];
			try {
				if (tail < head || head == 0) {
					System.arraycopy( this.buffer, tail, newBuffer, 0, this.bufferSize );
				} else {
					final int firstLength = this.bufferCapacity - tail;
					System.arraycopy( this.buffer, tail, newBuffer, 0, firstLength );
					System.arraycopy( this.buffer, 0, newBuffer, firstLength, head );
				}
			} catch (final Exception e) {
				throw new RuntimeException( this.describe() + ": exception", e );
			}
			// behavior.returnBuffer(buffer);
			this.buffer = newBuffer;
			System.arraycopy( buff, off, this.buffer, this.bufferSize, len );
			this.bufferSize = targetSize;
			this.bufferTail = 0;
			this.bufferHead = targetSize;
			this.bufferCapacity = targetCapacity;
		}
	}
	
	@Override
	public final void write(final ByteBuffer buff) throws IOException {
		final int len = buff.remaining();
		final int targetSize = this.bufferSize + len;
		final int head = this.bufferHead % this.bufferCapacity;
		/**
		 * fits in current buffer
		 */
		if (targetSize <= this.bufferCapacity) {
			final int firstLength = this.bufferCapacity - head;
			if (firstLength >= len) {
				buff.get( this.buffer, head, len );
			} else {
				buff.get( this.buffer, head, firstLength );
				buff.get( this.buffer, 0, len - firstLength );
			}
			this.bufferHead = head + len;
			this.bufferSize = targetSize;
			return;
		}
		/**
		 * will be used in any case
		 */
		final int tail = this.bufferTail % this.bufferCapacity;
		/**
		 * doesn't fit and bigger that biggest buffer allowed
		 */
		if (targetSize > Transfer.BUFFER_MAX) {
			final Collector collector = this.collector;
			final CollBufTemp result;
			if (tail < head || head == 0) {
				result = new CollBufTemp( collector, this.buffer, tail, this.bufferSize );
			} else {
				final int firstLength = this.bufferCapacity - tail;
				result = new CollBufTemp( collector, this.buffer, tail, firstLength );
				result.write( this.buffer, 0, head );
			}
			// behavior.returnBuffer(buffer);
			result.write( buff );
			collector.sequence.replaceLast( collector.binary = result, this );
			return;
		}
		/**
		 * doesn't fit and we're going to expand our buffer
		 */
		{
			final int tgExpansion1 = this.bufferCapacity += len;
			final int tgExpansion2 = this.bufferCapacity << 1;
			final int tgExpansion = tgExpansion1 < tgExpansion2
					? tgExpansion2
					: tgExpansion1;
			final int targetCapacity = tgExpansion > Transfer.BUFFER_MAX
					? Transfer.BUFFER_MAX
					: tgExpansion;
			final byte[] newBuffer = new byte[targetCapacity];
			try {
				if (tail < head || head == 0) {
					System.arraycopy( this.buffer, tail, newBuffer, 0, this.bufferSize );
				} else {
					final int firstLength = this.bufferCapacity - tail;
					System.arraycopy( this.buffer, tail, newBuffer, 0, firstLength );
					System.arraycopy( this.buffer, 0, newBuffer, firstLength, head );
				}
			} catch (final Exception e) {
				throw new RuntimeException( this.describe() + ": exception", e );
			}
			// behavior.returnBuffer(buffer);
			this.buffer = newBuffer;
			buff.get( this.buffer, this.bufferSize, len );
			this.bufferSize = targetSize;
			this.bufferTail = 0;
			this.bufferHead = targetSize;
			this.bufferCapacity = targetCapacity;
		}
	}
	
	// /////////////////////////////////////////////////////////////////////
	@Override
	public final void write(final int i) throws IOException {
		final int head = this.bufferHead % this.bufferCapacity;
		/**
		 * fits in current buffer
		 */
		if (this.bufferSize < this.bufferCapacity) {
			this.buffer[this.bufferHead++ % this.bufferCapacity] = (byte) i;
			this.bufferSize++;
			return;
		}
		/**
		 * will be used in any case
		 */
		final int tail = this.bufferTail % this.bufferCapacity;
		/**
		 * doesn't fit and bigger that biggest buffer allowed
		 */
		if (this.bufferCapacity >= Transfer.BUFFER_MAX) {
			final Collector collector = this.collector;
			final CollBufTemp result;
			if (tail < head || head == 0) {
				result = new CollBufTemp( collector, this.buffer, tail, this.bufferSize );
			} else {
				final int firstLength = this.bufferCapacity - tail;
				result = new CollBufTemp( collector, this.buffer, tail, firstLength );
				result.write( this.buffer, 0, head );
			}
			// behavior.returnBuffer(buffer);
			result.write( i );
			collector.sequence.replaceLast( collector.binary = result, this );
			return;
		}
		/**
		 * doesn't fit and we're going to expand our buffer
		 */
		{
			final int tgExpansion = this.bufferCapacity << 1;
			final int targetCapacity = tgExpansion > Transfer.BUFFER_MAX
					? Transfer.BUFFER_MAX
					: tgExpansion;
			final byte[] newBuffer = new byte[targetCapacity];
			try {
				if (tail < head || head == 0) {
					System.arraycopy( this.buffer, tail, newBuffer, 0, this.bufferSize );
				} else {
					final int firstLength = this.bufferCapacity - tail;
					System.arraycopy( this.buffer, tail, newBuffer, 0, firstLength );
					System.arraycopy( this.buffer, 0, newBuffer, firstLength, head );
				}
			} catch (final Exception e) {
				throw new RuntimeException( this.describe() + ": exception", e );
			}
			// behavior.returnBuffer(buffer);
			this.buffer = newBuffer;
			this.bufferTail = 0;
			this.buffer[this.bufferSize++] = (byte) i;
			this.bufferHead = this.bufferSize;
			this.bufferCapacity = targetCapacity;
		}
	}
}
