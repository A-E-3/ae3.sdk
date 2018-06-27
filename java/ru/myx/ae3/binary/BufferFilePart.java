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

final class BufferFilePart implements TransferBuffer {
	private final File			file;
	
	private long				limit;
	
	private RandomAccessFile	in;
	
	private long				position;
	
	BufferFilePart(final File file) throws IllegalArgumentException {
		assert file != null : "file is null";
		if (!file.isFile()) {
			throw new IllegalArgumentException( "file '" + file.getAbsolutePath() + "' doesn't exist or not a file!" );
		}
		this.file = file;
		this.limit = file.length();
		this.position = 0;
	}
	
	BufferFilePart(final File file, final long position, final long limit) throws IllegalArgumentException {
		assert file != null : "file is null";
		if (!file.isFile()) {
			throw new IllegalArgumentException( "file '" + file.getAbsolutePath() + "' doesn't exist or not a file!" );
		}
		this.file = file;
		this.limit = limit;
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
	protected void finalize() throws Throwable {
		if (this.in != null) {
			try {
				this.in.close();
			} catch (final Throwable t) {
				// ignore
			}
			this.in = null;
		}
		super.finalize();
	}
	
	@Override
	public final MessageDigest getMessageDigest() {
		return this.updateMessageDigest( Engine.getMessageDigestInstance() );
	}
	
	@Override
	public final boolean hasRemaining() {
		return this.limit > this.position;
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
		if (this.position == this.limit) {
			return -1;
		}
		try {
			if (this.in == null) {
				this.in = new RandomAccessFile( this.file, "r" );
				if (this.position != 0) {
					this.in.seek( this.position );
				}
			}
			final int i = this.in.read();
			if (i != -1) {
				this.position++;
			} else {
				this.in.close();
				this.in = null;
				this.position = this.limit;
			}
			return i;
		} catch (final IOException e) {
			this.position = this.limit;
			return -1;
		}
	}
	
	@Override
	public final int next(final byte[] buffer, final int offset, final int length) {
		final int amount = (int) Math.min( this.limit - this.position, length );
		if (amount > 0) {
			try {
				if (this.in == null) {
					this.in = new RandomAccessFile( this.file, "r" );
					if (this.position != 0) {
						this.in.seek( this.position );
					}
				}
				this.in.readFully( buffer, offset, amount );
				this.position += amount;
				if (this.limit == this.position) {
					this.in.close();
					this.in = null;
				}
			} catch (final IOException e) {
				throw new RuntimeException( "Error reading file contents", e );
			}
		}
		return amount;
	}
	
	@Override
	public final TransferBuffer nextSequenceBuffer() {
		throw new UnsupportedOperationException( "Not a sequence!" );
	}
	
	@Override
	public final long remaining() {
		return this.limit - this.position;
	}
	
	@Override
	public final TransferCopier toBinary() {
		if (this.position == 0) {
			this.position = this.limit;
			return new CopierFileComplete( this.file );
		}
		if (this.position >= this.limit) {
			return TransferCopier.NUL_COPIER;
		}
		return new CopierFilePart( this.file, this.position, this.limit );
	}
	
	@Override
	public final byte[] toDirectArray() {
		try {
			if (this.in == null) {
				this.in = new RandomAccessFile( this.file, "r" );
				if (this.position != 0) {
					this.in.seek( this.position );
				}
			}
			final long remaining = this.limit - this.position;
			if (remaining > Integer.MAX_VALUE) {
				throw new RuntimeException( "Bigger than maximum byte array size, size=" + remaining + "!" );
			}
			final byte[] result = new byte[(int) remaining];
			try {
				this.in.readFully( result );
			} finally {
				try {
					this.in.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.in = null;
			}
			this.position = this.limit;
			return result;
		} catch (final IOException e) {
			throw new RuntimeException( "Error reading file contents", e );
		}
	}
	
	@Override
	public final FileInputStream toInputStream() {
		try {
			final FileInputStream stream = new FileInputStream( this.file );
			if (this.position != 0) {
				stream.skip( this.position );
			}
			this.position = this.limit;
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
			throw new RuntimeException( "Error reading file contents", e );
		}
	}
	
	@Override
	public final TransferBuffer toNioBuffer(final ByteBuffer target) throws IOException {
		final long sourceRemaining = this.limit - this.position;
		if (sourceRemaining <= 0) {
			return null;
		}
		final int targetRemaining = target.remaining();
		if (targetRemaining <= 0) {
			return this;
		}
		if (this.in == null) {
			this.in = new RandomAccessFile( this.file, "r" );
			if (this.position != 0) {
				this.in.seek( this.position );
			}
		}
		if (targetRemaining > sourceRemaining) {
			final int limit = target.limit();
			try {
				target.limit( (int) (target.position() + sourceRemaining) );
				final int read = this.in.getChannel().read( target );
				if (read == -1) {
					this.position = this.limit;
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
				target.limit( limit );
			}
		}
		final int read = this.in.getChannel().read( target );
		if (read <= 0) {
			this.in.close();
			this.in = null;
			this.limit = this.position;
			return null;
		}
		this.position += read;
		if (read >= sourceRemaining) {
			this.in.close();
			this.in = null;
			this.position = this.limit;
			return null;
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
		final long remaining = this.limit - this.position;
		if (start < 0 || start > end || end > remaining) {
			throw new IllegalArgumentException( "Indexes are out of bounds: start="
					+ start
					+ ", end="
					+ end
					+ ", length="
					+ remaining );
		}
		this.limit = this.position + end;
		this.position += start;
		if (this.in != null) {
			if (this.position == this.limit) {
				try {
					this.in.close();
				} catch (final Throwable t) {
					// ignore
				}
				this.in = null;
			} else {
				if (start > 0) {
					try {
						this.in.seek( this.position );
					} catch (final IOException e) {
						throw new ArrayIndexOutOfBoundsException( "Error while skipping!" );
					}
				}
			}
		}
		return this;
	}
	
	@Override
	public final MessageDigest updateMessageDigest(final MessageDigest digest) {
		try (final RandomAccessFile in = new RandomAccessFile( this.file, "r" )) {
			if (this.position != 0) {
				in.seek( this.position );
			}
			final long remaining = this.limit - this.position;
			final byte[] result = new byte[remaining < 65536
					? (int) remaining
					: 65536];
			for (long left = remaining; left > 0;) {
				final int read = in.read( result, 0, (int) (left < result.length
						? left
						: result.length) );
				if (read <= 0) {
					break;
				}
				left -= read;
				digest.update( result, 0, read );
			}
		} catch (final IOException e) {
			throw new RuntimeException( "Error reading file contents", e );
		}
		return digest;
	}
}
