/*
 * Created on 25.04.2006
 */
package ru.myx.util;

import java.util.Comparator;

/**
 * @author myx
 * 
 */
public final class ComparatorFast implements Comparator<Object> {
	@Override
	public final int compare(final Object o1, final Object o2) {
		if (o1 == o2) {
			return 0;
		}
		if (o1.getClass() == o2.getClass()) {
			if (o1.equals( o2 )) {
				return 0;
			}
		} else //
		if (o1.getClass().isInstance( o2 )) {
			if (o1.equals( o2 )) {
				return 0;
			}
		} else //
		if (o2.getClass().isInstance( o1 )) {
			if (o2.equals( o1 )) {
				return 0;
			}
		}
		return System.identityHashCode( o2 ) - System.identityHashCode( o1 );
	}
}
