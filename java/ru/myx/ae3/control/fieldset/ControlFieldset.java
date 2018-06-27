/*
 * Created on 11.05.2006
 */
package ru.myx.ae3.control.fieldset;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.RandomAccess;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseHostLookup;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseProperty;
import ru.myx.ae3.control.ControlBasic;
import ru.myx.ae3.control.field.ControlField;
import ru.myx.ae3.control.field.ControlFieldFactory;
import ru.myx.ae3.exec.ExecProcess;
import ru.myx.ae3.exec.NamedToIndexMapper;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.help.Dom;
import ru.myx.ae3.reflect.Reflect;
import ru.myx.ae3.xml.Xml;

/**
 * @author myx
 * @param <T>
 *
 */
public interface ControlFieldset<T extends ControlFieldset<?>> extends NamedToIndexMapper, ControlBasic<T>, BaseList<ControlField>, RandomAccess {
	
	
	/**
	 *
	 * @param map
	 * @param name
	 * @return
	 */
	static ControlFieldset<?> getFieldsetFromMap(final BaseObject map, final String name) {
		
		
		if (map == null) {
			return null;
		}
		final BaseObject object = map.baseGet(name, BaseObject.UNDEFINED);
		assert object != null : "NULL java value";
		if (object == BaseObject.UNDEFINED) {
			return null;
		}
		if (object instanceof ControlFieldset<?>) {
			return (ControlFieldset<?>) object;
		}
		{
			final BaseObject fieldset = object.baseGet("fieldset", BaseObject.UNDEFINED);
			assert fieldset != null : "NULL java value";
			if (fieldset != BaseObject.UNDEFINED) {
				final BaseObject fields = fieldset.baseGet("field", BaseObject.UNDEFINED);
				assert fields != null : "NULL java valie";
				if (fields != BaseObject.UNDEFINED) {
					final ControlFieldset<?> result = ControlFieldset.createFieldset();
					final BaseArray array = fields.baseArray();
					if (array != null) {
						final int length = array.length();
						for (int i = 0; i < length; ++i) {
							final BaseObject current = array.baseGet(i, BaseObject.UNDEFINED);
							assert current != null : "NULL java value";
							if (Base.hasKeys(current)) {
								final String type = Base.getString(current, "class", "object");
								result.add(ControlFieldFactory.createField(type, current));
							}
						}
					} else if (Base.hasKeys(fields)) {
						final String type = Base.getString(fields, "class", "object");
						result.add(ControlFieldFactory.createField(type, fields));
					}
					return result;
				}
			}
		}
		return ControlFieldset.materializeFieldset(String.valueOf(object));
	}
	
	/**
	 *
	 */
	public static final BaseObject PROTOTYPE = new BaseNativeObject(Reflect.classToBasePrototype(ControlFieldset.class));
	
	@Override
	default BaseObject basePrototype() {
		
		
		return ControlFieldset.PROTOTYPE;
	}
	
	/**
	 * Safe to pass null field. However, field name should be defined and musn't
	 * be already taken.
	 *
	 * @param field
	 * @return fieldset
	 */
	public T addField(final ControlField field);
	
	/**
	 * Safe to pass null arrays and arrays with null elements. However, field
	 * name should be defined and musn't be already taken.
	 *
	 * @param fields
	 * @return fieldset
	 */
	public T addFields(final ControlField[] fields);
	
	/**
	 * @param fieldset
	 * @return fieldset
	 */
	public T addFields(final ControlFieldset<?> fieldset);
	
	/**
	 * From data to form
	 *
	 * @param source
	 * @param target
	 */
	public void dataRetrieve(final BaseObject source, final BaseObject target);
	
	/**
	 * From form to data
	 *
	 * @param source
	 * @param target
	 */
	public void dataStore(final BaseObject source, final BaseObject target);
	
	/**
	 * @param source
	 * @return error map
	 */
	public Map<String, String> dataValidate(final BaseObject source);
	
