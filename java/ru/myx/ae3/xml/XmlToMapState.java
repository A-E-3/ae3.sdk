/*
 * Created on 24.04.2006
 */
package ru.myx.ae3.xml;

import org.xml.sax.Attributes;

import ru.myx.ae3.Engine;
import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseArray;
import ru.myx.ae3.base.BaseDate;
import ru.myx.ae3.base.BaseLazyCompilationString;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseMessage;
import ru.myx.ae3.base.BaseNativeObject;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.base.BaseString;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.control.field.ControlFieldFactory;
import ru.myx.ae3.control.fieldset.ControlFieldset;
import ru.myx.ae3.eval.Evaluate;
import ru.myx.ae3.eval.LanguageImpl;
import ru.myx.ae3.extra.External;
import ru.myx.ae3.flow.Flow;
import ru.myx.ae3.help.Convert;
import ru.myx.ae3.help.Format;
import ru.myx.ae3.produce.BaseProduceReferenceCached;
import ru.myx.ae3.transform.Transform;

/** @author myx */
enum XmlToMapState {
	/**
	 *
	 */
	BOOLEAN {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			if (handler.stringBuilder.length() == 0) {
				handler.put(BaseObject.FALSE);
			} else {
				handler.put(
						Convert.Any.toBoolean(handler.stringBuilder.toString(), false)
							? BaseObject.TRUE
							: BaseObject.FALSE);
				handler.stringBuilder.setLength(0);
			}
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}
	},
	/**
	 *
	 */
	CONTENTLESS {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element {name=" + name + ", attrs=" + Format.Describe.toEcmaSource(attributes, "") + "}, handler=" + handler);
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}
	},
	/**
	 *
	 */
	COPIER64 {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.fillBase64(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			handler.flushBase64();
			final TransferCopier copier = handler.targetCollector.toBinary();
			handler.put(copier);
			handler.targetCollector.reset();
			handler.targetName = String.valueOf(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}
	},
	/**
	 *
	 */
	FIELDSET {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			final BaseObject data = Convert.Any.toAny(handler.pop());
			final ControlFieldset<?> fieldset = Convert.Any.toAny(handler.peek());
			fieldset.add(ControlFieldFactory.createField(Base.getString(data, "class", "string"), data));
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			if ("field".equals(name)) {
				final BaseObject data = new BaseNativeObject();
				for (int i = attributes.getLength() - 1; i >= 0; --i) {
					data.baseDefine(attributes.getLocalName(i), attributes.getValue(i));
				}
				handler.push(data);
				handler.push(handler.state);
				handler.push(handler.order);
				handler.push(handler.targetName);
				handler.push(handler.targetMap);
				handler.targetName = null;
				handler.order = null;
				handler.targetMap = data;
				handler.state = XmlToMapState.MAP;
				assert handler.state != null : "state should never be NULL";
				return;
			}
			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			handler.pop();
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}
	},
	/**
	 *
	 */
	LIST {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			XmlToMapState.MAP.onChildElementStart(handler, name, attributes);
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final XmlMultiple multiple = (XmlMultiple) handler.targetMap;
			{
				final BaseObject list = multiple.baseGet(handler.targetName, BaseObject.UNDEFINED);
				assert list != null : "NULL java value";
				if (list.baseIsPrimitive()) {
					if (list != BaseObject.UNDEFINED) {
						multiple.putAppend(list);
					}
				} else {
					final BaseArray array = list.baseArray();
					if (array != null) {
						final int length = array.length();
						for (int i = 0; i < length; ++i) {
							multiple.putAppend(array.baseGet(i, null));
						}
					} else {
						multiple.putAppend(list);
					}
				}
				multiple.baseDelete(handler.targetName);
			}

			handler.targetMap = Convert.Any.toAny(handler.pop());
			handler.targetName = (String) handler.pop();
			handler.order = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}
	},
	/**
	 *
	 */
	MAP {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String localName, final Attributes attributes) {

			final String targetName;
			final int extraAttribute;
			if ("param".equals(localName)) {
				final String key = attributes.getValue("key");
				if (key != null) {
					targetName = key;
					extraAttribute = 1;
				} else {
					targetName = localName;
					extraAttribute = 0;
				}
			} else {
				targetName = localName;
				extraAttribute = 0;
			}
			final String cls = attributes.getValue("class");
			if (cls != null && cls.length() > 1) {
				final char first = cls.charAt(0);
				if (first == 's') {
					final char second = cls.charAt(1);
					if (second == 't' && cls.equals("string") && attributes.getValue("id") == null) {
						final String type = attributes.getValue("type");
						if ("empty".equals(type)) {
							handler.put(targetName, BaseString.EMPTY);
							handler.push(handler.state);
							handler.state = XmlToMapState.CONTENTLESS;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						handler.push(handler.state);
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.STRING_COLLECT;
						assert handler.state != null : "state should never be NULL";
						return;
					}
					if (second == 'c' && cls.equals("script")) {
						handler.push(handler.state);
						handler.push(handler.targetParam1);
						handler.targetParam1 = attributes.getValue("type");
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.SCRIPT;
						assert handler.state != null : "state should never be NULL";
						return;
					}
					if (second == 'e' && cls.equals("serialized")) {
						final String encoding = attributes.getValue("encoding");
						if (!"base64".equals(encoding)) {
							throw new IllegalArgumentException("Unknown encoding method: " + encoding);
						}
						handler.push(handler.state);
						handler.push(handler.targetParam1);
						handler.targetParam1 = attributes.getValue("type");
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.prepareCollector();
						handler.state = XmlToMapState.SERIALIZED64;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'd') {
					final char second = cls.charAt(1);
					if (second == 'a' && cls.equals("date")) {
						final String value = attributes.getValue("value");
						if (value == null || value.length() == 0) {
							throw new IllegalArgumentException("No value for date");
						}
						handler.put(
								targetName,
								value.equalsIgnoreCase("NOW")
									? BaseDate.NOW
									: value.equalsIgnoreCase("UNDEFINED")
										? BaseObject.UNDEFINED
										: Base.forDateMillis(Long.parseLong(value)));
						handler.push(handler.state);
						handler.state = XmlToMapState.CONTENTLESS;
						assert handler.state != null : "state should never be NULL";
						return;
					}
					if (second == 'o' && cls.equals("double")) {
						handler.push(handler.state);
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.NUMBER_FLOATING;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'e') {
					final char second = cls.charAt(1);
					if (second == 'x' && cls.equals("extra")) {
						final String identity = attributes.getValue("identifier");
						if (handler.handler == null) {
							handler.put(targetName, Base.forString("EXTERNAL{" + identity + "}"));
						} else {
							try {
								handler.put(targetName, Base.forUnknown(handler.handler.getExternal(handler.attachment, identity)));
							} catch (final RuntimeException ex) {
								throw ex;
							} catch (final Exception ex) {
								throw new RuntimeException(ex);
							}
						}
						handler.push(handler.state);
						handler.state = XmlToMapState.CONTENTLESS;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'r') {
					final char second = cls.charAt(1);
					if (second == 'e' && cls.equals("restorable")) {
						final String factoryIdentity = attributes.getValue("factory");
						final String restoreParameter = attributes.getValue("parameter");
						handler.put(targetName, new BaseProduceReferenceCached(factoryIdentity, restoreParameter));
						handler.push(handler.state);
						handler.state = XmlToMapState.CONTENTLESS;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'f') {
					final char second = cls.charAt(1);
					if (second == 'l' && cls.equals("float")) {
						handler.push(handler.state);
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.NUMBER_FLOATING;
						assert handler.state != null : "state should never be NULL";
						return;
					}
					if (second == 'i' && cls.equals("fieldset")) {
						final ControlFieldset<?> fieldset = ControlFieldset.createFieldset();
						for (int i = attributes.getLength() - 1; i >= 0; --i) {
							final String name = attributes.getLocalName(i);
							if ("key".equals(name) && "param".equals(localName)) {
								continue;
							}
							fieldset.setAttribute(name, attributes.getValue(i));
						}
						handler.put(targetName, fieldset);
						handler.push(handler.state);
						handler.push(fieldset);
						handler.state = XmlToMapState.FIELDSET;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'l') {
					final char second = cls.charAt(1);
					if (second == 'o' && cls.equals("long")) {
						handler.push(handler.state);
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.NUMBER_INTEGER;
						assert handler.state != null : "state should never be NULL";
						return;
					}
					if (second == 'i' && cls.equals("list")) {
						handler.push(handler.state);
						handler.push(handler.order);
						handler.push(handler.targetName);
						handler.push(handler.targetMap);
						String items = attributes.getValue("items");
						if (items == null || items.length() == 0) {
							items = targetName;
						}
						// handler.push( handler.targetParam1 );
						// handler.targetParam1 = items;
						handler.targetName = items; // targetName;
						final XmlMultiple multiple = new XmlMultiple();
						handler.put(targetName, multiple);
						handler.order = null;
						handler.targetMap = multiple;
						handler.state = XmlToMapState.LIST;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'i') {
					final char second = cls.charAt(1);
					if (second == 'n' && cls.equals("integer")) {
						handler.push(handler.state);
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.NUMBER_INTEGER;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'b' || first == 'c') {
					final char second = cls.charAt(1);
					if (second == 'u' && cls.equals("buffer") || second == 'y' && cls.equals("bytes") || second == 'o' && cls.equals("copier")) {
						final String type = attributes.getValue("type");
						if ("empty".equals(type)) {
							handler.put(targetName, TransferCopier.NUL_COPIER);
							handler.push(handler.state);
							handler.state = XmlToMapState.CONTENTLESS;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						if ("base64".equals(type)) {
							handler.push(handler.state);
							handler.push(handler.targetName);
							handler.targetName = targetName;
							handler.prepareCollector();
							handler.state = XmlToMapState.COPIER64;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						if ("reference".equals(type)) {
							final String identity = attributes.getValue("reference");
							if (handler.handler == null) {
								handler.put(targetName, Transfer.wrapCopier(("COPIER{" + identity + "}").getBytes()));
							} else {
								final External extra;
								try {
									extra = handler.handler.getExternal(handler.attachment, identity);
								} catch (final RuntimeException e) {
									throw e;
								} catch (final Exception e) {
									throw new RuntimeException(e);
								}
								if (extra != null) {
									handler.put(targetName, Base.forUnknown(extra.toBinary()));
								}
							}
							handler.push(handler.state);
							handler.state = XmlToMapState.CONTENTLESS;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						throw new IllegalArgumentException("Unknown encoding method: " + type);
					}
					if (second == 'o' && cls.equals("boolean")) {
						handler.push(handler.state);
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.BOOLEAN;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'm') {
					final char second = cls.charAt(1);
					if (second == 'a' && cls.equals("map")) {
						BaseList<Object> order = null;
						final BaseObject map = new BaseNativeObject();
						for (int i = attributes.getLength() - 1; i >= 0; --i) {
							final String name = attributes.getLocalName(i);
							if ("key".equals(name) && "param".equals(localName)) {
								continue;
							}
							final String value = attributes.getValue(i);
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
						handler.push(handler.state);
						handler.push(handler.order);
						handler.push(handler.targetName);
						handler.push(handler.targetMap);
						handler.targetName = targetName;
						handler.targetMap = map;
						if (handler.map == null) {
							/** it is put here - not in the end! be sure not to put in the end if it
							 * was put here!
							 *
							 * cause otherwise embedded item will become root one! */
							handler.map = map;
						}
						handler.order = order;
						handler.state = XmlToMapState.MAP; // for refactoring
						assert handler.state != null : "state should never be NULL";
						return;
					}
					if (second == 'e' && cls.equals("message")) {
						String type = "base64";
						final BaseObject map = new BaseNativeObject();
						for (int i = attributes.getLength() - 1; i >= 0; --i) {
							final String name = attributes.getLocalName(i);
							if ("class".equals(name)) {
								continue;
							}
							final String value = attributes.getValue(i);
							if ("message_type".equals(name)) {
								type = value;
								continue;
							}
							map.baseDefine(name, value);
						}
						if ("base64".equals(type)) {
							handler.push(handler.state);
							handler.push(handler.targetName);
							handler.push(handler.targetMap);
							handler.targetName = targetName;
							handler.targetMap = map;
							handler.prepareCollector();
							handler.state = XmlToMapState.MESSAGE64;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						if ("text".equals(type)) {
							handler.push(handler.state);
							handler.push(handler.targetName);
							handler.push(handler.targetMap);
							handler.targetName = targetName;
							handler.targetMap = map;
							handler.state = XmlToMapState.MESSAGE_TEXT;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						if ("sequence".equals(type)) {
							handler.push(handler.state);
							handler.push(handler.order);
							handler.push(handler.targetName);
							handler.push(handler.targetMap);
							handler.targetName = targetName;
							handler.order = null;
							handler.targetMap = map;
							handler.state = XmlToMapState.MESSAGE_SEQUENCE;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						if ("empty".equals(type)) {
							final String owner = Base.getString(map, "message_owner", "XML-SAX/MATERIALIZE");
							final String title = Base.getString(map, "message_title", "Untitled");
							map.baseDelete("message_owner");
							map.baseDelete("message_title");
							handler.put(targetName, Flow.character(owner, title, map, ""));
							handler.push(handler.state);
							handler.state = XmlToMapState.CONTENTLESS;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						throw new IllegalArgumentException("Unknown encoding method: " + type);
					}
				}
				if (first == 'u') {
					final char second = cls.charAt(1);
					if (second == 'n' && cls.equals("undefined")) {
						handler.put(targetName, BaseObject.UNDEFINED);
						handler.push(handler.state);
						handler.state = XmlToMapState.CONTENTLESS;
						assert handler.state != null : "state should never be NULL";
						return;
					}
				}
				if (first == 'n') {
					final char second = cls.charAt(1);
					if (second == 'u') {
						if (cls.equals("number")) {
							handler.push(handler.state);
							handler.push(handler.targetName);
							handler.targetName = targetName;
							final String type = attributes.getValue("type");
							if ("long".equals(type) || "integer".equals(type)) {
								handler.state = XmlToMapState.NUMBER_INTEGER;
								assert handler.state != null : "state should never be NULL";
								return;
							}
							handler.state = XmlToMapState.NUMBER_FLOATING;
							assert handler.state != null : "state should never be NULL";
							return;
						}
						if (cls.equals("null")) {
							handler.put(targetName, BaseObject.NULL);
							handler.push(handler.state);
							handler.state = XmlToMapState.CONTENTLESS;
							assert handler.state != null : "state should never be NULL";
							return;
						}
					}
				}
				// throw new UnsupportedOperationException("class=" + cls);
			}
			{
				final String value = attributes.getValue("value");
				if (value != null && value.length() > 0 && attributes.getLength() == 1 + extraAttribute) {
					handler.put(targetName, Base.forString(value));
					handler.push(handler.state);
					handler.state = XmlToMapState.CONTENTLESS;
					assert handler.state != null : "state should never be NULL";
					return;
				}
			}
			{
				final String number = attributes.getValue("number");
				if (number != null && number.length() > 0 && "true".equals(number)) {
					handler.push(handler.state);
					final String integer = attributes.getValue("integer");
					if ("true".equals(integer)) {
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.NUMBER_INTEGER;
					} else {
						handler.push(handler.targetName);
						handler.targetName = targetName;
						handler.state = XmlToMapState.NUMBER_FLOATING;
					}
					assert handler.state != null : "state should never be NULL";
					return;
				}
			}
			{
				final String blob = attributes.getValue("blob");
				if (blob != null && blob.length() > 0 && "true".equals(blob)) {
					final String encoding = attributes.getValue("encoding");
					if (!"base64".equals(encoding)) {
						throw new IllegalArgumentException("Unknown encoding method: " + encoding);
					}
					handler.push(handler.state);
					handler.push(handler.targetParam1);
					handler.targetParam1 = attributes.getValue("type");
					handler.push(handler.targetName);
					handler.targetName = targetName;
					handler.prepareCollector();
					handler.state = XmlToMapState.COPIER64;
					assert handler.state != null : "state should never be NULL";
					return;
				}
			}
			handler.push(handler.state);
			handler.push(handler.order);
			handler.push(handler.targetName);
			handler.push(handler.targetMap);
			handler.push(handler.stringBuilderUnknown);
			BaseList<Object> order = null;
			BaseObject map = null;
			for (int i = attributes.getLength() - 1; i >= 0; --i) {
				final String name = attributes.getLocalName(i);
				if ("key".equals(name) && "param".equals(localName)) {
					continue;
				}
				final String value = attributes.getValue(i);
				if ("class".equals(name) && "map".equals(value)) {
					continue;
				}
				if (map == null) {
					map = new BaseNativeObject();
				}
				if ("ordered".equals(name) && "true".equals(value)) {
					order = BaseObject.createArray();
					map.baseDefine("$ORDER", order);
					continue;
				}
				map.baseDefine(name, value);
			}
			handler.targetName = targetName;
			handler.targetMap = map;
			if (handler.map == null) {
				/** it is put here - not in the end! be sure not to put in the end if it was put
				 * here!
				 *
				 * cause otherwise embedded item will become root one! */
				handler.map = map;
			}

			handler.order = order;
			handler.stringBuilderUnknown = null;
			handler.state = XmlToMapState.UNKNOWN;
			assert handler.state != null : "state should never be NULL";
			return;
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final BaseObject current = handler.targetMap;
			handler.targetMap = Convert.Any.toAny(handler.pop());
			if (handler.map != current) {
				/** Put here only is it haven't been put before! */
				handler.put(current);
			}
			handler.targetName = (String) handler.pop();
			handler.order = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}
	},
	/**
	 *
	 */
	MESSAGE_SEQUENCE {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			XmlToMapState.MAP.onChildElementStart(handler, name, attributes);
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final BaseObject map = handler.targetMap;
			final BaseMessage[] sequence;
			{
				final BaseObject sequenceObject = map.baseGet(handler.targetName, BaseObject.UNDEFINED);
				assert sequenceObject != null : "NULL java value";
				final BaseArray array = sequenceObject.baseArray();
				if (array != null) {
					final int length = array.length();
					sequence = new BaseMessage[length];
					for (int i = 0; i < length; ++i) {
						sequence[i] = (BaseMessage) array.baseGet(i, null);
					}
				} else {
					sequence = new BaseMessage[]{
							(BaseMessage) sequenceObject
					};
				}
			}
			final String owner = Base.getString(map, "message_owner", "XML-SAX/MATERIALIZE");
			final String title = Base.getString(map, "message_title", "Untitled");
			map.baseDelete("message_owner");
			map.baseDelete("message_title");
			handler.targetMap = Convert.Any.toAny(handler.pop());
			handler.put(Flow.sequence(owner, title, map, sequence));
			handler.targetName = (String) handler.pop();
			handler.order = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}
	},
	/**
	 *
	 */
	MESSAGE_TEXT {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final BaseObject map = handler.targetMap;
			final String owner = Base.getString(map, "message_owner", "XML-SAX/MATERIALIZE");
			final String title = Base.getString(map, "message_title", "Untitled");
			map.baseDelete("message_owner");
			map.baseDelete("message_title");
			handler.targetMap = Convert.Any.toAny(handler.pop());
			handler.put(Flow.character(owner, title, map, handler.stringBuilder.toString()));
			handler.stringBuilder.setLength(0);
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}
	},
	/**
	 *
	 */
	MESSAGE64 {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.fillBase64(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final TransferCopier copier = handler.targetCollector.toBinary();
			final BaseObject map = handler.targetMap;
			final String owner = Base.getString(map, "message_owner", "XML-SAX/MATERIALIZE");
			final String title = Base.getString(map, "message_title", "Untitled");
			map.baseDelete("message_owner");
			map.baseDelete("message_title");
			handler.targetMap = Convert.Any.toAny(handler.pop());
			handler.put(Flow.binary(owner, title, map, copier));
			handler.targetCollector.reset();
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}
	},
	/**
	 *
	 */
	NUMBER_FLOATING {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			if (handler.stringBuilder.length() > 0) {
				handler.put(Base.forDouble(Convert.Any.toDouble(handler.stringBuilder.toString(), Double.NaN)));
				handler.stringBuilder.setLength(0);
			}
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}
	},
	/**
	 *
	 */
	NUMBER_INTEGER {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			if (handler.stringBuilder.length() > 0) {
				handler.put(Base.forLong(Convert.Any.toLong(handler.stringBuilder.toString(), 0L)));
				handler.stringBuilder.setLength(0);
			}
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}
	},
	/**
	 *
	 */
	ROOT {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String localName, final Attributes attributes) {

			handler.push(ROOT);
			handler.push(handler.order);
			handler.push(handler.targetName);
			handler.push(handler.targetMap);
			handler.state = MAP;
			return;
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// empty
		}
	},
	/**
	 *
	 */
	SCRIPT {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final LanguageImpl language = Evaluate.getLanguageImpl(handler.targetParam1);
			if (language == null) {
				handler.put(Base.forString(handler.stringBuilder.toString()));
			} else {
				final String description = handler.toState();
				final String identity = "xmat{" + description + '}';
				final String source = handler.stringBuilder.toString();
				handler.put(
						Engine.MODE_SIZE || source.length() > Transfer.BUFFER_SMALL
							? new BaseLazyCompilationString(language, identity, source)
							: Evaluate.compileProgramSilent(language, identity, source));
			}
			handler.stringBuilder.setLength(0);
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.targetParam1 = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}
	},
	/**
	 *
	 */
	SERIALIZED64 {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.fillBase64(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final TransferCopier copier = handler.targetCollector.toBinary();
			handler.put(Base.forUnknown(Transform.materialize(Object.class, handler.targetParam1, new BaseNativeObject("Content-Type", handler.targetParam1), copier.nextCopy())));
			handler.targetCollector.reset();
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.targetParam1 = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}
	},
	/**
	 *
	 */
	STRING_COLLECT {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			throw new IllegalStateException("Unexpected element!");
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final StringBuilder builder = handler.stringBuilder;
			if (builder.length() > 0) {
				handler.put(Base.forString(builder.toString()));
				builder.setLength(0);
			} else {
				handler.put(BaseString.EMPTY);
			}
			handler.targetName = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			handler.stringBuilder.append(buffer, offset, length);
		}
	},
	/**
	 *
	 */
	UNKNOWN {

		@Override
		public void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			if (handler.stringBuilderUnknown == null) {
				handler.stringBuilderUnknown = new StringBuilder(64);
			}
			handler.stringBuilderUnknown.append(buffer, offset, length);
		}

		@Override
		public void onChildElementEnd(final XmlToMapContentHandler handler) {

			// empty
		}

		@Override
		public void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes) {

			if (handler.targetMap == null) {
				handler.targetMap = new BaseNativeObject();
				if (handler.map == null) {
					handler.map = handler.targetMap;
				}
			}
			XmlToMapState.MAP.onChildElementStart(handler, name, attributes);
		}

		@Override
		public void onThisElementEnd(final XmlToMapContentHandler handler) {

			final StringBuilder builder = handler.stringBuilderUnknown;
			final BaseObject current = handler.targetMap;
			handler.stringBuilderUnknown = (StringBuilder) handler.pop();
			handler.targetMap = Convert.Any.toAny(handler.pop());
			if (builder == null) {
				handler.put(
						current == null
							? BaseString.EMPTY
							: current);
			} else {
				final String textual = builder.toString().trim();
				if (textual.length() == 0) {
					handler.put(
							current == null
								? BaseString.EMPTY
								: current);
					// Engine.trace("M=" + current);
				} else {
					handler.put(Base.forString(textual));
					// Engine.trace("T=" + textual);
				}
			}
			handler.targetName = (String) handler.pop();
			handler.order = Convert.Any.toAny(handler.pop());
			handler.state = (XmlToMapState) handler.pop();
			assert handler.state != null : "state should never be NULL";
			handler.state.onChildElementEnd(handler);
		}

		@Override
		public void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length) {

			// ignore
		}
	},;

	/** @param handler
	 * @param buffer
	 * @param offset
	 * @param length */
	public abstract void onCharacters(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length);

	/** @param handler */
	public abstract void onChildElementEnd(final XmlToMapContentHandler handler);

	/** @param handler
	 * @param name
	 * @param attributes */
	public abstract void onChildElementStart(final XmlToMapContentHandler handler, final String name, final Attributes attributes);

	/** @param handler */
	public abstract void onThisElementEnd(final XmlToMapContentHandler handler);

	/** @param handler
	 * @param buffer
	 * @param offset
	 * @param length */
	public abstract void onWhitespace(final XmlToMapContentHandler handler, final char[] buffer, final int offset, final int length);
}
