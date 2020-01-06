/**
 *
 */
package ru.myx.ae3.eval.collection;

import ru.myx.ae3.exec.ProgramPart;

final class RenderCacheEntry {

	final ProgramPart renderer;
	
	final long modified;
	
	/** @param renderer
	 * @param modified */
	RenderCacheEntry(final ProgramPart renderer, final long modified) {

		// System.out.println(" >>> >>>> W: renderer: " + renderer + ", modified: " + modified);
		this.renderer = renderer;
		this.modified = modified;
	}
	
	@Override
	public final String toString() {

		return String.valueOf(this.renderer);
	}
}
