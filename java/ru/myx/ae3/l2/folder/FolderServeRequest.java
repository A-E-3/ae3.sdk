package ru.myx.ae3.l2.folder;

import java.io.File;

import java.util.function.Function;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.serve.AbstractServeRequestMutable;

/**
 * @author myx
 * 
 */
public class FolderServeRequest extends AbstractServeRequestMutable<FolderServeRequest> implements Function<ReplyAnswer, Boolean> {

	/**
	 *
	 */
	protected final File folder;

	/**
	 *
	 */
	protected final File file;

	FolderServeRequest(final File folder, final String name) {
		super("FOLDER", "GET", null);
		this.folder = folder;
		this.file = new File(folder, name);
		this.setResponseTarget(this);
	}

	@Override
	public Boolean apply(final ReplyAnswer reply) {

		// TODO Auto-generated method stub
		return Boolean.TRUE;
	}

	@Override
	public String toString() {

		return this.getClass().getSimpleName() + ": folder=" + this.folder + ", file=" + this.file;
	}

}
