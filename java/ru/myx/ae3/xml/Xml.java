/*
 * Created on 25.04.2006
 */
package ru.myx.ae3.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseArrayDynamic;
import ru.myx.ae3.base.BaseDate;
import ru.myx.ae3.base.BaseLazyCompilationString;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseMap;
import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferBuffer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.common.BodyAccessBinary;
import ru.myx.ae3.common.BodyAccessCharacter;
import ru.myx.ae3.common.Value;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.extra.External;
import ru.myx.ae3.extra.ExternalHandler;
import ru.myx.ae3.flow.Flow;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.help.Dom;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.produce.BaseProduceReferenceCached;
import ru.myx.ae3.produce.Reproducible;
import ru.myx.ae3.transform.Transform;
import ru.myx.util.Base64;
import ru.myx.util.ComparatorFast;
import ru.myx.util.FifoQueueBuffered;

/** @author myx */
public final class Xml {

	private static final Comparator<Object> COMPARATOR_XML = new ComparatorFast();

	private static final int SAX_PARSER_LIMIT_FACTOR = 4;

	private static final FifoQueueBuffered<XmlToMapContentHandler>[] SAX_PARSERS = Convert.Array.toAny(new FifoQueueBuffered[]{//
			new FifoQueueBuffered<XmlToMapContentHandler>(Xml.SAX_PARSER_LIMIT_FACTOR), //
			new FifoQueueBuffered<XmlToMapContentHandler>(Xml.SAX_PARSER_LIMIT_FACTOR), //
			new FifoQueueBuffered<XmlToMapContentHandler>(Xml.SAX_PARSER_LIMIT_FACTOR), //
			new FifoQueueBuffered<XmlToMapContentHandler>(Xml.SAX_PARSER_LIMIT_FACTOR), //
	});

	private static final int SAX_PARSERS_MASK = Xml.SAX_PARSERS.length - 1;

	private static final String[] LIST_ITEM_NAME = {
			"item", "li", "element"
	};

	private static int saxParserIndex = 0;

	private static final boolean cleanStringReadable(final CharSequence string) {

		boolean prevSpace = false;
		for (int i = string.length() - 1; i >= 0; --i) {
			final char c = string.charAt(i);
			if (c == 32) {
				if (prevSpace) {
					return false;
				}
				prevSpace = true;
			} else //
			if (c < 33 || c > 127
			/** || c == 47 || c == 58 */
					|| c == 34 || c == 38) {
				return false;
			} else {
				prevSpace = false;
			}
		}
		return true;
	}

	/** @param doc
	 * @param target
	 * @param value
	 * @throws DOMException */
	private static final void createCData(//
			final Document doc,
			final Element target,
			final String value//
	) throws DOMException {

		assert Format.Xml.isValidCharacterData(value) : "Illegal CDATA characters: " + Format.Describe.toEcmaSource(value, "");
		target.setAttribute("type", "cdata");
		target.appendChild(doc.createCDATASection(value));
	}

	private static final Map<Object, String> createLookup() {

		return new TreeMap<>(Xml.COMPARATOR_XML);
	}

	private static final String getListItemName(final BaseObject o) {

		for (final String name : Xml.LIST_ITEM_NAME) {
			if (o.baseGetOwnProperty(name) == null) {
				return name;
			}
		}
		return "li-" + Engine.createGuid();
	}

