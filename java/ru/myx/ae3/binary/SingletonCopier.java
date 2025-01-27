package ru.myx.ae3.binary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ConcurrentModificationException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObjectNoOwnProperties;
import ru.myx.ae3.common.Describable;
import ru.myx.io.SingletonInputStream;

/** @author myx */
public class SingletonCopier implements BaseObjectNoOwnProperties, TransferCopier, Describable {
	
	private final byte oneByte;
	
	/** @param oneByte */
	public SingletonCopier(final byte oneByte) {
		
		this.oneByte = oneByte;
	}
	
	@Override
	public String baseDescribe() {
		
		return "[" + this.getClass().getSimpleName() + " size=1]";
	}
	
	@Override
	public SingletonCopier baseValue() {
		
		return this;
	}
	
	@Override
	public int compareTo(final TransferCopier o) {
		
		if (o == null) {
			return 1;
		}
		if (o.length() == 0) {
			return 1;
		}
		try {
			return Transfer.compareStreams(this.nextInputStream(), o.nextInputStream());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public int copy(final long start, final byte[] target, final int offset, final int count) throws ConcurrentModificationException {
		
		if (start == 0 && count > 0) {
			target[offset] = this.oneByte;
			return 1;
		}
		return 0;
	}
	
	@Override
	public boolean equals(final Object obj) {
		
		if (obj == null || !(obj instanceof TransferCopier) || ((TransferCopier) obj).length() != 1) {
			return false;
		}
		final TransferBuffer buffer = ((TransferCopier) obj).nextCopy();
		if ((this.oneByte & 0xFF) != buffer.next()) {
			return false;
		}
		return true;
	}
	
	@Override
	public MessageDigest getMessageDigest() {
		
		final MessageDigest digest = Engine.getMessageDigestInstance();
		digest.update(this.oneByte);
		return digest;
	}
	
	@Override
	public int hashCode() {
		
		int result = 1;
		result = 31 * result + (this.oneByte & 0xFF);
		return result;
	}
	
	@Override
	public long length() throws ConcurrentModificationException {
		
		return 1;
	}
	
	@Override
	public TransferBuffer nextCopy() throws ConcurrentModificationException {
		
		return new SingletonBuffer(this.oneByte);
	}
	
	@Override
	public byte[] nextDirectArray() throws ConcurrentModificationException {
		
		return new byte[]{
				this.oneByte
		};
	}
	
	@Override
	public InputStream nextInputStream() throws IOException, ConcurrentModificationException {
		
		return new SingletonInputStream(this.oneByte);
	}
	
	@Override
	public Reader nextReaderUtf8() throws IOException, ConcurrentModificationException {
		
		return new InputStreamReader(this.nextInputStream(), StandardCharsets.UTF_8);
	}
	
	@Override
	public TransferCopier slice(final long start, final long count) throws ConcurrentModificationException {
		
		if (start == 0 && count == 1) {
			return this;
		}
		return TransferCopier.NUL_COPIER;
	}
	
	@Override
	public String toString() {
		
		return this.toString(Charset.defaultCharset());
	}
	
	@Override
	public String toString(final Charset charset) {
		
		return new String(new byte[]{
				this.oneByte
		}, charset);
	}
	
	@Override
	public String toString(final String encoding) throws UnsupportedEncodingException {
		
		return new String(new byte[]{
				this.oneByte
		}, encoding);
	}
	
	@Override
	public MessageDigest updateMessageDigest(final MessageDigest digest) throws ConcurrentModificationException {
		
		digest.update(this.oneByte);
		return digest;
	}
}
