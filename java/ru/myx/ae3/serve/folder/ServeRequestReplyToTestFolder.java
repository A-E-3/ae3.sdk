package ru.myx.ae3.serve.folder;

import java.io.IOException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.answer.ReplyAnswer;

/**
 * @author myx
 *
 */
public class ServeRequestReplyToTestFolder extends ServeRequestReplyToTempFolder {
	
	/**
	 * @param name
	 * @throws IOException
	 */
	public ServeRequestReplyToTestFolder(final String name) throws IOException {
		super(name);
	}

	/**
	 * @param prefix
	 * @param suffix
	 * @param name
	 * @throws IOException
	 */
	public ServeRequestReplyToTestFolder(final String prefix, final String suffix, final String name) throws IOException {
		super(prefix, suffix, name);
	}

	@Override
	public Boolean apply(final ReplyAnswer reply) {
		
		final Boolean result = super.apply(reply);
		if (result != Boolean.TRUE) {
			return result;
		}
		Engine.createProcess(this.file.getName(), null, this.folder);
		return Boolean.TRUE;
	}

	@Override
	public String toString() {
		
		return "TestFolderServeRequest: folder=" + this.folder + ", file=" + this.file;
	}
}
