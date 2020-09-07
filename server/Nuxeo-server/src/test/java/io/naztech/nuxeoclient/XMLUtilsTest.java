package io.naztech.nuxeoclient;

import static io.naztech.nuxeoclient.XMLUtils.print;
import static io.naztech.nuxeoclient.XMLUtils.printXML;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XMLUtilsTest {

	//@Test
	public final void testSetNodeValue() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testRemoveNode() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testRemoveAttribute() {
		fail("Not yet implemented");
	}

	private Document sampleDocument() throws ParserConfigurationException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element root = doc.createElement("MyXmlDoc");
		doc.appendChild(root);
		Element el = doc.createElement("FirstNode");
		el.setAttribute("user", "imtiaz-rahi");
		root.appendChild(el);
		return doc;
	}

	//@Test
	public final void testPrint() throws ParserConfigurationException {
		print(sampleDocument(), System.out);
	}

	//@Test
	public final void testPrintXML() throws ParserConfigurationException {
		printXML(sampleDocument(), System.out);	}

	//@Test
	public final void testPrintNode() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testGetXmlFromFile() {
		fail("Not yet implemented");
	}

//	@Test
	public final void testRemoveEmptyTextNodes() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testSetNodeValueOrRemoveIfNull() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testSetNodeValueOrRemoveIfEmpty() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testGetBeanFromXML() throws FileNotFoundException {
		InputStream is = getClassLoader().getResourceAsStream("person-list.xml");
		//PersonList ob = getBeanFromXML(PersonList.class, slurp(is, 16384));
		//System.out.println(ob);
	}

	private String slurp(final InputStream is, final int bufferSize) {
	    final char[] buffer = new char[bufferSize];
	    final StringBuilder out = new StringBuilder();
	    try {
	    	Reader in = new InputStreamReader(is, "UTF-8");
	        for (;;) {
	            int rsz = in.read(buffer, 0, buffer.length);
	            if (rsz < 0)
	                break;
	            out.append(buffer, 0, rsz);
	        }
	    }
	    catch (UnsupportedEncodingException ex) {
	    }
	    catch (IOException ex) {
	    }
	    return out.toString();
	}
	
	//@Test
	public final void testGetOutStreamFromBean() {
		fail("Not yet implemented");
	}

	//@Test
	public final void testGetDocumentFromJaxbElement() {
//		Document doc = getDocumentFromJaxbElement(ob, new QName("Persons"));
//		assertNotNull(doc);
//		print(doc, System.out);
	}

	private ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException ex) {}

		if (cl == null) cl = ClassLoader.getSystemClassLoader();
		return cl;
	}
}
