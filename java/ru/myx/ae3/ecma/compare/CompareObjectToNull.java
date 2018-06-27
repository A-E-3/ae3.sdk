/**
 * 
 */
package ru.myx.ae3.ecma.compare;

import java.util.Comparator;

import ru.myx.ae3.common.Value;

/**
 * @author myx
 * 
 */
public final class CompareObjectToNull implements Comparator<Object> {
	
	/**
	 * 
	 */
	public static final CompareObjectToNull	INSTANCE	= new CompareObjectToNull();
	
	private CompareObjectToNull() {
		//
	}
	
	@Override
	public final int compare(final Object o2, final Object o1) {
		if (o2 == null) {
			return 0;
		}
		if (o2 instanceof Value<?>) {
			if (((Value<?>) o2).baseValue() == null) {
				return 0;
			}
		}
		return 1;
	}
	
}