	/** @param e
	 * @param classExplicit
	 *            null or override 'class' attribute
	 * @param namespace
	 * @param handler
	 * @param attachment
	 * @return */
	public static final BaseObject toBase(//
			final Element e,
			final String classExplicit,
			final String namespace,
			final ExternalHandler handler,
			final Object attachment//
	) {

		final String cls = classExplicit == null
			? e.getAttribute("class")
			: classExplicit;
		if (cls != null && cls.length() > 1) {
			final char first = cls.charAt(0);
			final char second = cls.charAt(1);
			if (first == 's' && second == 't' && cls.equals("string") && e.getAttribute("id").length() == 0) {
				final String type = e.getAttribute("type");
				if ("empty".equals(type)) {
					return BaseString.EMPTY;
				}
				return Base.forString(Dom.innerValue(e, namespace));
			}
			if (first == 's' && second == 'c' && cls.equals("script")) {
				final String type = e.getAttribute("type");
				final LanguageImpl language = Evaluate.getLanguageImpl(type);
				if (language == null) {
					return Base.forString(Dom.innerValue(e, namespace));
				}
				final String identity = "xml-map element";
				final String source = Dom.innerValue(e, namespace);
				return Engine.MODE_SIZE || source.length() > Transfer.BUFFER_SMALL
					? new BaseLazyCompilationString(language, identity, source)
					: Evaluate.compileProgramSilent(language, identity, source);
			}
			if (first == 's' && second == 'e' && cls.equals("serialized")) {
				final String type = e.getAttribute("type");
				final String encoding = e.getAttribute("encoding");
				if ("base64".equals(encoding)) {
					final byte[] array = Base64.decode(Dom.innerTextValue(e), true);
					final TransferCopier copier;
					{
						if (array.length <= Transfer.BUFFER_MEDIUM) {
							copier = Transfer.wrapCopier(array);
						} else {
							final TransferCollector collector = Transfer.createCollector();
							collector.getTarget().absorbArray(array, 0, array.length);
							copier = collector.toBinary();
						}
					}
					final BaseMap attributes = new BaseNativeObject("Content-Type", type);
					return Base.forUnknown(Transform.materialize(Object.class, type, attributes, copier.nextCopy()));
				}
				return null;
			}
			if (first == 'd' && second == 'o' && "double".equals(cls)) {
				return Base.forDouble(Convert.Any.toDouble(e.getFirstChild().getNodeValue(), Double.NaN));
			}
			if (first == 'f' && second == 'l' && "float".equals(cls)) {
				return Base.forDouble(Convert.Any.toDouble(e.getFirstChild().getNodeValue(), Double.NaN));
			}
			if (first == 'i' && second == 'n' && "integer".equals(cls)) {
				return Base.forLong(ru.myx.ae3.help.Convert.Any.toLong(e.getFirstChild().getNodeValue(), 0L));
			}
			if (first == 'l' && second == 'o' && "long".equals(cls)) {
				return Base.forLong(ru.myx.ae3.help.Convert.Any.toLong(e.getFirstChild().getNodeValue(), 0L));
			}
			if (first == 'u' && second == 'n' && cls.equals("undefined")) {
				return BaseObject.UNDEFINED;
			}
			if (first == 'n' && second == 'u' && cls.equals("null")) {
				return BaseObject.NULL;
			}
			if (first == 'n' && second == 'u' && cls.equals("number")) {
				final String type = e.getAttribute("type");
				if ("double".equals(type) || "float".equals(type)) {
					return Base.forDouble(Convert.Any.toDouble(e.getFirstChild().getNodeValue(), Double.NaN));
				}
				return Base.forLong(ru.myx.ae3.help.Convert.Any.toLong(e.getFirstChild().getNodeValue(), 0L));
			}
			if (first == 'd' && second == 'a' && cls.equals("date")) {
				final String date = e.getAttribute("value");
				if (date == null) {
					return Base.forDateMillis(0L);
				}
				if (date.equalsIgnoreCase("NOW")) {
					return BaseDate.NOW;
				}
				if (date.equalsIgnoreCase("UNDEFINED")) {
					return BaseDate.UNKNOWN;
				}
				return Base.forDateMillis(Long.parseLong(e.getAttribute("value")));
			}
			if (first == 'm' && second == 'a' && cls.equals("map")) {
				return Xml.toMap(e, namespace, BaseObject.createObject(), null, null);
			}
			if (first == 'l' && second == 'i' && cls.equals("list")) {
				return Xml.toList(e, namespace, BaseObject.createArray(), null, null);
			}
			if (first == 'b' && second == 'o' && cls.equals("boolean")) {
				return e.getFirstChild().getNodeValue().trim().equals("true")
					? BaseObject.TRUE
					: BaseObject.FALSE;
			}
			if (first == 'b' && second == 'y' && cls.equals("bytes") || first == 'b' && second == 'u' && cls.equals("buffer")
					|| first == 'c' && second == 'o' && cls.equals("copier")) {
				final String type = e.getAttribute("type");
				if ("empty".equals(type)) {
					return TransferCopier.NUL_COPIER;
				}
				if ("base64".equals(type)) {
					final byte[] array = Base64.decode(Dom.innerTextValue(e), true);
					if (array.length <= Transfer.BUFFER_MEDIUM) {
						return Transfer.wrapCopier(array);
					}
					final TransferCollector collector = Transfer.createCollector();
					collector.getTarget().absorbArray(array, 0, array.length);
					return collector.toBinary();
				}
				if ("reference".equals(type)) {
					final String identity = e.getAttribute("reference");
					if (handler == null) {
						return Transfer.wrapCopier(("COPIER{" + identity + "}").getBytes());
					}
					final External extra;
					try {
						extra = handler.getExternal(attachment, identity);
					} catch (final RuntimeException ex) {
						throw ex;
					} catch (final Exception ex) {
						throw new RuntimeException(ex);
					}
					return extra == null
						? null
						: Base.forUnknown(extra.toBinary());
				}
				return null;
			}
			if (first == 'e' && second == 'x' && cls.equals("extra")) {
				final String identity = e.getAttribute("identifier");
				if (handler == null) {
					return Base.forString("EXTERNAL{" + identity + "}");
				}
				try {
					return Base.forUnknown(handler.getExternal(attachment, identity));
				} catch (final RuntimeException ex) {
					throw ex;
				} catch (final Exception ex) {
					throw new RuntimeException(ex);
				}
			}
			if (first == 'r' && second == 'e' && cls.equals("restorable")) {
				final String factoryIdentity = e.getAttribute("factory");
				final String restoreParameter = e.getAttribute("parameter");
				return new BaseProduceReferenceCached(factoryIdentity, restoreParameter);
			}
			if (first == 'f' && second == 'i' && cls.equals("fieldset")) {
				return ControlFieldset.materializeFieldset(e);
			}
			if (first == 'm' && second == 'e' && cls.equals("message")) {
				String type = "base64";
				String title = "untitled";
				String owner = "XML/MATERIALIZE";
				final BaseObject attributes = new BaseNativeObject();
				final NamedNodeMap xmlAttributes = e.getAttributes();
				for (int i = xmlAttributes.getLength() - 1; i >= 0; --i) {
					final Node current = xmlAttributes.item(i);
					final String name = current.getNodeName();
					if ("class".equals(name)) {
						continue;
					}
					if ("message_owner".equals(name)) {
						owner = current.getNodeValue();
					} else //
					if ("message_title".equals(name)) {
						title = current.getNodeValue();
					} else //
					if ("message_type".equals(name)) {
						type = current.getNodeValue();
					} else {
						attributes.baseDefine(name, current.getNodeValue());
					}
				}
				if ("empty".equals(type)) {
					return Flow.binary(owner, title, attributes, TransferCopier.NUL_COPIER);
				}
				if ("base64".equals(type)) {
					final byte[] array = Base64.decode(Dom.innerTextValue(e), true);
					final TransferCopier copier;
					{
						if (array.length <= Transfer.BUFFER_MEDIUM) {
							copier = Transfer.wrapCopier(array);
						} else {
							final TransferCollector collector = Transfer.createCollector();
							collector.getTarget().absorbArray(array, 0, array.length);
							copier = collector.toBinary();
						}
					}
					return Flow.binary(owner, title, attributes, copier);
				}
				if ("text".equals(type)) {
					final String text = Dom.innerTextValue(e);
					return Flow.character(owner, title, attributes, text);
				}
				if ("sequence".equals(type)) {
					final List<BaseMessage> list = new ArrayList<>();
					final NodeList nl = e.getChildNodes();
					for (int i = nl.getLength() - 1; i >= 0; --i) {
						final Node n = nl.item(i);
						if (n.getNodeType() == Node.ELEMENT_NODE) {
							list.add((BaseMessage) Xml.toBase((Element) n, null, namespace, handler, attachment));
						}
					}
					return Flow.sequence(owner, title, attributes, list.toArray(new BaseMessage[list.size()]));
				}
				return Flow.character(owner, title, attributes, "");
			}
		}
		BaseObject result = null;
		BaseObject map = null;
		final NodeList nl = e.getChildNodes();
		final int length = nl.getLength();
		for (int i = 0; i < length; ++i) {
			final Node n = nl.item(i);
			if (n.getNodeType() == Node.ELEMENT_NODE) {
				BaseArrayDynamic<Object> order = null;
				if (map == null) {
					result = map = new BaseNativeObject();
					if (e.hasAttributes()) {
						final NamedNodeMap attributes = e.getAttributes();
						for (int j = attributes.getLength() - 1; j >= 0; j--) {
							final Node attribute = attributes.item(j);
							final String name = attribute.getNodeName();
							if ("key".equals(name) && "param".equals(e.getNodeName())) {
								continue;
							}
							final String value = attribute.getNodeValue();
							if ("class".equals(name) && "map".equals(value)) {
								continue;
							}
							if ("ordered".equals(name) && "true".equals(value)) {
								order = BaseObject.createArray();
								map.baseDefine("$ORDER", order);
								continue;
							}
							map.baseDefine(name, value);
						}
					}
				}
				final Element nElement = (Element) n;
				final String name = Dom.getNodeName(nElement, namespace);
				if (name == null) {
					continue;
				}
				final BaseObject object = Xml.toBase(nElement, null, namespace, handler, attachment);
				final BaseObject existing = map.baseGet(name, BaseObject.UNDEFINED);
				assert existing != null : "NULL java value";
				if (existing == BaseObject.UNDEFINED) {
					if (order != null) {
						order.add(Base.forString(name));
					}
					map.baseDefine(name, object);
				} else {
					if (existing instanceof XmlMultiple) {
						((XmlMultiple) existing).add(object);
					} else {
						final XmlMultiple l = new XmlMultiple();
						l.add(existing);
						l.add(object);
						map.baseDefine(name, l);
					}
				}
			}
		}
		if (result != null) {
			return result;
		}
		if (e.hasAttribute("value") && e.getAttributes().getLength() == 1) {
			return Base.forString(e.getAttribute("value"));
		}
		if (e.getAttribute("number").equals("true")) {
			return e.getAttribute("integer").equals("true")
				? Base.forLong(Convert.Any.toLong(Dom.innerTextValue(e), 0L))
				: Base.forDouble(Convert.Any.toDouble(Dom.innerTextValue(e), Double.NaN));
		}
		if (e.getAttribute("blob").equals("true")) {
			if (e.getAttribute("encoding").equals("base64")) {
				final byte[] array = Base64.decode(Dom.innerTextValue(e), true);
				if (array.length <= Transfer.BUFFER_MEDIUM) {
					return Transfer.wrapCopier(array);
				}
				final TransferCollector collector = Transfer.createCollector();
				collector.getTarget().absorbArray(array, 0, array.length);
				return collector.toBinary();
			}
			return null;
		}
		if (e.hasAttributes() && !e.hasChildNodes()) {
			final NamedNodeMap attributes = e.getAttributes();
			map = new BaseNativeObject();
			for (int j = attributes.getLength() - 1; j >= 0; j--) {
				final Node attribute = attributes.item(j);
				map.baseDefine(attribute.getNodeName(), attribute.getNodeValue());
			}
			return map;
		}
		return Base.forString(Dom.innerValue(e, namespace));
	}

