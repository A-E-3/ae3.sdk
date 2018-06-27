package ru.myx.ae3.console;

import java.util.Collection;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitiveNumber;
import ru.myx.ae3.common.Value;

/**
 * @author myx
 * 
 */
public abstract class AbstractNoInputConsole extends AbstractConsole {
	private int	newLine	= -1;
	
	/**
	 * To deal with string reader task and uncertainty with CRLF sequence.
	 * 
	 * 
	 * @param ch
	 *            '\r' or '\n'
	 * @return '\r' or '\n' whichever is registered for this session.
	 */
	public int checkRegisterNewLine(final int ch) {
		assert ch == '\r' || ch == '\n' : "ch is none of the valid values: ch=" + ch;
		return this.newLine == -1
				? this.newLine = ch
				: this.newLine;
	}
	
	@Override
	public final boolean isInteractive() {
		return false;
	}
	
	@Override
	public boolean isReadable() {
		return false;
	}
	
	@Override
	public final Value<Boolean> readBoolean(final String title) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public final Value<Boolean> readBoolean(final String title, final boolean defaultValue) {
		return Base.forBoolean( defaultValue );
	}
	
	@Override
	public final Value<String> readChoose(
			final String title,
			final Collection<Map.Entry<String, Object>> selection,
			final String defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public final Value<?> readContinue(final String title) {
		return BaseObject.TRUE;
	}
	
	@Override
	public final Value<Number> readInteger(final String title) {
		return BasePrimitiveNumber.NAN;
	}
	
	@Override
	public final Value<Number> readInteger(final String title, final int defaultValue) {
		return Base.forInteger( defaultValue );
	}
	
	@Override
	public final Value<String> readPassword(final String title) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public final Value<String> readString(final String title, final String defaultValue) {
		return Base.forString( defaultValue );
	}
	
	@Override
	public final void sendProgress(final String string) {
		/**
		 * Do nothing. Non-interactive console is not interacting with supplier
		 * of its input stream through the output stream.
		 */
	}
}
