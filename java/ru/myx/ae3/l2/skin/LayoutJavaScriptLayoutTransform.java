package ru.myx.ae3.l2.skin;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecFunctionImpl;
import ru.myx.ae3.exec.ExecNonMaskedException;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.l2.LayoutDefinition;
import ru.myx.ae3.l2.TargetContext;
import ru.myx.ae3.report.Report;
import ru.myx.ae3.vfs.Entry;

class LayoutJavaScriptLayoutTransform implements LayoutDefinition<TargetContext<?>> {
	
	
	private final String name;

	private final BaseFunction function;

	LayoutJavaScriptLayoutTransform(final String name, final Class<?> anchor, final String key) throws Exception {
		this.name = name;
		this.function = new ExecFunctionImpl(
				Exec.currentProcess().ri10GV,
				"layout jslt script", //
				new String[]{
						"context", "layout", "layout script: " + anchor.getName() + "/" + key
				}, Transfer.createBuffer(anchor.getResourceAsStream(key)).toString(Engine.CHARSET_UTF8));
	}

	LayoutJavaScriptLayoutTransform(final String name, final Entry source) throws Exception {
		this.name = name;
		this.function = new ExecFunctionImpl(
				Exec.currentProcess().ri10GV,
				"layout jslt script", //
				new String[]{
						"context", "layout", "layout file: " + source.getLocation()
				}, source.toBinary().getBinary().toString(Engine.CHARSET_UTF8));
	}

	LayoutJavaScriptLayoutTransform(final String name, final String source) throws Exception {
		this.name = name;
		this.function = new ExecFunctionImpl(
				Exec.currentProcess().ri10GV,
				"layout jslt script", //
				new String[]{
						"context", "layout", "layout script: <anon source>"
				}, source);
	}

	@Override
	public BaseObject onExecute(final TargetContext<?> target, final BaseObject layout) {
		
		
		if (Report.MODE_DEBUG) {
			target.dump("JSL execute: " + this.name);
		}
		try {
			return this.function.callNE2(target.getContext(), layout, Base.forUnknown(target), layout);
		} catch (final ExecNonMaskedException e) {
			throw e;
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String toString() {
		
		
		return "[object " + this.getClass().getSimpleName() + "(" + Format.Ecma.string(this.name) + ")]";
	}
}
