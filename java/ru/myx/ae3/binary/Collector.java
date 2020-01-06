/**
 * Created on 17.11.2002
 *
 * myx - barachta */
package ru.myx.ae3.binary;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.util.BasicQueue;
import ru.myx.util.FifoQueueLinked;

/** @author myx
 *
 *         myx - barachta "typecomment": Window>Preferences>Java>Templates. To enable and disable
 *         the creation of type comments go to Window>Preferences>Java>Code Generation. */
final class Collector extends OutputStream implements TransferCollector, TransferTarget {

	private static final byte[] CHUNK_STOP_SEQUENCE = "0\r\n\r\n".getBytes();
	
	private static final int CHUNK_STOP_SEQUENCE_LENGTH = Collector.CHUNK_STOP_SEQUENCE.length;
	
	private static final byte[] CRLF = "\r\n".getBytes();
	
	private static final int CRLF_LENGTH = Collector.CRLF.length;
	
	CollBuf binary;
	
	private int chunkCollected = 0;
	
	private Collector chunkCollector = null;
	
	private boolean chunking = false;
	
	private int chunkMaxLimit = 0;
	
	private int chunkMinLimit = 0;
	
	private boolean closed;
	
	FifoQueueLinked<TransferBuffer> sequence;
	
	private TransferTarget connected;

	Collector() {

		// empty
	}

	@Override
	public final void abort(final String reason) {

		final FifoQueueLinked<TransferBuffer> sequence = this.sequence;
		if (sequence != null) {
			this.sequence = null;
			while (sequence.hasNext()) {
				sequence.pollFirst().destroy();
			}
		}
		this.binary = null;
		this.chunking = false;
		this.chunkMinLimit = 0;
		this.chunkMaxLimit = 0;
		this.chunkCollected = 0;
		this.chunkCollector = null;
		this.closed = false;
		final TransferTarget connected = this.connected;
		if (connected != null) {
			this.connected = null;
			connected.abort(reason);
		}
	}

	@Override
	public final boolean absorb(final int i) {

		if (this.closed) {
			throw new IllegalStateException("Collector is closed!");
		}
		synchronized (this) {
			if (this.chunking) {
				if (this.chunkCollected == this.chunkMaxLimit) {
					this.flushChunk();
				}
				this.chunkCollected++;
				this.chunkCollector.absorb(i);
				return true;
			}
			if (this.connected != null) {
				return this.connected.absorb(i);
			}
			if (this.binary == null) {
				final CollBuf binary = new CollBufBinary(this);
				/** FIXME really? maybe this.binary? */
				this.addSequence(binary, binary);
			}
			this.binary.absorb(i);
		}
		return true;
	}

