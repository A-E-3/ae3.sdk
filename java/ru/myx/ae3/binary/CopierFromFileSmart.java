/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ConcurrentModificationException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.help.Format;

final class CopierFromFileSmart implements BaseObjectNoOwnProperties, TransferCopier, Describable {
	
	private static final long SMART_LOAD_DELAY_DENSITY = 5L * 60_000L;
	
	private final File file;
	
	private byte[] loaded = null;
	
	private final long skip;
	
	private final long limit;
	
	private byte loadLeft;
	
	private long loadDate;
	
	CopierFromFileSmart(final File file, final long skip, final long limit, final byte left) throws IllegalArgumentException {
		
		assert file != null : "File is null";
		if (!file.isFile()) {
			throw new IllegalArgumentException("file '" + file.getAbsolutePath() + "' doesn't exist or not a file!");
		}
		this.file = file;
		this.skip = skip;
		this.limit = limit;
		this.loadLeft = left;
		this.loadDate = -1L;
	}
	
	@Override
	public String baseDescribe() {
		
		return "[" + this.getClass().getSimpleName() + " size=" + Format.Compact.toBytes(this.limit - this.skip) + "]";
	}
	
	@Override
	public final CopierFromFileSmart baseValue() {
		
		return this;
	}
	
	@Override
	public int compareTo(final TransferCopier o) {
		
		if (o == null) {
			return 1;
		}
		if (o.length() == 0) {
			return this.length() == 0
				? 0
				: 1;
		}
		try {
			return Transfer.compareStreams(this.nextInputStream(), o.nextInputStream());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int copy(final long start, final byte[] target, final int offset, final int count) throws ConcurrentModificationException {
		
		final long position = this.skip + start;
		if (position >= this.limit) {
			return 0;
		}
		if (this.loaded != null) {
			final int length = (int) Math.min(this.loaded.length - start, count);
			System.arraycopy(this.loaded, (int) start, target, offset, length);
			return length;
		}
		try (final RandomAccessFile in = new RandomAccessFile(this.file, "r")) {
			if (position > 0) {
				in.seek(position);
			}
			final int remaining = (int) Math.min(this.limit - position, count);
			in.readFully(target, offset, remaining);
			return remaining;
		} catch (final IOException e) {
			throw new RuntimeException("Error reading file contents", e);
		}
	}
	
	@Override
	public final MessageDigest getMessageDigest() {
		
		return this.updateMessageDigest(Engine.getMessageDigestInstance());
	}
	
	@Override
	public final long length() {
		
		return this.limit - this.skip;
	}
	
	@Override
	public final TransferBuffer nextCopy() throws ConcurrentModificationException {
		
		if (this.loaded == null) {
			if (!this.file.isFile()) {
				throw new ConcurrentModificationException("file '" + this.file.getAbsolutePath() + "' is deleted by another process!");
			}
			if (this.loadLeft > 0) {
				final long time = Engine.fastTime();
				if (this.loadDate != -1L && this.loadDate + CopierFromFileSmart.SMART_LOAD_DELAY_DENSITY < time) {
					this.loadLeft--;
				}
				this.loadDate = time;
			}
			if (this.loadLeft > 0) {
				return new BufferFilePart(this.file, this.skip, this.limit);
			}
			synchronized (this) {
				if (this.loaded == null) {
					final byte[] buffer = new byte[(int) (this.limit - this.skip)];
					try (final RandomAccessFile raf = new RandomAccessFile(this.file, "r")) {
						if (this.skip > 0L) {
							raf.seek(this.skip);
						}
						raf.readFully(buffer);
					} catch (final FileNotFoundException e) {
						return null;
					} catch (final IOException e) {
						throw new RuntimeException("While reading: " + this.file.getAbsolutePath(), e);
					}
					this.loaded = buffer;
				}
			}
		}
		return new WrapBuffer(this.loaded);
	}
	
	@Override
	public byte[] nextDirectArray() {
		
		return this.nextCopy().toDirectArray();
	}
	
	@Override
	public InputStream nextInputStream() {
		
		return this.nextCopy().toInputStream();
	}
	
	@Override
	public final Reader nextReaderUtf8() throws IOException {
		
		return this.nextCopy().toReaderUtf8();
	}
	
	@Override
	public TransferCopier slice(final long start, final long count) throws ConcurrentModificationException {
		
		final long avail = this.limit - this.skip;
		if (start == 0 && avail == count) {
			return this;
		}
		if (start >= avail) {
			return TransferCopier.NUL_COPIER;
		}
		final long length = Math.min(avail, count);
		if (this.loaded != null) {
			return new WrapCopier(this.loaded, (int) start, (int) length);
		}
		final long skip = this.skip + start;
		if (length <= Transfer.BUFFER_SMALL) {
			final byte[] buffer = new byte[(int) length];
			try (final RandomAccessFile raf = new RandomAccessFile(this.file, "r")) {
				if (skip > 0L) {
					raf.seek(skip);
				}
				raf.readFully(buffer);
			} catch (final IOException e) {
				throw new RuntimeException("Error reading file contents", e);
			}
			return new WrapCopier(buffer);
		}
		return new CopierFilePart(this.file, skip, skip + length);
	}
	
	@Override
	public String toString() {
		
		return this.toString(Charset.defaultCharset());
	}
	
	@Override
	public final String toString(final Charset charset) {
		
		return this.nextCopy().toString(charset);
	}
	
	@Override
	public final String toString(final String encoding) throws UnsupportedEncodingException {
		
		return this.nextCopy().toString(encoding);
	}
	
	@Override
	public String toStringUtf8() {
		
		return this.nextCopy().toString(StandardCharsets.UTF_8);
	}
	
	@Override
	public MessageDigest updateMessageDigest(final MessageDigest digest) {
		
		return this.nextCopy().updateMessageDigest(digest);
	}
}
