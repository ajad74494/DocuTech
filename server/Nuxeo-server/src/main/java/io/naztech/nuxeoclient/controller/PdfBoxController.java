package io.naztech.nuxeoclient.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.io.File;
import java.io.IOException;
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
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.io.RandomAccessBufferedFileInputStream;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import io.github.jonathanlink.PDFLayoutTextStripper;
import io.naztech.nuxeoclient.model.Invoice;
import io.naztech.nuxeoclient.service.ACService;
import io.naztech.nuxeoclient.service.ADAService;
import io.naztech.nuxeoclient.service.AdtrakServiec;
import io.naztech.nuxeoclient.service.AllBeesService;
import io.naztech.nuxeoclient.service.AllElectronics;
import io.naztech.nuxeoclient.service.AngloNordenService;
import io.naztech.nuxeoclient.service.ApcService;
import io.naztech.nuxeoclient.service.ApsService;
import io.naztech.nuxeoclient.service.AstroFlameService;
import io.naztech.nuxeoclient.service.BiraService;
import io.naztech.nuxeoclient.service.BristanService;
import io.naztech.nuxeoclient.service.BywatersProcessService;
import io.naztech.nuxeoclient.service.CartridgeService;
import io.naztech.nuxeoclient.service.CollegeTyreService;
import io.naztech.nuxeoclient.service.DebenhamsService;
import io.naztech.nuxeoclient.service.EPDSevice;
import io.naztech.nuxeoclient.service.EssexService;
import io.naztech.nuxeoclient.service.FPMcCannService;
import io.naztech.nuxeoclient.service.FalconService;
import io.naztech.nuxeoclient.service.FirefoxService;
import io.naztech.nuxeoclient.service.FlorprotecService;
import io.naztech.nuxeoclient.service.FrankMercerService;
import io.naztech.nuxeoclient.service.FrankService;
import io.naztech.nuxeoclient.service.HSSService;
import io.naztech.nuxeoclient.service.HandlingTruckService;
import io.naztech.nuxeoclient.service.HarpendenService;
import io.naztech.nuxeoclient.service.HarrisonService;
import io.naztech.nuxeoclient.service.HellerService;
import io.naztech.nuxeoclient.service.HoppingSoftwoodService;
import io.naztech.nuxeoclient.service.IbexService;
import io.naztech.nuxeoclient.service.InTouchService;
import io.naztech.nuxeoclient.service.InvoiceDataProcessService;
import io.naztech.nuxeoclient.service.JslService;
import io.naztech.nuxeoclient.service.KiteService;
import io.naztech.nuxeoclient.service.LewisService;
import io.naztech.nuxeoclient.service.MightonService;
import io.naztech.nuxeoclient.service.MirkaService;
import io.naztech.nuxeoclient.service.MolanService;
import io.naztech.nuxeoclient.service.NationalService;
import io.naztech.nuxeoclient.service.NavigatorService;
import io.naztech.nuxeoclient.service.OnSiteService;
import io.naztech.nuxeoclient.service.OxGroupService;
import io.naztech.nuxeoclient.service.ParkersProductService;
import io.naztech.nuxeoclient.service.PirtekService;
import io.naztech.nuxeoclient.service.RobertlrService;
import io.naztech.nuxeoclient.service.RoyalMailService;
import io.naztech.nuxeoclient.service.StockgapService;
import io.naztech.nuxeoclient.service.SuperglassService;
import io.naztech.nuxeoclient.service.TannasService;
import io.naztech.nuxeoclient.service.TaxService;
import io.naztech.nuxeoclient.service.TopService;
import io.naztech.nuxeoclient.service.UValueService;
import io.naztech.nuxeoclient.service.UniteEightService;
import io.naztech.nuxeoclient.service.UniversalService;
import io.naztech.nuxeoclient.service.ValleySawService;
import io.naztech.nuxeoclient.service.VrService;
import io.naztech.nuxeoclient.service.WightmanService;

/**
 * @author Muhammad Tarek
 * @since 2020-06-15
 */
@Controller
public class PdfBoxController {
	private static Logger log = LoggerFactory.getLogger(PdfBoxController.class);

	private Invoice invoice = new Invoice();

	@Autowired
	private InvoiceDataProcessService invoiceDataProcessService;

	@Autowired
	private CollegeTyreService collegeTyre;

