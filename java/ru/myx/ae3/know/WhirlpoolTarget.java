package ru.myx.ae3.know;

import java.nio.ByteBuffer;

import java.util.function.Function;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferTarget;
import ru.myx.ae3.exec.ExecProcess;

class WhirlpoolTarget implements TransferTarget {
	private final WhirlpoolDigest	digest;
	
	private long					length	= 0;
	
	WhirlpoolTarget() {
		this.digest = new WhirlpoolDigest();
	}
	
	@Override
	public void abort(String reason) {
		this.digest.engineReset();
	}
	
	@Override
	public boolean absorb(final int i) {
		this.digest.update( (byte) i );
		this.length++;
		return true;
	}
	
	@Override
	public boolean absorbArray(final byte[] array, final int off, final int len) {
		this.digest.update( array, off, len );
		this.length += len;
		return true;
	}
	
	@Override
	public boolean absorbBuffer(final TransferBuffer buffer) {
		this.length += buffer.remaining();
		buffer.updateMessageDigest( this.digest );
		return true;
	}
	
	@Override
	public boolean absorbNio(final ByteBuffer buffer) {
		this.length += buffer.remaining();
		this.digest.update( buffer );
		return true;
	}
	
	@Override
	public void close() {
		//
	}
	
	@Override
	public <A, R> boolean enqueueAction(final ExecProcess ctx, final Function<A, R> function, final A argument) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void force() {
		// TODO Auto-generated method stub
	}
	
	protected WhirlpoolDigest getDigest() {
		return this.digest;
	}
	
	protected long getLength() {
		return this.length;
	}
}