	/**
	 * @param name
	 * @return field
	 */
	public ControlField getField(String name);
	
	/**
	 * @return set
	 */
	public Set<String> innerFields();
	
	/**
	 * @param argumentsFieldset
	 * @param ctx
	 * @param arguments
	 * @param locals
	 * @return variable context
	 */
	static BaseObject argumentsFieldsetContextToLocals(final ControlFieldset<?> argumentsFieldset, final ExecProcess ctx, final BaseArray arguments, final BaseObject locals) {
		
		
		if (argumentsFieldset == null) {
			locals.baseDefine("data", ExecProcess.vmEnsureArgumentsDetached(ctx, arguments), BaseProperty.ATTRS_MASK_WEN);
			return locals;
		}
		if (arguments.baseHasKeysOwn()) {
			argumentsFieldset.dataRetrieve(ExecProcess.vmEnsureNative(arguments), locals);
			return locals;
		}
		{
			final int argumentCount = argumentsFieldset.size();
			final int count = Math.min(argumentCount, arguments.length());
			for (int i = 0; i < count; ++i) {
				final ControlField field = argumentsFieldset.get(i);
				assert field != null : "Shouldn't be NULL";
				final BaseObject retrieved = field.dataRetrieve(arguments.baseGet(i, null), arguments);
				assert retrieved != null : "Shouldn't be NULL";
				locals.baseDefine(field.getKey(), retrieved, BaseProperty.ATTRS_MASK_WEN);
			}
			for (int i = count; i < argumentCount; ++i) {
				final ControlField field = argumentsFieldset.get(i);
				assert field != null : "Shouldn't be NULL";
				final BaseObject retrieved = field.dataRetrieve(BaseObject.UNDEFINED, arguments);
				assert retrieved != null : "Shouldn't be NULL";
				locals.baseDefine(field.getKey(), retrieved, BaseProperty.ATTRS_MASK_WEN);
			}
			return locals;
		}
	}
	
	/**
	 * @param argumentsFieldset
	 * @param arguments
	 * @param locals
	 * @return variable context
	 */
	static BaseObject argumentsFieldsetContextToLocals(final ControlFieldset<?> argumentsFieldset, final ExecProcess arguments, final BaseObject locals) {
		
		
		if (argumentsFieldset == null) {
			locals.baseDefine("data", arguments.contextGetArguments(), BaseProperty.ATTRS_MASK_WEN);
			return locals;
		}
		if (arguments.baseHasKeysOwn()) {
			argumentsFieldset.dataRetrieve(arguments.contextGetArguments(), locals);
			return locals;
		}
		{
			final int argumentCount = argumentsFieldset.size();
			final int count = Math.min(argumentCount, arguments.length());
			for (int i = 0; i < count; ++i) {
				final ControlField field = argumentsFieldset.get(i);
				assert field != null : "Shouldn't be NULL";
				final BaseObject retrieved = field.dataRetrieve(arguments.baseGet(i, null), arguments);
				assert retrieved != null : "Shouldn't be NULL";
				locals.baseDefine(field.getKey(), retrieved, BaseProperty.ATTRS_MASK_WEN);
			}
			for (int i = count; i < argumentCount; ++i) {
				final ControlField field = argumentsFieldset.get(i);
				assert field != null : "Shouldn't be NULL";
				final BaseObject retrieved = field.dataRetrieve(BaseObject.UNDEFINED, arguments);
				assert retrieved != null : "Shouldn't be NULL";
				locals.baseDefine(field.getKey(), retrieved, BaseProperty.ATTRS_MASK_WEN);
			}
			return locals;
		}
	}
	//
	