	@Autowired
	private FalconService falconService;

	@Autowired
	TopService topservice;

	@Autowired
	HoppingSoftwoodService hoppingSoftwood;

	@Autowired
	BywatersProcessService bywatersProcess;

	@Autowired
	AstroFlameService astroFlame;

	@Autowired
	PirtekService pirtek;

	@Autowired
	HandlingTruckService handlingTruck;

	@Autowired
	NationalService nationalService;

	@Autowired
	OnSiteService onSiteService;

	@Autowired
	ApcService apcService;

	@Autowired
	AllBeesService allBeesService;

	@Autowired
	HarrisonService harrisonService;

	@Autowired
	HellerService hellerService;

	@Autowired
	ValleySawService valleySawService;

	@Autowired
	UniteEightService uniteEightService;

	@Autowired
	MightonService mightonService;

	@Autowired
	VrService vrService;

	@Autowired
	InTouchService inTouchService;

	@Autowired
	LewisService lewisService;

	@Autowired
	UValueService uValueService;

	@Autowired
	ADAService aDAService;

	@Autowired
	CartridgeService cartridgeService;

	@Autowired
	FPMcCannService fPMcCannService;

	@Autowired
	JslService jslService;

	@Autowired
	KiteService kiteService;

	@Autowired
	RobertlrService robertlrService;

	@Autowired
	StockgapService stockgapService;

	@Autowired
	TannasService tannasService;

	@Autowired
	WightmanService wightmanService;

	@Autowired
	FlorprotecService florprotecService;

	@Autowired
	BiraService biraService;

	@Autowired
	ACService acService;

	@Autowired
	DebenhamsService debenhamsService;

	@Autowired
	EssexService essexService;

	@Autowired
	FirefoxService firefoxService;

	@Autowired
	IbexService ibexService;

	@Autowired
	MolanService molanService;

	@Autowired
	MirkaService mirkaService;

	@Autowired
	FrankService frankService;

	@Autowired
	SuperglassService superglassService;

	@Autowired
	UniversalService universalService;

	@Autowired
	AllElectronics allElectronics;

	@Autowired
	ApsService apsService;

	@Autowired
	EPDSevice epdService;

	@Autowired
	BristanService bristanService;

	@Autowired
	HSSService hssService;

	@Autowired
	ParkersProductService parkersProductService;

	@Autowired
	FrankMercerService frankMercerServic;

	@Autowired
	HarpendenService harpendenServic;

	@Autowired
	NavigatorService navigatorService;

	@Autowired
	OxGroupService oxGroupService;

	@Autowired
	RoyalMailService royalMailService;

	@Autowired
	TaxService taxService;

//	@Autowired
//	TLCService tlcService;

	@Autowired
	AdtrakServiec adtrakService;

	@Autowired
	AngloNordenService angloNordenService;

	@Value("${dir.newSupplier}")
	private String newSupplier;

	@Value("${dir.imgPdf}")
	private String imgPdf;

	@Value("${pdfpoller.inTouchTitle}")
	private String inTouchTitle;

	@Value("${pdfpoller.collegeTitle}")
	private String collegeTitle;

	@Value("${pdfpoller.falconTitle}")
	private String falconTitle;

	@Value("${pdfpoller.nationalTitle}")
	private String nationalTitle;

	@Value("${pdfpoller.piretkTitle}")
	private String piretkTitle;

	@Value("${pdfpoller.topServiceTitle}")
	private String topServiceTitle;

	@Value("${pdfpoller.hoppingTitle}")
	private String hoppingTitle;

	@Value("${pdfpoller.astroflamTitle}")
	private String astroflamTitle;

	@Value("${pdfpoller.bywaterTitle}")
	private String bywaterTitle;

	@Value("${pdfpoller.handlingTitle}")
	private String handlingTitle;

	@Value("${pdfpoller.onsiteTitle}")
	private String onsiteTitle;

	@Value("${pdfpoller.apcTitle}")
	private String apcTitle;

	@Value("${pdfpoller.allBeesTitle}")
	private String allBees;

	@Value("${pdfpoller.harrisonTitle}")
	private String harrison1;

	@Value("${pdfpoller.vrTitle}")
	private String vrTitle;

	@Value("${pdfpoller.hellerTitle}")
	private String hellerTitle;

