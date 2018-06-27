/**
 * Created on 17.02.2003
 * 
 * myx - barachta */
package ru.myx.file_watcher;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;

import ru.myx.ae3.act.Act;
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
final class RecordInputStream extends RecordEx {
	RecordInputStream(final File inbox, final FileFilter filter, final Function<FileInputStream, Object> target) {
		super( inbox, filter, target );
	}
	
	@Override
	protected void handle(final File file, final File done) throws Exception {
		this.busy = true;
		final FileInputStream input = new FileInputStream( file ) {
			private boolean	d	= false;
			
			@Override
			public void close() throws IOException {
				super.close();
				if (!this.d) {
					this.d = true;
					file.renameTo( done );
					RecordInputStream.this.busy = false;
				}
			}
			
			@Override
			protected void finalize() throws IOException {
				super.close();
				if (!this.d) {
					this.d = true;
					file.renameTo( done );
					RecordInputStream.this.busy = false;
				}
				super.finalize();
			}
		};
		final Function<FileInputStream, Object> target = Convert.Any.toAny( this.target );
		Act.launch( this.context, target, input );
	}
}
