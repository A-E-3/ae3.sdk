/**
 * 
 */
package ru.myx.sapi;

import java.util.Comparator;

import ru.myx.ae3.control.ControlBasic;
import ru.myx.ae3.help.Convert;

final class ComparatorBasicNumericAsc implements Comparator<ControlBasic<?>> {
	private final String	fieldName;
	
	ComparatorBasicNumericAsc(final String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Override
	public final int compare(final ControlBasic<?> arg0, final ControlBasic<?> arg1) {
		final double d0 = Convert.MapEntry.toDouble( arg0.getData(), this.fieldName, 0d );
		final double d1 = Convert.MapEntry.toDouble( arg1.getData(), this.fieldName, 0d );
		return d0 > d1
				? 1
				: d0 < d1
						? -1
						: 0;
	}
}
