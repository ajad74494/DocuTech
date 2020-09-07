//package io.naztech.nuxeoclient.model;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.JAXBElement;
//import javax.xml.bind.JAXBException;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.stream.FactoryConfigurationError;
//import javax.xml.stream.XMLInputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.XMLStreamReader;
//
//import org.junit.Test;
//
//import io.naztech.nuxeoclient.model.invoice.festool.FestoolInvoiceDocument;
//
//public class TestJaxbClasses {
//
//	@Test
//	public void testUnmarshallFromClasspath() throws JAXBException, XMLStreamException, FactoryConfigurationError {
//		Unmarshaller ums = JAXBContext.newInstance(FestoolInvoiceDocument.class).createUnmarshaller();
//		XMLStreamReader reader = XMLInputFactory.newInstance()
//				.createXMLStreamReader(getClassLoader().getResourceAsStream("Festool_Invoice12.xml"));
//		JAXBElement<FestoolInvoiceDocument> jaob = ums.unmarshal(reader, FestoolInvoiceDocument.class);
//		FestoolInvoiceDocument ob = jaob.getValue();
//		System.out.println(ob.toString());
//		//System.out.println(ob.getFestoolDocumentDefinition().getAssociateMembershipNo());
//		System.out.println(ob.getFestoolDocumentDefinition().getCurrency());
//		//System.out.println(ob.getFestoolDocumentDefinition().getRepNo());
//	}
//
//	private ClassLoader getClassLoader() {
//		ClassLoader cl = null;
//		try {
//			cl = Thread.currentThread().getContextClassLoader();
//		} catch (SecurityException ex) {
//		}
//
//		if (cl == null)
//			cl = ClassLoader.getSystemClassLoader();
//		return cl;
//	}
//}