	/** @param identity
	 * @param xml
	 * @param uri
	 * @param handler
	 * @param attachment
	 * @return map */
	public static final BaseObject toBase(//
			final String identity,
			final CharSequence xml,
			final String uri,
			final ExternalHandler handler,
			final Object attachment//
	) {

		if (xml == null || xml.length() == 0) {
			return BaseObject.UNDEFINED;
		}
		/** BOM, http://en.wikipedia.org/wiki/Byte_Order_Mark
		 *
		 * fails the parser otherwise */
		final String xmlString = xml.charAt(0) == 0xFEFF
			? xml.toString().substring(1).trim()
			: xml.toString().trim();
		if (xmlString.length() == 0) {
			return BaseObject.UNDEFINED;
		}
		final FifoQueueBuffered<XmlToMapContentHandler> parserQueue = Xml.SAX_PARSERS[--Xml.saxParserIndex & Xml.SAX_PARSERS_MASK];
		XmlToMapContentHandler reader;
		synchronized (parserQueue) {
			reader = parserQueue.pollFirst();
		}
		final BaseObject target;
		try {
			if (reader == null) {
				reader = new XmlToMapContentHandler();
			}
			reader.reuse(identity, new StringReader(xmlString), uri, null, handler, attachment);
			target = reader.parse();

			/** oops - there's no way to reset parser (internal sax parser) at the moment */
			synchronized (parserQueue) {
				parserQueue.offerLast(reader);
			}
			/** end of oops. */

		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException("Identity: " + identity, e);
		} finally {
			/** oops - there's no way to reset parser (internal sax parser) at the moment so the
			 * code which is belong here was moved upwards ^^^^ until better times */
		}
		return target;
	}

	/** @param identity
	 * @param xml
	 * @param charsetOverride
	 * @param uri
	 * @param handler
	 * @param attachment
	 * @return map */
	public static final BaseObject toBase(//
			final String identity,
			final TransferCopier xml,
			final Charset charsetOverride,
			final String uri,
			final ExternalHandler handler,
			final Object attachment//
	) {

		if (xml == null || xml.length() == 0) {
			return BaseObject.UNDEFINED;
		}
		final FifoQueueBuffered<XmlToMapContentHandler> parserQueue = Xml.SAX_PARSERS[--Xml.saxParserIndex & Xml.SAX_PARSERS_MASK];
		XmlToMapContentHandler reader;
		synchronized (parserQueue) {
			reader = parserQueue.pollFirst();
		}
		final BaseObject target;
		try {
			if (reader == null) {
				reader = new XmlToMapContentHandler();
			}
			if (charsetOverride == null) {
				try (final InputStream in = xml.nextInputStream()) {
					if (in.read() == '<' && in.read() == '?') {
						reader.reuse(identity, xml.nextInputStream(), uri, null, handler, attachment);
					} else {
						final InputStreamReader input = new InputStreamReader(xml.nextInputStream(), Engine.CHARSET_DEFAULT);
						reader.reuse(identity, input, uri, null, handler, attachment);
					}
				}
			} else {
				final InputStreamReader input = new InputStreamReader(xml.nextInputStream(), charsetOverride);
				reader.reuse(identity, input, uri, null, handler, attachment);
			}
			target = reader.parse();

			/** oops - there's no way to reset parser (internal sax parser) at the moment. So on
			 * error we are not even trying to reuse it. */
			synchronized (parserQueue) {
				parserQueue.offerLast(reader);
			}
			/** end of oops. */

		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException("Identity: " + identity, e);
		} finally {
			/** oops - there's no way to reset parser (internal sax parser) at the moment so the
			 * code which is belong here was moved upwards ^^^^ until better times */
		}
		return target;
	}

	private static final Element toElement(//
			final Document doc,
			final Element target,
			final String value,
			final boolean readable//
	) {

		if (readable) {
			if (value.length() == 0) {
				// empty
			} else //
			if (value.length() < Transfer.BUFFER_SMALL && Xml.cleanStringReadable(value)) {
				target.appendChild(doc.createTextNode(value));
			} else {
				target.setAttribute("class", "string");
				Xml.createCData(doc, target, value);
			}
		} else {
			target.setAttribute("class", "string");
			if (value.length() == 0) {
				target.setAttribute("type", "empty");
			} else //
			if (value.length() < 80 && Format.Xml.isValidAttributeValue(value)) {
				target.setAttribute("type", "inner");
				target.appendChild(doc.createTextNode(value));
			} else {
				Xml.createCData(doc, target, value);
			}
		}
		return target;
	}

