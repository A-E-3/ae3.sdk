package ru.myx.ae3.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.common.Describable;
import ru.myx.ae3.help.Format;
import ru.myx.io.SingletonInputStream;

/**
 * @author myx
 * 
 */
final class SingletonBuffer implements BaseObjectNoOwnProperties, BaseTransferBuffer, Describable {
	
	
	private int theByte;
	
	SingletonBuffer(final byte oneByte) {
		
		this.theByte = oneByte & 0xFF;
	}
	
	@Override
	public String baseDescribe() {
		
		
		return "[SingletonBuffer size=" + Format.Compact.toBytes(this.remaining()) + "]";
	}
	
	@Override
	public SingletonBuffer baseValue() {
		
		
		return this;
	}
	
	@Override
	public final void destroy() {
		
		
		// ignore
	}
	
	@Override
	public MessageDigest getMessageDigest() {
		
		
		final MessageDigest digest = Engine.getMessageDigestInstance();
		if (this.theByte != -1) {
			digest.update((byte) this.theByte);
		}
		return digest;
	}
	
	@Override
	public boolean hasRemaining() {
		
		
		return this.theByte != -1;
	}
	
	@Override
	public boolean isDirectAbsolutely() {
		
		
		return false;
	}
	
	@Override
	public boolean isSequence() {
		
		
		return false;
	}
	
	@Override
	public int next() {
		
		
		final int theByte = this.theByte;
		if (theByte != -1) {
			this.theByte = -1;
		}
		return theByte;
	}
	
	@Override
	public int next(final byte[] buffer, final int offset, final int length) {
		
		
		final int theByte = this.theByte;
		if (theByte == -1) {
			return -1;
		}
		if (length == 0) {
			return 0;
		}
		buffer[offset] = (byte) theByte;
		this.theByte = -1;
		return 1;
	}
	
	@Override
	public TransferBuffer nextSequenceBuffer() {
		
		
		throw new UnsupportedOperationException("Not a sequence!");
	}
	
	@Override
	public long remaining() {
		
		
		return this.theByte == -1
			? 0
			: 1;
	}
	
	@Override
	public TransferCopier toBinary() {
		
		
		final int theByte = this.theByte;
		if (theByte == -1) {
			return TransferCopier.NUL_COPIER;
		}
		/**
		 * TODO: could be pre-enumerated in 256 element array
		 */
		return new SingletonCopier((byte) theByte);
	}
	
	@Override
	public byte[] toDirectArray() {
		
		
		final int theByte = this.theByte;
		return theByte == -1
			? Transfer.EMPTY_BYTE_ARRAY
			: new byte[]{
					(byte) theByte
			};
	}
	
	@Override
	public InputStream toInputStream() {
		
		
		final int theByte = this.theByte;
		if (theByte == -1) {
			return Transfer.EMPTY_STREAM;
		}
		return new SingletonInputStream((byte) theByte);
	}
	
	@Override
	public TransferBuffer toNioBuffer(final ByteBuffer target) throws IOException {
		
		
		final int remaining = this.theByte == -1
			? 0
			: 1;
		if (remaining <= 0) {
			return null;
		}
		final int writable = target.remaining();
		if (writable <= 0) {
			return this;
		}
		target.put((byte) this.theByte);
		this.theByte = -1;
		return null;
	}
	
	@Override
	public Reader toReaderUtf8() throws IOException {
		
		
		if (this.theByte == -1) {
			return Transfer.EMPTY_READER;
		}
		return new InputStreamReader(this.toInputStream(), Engine.CHARSET_UTF8);
	}
	
	@Override
	public final String toString() {
		
		
		if (this.theByte == -1) {
			return "";
		}
		return this.toString(Engine.CHARSET_DEFAULT);
	}
	
	@Override
	public String toString(final Charset charset) {
		
		
		final int theByte = this.theByte;
		if (theByte == -1) {
			return "";
		}
		this.theByte = -1;
		return new String(new byte[]{
				(byte) theByte
		}, charset);
	}
	
	@Override
	public String toString(final String encoding) throws UnsupportedEncodingException {
		
		
		final int theByte = this.theByte;
		if (theByte == -1) {
			return "";
		}
		this.theByte = -1;
		return new String(new byte[]{
				(byte) theByte
		}, encoding);
	}
	
	@Override
	public TransferBuffer toSubBuffer(final long start, final long end) throws IllegalArgumentException {
		
		
		final int remaining = this.theByte == -1
			? 0
			: 1;
		if (start < 0 || start > end || end > remaining) {
			throw new IllegalArgumentException("Indexes are out of bounds: start=" + start + ", end=" + end + ", length=" + remaining);
		}
		if (remaining == 0) {
			return TransferBuffer.NUL_BUFFER;
		}
		if (start < end) {
			return this;
		}
		this.theByte = -1;
		return TransferBuffer.NUL_BUFFER;
	}
	
	@Override
	public MessageDigest updateMessageDigest(final MessageDigest digest) {
		
		
		if (this.theByte != -1) {
			digest.update((byte) this.theByte);
		}
		return digest;
	}
	//
}
