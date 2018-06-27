package ru.myx.ae3.l2.folder;

import java.io.File;
import java.io.IOException;

/**
 * @author myx
 * 
 */
public class TempFolderServeRequest extends FolderServeRequest {
	private static final File createTempFolder(final String prefix, final String suffix) throws IOException {
		final File folder = File.createTempFile( prefix, suffix );
		folder.delete();
		folder.mkdirs();
		return folder;
	}
	
	/**
	 * @param name
	 * @throws IOException
	 */
	public TempFolderServeRequest(final String name) throws IOException {
		super( TempFolderServeRequest.createTempFolder( "iface-", ".tmp" ), name );
	}
	
	/**
	 * @param prefix
	 * @param suffix
	 * @param name
	 * @throws IOException
	 */
	public TempFolderServeRequest(final String prefix, final String suffix, final String name) throws IOException {
		super( TempFolderServeRequest.createTempFolder( prefix, suffix ), name );
	}
	
	@Override
	public String toString() {
		return "TempFolderServeRequest: folder=" + this.folder + ", file=" + this.file;
	}
}