	private static final Element toElement(//
			final Document doc,
			final Element target,
			final String namePrefix,
			final String name,
			final BaseMessage data,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength,
			final Map<Object, String> lookup//
	) {

		target.setAttribute("class", "message");
		target.setAttribute("message_owner", data.getEventTypeId());
		target.setAttribute("message_title", data.getTitle());
		final BaseObject attributes = data.getAttributes();
		if (attributes != null && Base.hasKeys(attributes)) {
			for (final Iterator<String> iterator = Base.keys(attributes); iterator.hasNext();) {
				final String key = iterator.next();
				if (key.startsWith("message_")) {
					continue;
				}
				final BaseObject value = attributes.baseGet(key, BaseObject.UNDEFINED);
				assert value != null : "NULL java value!";
				if (value == BaseObject.UNDEFINED) {
					continue;
				}
				target.setAttribute(key, value.baseToJavaString());
			}
		}
		if (data.isEmpty()) {
			target.setAttribute("message_type", "empty");
			return target;
		}
		if (data.isSequence()) {
			target.setAttribute("message_type", "sequence");
			final BaseMessage[] messages = data.getSequence();
			for (int i = messages.length - 1; i >= 0; --i) {
				target.appendChild(Xml.toNode(doc, Dom.createElement(doc, name), namePrefix, name, messages[i], readable, handler, attachment, maxFieldLength, lookup));
			}
			return target;
		}
		if (data.isCharacter()) {
			final CharSequence text = ((BodyAccessCharacter) data).getText();
			if (text.length() < Transfer.BUFFER_MEDIUM && Format.Xml.isValidCharacterData(text)) {
				target.setAttribute("message_type", "text");
				target.appendChild(doc.createCDATASection(text.toString()));
				return target;
			}
		}
		{
			target.setAttribute("message_type", "base64");
			final TransferCopier binary = data.toBinary().getBinary();
			target.appendChild(doc.createCDATASection(Base64.encode(binary.nextDirectArray(), readable)));
			return target;
		}
	}

	private static final Element toElement(//
			final Document doc,
			final Element target,
			final String namePrefix,
			final String mapName,
			final Map<?, ?> map,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength,
			final Map<Object, String> lookup//
	) {

		if (readable) {
			if (map.isEmpty()) {
				target.setAttribute("class", "map");
			}
		} else {
			target.setAttribute("class", "map");
		}
		final String newPrefix = namePrefix == null
			? ""
			: namePrefix + mapName + '/';
		final Object order = map.get("$ORDER");
		if (order != null) {
			if (order.getClass() == String.class) {
				if (map.size() == 2) {
					final String name = (String) order;
					target.setAttribute("ordered", "true");
					final Map<Object, ?> source = Convert.Any.toAny(map);
					final Node child = Xml.toNode(doc, Dom.createElement(doc, name), newPrefix, name, source.get(name), readable, handler, attachment, maxFieldLength, lookup);
					if (child != null) {
						target.appendChild(child);
					}
					return target;
				}
			}
			if (order instanceof Collection<?>) {
				final Collection<?> sequence = (Collection<?>) order;
				if (sequence.size() == map.size() - 1) {
					target.setAttribute("ordered", "true");
					final Map<Object, ?> source = Convert.Any.toAny(map);
					for (final Object current : sequence) {
						final String name = String.valueOf(current);
						final Node child = Xml
								.toNode(doc, Dom.createElement(doc, name), newPrefix, name, source.get(current), readable, handler, attachment, maxFieldLength, lookup);
						if (child != null) {
							target.appendChild(child);
						}
					}
					return target;
				}
			}
		}
		for (final Map.Entry<?, ?> current : map.entrySet()) {
			final String name = String.valueOf(current.getKey());
			final Node child = Xml.toNode(doc, Dom.createElement(doc, name), newPrefix, name, current.getValue(), readable, handler, attachment, maxFieldLength, lookup);
			if (child != null) {
				target.appendChild(child);
			}
		}
		return target;
	}

