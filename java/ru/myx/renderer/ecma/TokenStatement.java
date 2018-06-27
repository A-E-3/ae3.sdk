/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;


import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ProgramAssembly;

interface TokenStatement {
	/**
	 * @param statement
	 * @return boolean
	 */
	boolean addStatement(//
			final TokenStatement statement);
	
	/**
	 * @param identity
	 * @param line
	 * @return statement
	 */
	TokenStatement createStatement(//
			final String identity,
			final int line);
	
	/**
	 * @param level
	 * @param buffer
	 */
	void dump(//
			final int level,
			final StringBuilder buffer);
	
	/**
	 * @return string
	 */
	String getIdentity();
	
	/**
	 * @return string
	 */
	String getKeyword();
	
	/**
	 * @return integer
	 */
	int getLine();
	
	/**
	 * Only for ROOT or BLOCK statements. { ... }
	 * 
	 * @return boolean
	 */
	boolean isBlockStatement();
	
	/**
	 * @return
	 */
	boolean isEmpty();
	
	/**
	 * @return boolean
	 */
	boolean isIdentifierPossible();
	
	/**
	 * @return boolean
	 */
	boolean isIdentifierRequired();
	
	/**
	 * When keyword expects statement
	 * 
	 * @return boolean
	 */
	boolean isKeywordExpectStatement();
	
	/**
	 * @return boolean
	 */
	boolean isLabelStatement();
	
	/**
	 * @return boolean
	 */
	boolean isNewLineSemicolon();
	
	/**
	 * Any next statement should be considered as start of new statement
	 * 
	 * @return
	 */
	boolean isNextStatementFromScratch();
	
	/**
	 * @return boolean
	 */
	boolean isOnlyWhenFirstInStatement();
	
	/**
	 * When statement has everything it can already.
	 * 
	 * @return boolean
	 */
	boolean isTotallyComplete();
	
	TokenStatement lastStatement();
	
	/**
	 * @param expression
	 * @return boolean
	 */
	boolean setArguments(//
			final String expression);
	
	/**
	 * 
	 * @return
	 */
	boolean setControlBreakUsed();
	
	/**
	 * 
	 * @return
	 */
	boolean setControlContinueUsed();
	
	/**
	 * @param identifier
	 * @return boolean
	 */
	boolean setIdentifier(//
			final String identifier);
	
	/**
	 * @param locals
	 * @return boolean
	 */
	boolean setLocals(//
			final BaseObject locals);
	
	/**
	 * StatementMulti - should pass it's locals to locals target given, others
	 * should ignore
	 * 
	 * @param target
	 */
	void setLocalsTarget(//
			final TokenStatement target);
	
	/**
	 * @param parent
	 * @return boolean
	 */
	boolean setParent(//
			final TokenStatement parent);
	
	/**
	 * @param assembly
	 * @param startOffset
	 *            - program start point, lowest address to optimize till.
	 * @throws Exception
	 */
	void toAssembly( //
			final ProgramAssembly assembly,
			final int startOffset) throws Exception;
}
