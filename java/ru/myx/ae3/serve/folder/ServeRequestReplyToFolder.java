package ru.myx.ae3.serve.folder;

import java.io.File;
import java.io.IOException;

import java.util.function.Function;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.serve.AbstractServeRequestMutable;
import ru.myx.ae3.serve.file.ServeRequestReplyToFile;

/**
 * @author myx
 *
 */
public class ServeRequestReplyToFolder extends AbstractServeRequestMutable<ServeRequestReplyToFolder> implements Function<ReplyAnswer, Boolean> {
	
	/**
	 *
	 */
	protected final File folder;
	
	/**
	 *
	 */
	protected final File file;
	
	ServeRequestReplyToFolder(final File folder, final String name) {
		super("FOLDER", "GET", null);
		this.folder = folder;
		this.file = new File(folder, name);
		this.setResponseTarget(this);
	}
	
	@Override
	public Boolean apply(final ReplyAnswer reply) {
		
		try {
			ServeRequestReplyToFile.writeReplyToFile(reply, this.file);
		} catch (final IOException e) {
			throw new Transfer.TransferOperationException("writing: " + this.file.getAbsolutePath(), e);
		}
		return Boolean.TRUE;
	}
	
	@Override
	public String toString() {
		
		return this.getClass().getSimpleName() + ": folder=" + this.folder + ", file=" + this.file;
	}
	
}
