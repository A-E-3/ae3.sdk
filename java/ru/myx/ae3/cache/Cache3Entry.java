/*
 * Created on 06.04.2006
 */
package ru.myx.ae3.cache;

import ru.myx.ae3.Engine;

abstract class Cache3Entry {
	private final String	key;
	
	private long			accessed;
	
	Cache3Entry(final String key) {
		this.key = key;
		this.accessed = Engine.fastTime();
	}
	
	final long getAccessed() {
		return this.accessed;
	}
	
	final String getKey() {
		return this.key;
	}
	
	final void setAccessed() {
		this.accessed = Engine.fastTime();
	}
}
