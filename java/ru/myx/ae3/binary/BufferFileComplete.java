/*
 * Created on 19.05.2005
 */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import ru.myx.ae3.Engine;
import ru.myx.util.WeakFinalizer;

/** @author myx */
public final class BufferFileComplete implements TransferBuffer {

	private static void finalizeStatic(final BufferFileComplete x) {

		if (x.in != null) {
			try {
				x.in.close();
			} catch (final Throwable t) {
				// ignore
			}
			x.in = null;
		}
	}
	
	private final File file;
	
	private RandomAccessFile in;

	private long position;

	{
		WeakFinalizer.register(this, BufferFileComplete::finalizeStatic);
	}
	
	/** Please consider using one of the Transfer.createBuffer(). They are smarter then just
	 * creating instance of this class in every situation.
	 *
	 * @param file
	 * @throws IllegalArgumentException */
	public BufferFileComplete(final File file) throws IllegalArgumentException {

		assert file != null : "file is null";
		if (!file.isFile()) {
			throw new IllegalArgumentException("file '" + file.getAbsolutePath() + "' doesn't exist or not a file!");
		}
		this.file = file;
		this.position = 0;
	}
	
	BufferFileComplete(final File file, final long position) throws IllegalArgumentException {

		assert file != null : "file is null";
		if (!file.isFile()) {
			throw new IllegalArgumentException("file '" + file.getAbsolutePath() + "' doesn't exist or not a file!");
		}
		this.file = file;
		this.position = position;
	}
	
	@Override
	public final void destroy() {

		if (this.in != null) {
			try {
				this.in.close();
			} catch (final Throwable t) {
				// ignore
			}
			this.in = null;
		}
	}
	
	@Override
	public final MessageDigest getMessageDigest() {

		return this.updateMessageDigest(Engine.getMessageDigestInstance());
	}
	
	@Override
	public final boolean hasRemaining() {

		return this.file.length() > this.position;
	}
	
	@Override
	public boolean isDirectAbsolutely() {

		return false;
	}
	
	@Override
	public final boolean isSequence() {

		return false;
	}
	
	@Override
	public final int next() {

		final long limit = this.file.length();
		if (this.position == limit) {
			return -1;
		}
		try {
			if (this.in == null) {
				this.in = new RandomAccessFile(this.file, "r");
				if (this.position != 0) {
					this.in.seek(this.position);
				}
			}
			final int i = this.in.read();
			if (i != -1) {
				this.position++;
			} else {
				this.in.close();
				this.in = null;
				this.position = limit;
			}
			return i;
		} catch (final IOException e) {
			this.position = limit;
			return -1;
		}
	}
	
	@Override
	public final int next(final byte[] buffer, final int offset, final int length) {

		final long limit = this.file.length();
		final int amount = (int) Math.min(limit - this.position, length);
		if (amount > 0) {
			try {
				if (this.in == null) {
					this.in = new RandomAccessFile(this.file, "r");
					if (this.position != 0) {
						this.in.seek(this.position);
					}
				}
				this.in.readFully(buffer, offset, amount);
				this.position += amount;
				if (limit == this.position) {
					this.in.close();
					this.in = null;
				}
			} catch (final IOException e) {
				throw new RuntimeException("Error reading file contents", e);
			}
		}
		return amount;
	}
	
	@Override
	public final TransferBuffer nextSequenceBuffer() {

		throw new UnsupportedOperationException("Not a sequence!");
	}
	
	@Override
	public final long remaining() {

		return this.file.length() - this.position;
	}
	
	@Override
	public final TransferCopier toBinary() {

		final long limit = this.file.length();
		if (this.position == 0) {
			this.position = limit;
			return new CopierFileComplete(this.file);
		}
		if (this.position >= limit) {
			return TransferCopier.NUL_COPIER;
		}
		return new CopierFilePart(this.file, this.position, limit);
	}
	
