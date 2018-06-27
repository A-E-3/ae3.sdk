/**
 * 
 */
package ru.myx.sapi;

import java.util.Comparator;

import ru.myx.ae3.control.ControlBasic;
import ru.myx.ae3.help.Convert;

final class ComparatorBasicDateDesc implements Comparator<ControlBasic<?>> {
	private final String	fieldName;
	
	ComparatorBasicDateDesc(final String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public final int compare(final ControlBasic<?> arg0, final ControlBasic<?> arg1) {
		final long d0 = Convert.MapEntry.toLong( arg1.getData(), this.fieldName, 0L );
		final long d1 = Convert.MapEntry.toLong( arg0.getData(), this.fieldName, 0L );
		return d0 > d1
				? 1
				: d0 < d1
						? -1
						: 0;
	}
}
