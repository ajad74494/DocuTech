package io.naztech.nuxeoclient.service;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.naztech.nuxeoclient.model.Invoice;

public abstract class AbstractScannedInvoidUploadService {

	private static Logger log = LoggerFactory.getLogger(AbstractScannedInvoidUploadService.class);

	public abstract void uploadDocument(Invoice invoice, File file, Boolean sendToNuxeoFlag)
			throws IOException, Exception;

	protected void moveToArchive(String archiveFolderPath, File file, String strDate) throws IOException {
		String archive = archiveFolderPath + File.separator + strDate;
		File arcDir = new File(archive);
		if (!arcDir.exists())
			arcDir.mkdir();
		if (!new File(archive + File.separator + file.getName()).exists())
			Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(archive + File.separator + file.getName()),
					StandardCopyOption.REPLACE_EXISTING);

//		if (!new File(archive + File.separator + file.getName()).exists())
//			Files.copy(Paths.get(file.getAbsolutePath()), Paths.get(archive + File.separator + file.getName()),
//					StandardCopyOption.REPLACE_EXISTING);
	}

	protected void moveToBadFolder(String badfolderPath, File file, String strDate) throws IOException {
		String badFolderPath = badfolderPath + File.separator + strDate;
		File badDir = new File(badFolderPath);
		if (!badDir.exists())
			badDir.mkdir();

		if (!new File(badFolderPath + File.separator + file.getName()).exists())
			Files.move(Paths.get(file.getAbsolutePath()), Paths.get(badFolderPath + File.separator + file.getName()),
					StandardCopyOption.REPLACE_EXISTING);

//		if (!new File(badFolderPath + File.separator + file.getName()).exists())
//			Files.move(Paths.get(file.getAbsolutePath()), Paths.get(badFolderPath + File.separator + file.getName()),
//					StandardCopyOption.REPLACE_EXISTING);
	}

	protected void moveToNuxeoFailed(String nuxeoFailedFolderPath, File invoiceInfoXml, List<File> attachments,
			String strDate) throws IOException {
		String nuxeoFailed = nuxeoFailedFolderPath + File.separator + strDate;
		File nuxDir = new File(nuxeoFailed);
		if (!nuxDir.exists())
			nuxDir.mkdir();

		if (!new File(nuxeoFailed + File.separator + invoiceInfoXml.getName()).exists())
			Files.move(Paths.get(invoiceInfoXml.getAbsolutePath()),
					Paths.get(nuxeoFailed + File.separator + invoiceInfoXml.getName()),
					StandardCopyOption.REPLACE_EXISTING);

		for (File doc : attachments) {
			if (!new File(nuxeoFailed + File.separator + doc.getName()).exists())
				Files.move(Paths.get(doc.getAbsolutePath()), Paths.get(nuxeoFailed + File.separator + doc.getName()),
						StandardCopyOption.REPLACE_EXISTING);
		}
	}

	protected String dateFormatter(String date) {
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
		} else if (date.contains("st") || date.contains("nd") || date.contains("rd") || date.contains("th")) {
			DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d['st']['nd']['rd']['th'] MMMM yyyy",
					Locale.ENGLISH);
			LocalDate localDate = LocalDate.parse(date, formatter1);
			date = localDate.toString();
		}

		SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
		List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
		knownPatterns.add(new SimpleDateFormat("dd.MM.yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("d/MMMM/yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MMMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd MM yyyy"));
		knownPatterns.add(new SimpleDateFormat("ddMMMyy"));
		knownPatterns.add(new SimpleDateFormat("dd/MM/yyyy"));
		knownPatterns.add(new SimpleDateFormat("ddd MMM yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd-MM-yyyy"));
		knownPatterns.add(new SimpleDateFormat("dd-MMMM-yyyy"));
		knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));

		Exception ex = null;
		for (SimpleDateFormat df : knownPatterns) {
			try {
				if (!formatter.format(df.parse(date)).startsWith("0")) {
					return formatter.format(df.parse(date));
				}
			} catch (ParseException e) {
				ex = e;
			}
		}
		if (ex != null)
			log.warn("Failed to parse date " + date, ex);
		return "1970/01/01";
	}

	protected String getCleanData(String value) {
		if (null != value) {

			value = value.trim();

			if (value.contains("$") || value.contains("£") || value.contains(",") || value.contains("*")
					|| value.contains("GBP") || value.contains("GB")) {

				value = value.replaceAll("[$£*a-zA-Z,]", "").trim();
			}

			return value;
		} else
			return null;

	}

	protected String getLeftString(String value) {
		if (null != value) {
			String[] values = null;

			if (value.contains(" ")) {
				values = value.split(" ");
				value = values[0];
			}
			return value;
		} else
			return null;
	}

	protected String getRightString(String value) {
		if (null != value) {
			String[] values = null;

			if (value.contains(" ")) {
				values = value.split(" ");
				value = values[1];
			}
			return value;
		} else
			return null;
	}

	protected String bigDecimalToString(BigDecimal bdValue) {
		if (null != bdValue) {
			BigDecimal bd = new BigDecimal(bdValue.toString());
			return bd.toString();
		} else
			return "NULL";
	}

	protected Double bigDecimalToDouble(BigDecimal bd) {
		if (null != bd) {
			return bd.doubleValue();
		} else
			return 0.0;
	}

	protected Double nullHandlerDouble(String flt) {
		DecimalFormat df = new DecimalFormat("#.0");
		if (null == flt || flt.equals("")) {
			return (Double) 0.0;
		} else if (flt.equals("Each"))
			return 1.0;

		else {
			flt = getCleanData(flt);
			return Double.valueOf(df.format(Double.parseDouble(flt)));
		}

	}

	protected Integer nullHandlerInteger(String in) {
		double num1;
		int num2;
		if (null == in || in.equals("")) {
			return 0;
		} else {
			num1 = Double.parseDouble(getCleanData(in));
			num2 = (int) num1;

			return num2;
		}
	}

	/**
	 * @author samiul.islam
	 * @param in
	 * @return this method take a integer parameter and convert to string
	 */
	protected String IntegerToString(int in) {
		Integer i = in;
		return i.toString();
	}

	/**
	 * @author samiul.islam
	 * @param in
	 * @return this method take a string parameter if that null then return empty
	 *         string
	 */
	protected String nullHandler(String in) {
		if (null == in) {
			return "";
		} else {
			return in;
		}

	}

	/**
	 * @author samiul.islam
	 * @param bd
	 * @return this method take a bigDecimal parameter and convert integer
	 */
	protected Integer bigDecimalToInteger(BigDecimal bd) {
		if (null == bd) {
			return 0;
		} else {
			return bd.intValue();
		}
	}

	/**
	 * @author samiul.islam
	 * @param s
	 * @return convert short to integer value
	 */
	protected Integer shortToInteger(short s) {
		Short sh = new Short(s);
		int n = sh.intValue();
		return n;
	}

	/**
	 * @author samiul.islam
	 * @param i
	 * @return primitive int valut to convert Integer value
	 */
	protected Integer primitiveIntTOInteger(int i) {
		Integer n = (Integer) i;
		return n;
	}

	/**
	 * @author samiul.islam
	 * @param afterVatAmount
	 * @param beforeVatAmount
	 * @return this method vat rate value as Double value
	 */
	protected Double getVatRate(double afterVatAmount, double beforeVatAmount) {
		DecimalFormat value = new DecimalFormat("#.#");
		return Double.valueOf(value.format((afterVatAmount - beforeVatAmount * 100) / beforeVatAmount));
	}
}