	@Override
	public final byte[] toDirectArray() {

		try {
			if (this.in == null) {
				this.in = new RandomAccessFile(this.file, "r");
				if (this.position != 0) {
					this.in.seek(this.position);
				}
			}
			final long limit = this.file.length();
			final long remaining = limit - this.position;
			if (remaining > Integer.MAX_VALUE) {
				throw new RuntimeException("Bigger than maximum byte array size, size=" + remaining + "!");
			}
			final byte[] result = new byte[(int) remaining];
			try {
				this.in.readFully(result);
			} finally {
				try {
					this.in.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.in = null;
			}
			this.position = limit;
			return result;
		} catch (final IOException e) {
			throw new RuntimeException("Error reading file contents", e);
		}
	}
	
	@Override
	public final FileInputStream toInputStream() {

		try {
			final FileInputStream stream = new FileInputStream(this.file);
			if (this.position != 0) {
				stream.skip(this.position);
			}
			this.position = this.file.length();
			if (this.in != null) {
				try {
					this.in.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.in = null;
			}
			return stream;
		} catch (final IOException e) {
			throw new RuntimeException("Error reading file contents", e);
		}
	}
	
	@Override
	public final TransferBuffer toNioBuffer(final ByteBuffer target) throws IOException {

		final long limit = this.file.length();
		final long sourceRemaining = limit - this.position;
		if (sourceRemaining <= 0) {
			return null;
		}
		final int targetRemaining = target.remaining();
		if (targetRemaining <= 0) {
			return this;
		}
		if (this.in == null) {
			this.in = new RandomAccessFile(this.file, "r");
			if (this.position != 0) {
				this.in.seek(this.position);
			}
		}
		if (targetRemaining > sourceRemaining) {
			final int targetLimit = target.limit();
			try {
				target.limit((int) (target.position() + sourceRemaining));
				final int read = this.in.getChannel().read(target);
				if (read == -1) {
					this.position = limit;
					this.in.close();
					this.in = null;
					return null;
				}
				this.position += read;
				if (read >= sourceRemaining) {
					this.in.close();
					this.in = null;
					return null;
				}
				return this;
			} finally {
				target.limit(targetLimit);
			}
		}
		final int read = this.in.getChannel().read(target);
		if (read <= 0) {
			this.in.close();
			this.in = null;
			this.position = limit;
			return null;
		}
		this.position += read;
		if (read >= sourceRemaining) {
			this.in.close();
			this.in = null;
			this.position = limit;
			return null;
		}
		return this;
	}
	
	@Override
	public final InputStreamReader toReaderUtf8() {

		return new InputStreamReader(this.toInputStream(), Engine.CHARSET_UTF8);
	}
	
	@Override
	public final String toString() {

		return this.toString(Engine.CHARSET_DEFAULT);
	}
	
	@Override
	public final String toString(final Charset charset) {

		final byte[] bytes = this.toDirectArray();
		return bytes == null
			? null
			: bytes.length == 0
				? ""
				: new String(bytes, charset);
	}
	
	@Override
	public final String toString(final String encoding) throws UnsupportedEncodingException {

		final byte[] bytes = this.toDirectArray();
		return bytes == null
			? null
			: bytes.length == 0
				? ""
				: new String(bytes, encoding);
	}
	
	@Override
	public final TransferBuffer toSubBuffer(final long start, final long end) {

		final long limit = this.file.length();
		final long remaining = limit - this.position;
		if (start < 0 || start > end || end > remaining) {
			throw new IllegalArgumentException("Indexes are out of bounds: start=" + start + ", end=" + end + ", length=" + remaining);
		}
		if (limit != this.position + end) {
			return new BufferFilePart(this.file, this.position + start, this.position + end);
		}
		this.position += start;
		if (this.in != null) {
			if (this.position == limit) {
				try {
					this.in.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.in = null;
			} else {
				if (start > 0) {
					try {
						this.in.seek(this.position);
					} catch (final IOException e) {
						throw new ArrayIndexOutOfBoundsException("Error while skipping!");
					}
				}
			}
		}
		return this;
	}
	
	@Override
	public final MessageDigest updateMessageDigest(final MessageDigest digest) {

		try (final RandomAccessFile in = new RandomAccessFile(this.file, "r")) {
			if (this.position != 0) {
				in.seek(this.position);
			}
			final long remaining = this.file.length() - this.position;
			final byte[] result = new byte[remaining < 65536
				? (int) remaining
				: 65536];
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
			throw new RuntimeException("Error reading file contents", e);
		}
		return digest;
	}
}
