package io.naztech.abbyy;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

public class Jaxb {
	
	public void unmarshall() {
		try {
			
		JAXBContext jc = JAXBContext.newInstance(Documents.class);
		Unmarshaller ums = jc.createUnmarshaller();
		Documents form = (Documents) ums.unmarshal(getClassLoader().getResourceAsStream("festtool_extraction_3.xml"));
		System.out.println(form.getFestoolExtractionV3().getAssociationMembershipNo());
		System.out.println(form.getFestoolExtractionV3().getLCurrency());
		System.out.println(form.getFestoolExtractionV3().getOrder());
		
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	private ClassLoader getClassLoader() {
		ClassLoader cl = null;
		try {
			cl = Thread.currentThread().getContextClassLoader();
		} catch (SecurityException ex) {
		}

		if (cl == null) cl = ClassLoader.getSystemClassLoader();
		return cl;
	}

}
