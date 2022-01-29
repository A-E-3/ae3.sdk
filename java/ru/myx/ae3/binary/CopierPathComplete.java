/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.util.ConcurrentModificationException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.help.Format;

final class CopierPathComplete implements BaseObjectNoOwnProperties, TransferCopier {

	private final Path file;

	CopierPathComplete(final Path file) throws IllegalArgumentException {

		assert file != null : "file is null";
		if (Files.isRegularFile(file)) {
			throw new IllegalArgumentException("file '" + file.toAbsolutePath() + "' doesn't exist or not a file!");
		}
		this.file = file;
	}

	@Override
	public BasePrimitiveString baseToString() {

		try {
			return Base.forString("[" + this.getClass().getSimpleName() + " size=" + Format.Compact.toBytes(Files.size(this.file)) + "]");
		} catch (final IOException e) {
			return Base.forString("[" + this.getClass().getSimpleName() + " error=" + e.getMessage() + "]");
		}
	}

	@Override
	public final CopierPathComplete baseValue() {

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

		try (final SeekableByteChannel in = Files.newByteChannel(this.file, StandardOpenOption.READ)) {
			if (start >= in.size()) {
				return 0;
			}
			if (start > 0) {
				in.position(start);
			}

			final ByteBuffer buffer = ByteBuffer.wrap(target, offset, count);
			for (int left = count; left > 0;) {
				final int read = in.read(buffer);
				if (read == -1) {
					return count - left;
				}
				left -= read;
			}
			return count;
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

		try {
			return Files.size(this.file);
		} catch (final IOException e) {
			throw new RuntimeException("Error getting file size", e);
		}
	}

	@Override
	public final TransferBuffer nextCopy() {

		/** TODO: make BufferPathComplete */
		return new BufferFileComplete(this.file.toFile());
	}

	@Override
	public byte[] nextDirectArray() throws ConcurrentModificationException {

		try {
			return Files.readAllBytes(this.file);
		} catch (final IOException e) {
			throw new RuntimeException("Error reading file contents", e);
		}
	}

	@Override
	public final InputStream nextInputStream() throws IOException {

		return Files.newInputStream(this.file);
	}

	@Override
	public final InputStreamReader nextReaderUtf8() throws IOException {

		return new InputStreamReader(Files.newInputStream(this.file), StandardCharsets.UTF_8);
	}

	@Override
	public TransferCopier slice(final long start, final long count) throws ConcurrentModificationException {

		final File file = this.file.toFile();
		final long length = file.length();
		if (start >= length) {
			return TransferCopier.NUL_COPIER;
		}
		return new CopierFilePart(file, start, Math.min(start + count, length));
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
