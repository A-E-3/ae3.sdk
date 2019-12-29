/*
 * Created on 19.05.2005
 */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

import ru.myx.ae3.Engine;
import ru.myx.ae3.report.Report;
import ru.myx.util.WeakFinalizer;

final class CollBufTemp extends CollBuf {

	private final static void finalizeStatic(final CollBufTemp x) {

		x.destroy();
	}
	
	private long position;
	
	private final File file;
	
	private long length = -1;
	
	private RandomAccessFile input;
	
	private RandomAccessFile output;
	
	private final Collector collector;

	// ////////////////////////////////////////////////////////////////////////
	// ////////////////////////////////////////////////////////////////////////
	private boolean readyInput = false;

	{
		WeakFinalizer.register(this, CollBufTemp::finalizeStatic);
	}
	
	CollBufTemp(final Collector collector, final byte[] data) throws IOException {

		this.collector = collector;
		this.file = Behavior.tempFile();
		this.output = new RandomAccessFile(this.file, "rw");
		this.output.write(data);
	}
	
	CollBufTemp(final Collector collector, final byte[] data, final int off, final int len) throws IOException {

		this.collector = collector;
		this.file = Behavior.tempFile();
		this.output = new RandomAccessFile(this.file, "rw");
		this.output.write(data, off, len);
	}
	
	CollBufTemp(final Collector collector, final ByteBuffer data) throws IOException {

		this.collector = collector;
		this.file = Behavior.tempFile();
		this.output = new RandomAccessFile(this.file, "rw");
		this.output.getChannel().write(data);
	}
	
	private final void checkInput() {

		if (this.input == null && this.readyInput) {
			this.readyInput = false;
			try {
				this.input = new RandomAccessFile(this.file, "r");
				if (this.position > 0) {
					this.input.seek(this.position);
				}
			} catch (final IOException e) {
				throw new RuntimeException("Error opening the file", e);
			}
		}
	}
	
	@Override
	public final void close() {

		try {
			if (this.output != null) {
				this.output.setLength(this.output.getFilePointer());
				this.output.close();
				this.output = null;
			}
		} catch (final IOException e) {
			Report.exception("COLLECTOR-BUFFER-TEMP", "Error while closing output", e);
		}
	}
	
	private final void closeSilently() {

		try {
			if (this.input != null) {
				this.input.close();
				this.input = null;
			}
			if (this.output != null) {
				this.output.close();
				this.output = null;
			}
			this.file.delete();
		} catch (final IOException e) {
			Report.exception("COLLECTOR-BUFFER-TEMP", "Error while closing output silently", e);
		}
	}
	
