package ru.myx.ae3.exec;

import ru.myx.ae3.common.Describable;
import ru.myx.ae3.report.Report;

/** @author myx
 *
 *         TODO: move to .help or even rename */
final class ExecValueFormat {

	static final void formatValue(final StringBuilder data, final Object value, final boolean detail) {

		if (value == null) {
			data.append("null");
			return;
		}

		if (value instanceof final Describable describable) {
			data.append(describable.baseDescribe());
			return;
		}

		final String stringValue;
		{
			String stringValueCheck;
			try {
				stringValueCheck = value.toString();
			} catch (final Throwable t) {
				stringValueCheck = "ERROR: " + t.toString();
			}
			stringValue = stringValueCheck;
		}
		data.append(
				stringValue == null
					? "null"
					: ExecValueFormat.limitString(stringValue));
		if (detail) {
			data.append("\n\t\t").append(value.getClass().getName());
		}
	}

	static final String limitString(final String stringValue) {

		final int length = stringValue.length();
		final int limit = Report.MODE_ASSERT || Report.MODE_DEBUG
			? Report.MODE_DEVEL
				? 1024
				: 512
			: 256;
		if (length <= limit) {
			return stringValue.replace("\r", "").replace("\n", "\\n").replace("\t", "\\t");
		}
		return stringValue.substring(0, limit).replace("\r", "").replace("\n", "\\n").replace("\t", "\\t") + "... and " + (length - limit) + " of " + length
				+ " characters left...";
	}

	static final void printNumber(final StringBuilder builder, final int number, final int length) {

		if (number < 0) {
			{
				final String decimal = Long.toString(number & 0xFFFFFFFFL, 16);
				for (int i = length - decimal.length(); i > 0; --i) {
					builder.append('f');
				}
				builder.append(decimal);
			}
			builder.append(' ');
			builder.append('(');
			{
				builder.append('-');
				final String decimal = Integer.toString(-number, 10);
				for (int i = length - decimal.length(); i > 1; --i) {
					builder.append('0');
				}
				builder.append(decimal);
			}
			builder.append(')');
		} else {
			{
				final String decimal = Integer.toString(number, 16);
				for (int i = length - decimal.length(); i > 0; --i) {
					builder.append('0');
				}
				builder.append(decimal);
			}
			builder.append(' ');
			builder.append('(');
			{
				final String decimal = Integer.toString(number, 10);
				for (int i = length - decimal.length(); i > 0; --i) {
					builder.append('0');
				}
				builder.append(decimal);
			}
			builder.append(')');
		}
	}
}
