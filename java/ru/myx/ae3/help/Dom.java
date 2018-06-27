/*
 * Created on 12.03.2006
 */
package ru.myx.ae3.help;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import ru.myx.ae3.Engine;
import ru.myx.ae3.binary.Transfer;
import ru.myx.ae3.binary.TransferCollector;
import ru.myx.ae3.binary.TransferCopier;
import ru.myx.ae3.report.Report;
import ru.myx.sapi.FormatSAPI;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

/**
 * @author myx
 * 
 */
public final class Dom {
	private static final DocumentBuilderFactory	factory;
	
	private static final OutputFormat			formatXmlCompact;
	
	private static final OutputFormat			formatXmlExternal;
	
	private static final OutputFormat			formatXmlReadable;
	
	static {
		factory = DocumentBuilderFactory.newInstance();
		Dom.factory.setCoalescing( true );
		Dom.factory.setIgnoringComments( true );
		Dom.factory.setIgnoringElementContentWhitespace( false );
		Dom.factory.setNamespaceAware( true );
		Dom.factory.setValidating( false );
		
		formatXmlCompact = new OutputFormat();
		Dom.formatXmlCompact.setOmitXMLDeclaration( true );
		Dom.formatXmlCompact.setOmitComments( true );
		Dom.formatXmlCompact.setOmitDocumentType( true );
		Dom.formatXmlCompact.setIndenting( false );
		Dom.formatXmlCompact.setLineWidth( 128 );
		Dom.formatXmlCompact.setEncoding( Engine.ENCODING_UTF8 );
		Dom.formatXmlCompact.setVersion( "1.0" );
		Dom.formatXmlCompact.setStandalone( true );
		Dom.formatXmlCompact.setMediaType( "text/xml" );
		
		formatXmlExternal = new OutputFormat();
		Dom.formatXmlExternal.setOmitXMLDeclaration( false );
		Dom.formatXmlExternal.setOmitComments( false );
		Dom.formatXmlExternal.setOmitDocumentType( false );
		Dom.formatXmlExternal.setIndenting( false );
		Dom.formatXmlExternal.setLineWidth( 128 );
		Dom.formatXmlExternal.setEncoding( Engine.ENCODING_UTF8 );
		Dom.formatXmlExternal.setVersion( "1.0" );
		Dom.formatXmlExternal.setStandalone( false );
		Dom.formatXmlExternal.setMediaType( "text/xml" );
		
		formatXmlReadable = new OutputFormat();
		Dom.formatXmlReadable.setOmitXMLDeclaration( false );
		Dom.formatXmlReadable.setOmitComments( false );
		Dom.formatXmlReadable.setOmitDocumentType( true );
		Dom.formatXmlReadable.setIndenting( true );
		Dom.formatXmlReadable.setLineWidth( 80 );
		Dom.formatXmlReadable.setEncoding( Engine.ENCODING_UTF8 );
		Dom.formatXmlReadable.setVersion( "1.0" );
		Dom.formatXmlReadable.setStandalone( true );
		Dom.formatXmlReadable.setMediaType( "text/xml" );
	}
	
	/**
	 * @return document with no elements
	 */
	public static final Document createDocument() {
		final DocumentBuilder builder;
		try {
			builder = Dom.factory.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException( "Configuration error: " + e.getMessage(), e );
		}
		return builder.newDocument();
	}
	
	/**
	 * @param rootName
	 * @return document
	 */
	public static final Document createDocument(final String rootName) {
		final DocumentBuilder builder;
		try {
			builder = Dom.factory.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException( "Configuration error: " + e.getMessage(), e );
		}
		final Document result = builder.newDocument();
		final Element root = result.createElement( rootName );
		result.appendChild( root );
		return result;
	}
	
	/**
	 * Creates an element with given name. When given name cannot be xml-element
	 * name, 'param' element created with given name passed as 'key' attribute.
	 * 
	 * @param doc
	 * @param name
	 * @return
	 */
	public static final Element createElement(final Document doc, final String name) {
		{
			final int length = name.length();
			if (length > 0 && length < 128 && Format.Xml.isValidName( name )) {
				try {
					return doc.createElement( name );
				} catch (final Exception e) {
					throw new RuntimeException( "Can't create element: " + name );
				}
			}
		}
		{
			final Element element = doc.createElement( "param" );
			element.setAttribute( "key", name );
			return element;
		}
	}
	
