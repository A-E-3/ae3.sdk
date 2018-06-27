package ru.myx.ae3.serve.folder;

import java.io.File;
import java.io.IOException;

/**
 * @author myx
 * 
 */
public class ServeRequestReplyToTempFolder extends ServeRequestReplyToFolder {
	/**
	 * Creates temporary folder
	 * 
	 * <code>
		final File folder = File.createTempFile( prefix, suffix );
		folder.delete();
		folder.mkdirs();
		return folder;
	 *</code>
	 * 
	 * @param prefix
	 * @param suffix
	 * @return
	 * @throws IOException
	 */
	public static final File createTempFolder(final String prefix, final String suffix) throws IOException {
		final File folder = File.createTempFile( prefix, suffix );
		folder.delete();
		folder.mkdirs();
		return folder;
	}
	
	/**
	 * @param name
	 * @throws IOException
	 */
	public ServeRequestReplyToTempFolder(final String name) throws IOException {
		super( ServeRequestReplyToTempFolder.createTempFolder( "iface-", ".tmp" ), name );
	}
	
	/**
	 * @param prefix
	 * @param suffix
	 * @param name
	 * @throws IOException
	 */
	public ServeRequestReplyToTempFolder(final String prefix, final String suffix, final String name) throws IOException {
		super( ServeRequestReplyToTempFolder.createTempFolder( prefix, suffix ), name );
	}
	
	@Override
	public String toString() {
		return "TempFolderServeRequest: folder=" + this.folder + ", file=" + this.file;
	}
}