	@Override
	public final void destroy() {

		try {
			if (this.input != null) {
				try {
					this.input.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.input = null;
			}
			if (this.output != null) {
				try {
					this.output.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.output = null;
			}
		} catch (final Throwable t) {
			// ignore
		}
	}
	
	@Override
	public MessageDigest getMessageDigest() {

		return this.updateMessageDigest(Engine.getMessageDigestInstance());
	}
	
	@Override
	public final boolean hasRemaining() {

		return this.remaining() > 0;
	}
	
	@Override
	public final boolean isDirectAbsolutely() {

		return false;
	}
	
	@Override
	public final boolean isSequence() {

		return false;
	}
	
	@Override
	public final int next() {

		this.checkInput();
		if (this.input == null) {
			return -1;
		}
		try {
			final int i = this.input.read();
			if (i == -1) {
				this.closeSilently();
			}
			this.position++;
			return i;
		} catch (final IOException e) {
			this.closeSilently();
			return -1;
		}
	}
	
	@Override
	public final int next(final byte[] buffer, final int offset, final int length) {

		final long remaining = this.remaining();
		final int amount = (int) Math.min(remaining, length);
		if (amount > 0) {
			this.checkInput();
			try {
				this.input.readFully(buffer, offset, length);
			} catch (final IOException e) {
				this.closeSilently();
				throw new RuntimeException(e);
			}
			if (amount == remaining) {
				this.closeSilently();
			}
		}
		return amount;
	}
	
	@Override
	public final TransferBuffer nextSequenceBuffer() {

		throw new UnsupportedOperationException("Not a sequence!");
	}
	
	@Override
	public final void queued() {

		try {
			if (this.output != null) {
				this.output.setLength(this.output.getFilePointer());
				this.output.close();
				this.output = null;
			}
			this.length = (int) this.file.length();
			this.input = null;
			this.readyInput = true;
		} catch (final IOException e) {
			Report.exception("COLLECTOR-BUFFER-TEMP", "Error while queueing output", e);
		}
	}
	
	@Override
	public final long remaining() {

		if (this.length == -1) {
			if (this.output != null) {
				try {
					return this.output.getFilePointer();
				} catch (final IOException e) {
					Report.exception("COLLECTOR-BUFFER-TEMP", "Error while getting the length of an incomplete binary buffer", e);
				}
			}
			return 0;
		}
		if (this.file == null) {
			return 0;
		}
		return this.length - this.position;
	}
	
	@Override
	public final void reset() {

		this.collector.binary = new CollBufBinary(this.collector);
		this.closeSilently();
	}
	
	@Override
	public final TransferCopier toBinary() {

		if (this.length == -1) {
			throw new IllegalStateException("collector buffer is still unfinished");
		}
		if (this.position == 0) {
			return new CopierFileComplete(this.file);
		}
		return new CopierFilePart(this.file, this.position, this.length);
	}
	
	@Override
	public final byte[] toDirectArray() {

		this.checkInput();
		final long remaining = this.remaining();
		if (remaining > Integer.MAX_VALUE) {
			throw new RuntimeException("Bigger than maximum byte array size, size=" + remaining + "!");
		}
		final int length = (int) remaining;
		int read = 0;
		final byte[] result = new byte[length];
		try {
			for (;;) {
				final int count = this.input.read(result, read, length - read);
				if (count <= 0) {
					throw new RuntimeException("Cannot read whole stream!");
				}
				read += count;
				this.position += read;
				if (read == length) {
					return result;
				}
			}
		} catch (final IOException e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	@Override
	public final FileInputStream toInputStream() {

		try {
			final FileInputStream result = new FileInputStream(this.file);
			if (this.position != 0) {
				result.skip(this.position);
			}
			return result;
		} catch (final IOException e) {
			Report.exception("COLLECTOR-BUFFER-TEMP", "Error while getting stream input, will return null", e);
			return null;
		}
	}
	
	@Override
	public final TransferBuffer toNioBuffer(final ByteBuffer target) throws IOException {

		final long remaining = this.remaining();
		if (remaining <= 0) {
			return null;
		}
		final int writable = target.remaining();
		if (writable <= 0) {
			return this;
		}
		this.checkInput();
		if (writable > remaining) {
			final int limit = target.limit();
			try {
				target.limit((int) (target.position() + remaining));
				final int read = this.input.getChannel().read(target);
				if (read == -1) {
					this.closeSilently();
					return null;
				}
				this.position += read;
				if (read == remaining) {
					this.closeSilently();
					return null;
				}
				return this;
			} finally {
				target.limit(limit);
			}
		}
		final int read = this.input.getChannel().read(target);
		this.position += read;
		if (read == remaining) {
			this.closeSilently();
			return null;
		}
		return this;
	}
	
	@Override
	public final InputStreamReader toReaderUtf8() {

		return new InputStreamReader(this.toInputStream(), Engine.CHARSET_UTF8);
	}
	
	@Override
	public final TransferBuffer toSubBuffer(final long start, final long end) {

		if (this.length == -1) {
			throw new IllegalStateException("collector buffer is still unfinished");
		}
		final long remaining = this.length - this.position;
		if (start < 0 || start > end || end > remaining) {
			throw new IllegalArgumentException("Indexes are out of bounds: start=" + start + ", end=" + end + ", length=" + remaining);
		}
		this.length = this.position + end;
		this.position += start;
		if (this.input != null) {
			if (this.position == this.length) {
				try {
					this.input.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.input = null;
			} else {
				try {
					this.input.seek(this.position);
				} catch (final IOException e) {
					throw new RuntimeException(e);
				}
			}
		}
		return this;
	}
	
	@Override
	public MessageDigest updateMessageDigest(final MessageDigest digest) {

		try (final RandomAccessFile in = new RandomAccessFile(this.file, "r")) {
			if (this.position != 0) {
				in.seek(this.position);
			}
			final long remaining = this.remaining();
			final byte[] result = new byte[65536];
			for (long left = remaining; left > 0;) {
				final int read = in.read(
						result,
						0,
						(int) (left < result.length
							? left
							: result.length));
				if (read <= 0) {
					break;
				}
				left -= read;
				digest.update(result, 0, read);
			}
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
		return digest;
	}
	
	@Override
	public final void write(final byte[] buff, final int off, final int len) throws IOException {

		this.output.write(buff, off, len);
	}
	
	@Override
	public final void write(final ByteBuffer buffer) throws IOException {

		this.output.getChannel().write(buffer);
	}
	
	@Override
	public final void write(final int i) throws IOException {

		this.output.write(i);
	}
}
