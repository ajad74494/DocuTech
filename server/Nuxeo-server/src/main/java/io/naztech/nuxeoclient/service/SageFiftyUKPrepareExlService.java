package io.naztech.nuxeoclient.service;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.naztech.nuxeoclient.constants.SPName;
import io.naztech.nuxeoclient.model.Document;
import io.naztech.nuxeoclient.model.SupplierMeta;

/**
 * @author bm.alamin
 * @since 2020-01-27 <br>
 *        <hr>
 *        This <b>{@code SageFiftyUKPrepareExlService}</b> class prepare 2 excel
 *        sheet to import data in sage 50 UK. Sheets are
 *        <ol>
 *        <li>Invoice Transactions</li>
 *        <li>Trading Accounts</li>
 *        </ol>
 */
@Service
public class SageFiftyUKPrepareExlService {
	private static Logger log = LoggerFactory.getLogger(SageFiftyUKPrepareExlService.class);

	@Autowired
	private DocumentService documentService;

	@Autowired
	private SupplierMetaService supplierMetaService;
	@Value("${path.sage50.excel.trans}")
	private String transPath;
	@Value("${path.sage50.excel.tradeacc}")
	private String tradeAccPath;
	private List<Document> purchaseInvoices;
	private List<SupplierMeta> suppliers;
	private int rowCount;

	/**
	 * {@code importDataInExcel(String, String, String)} method is used to import
	 * invoice data in excel sheet. This method is also used for decorate data as
	 * per sage 50 UK expected.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param sheetName
	 * @return <b><em>true</em></b> if it can import data successfully otherwise
	 *         return <b><em>false</em></b>
	 */
	public boolean importDataInExcel(String startDate, String endDate) {
		try {
			rowCount = 0;
			purchaseInvoices = documentService.getPurchaseInvoiceList(startDate, endDate,
					SPName.ACTION_TRANSACTIONS.toString());
			try (FileOutputStream outputStream = new FileOutputStream(transPath + getRightNowDateAndTime() + ".xlsx")) {
				createAndWriteExcelSheet(SPName.SHEET_TRANSACTIONS.toString(), purchaseInvoices).write(outputStream);
			}
			return true;
		} catch (Exception e) {
			log.error("Transaction Data import status: Unsuccessful", e);
			return false;
		}

	}

	/**
	 * {@code importDataInExcel(String, String, String)} method is used to import
	 * supplier data in excel sheet. This method is also used for decorate data as
	 * per sage 50 UK expected.
	 * 
	 * @param startDate
	 * @param endDate
	 * @param sheetName
	 * @return <b><em>true</em></b> if it can import data successfully otherwise
	 *         return <b><em>false</em></b>
	 */
	public boolean importDataInExcel(boolean supplierFlag) {
		try {
			suppliers = supplierMetaService.getSupplierAccoutList(SPName.ACTION_SUPPLIER_META.toString());

			try (FileOutputStream outputStream = new FileOutputStream(
					tradeAccPath + getRightNowDateAndTime() + ".xlsx")) {
				createAndWriteExcelSheet(SPName.SHEET_SUPPLIERS.toString(), suppliers, supplierFlag)
						.write(outputStream);
			}
			return true;
		} catch (Exception e) {
			log.error("Supplier Data import status: Unsuccessful", e);
			return false;
		}

	}

