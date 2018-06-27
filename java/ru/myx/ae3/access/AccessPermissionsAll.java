/*
 * Created on 10.05.2006
 */
package ru.myx.ae3.access;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;

final class AccessPermissionsAll extends AbstractSet<String> {
	private static final Set<String>	EMPTY_SET	= Collections.emptySet();
	
	@Override
	public boolean contains(final Object arg) {
		return true;
	}
	
	@Override
	public boolean containsAll(final Collection<?> arg) {
		return true;
	}
	
	@Override
	public boolean isEmpty() {
		return false;
	}
	
	@Override
	public Iterator<String> iterator() {
		return AccessPermissionsAll.EMPTY_SET.iterator();
	}
	
	@Override
	public int size() {
		return 0;
	}
}
