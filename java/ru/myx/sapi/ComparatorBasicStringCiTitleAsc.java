/**
 * 
 */
package ru.myx.sapi;

import java.util.Comparator;

import ru.myx.ae3.control.ControlBasic;

final class ComparatorBasicStringCiTitleAsc implements Comparator<ControlBasic<?>> {
	@Override
	public final int compare(final ControlBasic<?> arg0, final ControlBasic<?> arg1) {
		return arg0.getTitle().compareToIgnoreCase( arg1.getTitle() );
	}
}
