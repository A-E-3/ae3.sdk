/**
 * Created on 17.11.2002
 *
 * myx - barachta */
package ru.myx.ae3.binary;

import java.io.File;
import java.io.IOException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.act.Act;

/** @author barachta
 *
 *         myx - barachta "typecomment": Window>Preferences>Java>Templates. To enable and disable
 *         the creation of type comments go to Window>Preferences>Java>Code Generation. */
final class Behavior {
	
	static {
		Engine.PATH_TEMP.mkdirs();
		Act.later(null, new BehaviorMaintenance(Engine.PATH_TEMP), 60_000L);
	}

	/** behavior is responsible for deleting temporary files after they become weakly reachable.
	 *
	 * @return file
	 * @throws IOException */
	static File tempFile() throws IOException {
		
		return File.createTempFile("cbf", ".bin", Engine.PATH_TEMP);
	}
}
