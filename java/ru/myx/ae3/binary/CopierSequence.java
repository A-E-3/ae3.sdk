/*
 * Created on 19.05.2005
 */
package ru.myx.ae3.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ConcurrentModificationException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.help.Format;

final class CopierSequence implements BaseObjectNoOwnProperties, TransferCopier, Describable {
	
	private final TransferCopier[] sequence;
	
	private final int length;
	
	CopierSequence(final TransferCopier[] sequence) {
		
		this.sequence = sequence;
		int length = 0;
		for (int i = sequence.length - 1; i >= 0; --i) {
			length += sequence[i].length();
		}
		this.length = length;
	}
	
	@Override
	public String baseDescribe() {
		
		return "[" + this.getClass().getSimpleName() + " size=" + Format.Compact.toBytes(this.length) + "]";
	}
	
	@Override
	public final CopierSequence baseValue() {
		
		return this;
	}
	
	@Override
	public int compareTo(final TransferCopier o) {
		
		if (o == null) {
			return 1;
		}
		if (o.length() == 0) {
			return this.length == 0
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
		
		int index = 0, copied = 0;
		long skipped = 0;
		for (;;) {
			if (index == this.sequence.length) {
				return copied;
			}
			final TransferCopier copier = this.sequence[index++];
			final long length = copier.length();
			if (skipped < start) {
				if (length <= start - skipped) {
					skipped += length;
					continue;
				}
			}
			{
				final long skip = start - skipped;
				final int task = (int) Math.min(length - skip, count - copied);
				final int done = copier.copy(skip, target, offset + copied, task);
				if (done == 0) {
					return copied;
				}
				copied += done;
				if (copied == count) {
					return count;
				}
				skipped += skip;
			}
		}
	}
	
	@Override
	public final MessageDigest getMessageDigest() {
		
		return this.updateMessageDigest(Engine.getMessageDigestInstance());
	}
	
	@Override
	public final long length() {
		
		return this.length;
	}
	
	@Override
	public final TransferBuffer nextCopy() {
		
		final TransferBuffer[] result = new TransferBuffer[this.sequence.length];
		for (int i = this.sequence.length - 1; i >= 0; --i) {
			result[i] = this.sequence[i].nextCopy();
		}
		return new BufferSequence(result);
	}
	
	@Override
	public final byte[] nextDirectArray() {
		
		return this.nextCopy().toDirectArray();
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
		
		if (start == 0 && count == this.length) {
			return this;
		}
		if (start >= this.length) {
			return TransferCopier.NUL_COPIER;
		}
		return this.nextCopy().toSubBuffer(start, Math.min(start + count, this.length)).toBinary();
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
		
		int index = 0;
		for (;;) {
			if (index == this.sequence.length) {
				break;
			}
			this.sequence[index++].updateMessageDigest(digest);
		}
		return digest;
	}
}
