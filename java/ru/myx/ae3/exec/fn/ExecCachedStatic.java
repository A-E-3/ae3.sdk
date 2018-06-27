package ru.myx.ae3.exec.fn;

import ru.myx.ae3.base.BaseAbstractException;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseFunctionAbstract;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.cache.CacheL2;
import ru.myx.ae3.cache.CreationHandlerObject;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.exec.ExecCallableBase;
import ru.myx.ae3.exec.ExecCallableJava;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ExecStateCode;
import ru.myx.ae3.exec.ProgramPart;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.know.Guid;
import ru.myx.ae3.know.GuidStream;
import ru.myx.ae3.reflect.ControlType;
import ru.myx.ae3.report.Report;

/*
 * Created on 16.01.2005
 */
/** @author myx */
public final class ExecCachedStatic extends BaseFunctionAbstract implements ExecCallableBase.ExecStoreX, ExecCallableJava.NativeEX, CreationHandlerObject<ExecProcess, BaseObject>

{

	private final ControlFieldset<?> argumentsFieldset;

	private final CacheL2<BaseObject> cache;

	private final long expiration;

	private final String method;

	private final ExecProcess parentContext;

	private final ProgramPart renderer;

	private final ControlType<?, ?> result;

	private final BaseObject staticInstance;

	/** @param cache
	 * @param instance
	 * @param type
	 * @param arguments
	 * @param renderer
	 * @param method
	 * @param expiration
	 * @param parentContext */
	public ExecCachedStatic(
			final CacheL2<BaseObject> cache,
			final BaseObject instance,
			final ControlType<?, ?> type,
			final ControlFieldset<?> arguments,
			final ProgramPart renderer,
			final String method,
			final long expiration,
			final ExecProcess parentContext) {
		this.cache = cache;
		this.staticInstance = instance;
		this.result = type;
		this.argumentsFieldset = arguments;
		this.renderer = renderer;
		this.method = method;
		this.expiration = expiration;
		this.parentContext = parentContext;
	}

	@Override
	public BaseObject callNEX(final ExecProcess ctx, final BaseObject instance, final BaseArray arguments) {

		final String guid;
		{
			guid = this.result.getTypeName();
		}
		final BaseObject context = ControlFieldset.argumentsFieldsetContextToLocals(//
				this.argumentsFieldset,
				ctx,
				arguments,
				ctx.vmScopeDeriveContext(this.parentContext)//
		);
		final String method;
		{
			final StringBuilder unique = new StringBuilder(64);
			unique.append(this.method);
			unique.append(':'); // for STATIC
			/** It is really important for this key to be as compact as possible and not to dig
			 * internally when object can be represented as reproducible */
			final Guid identifier = GuidStream.toUniqueIdentifier(context, false);
			unique.append(identifier.toBase64());
			method = unique.toString();
		}
		{
			final BaseObject result = this.cache.get(guid, method);
			if (result != null) {
				return this.result.convertAnyNativeToNative(result);
			}
		}
		if (Report.MODE_DEVEL) {
			Report.info("TN_METHOD/CACHE/STATIC", "step1: " + method);
		}
		{
			final int ri0ASP = ctx.ri0ASP;
			ctx.vmFrameEntryExCall(true, this.staticInstance, this, arguments, ResultHandler.FA_BNN_NXT);
			final BaseObject result;
			try {
				result = this.cache.get(guid, method, ctx, guid, this);
				assert result != null : "NULL result!";
			} finally {
				/** what if it was not executed at all? */
				if (ctx.ri0BSB > ri0ASP) {
					ctx.vmFrameLeave();
				}
			}
			return this.result.convertAnyNativeToNative(result);
		}
	}

	@Override
	public final BaseObject create(final ExecProcess ctx, final String key) {

		try {
			return this.renderer.execCallPreparedInilne(ctx);
		} catch (final RuntimeException e) {
			throw e;
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public final int execArgumentsAcceptable() {

		return this.argumentsFieldset == null
			? Integer.MAX_VALUE
			: this.argumentsFieldset.size();
	}

	@Override
	public final int execArgumentsDeclared() {

		return this.argumentsFieldset == null
			? 0
			: this.argumentsFieldset.size();
	}

	@Override
	public ExecStateCode execCallPrepare(final ExecProcess ctx, final BaseObject instance, final ResultHandler store, final boolean inline, final BaseArray arguments) {

		final String guid;
		{
			guid = this.result.getTypeName();
		}
		final BaseObject context = ControlFieldset.argumentsFieldsetContextToLocals(//
				this.argumentsFieldset,
				ctx,
				arguments,
				BaseObject.createObject(this.parentContext.rb7FV)//
		);
		final String method;
		{
			final StringBuilder unique = new StringBuilder(64);
			unique.append(this.method);
			unique.append(':'); // for STATIC
			/** It is really important for this key to be as compact as possible and not to dig
			 * internally when object can be represented as reproducible */
			final Guid identifier = GuidStream.toUniqueIdentifier(context, false);
			unique.append(identifier.toBase64());
			method = unique.toString();
		}
		{
			final BaseObject result = this.cache.get(guid, method);
			if (result != null) {
				return store.execReturn(ctx, this.result.convertAnyNativeToNative(result));
			}
		}
		if (Report.MODE_DEVEL) {
			Report.info("TN_METHOD/CACHE/STATIC", "step1: " + method);
		}
		{
			final int ri0ASP = ctx.ri0ASP;
			ctx.vmFrameEntryExCall(true, this.staticInstance, this, arguments, ResultHandler.FA_BNN_NXT);
			ctx.rb7FV = ctx.ri10GV = context;
			final BaseObject result;
			try {
				result = this.cache.get(guid, method, ctx, guid, this);
				assert result != null : "NULL result!";
			} catch (final BaseAbstractException e) {
				ctx.ra0RB = e;
				return ExecStateCode.ERROR;
			} finally {
				/** what if it was not executed at all? */
				if (ctx.ri0BSB > ri0ASP) {
					ctx.vmFrameLeave();
				}
			}
			return store.execReturn(ctx, this.result.convertAnyNativeToNative(result));
		}
	}

	@Override
	public final boolean execHasNamedArguments() {

		return false;
	}

	@Override
	public final boolean execIsConstant() {

		return false;
	}

	@Override
	public Class<?> execResultClassJava() {

		return this.result.getJavaClass();
	}

	@Override
	public BaseObject execScope() {

		/** executes in real current scope */
		return ExecProcess.GLOBAL;
	}

	@Override
	public final long getTTL() {

		return this.expiration;
	}

	@Override
	public final String toString() {

		return "CACHE-MTHD(S), key=" + this.method;
	}
}
