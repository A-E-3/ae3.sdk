/*
 * Created on 11.04.2006
 */
package ru.myx.ae3.exec.fn;

import ru.myx.ae3.base.BaseObject;

final class RecordBufferedInstance {
	
	final BaseObject map;
	
	final BaseObject object;
	
	RecordBufferedInstance(final BaseObject object, final BaseObject map) {
		this.object = object;
		this.map = map;
	}
}
