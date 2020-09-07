package io.naztech.nuxeoclient.controller;

import java.io.File;

import org.apache.commons.io.FilenameUtils;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ScannedInvoiceUploadControllerTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testUploadDocument() {
		File[] fileList =  new File("\\\\10.33.42.41\\HotFolder").listFiles();
		System.out.println(fileList.length);
		if(fileList.length > 1) {
			//if(FilenameUtils.getExtension(fileList[1].getName()).get == "pdf")
				System.out.println(FilenameUtils.getExtension(fileList[1].getName()).contains("pdf"));
			System.out.println(FilenameUtils.getExtension(fileList[0].getName()));
			System.out.println(FilenameUtils.getExtension(fileList[1].getName()));
			System.out.println(FilenameUtils.getExtension(fileList[2].getName()));
		}
	}
	
	@Test
	public void testUploadDocument1() {
		String val="£ 9,687.50";
		String value=val.trim();
		String []value1=null;
		if(value.contains("£")) {
			value1=value.split("£");
		}
		if(value.contains("$")) {
			value1=value.split("$");
		}
		if(value.contains(",")) {
			value1=value.split(",");
		}
//		if(value1.length==0) {
//			return value1[0].toString();
//		}
//		else
//			return value1[value1.length-1].toString();
		for(int i=0;i<value1.length;i++) {
			System.out.println(value1.length);
		System.out.println(value1[i].toString());
		}
	}

}
