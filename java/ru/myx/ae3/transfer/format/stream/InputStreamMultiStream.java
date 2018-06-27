/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.transfer.format.stream;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;

/**
 * @author myx
 * 
 */
public abstract class InputStreamMultiStream extends FilterInputStream implements Enumeration<InputStream> {
	/**
	 * 
	 */
	protected InputStreamMultiStream() {
		super( null );
	}
	
	@Override
	public final boolean hasMoreElements() {
		try {
			return this.hasMoreStreams();
		} catch (final IOException e) {
			return false;
		}
	}
	
	abstract boolean hasMoreStreams() throws IOException;
	
	@Override
	public final boolean markSupported() {
		return false;
	}
	
	@Override
	public final InputStream nextElement() {
		try {
			this.nextStream();
			return this.in;
		} catch (final IOException e) {
			return null;
		}
	}
	
	abstract void nextStream() throws IOException;
}
