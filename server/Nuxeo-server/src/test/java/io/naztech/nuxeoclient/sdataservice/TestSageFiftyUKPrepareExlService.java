 package io.naztech.nuxeoclient.sdataservice;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import io.naztech.nuxeoclient.service.SageFiftyUKPrepareExlService;

public class TestSageFiftyUKPrepareExlService {

	@Test
	public void selectData() {

		@SuppressWarnings("resource")
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Java Books");

		Object[][] bookDataArr = { { "Head First Java", "Kathy Serria", 79 }, { "Effective Java", "Joshua Bloch", 36 },
		        { "Clean Code", "Robert martin", 42 }, { "Thinking in Java", "Bruce Eckel", 35 }, };

		/**
		 * define rows and columns
		 */
		int rowCount = 0;
		int columnCount = 0;

		for (Object[] bookData : bookDataArr) {
			Row row = sheet.createRow(++rowCount);
			columnCount = 0;
			for (Object field : bookData) {
				Cell cell = row.createCell(++columnCount);
				if (field instanceof String) {
					cell.setCellValue((String) field);
				}
				else if (field instanceof Integer) {
					cell.setCellValue((Integer) field);
				}
			}
		}
		try{
			FileOutputStream output = new FileOutputStream("C:/Apps/JavaBooks.xlsx");
			workbook.write(output);
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());

		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println(e.getMessage());

		}

	}
	
	@Test
	public void testJavaTool() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		   LocalDateTime now = LocalDateTime.now();  
		   System.out.println(dtf.format(now)+".xlsx");
	}
	
	@Test
	public void testImportDataInExcel() {
		SageFiftyUKPrepareExlService sage = new SageFiftyUKPrepareExlService();
		System.out.println(sage.importDataInExcel("2020-02-26", "2020-02-26"));
	}
	
	@Test
	public void ntestNull() throws ParseException {
		String date="7th/04/2020";
//		DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th']/MMMM/yyyy", Locale.ENGLISH);
//		LocalDate date1 = LocalDate.parse(date, formatter1);
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
//		SimpleDateFormat known = new SimpleDateFormat("d['st']['nd']['rd']['th']/MM/yyyy", Locale.ENGLISH);

		//System.out.println(formatter.format(known.parse(date)));
		//System.out.println(date1);
		
		
		String[] parts = null;
		if (date.length() < 10 && (date.contains("/") || date.contains(".") || date.contains(" "))) {

			if (date.contains("/"))
				parts = date.split("/");
			else if (date.contains("."))
				parts = date.split(".");
			else if (date.contains(" "))
				parts = date.split(" ");

			if (parts.length > 2 && parts[2].trim().length() == 2) {
				date = parts[0] + "/" + parts[1] + "/20" + parts[2];
			}
		} else if (date.contains("st")||date.contains("nd")||date.contains("rd")||date.contains("th")) {
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th'] MMMM yyyy", Locale.ENGLISH);;
			LocalDate date1 = LocalDate.parse(date, formatter1);
			date=date1.toString();
		}
		System.out.println(date);

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat("dd.MM.yyyy"));// "d['st']['nd']['rd']['th'] MMMM yyyy"
		knownPatterns.add(new SimpleDateFormat("dd MMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("d/MMMM/yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MMMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MM yyyy"));
		knownPatterns.add(new SimpleDateFormat("ddMMMyy"));
		knownPatterns.add(new SimpleDateFormat("dd/MM/yyyy"));
		knownPatterns.add(new SimpleDateFormat("ddd MMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));
		knownPatterns.add(new SimpleDateFormat("dd-MM-yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd-MMMM-yyyy"));
		

		Exception ex = null;
		for (SimpleDateFormat df : knownPatterns) {
			try {
				if(!formatter.format(df.parse(date)).startsWith("0")) {
				System.out.println(formatter.format(df.parse(date)));
				}
			} catch (ParseException e) {
				ex = e;
			}
		}
		if (ex != null)
			System.out.println(ex);
			//log.warn("Failed to parse date " + date, ex);
		
	
		
	}
}
