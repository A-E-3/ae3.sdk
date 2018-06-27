/**
 * Created on 17.02.2003
 * 
 * myx - barachta */
package ru.myx.file_watcher;

import java.io.File;
import java.io.FileFilter;

import ru.myx.ae3.Engine;
import java.util.function.Function;

/**
 * @author myx
 * 
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
final class RecordFile extends Record {
	private final File						waiter;
	
	private final File						inbox1;
	
	private final File						inbox2;
	
	private final FileFilter				filter;
	
	private final File						bad;
	
	private final File						processed;
	
	private final Function<File, Object>	target;
	
	RecordFile(final File inbox, final FileFilter filter, final Function<File, Object> target) {
		this.waiter = new File( inbox, "delete.when.ready" );
		this.inbox1 = inbox;
		this.inbox2 = new File( inbox, "inbox" );
		this.filter = filter;
		this.bad = new File( inbox, "bad" );
		this.processed = new File( inbox, "processed" );
		this.target = target;
		this.inbox2.mkdirs();
		this.bad.mkdirs();
		this.processed.mkdirs();
	}
	
	@Override
	void check() throws Exception {
		{
			final File[] files = this.inbox2.listFiles( this.filter );
			if (files != null && files.length > 0) {
				for (final File file : files) {
					if (this.process( file )) {
						return;
					}
				}
			}
		}
		if (this.waiter.exists()) {
			this.waiter.setLastModified( Engine.fastTime() );
			return;
		}
		try {
			final File[] files = this.inbox1.listFiles( this.filter );
			if (files != null && files.length > 0) {
				for (final File file : files) {
					file.renameTo( new File( this.inbox2, file.getName() ) );
				}
			}
		} finally {
			this.waiter.createNewFile();
		}
	}
	
	boolean process(final File file) {
		final String name = file.getName();
		final File target = new File( this.processed, name );
		if (target.exists()) {
			final File bad = new File( this.bad, name + " - " + Engine.createGuid() );
			file.renameTo( bad );
		} else {
			file.renameTo( target );
			try {
				this.target.apply( target );
			} catch (final Throwable t) {
				t.printStackTrace();
			}
		}
		return true;
	}
}
