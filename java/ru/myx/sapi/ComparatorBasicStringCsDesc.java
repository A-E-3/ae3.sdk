/**
 * 
 */
package ru.myx.sapi;

import java.util.Comparator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.control.ControlBasic;

final class ComparatorBasicStringCsDesc implements Comparator<ControlBasic<?>> {
	private final String	fieldName;
	
	ComparatorBasicStringCsDesc(final String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public final int compare(final ControlBasic<?> arg0, final ControlBasic<?> arg1) {
		final String s0 = Base.getString( arg0.getData(), this.fieldName, "" ).trim();
		final String s1 = Base.getString( arg1.getData(), this.fieldName, "" ).trim();
		return s1.compareTo( s0 );
	}
}
