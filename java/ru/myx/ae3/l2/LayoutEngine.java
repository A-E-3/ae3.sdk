package ru.myx.ae3.l2;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.StringTokenizer;

import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.exec.Exec;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.l2.base.BaseFunctionLayoutsConvertClassNameToLayoutName;
import ru.myx.ae3.l2.base.BaseFunctionLayoutsConvertLayoutNameToClassName;
import ru.myx.ae3.l2.base.BaseFunctionLayoutsExtend;
import ru.myx.ae3.l2.base.BaseFunctionLayoutsFormatObject;

/** @author myx */
public class LayoutEngine {

	static {
		/** Setup GLOBAL object with Layouts API! */
		final BaseObject layoutsApi = BaseObject.createObject();
		layoutsApi.baseDefine("convertClassNameToLayoutName", new BaseFunctionLayoutsConvertClassNameToLayoutName(), BaseProperty.ATTRS_MASK_NNN);
		layoutsApi.baseDefine("convertLayoutNameToClassName", new BaseFunctionLayoutsConvertLayoutNameToClassName(), BaseProperty.ATTRS_MASK_NNN);
		layoutsApi.baseDefine("extend", new BaseFunctionLayoutsExtend(), BaseProperty.ATTRS_MASK_NNN);
		layoutsApi.baseDefine("formatObject", new BaseFunctionLayoutsFormatObject(), BaseProperty.ATTRS_MASK_NNN);
		ExecProcess.GLOBAL.baseDefine("Layouts", layoutsApi, BaseProperty.ATTRS_MASK_NNN);
	}

	/** SequenceAttachmentWest -> sequence-attachment-west
	 *
	 * @param className
	 * @return */
	public final static String convertClassNameToLayoutName(final String className) {

		final int length = className.length();
		final StringBuilder result = new StringBuilder(length + 16);
		char prev = 0;
		for (int i = 0; i < length; ++i) {
			final char c = className.charAt(i);
			if (Character.isUpperCase(c) || Character.isDigit(c) && !Character.isDigit(prev)) {
				if (i > 0) {
					result.append('-');
				}
				result.append(Character.toLowerCase(c));
			} else {
				result.append(c);
			}
			prev = c;
		}
		return result.toString();
	}

	/** sequence-attachment-west -> SequenceAttachmentWest
	 *
	 * @param layoutName
	 * @return */
	public final static String convertLayoutNameToClassName(final String layoutName) {

		final StringBuilder result = new StringBuilder(layoutName.length());
		for (final StringTokenizer st = new StringTokenizer(layoutName, "_-"); st.hasMoreTokens();) {
			final String token = st.nextToken();
			result.append(token.substring(0, 1).toUpperCase() + token.substring(1));
		}
		return result.toString();
	}

	/** @return */
	public final static BaseObject getDocumentation() {

		return new BaseNativeObject("layout", "documentation-layouts");
	}

	/** @param input
	 * @return */
	public final static BaseObject parseJSLD(final InputStream input) {

		return LayoutEngine.parseJSLD(Transfer.createBuffer(input).toString(StandardCharsets.UTF_8));
	}

	/** @param input
	 * @return */
	public final static BaseObject parseJSLD(final String input) {

		final ExecProcess ctx = Exec.createProcess(null, "JSON parse");
		return Evaluate.evaluateObject(input, ctx, null);
	}
}