	/**
	 * {@code createExcelSheet(String)} method is used to create new excel sheet for
	 * transaction entity. Sheet name is selected by it's parameter
	 * 
	 * @param sheetName
	 * @return a excel sheet
	 */
	private XSSFWorkbook createAndWriteExcelSheet(String sheetName, List<Document> purchaseInvoices) {
		try {
			rowCount = 0;
			XSSFWorkbook workbook = getWorkbook();
			XSSFSheet sheet = workbook.createSheet(sheetName);
			sheet = getHeader(rowCount, sheet);
			for (Document document : purchaseInvoices) {
				Row row = sheet.createRow(++rowCount);
				Cell cell = row.createCell(0);
				cell.setCellValue((String) document.getTransactionType());

				cell = row.createCell(1);
				cell.setCellValue((String) document.getAccountRef());

				cell = row.createCell(2);
				cell.setCellValue((String) document.getNominalCode());

				cell = row.createCell(3);
				cell.setCellValue((String) document.getDeptCode());

				cell = row.createCell(4);
				cell.setCellValue((String) document.getInvoiceDate().toString());

				cell = row.createCell(5);
				cell.setCellValue((String) document.getNetInvoiceTotal().toString());

				cell = row.createCell(6);
				cell.setCellValue((String) document.getVat().toString());

				cell = row.createCell(7);
				cell.setCellValue((String) document.getTaxCode());
			}
			return workbook;
		} catch (Exception e) {
			log.error("Excel sheet is not created", e);
			return null;
		}
	}

	/**
	 * @param rowCount
	 * @param sheet
	 * @return a sheet with header as per sage50 Transaction pattern
	 */
	private XSSFSheet getHeader(int rowCount, XSSFSheet sheet) {
		Row row = sheet.createRow(rowCount);
		Cell cell = row.createCell(0);
		cell.setCellValue((String) "Type");

		cell = row.createCell(1);
		cell.setCellValue((String) "Account Reference");

		cell = row.createCell(2);
		cell.setCellValue((String) "Nominal A/C Ref");

		cell = row.createCell(3);
		cell.setCellValue((String) "Department Code");

		cell = row.createCell(4);
		cell.setCellValue((String) "Date");

		cell = row.createCell(5);
		cell.setCellValue((String) "Net Amount");

		cell = row.createCell(6);
		cell.setCellValue((String) "Tax Amount");

		cell = row.createCell(7);
		cell.setCellValue((String) "Tax Code");
		return sheet;
	}

	/**
	 * {@code createExcelSheet(String)} method is used to create new excel sheet for
	 * supplier's account entity. Sheet name is selected by it's parameter
	 * 
	 * @param sheetName
	 * @param supplierFlag
	 * @return a excel sheet
	 */
	private XSSFWorkbook createAndWriteExcelSheet(String sheetName, List<SupplierMeta> suppliers,
			boolean supplierFlag) {
		try {
			rowCount = 0;
			XSSFWorkbook workbook = getWorkbook();
			XSSFSheet sheet = workbook.createSheet(sheetName);
			sheet = getHeader(rowCount, sheet, supplierFlag);
			for (SupplierMeta supplier : suppliers) {
				Row row = sheet.createRow(++rowCount);
				Cell cell = row.createCell(0);
				cell.setCellValue((String) supplier.getAccountRef());

				cell = row.createCell(1);
				cell.setCellValue((String) supplier.getSupplierFullName());
			}

			return workbook;
		} catch (Exception e) {
			log.error("Excel sheet is not created", e);
			return null;
		}
	}

	/**
	 * @param rowCount
	 * @param sheet
	 * @param supplierFlag
	 * @return a sheet with header as per sage50 Trading Account pattern
	 */
	private XSSFSheet getHeader(int rowCount, XSSFSheet sheet, boolean supplierFlag) {
		Row row = sheet.createRow(rowCount);
		Cell cell = row.createCell(0);
		cell.setCellValue((String) "Account Reference");

		cell = row.createCell(1);
		cell.setCellValue((String) "Account Name");
		return sheet;
	}

	/**
	 * @return just a {@code XSSFWorkbook} type variable
	 * @see XSSFWorkbook
	 */
	private XSSFWorkbook getWorkbook() {
		XSSFWorkbook workbook = new XSSFWorkbook();
		return workbook;
	}

	/**
	 * {@code getRightNowDateAndTime()} method gather date and time of now with
	 * specific format
	 * 
	 * @return String
	 */
	public static String getRightNowDateAndTime() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDateTime now = LocalDateTime.now();
		return dtf.format(now);
	}

}
