/**
 * 
 */
package ru.myx.ae3.exec;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Iterator;

import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BasePrimitive;
import ru.myx.ae3.base.BasePrimitiveString;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.base.ToPrimitiveHint;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.reflect.ReflectionManual;
import ru.myx.ae3.report.Report;

/**
 * Class for ExecNonMaskedException exceptions explicitly thrown from the
 * script.
 * 
 * 
 * @author myx
 * 		
 */
@ReflectionManual
public class ExecThrown extends ExecNonMaskedException implements Value<Object> {
	
	private static final BaseObject PROTOTYPE = Reflect.classToBasePrototype(ExecThrown.class);
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9035103253157862157L;
	
	// private final BaseObject message;
	
	// // replace '.prototype' to '.message'
	
	/**
	 * 
	 */
	protected final Object detail;
	
	/**
	 * 
	 */
	protected final Object source;
	
	/**
	 * 
	 * @param message
	 * @param source
	 * @param detail
	 */
	@ReflectionExplicit
	public ExecThrown(final BaseObject message, final Object source, final Object detail) {
		// super( BaseAbstractException.PROTOTYPE, "exec-thrown" );
		super(message, "exec-thrown");
		// this.message = message;
		this.source = source;
		this.detail = detail;
	}
	
	@Override
	public BaseArray baseArray() {
		
		return this.prototype.baseArray();
	}
	
	@Override
	public BaseFunction baseCall() {
		
		return this.prototype.baseCall();
	}
	
	@Override
	public void baseClear() {
		
		this.prototype.baseClear();
	}
	
	@Override
	public BaseFunction baseConstruct() {
		
		return this.prototype.baseConstruct();
	}
	
	@Override
	public boolean baseDefine(final BasePrimitiveString name, final BaseObject value, final short attributes) {
		
		return this.prototype.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDefine(final String name, final BaseObject value, final short attributes) {
		
		return this.prototype.baseDefine(name, value, attributes);
	}
	
	@Override
	public boolean baseDelete(final String name) {
		
		return this.prototype.baseDelete(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final BasePrimitiveString name) {
		
		return ExecThrown.PROTOTYPE.baseGetOwnProperty(name);
	}
	
	@Override
	public BaseProperty baseGetOwnProperty(final String name) {
		
		return ExecThrown.PROTOTYPE.baseGetOwnProperty(name);
	}
	
	@Override
	public boolean baseHasKeysOwn() {
		
		return this.prototype.baseHasKeysOwn();
	}
	
	@Override
	public boolean baseIsExtensible() {
		
		return this.prototype.baseIsExtensible();
	}
	
	@Override
	public Iterator<String> baseKeysOwn() {
		
		return this.prototype.baseKeysOwn();
	}
	
	@Override
	public Iterator<? extends CharSequence> baseKeysOwnAll() {
		
		return this.prototype.baseKeysOwnAll();
	}
	
	@Override
	public Iterator<? extends BasePrimitive<?>> baseKeysOwnPrimitive() {
		
		return this.prototype.baseKeysOwnPrimitive();
	}
	
	@Override
	public BasePrimitive<?> baseToPrimitive(final ToPrimitiveHint hint) {
		
		return this.prototype.baseToPrimitive(hint);
	}
	
	@Override
	public BasePrimitiveString baseToString() {
		
		return this.prototype.baseToString();
	}
	
	@Override
	public BaseObject baseValue() {
		
		return this.prototype;
	}
	
	@Override
	public boolean equals(final Object o) {
		
		return this.prototype.equals(o);
	}
	
	@Override
	public final String getLocalizedMessage() {
		
		return this.getMessage();
	}
	
	@Override
	/**
	 * No reflection for this method of this class - should be transparent for
	 * thrown object
	 */
	// @ReflectionExplicit
	public String getMessage() {
		
		return String.valueOf(this.prototype);
	}
	
	@Override
	@ReflectionExplicit
	public StackTraceElement[] getStackTrace() {
		
		if (this.getCause() == null && !(Report.MODE_ASSERT || Report.MODE_DEVEL)) {
			return null;
		}
		return super.getStackTrace();
	}
	
	/**
	 * 
	 * @return
	 */
	@ReflectionExplicit
	public Object getThrownDetail() {
		
		return this.detail;
	}
	
	/**
	 * 
	 * @return
	 */
	@ReflectionExplicit
	public Object getThrownSource() {
		
		return this.source;
	}
	
	/**
	 * 
	 * @return
	 */
	@ReflectionExplicit
	public Object getThrownValue() {
		
		return this.prototype;
	}
	
	@Override
	public int hashCode() {
		
		return this.prototype.hashCode();
	}
	
	@Override
	public void printStackTrace(final PrintStream s) {
		
		if (this.getCause() == null && !(Report.MODE_ASSERT || Report.MODE_DEVEL)) {
			s.println(this.toString());
		} else {
			super.printStackTrace(s);
		}
		
	}
	
	@Override
	public void printStackTrace(final PrintWriter s) {
		
		if (this.getCause() == null && !(Report.MODE_ASSERT || Report.MODE_DEVEL)) {
			s.println(this.toString());
		} else {
			super.printStackTrace(s);
		}
	}
	
	@Override
	public String toString() {
		
		return String.valueOf(this.prototype);
	}
	
	@Override
	public BaseObject getMessageContent() {
		
		return this.prototype;
	}
}
