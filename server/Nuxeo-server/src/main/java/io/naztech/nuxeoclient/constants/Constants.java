package io.naztech.nuxeoclient.constants;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * @author Md. Mahbub Hasan Mohiuddin
 * @since 2019-09-11
 **/
public final class Constants {

	//Left square brace
	public static final String STR_LSB = "[";
	//Right square brace
	public static final String STR_RSB = "]";

	public static final String STR_OK = "OK";
	
	public static final String AES = "AES";
	public static final String SHA_1 = "SHA-1";
	public static final String ENCODING = "UTF-8";
	
	public static final String DB_NULL_STR = "?";
	public static final String FILE_SEPARATOR = File.separator;

	/*HTTP config*/
	public static final String USER_AGENT = "Mozilla/5.0";
	public static final String HTTP_CONTENT_TYPE = "Content-type";
	public static final String HTTP_TEXT_CONTENT_VALUE = "text/plain";
	public static final String HTTP_JSON_CONTENT_VALUE = "application/json";
	public static final String HTTP_XML_CONTENT_VALUE = "text/xml";
	
	/* mail Content Type */
	
	public static final String TEXT_HTML_CONTENT_VALUE = "text/html";
	public static final String MULTIPLE_PART = "multipart/*";
	public static final String MESSAGE_CONTENT = "message/rfc822";
	public static final String PDF_TYPE = "application/pdf" ;
	public static final String BINARY_TYPE = "application/binary" ;
	
	public static final String TEMPLATE = "TEMPLATE";
	public static final String YES = "YES";
	public static final String FOUND = "FOUND";
	public static final String EXCEPTION = "EXCEPTION";
	public static final String CUSTOMER = "CUSTOMER";
	public static final String DOC = "DOC";
	public static final String DOC_COUNT = "DOC_COUNT";
	public static final String SUPPLIER_META = "SUPPLIER_META";
	public static final String DOC_DETAILS = "DOC_DETAILS";
	public static final String DOCUTECH = "docutech";
	public static final String JSON_REQUEST = "jsonRequest";
	public static final String IMAP = "imap";
	public static final String POP3 = "pop3";
	public static final String INBOX = "INBOX";
	public static final String PARSED = "Parsed";
	public static final String EMAIL = "EMAIL";
	public static final String FTP = "FTP";
	public static final String DOT = ".";
	public static final String UNDERSCORE = "_";
	//public static final String SELECT = "SELECT";
	
	public static final String INVOICE = "INVOICE";
	public static final String PDF = ".pdf";
	public static final String XML = "xml";
	public static final String ONEDRIVE_URL_PREFIX = "https://1drv.ms/";
	public static final String ONEDRIVE_URL_PREFIX2 = "onedrive.live.com/redir";
	public static final String HEADERFIELD="Content-Disposition";
	public static final String HEADER_INDEX_DROPBOX="filename*=UTF-8";
	public static final String HEADER_INDEX_ONEDRIVE="filename=";
	public static final String ONEDRIVE_REDIR="redir";
	public static final String ONEDRIVE_DOWNLOAD="download";
	public static final String INDEX1="filename=";
	public static final String INDEX2="\";filename*=UTF-8";
	public static final String GOOGLEDRIVE_FINAL_URL1="https://drive.google.com/uc?authuser=0&id=";
	public static final String GOOGLEDRIVE_FINAL_URL2="&export=download";
	
	public static final SimpleDateFormat DTF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat DTF_SENT = new SimpleDateFormat("yyyyMMdd_HH:mm:ss");
	
	public static final String FESTOOL="FEST";
	public static final String NMBS="NMBS";
	public static final String OX="OX";
	public static final String APS="APS";
	public static final String BRISTAN="BRST";
	public static final String CTECH="CTCH";
	public static final String CURRENCY="GBP";

