/**
 * Created on 14.02.2003
 *
 * myx - barachta */
package ru.myx.file_watcher;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.function.Function;

import ru.myx.ae3.Engine;

/** @author myx
 *
 *         myx - barachta "typecomment": Window>Preferences>Java>Templates. To enable and disable
 *         the creation of type comments go to Window>Preferences>Java>Code Generation. */
public final class FileWatcher {
	
	private static final FileFilter DEFAULT_FILTER = File::isFile;

	private static final Controller CONTROLLER = new Controller();

	private static final File FOLDER_IMPORT = new File(Engine.PATH_PRIVATE, "import");
	static {
		System.out.println("FILE_WATCHER: default import folder is: " + FileWatcher.FOLDER_IMPORT.getAbsolutePath());
	}

	/** Found files will be passed as an File.
	 *
	 * @param folder
	 * @param target
	 * @return boolean */
	public static final synchronized boolean registerFile(final File folder, final Function<File, Object> target) {
		
		FileWatcher.CONTROLLER.add(new RecordFile(folder, FileWatcher.DEFAULT_FILTER, target));
		return true;
	}

	/** Found files will be passed as an File.
	 *
	 * @param appName
	 * @param target
	 * @return boolean */
	public static final synchronized boolean registerFile(final String appName, final Function<File, Object> target) {
		
		FileWatcher.CONTROLLER.add(
				new RecordFile(
						new File(FileWatcher.FOLDER_IMPORT, appName.toLowerCase().replace('\\', '_').replace('/', '_').replace(':', '_')),
						FileWatcher.DEFAULT_FILTER,
						target));
		return true;
	}

	/** Found files will be passed as an FileInputStream.
	 *
	 * @param folder
	 * @param target
	 * @return boolean */
	public static final boolean registerInputStream(final File folder, final Function<FileInputStream, Object> target) {
		
		FileWatcher.CONTROLLER.add(new RecordInputStream(folder, FileWatcher.DEFAULT_FILTER, target));
		return true;
	}

	/** Found files will be passed as an FileInputStream.
	 *
	 * @param appName
	 * @param target
	 * @return boolean */
	public static final boolean registerInputStream(final String appName, final Function<FileInputStream, Object> target) {
		
		FileWatcher.CONTROLLER.add(
				new RecordInputStream(
						new File(FileWatcher.FOLDER_IMPORT, appName.toLowerCase().replace('\\', '_').replace('/', '_').replace(':', '_')),
						FileWatcher.DEFAULT_FILTER,
						target));
		return true;
	}

	/** Found files will be passed as an FileReader.
	 *
	 * @param folder
	 * @param target
	 * @return boolean */
	public static final boolean registerReader(final File folder, final Function<FileReader, Object> target) {
		
		FileWatcher.CONTROLLER.add(new RecordReader(folder, FileWatcher.DEFAULT_FILTER, target));
		return true;
	}

	/** Found files will be passed as an FileReader.
	 *
	 * @param appName
	 * @param target
	 * @return boolean */
	public static final boolean registerReader(final String appName, final Function<FileReader, Object> target) {
		
		FileWatcher.CONTROLLER.add(
				new RecordReader(
						new File(FileWatcher.FOLDER_IMPORT, appName.toLowerCase().replace('\\', '_').replace('/', '_').replace(':', '_')),
						FileWatcher.DEFAULT_FILTER,
						target));
		return true;
	}
}
