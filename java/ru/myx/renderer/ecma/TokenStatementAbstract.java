/*
 * Created on 15.10.2005
 */
package ru.myx.renderer.ecma;

import java.util.Iterator;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.exec.ProgramAssembly;

abstract class TokenStatementAbstract implements TokenStatement {
	
	/**
	 * TRUE for ones without initialization code, FALSE for ones that will have
	 * their own FDECLARE command.
	 * 
	 * @param statement
	 * @param variables
	 * @return
	 */
	static final boolean toLocals(final String statement, final BaseObject variables) {
		
		boolean code = false;
		{
			final StringBuilder buffer = new StringBuilder();
			int levelBrace = 0;
			boolean isQuote = false;
			boolean isApos = false;
			boolean nextSymbol = false;
			boolean skipping = false;
			for (final char c : statement.toCharArray()) {
				if (!skipping && buffer.length() > 0 && !Character.isJavaIdentifierPart(c)) {
					skipping = true;
				}
				switch (c) {
					case '=' :
						if (!isQuote && !isApos && levelBrace == 0) {
							if (buffer.length() > 0) {
								/**
								 * does have initialization code
								 */
								variables.baseDefine(buffer.toString(), BaseObject.FALSE);
								buffer.setLength(0);
							}
							code = true;
						}
						nextSymbol = false;
						break;
					case '{' :
					case '[' :
					case '(' :
						if (!isQuote && !isApos) {
							levelBrace++;
						}
						nextSymbol = false;
						break;
					case '}' :
					case ']' :
					case ')' :
						if (!isQuote && !isApos) {
							levelBrace--;
						}
						nextSymbol = false;
						break;
					case '"' :
						if (!nextSymbol) {
							if (!isApos) {
								isQuote = !isQuote;
							}
						}
						nextSymbol = false;
						break;
					case '\'' :
						if (!nextSymbol) {
							if (!isQuote) {
								isApos = !isApos;
							}
						}
						nextSymbol = false;
						break;
					case '\\' :
						if (isQuote || isApos) {
							nextSymbol = true;
						}
						break;
					case ',' :
						if (!isQuote && !isApos && levelBrace == 0) {
							skipping = false;
							if (buffer.length() > 0) {
								/**
								 * does not have initialization code
								 */
								variables.baseDefine(buffer.toString(), BaseObject.TRUE);
								buffer.setLength(0);
							}
							continue;
						}
						break;
					default :
						nextSymbol = false;
				}
				if (!skipping && (buffer.length() > 0 || Character.isJavaIdentifierStart(c))) {
					buffer.append(c);
				}
			}
			if (buffer.length() > 0) {
				/**
				 * does not have initialization code
				 */
				variables.baseDefine(buffer.toString(), BaseObject.TRUE);
			}
			if (!variables.baseHasKeysOwn()) {
				throw new IllegalArgumentException("No variable names in var statement!");
			}
		}
		return code;
	}
	
	/**
	 * returns an array of locals to be declared.
	 * 
	 * @return
	 */
	static final BaseObject toLocalsObjectOrArray(final BaseObject locals) {
		
		if (locals == null) {
			return null;
		}
		BaseObject first = null;
		BaseList<String> result = null;
		for (final Iterator<String> iterator = locals.baseKeysOwn(); iterator.hasNext();) {
			final String key = iterator.next();
			if (locals.baseGet(key, null) == BaseObject.TRUE) {
				if (first == null) {
					first = Base.forString(key);
				} else {
					if (result == null) {
						result = BaseObject.createArray();
						result.baseDefaultPush(first);
					}
					result.baseDefaultPush(Base.forString(key));
				}
			}
		}
		return result != null
			? result
			: first;
	}
	
	protected final String identity;
	
	protected final int line;
	
	protected TokenStatement parent;
	
	TokenStatementAbstract(final String identity, final int line) {
		
		this.identity = identity;
		this.line = line;
	}
	
	protected final void addDebug(final ProgramAssembly assembly, final String comment) throws Exception {
		
		assembly.addDebug( /* this.identity // + ":" + */
				this.line //
						+ (comment == null || comment.length() == 0
							? ""
							: " // " + comment) //
		);
	}
	
	@Override
	public abstract TokenStatement createStatement(final String identity, final int line);
	
	@Override
	public final String getIdentity() {
		
		return this.identity;
	}
	
	@Override
	public final int getLine() {
		
		return this.line;
	}
	
	@Override
	public boolean isBlockStatement() {
		
		return false;
	}
	
	@Override
	public boolean isEmpty() {
		
		return false;
	}
	
	/**
	 * TODO: final
	 */
	@Override
	public boolean isNewLineSemicolon() {
		
		return false;
	}
	
	@Override
	public boolean isNextStatementFromScratch() {
		
		return false;
	}
	
	@Override
	public boolean isOnlyWhenFirstInStatement() {
		
		return false;
	}
	
	@Override
	public TokenStatement lastStatement() {
		
		return null;
	}
	
	@Override
	public boolean setControlBreakUsed() {
		
		this.parent.setControlBreakUsed();
		return true;
	}
	
	@Override
	public boolean setControlContinueUsed() {
		
		this.parent.setControlContinueUsed();
		return true;
	}
	
	@Override
	public void setLocalsTarget(final TokenStatement target) {
		
		// ignore
		/**
		 * DEBUG CHECK<code>
		 	assert !(this instanceof TokenStatementMulti)
		 * </code>
		 */
	}
	
	@Override
	public final boolean setParent(final TokenStatement parent) {
		
		assert this.parent == null : "Parent statement is already assigned!";
		this.parent = parent;
		return true;
	}
}
