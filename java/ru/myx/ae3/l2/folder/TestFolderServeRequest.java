package ru.myx.ae3.l2.folder;

import java.io.IOException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.answer.ReplyAnswer;
import ru.myx.ae3.base.BaseObject;

/**
 * @author myx
 *
 */
public class TestFolderServeRequest extends TempFolderServeRequest {
	
	/**
	 * @param name
	 * @throws IOException
	 */
	public TestFolderServeRequest(final String name) throws IOException {
		super(name);
	}

	/**
	 * @param prefix
	 * @param suffix
	 * @param name
	 * @throws IOException
	 */
	public TestFolderServeRequest(final String prefix, final String suffix, final String name) throws IOException {
		super(prefix, suffix, name);
	}

	@Override
	public Boolean apply(final ReplyAnswer reply) {
		
		// TODO Auto-generated method stub
		Engine.createProcess(this.file.getName(), BaseObject.UNDEFINED, this.folder);
		return Boolean.TRUE;
	}

	@Override
	public String toString() {
		
		return "TestFolderServeRequest: folder=" + this.folder + ", file=" + this.file;
	}
}