	/**
	 * Returns node name for given element (considering namespace) and if this
	 * name is 'param' checks for 'key' attribute as well.
	 * 
	 * @param e
	 * @param namespace
	 * @return
	 */
	public static final String getNodeName(final Element e, final String namespace) {
		if (namespace != null) {
			final String elementNamespace = e.getNamespaceURI();
			if (elementNamespace != null && namespace != elementNamespace && !namespace.equals( elementNamespace )) {
				return null;
			}
		}
		final String name = e.getLocalName();
		if (name == null) {
			final String node = e.getNodeName();
			if (node.equals( "param" ) && e.hasAttribute( "key" )) {
				return e.getAttribute( "key" );
			}
			return node;
		}
		if (name.equals( "param" ) && e.hasAttribute( "key" )) {
			return e.getAttribute( "key" );
		}
		return name;
	}
	
	/**
	 * @param root
	 * @return
	 */
	public static final String innerTextValue(final Node root) {
		final StringBuilder result = new StringBuilder();
		Dom.innerTextValue( result, root.getFirstChild() );
		return result.toString();
	}
	
	/**
	 * @param target
	 * @param node
	 */
	public static final void innerTextValue(final StringBuilder target, final Node node) {
		for (Node current = node; current != null; current = current.getNextSibling()) {
			if (current.getNodeType() == Node.ENTITY_NODE) {
				target.append( ((Entity) current).getNotationName() );
			} else //
			if (current.getNodeType() == Node.CDATA_SECTION_NODE) {
				target.append( current.getNodeValue() );
			} else {
				target.append( Text.decodeXmlNodeValue( current.getNodeValue() ) );
			}
		}
	}
	
	/**
	 * @param root
	 * @param namespace
	 * @return
	 */
	public static final String innerValue(final Node root, final String namespace) {
		final StringBuilder result = new StringBuilder();
		Dom.innerValue( result, root.getFirstChild(), namespace );
		return result.toString();
	}
	
	/**
	 * @param target
	 * @param node
	 * @param namespace
	 */
	public static final void innerValue(final StringBuilder target, final Node node, final String namespace) {
		for (Node current = node; current != null; current = current.getNextSibling()) {
			switch (current.getNodeType()) {
			case Node.CDATA_SECTION_NODE:
				target.append( current.getNodeValue() );
				break;
			case Node.TEXT_NODE:
				target.append( Text.decodeXmlNodeValue( current.getNodeValue() ) );
				break;
			case Node.ENTITY_NODE:
				target.append( ((Entity) current).getNotationName() );
				break;
			default:
				final String name = current instanceof Element
						? Dom.getNodeName( (Element) current, namespace )
						: current.getNodeName();
				if (name == null) {
					continue;
				}
				target.append( "<" + name );
				final NamedNodeMap attributes = current.getAttributes();
				if (attributes != null) {
					for (int i = 0; i < attributes.getLength(); ++i) {
						final Node item = attributes.item( i );
						target.append( FormatSAPI.xmlAttribute( item.getNodeName(), item.getNodeValue() ) );
					}
				}
				if (current.hasChildNodes()) {
					target.append( '>' );
					final NodeList Children = current.getChildNodes();
					for (int i = 0; i < Children.getLength(); ++i) {
						Dom.innerValue( target, Children.item( i ), namespace );
					}
					target.append( "</" + name + ">" );
				} else {
					target.append( "/>" );
				}
			}
		}
	}
	
	/**
	 * @param xml
	 * @return document
	 */
	public static final Document toDocument(final File xml) {
		try (final FileInputStream input = new FileInputStream( xml )) {
			return Dom.toDocument( input );
		} catch (final IOException e) {
			throw new RuntimeException( xml.getName(), e );
		}
	}
	
	/**
	 * @param xml
	 * @return document
	 */
	public static final Document toDocument(final InputStream xml) {
		final DocumentBuilder builder;
		try {
			builder = Dom.factory.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException( "Configuration error: " + e.getMessage(), e );
		}
		try {
			return builder.parse( xml );
		} catch (final SAXException e) {
			throw new RuntimeException( "Error while parsing XML: " + e.getMessage() + '\n' + xml + '\n', e );
		} catch (final IOException e) {
			throw new Error( "I/O error while parsing XML: " + e.getMessage() );
		}
	}
	
	/**
	 * @param xml
	 * @return document
	 */
	public static final Document toDocument(final String xml) {
		final DocumentBuilder builder;
		try {
			builder = Dom.factory.newDocumentBuilder();
		} catch (final ParserConfigurationException e) {
			throw new RuntimeException( "Configuration error: " + e.getMessage(), e );
		}
		try {
			return builder.parse( new InputSource( new StringReader( xml ) ) );
		} catch (final SAXException e) {
			throw new RuntimeException( "Error while parsing XML: "
					+ e.getMessage()
					+ '\n'
					+ Text.limitString( xml, 2048, "..." )
					+ '\n', e );
		} catch (final IOException e) {
			throw new Error( "I/O error while parsing XML: " + e.getMessage() );
		}
	}
	
