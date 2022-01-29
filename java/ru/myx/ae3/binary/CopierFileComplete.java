/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ConcurrentModificationException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.help.Format;

final class CopierFileComplete implements BaseObjectNoOwnProperties, TransferCopier {
	
	private final File file;

	CopierFileComplete(final File file) throws IllegalArgumentException {

		assert file != null : "file is null";
		if (!file.isFile()) {
			throw new IllegalArgumentException("file '" + file.getAbsolutePath() + "' doesn't exist or not a file!");
		}
		this.file = file;
	}

	@Override
	public BasePrimitiveString baseToString() {
		
		return Base.forString("[" + this.getClass().getSimpleName() + " size=" + Format.Compact.toBytes(this.file.length()) + "]");
	}

	@Override
	public final CopierFileComplete baseValue() {
		
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
		
		final long available = this.file.length();
		if (start >= available) {
			return 0;
		}
		try (final RandomAccessFile in = new RandomAccessFile(this.file, "r")) {
			if (start > 0) {
				in.seek(start);
			}
			final int length = (int) Math.min(available - start, count);
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
		
		return this.file.length();
	}

	@Override
	public final TransferBuffer nextCopy() {
		
		return new BufferFileComplete(this.file);
	}

	@Override
	public byte[] nextDirectArray() throws ConcurrentModificationException {
		
		try (final RandomAccessFile in = new RandomAccessFile(this.file, "r")) {
			final long remaining = in.length();
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
	public final FileInputStream nextInputStream() throws FileNotFoundException {
		
		return new FileInputStream(this.file);
	}

	@Override
	public final InputStreamReader nextReaderUtf8() throws FileNotFoundException {
		
		return new InputStreamReader(new FileInputStream(this.file), StandardCharsets.UTF_8);
	}

	@Override
	public TransferCopier slice(final long start, final long count) throws ConcurrentModificationException {
		
		final long length = this.file.length();
		if (start >= length) {
			return TransferCopier.NUL_COPIER;
		}
		return new CopierFilePart(this.file, start, Math.min(start + count, length));
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
	public String toStringUtf8() {
		
		return this.nextCopy().toString(StandardCharsets.UTF_8);
	}

	@Override
	public final MessageDigest updateMessageDigest(final MessageDigest digest) {
		
		return this.nextCopy().updateMessageDigest(digest);
	}
}
