package io.naztech.nuxeoclient.service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * @author Asadullah.galib
 * @since 2019-09-17
 */
@Service
public class ImagePdfParserService {
	private static Logger log = LoggerFactory.getLogger(ImagePdfParserService.class);
	@Value("${import.pdfParser.tesseractFolder-path}")
	private String tesserPath;
	@Value("${import.pdfParser.pdfInputFolder-path}")
	private String inputPath;
	@Value("${import.pdfParser.pdfOutputFolder-path}")
	private String outputPath;

	public void pdfParser() throws IOException, SAXException, TikaException, DocumentException {
		InputStream pdf = Files.newInputStream(Paths.get(inputPath));
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		TikaConfig config = TikaConfig.getDefaultConfig();
		BodyContentHandler handler = new BodyContentHandler(out);
		Parser parser = new AutoDetectParser(config);
		Metadata meta = new Metadata();
		ParseContext parsecontext = new ParseContext();

		PDFParserConfig pdfConfig = new PDFParserConfig();
		pdfConfig.setExtractInlineImages(true);

		TesseractOCRConfig tesserConfig = new TesseractOCRConfig();
		tesserConfig.setLanguage("eng");
		tesserConfig.setTesseractPath(tesserPath);

		parsecontext.set(Parser.class, parser);
		parsecontext.set(PDFParserConfig.class, pdfConfig);
		parsecontext.set(TesseractOCRConfig.class, tesserConfig);

		parser.parse(pdf, handler, meta, parsecontext);
		String strData = new String(out.toByteArray(), Charset.defaultCharset());

		if (strData != null) {
			log.info(strData);
		}

		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outputPath));

		document.open();

		document.add(new Paragraph(strData));
		document.close();
		writer.close();

	}
}