	@Override
	public final boolean absorbArray(final byte[] bytes, final int offset, final int length) {

		if (this.closed) {
			throw new IllegalStateException("Collector is closed!");
		}
		if (length == 0) {
			return true;
		}
		synchronized (this) {
			if (this.chunking) {
				if (this.chunkCollected > 0 && this.chunkCollected + length > this.chunkMaxLimit) {
					if (this.chunkCollected < this.chunkMinLimit) {
						final int limit = this.chunkMaxLimit - this.chunkCollected;
						this.chunkCollected += limit;
						this.chunkCollector.absorbArray(bytes, offset, limit);
						this.flushChunk();
						this.absorbArray(bytes, offset + limit, length - limit);
						return true;
					}
					this.flushChunk();
				}
				this.chunkCollected += length;
				this.chunkCollector.absorbArray(bytes, offset, length);
				return true;
			}
			if (this.connected != null) {
				return this.connected.absorbArray(bytes, offset, length);
			}
			if (this.binary == null) {
				try {
					final CollBuf binary = length > Transfer.BUFFER_MAX
						? (CollBuf) new CollBufTemp(this, bytes, offset, length)
						: new CollBufBinary(this, bytes, offset, length);
					this.addSequence(binary, binary);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				this.binary.absorbArray(bytes, offset, length);
			}
		}
		return true;
	}

	@Override
	public final boolean absorbBuffer(final TransferBuffer buffer) {

		if (this.closed) {
			throw new IllegalStateException("Collector is closed!");
		}
		synchronized (this) {
			if (this.chunking) {
				final long remaining = buffer.remaining();
				if (this.chunkCollected > 0 && this.chunkCollected + remaining > this.chunkMaxLimit) {
					if (this.chunkCollected < this.chunkMinLimit && buffer.isSequence()) {
						do {
							final TransferBuffer next = buffer.nextSequenceBuffer();
							if (!this.absorbBuffer(next)) {
								return false;
							}
						} while (buffer.hasRemaining());
						return true;
					}
					if (this.chunkCollected < this.chunkMinLimit && buffer.isDirectAbsolutely()) {
						final byte[] bytes = buffer.toDirectArray();
						return this.absorbArray(bytes, 0, bytes.length);
					}
					this.flushChunk();
				}
				this.chunkCollected += remaining;
				this.chunkCollector.absorbBuffer(buffer);
				return true;
			}
			if (this.connected != null) {
				if (!this.connected.absorbBuffer(buffer)) {
					if (buffer.hasRemaining()) {
						this.addSequence(buffer, null);
					}
					this.connected = null;
					return false;
				}
				return true;
			}
			this.addSequence(buffer, null);
		}
		return true;
	}

	@Override
	public final boolean absorbNio(final ByteBuffer buffer) {

		if (this.closed) {
			throw new IllegalStateException("Collector is closed!");
		}
		final int length = buffer.remaining();
		synchronized (this) {
			if (this.chunking) {
				if (this.chunkCollected > 0 && this.chunkCollected + length > this.chunkMaxLimit) {
					if (this.chunkCollected < this.chunkMinLimit) {
						final int limit = this.chunkMaxLimit - this.chunkCollected;
						this.chunkCollected += limit;
						buffer.limit(buffer.position() + limit);
						this.chunkCollector.absorbNio(buffer);
						this.flushChunk();
						buffer.limit(buffer.position() + length - limit);
						this.absorbNio(buffer);
						return true;
					}
					this.flushChunk();
				}
				this.chunkCollected += length;
				this.chunkCollector.absorbNio(buffer);
				return true;
			}
			if (this.connected != null) {
				if (!this.connected.absorbNio(buffer)) {
					if (buffer.hasRemaining()) {
						try {
							final CollBuf binary = length > Transfer.BUFFER_MAX
								? (CollBuf) new CollBufTemp(this, buffer)
								: new CollBufBinary(this, buffer);
							this.addSequence(binary, binary);
						} catch (final IOException e) {
							throw new RuntimeException(e);
						}
					}
					this.connected = null;
					return false;
				}
				return true;
			}
			if (this.binary == null) {
				try {
					final CollBuf binary = length > Transfer.BUFFER_MAX
						? (CollBuf) new CollBufTemp(this, buffer)
						: new CollBufBinary(this, buffer);
					this.addSequence(binary, binary);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			} else {
				this.binary.absorbNio(buffer);
			}
		}
		return true;
	}

	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// Output stream
	private final void addSequence(final TransferBuffer buffer, final CollBuf binary) {

		if (this.sequence == null) {
			this.sequence = new FifoQueueLinked<>();
		}
		this.sequence.offerLast(buffer);
		this.binary = binary;
	}

	@Override
	public final void close() {

		if (this.closed) {
			return;
		}
		if (this.chunking) {
			this.stopChunking();
		}
		final TransferTarget connected = this.connected;
		if (connected != null) {
			this.connected = null;
			connected.close();
		}
		if (this.binary != null) {
			this.binary.close();
		}
		this.closed = true;
	}

	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// Target
	
	@Override
	public boolean connectTarget(final TransferTarget target) throws IllegalStateException {

		if (this.closed) {
			/** transfer all that we collected at once */
			final TransferBuffer buffer = this.toBuffer();
			if (!target.absorbBuffer(buffer)) {
				if (buffer.hasRemaining()) {
					this.addSequence(buffer, null);
				}
				return false;
			}
			target.close();
			return true;
		}
		synchronized (this) {
			final FifoQueueLinked<TransferBuffer> sequence = this.sequence;
			if (sequence != null && sequence.hasNext()) {
				do {
					final TransferBuffer current = sequence.pollFirst();
					if (current == this.binary) {
						this.binary.queued();
						this.binary = null;
					}
					if (!target.absorbBuffer(current)) {
						if (current.hasRemaining()) {
							sequence.offerFirst(current);
						}
						return false;
					}
				} while (sequence.hasNext());
			}
			this.connected = target;
		}
		return false;
	}

	@Override
	public final <A, R> boolean enqueueAction(final ExecProcess process, final Function<A, R> function, final A argument) {

		Act.launch(process, function, argument);
		return true;
	}

	private final void flushChunk() {

		this.chunkCollector.close();
		final TransferBuffer buffer = this.chunkCollector.toBuffer();
		this.chunkCollector.reset();
		this.chunking = false;
		final long remaining = buffer.remaining();
		if (remaining > Integer.MAX_VALUE) {
			throw new RuntimeException("Bigger than maximum byte array size, size=" + remaining + "!");
		}
		final byte[] bytes = Integer.toHexString((int) remaining).getBytes();
		this.absorbArray(bytes, 0, bytes.length);
		this.absorbArray(Collector.CRLF, 0, Collector.CRLF_LENGTH);
		this.absorbBuffer(buffer);
		this.absorbArray(Collector.CRLF, 0, Collector.CRLF_LENGTH);
		this.chunkCollected = 0;
		this.chunking = true;
	}

	@Override
	public final void force() {

		// empty
	}

	@Override
	public final OutputStream getOutputStream() {

		return this;
	}

	@Override
	public final TransferTarget getTarget() {

		return this;
	}

	private final int length() {

		if (this.sequence == null) {
			return 0;
		}
		
		class QueueLengthCallback implements BasicQueue.IterationAllCallback<TransferBuffer> {
			
			int length;

			@Override
			public boolean onNextItem(final TransferBuffer item) {

				this.length += item.remaining();
				return true;
			}
		}
		
		final QueueLengthCallback callback = new QueueLengthCallback();
		this.sequence.iterateAll(callback);
		return callback.length;
	}

	@Override
	public void printBinary(final TransferCopier binary) {
		
		this.absorbBuffer(binary.nextCopy());
	}

	@Override
	public void printByte(final int i) {
		
		this.absorb(i);
	}

	@Override
	public void printBytes(final byte[] bytes) {

		this.absorbArray(bytes, 0, bytes.length);
	}

	/** TODO optimizations are very much possible: CharsetEncoder and more */
	@Override
	public void printUtf8(final String string) {

		final byte[] bytes = string.getBytes(Engine.CHARSET_UTF8);
		this.absorbArray(bytes, 0, bytes.length);
	}

	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////
	// Common
	@Override
	public final void reset() {

		final FifoQueueLinked<TransferBuffer> sequence = this.sequence;
		if (sequence != null) {
			this.sequence = null;
			for (TransferBuffer buffer; (buffer = sequence.pollFirst()) != null;) {
				buffer.destroy();
			}
		}
		if (this.chunking) {
			this.stopChunking();
		}
		if (this.binary != null) {
			this.binary.reset();
			if (this.sequence == null) {
				this.binary = null;
			} else {
				this.sequence.offerLast(this.binary);
			}
		}
		this.closed = false;
		this.connected = null;
	}

	@Override
	public final void startChunking(final int minChunk, final int maxChunk) throws IllegalStateException, IllegalArgumentException {

		if (this.chunking) {
			throw new IllegalStateException("Already in chunking state!");
		}
		this.chunking = true;
		this.chunkMinLimit = minChunk;
		this.chunkMaxLimit = maxChunk;
		this.chunkCollected = 0;
		if (this.chunkCollector == null) {
			this.chunkCollector = new Collector();
		}
	}

	@Override
	public final void stopChunking() throws IllegalStateException {

		if (!this.chunking) {
			throw new IllegalStateException("Not in chunking state!");
		}
		if (this.chunkCollected > 0) {
			this.flushChunk();
		}
		this.chunking = false;
		this.absorbArray(Collector.CHUNK_STOP_SEQUENCE, 0, Collector.CHUNK_STOP_SEQUENCE_LENGTH);
	}

	@Override
	public final TransferCopier toBinary() {

		if (!this.closed) {
			this.close();
		}
		return this.toCloneFactory();
	}

	@Override
	public final TransferBuffer toBuffer() {

		if (!this.closed) {
			throw new IllegalStateException("Collector is not closed!");
		}
		final List<TransferBuffer> list;
		synchronized (this) {
			final FifoQueueLinked<TransferBuffer> sequence = this.sequence;
			if (sequence == null || !sequence.hasNext()) {
				this.reset();
				return TransferBuffer.NUL_BUFFER;
			}
			list = new ArrayList<>();
			do {
				final TransferBuffer current = sequence.pollFirst();
				if (current == this.binary) {
					this.binary.queued();
					this.binary = null;
				}
				if (current.hasRemaining()) {
					list.add(current);
				}
			} while (sequence.hasNext());
			this.sequence = null;
			this.closed = false;
		}
		final int size = list.size();
		if (size == 1) {
			return list.get(0);
		}
		if (size == 0) {
			return TransferBuffer.NUL_BUFFER;
		}
		return new BufferSequence(list.toArray(new TransferBuffer[size]));
	}

	@Override
	public final TransferCopier toCloneFactory() {

		if (!this.closed) {
			throw new IllegalStateException("Collector is not closed!");
		}
		final List<TransferCopier> list;
		synchronized (this) {
			final FifoQueueLinked<TransferBuffer> sequence = this.sequence;
			if (sequence == null || !sequence.hasNext()) {
				this.reset();
				return TransferCopier.NUL_COPIER;
			}
			list = new ArrayList<>();
			do {
				final TransferBuffer current = sequence.pollFirst();
				if (current == this.binary) {
					this.binary.queued();
					this.binary = null;
				}
				if (current.hasRemaining()) {
					list.add(current.toBinary());
				}
			} while (sequence.hasNext());
			this.sequence = null;
			this.closed = false;
		}
		final int size = list.size();
		if (size == 1) {
			return list.get(0);
		}
		if (size == 0) {
			return TransferCopier.NUL_COPIER;
		}
		return new CopierSequence(list.toArray(new TransferCopier[size]));
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + "(length=" + this.length() + ", chunking=" + this.chunking + ", closed=" + this.closed + ")";
	}

	@Override
	public final void write(final byte[] bytes) throws IOException {

		if (this.closed) {
			throw new IllegalStateException("Collector is closed!");
		}
		synchronized (this) {
			if (this.chunking) {
				if (this.chunkCollected > 0 && this.chunkCollected + bytes.length > this.chunkMaxLimit) {
					if (this.chunkCollected < this.chunkMinLimit) {
						final int limit = this.chunkMaxLimit - this.chunkCollected;
						this.chunkCollected += limit;
						this.chunkCollector.absorbArray(bytes, 0, limit);
						this.flushChunk();
						this.absorbArray(bytes, 0 + limit, bytes.length - limit);
						return;
					}
					this.flushChunk();
				}
				this.chunkCollected += bytes.length;
				this.chunkCollector.absorbArray(bytes, 0, bytes.length);
				return;
			}
			if (this.connected != null) {
				if (!this.connected.absorbArray(bytes, 0, bytes.length)) {
					this.connected = null;
				}
				return;
			}
			if (this.binary == null) {
				final CollBuf binary = bytes.length > Transfer.BUFFER_MAX
					? (CollBuf) new CollBufTemp(this, bytes)
					: new CollBufBinary(this, bytes);
				this.addSequence(binary, binary);
			} else {
				this.binary.absorbArray(bytes, 0, bytes.length);
			}
		}
	}

	@Override
	public final void write(final byte[] bytes, final int offset, final int length) throws IOException {

		if (this.closed) {
			throw new IllegalStateException("Collector is closed!");
		}
		synchronized (this) {
			if (this.chunking) {
				if (this.chunkCollected > 0 && this.chunkCollected + length > this.chunkMaxLimit) {
					if (this.chunkCollected < this.chunkMinLimit) {
						final int limit = this.chunkMaxLimit - this.chunkCollected;
						this.chunkCollected += limit;
						this.chunkCollector.absorbArray(bytes, offset, limit);
						this.flushChunk();
						this.absorbArray(bytes, offset + limit, length - limit);
						return;
					}
					this.flushChunk();
				}
				this.chunkCollected += length;
				this.chunkCollector.absorbArray(bytes, offset, length);
				return;
			}
			if (this.connected != null) {
				if (!this.connected.absorbArray(bytes, offset, length)) {
					this.connected = null;
				}
				return;
			}
			if (this.binary == null) {
				final CollBuf binary = length > Transfer.BUFFER_MAX
					? (CollBuf) new CollBufTemp(this, bytes, offset, length)
					: new CollBufBinary(this, bytes, offset, length);
				this.addSequence(binary, binary);
			} else {
				this.binary.write(bytes, offset, length);
			}
		}
	}

	@Override
	public final void write(final int i) throws IOException {

		if (this.closed) {
			throw new IllegalStateException("Collector is closed!");
		}
		synchronized (this) {
			if (this.chunking) {
				if (this.chunkCollected == this.chunkMaxLimit) {
					this.flushChunk();
				}
				this.chunkCollected++;
				this.chunkCollector.absorb(i);
				return;
			}
			if (this.connected != null) {
				if (!this.connected.absorb(i)) {
					this.connected = null;
				}
				return;
			}
			if (this.binary == null) {
				final CollBuf binary = new CollBufBinary(this);
				this.addSequence(binary, binary);
			}
			this.binary.write(i);
		}
	}
}
