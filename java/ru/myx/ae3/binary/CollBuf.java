/*
 * Created on 19.05.2005
 */
package ru.myx.ae3.binary;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;
import java.util.function.Function;
import ru.myx.ae3.exec.ExecProcess;

abstract class CollBuf implements TransferTarget, TransferBuffer {
	@Override
	public void abort(String reason) {
		// empty
	}
	
	@Override
	public boolean absorb(final int i) {
		try {
			this.write( i );
			return true;
		} catch (final IOException e) {
			return false;
		}
	}
	
	@Override
	public boolean absorbArray(final byte[] array, final int off, final int len) {
		try {
			this.write( array, off, len );
			return true;
		} catch (final IOException e) {
			return false;
		}
	}
	
	@Override
	public boolean absorbBuffer(final TransferBuffer buffer) {
		final byte[] bytes = buffer.toDirectArray();
		return this.absorbArray( bytes, 0, bytes.length );
	}
	
	@Override
	public boolean absorbNio(final ByteBuffer buffer) {
		try {
			this.write( buffer );
			return true;
		} catch (final IOException e) {
			return false;
		}
	}
	
	@Override
	public abstract void close();
	
	@Override
	public <A, R> boolean enqueueAction(final ExecProcess process, final Function<A, R> function, final A argument) {
		Act.launch( process, function, argument );
		return true;
	}
	
	@Override
	public void force() {
		// empty
	}
	
	abstract void queued();
	
	abstract void reset();
	
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
	
	abstract void write(final byte[] buff, final int off, final int len) throws IOException;
	
	abstract void write(final ByteBuffer buffer) throws IOException;
	
	abstract void write(final int i) throws IOException;
}
