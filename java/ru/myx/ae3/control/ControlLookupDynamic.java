/*
 * Created on 14.07.2003 To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.control;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseHostLookup;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecNonMaskedException;
import ru.myx.ae3.exec.ProgramPart;

/** @author myx
 *
 *         To change the template for this generated type comment go to Window>Preferences>Java>Code
 *         Generation>Code and Comments */
public final class ControlLookupDynamic extends BaseHostLookup {
	
	private final String expression;
	
	private final ProgramPart calc;
	
	private final String entryDelimeter;
	
	private final String fieldDelimeter;
	
	/** @param expression
	 * @param entryDelimeter
	 * @param fieldDelimeter */
	public ControlLookupDynamic(final String expression, final String entryDelimeter, final String fieldDelimeter) {

		this.expression = expression;
		this.calc = Evaluate.prepareFunctionObjectForExpression(expression, null);
		this.entryDelimeter = entryDelimeter;
		this.fieldDelimeter = fieldDelimeter;
	}
	
	@Override
	public final BaseObject baseGetLookupValue(final BaseObject key) {
		
		try {
			final Object o = this.calc.callNE0(Exec.currentProcess(), this);
			if (o == null) {
				return key;
			}
			final String s = o.toString().trim();
			if (s.length() == 0) {
				return key;
			}
			final int fdLength = this.fieldDelimeter.length();
			if (this.entryDelimeter.length() == 1) {
				for (final StringTokenizer st = new StringTokenizer(s, this.entryDelimeter); st.hasMoreTokens();) {
					final String Current = st.nextToken().trim();
					if (Current.length() == 0) {
						continue;
					}
					final String Key;
					final String Title;
					final int Pos = Current.indexOf(this.fieldDelimeter);
					if (Pos == -1) {
						Key = Title = Current;
					} else {
						Key = Current.substring(0, Pos);
						Title = Current.substring(Pos + fdLength);
					}
					if (Key.equals(key.baseToJavaString())) {
						return Base.forString(Title);
					}
				}
			} else {
				final String[] entries = s.split(this.entryDelimeter);
				for (final String element : entries) {
					final String Current = element.trim();
					if (Current.length() == 0) {
						continue;
					}
					final String Key;
					final String Title;
					final int Pos = Current.indexOf(this.fieldDelimeter);
					if (Pos == -1) {
						Key = Title = Current;
					} else {
						Key = Current.substring(0, Pos);
						Title = Current.substring(Pos + fdLength);
					}
					if (Key.equals(key.baseToJavaString())) {
						return Base.forString(Title);
					}
				}
			}
			return key;
		} catch (final ExecNonMaskedException e) {
			throw e;
		} catch (final Throwable e) {
			throw new RuntimeException("Dynamic lookup exception, expr=" + this.expression, e);
		}
	}
	
	@Override
	public final Iterator<String> baseKeysOwn() {
		
		try {
			final Object o = this.calc.callNE0(Exec.currentProcess(), this);
			if (o == null) {
				return null;
			}
			final String s = o.toString().trim();
			if (s.length() == 0) {
				return null;
			}
			final List<String> keys = new ArrayList<>();
			if (this.entryDelimeter.length() == 1) {
				for (final StringTokenizer st = new StringTokenizer(s, this.entryDelimeter); st.hasMoreTokens();) {
					final String Current = st.nextToken().trim();
					if (Current.length() == 0) {
						continue;
					}
					final int Pos = Current.indexOf(this.fieldDelimeter);
					keys.add(
							Pos == -1
								? Current
								: Current.substring(0, Pos));
				}
			} else {
				final String[] entries = s.split(this.entryDelimeter);
				for (final String element : entries) {
					final String Current = element.trim();
					if (Current.length() == 0) {
						continue;
					}
					final int Pos = Current.indexOf(this.fieldDelimeter);
					keys.add(
							Pos == -1
								? Current
								: Current.substring(0, Pos));
				}
			}
			return keys.iterator();
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Throwable e) {
			throw new RuntimeException("Dynamic lookup exception, expr=" + this.expression, e);
		}
	}
	
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		return this.baseKeysOwn();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		return Base.iteratorPrimitiveSafe(this.baseKeysOwn());
	}
	
	@Override
	public String toString() {
		
		return "[Lookup: dynamic: " + this.expression + "]";
	}
}