	/**
	 * @param fieldset
	 * @param element
	 * @param readable
	 */
	static void serializeFieldset(final ControlFieldset<?> fieldset, final Element element, final boolean readable) {
		
		
		final BaseObject attributes = fieldset.getAttributes();
		if (attributes != null) {
			for (final Iterator<String> iterator = Base.keys(attributes); iterator.hasNext();) {
				final String key = iterator.next();
				element.setAttribute(key, attributes.baseGet(key, BaseObject.UNDEFINED).baseToJavaString());
			}
		}
		final int length = fieldset.size();
		for (int i = 0; i < length; ++i) {
			final ControlField field = fieldset.get(i);
			final BaseObject fieldAttributes = field.getAttributes();
			final Element current = Xml.toElement(element.getOwnerDocument(), "field", fieldAttributes, readable);
			if (fieldAttributes.baseGet("class", null) == null) {
				current.setAttribute("class", field.getFieldClass());
			}
			element.appendChild(current);
		}
	}
	
	/**
	 * @param fieldset
	 * @param readable
	 * @return fieldset
	 */
	static String serializeFieldset(final ControlFieldset<?> fieldset, final boolean readable) {
		
		
		final Document doc = Dom.createDocument();
		final Element root = doc.createElement("fieldset");
		root.setAttribute("class", "fieldset");
		ControlFieldset.serializeFieldset(fieldset, root, readable);
		return readable
			? Dom.toXmlReadable(root)
			: Dom.toXmlCompact(root);
	}
	
	/**
	 * @param definition
	 *            - XML source code with fieldset definition.
	 * @return fieldset
	 */
	static ControlFieldset<?> materializeFieldset(final String definition) {
		
		
		return ControlFieldset.materializeFieldset(Dom.toElement(definition.trim()));
	}
	
