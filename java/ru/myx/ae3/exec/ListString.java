/**
 *
 */

package ru.myx.ae3.exec;

import java.util.ArrayList;

import ru.myx.ae3.help.Text;

/** @author myx */
final class ListString extends ArrayList<String> {

	/**
	 *
	 */
	private static final long serialVersionUID = 2886999588609714442L;

	ListString(final String e1, final String e2) {

		this.add(e1);
		this.add(e2);
	}

	@Override
	public final String toString() {

		return String.join("\n", (Iterable<? extends CharSequence>) this);
	}
}
