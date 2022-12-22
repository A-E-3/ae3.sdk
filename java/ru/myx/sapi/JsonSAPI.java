package ru.myx.sapi;

import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.WaitTimeoutException;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecCallable;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.ProgramAssembly;
import ru.myx.ae3.exec.ResultHandler;
import ru.myx.ae3.reflect.ReflectionExplicit;
import ru.myx.ae3.vfs.Entry;

/** @author myx */
public final class JsonSAPI {
	
	private static final BaseObject JSON_GLOBAL = BaseMap.create(null)//
			.putAppend("Date", ExecProcess.GLOBAL.baseGet("Date", null))//
			// .putAppend("Binary", ExecProcess.GLOBAL.baseGet("Binary",
			// null))//
			.putAppend("Guid", ExecProcess.GLOBAL.baseGet("Guid", null))//
			.putAppend("Binary", ExecProcess.GLOBAL.baseGet("Binary", null))//
	;

	/** @param ctx
	 * @param vfs
	 * @return
	 * @throws Exception
	 * @throws WaitTimeoutException */
	@ReflectionExplicit
	public static final BaseObject parse(final ExecProcess ctx, final Entry vfs) throws WaitTimeoutException, Exception {
		
		return vfs == null
			? BaseObject.NULL
			: vfs.isPrimitive()
				? JsonSAPI.parse(ctx, String.valueOf(vfs.toPrimitive().getPrimitiveValue()))
				: vfs.isBinary()
					? JsonSAPI.parse(ctx, vfs.toBinary().getBinaryContent().baseValue())
					/** TODO: container are also possible to implement */
					: null;
	}

	/** @param ctx
	 * @param text
	 * @return
	 * @throws Exception */
	@ReflectionExplicit
	public static final BaseObject parse(final ExecProcess ctx, final String text) throws Exception {
		
		return text == null
			? BaseObject.NULL
			: JsonSAPI.parse(ctx, text, null);
	}

	/** @param ctx
	 * @param text
	 * @param reviver
	 * @return
	 * @throws Exception */
	@ReflectionExplicit
	public static final BaseObject parse(final ExecProcess ctx, final String text, final ExecCallable reviver) throws Exception {
		
		if (text == null) {
			return BaseObject.NULL;
		}

		/** TODO: execute in new sand-boxed thread (it's easy) 8-) */

		final ExecProcess container = Exec.createProcess(ctx, "JSON / Eval Bridge");
		container.contextExecFDEBUG("JSON parse");

		container.vmScopeCreateSandbox(JsonSAPI.JSON_GLOBAL);
		final ProgramAssembly assembly = new ProgramAssembly(container);
		Evaluate.compileExpression(assembly, text, ResultHandler.FC_PNN_RET);
		return assembly.toProgram(0).callNE0(container, BaseObject.UNDEFINED);
	}

	/** @param ctx
	 * @param binary
	 * @return
	 * @throws Exception */
	@ReflectionExplicit
	public static final BaseObject parse(final ExecProcess ctx, final TransferCopier binary) throws Exception {
		
		return binary != null
			? JsonSAPI.parse(ctx, binary.toStringUtf8(), null)
			: BaseObject.NULL;
	}

	/** @param o
	 * @return */
	@ReflectionExplicit
	public static final String stringify(final BaseObject o) {
		
		return JsonSAPI.stringify(o, null);
	}

	/** @param o
	 * @param replacer
	 * @return */
	@ReflectionExplicit
	public static final String stringify(final BaseObject o, final ExecCallable replacer) {
		
		return FormatSAPI.jsObject(o);
	}

	@Override
	@ReflectionExplicit
	public final String toString() {
		
		return "JSON static function scope";
	}
}
