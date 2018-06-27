/*
 * Created on 10.03.2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ru.myx.ae3.eval;

import java.util.function.Function;

/**
 * @author myx
 * 
 */
final class FolderOneSource implements Function<String, String> {
	private final String	name;
	
	private final String	source;
	
	FolderOneSource(final String name, final String source) {
		this.name = name;
		this.source = source;
	}
	
	@Override
	public final String apply(final String name) {
		if (this.name.equals( name )) {
			return this.source;
		}
		return null;
	}
}
