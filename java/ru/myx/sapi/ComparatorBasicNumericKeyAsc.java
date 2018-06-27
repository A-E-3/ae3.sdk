/**
 * 
 */
package ru.myx.sapi;

import java.util.Comparator;

import ru.myx.ae3.control.ControlBasic;
import ru.myx.ae3.help.Convert;

final class ComparatorBasicNumericKeyAsc implements Comparator<ControlBasic<?>> {
	@Override
	public final int compare(final ControlBasic<?> arg0, final ControlBasic<?> arg1) {
		final double d0 = Convert.Any.toDouble( arg0.getKey(), 0d );
		final double d1 = Convert.Any.toDouble( arg1.getKey(), 0d );
		return d0 > d1
				? 1
				: d0 < d1
						? -1
						: 0;
	}
}