	/** @param doc
	 * @param mapName
	 * @param object
	 * @param readable
	 * @return element */
	public static final Element toElement(//
			final Document doc,
			final String mapName,
			final BaseObject object,
			final boolean readable//
	) {

		final Element element = doc.createElement(mapName);
		final Node node = Xml.toNode(doc, element, null, mapName, object, readable, null, null, 0, null);
		assert node == element;
		/** TODO : return element; */
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return (Element) node;
		}
		{
			final BaseArray array = object.baseArray();
			if (array != null) {
				element.setAttribute("class", "list");
				element.appendChild(node);
				return element;
			}
		}
		throw new IllegalStateException("Cannot convert node (" + node + ") to an element, source=" + object);
	}

	/** @param rootName
	 * @param object
	 * @param readable
	 * @return element */
	public static final Element toElement(//
			final String rootName,
			final BaseObject object,
			final boolean readable//
	) {

		final Document doc = Dom.createDocument();
		return Xml.toElement(doc, rootName, object, readable);
	}

	/** @param rootName
	 * @param object
	 * @param readable
	 * @param handler
	 * @param attachment
	 * @param maxFieldLength
	 * @return element */
	public static final Element toElement(//
			final String rootName,
			final BaseObject object,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength//
	) {

		final Document doc = Dom.createDocument();
		final Element element = Dom.createElement(doc, rootName);
		final Node node = Xml.toNode(doc, element, null, rootName, object, readable, handler, attachment, maxFieldLength, Xml.createLookup());
		assert node == element;
		/** TODO: return target; */
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			return (Element) node;
		}
		{
			final BaseArray array = object.baseArray();
			if (array != null) {
				element.setAttribute("class", "list");
				element.appendChild(node);
				return element;
			}
		}
		throw new IllegalStateException("Cannot convert node (" + node + ") to an element, source=" + object);
	}

	private static final Element toElementExtraSerialized(//
			final Document doc,
			final Element target,
			final String namePrefix,
			final String name,
			final Object object,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength//
	) {

		try {
			final StringBuilder contentTypeBuffer = new StringBuilder(32);
			final TransferCollector collector = Transfer.createCollector();
			if (!Transform.serialize(new XmlSerializationRequest(contentTypeBuffer, object, collector))) {
				throw new RuntimeException("Cannot convert an object, class=" + object.getClass().getName() + "!");
			}
			/** copier is supposed to be closed */
			final TransferCopier copier = collector.toCloneFactory();
			if (handler != null && copier.length() > maxFieldLength) {
				try {
					final String extra = handler.putExternal(attachment, namePrefix + name, contentTypeBuffer.toString(), copier);
					if (extra == null) {
						return null;
					}
					target.setAttribute("class", "extra");
					target.setAttribute("identifier", extra);
					return target;
				} catch (final RuntimeException e) {
					throw e;
				} catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
			target.setAttribute("class", "serialized");
			target.setAttribute("type", contentTypeBuffer.toString());
			target.setAttribute("encoding", "base64");
			target.appendChild(doc.createCDATASection(Base64.encode(copier.nextDirectArray(), true)));
			return target;
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static final BaseList<Object> toList(//
			final Element e,
			final String namespace,
			final BaseList<Object> target,
			final ExternalHandler handler,
			final Object attachment//
	) {

		assert target != null : "Target object must not be NULL";
		if (e == null) {
			return target;
		}
		BaseArrayDynamic<Object> order = null;
		String arrayTarget = Dom.getNodeName(e, namespace);
		if (e.hasAttributes()) {
			final NamedNodeMap attributes = e.getAttributes();
			for (int j = attributes.getLength() - 1; j >= 0; j--) {
				final Node attribute = attributes.item(j);
				final String name = attribute.getNodeName();
				if ("key".equals(name) && "param".equals(e.getNodeName())) {
					continue;
				}
				final String value = attribute.getNodeValue();
				if ("class".equals(name)) {
					if ("map".equals(value)) {
						continue;
					}
					if ("list".equals(value)) {
						continue;
					}
				}
				if ("ordered".equals(name) && "true".equals(value)) {
					order = BaseObject.createArray();
					target.baseDefine("$ORDER", order);
					continue;
				}
				if (name.equals("items")) {
					arrayTarget = value;
					continue;
				}
				if (name.equals("xmlns") || name.startsWith("xmlns:")) {
					continue;
				}
				target.baseDefine(name, value);
			}
		}
		for (Node targetNode = e.getFirstChild(); targetNode != null; targetNode = targetNode.getNextSibling()) {
			if (targetNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element targetElement = (Element) targetNode;
				final String name = Dom.getNodeName(targetElement, namespace);
				if (name == null) {
					continue;
				}
				final BaseObject object = Xml.toBase(targetElement, null, namespace, handler, attachment);
				if (arrayTarget != null && name.equals(arrayTarget)) {
					target.add(object);
					continue;
				}
				final BaseObject existing = target.baseGet(name, BaseObject.UNDEFINED);
				assert existing != null : "NULL java value";
				if (existing == BaseObject.UNDEFINED) {
					if (order != null) {
						order.add(Base.forString(name));
					}
					target.baseDefine(name, object);
				} else {
					if (existing instanceof XmlMultiple) {
						((XmlMultiple) existing).add(object);
					} else {
						final XmlMultiple l = new XmlMultiple();
						l.add(existing);
						l.add(object);
						target.baseDefine(name, l);
					}
				}
			}
		}
		return target;
	}

	private static final <T extends BaseObject> T toMap(//
			final Element e,
			final String namespace,
			final T target,
			final ExternalHandler handler,
			final Object attachment//
	) {

		assert target != null : "Target object must not be NULL";
		if (e == null) {
			return target;
		}
		BaseArrayDynamic<Object> order = null;
		if (e.hasAttributes()) {
			final NamedNodeMap attributes = e.getAttributes();
			for (int j = attributes.getLength() - 1; j >= 0; j--) {
				final Node attribute = attributes.item(j);
				final String name = attribute.getNodeName();
				if ("key".equals(name) && "param".equals(e.getNodeName())) {
					continue;
				}
				final String value = attribute.getNodeValue();
				if ("class".equals(name)) {
					if ("map".equals(value)) {
						continue;
					}
					if ("list".equals(value)) {
						continue;
					}
				}
				if ("ordered".equals(name) && "true".equals(value)) {
					order = BaseObject.createArray();
					target.baseDefine("$ORDER", order);
					continue;
				}
				if (name.equals("xmlns") || name.startsWith("xmlns:")) {
					continue;
				}
				target.baseDefine(name, value);
			}
		}
		for (Node targetNode = e.getFirstChild(); targetNode != null; targetNode = targetNode.getNextSibling()) {
			if (targetNode.getNodeType() == Node.ELEMENT_NODE) {
				final Element targetElement = (Element) targetNode;
				final String name = Dom.getNodeName(targetElement, namespace);
				if (name == null) {
					continue;
				}
				final BaseObject object = Xml.toBase(targetElement, null, namespace, handler, attachment);
				final BaseObject existing = ((BaseObject) target).baseGet(name, BaseObject.UNDEFINED);
				assert existing != null : "NULL java value";
				if (existing == BaseObject.UNDEFINED) {
					if (order != null) {
						order.add(Base.forString(name));
					}
					target.baseDefine(name, object);
				} else {
					if (existing instanceof XmlMultiple) {
						((XmlMultiple) existing).add(object);
					} else {
						final XmlMultiple l = new XmlMultiple();
						l.add(existing);
						l.add(object);
						target.baseDefine(name, l);
					}
				}
			}
		}
		return target;
	}

	/** @param <T>
	 * @param identity
	 * @param xml
	 * @param uri
	 * @param target
	 * @param handler
	 * @param attachment
	 * @return map */
	public static final <T extends BaseObject> T toMap(//
			final String identity,
			final CharSequence xml,
			final String uri,
			final T target,
			final ExternalHandler handler,
			final Object attachment//
	) {

		assert target != null : "Target object must not be NULL";
		if (xml == null || xml.length() == 0) {
			return target;
		}
		/** BOM, http://en.wikipedia.org/wiki/Byte_Order_Mark
		 *
		 * fails the parser otherwise */
		final String xmlString = xml.charAt(0) == 0xFEFF
			? xml.toString().substring(1).trim()
			: xml.toString().trim();
		if (xmlString.length() == 0) {
			return target;
		}
		final FifoQueueBuffered<XmlToMapContentHandler> parserQueue = Xml.SAX_PARSERS[--Xml.saxParserIndex & Xml.SAX_PARSERS_MASK];
		XmlToMapContentHandler reader;
		synchronized (parserQueue) {
			reader = parserQueue.pollFirst();
		}
		try {
			if (reader == null) {
				reader = new XmlToMapContentHandler();
			}
			reader.reuse(identity, new StringReader(xmlString), uri, target, handler, attachment);
			reader.parse();

			/** oops - there's no way to reset parser (internal sax parser) at the moment */
			synchronized (parserQueue) {
				parserQueue.offerLast(reader);
			}
			/** end of oops. */

		} catch (final SAXException e) {
			/** TODO: replace with one used in exception stack - can just limit lengths of strings
			 * (even better: based on report levels and java assertions) */
			throw new RuntimeException(
					xmlString.length() < 256
						? "Identity: " + identity + ", xml=" + Format.Ecma.string(xmlString)
						: "Identity: " + identity, //
					e);
		} catch (ParserConfigurationException | IOException e) {
			throw new RuntimeException("Identity: " + identity, e);
		} finally {
			/** oops - there's no way to reset parser (internal sax parser) at the moment so the
			 * code which is belong here was moved upwards ^^^^ until better times */
		}
		return target;
	}

	/** @param <T>
	 * @param identity
	 * @param xml
	 * @param charsetOverride
	 * @param uri
	 * @param target
	 * @param handler
	 * @param attachment
	 * @return map */
	public static final <T extends BaseObject> T toMap(//
			final String identity,
			final TransferCopier xml,
			final Charset charsetOverride,
			final String uri,
			final T target,
			final ExternalHandler handler,
			final Object attachment//
	) {

		assert target != null : "Target object must not be NULL";
		if (xml == null || xml.length() == 0) {
			return target;
		}
		final FifoQueueBuffered<XmlToMapContentHandler> parserQueue = Xml.SAX_PARSERS[--Xml.saxParserIndex & Xml.SAX_PARSERS_MASK];
		XmlToMapContentHandler reader;
		synchronized (parserQueue) {
			reader = parserQueue.pollFirst();
		}
		try {
			if (charsetOverride == null) {
				try (final InputStream in = xml.nextInputStream()) {
					if (in.read() == '<' && in.read() == '?') {
						if (reader == null) {
							reader = new XmlToMapContentHandler();
						}
						reader.reuse(identity, xml.nextInputStream(), uri, target, handler, attachment);
					} else {
						if (reader == null) {
							reader = new XmlToMapContentHandler();
						}
						reader.reuse(identity, new InputStreamReader(xml.nextInputStream(), Engine.CHARSET_DEFAULT), uri, target, handler, attachment);
					}
				}
			} else {
				if (reader == null) {
					reader = new XmlToMapContentHandler();
				}
				reader.reuse(identity, new InputStreamReader(xml.nextInputStream(), charsetOverride), uri, target, handler, attachment);
			}
			reader.parse();

			/** oops - there's no way to reset parser (internal sax parser) at the moment */
			synchronized (parserQueue) {
				parserQueue.offerLast(reader);
			}
			/** end of oops. */

		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException("Identity: " + identity, e);
		} finally {
			/** oops - there's no way to reset parser (internal sax parser) at the moment so the
			 * code which is belong here was moved upwards ^^^^ until better times */
		}
		return target;
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	private static final Element toNode(//
			final Document doc,
			final Element target,
			final String namePrefix,
			final String name,
			final BaseObject o,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength,
			final Map<Object, String> lookup//
	) {

		assert o != null : "NULL java value";
		if (o.baseIsPrimitive()) {
			if (o == BaseObject.NULL) {
				target.setAttribute("class", "null");
				return target;
			}
			if (o == BaseObject.UNDEFINED) {
				target.setAttribute("class", "undefined");
				return target;
			}
			if (o.baseIsPrimitiveString()) {
				final String string = o.baseToJavaString();
				if (handler != null && string.length() > maxFieldLength) {
					final String extra;
					{
						final String ready = lookup.get(o);
						if (ready == null) {
							try {
								extra = handler.putExternal(attachment, namePrefix + name, "text/plain", Transfer.wrapCopier(string.getBytes(Engine.CHARSET_UTF8)));
							} catch (final RuntimeException e) {
								throw e;
							} catch (final Exception e) {
								throw new RuntimeException(e);
							}
						} else {
							extra = ready;
						}
					}
					if (extra == null) {
						return null;
					}
					target.setAttribute("class", "extra");
					target.setAttribute("identifier", extra);
					return target;
				}
				return Xml.toElement(doc, target, string, readable);
			}
			if (o.baseIsPrimitiveBoolean()) {
				target.setAttribute("class", "boolean");
				target.appendChild(
						doc.createTextNode(
								o.baseToJavaBoolean()
									? "true"
									: "false"));
				return target;
			}
			if (o.baseIsPrimitiveInteger()) {
				target.setAttribute("class", "number");
				target.setAttribute("type", "long");
				target.appendChild(doc.createTextNode(Long.toString(o.baseToJavaLong())));
				return target;
			}
			if (o.baseIsPrimitiveNumber()) {
				target.setAttribute("class", "number");
				target.setAttribute("type", "double");
				target.appendChild(doc.createTextNode(Double.toString(o.baseToNumber().doubleValue())));
				return target;
			}
		}
		if (o instanceof Reproducible) {
			final Reproducible restorable = (Reproducible) o;
			if (restorable.restoreFactoryIdentity() != null) {
				target.setAttribute("class", "restorable");
				target.setAttribute("factory", restorable.restoreFactoryIdentity());
				target.setAttribute("parameter", restorable.restoreFactoryParameter());
				return target;
			}
		}
		if (handler != null && o instanceof External) {
			final External external = (External) o;
			if (handler.checkIssuer(external.getRecordIssuer())) {
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", external.getIdentity());
				return target;
			}
		}
		if (o instanceof BaseMessage) {
			final BaseMessage message = (BaseMessage) o;
			if (message.isEmpty()) {
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			if (message.isBinary()) {
				if (handler != null && ((BodyAccessBinary) message).getBinaryContentLength() > maxFieldLength) {
					return Xml.toElementExtraSerialized(doc, target, namePrefix, name, message, handler, attachment, maxFieldLength);
				}
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			if (message.isFile()) {
				if (handler != null && message.getFile().length() > maxFieldLength) {
					return Xml.toElementExtraSerialized(doc, target, namePrefix, name, message, handler, attachment, maxFieldLength);
				}
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			if (message.isCharacter()) {
				if (handler != null && ((BodyAccessCharacter) message).getCharacterContentLength() > maxFieldLength) {
					return Xml.toElementExtraSerialized(doc, target, namePrefix, name, message, handler, attachment, maxFieldLength);
				}
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			return Xml.toElementExtraSerialized(
					doc, //
					target,
					namePrefix,
					name,
					message,
					handler,
					attachment,
					maxFieldLength);
		}
		if (o instanceof TransferBuffer) {
			if (handler != null && ((TransferBuffer) o).remaining() > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "ae2/binary", ((TransferBuffer) o).toBinary());
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			target.setAttribute("class", "buffer");
			target.setAttribute("type", "base64");
			target.appendChild(doc.createCDATASection(Base64.encode(((TransferBuffer) o).toDirectArray(), true)));
			return target;
		}
		if (o instanceof TransferCopier) {
			if (handler != null && ((TransferCopier) o).length() > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "ae2/binary", (TransferCopier) o);
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			target.setAttribute("class", "copier");
			target.setAttribute("type", "base64");
			target.appendChild(
					doc.createCDATASection(
							Base64.encode(
									((TransferCopier) o)//
											.nextCopy()//
											.toDirectArray(),
									true)));
			return target;
		}
		if (o instanceof ControlFieldset<?>) {
			ControlFieldset.serializeFieldset((ControlFieldset<?>) o, target, readable);
			target.setAttribute("class", "fieldset");
			return target;
		}
		{
			final BaseArray array = o.baseArray();
			if (array != null) {
				target.setAttribute("class", "list");
				final int count = array.length();
				if (count > 0) {
					final String item = Xml.getListItemName(o);
					target.setAttribute("items", item);
					for (int i = 0; i < count; ++i) {
						final Element element = Dom.createElement(doc, item);
						final Node child = Xml
								.toNode(doc, element, namePrefix, name, array.baseGet(i, BaseObject.UNDEFINED), readable, handler, attachment, maxFieldLength, lookup);
						assert element == child;
						target.appendChild(element);
					}
				}
				return target;
			}
		}
		if (o instanceof BaseMap || o instanceof Map<?, ?> //
		/* || Base.hasKeys( o, BaseObject.PROTOTYPE ) */) {
			final Iterator<String> iterator = Base.keys(o, BaseObject.PROTOTYPE);
			if (!readable || !iterator.hasNext()) {
				target.setAttribute("class", "map");
			}
			final String newPrefix = namePrefix == null
				? ""
				: namePrefix + name + '/';
			int count = 0;
			final DocumentFragment fragment = doc.createDocumentFragment();
			for (; iterator.hasNext();) {
				final String key = iterator.next();
				count++;
				final BaseObject value = o.baseGet(key, BaseObject.UNDEFINED);
				final Element element = Dom.createElement(doc, key);
				final Node child = Xml.toNode(doc, element, newPrefix, key, value, readable, handler, attachment, maxFieldLength, lookup);
				assert child == element : "expecting child be equal to original element: key=" + key + ",child=" + child + ", element=" + element + ", value=" + value;
				fragment.appendChild(child);
			}
			if (handler != null && count > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						final Element extraElement = doc.createElement("extra_map");
						extraElement.appendChild(fragment);
						final TransferCopier binary = readable
							? Dom.toXmlReadableBinary(extraElement)
							: Dom.toXmlCompactBinary(extraElement);
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "text/xml", binary);
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			target.appendChild(fragment);
			return target;
		}
		{
			final Object base = o.baseValue();
			if (base != o && base != null) {
				return Xml.toNode(doc, target, namePrefix, name, base, readable, handler, attachment, maxFieldLength, lookup);
			}
		}
		if (o instanceof Date) {
			final Date date = (Date) o;
			target.setAttribute("class", "date");
			target.setAttribute(
					"value",
					date == BaseDate.NOW
						? "now"
						: date == BaseDate.UNKNOWN
							? "undefined"
							: Long.toString(date.getTime()));
			target.appendChild(doc.createTextNode(date.toString()));
			return target;
		}
		{
			final String value = o.toString();
			target.setAttribute("class", "string");
			target.setAttribute("original", o.getClass().getName());
			if (value.length() == 0) {
				target.setAttribute("type", "empty");
			} else //
			if (value.length() < 128 && Format.Xml.isValidAttributeValue(value)) {
				target.setAttribute("type", "inner");
				target.appendChild(doc.createTextNode(value));
			} else {
				Xml.createCData(doc, target, value);
			}
			return target;
		}
	}

	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	// //////////////////////////////////////////////////////////////////////////////////////////
	private static final Element toNode(//
			final Document doc,
			final Element target,
			final String namePrefix,
			final String name,
			final Object o,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength,
			final Map<Object, String> lookup//
	) {

		if (o == null || o == BaseObject.NULL) {
			target.setAttribute("class", "null");
			return target;
		}
		if (o == BaseObject.UNDEFINED) {
			target.setAttribute("class", "undefined");
			return target;
		}
		if (o == BaseObject.TRUE) {
			target.setAttribute("class", "boolean");
			target.appendChild(doc.createTextNode("true"));
			return target;
		}
		if (o == BaseObject.FALSE) {
			target.setAttribute("class", "boolean");
			target.appendChild(doc.createTextNode("false"));
			return target;
		}
		final Class<?> cls = o.getClass();
		if (cls == String.class) {
			if (handler != null && ((String) o).length() > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "text/plain", Transfer.wrapCopier(((String) o).getBytes(Engine.CHARSET_UTF8)));
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			return Xml.toElement(doc, target, (String) o, readable);
		}
		if (cls == Boolean.class || cls == boolean.class) {
			target.setAttribute("class", "boolean");
			target.appendChild(
					doc.createTextNode(
							((Boolean) o).booleanValue()
								? "true"
								: "false"));
			return target;
		}
		if (cls == Integer.class || cls == int.class) {
			target.setAttribute("class", "number");
			target.setAttribute("type", "integer");
			target.appendChild(doc.createTextNode(Integer.toString(((Integer) o).intValue())));
			return target;
		}
		if (cls == Long.class || cls == long.class) {
			target.setAttribute("class", "number");
			target.setAttribute("type", "long");
			target.appendChild(doc.createTextNode(Long.toString(((Long) o).longValue())));
			return target;
		}
		if (cls == Float.class || cls == float.class) {
			target.setAttribute("class", "number");
			target.setAttribute("type", "float");
			target.appendChild(doc.createTextNode(Float.toString(((Float) o).floatValue())));
			return target;
		}
		if (cls == Double.class || cls == double.class) {
			target.setAttribute("class", "number");
			target.setAttribute("type", "double");
			target.appendChild(doc.createTextNode(Double.toString(((Double) o).doubleValue())));
			return target;
		}
		if (cls == byte[].class) {
			if (handler != null && ((byte[]) o).length > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "ae2/bytes", Transfer.wrapCopier((byte[]) o));
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			target.setAttribute("class", "bytes");
			target.setAttribute("type", "base64");
			target.appendChild(doc.createCDATASection(Base64.encode((byte[]) o, true)));
			return target;
		}
		if (o instanceof Reproducible) {
			final Reproducible restorable = (Reproducible) o;
			if (restorable.restoreFactoryIdentity() != null) {
				target.setAttribute("class", "restorable");
				target.setAttribute("factory", restorable.restoreFactoryIdentity());
				target.setAttribute("parameter", restorable.restoreFactoryParameter());
				return target;
			}
		}
		if (handler != null && o instanceof External) {
			final External external = (External) o;
			if (handler.checkIssuer(external.getRecordIssuer())) {
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", external.getIdentity());
				return target;
			}
		}
		if (o instanceof CharSequence) {
			if (handler != null && ((CharSequence) o).length() > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "text/plain", Transfer.wrapCopier(o.toString().getBytes(Engine.CHARSET_UTF8)));
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			return Xml.toElement(doc, target, o.toString(), readable);
		}
		if (o instanceof Value<?>) {
			final Object base = ((Value<?>) o).baseValue();
			if (base == null) {
				target.setAttribute("class", "null");
				return target;
			}
			if (base != o) {
				return Xml.toNode(doc, target, namePrefix, name, base, readable, handler, attachment, maxFieldLength, lookup);
			}
		}
		if (o instanceof Map<?, ?>) {
			if (handler != null && ((Map<?, ?>) o).size() > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						final TransferCopier binary = Xml.toXmlBinary(namePrefix + name, (Map<?, ?>) o, readable, handler, attachment, maxFieldLength);
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "text/xml", binary);
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			return Xml.toElement(doc, target, namePrefix, name, (Map<?, ?>) o, readable, handler, attachment, maxFieldLength, lookup);
		}
		if (o instanceof List<?>) {
			if (o instanceof ControlFieldset<?>) {
				ControlFieldset.serializeFieldset((ControlFieldset<?>) o, target, readable);
				target.setAttribute("class", "fieldset");
				return target;
			}
			target.setAttribute("class", "list");
			final List<?> list = (List<?>) o;
			final int count = list.size();
			if (count > 0) {
				target.setAttribute("items", "item");
			}
			for (int i = 0; i < count; ++i) {
				final Element element = Dom.createElement(doc, "item");
				final Node child = Xml.toNode(doc, element, namePrefix, name, list.get(i), readable, handler, attachment, maxFieldLength, lookup);
				assert element == child;
				target.appendChild(child);
			}
			return target;
		}
		if (o instanceof Collection<?>) {
			target.setAttribute("class", "list");
			final Collection<?> collection = (Collection<?>) o;
			if (!collection.isEmpty()) {
				target.setAttribute("items", "item");
			}
			for (final Object object : collection) {
				final Element element = Dom.createElement(doc, "item");
				final Node child = Xml.toNode(doc, element, namePrefix, name, object, readable, handler, attachment, maxFieldLength, lookup);
				assert element == child;
				target.appendChild(child);
			}
			return target;
		}
		if (o instanceof TransferBuffer) {
			if (handler != null && ((TransferBuffer) o).remaining() > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "ae2/binary", ((TransferBuffer) o).toBinary());
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			target.setAttribute("class", "buffer");
			target.setAttribute("type", "base64");
			target.appendChild(doc.createCDATASection(Base64.encode(((TransferBuffer) o).toDirectArray(), true)));
			return target;
		}
		if (o instanceof TransferCopier) {
			if (handler != null && ((TransferCopier) o).length() > maxFieldLength) {
				final String extra;
				{
					final String ready = lookup.get(o);
					if (ready == null) {
						try {
							extra = handler.putExternal(attachment, namePrefix + name, "ae2/binary", (TransferCopier) o);
						} catch (final RuntimeException e) {
							throw e;
						} catch (final Exception e) {
							throw new RuntimeException(e);
						}
					} else {
						extra = ready;
					}
				}
				if (extra == null) {
					return null;
				}
				target.setAttribute("class", "extra");
				target.setAttribute("identifier", extra);
				return target;
			}
			target.setAttribute("class", "copier");
			target.setAttribute("type", "base64");
			target.appendChild(
					doc.createCDATASection(
							Base64.encode(
									((TransferCopier) o)//
											.nextCopy()//
											.toDirectArray(),
									true)));
			return target;
		}
		if (o instanceof BaseMessage) {
			final BaseMessage message = (BaseMessage) o;
			if (message.isEmpty()) {
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			if (message.isBinary()) {
				if (handler != null && ((BodyAccessBinary) message).getBinaryContentLength() > maxFieldLength) {
					return Xml.toElementExtraSerialized(doc, target, namePrefix, name, message, handler, attachment, maxFieldLength);
				}
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			if (message.isFile()) {
				if (handler != null && message.getFile().length() > maxFieldLength) {
					return Xml.toElementExtraSerialized(doc, target, namePrefix, name, message, handler, attachment, maxFieldLength);
				}
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			if (message.isCharacter()) {
				if (handler != null && ((BodyAccessCharacter) message).getCharacterContentLength() > maxFieldLength) {
					return Xml.toElementExtraSerialized(doc, target, namePrefix, name, message, handler, attachment, maxFieldLength);
				}
				return Xml.toElement(doc, target, namePrefix, name, message, readable, handler, attachment, maxFieldLength, lookup);
			}
			return Xml.toElementExtraSerialized(
					doc, //
					target,
					namePrefix,
					name,
					message,
					handler,
					attachment,
					maxFieldLength);
		}
		if (o instanceof Object[]) {
			target.setAttribute("class", "list");
			final Object[] array = (Object[]) o;
			final int count = array.length;
			if (count > 0) {
				target.setAttribute("items", "item");
			}
			for (int i = 0; i < count; ++i) {
				final Element element = Dom.createElement(doc, "item");
				final Node child = Xml.toNode(doc, element, namePrefix, name, array[i], readable, handler, attachment, maxFieldLength, lookup);
				assert element == child;
				target.appendChild(child);
			}
			return target;
		}
		if (o instanceof Date) {
			final Date date = (Date) o;
			target.setAttribute("class", "date");
			target.setAttribute(
					"value",
					date == BaseDate.NOW
						? "now"
						: date == BaseDate.UNKNOWN
							? "undefined"
							: Long.toString(date.getTime()));
			target.appendChild(doc.createTextNode(date.toString()));
			return target;
		}
		if (o instanceof Number) {
			final Number number = (Number) o;
			target.setAttribute("class", "number");
			target.setAttribute(
					"type",
					number instanceof Integer
						? "integer"
						: number instanceof Long
							? "long"
							: number instanceof Float
								? "float"
								: "double");
			target.appendChild(doc.createTextNode(number.toString()));
			return target;
		}
		{
			final String value = o.toString();
			target.setAttribute("class", "string");
			target.setAttribute("original", o.getClass().getName());
			if (value.length() == 0) {
				target.setAttribute("type", "empty");
			} else //
			if (value.length() < 128 && Format.Xml.isValidAttributeValue(value)) {
				target.setAttribute("type", "inner");
				target.appendChild(doc.createTextNode(value));
			} else {
				Xml.createCData(doc, target, value);
			}
			return target;
		}
	}

	/** @param rootName
	 * @param object
	 * @param readable
	 * @param handler
	 * @param attachment
	 * @param maxFieldLength
	 * @return string */
	public static final TransferCopier toXmlBinary(//
			final String rootName,
			final BaseObject object,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength//
	) {

		final Document doc = Dom.createDocument();
		final Element element = Dom.createElement(doc, rootName);
		final Node node = Xml.toNode(doc, element, null, rootName, object, readable, handler, attachment, maxFieldLength, Xml.createLookup());
		assert element == node;
		return readable
			? Dom.toXmlReadableBinary(element)
			: Dom.toXmlCompactBinary(element);
	}

	private static final TransferCopier toXmlBinary(//
			final String name,
			final Map<?, ?> map,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength//
	) {

		if (map == null || map.isEmpty()) {
			final int length = name.length();
			if (length > 0 && length < 128 && Format.Xml.isValidName(name)) {
				return Transfer.wrapCopier(('<' + name + " class=\"map\"/>").getBytes(Engine.CHARSET_UTF8));
			}
			final Document doc = Dom.createDocument();
			final Element root = doc.createElement("param");
			root.setAttribute("key", name);
			root.setAttribute("class", "map");
			return readable
				? Dom.toXmlReadableBinary(root)
				: Dom.toXmlCompactBinary(root);
		}
		{
			final Document doc = Dom.createDocument();
			final Element root = Xml.toElement(doc, Dom.createElement(doc, name), null, name, map, readable, handler, attachment, maxFieldLength, Xml.createLookup());
			return readable
				? Dom.toXmlReadableBinary(root)
				: Dom.toXmlCompactBinary(root);
		}
	}

	/** @param rootName
	 * @param object
	 * @param readable
	 * @return string */
	public static final String toXmlString(//
			final String rootName,
			final BaseObject object,
			final boolean readable//
	) {

		assert object != null : "NULL java value";
		final Document doc = Dom.createDocument();
		final Element element = Dom.createElement(doc, rootName);
		final Node node = Xml.toNode(doc, element, null, rootName, object, readable, null, null, 0, null);
		assert element == node;
		return readable
			? Dom.toXmlReadable(element)
			: Dom.toXmlCompact(element);
	}

	/** @param rootName
	 * @param map
	 * @param readable
	 * @param handler
	 * @param attachment
	 * @param maxFieldLength
	 * @return string */
	public static final String toXmlString(//
			final String rootName,
			final BaseObject map,
			final boolean readable,
			final ExternalHandler handler,
			final Object attachment,
			final int maxFieldLength//
	) {

		final Document doc = Dom.createDocument();
		final Element element = Dom.createElement(doc, rootName);
		final Node node = Xml.toNode(doc, element, null, rootName, map, readable, handler, attachment, maxFieldLength, Xml.createLookup());
		assert node == element;
		doc.appendChild(element);
		return readable
			? Dom.toXmlReadable(element)
			: Dom.toXmlCompact(element);
	}

	private Xml() {

		// empty
	}
}
