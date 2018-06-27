/**
 * Created on 14.02.2003
 * 
 * myx - barachta */
package ru.myx.file_watcher;

import java.io.File;
import java.io.FileFilter;

import ru.myx.ae3.Engine;
import java.util.function.Function;
import ru.myx.ae3.help.Convert;

/**
 * @author myx
 * 
 * myx - barachta 
 *         "typecomment": Window>Preferences>Java>Templates. To enable and
 *         disable the creation of type comments go to
 *         Window>Preferences>Java>Code Generation.
 */
class RecordEx extends Record {
	protected boolean				busy	= false;
	
	private final File				waiter;
	
	private final File				inbox1;
	
	private final File				inbox2;
	
	private final FileFilter		filter;
	
	private final File				bad;
	
	private final File				current;
	
	private final File				processed;
	
	final Function<?, Object>	target;
	
	RecordEx(final File inbox, final FileFilter filter, final Function<?, Object> target) {
		this.waiter = new File( inbox, "delete.when.ready" );
		this.inbox1 = inbox;
		this.inbox2 = new File( inbox, "inbox" );
		this.filter = filter;
		this.bad = new File( inbox, "bad" );
		this.current = new File( inbox, "current" );
		this.processed = new File( inbox, "processed" );
		this.target = target;
		this.inbox2.mkdirs();
		this.bad.mkdirs();
		this.current.mkdirs();
		this.processed.mkdirs();
	}
	
	@Override
	void check() throws Exception {
		if (!this.busy) {
			final File[] files = this.inbox2.listFiles( this.filter );
			if (files != null && files.length > 0) {
				for (final File element : files) {
					if (this.process( element )) {
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
				for (final File element : files) {
					element.renameTo( new File( this.inbox2, element.getName() ) );
				}
			}
		} finally {
			this.waiter.createNewFile();
		}
	}
	
	protected void handle(final File file, final File done) throws Throwable {
		final Function<File, Object> target = Convert.Any.toAny( this.target );
		target.apply( file );
		file.renameTo( done );
	}
	
	boolean process(final File file) {
		final String name = file.getName();
		final File done = new File( this.processed, name );
		if (done.exists()) {
			final File target = new File( this.bad, name + " - " + Engine.createGuid() );
			file.renameTo( target );
			return false;
		}
		if (this.busy) {
			return false;
		}
		{
			final File target = new File( this.current, name );
			file.renameTo( target );
			try {
				this.handle( target, done );
			} catch (final Throwable t) {
				t.printStackTrace();
			}
			return true;
		}
	}
}
