/**
 * 
 */
package ru.myx.renderer.ecma;

import java.util.LinkedList;

/**
 * @author myx
 * 
 */
final class ListStatements extends LinkedList<TokenStatement> {
	
	/**
     * 
     */
	private static final long	serialVersionUID	= -8246073961479673626L;
	
	ListStatements(final TokenStatement statement1, final TokenStatement statement2) {
		this.add( statement1 );
		this.add( statement2 );
	}
}