	@Value("${extension.pdf}")
	private String pdf;

	@Value("${pdfpoller.uniteTitle}")
	private String uniteTitle;

	@Value("${pdfpoller.valleyTitle}")
	private String valleyTitle;

	@Value("${pdfpoller.mightonTitle}")
	private String mightonTitle;

	@Value("${pdfpoller.lwitchTitle}")
	private String lwitchTitle;

	@Value("${pdfpoller.uValueTitle}")
	private String uValueTitle;

	@Value("${pdfpoller.adaTitle}")
	private String adaTitle;

	@Value("${pdfpoller.cartriTitle}")
	private String cartriTitle;

	@Value("${pdfpoller.fpmTitle}")
	private String fpmTitle;

	@Value("${pdfpoller.jslTitle}")
	private String jslTitle;

	@Value("${pdfpoller.kiteTitle}")
	private String kiteTitle;

	@Value("${pdfpoller.robertlritle}")
	private String robertlritle;

	@Value("${pdfpoller.stockTitle}")
	private String stockTitle;

	@Value("${pdfpoller.tannasTitle}")
	private String tannasTitle;

	@Value("${pdfpoller.wightmnTitle}")
	private String wightmnTitle;

	@Value("${pdfpoller.florprotecTitle}")
	private String florprotecTitle;

	@Value("${pdfpoller.acTitle}")
	private String acTitle;

	@Value("${pdfpoller.debenhamsTitle}")
	private String debenhamsTitle;

	@Value("${pdfpoller.essexTitle}")
	private String essexTitle;

	@Value("${pdfpoller.firFoxTitle}")
	private String firFoxTitle;

	@Value("${pdfpoller.frankTitle}")
	private String frankTitle;

	@Value("${pdfpoller.ibexTitle}")
	private String ibexTitle;

	@Value("${pdfpoller.mirkaTitle}")
	private String mirkaTitle;

	@Value("${pdfpoller.molanTitle}")
	private String molanTitle;

	@Value("${pdfpoller.superglassTitle}")
	private String superglassTitle;

	@Value("${pdfpoller.universalTitle}")
	private String universalTitle;

	@Value("${pdfpoller.allelectronicsTitle}")
	private String allelectronicsTitle;

	@Value("${pdfpoller.apsTitle}")
	private String apsTitle;

	@Value("${pdfpoller.epdTitle}")
	private String epdTitle;

	@Value("${pdfpoller.bristanTitle}")
	private String bristanTitle;

	@Value("${pdfpoller.frankMercerTitle}")
	private String frankMercerTitle;

	@Value("${pdfpoller.parkesTitle}")
	private String parkesTitle;

	@Value("${pdfpoller.harpendenTitle}")
	private String harpendenTitle;

	@Value("${pdfpoller.navigatorTitle}")
	private String navigatorTitle;

	@Value("${pdfpoller.oxTitle}")
	private String oxTitle;

	@Value("${pdfpoller.royalTitle}")
	private String royalTitle;

	@Value("${pdfpoller.iHASCOTitle}")
	private String iHASCOTitle;

//	@Value("${pdfpoller.tlcTitle}")
//	private String tlcTitle;

	@Value("${pdfpoller.adtrakTitle}")
	private String adtrakTitle;

	@Value("${pdfpoller.angloTitle}")
	private String angloTitle;

	@Value("${pdfpoller.hssTitle}")
	private String hssTitle;

