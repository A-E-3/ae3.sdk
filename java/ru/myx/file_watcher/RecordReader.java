/**
 * Created on 17.02.2003
 *
 * myx - barachta */
package ru.myx.file_watcher;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.function.Function;

import ru.myx.ae3.act.Act;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.help.Convert;

/** @author myx
 *
 *         myx - barachta "typecomment": Window>Preferences>Java>Templates. To enable and disable
 *         the creation of type comments go to Window>Preferences>Java>Code Generation. */
final class RecordReader extends RecordEx {
	
	RecordReader(final File inbox, final FileFilter filter, final Function<FileReader, Object> target) {
		
		super(inbox, filter, target);
	}

	@Override
	protected void handle(final File file, final File done) throws Exception {
		
		this.busy = true;
		final FileReader reader = new FileReader(file) {
			
			private boolean d = false;

			@Override
			public void close() throws IOException {
				
				super.close();
				if (!this.d) {
					this.d = true;
					file.renameTo(done);
					RecordReader.this.busy = false;
				}
			}
		};
		Transfer.deferClose(reader);
		final Function<FileReader, Object> target = Convert.Any.toAny(this.target);
		Act.launch(this.context, target, reader);
	}
}
