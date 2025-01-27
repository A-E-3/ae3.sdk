/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ConcurrentModificationException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.help.Format;

final class CopierFilePart implements BaseObjectNoOwnProperties, TransferCopier, Describable {

	private final File file;
	
	private final long skip;
	
	private final long limit;
	
	CopierFilePart(final File file, final long position, final long limit) throws IllegalArgumentException {
		
		assert file != null : "file is null";
		if (!file.isFile()) {
			throw new IllegalArgumentException("file '" + file.getAbsolutePath() + "' doesn't exist or not a file!");
		}
		this.file = file;
		this.skip = position;
		this.limit = limit;
	}
	
	@Override
	public String baseDescribe() {

		return "[" + this.getClass().getSimpleName() + " size=" + Format.Compact.toBytes(this.limit - this.skip) + "]";
	}
	
	@Override
	public final CopierFilePart baseValue() {

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
		try (final RandomAccessFile in = new RandomAccessFile(this.file, "r")) {
			if (position > 0) {
				in.seek(position);
			}
			final int length = (int) Math.min(this.limit - position, count);
			in.readFully(target, offset, length);
			return length;
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

		if (!this.file.isFile()) {
			throw new ConcurrentModificationException("file '" + this.file.getAbsolutePath() + "' is deleted by another process!");
		}
		return new BufferFilePart(this.file, this.skip, this.limit);
	}
	
	@Override
	public byte[] nextDirectArray() throws ConcurrentModificationException {

		try (final RandomAccessFile in = new RandomAccessFile(this.file, "r")) {
			if (this.skip != 0) {
				in.seek(this.skip);
			}
			final long remaining = this.limit - this.skip;
			if (remaining > Integer.MAX_VALUE) {
				throw new RuntimeException("Bigger than maximum byte array size, size=" + remaining + "!");
			}
			final byte[] result = new byte[(int) remaining];
			in.readFully(result);
			return result;
		} catch (final IOException e) {
			throw new RuntimeException("Error reading file contents", e);
		}
	}
	
	@Override
	public final InputStream nextInputStream() {

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
		final long skip = this.skip + start;
		final long limit = Math.min(this.limit, skip + count);
		return new CopierFilePart(this.file, skip, limit);
	}
	
	@Override
	public final String toString() {

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
	public final MessageDigest updateMessageDigest(final MessageDigest digest) {

		return this.nextCopy().updateMessageDigest(digest);
	}
}