	public void pdfDataExtraction(File[] files) {

		boolean sendToNuxeoFlag = true;

		for (File pdfInvoice : files) {

			if (pdfInvoice.getName().endsWith(pdf) || pdfInvoice.getName().endsWith("PDF")) {

				try {

					PDDocument document = PDDocument.load(pdfInvoice);
					PDFTextStripper tStripper = new PDFTextStripper();
					String pdfStr = tStripper.getText(document);
					RandomAccessBufferedFileInputStream strm = new RandomAccessBufferedFileInputStream(pdfInvoice);
					PDFParser pdfParser = new PDFParser(strm);
					pdfParser.parse();
					PDDocument pdDocument = new PDDocument(pdfParser.getDocument());
					PDFTextStripper pdfTextStripper = new PDFLayoutTextStripper();
					String pdfStrTextStripper = pdfTextStripper.getText(pdDocument);

					strm.close();
					pdDocument.close();
					document.close(); 
					Boolean falconA1 = pdfStr.contains(falconTitle);
					Boolean college = pdfStr.contains(collegeTitle);
					Boolean nationalA = pdfStr.contains(nationalTitle);
					Boolean piretk = pdfStr.contains(piretkTitle);
					Boolean topService = pdfStr.contains(topServiceTitle);
					Boolean hopping = pdfStr.contains(hoppingTitle);
					Boolean astroflam = pdfStr.contains(astroflamTitle);
					Boolean bywater = pdfStr.contains(bywaterTitle);
					Boolean handling = pdfStr.contains(handlingTitle);
					Boolean onSite = pdfStrTextStripper.contains(onsiteTitle);
					Boolean apc = pdfStrTextStripper.contains(apcTitle);
					Boolean allees = pdfStrTextStripper.contains(allBees);
					Boolean harrison = pdfStrTextStripper.contains(harrison1);
					Boolean heller = pdfStrTextStripper.contains(hellerTitle);
					Boolean vr = pdfStrTextStripper.contains(vrTitle);
					Boolean valley = pdfStrTextStripper.contains(valleyTitle);
					Boolean unite = pdfStrTextStripper.contains(uniteTitle);
					Boolean mighton = pdfStrTextStripper.contains(mightonTitle);
					Boolean inTouch = pdfStrTextStripper.contains(inTouchTitle);
					Boolean lwitch = pdfStrTextStripper.contains(lwitchTitle);
					Boolean uValue = pdfStrTextStripper.contains(uValueTitle);
					Boolean ada = pdfStrTextStripper.contains(adaTitle);
					Boolean cartri = pdfStrTextStripper.contains(cartriTitle);
					Boolean fpm = pdfStrTextStripper.contains(fpmTitle);
					Boolean jsl = pdfStrTextStripper.contains(jslTitle);
					Boolean kite = pdfStrTextStripper.contains(kiteTitle);
					Boolean robertlr = pdfStrTextStripper.contains(robertlritle);
					Boolean stock = pdfStrTextStripper.contains(stockTitle);
					Boolean tannas = pdfStrTextStripper.contains(tannasTitle);
					Boolean wightmn = pdfStrTextStripper.contains(wightmnTitle);
					Boolean florprotec = pdfStrTextStripper.contains(florprotecTitle);
					Boolean ac = pdfStrTextStripper.contains(acTitle);
					Boolean debenhams = pdfStrTextStripper.contains(debenhamsTitle);
					Boolean essex = pdfStrTextStripper.contains(essexTitle);
					Boolean firFox = pdfStrTextStripper.contains(firFoxTitle);
					Boolean frank = pdfStrTextStripper.contains(frankTitle);
					Boolean ibex = pdfStrTextStripper.contains(ibexTitle);
					Boolean mirka = pdfStrTextStripper.contains(mirkaTitle);
					Boolean molan = pdfStrTextStripper.contains(molanTitle);
					Boolean superglass = pdfStrTextStripper.contains(superglassTitle);
					Boolean universal = pdfStrTextStripper.contains(universalTitle);
					Boolean allelectronics = pdfStrTextStripper.contains(allelectronicsTitle);
					Boolean aps = pdfStrTextStripper.contains(apsTitle);
					Boolean epd = pdfStrTextStripper.contains(epdTitle);
					Boolean bristan = pdfStrTextStripper.contains(bristanTitle);
					Boolean hss = pdfStrTextStripper.contains(hssTitle);
					Boolean parkes = pdfStrTextStripper.contains(parkesTitle);
					Boolean frankMercer = pdfStrTextStripper.contains(frankMercerTitle);
					Boolean harpenden = pdfStrTextStripper.contains(harpendenTitle);
					Boolean navigator = pdfStrTextStripper.contains(navigatorTitle);
					Boolean ox = pdfStrTextStripper.contains(oxTitle);
					Boolean royal = pdfStrTextStripper.contains(royalTitle);
					Boolean iHASCO = pdfStrTextStripper.contains(iHASCOTitle);
//					Boolean tlc = pdfStrTextStripper.contains(tlcTitle);
					Boolean adtrak = pdfStrTextStripper.contains(adtrakTitle);
					Boolean anglo = pdfStrTextStripper.contains(angloTitle);
					Boolean bira = pdfStrTextStripper.contains("159922");

					if (pdfStr.trim().equals(null) || pdfStrTextStripper.trim().equals(null)) {
						// Move to Img Folder
						File source = new File(pdfInvoice.getAbsoluteFile().toString());
						File dest = new File(imgPdf);
						try {
							FileUtils.copyFileToDirectory(source, dest);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else if (superglass == true) {
						invoice = superglassService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (universal == true) {
						invoice = universalService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (ibex == true) {
						invoice = ibexService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (mirka == true) {
						invoice = mirkaService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (molan == true) {
						invoice = molanService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (college == true) {
						invoice = collegeTyre.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (falconA1 == true) {
						invoice = falconService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (ac == true) {
						invoice = acService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (debenhams == true) {
						invoice = debenhamsService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (essex == true) {
						invoice = essexService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (firFox == true) {
						invoice = firefoxService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (frank == true) {
						invoice = frankService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (topService == true) {
						invoice = topservice.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (hopping == true) {
						invoice = hoppingSoftwood.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (bywater == true) {
						invoice = bywatersProcess.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (astroflam == true) {
						invoice = astroFlame.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (piretk == true) {
						invoice = pirtek.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (handling == true) {
						invoice = handlingTruck.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (nationalA == true) {
						invoice = nationalService.processPdfInvoice(pdfStr, pdfInvoice);
					} else if (onSite == true) {
						invoice = onSiteService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (apc == true) {
						invoice = apcService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (allees == true) {
						invoice = allBeesService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (harrison == true) {
						invoice = harrisonService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (heller == true) {
						invoice = hellerService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (vr == true) {
						invoice = vrService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (valley == true) {
						invoice = valleySawService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (unite == true) {
						invoice = uniteEightService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (mighton == true) {
						invoice = mightonService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (inTouch == true) {
						invoice = inTouchService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (lwitch == true) {
						invoice = lewisService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (uValue == true) {
						invoice = uValueService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (ada == true) {
						invoice = aDAService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (cartri == true) {
						invoice = cartridgeService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (fpm == true) {
						invoice = fPMcCannService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (jsl == true) {
						invoice = jslService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (kite == true) {
						invoice = kiteService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (robertlr == true) {
						invoice = robertlrService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (stock == true) {
						invoice = stockgapService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (tannas == true) {
						invoice = tannasService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (wightmn == true) {
						invoice = wightmanService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (florprotec == true) {
						invoice = florprotecService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (bira == true) {
						invoice = biraService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					}

					else if (allelectronics == true) {
						invoice = allElectronics.processPdfInvoice(pdfStrTextStripper, pdfStr, pdfInvoice);
					} else if (aps == true) {
						invoice = apsService.processPdfInvoice(pdfStrTextStripper, pdfStr, pdfInvoice);
					} else if (epd == true) {
						invoice = epdService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (bristan == true) {
						invoice = bristanService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (hss == true) {
						invoice = hssService.processPdfInvoice(pdfStrTextStripper, pdfStr, pdfInvoice);
					} else if (parkes == true) {
						invoice = parkersProductService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (frankMercer == true) {
						invoice = frankMercerServic.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (harpenden == true) {
						invoice = harpendenServic.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (navigator == true) {
						invoice = navigatorService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (ox == true) {
						invoice = oxGroupService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (royal == true) {
						invoice = royalMailService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (iHASCO == true) {
						invoice = taxService.processPdfInvoice(pdfStrTextStripper, pdfInvoice);
					} else if (adtrak == true) {
						invoice = adtrakService.processPdfInvoice(pdfStrTextStripper, pdfStr, pdfInvoice);
					} else if (anglo == true) {
						invoice = angloNordenService.processPdfInvoice(pdfStrTextStripper, pdfStr, pdfInvoice);
					} else {
						// Move to new Folder
						File source = new File(pdfInvoice.getAbsoluteFile().toString());
						File dest = new File(newSupplier);
						try {
							FileUtils.copyFileToDirectory(source, dest);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}

					log.info(invoice.toString());
					invoiceDataProcessService.uploadDocument(invoice, pdfInvoice, sendToNuxeoFlag);

				} catch (Exception e) {
					log.info(e.getMessage());
				}
			}
		}

	}

}
