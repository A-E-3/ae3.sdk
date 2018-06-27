/**
 * 
 */
package ru.myx.sapi;

import java.util.Comparator;

import ru.myx.ae3.control.ControlBasic;
import ru.myx.ae3.help.Convert;

final class ComparatorBasicDateTitleAsc implements Comparator<ControlBasic<?>> {
	@Override
	public final int compare(final ControlBasic<?> arg0, final ControlBasic<?> arg1) {
		final long d0 = Convert.Any.toLong( arg0.getTitle(), 0L );
		final long d1 = Convert.Any.toLong( arg1.getTitle(), 0L );
		return d0 > d1
				? 1
				: d0 < d1
						? -1
						: 0;
	}
}
