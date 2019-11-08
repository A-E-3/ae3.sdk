/**
 *
 */
package ru.myx.ae3.exec;

import ru.myx.ae3.status.StatusInfo;

/** @author myx */
public class ExecArgumentsFactory {
	
	/** @param data */
	public static final void statusFill(final StatusInfo data) {

		ExecArgumentsEmpty.statusFill(data);
		ExecArgumentsList1.statusFill(data);
		ExecArgumentsList2.statusFill(data);
		ExecArgumentsList3.statusFill(data);
		ExecArgumentsList4.statusFill(data);
		ExecArgumentsListXWrapped.statusFill(data);
		ExecArgumentsMap1.statusFill(data);
		ExecArgumentsMapX.statusFill(data);
	}
}