	public static final String INTOUCHWITHBRICKS="INTO";
	public static final String MBM="MBM";
	public static final String EDMUNDSON = "EDMU";
	public static final String FPMCCANN = "FPMC";
	public static final String HOWDENS = "HOWD";
	public static final String ENCON = "ENCO";
	public static final String VALLEYSAW = "VALL";
	public static final String TANNAS = "TANN";
	public static final String ALLELECTRICS = "ALLE";
	public static final String STOCKGAP="STOC";
	public static final String KERRYLONDON = "KERR";
	public static final String INNOTEC = "INNO";
	public static final String ADAFASTFIX = "ADAF";
	public static final String DECCO = "DECC";
	public static final String UNIT8LVORYSLTD = "UNIT";
	public static final String EASTBROOK = "EAST";
	public static final String FALCON = "FALC";
	public static final String GEORGELINES = "GEOR";
	public static final String DAVIDHUGGETT = "DAVI";
	public static final String FWHIPKINLTD = "FWHI";
	public static final String MIGHTON = "MIGH";
	public static final String PROCOL = "PROC";
	public static final String FRANKHOWARD = "FRAN";
	public static final String ECONEASTLONDON = "ECON";
	public static final String TREND = "TREN";
	public static final String FWHIPKIN = "FWHI";
	public static final String DKTOOLS = "DKTO";
	public static final String ANGLONORDEN = "ANGL";
	public static final String MIGHTONPRODUCTS = "MIGH";
	public static final String THAMESVALLY = "THAM";
	public static final String PITNEYBOWES = "PITN";
	public static final String BIFFA = "BIFF";
	public static final String GOOGLEPAYMENTS = "GOOG";
	public static final String ARMATOOL = "ARMA";
	public static final String SOUTHGATETIMBER = "SOUT";
	public static final String ECOTHERM = "ECOT";
	public static final String KITEPACKAGING = "KITE";
	public static final String BLAKLADERWORKWERLTD = "BLAK";
	public static final String APC = "APC";
	public static final String CARTRIDGE = "CART";
	public static final String RHA = "RHA";
	public static final String BYWATERS = "BYWA";
	public static final String JSL = "JSL";
	public static final String ADTRAKMEDIALTD = "ADTR";
	public static final String INFOLOGIC = "INFO";
	public static final String THEPLASTICSGROUPLTD = "THEP";
	public static final String HSSECURITYSERVICESLTD = "HSSE";
	public static final String PDC = "PDC";
	public static final String ACSUPPLY = "ACSU";
	public static final String PARKESPRODUCTSLTD = "PARK";
	public static final String YOURBILL = "YOUR";
	public static final String UVALUE = "UVAL";
	public static final String GORDONSMOTORS = "GORD";
	public static final String BECK = "BECK";
	public static final String FRISCO = "FRIS";
	public static final String PREMIEOFORESTPRODUCTSLTD = "PREM";
	public static final String UNIVERSAL = "UNIV";
	public static final String JOHNSONSINSULATIONSUPPLIESLTD = "JOHN";
	public static final String TIMLOC = "TIML";
	public static final String HELLERTOOLSGMBH = "HELL";
	public static final String NATIONALABRASIVESLTD = "NATI";
	public static final String EPD = "EPD";
	public static final String VR = "VR";
	public static final String MERCEDESBENZ = "MERC";
	public static final String THEFUELCARD = "THEF";
	public static final String FRANKMERCERSONSLTD = "FRAN";
	public static final String IBEXMARINROPESLTD = "IBEX";
	public static final String WIGHTMAN = "WIGH";
	public static final String CARTRIDGESAVELIMITED = "CART";
	public static final String GRS = "GRS";
	public static final String COLLEGETYRES = "COLL";
	public static final String MOLANLTD = "MOLA";
	public static final String PREMIER = "PREM";
	public static final String NATIONAL = "NATI";
	public static final String MIRKA = "MIRK";
	public static final String ONSITE = "ONSI";
	public static final String ASTROFLAME = "ASTR";

	public static final String JJROOFING ="JJRO";
	public static final String LM = "LM";
	public static final String SUPERGLASS = "SUPE";
	public static final String HOPPINGS = "HOPP";
	public static final String HTSHANDLINGTRUCK = "HTSH";
	public static final String PIRTEK = "PIRT";
	public static final String ROBERTLEE = "ROBE";
	public static final String TOPSERVICE = "TOPS";
	public static final String PROKOL = "PROK";
	public static final String CSYRETAIL = "CSYR";
	public static final String CIRET = "CIRET";
	public static final String FORKTRUCK = "FORKTRUCK";
	public static final String JHONSON = "JHONSON";
	public static final String PTS = "PTS";

	public static final String FALSE = "false";
	public static final String TRUE = "true";
	public static final String FWD_SL = "/";
	public static final String Archive = "Archive";
	public static final String Bad="Bad";
	public static final String NuxeoFailed="NuxeoFailed";
	
	/*
	 * public static int fileCount = 0;
	 * 
	 * // Downloaded file name public static String D_F_N = null;
	 */
}
