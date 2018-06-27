package ru.myx.ae3.l2.skin;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.l2.LayoutDefinition;
import ru.myx.ae3.l2.TargetContext;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.Entry;

class LayoutJavaScriptObject implements LayoutDefinition<TargetContext<?>> {
	
	
	private final String name;
	
	private final BaseObject object;
	
	LayoutJavaScriptObject(final String name, final Class<?> anchor, final String key) throws Exception {
		
		this.name = name;
		final String code = Transfer//
				.createBuffer(anchor.getResourceAsStream(key))//
				.toString(Engine.CHARSET_UTF8);
		/**
		 * FIXME: Current process is kinda wrong?!
		 */
		this.object = Evaluate.evaluateObject(code, Exec.currentProcess(), null);
	}
	
	LayoutJavaScriptObject(final String name, final Entry source) throws Exception {
		
		this.name = name;
		final String code = source//
				.toBinary()//
				.getBinaryContent()//
				.baseValue()//
				.nextCopy()//
				.toString(Engine.CHARSET_UTF8);
		/**
		 * FIXME: Current process is kinda wrong?!
		 */
		this.object = Evaluate.evaluateObject(code, Exec.currentProcess(), null);
	}
	
	LayoutJavaScriptObject(final String name, final String source) throws Exception {
		
		this.name = name;
		/**
		 * FIXME: Current process is kinda wrong?!
		 */
		this.object = Evaluate.evaluateObject(source, Exec.currentProcess(), null);
	}
	
	@Override
	public BaseObject onExecute(final TargetContext<?> target, final BaseObject layout) {
		
		
		if (Report.MODE_DEBUG) {
			target.dump("JSO execute: " + this.name);
		}
		try {
			final BaseFunction function = this.object.baseGet("onLayoutExecute", BaseObject.UNDEFINED).baseCall();
			if (function == null) {
				return Base.forString("Layout '" + this.name + "' must have 'onLayoutExecute' method.");
			}
			return function.callNE2(target.getContext(), this.object, Base.forUnknown(target), layout);
			/**
			 * <code>
			context.vmPush( layout );
			context.vmPush( Base.forUnknown( target ) );
			return context.vmCallS( function, this.object, 2, false );
			</code>
			 */
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
}
