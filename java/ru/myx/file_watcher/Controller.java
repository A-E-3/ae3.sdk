/**
 * 
 */
package ru.myx.file_watcher;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import ru.myx.ae3.act.Act;

final class Controller implements Runnable {
	private static boolean				active	= false;
	
	private static final List<Record>	RECORDS	= new LinkedList<>();
	
	private static final Set<Record>	ADDED	= new HashSet<>();
	
	synchronized void add(final Record record) {
		Controller.ADDED.add( record );
		if (!Controller.active) {
			Controller.active = true;
			Act.later( null, this, 10000 );
		}
	}
	
	@Override
	public void run() {
		try {
			synchronized (this) {
				if (!Controller.ADDED.isEmpty()) {
					Controller.RECORDS.addAll( Controller.ADDED );
					Controller.ADDED.clear();
				}
			}
			for (final Record record : Controller.RECORDS) {
				try {
					record.check();
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		} catch (final Throwable t) {
			t.printStackTrace();
		} finally {
			Act.later( null, this, 10000 );
		}
	}
}
