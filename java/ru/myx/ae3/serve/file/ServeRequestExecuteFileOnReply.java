package ru.myx.ae3.serve.file;

import java.io.IOException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.answer.ReplyAnswer;

/**
 * Temporary document which is then opened using user's GUI associated
 * application.
 *
 *
 * @author myx
 *
 */
public class ServeRequestExecuteFileOnReply extends ServeRequestReplyToTempFile {
	
	/**
	 * @param name
	 * @throws IOException
	 */
	public ServeRequestExecuteFileOnReply(final String name) throws IOException {
		super(name);
	}

	/**
	 * @param prefix
	 * @param suffix
	 * @throws IOException
	 */
	public ServeRequestExecuteFileOnReply(final String prefix, final String suffix) throws IOException {
		super(prefix, suffix);
	}

	@Override
	public Boolean apply(final ReplyAnswer reply) {
		
		final Boolean result = super.apply(reply);
		if (result != Boolean.TRUE) {
			return result;
		}
		Engine.createProcess(this.file.getName(), null, this.file.getParentFile());
		return Boolean.TRUE;
	}

	@Override
	public String toString() {
		
		return "ExecuteFileServeRequest: file=" + this.file;
	}
}
