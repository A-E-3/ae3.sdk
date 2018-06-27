package ru.myx.ae3.serve.file;

import java.io.File;
import java.io.IOException;

/**
 * @author myx
 * 
 */
public class ServeRequestReplyToTempFile extends ServeRequestReplyToFile {
	/**
	 * @param name
	 * @throws IOException
	 */
	public ServeRequestReplyToTempFile(final String name) throws IOException {
		super( File.createTempFile( "iface-", '-' + name ) );
	}
	
	/**
	 * @param prefix
	 * @param suffix
	 * @throws IOException
	 */
	public ServeRequestReplyToTempFile(final String prefix, final String suffix) throws IOException {
		super( File.createTempFile( prefix, suffix ) );
	}
	
	@Override
	public String toString() {
		return "TempFileServeRequest: file=" + this.file;
	}
}
