package io.naztech.nuxeoclient;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.*;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Utility methods to manipulate, print XML {@link Document} or {@link Node}. <br>
 * Mentionable functions include:
 * <ul>
 * <li>{@link #setNodeValue(Document, String, String)}
 * <li>{@link #removeNode(Document, String)}
 * <li>{@link #removeAttribute(Document, String)}
 * <li>{@link #removeEmptyTextNodes(Document)}
 * <li>{@link #printNode(Node, OutputStream)}
 * <li>{@link #printXML(Document, OutputStream)}
 * <li>{@link #getXmlFromFile(String)}
 * <li>{@link #getBeanFromXML(Class, String)}
 * <li>{@link #getDocumentFromJaxbElement(Object, QName)}
 * <li>{@link #getDocumentFromBytes(byte[])}
 * <li>{@link #getDocumentFromStream(InputStream)}
 * </ul>
 *
 * @author Imtiaz Rahi
 */
public class XMLUtils {

	private static final Logger logger = Logger.getLogger(XMLUtils.class.getName());

	/** {@link FactoryConfigurationError} */
	protected static String FCEx = "XMLInputFactory failed to load";
	/** {@link IOException} message */
	protected static String IOEx = "IO error";
	/** {@link JAXBException} message */
	protected static String JXEx = "JAXB unmarshall failed";
	/** {@link ParserConfigurationException} message */
	protected static String PCEx = "Document builder creation failed";
	/** {@link SAXException} message */
	protected static String SXEx = "XML parsing failed";
	/** {@link TransformerException} message */
	protected static String TfEx = "XML transformation failed";
	/** {@link XPathExpressionException} message */
	protected static String XPEx = "XML Node not found";
	/** {@link XMLStreamException} message */
	protected static String XSEx = "XML stream processing failed";

	/**
	 * Set {@link Node} value with the provided value (data).
	 *
	 * @param doc {@link Document}
	 * @param xpr XPath expression of the target {@link Node}
	 * @param val Value to be set in {@link Node}
	 */
	public static void setNodeValue(Document doc, String xpr, String val) {
		XPath xp = XPathFactory.newInstance().newXPath();
		try {
			Node node = (Node) xp.evaluate(xpr, doc, XPathConstants.NODE);
			node.setTextContent(val == null ? "" : val);
		} catch (XPathExpressionException e) {
			logger.log(Level.WARNING, XPEx, e);
		}
	}

	/**
	 * Remove XML {@link Node} from {@link Document} using XPath expression.
	 *
	 * @param doc XML {@link Document}
	 * @param xpr XPath expression
	 */
	public static void removeNode(Document doc, String xpr) {
		XPath xp = XPathFactory.newInstance().newXPath();
		try {
			Node template = (Node) xp.evaluate(xpr, doc, XPathConstants.NODE);
			Node parent = template.getParentNode();
			parent.removeChild(template);
		} catch (XPathExpressionException e) {
			logger.log(Level.WARNING, XPEx, e);
		}
	}

	/**
	 * Remove {@link Attr attribute} from XML {@link Node} using XPath expression.
	 *
	 * @param doc XML {@link Document}
	 * @param xpr XPath expression
	 */
	public static void removeAttribute(Document doc, String xpr) {
		XPath xp = XPathFactory.newInstance().newXPath();
		try {
			Attr template = (Attr) xp.evaluate(xpr, doc, XPathConstants.NODE);
			Element owner = template.getOwnerElement();
			owner.removeAttributeNode(template);
		} catch (XPathExpressionException e) {
			logger.log(Level.WARNING, XPEx, e);
		}
	}

	/**
	 * Transform XML {@link Document} to a {@link OutputStream} like {@link System#out} or {@link java.io.ByteArrayOutputStream} with some
	 * beauty. <br>
	 * e.g.
	 *
	 * <pre>
	 * 1. XMLUtils.printXML(doc, System.out); <br>
	 * 2. ByteArrayOutputStream stream = new ByteArrayOutputStream();
	 *    XMLUtils.printXML(request, stream);
	 * </pre>
	 *
	 * @param doc {@link org.w3c.dom.Document}
	 * @param out {@link OutputStream} like {@link System#out}, {@link java.io.ByteArrayOutputStream} etc.
	 */
	public static void printXML(Document doc, OutputStream out) {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			tf.transform(new DOMSource(doc), new StreamResult(out));
		} catch (TransformerException e) {
			logger.log(Level.WARNING, TfEx, e);
		}
	}

	/**
	 * Transform XML {@link Document} to a {@link OutputStream} like {@link System#out} or
	 * {@link java.io.ByteArrayOutputStream} with beauty.
	 * 
	 * @param doc {@link org.w3c.dom.Document}
	 * @param out {@link OutputStream} like {@link System#out}, {@link java.io.ByteArrayOutputStream} etc.
	 * @see XMLUtils#printXML(Document, OutputStream)
	 */
	public static void print(Document doc, OutputStream out) {
		printXML(doc, out);
	}

	/**
	 * Pretty print XML {@link Node} to a {@link OutputStream} like {@link System#out}, {@link java.io.ByteArrayOutputStream}. <br>
	 * e.g. Helper.printNode(nodeObject, System.out);
	 *
	 * @param node XML {@link Node}
	 * @param out {@link OutputStream} like {@link System#out}, {@link java.io.ByteArrayOutputStream}
	 */
	public static void printNode(Node node, OutputStream out) {
		try {
			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			tf.transform(new DOMSource(node), new StreamResult(out));
		} catch (TransformerException e) {
			logger.log(Level.WARNING, TfEx, e);
		}
	}

	/**
	 * Returns XML document from file system and return it. <br>
	 * Useful to test XML message parsing issues.
	 *
	 * @param fileName XML message file name
	 * @return {@link Document}
	 * @throws ParserConfigurationException
	 */
	public static Document getXmlFromFile(String fileName) throws ParserConfigurationException {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException ex) {}

		if (cl == null) cl = ClassLoader.getSystemClassLoader();

		return getDocumentFromStream(cl.getResourceAsStream(fileName));
	}

	/**
	 * Remove empty text nodes from XML {@link Document}. <br>
	 * Run this after removing some node from message template document. <br>
	 *
	 * @param doc XML {@link Document}
	 */
	public static void removeEmptyTextNodes(Document doc) {
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			NodeList list = (NodeList) xpath.evaluate("//text()[normalize-space(.) = '']", doc, XPathConstants.NODESET);
			for (int i = 0; i < list.getLength(); i++) {
				Node empty = list.item(i);
				empty.getParentNode().removeChild(empty);
			}
		} catch (XPathExpressionException e) {
			logger.log(Level.WARNING, XPEx, e);
		}
	}

	/**
	 * Set {@link Node} value with provide data. <br>
	 * {@link Node} or {@link Attr} will be removed if data is <code>NULL {@link Object}</code> or <code>ZERO ({@link Number})</code>.
	 *
	 * @param doc XML {@link Document}
	 * @param xpr XPath expression of the target {@link Node}
	 * @param val Value to be set in {@link Node}
	 * @param isAttr Whether Node is an {@link Attr attribute} or not
	 */
	public static void setNodeValueOrRemoveIfNull(Document doc, String xpr, Object val, boolean isAttr) {
		boolean removeIt = false;
		if (val == null) removeIt = true;
		else if (val instanceof Number) {
			Number num = (Number) val;
			if (num.intValue() == 0) removeIt = true;
		}

		if (removeIt) {
			if (isAttr) removeAttribute(doc, xpr);
			else removeNode(doc, xpr);
		} else setNodeValue(doc, xpr, String.valueOf(val));
	}

	/**
	 * Set {@link Node} value with provide data. <br>
	 * {@link Node} or {@link Attr} will be removed if data is <code>NULL, EMPTY {@link String}</code> or <code>ZERO ({@link Number})</code>.
	 *
	 * @param doc XML {@link Document}
	 * @param xpr XPath expression of the target {@link Node}
	 * @param val Value to be set in {@link Node}
	 * @param isAttr Whether Node is an {@link Attr attribute} or not
	 */
	public static void setNodeValueOrRemoveIfEmpty(Document doc, String xpr, Object val, boolean isAttr) {
		boolean removeIt = false;
		if (val == null) removeIt = true;
		else if (val instanceof String) {
			if (((String) val).isEmpty()) removeIt = true;
		} else if (val instanceof Number) {
			Number num = (Number) val;
			if (num.intValue() == 0) removeIt = true;
		}

		if (removeIt) {
			if (isAttr) removeAttribute(doc, xpr);
			else removeNode(doc, xpr);
		} else setNodeValue(doc, xpr, String.valueOf(val));
	}

	/**
	 * Returns typed bean (model) instance from serialized XML message string. <br>
	 * Usually needed to process serialized <tt>response XML messages</tt>.
	 *
	 * @param clazz Bean class (type); e.g. <code>FlightResult.class</code>
	 * @param xml Serialized XML message
	 * @param <T> POJO
	 * @return JAXB annotated bean instance filled with data from XML
	 * 
	 * @see #getXmlFromBean(JAXBElement)
	 * @see #getXmlFromBean(Object, QName)
	 * @see #getOutStreamFromBean(JAXBElement)
	 * @see #getOutStreamFromBean(Object, QName)
	 */
	public static <T> T getBeanFromXML(Class<T> clazz, String xml) {
		XMLStreamReader reader = null;
		try {
			JAXBContext jc = JAXBContext.newInstance(clazz);
			Unmarshaller um = jc.createUnmarshaller();
			reader = XMLInputFactory.newFactory().createXMLStreamReader(new StringReader(xml));
			JAXBElement<T> ob = um.unmarshal(reader, clazz);
			reader.close();
			return ob.getValue();
		} catch (JAXBException e) {
			logger.log(Level.WARNING, JXEx, e);
		} catch (XMLStreamException e) {
			logger.log(Level.WARNING, XSEx, e);
		} catch (FactoryConfigurationError e) {
			logger.log(Level.WARNING, FCEx, e);
		} finally {
			try { if (reader != null) reader.close(); } catch (XMLStreamException e) {}
		}
		return null;
	}

	/**
	 * Returns {@link ByteArrayOutputStream} from JAXB annotated POJO
	 *
	 * @param ob JAXB annotated POJO
	 * @param qn {@link QName} of an XML element; e.g. QName("Result")
	 * @param <T> POJO
	 * @return Generated {@link ByteArrayOutputStream} from bean
	 * @throws JAXBException if JAXB marshal fails
	 * 
	 * @see #getOutStreamFromBean(JAXBElement)
	 * @see #getXmlFromBean(JAXBElement)
	 * @see #getXmlFromBean(Object, QName)
	 */
	@SuppressWarnings("unchecked")
	public static <T> ByteArrayOutputStream getOutStreamFromBean(T ob, QName qn) throws JAXBException {
		return getOutStreamFromBean(new JAXBElement<T>(qn, (Class<T>) ob.getClass(), ob));
	}

	/**
	 * Returns {@link ByteArrayOutputStream} from JAXB annotated POJO.
	 * 
	 * @param jaxbE Java object instance wrapped with {@link JAXBElement}
	 * @param <T> POJO
	 * @return Generated {@link ByteArrayOutputStream} from bean
	 * @throws JAXBException if JAXB marshal fails
	 * 
	 * @see #getOutStreamFromBean(Object, QName)
	 * @see #getXmlFromBean(JAXBElement)
	 * @see #getXmlFromBean(Object, QName)
	 */
	public static <T> ByteArrayOutputStream getOutStreamFromBean(JAXBElement<T> jaxbE) throws JAXBException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		Marshaller msr = JAXBContext.newInstance(jaxbE.getDeclaredType()).createMarshaller();
		msr.marshal(jaxbE, stream);
		return stream;
	}

	/**
	 * Returns XML message as String from JAXB annotated POJO.
	 * 
	 * @param ob JAXB annotated POJO
	 * @param qn {@link QName} of an XML element; e.g. QName("Result")
	 * @param <T> POJO
	 * @return Generated XML message from bean
	 * @throws JAXBException if JAXB marshal fails
	 * 
	 * @see #getXmlFromBean(JAXBElement)
	 * @see #getOutStreamFromBean(Object, QName)
	 * @see #getOutStreamFromBean(JAXBElement)
	 */
	public static <T> String getXmlFromBean(T ob, QName qn) throws JAXBException {
		return getOutStreamFromBean(ob, qn).toString();
	}

	/**
	 * Returns XML message as String from JAXB annotated POJO.
	 * 
	 * @param jaxbE Java object instance wrapped with {@link JAXBElement}
	 * @param <T> POJO
	 * @return Generated XML message from bean
	 * @throws JAXBException if JAXB marshal fails
	 * 
	 * @see #getXmlFromBean(Object, QName)
	 * @see #getOutStreamFromBean(Object, QName)
	 * @see #getOutStreamFromBean(JAXBElement)
	 */
	public static <T> String getXmlFromBean(JAXBElement<T> jaxbE) throws JAXBException {
		return getOutStreamFromBean(jaxbE).toString();
	}

	/**
	 * Returns XML {@link Document} from JAXB annotated POJO. <br>
	 * Beans are first converted into {@link JAXBElement} first.
	 *
	 * @param ob JAXB annotated POJO
	 * @param qn {@link QName} of an XML element
	 * @param <T> Any Java bean
	 * @return XML {@link Document} object
	 * @see #getOutStreamFromBean(Object, QName)
	 * @see #getDocumentFromBytes(byte[])
	 */
	public static <T> Document getDocumentFromJaxbElement(T ob, QName qn) {
		ByteArrayOutputStream stream = null;
		try {
			stream = getOutStreamFromBean(ob, qn);
			return getDocumentFromBytes(stream.toByteArray());
		} catch (JAXBException e) {
			logger.log(Level.WARNING, JXEx, e);
		} finally {
			try { if (stream != null) stream.close(); } catch (IOException e) {}
		}
		return null;
	}

	/**
	 * Returns XML {@link Document} from byte array. <br>
	 * Any stream or string will provide byte array output.
	 * 
	 * @param ar Array of bytes
	 * @return Normalized XML {@link Document} object
	 * @see #getDocumentFromJaxbElement(Object, QName)
	 */
	public static Document getDocumentFromBytes(byte[] ar) {
		return getDocumentFromStream(new ByteArrayInputStream(ar));
	}

	/**
	 * Returns XML {@link Document} from any {@link InputStream} instance. <br>
	 * 
	 * @param stream {@link InputStream}
	 * @return Normalized XML {@link Document} object
	 * @see #getDocumentFromJaxbElement(Object, QName)
	 * @see #getDocumentFromBytes(byte[])
	 */
	public static Document getDocumentFromStream(InputStream stream) {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(stream);
			doc.getDocumentElement().normalize();
			return doc;
		} catch (SAXException e) {
			logger.log(Level.WARNING, SXEx, e);
		} catch (IOException e) {
			logger.log(Level.WARNING, IOEx, e);
		} catch (ParserConfigurationException e) {
			logger.log(Level.WARNING, PCEx, e);
		}
		return null;
	}
}
