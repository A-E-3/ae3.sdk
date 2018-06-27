/**
 * 
 */
package ru.myx.sapi;

import java.util.Comparator;

import ru.myx.ae3.control.ControlBasic;

final class ComparatorBasicStringCsTitleDesc implements Comparator<ControlBasic<?>> {
	@Override
	public final int compare(final ControlBasic<?> arg0, final ControlBasic<?> arg1) {
		return arg1.getTitle().compareTo( arg0.getTitle() );
	}
}
