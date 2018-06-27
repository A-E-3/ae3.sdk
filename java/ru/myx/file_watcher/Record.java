/**
 * 
 */
package ru.myx.file_watcher;

import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;

abstract class Record {
	final ExecProcess	context	= Exec.currentProcess();
	
	/**
	 * @throws Exception
	 */
	abstract void check() throws Exception;
}
