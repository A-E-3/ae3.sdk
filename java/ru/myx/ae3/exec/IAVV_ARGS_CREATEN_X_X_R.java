/*
 * Created on 29.10.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ru.myx.ae3.exec;

import java.util.Map;

import ru.myx.ae3.base.BaseObject;

/** @author myx */
public final class IAVV_ARGS_CREATEN_X_X_R implements Instruction, NamedToIndexMapper {

	private final int[] paramIndexes;

	private final String[] paramNames;

	private final Map<String, Integer> map;

	private final int params;

	private final int size;

	/** @param argumentCount
	 * @param map */
	public IAVV_ARGS_CREATEN_X_X_R(final int argumentCount, final Map<String, Integer> map) {

		assert map != null : "Use ARGS_CREATEL for non-named argument lists";
		this.size = argumentCount;
		this.params = map.size();
		this.paramNames = new String[this.params];
		this.paramIndexes = new int[this.params];
		int current = 0;
		for (final Map.Entry<String, Integer> entry : map.entrySet()) {
			final String key = entry.getKey();
			final Integer index = entry.getValue();
			this.paramNames[current] = key;
			this.paramIndexes[current] = index.intValue();
			current++;
		}
		this.map = map;
	}

	@Override
	public final ExecStateCode execCall(final ExecProcess ctx) {
		/** Can't use CTX - used for vm calls and stack values are overwritten immediately
		 * <code>final ExecArguments arguments = process.argumentsCtxMap( this.size, this );</code> */

		final int size = this.size;
		final ExecArguments arguments = ctx.argumentsCopyMap(size, this);
		final BaseObject[] stack = ctx.fldStack;
		final int rASP = ctx.ri0ASP;
		for (int i = size; i > 0; --i) {
			stack[rASP - i] = null;
		}
		ctx.ri0ASP -= size;
		ctx.ra0RB = arguments;
		return null;
	}

	@Override
	public final int getOperandCount() {

		return this.size;
	}

	@Override
	public final int getResultCount() {

		return 0;
	}

	@Override
	public int length() {

		return this.size;
	}

	@Override
	public final int nameCount() {

		return this.params;
	}

	@Override
	public final int nameIndex(final int nameIndex) {

		return this.paramIndexes[nameIndex];
	}

	@Override
	public final int nameIndex(final String key) {

		final Integer result = this.map.get(key);
		if (result == null) {
			return -1;
		}
		return result.intValue();
	}

	@Override
	public final String[] names() {

		return this.paramNames;
	}

	@Override
	public final String toCode() {

		return "ARGS_CREATEN\t"//
				+ this.size + "\t0\tNN\tNEXT" + " /*" + this.map + "*/";
	}
}
