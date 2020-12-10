/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.binary;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ConcurrentModificationException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.help.Format;
import ru.myx.io.WrapInputStream;

/** @author myx */
public final class WrapCopier implements BaseObjectNoOwnProperties, TransferCopier, Describable {
	
	private final byte[] bytes;

	private final int offset;

	private final int length;

	/** @param bytes */
	public WrapCopier(final byte[] bytes) {

		this.bytes = bytes;
		this.offset = 0;
		this.length = bytes.length;
	}

	/** @param bytes
	 * @param offset
	 * @param length */
	public WrapCopier(final byte[] bytes, final int offset, final int length) {

		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
	}

	@Override
	public String baseDescribe() {
		
		return "[" + this.getClass().getSimpleName() + " size=" + Format.Compact.toBytes(this.length) + "]";
	}

	@Override
	public WrapCopier baseValue() {
		
		return this;
	}

	@Override
	public int compareTo(final TransferCopier o) {
		
		if (o == null) {
			return 1;
		}
		final long otherLength = o.length();
		if (otherLength < this.length) {
			return 1;
		}
		if (this.length < otherLength) {
			return -1;
		}
		if (otherLength == 0) {
			return 0;
		}
		final TransferBuffer buffer = o.nextCopy();
		for (int i = this.offset, l = this.length; l > 0; --l, ++i) {
			final int diff = (this.bytes[i] & 0xFF) - buffer.next();
			if (diff == 0) {
				continue;
			}
			return diff < 0
				? -1
				: 1;
		}
		return 0;
		/** <code>
		try {
			return Transfer.compareStreams( this.nextInputStream(), o.nextInputStream() );
		} catch (final IOException e) {
			throw new RuntimeException( e );
		}
		</code> */
	}

	@Override
	public int copy(final long start, final byte[] target, final int offset, final int count) throws ConcurrentModificationException {
		
		if (start >= this.length) {
			return 0;
		}
		final int length = Math.min(count, (int) (this.length - start));
		System.arraycopy(this.bytes, (int) (this.offset + start), target, offset, length);
		return length;
	}

	@Override
	public boolean equals(final Object obj) {
		
		if (obj == null || !(obj instanceof TransferCopier) || ((TransferCopier) obj).length() != this.length) {
			return false;
		}
		final TransferBuffer buffer = ((TransferCopier) obj).nextCopy();
		for (int i = this.offset, l = this.length; l > 0; --l, ++i) {
			if ((this.bytes[i] & 0xFF) != buffer.next()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public MessageDigest getMessageDigest() {
		
		final MessageDigest digest = Engine.getMessageDigestInstance();
		digest.update(this.bytes, this.offset, this.length);
		return digest;
	}

	@Override
	public int hashCode() {
		
		int result = 1;
		for (int i = this.offset, l = this.length; l > 0; --l, ++i) {
			result = 31 * result + (this.bytes[i] & 0xFF);
		}
		return result;
	}

	@Override
	public final long length() {
		
		return this.length;
	}

	@Override
	public final TransferBuffer nextCopy() {
		
		return new WrapBuffer(this.bytes, this.offset, this.length);
	}

	@Override
	public byte[] nextDirectArray() {
		
		if (this.offset == 0 && this.length == this.bytes.length) {
			return this.bytes;
		}
		final int length = this.length;
		final byte[] result = new byte[length];
		System.arraycopy(this.bytes, this.offset, result, 0, length);
		return result;
	}
	
	@Override
	public InputStream nextInputStream() {
		
		return new WrapInputStream(this.bytes, this.offset, this.length);
	}
	
	@Override
	public ByteBuffer nextNioBuffer() {
		
		if (this.offset == 0 && this.length == this.bytes.length) {
			return ByteBuffer.wrap(this.bytes);
		}
		return ByteBuffer.wrap(this.bytes, this.offset, this.length);
	}

	@Override
	public final InputStreamReader nextReaderUtf8() {
		
		return new InputStreamReader(this.nextInputStream(), Engine.CHARSET_UTF8);
	}

	@Override
	public TransferCopier slice(final long start, final long count) throws ConcurrentModificationException {
		
		if (start == 0 && count == this.length) {
			return this;
		}
		if (start >= this.length) {
			return TransferCopier.NUL_COPIER;
		}
		return new WrapCopier(
				this.bytes, //
				(int) (this.offset + start),
				Math.min((int) count, (int) (this.length - start)));
	}

	@Override
	public String toString() {
		
		return this.toString(Engine.CHARSET_DEFAULT);
	}

	@Override
	public String toString(final Charset charset) {
		
		return 0 == this.length
			? ""
			: new String(this.bytes, this.offset, this.length, charset);
	}

	@Override
	public String toString(final String encoding) throws UnsupportedEncodingException {
		
		return 0 == this.length
			? ""
			: new String(this.bytes, this.offset, this.length, encoding);
	}

	@Override
	public String toStringUtf8() {
		
		return this.toString(Engine.CHARSET_UTF8);
	}

	@Override
	public MessageDigest updateMessageDigest(final MessageDigest digest) {
		
		digest.update(this.bytes, this.offset, this.length);
		return digest;
	}
}
