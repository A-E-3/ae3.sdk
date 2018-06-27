/*
 * Created on 24.04.2006
 */
package ru.myx.ae3.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import ru.myx.ae3.base.Base;
import ru.myx.ae3.base.BaseList;
import ru.myx.ae3.base.BaseObject;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.extra.ExternalHandler;
import ru.myx.ae3.help.Format;

class XmlToMapContentHandler extends InputSource implements ContentHandler, ErrorHandler, EntityResolver {
	
	
	// base 64 character scheme
	private static final char[] B64_STATICS = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
			'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '+', '/'
	};
	
	// this is the encoded character's values
	private static final byte[] MATRIX = new byte[256];
	static {
		// fill values so that the matrix holds every encoded characters
		// actual value at it's own position in the matrix
		for (int i = 0; i < XmlToMapContentHandler.B64_STATICS.length; ++i) {
			XmlToMapContentHandler.MATRIX[XmlToMapContentHandler.B64_STATICS[i]] = (byte) i;
		}
	}
	
	Object attachment;
	
	private final char[] decodeBuffer;
	
	private int decodeBufferSize;
	
	ExternalHandler handler;
	
	BaseObject map;
	
	BaseList<Object> order;
	
	// instance data
	private final byte[] readBuffer;
	
	private int readBufferIndex;
	
	private int readBufferSize;
	
	XmlToMapState state = null;
	
	final StringBuilder stringBuilder;
	
	StringBuilder stringBuilderUnknown = null;
	
	TransferCollector targetCollector = null;
	
	BaseObject targetMap = null;
	
	String targetName = null;
	
	String targetParam1 = null;
	
	private Object[] stack = new Object[128];
	
	private int stackPointer;
	
	private String uri;
	
	private String identity;
	
	private final XMLReader parser;
	
	private int skipCounter = 0;
	
	private String skipDefaultURI = null;
	
	XmlToMapContentHandler() throws SAXException {
		this.stringBuilder = new StringBuilder(1024);
		this.parser = XMLReaderFactory.createXMLReader();
		this.parser.setEntityResolver(this);
		this.parser.setContentHandler(this);
		this.parser.setErrorHandler(this);
		this.readBuffer = new byte[3];
		this.decodeBuffer = new char[4];
		this.readBufferIndex = 0;
		this.readBufferSize = 0;
		this.stackPointer = 0;
	}
	
	@Override
	public final void characters(final char[] ch, final int start, final int length) {
		
		
		this.state.onCharacters(this, ch, start, length);
	}
	
	@Override
	public final void endDocument() {
		
		
		this.setByteStream(null);
		this.setCharacterStream(null);
		this.setSystemId(null);
		this.handler = null;
		this.attachment = null;
		this.readBufferIndex = 0;
		this.readBufferSize = 0;
		this.stackPointer = 0;
		this.targetMap = null;
		this.targetName = null;
	}
	
	@Override
	public final void endElement(final String uri, final String localName, final String qName) {
		
		
		if (this.skipCounter > 0) {
			this.skipCounter--;
			return;
		}
		this.state.onThisElementEnd(this);
	}
	
	@Override
	public final void endPrefixMapping(final String prefix) {
		
		
		if (this.skipDefaultURI != null && this.skipDefaultURI.equals(prefix)) {
			this.skipDefaultURI = null;
		}
	}
	
	@Override
	public void error(final SAXParseException exception) throws SAXException {
		
		
		// throw exception;
	}
	
	@Override
	public void fatalError(final SAXParseException exception) throws SAXException {
		
		
		// throw exception;
	}
	
	/**
	 * This method decodes four encoded characters into three unencoded bytes in
	 * the read buffer and return the size of the buffer. (Ie: zero if we're at
	 * the end of the stream).
	 * 
	 * @param chars
	 * @param offset
	 * @param length
	 */
	final void fillBase64(final char[] chars, final int offset, final int length) {
		
		
		for (int index = offset, left = length;;) {
			while (this.readBufferIndex < this.readBufferSize) {
				this.targetCollector.getTarget().absorb(this.readBuffer[this.readBufferIndex++] & 0xFF);
			}
			this.readBufferSize = 0;
			if (left == 0) {
				return;
			}
			// load characters to a decoding character buffer
			// and count the characters to make sure we've got
			// an valid encoding on our hands
			while (this.decodeBufferSize < 4) {
				for (;;) {
					if (left-- == 0) {
						return;
					}
					final char tmp = chars[index++];
					if (tmp != '\n' && tmp != '\r') {
						this.decodeBuffer[this.decodeBufferSize++] = tmp;
						break;
					}
				}
			}
			// get the byte representation of the two first characters
			final byte byte1 = XmlToMapContentHandler.MATRIX[this.decodeBuffer[0] & 0xff];
			final byte byte2 = XmlToMapContentHandler.MATRIX[this.decodeBuffer[1] & 0xff];
			// decode the first real byte by shifting the bytes from previous
			// step
			// to their real position and then "merge" them (bitwise OR)
			this.readBuffer[0] = (byte) (byte1 << 2 & 0xfc | byte2 >>> 4 & 0x3);
			// character 3 and 4 can be '=' characters to mark the end of the
			// file, but if they are not - decode them too
			final char char2 = this.decodeBuffer[2];
			if (char2 != '=') {
				final byte byte3 = XmlToMapContentHandler.MATRIX[char2 & 0xff];
				this.readBuffer[1] = (byte) (byte2 << 4 & 0xf0 | byte3 >>> 2 & 0xf);
				final char char3 = this.decodeBuffer[3];
				if (char3 != '=') {
					final byte byte4 = XmlToMapContentHandler.MATRIX[char3 & 0xff];
					this.readBuffer[2] = (byte) (byte3 << 6 & 0xc0 | byte4 & 0x3f);
					this.readBufferSize = 3;
				} else {
					this.readBuffer[2] = -1;
					this.readBufferSize = 2;
				}
			} else {
				this.readBuffer[1] = -1;
				this.readBuffer[2] = -1;
				this.readBufferSize = 1;
			}
			this.decodeBufferSize = 0;
			this.readBufferIndex = 0;
		}
	}
	
	final void flushBase64() {
		
		
		while (this.readBufferIndex < this.readBufferSize) {
			this.targetCollector.getTarget().absorb(this.readBuffer[this.readBufferIndex++] & 0xFF);
		}
		this.decodeBufferSize = 0;
		this.readBufferSize = 0;
	}
	
	@Override
	public final void ignorableWhitespace(final char[] ch, final int start, final int length) {
		
		
		this.state.onWhitespace(this, ch, start, length);
	}
	
	final BaseObject parse() throws IOException, SAXException {
		
		
		this.parser.parse(this);
		try {
			return this.map;
		} finally {
			this.map = null;
		}
	}
	
	final Object peek() {
		
		
		if (this.stackPointer == 0) {
			throw new RuntimeException("Stack underflow!");
		}
		return this.stack[this.stackPointer - 1];
	}
	
	final Object pop() {
		
		
		if (this.stackPointer == 0) {
			throw new RuntimeException("Stack underflow!");
		}
		try {
			return this.stack[--this.stackPointer];
		} finally {
			this.stack[this.stackPointer + 1] = null;
		}
	}
	
	final void prepareCollector() {
		
		
		if (this.targetCollector == null) {
			this.targetCollector = Transfer.createCollector();
		} else {
			this.targetCollector.reset();
		}
	}
	
	@Override
	public final void processingInstruction(final String target, final String data) {
		
		
		// ignore
	}
	
	final void push(final Object o) {
		
		
		if (this.stackPointer == this.stack.length) {
			final Object[] newStack = new Object[this.stack.length << 1];
			System.arraycopy(this.stack, 0, newStack, 0, this.stack.length);
			this.stack = newStack;
		}
		this.stack[this.stackPointer++] = o;
	}
	
	final void put(final BaseObject o) {
		
		
		if (this.targetName != null) {
			this.put(this.targetName, o);
		} else //
		if (this.map == null) {
			this.targetMap = this.map = o;
		}
	}
	
	final void put(final String targetName, final BaseObject o) {
		
		
		if (o == this.map) {
			return;
		}
		if (this.map == null) {
			this.targetMap = this.map = o;
			return;
		}
		final BaseObject old = this.targetMap.baseGet(targetName, BaseObject.UNDEFINED);
		assert old != null : "NULL java value";
		if (old == BaseObject.UNDEFINED) {
			if (this.order != null) {
				this.order.add(Base.forString(targetName));
			}
			this.targetMap.baseDefine(targetName, o);
		} else {
			if (old instanceof XmlMultiple) {
				((XmlMultiple) old).add(o);
			} else {
				final XmlMultiple list = new XmlMultiple();
				list.add(old);
				list.add(o);
				this.targetMap.baseDefine(targetName, list);
			}
		}
	}
	
	@Override
	public InputSource resolveEntity(final String publicId, final String systemId) {
		
		
		// dummy source
		return new InputSource(new StringReader("<?xml version='1.0' encoding='UTF-8'?>"));
	}
	
	final void reuse(final String identity, final InputStream input, final String uri, final BaseObject map, final ExternalHandler handler, final Object attachment) {
		
		
		this.identity = identity;
		this.stringBuilder.setLength(0);
		this.setByteStream(input);
		this.setSystemId(uri);
		this.uri = uri;
		this.map = map;
		this.handler = handler;
		this.attachment = attachment;
	}
	
	final void reuse(final String identity, final Reader input, final String uri, final BaseObject map, final ExternalHandler handler, final Object attachment) {
		
		
		this.identity = identity;
		this.stringBuilder.setLength(0);
		this.setCharacterStream(input);
		this.setSystemId(uri);
		this.uri = uri;
		this.map = map;
		this.handler = handler;
		this.attachment = attachment;
	}
	
	@Override
	public final void setDocumentLocator(final Locator locator) {
		
		
		// ignore
	}
	
	@Override
	public final void skippedEntity(final String name) {
		
		
		// ignore
	}
	
	@Override
	public final void startDocument() {
		
		
		this.state = this.map == null
			/**
			 * MAP will create objects and put it as a root
			 */
			? XmlToMapState.MAP
			/**
			 * ROOT can reuse map passed
			 */
			: XmlToMapState.ROOT;
		this.targetName = null;
		this.targetMap = this.map;
		this.skipCounter = 0;
		this.skipDefaultURI = null;
	}
	
	@Override
	public final void startElement(final String uri, final String localName, final String qName, final Attributes atts) {
		
		
		if (this.skipCounter > 0 || this.uri != null && (this.skipDefaultURI != null && uri.length() == 0 || this.uri != uri && !this.uri.equals(uri))) {
			this.skipCounter++;
			return;
		}
		assert this.state != null : "state should never be NULL";
		this.state.onChildElementStart(this, localName, atts);
	}
	
	@Override
	public final void startPrefixMapping(final String prefix, final String uri) {
		
		
		if (this.uri != null && this.uri.equals(uri)) {
			this.skipDefaultURI = prefix;
			this.uri = uri; // with hope that it will be reused
		}
	}
	
	final String toState() {
		
		
		final StringBuilder builder = new StringBuilder(64);
		builder.append(this.identity);
		for (int i = this.stackPointer - 1; i >= 0; --i) {
			final Object object = this.stack[i];
			if (object == null || object.getClass() != String.class) {
				continue;
			}
			builder.append('/');
			builder.append(object);
		}
		return builder.toString();
	}
	
	@Override
	public String toString() {
		
		
		return "state: " + this.toState() + ", current: " + this.state + ", target: " + Format.Describe.toEcmaSource(this.targetMap, "") + ", targetName: " + this.targetName;
	}
	
	@Override
	public void warning(final SAXParseException exception) throws SAXException {
		
		
		// throw exception;
	}
}
