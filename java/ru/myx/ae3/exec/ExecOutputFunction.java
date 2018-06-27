package ru.myx.ae3.exec;

import java.util.StringTokenizer;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseFunction;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.flow.ObjectTarget;

/** @author myx */
public interface ExecOutputFunction extends ObjectTarget<BaseObject>, ExecCallableBase.ExecStore1, ExecCallableJava.NativeE1 {
	
	/** FIXME: taken from LayoutEngine - should be deleted.
	 *
	 * sequence-attachment-west -> SequenceAttachmentWest
	 *
	 * @param layoutName
	 * @return */
	static String convertLayoutNameToClassName(final String layoutName) {
		
		final StringBuilder result = new StringBuilder(layoutName.length());
		for (final StringTokenizer st = new StringTokenizer(layoutName, "_-"); st.hasMoreTokens();) {
			final String token = st.nextToken();
			result.append(token.substring(0, 1).toUpperCase() + token.substring(1));
		}
		return result.toString();
	}
	
	/** Reducing checked exceptions throws */
	@Override
	public boolean absorb(final BaseObject object);
	
	@Override
	default Class<? extends BaseObject> accepts() {
		
		return BaseObject.class;
	}
	
	@Override
	default BaseObject callNE1(final ExecProcess ctx, final BaseObject instance, final BaseObject argument) {
		
		for (BaseObject something = argument;;) {
			if (something.baseIsPrimitive()) {
				this.absorb(something);
				return BaseObject.UNDEFINED;
			}
			{
				final String layoutName = Base.getString(something, "$layout", "").trim();
				if (layoutName.length() == 0) {
					this.absorb(argument);
					return BaseObject.UNDEFINED;
				}
				final BaseObject layoutType = ctx.baseGet(
						ExecOutputFunction.convertLayoutNameToClassName(layoutName), //
						BaseObject.UNDEFINED);
				if (!layoutType.baseIsPrimitive()) {
					final BaseFunction candidate = layoutType.baseGet("drawLayout", BaseObject.UNDEFINED)//
							.baseCall();
					if (candidate != null) {
						something = candidate.callNE1(ctx, layoutType, something);
						continue;
					}
					final BaseObject layoutDefault = argument.baseGet(layoutName, null);
					if (layoutDefault != null) {
						something = layoutDefault;
						continue;
					}
				}
			}
		}
	}
	
	@Override
	default void close() {
		
		//
	}
	
	@Override
	default ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseObject argument) {
		
		for (BaseObject something = argument;;) {
			if (something.baseIsPrimitive()) {
				this.absorb(something);
				return store.execReturnUndefined(ctx);
			}
			{
				final String layoutName = Base.getString(something, "$layout", "").trim();
				if (layoutName.length() == 0) {
					this.absorb(argument);
					return store.execReturnUndefined(ctx);
				}
				final BaseObject layoutType = ctx.baseGet(
						ExecOutputFunction.convertLayoutNameToClassName(layoutName), //
						BaseObject.UNDEFINED);
				if (!layoutType.baseIsPrimitive()) {
					final BaseFunction candidate = layoutType.baseGet("drawLayout", BaseObject.UNDEFINED)//
							.baseCall();
					if (candidate != null) {
						final ExecStateCode code = candidate.execCallPrepare(ctx, layoutType, ResultHandler.FA_BNN_NXT, true, argument);
						if (code == null || code == ExecStateCode.NEXT || code == ExecStateCode.RETURN) {
							something = ctx.ra0RB;
							continue;
						}
						return code;
					}
					final BaseObject layoutDefault = argument.baseGet(layoutName, null);
					if (layoutDefault != null) {
						something = layoutDefault;
						continue;
					}
				}
			}
		}
	}
	
	@Override
	default Class<? extends Object> execResultClassJava() {
		
		return Void.class;
	}
	
}