	/**
	 * @param definition
	 * @return fieldset
	 */
	static ControlFieldset<?> materializeFieldset(final Element definition) {
		
		
		final ControlFieldset<?> result = ControlFieldset.createFieldset();
		{
			final NamedNodeMap attributes = definition.getAttributes();
			for (int i = attributes.getLength() - 1; i >= 0; --i) {
				final Node item = attributes.item(i);
				result.setAttribute(item.getNodeName(), item.getNodeValue());
			}
		}
		final String definitionType = definition.getNodeName();
		if ("fieldset".equals(definitionType) || "fieldset".equals(definition.getAttribute("class"))) {
			final NodeList children = definition.getChildNodes();
			for (int i = 0; i < children.getLength(); ++i) {
				final Node node = children.item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				final Element current = (Element) node;
				final BaseObject attributes = Xml.toBase(current, "map", null, null, null);
				final String type = Base.getString(attributes, "class", "").trim();
				final ControlField field = ControlFieldFactory.createField(type, attributes);
				try {
					result.addField(field);
				} catch (final Throwable e) {
					throw new RuntimeException("Error adding field #" + i + " to a group (type=" + type + "):\n" + Dom.toXmlReadable(definition), e);
				}
			}
			return result;
		}
		if ("group".equals(definitionType)) {
			final NodeList children = definition.getChildNodes();
			for (int i = 0; i < children.getLength(); ++i) {
				final Node node = children.item(i);
				if (node.getNodeType() != Node.ELEMENT_NODE) {
					continue;
				}
				final Element current = (Element) node;
				final String type = current.getNodeName();
				final BaseObject attributes = Xml.toBase(current, "map", null, null, null);
				final String id = Base.getString(attributes, "id", Engine.createGuid());
				final String title = Base.getString(attributes, "title", id);
				final ControlField field;
				if ("bit".equals(type) || "boolean".equals(type)) {
					final boolean defaultValue = Base.getBoolean(attributes, "default", false);
					field = ControlFieldFactory.createFieldBoolean(id, title, defaultValue);
				} else //
				if ("blob".equals(type) || "file".equals(type) || "image".equals(type) || "picture".equals(type)) {
					field = ControlFieldFactory.createField("binary", attributes).setFieldType("binary");
				} else //
				if ("number".equals(type)) {
					if ("checkbox".equals(Base.getString(attributes, "type", ""))) {
						final boolean defaultValue = Base.getBoolean(attributes, "default", false);
						field = ControlFieldFactory.createFieldBoolean(id, title, defaultValue);
					} else {
						if (Base.getBoolean(attributes, "integer", false)) {
							final int defaultValue = Convert.MapEntry.toInt(attributes, "default", 0);
							final int min = Convert.MapEntry.toInt(attributes, "min", Integer.MIN_VALUE);
							final int max = Convert.MapEntry.toInt(attributes, "max", Integer.MAX_VALUE);
							field = ControlFieldFactory.createFieldInteger(id, title, defaultValue, min, max);
						} else {
							final double defaultValue = Base.getDouble(attributes, "default", 0.0);
							final double min = Base.getDouble(attributes, "min", Double.NEGATIVE_INFINITY);
							final double max = Base.getDouble(attributes, "max", Double.POSITIVE_INFINITY);
							field = ControlFieldFactory.createFieldFloating(id, title, defaultValue, min, max);
						}
					}
				} else //
				if ("string".equals(type)) {
					final String kind = Base.getString(attributes, "type", "string");
					final String defaultValue = Base.getString(attributes, "default", "");
					final int min = Convert.MapEntry.toInt(attributes, "min", 0);
					final int max = Convert.MapEntry.toInt(attributes, "max", 1024);
					if ("text".equals(kind)) {
						field = ControlFieldFactory.createFieldString(id, title, defaultValue, min, max).setFieldType("text");
					} else //
					if ("bigtext".equals(kind)) {
						field = ControlFieldFactory.createFieldString(id, title, defaultValue, min, max).setFieldType("text").setFieldVariant("bigtext");
					} else //
					if ("htmltext".equals(kind)) {
						field = ControlFieldFactory.createFieldString(id, title, defaultValue, min, max).setFieldType("text").setFieldVariant("html");
					} else {
						field = ControlFieldFactory.createFieldString(id, title, defaultValue, min, max).setFieldVariant(kind);
					}
				} else //
				if ("select".equals(type)) {
					final String kind = Base.getString(attributes, "type", "string");
					final BaseObject lookup = BaseHostLookup.obtainLookup(attributes);
					final String defaultValue = Base.getString(attributes, "default", "").trim();
					if ("multiselect".equals(kind)) {
						final Set<?> defaultSet = defaultValue.length() > 0
							? Collections.singleton(defaultValue)
							: Collections.EMPTY_SET;
						field = ControlFieldFactory.createFieldSet(id, title, defaultSet).setFieldVariant("select").setAttribute("lookup", lookup);
					} else //
					if ("bigselect".equals(kind)) {
						field = ControlFieldFactory.createFieldString(id, title, defaultValue).setFieldType("select").setFieldVariant("bigselect").setAttribute("lookup", lookup);
					} else {
						field = ControlFieldFactory.createFieldString(id, title, defaultValue).setFieldType("select").setAttribute("lookup", lookup);
					}
				} else //
				if ("clob".equals(type)) {
					final String defaultValue = Base.getString(attributes, "default", "");
					final int min = Convert.MapEntry.toInt(attributes, "min", 0);
					final int max = Convert.MapEntry.toInt(attributes, "max", 65536);
					field = ControlFieldFactory.createFieldString(id, title, defaultValue, min, max).setFieldType("text").setFieldVariant("bigtext").setConstant();
				} else //
				if ("viewTitle".equals(type)) {
					final String defaultValue = Base.getString(attributes, "text", "no text");
					field = ControlFieldFactory.createFieldString(id, "", defaultValue).setConstant();
				} else //
				if ("viewString".equals(type)) {
					final String defaultValue = Base.getString(attributes, "text", "default", "no text here");
					field = ControlFieldFactory.createFieldString(id, title, defaultValue).setConstant();
				} else {
					field = ControlFieldFactory.createField(type, attributes);
				}
				final String hint = Base.getString(attributes, "hint", "").trim();
				if (hint.length() > 0) {
					field.setFieldHint(hint);
				}
				try {
					result.addField(field);
				} catch (final Throwable e) {
					throw new RuntimeException("Error adding field #" + i + " to a group (type=" + type + "):\n" + Dom.toXmlReadable(definition), e);
				}
			}
			return result;
		}
		throw new IllegalArgumentException("Unknown fieldset definition type: " + definitionType);
	}
	
