package ru.myx.ae3.help;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.flow.Flow;

/** @author myx */
public final class Message {
	
	/** @param attributes
	 * @param attributeName
	 * @param defaultValue
	 * @return string */
	public static final String cleanAttributeValue(final BaseMap attributes, final String attributeName, final String defaultValue) {
		
		if (attributes == null) {
			return defaultValue;
		}
		final String val = Base.getString(attributes, attributeName, "").trim();
		if (val.length() == 0) {
			return defaultValue;
		}
		final int pos = val.indexOf(';');
		return pos == -1
			? val
			: val.substring(0, pos);
	}
	
	/** @param message
	 * @param attributeName
	 * @param defaultValue
	 * @return string */
	public static final String cleanAttributeValue(final BaseMessage message, final String attributeName, final String defaultValue) {
		
		if (message == null) {
			return defaultValue;
		}
		return Message.cleanAttributeValue(message.getAttributes(), attributeName, defaultValue);
	}
	
	/** @param attributes
	 * @param attributeName
	 * @param defaultValue
	 * @return string */
	public static final String cleanAttributeValue(final BaseObject attributes, final String attributeName, final String defaultValue) {
		
		if (attributes == null) {
			return defaultValue;
		}
		final String val = Base.getString(attributes, attributeName, "").trim();
		if (val.length() == 0) {
			return defaultValue;
		}
		final int pos = val.indexOf(';');
		return pos == -1
			? val
			: val.substring(0, pos);
	}
	
	/** @param attributes
	 * @param attributeName
	 * @param defaultValue
	 * @return string */
	public static final String cleanAttributeValue(final Map<String, Object> attributes, final String attributeName, final String defaultValue) {
		
		if (attributes == null) {
			return defaultValue;
		}
		final String val = Convert.MapEntry.toString(attributes, attributeName, null);
		if (val == null) {
			return defaultValue;
		}
		final int pos = val.indexOf(';');
		return pos == -1
			? val
			: val.substring(0, pos);
	}
	
	/** Returns charset for given message attributes (based on 'Content-Type: xxx/xxx; charset='
	 * like stuff) and defaults to UTF-8.
	 *
	 * @param attributes
	 * @return */
	public static String getCharcterEncoding(final BaseObject attributes) {
		
		if (attributes == null || attributes.baseIsPrimitive()) {
			return "UTF-8";
		}
		{
			final String encoding = Base.getString(attributes, "Content-Charset", null);
			if (encoding != null) {
				return "UTF-8".equalsIgnoreCase(encoding)
					? "UTF-8"
					: encoding;
			}
		}
		{
			final String encoding = Message.subAttributeValue(attributes, "Content-Type", "charset", null);
			if (encoding != null) {
				return "UTF-8".equalsIgnoreCase(encoding)
					? "UTF-8"
					: encoding;
			}
		}
		{
			return "UTF-8";
		}
	}
	
	/** Returns charset for given message attributes (based on 'Content-Type: xxx/xxx; charset='
	 * like stuff) and defaults to UTF-8.
	 *
	 * @param attributes
	 * @return */
	public static Charset getCharset(final BaseObject attributes) {
		
		if (attributes == null || attributes.baseIsPrimitive()) {
			return StandardCharsets.UTF_8;
		}
		{
			final String encoding = Base.getString(attributes, "Content-Charset", null);
			if (encoding != null) {
				return "UTF-8".equalsIgnoreCase(encoding)
					? StandardCharsets.UTF_8
					: Charset.forName(encoding);
			}
		}
		{
			final String encoding = Message.subAttributeValue(attributes, "Content-Type", "charset", null);
			if (encoding != null) {
				return "UTF-8".equalsIgnoreCase(encoding)
					? StandardCharsets.UTF_8
					: Charset.forName(encoding);
			}
		}
		{
			return StandardCharsets.UTF_8;
		}
	}
	
	/** @param attributes
	 * @param attributeName
	 * @param subattributeName
	 * @param defaultValue
	 * @return string */
	public static final String subAttributeValue(final BaseMap attributes, final String attributeName, final String subattributeName, final String defaultValue) {
		
		if (attributes == null) {
			return defaultValue;
		}
		final String val = Base.getString(attributes, attributeName, "").trim();
		if (val.length() == 0) {
			return defaultValue;
		}
		return Base.getString(Flow.mimeAttribute("", "", val).getAttributes(), subattributeName, defaultValue);
	}
	
	/** @param message
	 * @param attributeName
	 * @param subattributeName
	 * @param defaultValue
	 * @return string */
	public static final String subAttributeValue(final BaseMessage message, final String attributeName, final String subattributeName, final String defaultValue) {
		
		return message == null
			? defaultValue
			: Message.subAttributeValue(message.getAttributes(), attributeName, subattributeName, defaultValue);
	}
	
	/** @param attributes
	 * @param attributeName
	 * @param subattributeName
	 * @param defaultValue
	 * @return string */
	public static final String subAttributeValue(final BaseObject attributes, final String attributeName, final String subattributeName, final String defaultValue) {
		
		if (attributes == null) {
			return defaultValue;
		}
		final String val = Base.getString(attributes, attributeName, "").trim();
		if (val.length() == 0) {
			return defaultValue;
		}
		return Base.getString(Flow.mimeAttribute("", "", val).getAttributes(), subattributeName, defaultValue);
	}
	
	/** @param attributes
	 * @param attributeName
	 * @param subattributeName
	 * @param defaultValue
	 * @return string */
	public static final String subAttributeValue(final Map<String, Object> attributes, final String attributeName, final String subattributeName, final String defaultValue) {
		
		if (attributes == null) {
			return defaultValue;
		}
		final String val = Convert.MapEntry.toString(attributes, attributeName, null);
		if (val == null) {
			return defaultValue;
		}
		return Convert.MapEntry.toString(Flow.mimeAttribute("", "", val).getAttributes(), subattributeName, defaultValue);
	}
	
	private Message() {
		
		// empty
	}
}
