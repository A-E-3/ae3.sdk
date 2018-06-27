package ru.myx.ae3.l2.skin;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.l2.LayoutDefinition;
import ru.myx.ae3.l2.TargetContext;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.Entry;

class LayoutJavaScriptLayoutData implements LayoutDefinition<TargetContext<?>> {
	private final String		name;
	
	private final BaseObject	object;
	
	LayoutJavaScriptLayoutData(final String name, final Class<?> anchor, final String key) throws Exception {
		this.name = name;
		final String code = Transfer//
				.createBuffer( anchor.getResourceAsStream( key ) )//
				.toString( Engine.CHARSET_UTF8 );
		/**
		 * FIXME: Current process is kinda wrong?!
		 */
		this.object = Evaluate.evaluateObject( code, Exec.currentProcess(), null );
	}
	
	LayoutJavaScriptLayoutData(final String name, final Entry source) throws Exception {
		this.name = name;
		final String code = source//
				.toBinary()//
				.getBinaryContent()//
				.baseValue()//
				.nextCopy()//
				.toString( Engine.CHARSET_UTF8 );
		/**
		 * FIXME: Current process is kinda wrong?!
		 */
		this.object = Evaluate.evaluateObject( code, Exec.currentProcess(), null );
	}
	
	LayoutJavaScriptLayoutData(final String name, final String source) throws Exception {
		this.name = name;
		/**
		 * FIXME: Current process is kinda wrong?!
		 */
		this.object = Evaluate.evaluateObject( source, Exec.currentProcess(), null );
	}
	
	@Override
	public BaseObject onExecute(final TargetContext<?> target, final BaseObject layout) {
		if (Report.MODE_DEBUG) {
			target.dump( "JSLD execute: " + this.name );
		}
		return this.object;
	}
}