	/**
	 * @param fieldset
	 * @return fieldset
	 */
	static ControlFieldset<?> createFieldsetConstant(final ControlFieldset<?> fieldset) {
		
		
		if (fieldset == null) {
			throw new IllegalArgumentException("Base fieldset is null!");
		}
		final ControlFieldset<?> result = ControlFieldset.createFieldset().setAttributes(fieldset.getAttributes());
		final int length = fieldset.size();
		for (int i = 0; i < length; ++i) {
			final ControlField field = fieldset.get(i);
			if (Base.getBoolean(field.getAttributes(), "constant", false)) {
				result.addField(field);
			} else {
				final ControlField replacement = field.cloneField().setConstant();
				result.addField(replacement);
			}
		}
		return result;
	}
	
	/**
	 * @param id
	 * @return fieldset
	 */
	static ControlFieldset<?> createFieldset(final String id) {
		
		
		return new FieldsetDefault(id);
	}
	
	/**
	 * @param base
	 * @param tail
	 * @return fieldset
	 */
	static ControlFieldset<?> createFieldset(final ControlFieldset<?> base, final ControlFieldset<?> tail) {
		
		
		if (base == null) {
			throw new IllegalArgumentException("Base fieldset is null!");
		}
		if (tail == null) {
			throw new IllegalArgumentException("Tail fieldset is null!");
		}
		final boolean replace;
		final BaseObject tailAttributes = tail.getAttributes();
		if (tailAttributes != null && !tailAttributes.baseIsPrimitive()) {
			final String merge = Base.getString(tailAttributes, "merge", "default");
			if ("replace".equals(merge)) {
				return tail;
			}
			replace = "default".equals(merge);
		} else {
			replace = false;
		}
		final ControlFieldset<?> result = ControlFieldset.createFieldset();
		{
			final BaseObject baseAttributes = base.getAttributes();
			if (baseAttributes != null) {
				result.setAttributes(baseAttributes);
			}
			if (tailAttributes != null) {
				result.setAttributes(tailAttributes);
			}
		}
		final ControlField[] tailFields = tail.toArray(new ControlField[tail.size()]);
		final int lengthBase = base.size();
		for (int i = 0; i < lengthBase; ++i) {
			final ControlField current = base.get(i);
			final String currentKey = current.getKey();
			ControlField replacement = null;
			for (int j = tailFields.length - 1; j >= 0; j--) {
				final ControlField candidate = tailFields[j];
				if (candidate == null) {
					continue;
				}
				if (currentKey.equals(candidate.getKey())) {
					final BaseObject attributes = new BaseNativeObject();
					final BaseObject currentAttributes = current.getAttributes();
					if (currentAttributes != null && !currentAttributes.baseIsPrimitive()) {
						attributes.baseDefineImportAllEnumerable(currentAttributes);
					}
					final BaseObject candidateAttributes = candidate.getAttributes();
					if (candidateAttributes != null && !candidateAttributes.baseIsPrimitive()) {
						attributes.baseDefineImportAllEnumerable(candidateAttributes);
					}
					replacement = current.cloneField();
					replacement.setAttributes(attributes);
					tailFields[j] = replace
						? null
						: replacement;
					break;
				}
			}
			
			if (replacement == null) {
				result.addField(current);
			} else //
			if (replace) {
				result.addField(replacement);
			}
		}
		result.addFields(tailFields);
		return result;
	}
	
	/**
	 * @return fieldset
	 */
	static ControlFieldset<?> createFieldset() {
		
		
		return new FieldsetDefault();
	}
}