	/**
	 * @param xml
	 * @return element
	 */
	public static final Element toElement(final File xml) {
		return Dom.toDocument( xml ).getDocumentElement();
	}
	
	/**
	 * @param xml
	 * @return element
	 */
	public static final Element toElement(final InputStream xml) {
		return Dom.toDocument( xml ).getDocumentElement();
	}
	
	/**
	 * @param xml
	 * @return element
	 */
	public static final Element toElement(final String xml) {
		return Dom.toDocument( xml ).getDocumentElement();
	}
	
	/**
	 * @param root
	 * @return string
	 */
	public static final String toXmlCompact(final Element root) {
		assert root != null : "NULL root element!";
		final StringWriter stringOut = new StringWriter();
		try {
			final XMLSerializer serial = new XMLSerializer( stringOut, Dom.formatXmlCompact );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
		return stringOut.toString();
	}
	
	/**
	 * @param root
	 * @return string
	 */
	public static final TransferCopier toXmlCompactBinary(final Element root) {
		assert root != null : "NULL root element!";
		final TransferCollector collector = Transfer.createCollector();
		final OutputStream output = collector.getOutputStream();
		try (final Writer writer = new OutputStreamWriter( output, Engine.CHARSET_UTF8 )) {
			final XMLSerializer serial = new XMLSerializer( writer, Dom.formatXmlCompact );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
		/**
		 * collector is closed by try ^^^
		 */
		return collector.toCloneFactory();
	}
	
	/**
	 * @param root
	 * @param os
	 */
	public static final void toXmlCompactStream(final Element root, final OutputStream os) {
		assert root != null : "NULL root element!";
		assert os != null : "NULL output stream!";
		try {
			final XMLSerializer serial = new XMLSerializer( os, Dom.formatXmlCompact );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
	}
	
	/**
	 * @param root
	 * @param os
	 */
	public static final void toXmlCompactWriter(final Element root, final Writer os) {
		assert root != null : "NULL root element!";
		assert os != null : "NULL output stream!";
		try {
			final XMLSerializer serial = new XMLSerializer( os, Dom.formatXmlCompact );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
	}
	
	/**
	 * @param root
	 * @return string
	 */
	public static final String toXmlExternal(final Element root) {
		assert root != null : "NULL root element!";
		final StringWriter stringOut = new StringWriter();
		try {
			final XMLSerializer serial = new XMLSerializer( stringOut, Dom.formatXmlExternal );
			serial.serialize( root.getOwnerDocument() );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
		return stringOut.toString();
	}
	
	/**
	 * @param root
	 * @return string
	 */
	public static final String toXmlReadable(final Element root) {
		assert root != null : "NULL root element!";
		final StringWriter stringOut = new StringWriter();
		try {
			final XMLSerializer serial = new XMLSerializer( stringOut, Dom.formatXmlReadable );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
		return stringOut.toString();
	}
	
	/**
	 * @param root
	 * @return string
	 */
	public static final TransferCopier toXmlReadableBinary(final Element root) {
		assert root != null : "NULL root element!";
		final TransferCollector collector = Transfer.createCollector();
		final OutputStream output = collector.getOutputStream();
		try (final Writer writer = new OutputStreamWriter( output, Engine.CHARSET_UTF8 )) {
			final XMLSerializer serial = new XMLSerializer( writer, Dom.formatXmlReadable );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
		/**
		 * collector is closed by try ^^^
		 */
		return collector.toCloneFactory();
	}
	
	/**
	 * @param root
	 * @param os
	 */
	public static final void toXmlReadableStream(final Element root, final OutputStream os) {
		assert root != null : "NULL root element!";
		assert os != null : "NULL output stream!";
		try {
			final XMLSerializer serial = new XMLSerializer( os, Dom.formatXmlReadable );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
	}
	
	/**
	 * @param root
	 * @param os
	 */
	public static final void toXmlReadableWriter(final Element root, final Writer os) {
		assert root != null : "NULL root element!";
		assert os != null : "NULL output stream!";
		try {
			final XMLSerializer serial = new XMLSerializer( os, Dom.formatXmlReadable );
			serial.serialize( root );
		} catch (final IOException e) {
			throw Report.MODE_ASSERT || Report.MODE_DEBUG
					? new Error( e )
					: new Error( e.getMessage() );
		}
	}
	
	private Dom() {
		// ignore
	}
}
