package ru.myx.sapi;

import java.io.IOException;

import ru.myx.ae3.base.BaseNativeString;
import ru.myx.ae3.help.InlineEvalExpression;

final class InlineEvalExpressionImpl extends BaseNativeString implements InlineEvalExpression {
	
	public InlineEvalExpressionImpl(final CharSequence stringValue) {
		super( stringValue );
	}
	
	@Override
	public Appendable toEvalExpression(final Appendable target) throws IOException {
		return target.append( this.stringValue );
	}
}
