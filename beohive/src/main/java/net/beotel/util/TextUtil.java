package net.beotel.util;

import iptv.rest.service.IptvService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

//import net.beotel.captcha.CaptchaServiceSingleton;
//import net.beotel.helper.FlexDisplay;
//import net.beotel.model.Agreement;
//import net.beotel.model.DistributerCommissionBasis;
//import net.beotel.model.DistributerCommissionParamValue;
//import net.beotel.model.EquipmentDevice;
//import net.beotel.model.OfferItem;
//import net.beotel.model.OfferItemOption;
//import net.beotel.model.Operator;
//import net.beotel.model.Package;
//import net.beotel.model.Promotion;
//import net.beotel.model.PromotionParamValue;
//import net.beotel.model.Shipment.ShipmentState;
import net.beotel.model.template.Idable;
//import net.beotel.model.template.Nameable;
//import net.beotel.security.ClientService;
//import net.beotel.service.DistributerCommissionParamService;
//import net.beotel.service.EquipmentModelGroupService;
//import net.beotel.service.PackageService;
//import net.beotel.service.ServiceService;
//import net.beotel.service.ServiceService.SERVICE_NAME;
//import nl.captcha.Captcha;

//import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
//import org.apache.struts2.ServletActionContext;
import org.springframework.security.core.context.SecurityContextHolder;

//import com.google.common.util.concurrent.AtomicDouble;
//import com.octo.captcha.service.CaptchaServiceException;
//import com.opensymphony.xwork2.ActionContext;


public class TextUtil {
	public static final String IPTV_ADSL_USERNAME_PREFIX = "T";
	public static final String INPUT_SEPARATOR = "#";
	public static final char INPUT_SEPARATOR_CHAR = '#';
	public static final String MULTIPLE_SPACES_REGEX = "\\s*";
	public static final String SPLIT_BY_DOT_REGEX = "\\.";
	public static final String NAME_VALUE_SEPARATOR = "|";
	public static final String[] CURRENCY_ARRAY = {"RSD", "EUR"};
	public static final String SEPARATOR = "##";
	public static final String ONLINE_PAYMENT_SEPARATOR = "#";
	public static final String ANY_CHAR_LIST_CHARACTER = "%";
	public static final String PERCENT = "%";
	public static final String SEMICOLON_SEPARATOR = ";";
	public static final String SEMICOLON_SEPARATOR_SPACE = "; ";
	public static final String COMMA_SEPARATOR = ",";
	public static final String COMMA_SEPARATOR_SPACE = ", ";
	public static final String SNAKE_CASE = "_";
	public static final String EMAIL_DELIMITER = SEMICOLON_SEPARATOR;
	public static final String DISTRIBUTER_PARAM_VALUE_DELIMITER = SEMICOLON_SEPARATOR_SPACE;
	public static final String UNDERSCORE = "_";
	public static final String DEFAULT_2GO_NAME = "Korisnik";
	public static final String DEFAULT_2GO_LAST_NAME = "2GO";
	public static final String AT = "@";
	public static final String PLUS = "+";
	public static final String PLUS_SPACE = " + ";
	/**
	 * Da li je vracen 'OK' response?
	 * 
	 * @param response
	 * @return
	 */
	public static final boolean isSuccessResponse(String response) {
		return SUCCESS_STRING_RESPONSE.equals(response);
	}
	public static final String SUCCESS_STRING_RESPONSE = "OK";
	public static final String ERROR_STRING_RESPONSE = "Greska";
	public static final String EMPTY = "";
	public static final String POSITIVE_RESPONSE = "da";
	public static final String BLANKO = " ";
	public static final String DASH = "-";
	public static final char ALIGN_RIGHT_CHAR = 'R';
	public static final char ALIGN_LEFT_CHAR = 'L';
	public static final char ALIGN_CENTER_CHAR = 'C';
	public static final int MAX_XLS_ROW_NUMBER = 1000;
	public static final String ORDER_LIST_KEY_PARAM = "Broj narudzbenice: ";
	private static final String USER_FORM_IND_NAME = "Reg. Domena Narudzbenica";
	private static final String USER_FORM_COM_NAME = "Reg. Domena P.L. Narudzbenica";
	private static final String USER_FORM_FOREIGN_IND_NAME = "Reg domain order - individual user";
	private static final String USER_FORM_FOREIGN_COM_NAME = "Reg domain order - company user";
	private static final String MAIL_TEMPLATE_ORDERLIST_NEW_INDIVIDUAL = "Narudzbenica novi FL";
	private static final String MAIL_TEMPLATE_ORDERLIST_NEW_COMPANY = "Narudzbenica novi PL";
	private static final String MAIL_TEMPLATE_ORDERLIST_RESUME_INDIVIDUAL = "Narudzbenica produzenje FL";
	private static final String MAIL_TEMPLATE_ORDERLIST_RESUME_COMPANY = "Narudzbenica produzenje PL";
	private static final String MAIL_TEMPLATE_FOREIGN_CLIENT = "Domain registration order";
	private static final String BEOTEL_ISP_MAIL_EXTENSION = "@isp.beotel.net";
	private static final String CARRIER_MAIL_EXTENSION = "@beotelcarrier.net";
	public static final String ACCOUNT_ALREADY_EXISTS_ON_CPANEL = "Account vec postoji na nekom od cpanela";
	private static final String DISCOUNT_PARAM_REGEX = "(##\\d+#\\d+\\.?\\d+)";
	private static final String SINGLE_DISCOUNT_PARAM_REGEX = "##(\\d+)#(.+)";
	private static final String INPUT_PARAMS_MAP_REGEX = "(.+\\|.+#)+";
	private static final Pattern DISCOUNT_PARAM_PATTERN = Pattern.compile(DISCOUNT_PARAM_REGEX);
	public static final String UNIVERSAL_CONTENT_TYPE = "application/octet-stream";
	public static final String CONTENT_DISPOSITION_PATTERN = "attachment;filename=\"##\"";
	public static final char SLASH = '/';
	public static final char HASH = '#';
	public static final char COMMA = ',';
	public static final String DASH_SEPARATOR = " - ";
	public static final String COLON_SEPARATOR = " : ";
	public static final String UNPARSEABLE_DEFAULT = " / ";
	public static final String NEW_LINE = "\n";
	public static final String PARSE_START_VALUE = "";
	public static final String INTERNATIONAL_PHONE_PREFIX ="381";
	public static final String LOCAL_PHONE_PREFIX = "0";
	public static final Character SQUARE_BRACKET_LEFT = new Character('[');
	public static final Character SQUARE_BRACKET_RIGHT = new Character(']');
	public static final String DOT = ".";
	public static final String COLON = ":";
	public static final String[] SERBIAN_SPECIAL_CHARACTERS_LAT = {"Č", "Ć", "Ž", "Š", "Đ", "č", "ć", "ž", "š", "đ"};
	public static final String[] SERBIAN_NO_DECORATION_CHARACTERS_LAT = {"C", "C", "Z", "S", "DJ", "c", "c", "z", "s", "dj"};
	
	/*pozicije i duzine u Halcom fajlu*/
	public static final int HALC0M_ACCOUNT_NO_START_POS = 0;
	public static final int HALC0M_ACCOUNT_NO_LENGTH = 18;
	
	public static final int HALC0M_PAYMENT_DIRECTION_START_POS = 18;
	public static final int HALC0M_PAYMENT_DIRECTION_LENGTH = 2;
	public static final String HALC0M_OUTGOING_PAYMENT_DIRECTION = "10";
	
	public static final int HALC0M_PAYMENT_DATE_START_POS = 20;
	public static final int HALC0M_PAYMENT_DATE_LENGTH = 8;
	
	public static final int HALC0M_ACCOUNT_OWNER_START_POS = 30;
	public static final int HALC0M_ACCOUNT_OWNER_LENGTH = 36;
	
	public static final int HALC0M_PAYMENT_DATE2_START_POS = 66;
	public static final int HALC0M_PAYMENT_DATE2_LENGTH = 6;
	
	public static final int HALC0M_OUR_ACCOUNT_NO_START_POS = 72;
	public static final int HALC0M_OUR_ACCOUNT_NO_LENGTH = 18;	
	
	public static final int HALC0M_AMOUNT_INT_START_POS = 90;
	public static final int HALC0M_AMOUNT_INT_LENGTH = 13;
	
	public static final int HALC0M_AMOUNT_DEC_START_POS = 103;
	public static final int HALC0M_AMOUNT_DEC_LENGTH = 2;
	
	public static final int HALC0M_PAYMENT_TYPE_START_POS = 106;
	public static final int HALC0M_PAYMENT_TYPE_LENGTH = 3;		

	public static final int HALC0M_CONTROL_NO_INV_START_POS = 111;
	public static final int HALC0M_CONTROL_NO_INV_LENGTH = 2;
	
	public static final int HALC0M_DOCUMENT_NO_INV_START_POS = 113;
	public static final int HALC0M_DOCUMENT_NO_INV_LENGTH = 22;
		
	public static final int HALC0M_CONTROL_NO_START_POS = 135;
	public static final int HALC0M_CONTROL_NO_LENGTH = 2;
	
	public static final int HALC0M_DOCUMENT_NO_START_POS = 137;
	public static final int HALC0M_DOCUMENT_NO_LENGTH = 22;
	
	public static final int HALC0M_DESCRIPTION_START_POS = 159;
	public static final int HALC0M_DESCRIPTION_LENGTH = 36;

	public static final int HALC0M_PAYER_PART2_START_POS = 195;
	public static final int HALC0M_PAYER_PART2_LENGTH = 10;
	
	public static final int HALC0M_PAYER_PART1_START_POS = 205;
	public static final int HALC0M_PAYER_PART1_LENGTH = 35;
	
	public static final int HALC0M_PAYMENT_ID_START_POS = 240;
	public static final int HALC0M_PAYMENT_ID_LENGTH = 22;
	
	
	public static final String TXT_FILE_EXTENSION = ".txt";
	private static Logger log = Logger.getLogger(TextUtil.class);
	
//	public static final List<String> ONLINE_PAYMENT_SERVICES = Arrays.asList(new String[] {
//			ServiceService.VOIP_SERVICE_NAME, 
//			ServiceService.HOSTING_SERVICE_NAME,
//			ServiceService.DOMAIN_REGISTRATION_SERVICE_NAME,
//			ServiceService.ADSL_SERVICE_NAME,
//			ServiceService.WIFI_SERVICE_NAME,
//			ServiceService.VPS_SERVICE_NAME,
//			ServiceService.CLOUD_SERVICE_NAME,
//			ServiceService.IPTV_SERVICE_NAME,
//			ServiceService.BUNDLE_SERVICE_NAME});
	public static final List<String> SERVICE_KEY_PARAM_LABELS = Arrays.asList(new String[] { 
			"VoIP broj : ",
			"Domen : ",
			"Domen : ",
			"Username : ",
			"Username : ",
			"IP adresa : ",
			"Username: ",
			"Subscriber Uid: ",
			"Username: "});
	public static final List<String> SERVICE_KEY_PARAM_NAMES = Arrays.asList(new String[] { 
			"account id",
			"naziv domena",
			"Naziv domena",
			"username",
			"username",
			"IP adresa",
			"username",
			"iptv_subscriber_uid",
			"username"});
	
	@SuppressWarnings("serial")
	public static final Map<String, String> IPTV_SWITCH_PACKAGE_MAP = new HashMap<String, String>() {{
		   put("ADSL IPTV Maxi 10", "Bundle ADSL 10 + IPTV Basic");
		   put("ADSL IPTV Maxi 10 DUO", "Bundle ADSL 10 DUO + IPTV Basic");
		   put("WiFi Mega 6 IPTV", "Bundle WIFI 6 + IPTV Basic");
	}};
	
	@SuppressWarnings("serial")
	public static final Map<String, String> OBLIGATION_STRING_BY_OBLIGATION_PERIOD = new HashMap<String, String>() {{
			put("1M", "1 mesec");
			put("12M", "12 meseci"); put("1y", "12 meseci");
			put("24M", "24 meseca"); put("2y", "24 meseca");
			put("36M", "36 meseci"); put("3y", "36 meseci");
			put("30d", "1 mesec");
			put("360d", "12 meseci"); 
			put("730d", "24 meseca"); 
			put("1090d", "36 meseci"); 
	}};
	
	public static final String[] IPTV_SERVICE_PARAMS = new String[]{"iptv_subscriber_uid", "iptv_auth_pin", "iptv_pin"};
	
	private static TextUtil instance;
	
//	@SuppressWarnings("serial")
//	public static final Map<ShipmentState, String> SHIPMENT_STATE_TRANSLATOR_MAP = new HashMap<ShipmentState, String>() {{
//		put(ShipmentState.APPLIED, "Prijavljen za slanje");
//		put(ShipmentState.READY, "Pripremljen za slanje");
//		put(ShipmentState.REQUESTED, "Poslat zahtev ekspres službi");
//		put(ShipmentState.SENT, "Poslat ekspres službom");
//		put(ShipmentState.CONFIRMED, "Dostavljena pošiljka");
//		put(ShipmentState.RETURNED, "Vraćena pošiljka");
//		put(ShipmentState.CANCELED, "Otkazano slanje");
//	}};
	
	private TextUtil(){
	}
	
	public static TextUtil getInstance(){
		if(instance == null){
			instance = new TextUtil();
		}
		return instance;
	}
	
	/**
	 * Metoda koja prevodi Carrier-ov mail u Beotel-ov
	 * @param original
	 * @return
	 */
	public String translateCarrierMail(String original){
		String result = original;
		if(result != null && result.contains(CARRIER_MAIL_EXTENSION)){
			result = result.replaceAll(CARRIER_MAIL_EXTENSION, BEOTEL_ISP_MAIL_EXTENSION);
			log.info("Menjamo mail adresu iz "+ original +" u "+result);
		}
		return result;
	}
	
//	public String getServiceKeyParamLabel(String serviceName){
//		String result = "";
//		int index = ONLINE_PAYMENT_SERVICES.indexOf(serviceName);
//		if(index>-1){
//			result = SERVICE_KEY_PARAM_LABELS.get(index);
//		}
//		return result;
//	}
//	
//	public String getServiceKeyParamName(String serviceName){
//		String result = "";
//		int index = ONLINE_PAYMENT_SERVICES.indexOf(serviceName);
//		if(index>-1){
//			result = SERVICE_KEY_PARAM_NAMES.get(index);
//		}
//		return result;
//	}
	
	public String trim (String input){
		return input != null? input.trim() : null;
	}
	
	public String trimToNoNull (String input){
		return input != null? input.trim() : "";
	}

	public String digitsOnly(String input){
		return input.trim().replaceAll("\\D+", "");
	}
	
	public String alphanumericsOnly(String input){
		return input.replaceAll("[^A-Za-z0-9]", "");
	}
	
	public String alphanumericsAndSemicolonOnly(String input){
		return input.replaceAll("[^A-Za-z0-9;]", "");
	}
	
	public String alphaOnly(String input){
		return input.replaceAll("[^A-Za-z]", "");
	}
	
	/**
	 * Metoda za uklanjanje vodecih cifara
	 * @param input
	 * @return
	 */
	public String removeLeadingDigits(String input){
		String result = input;
		if(input != null){	
			int toRemove = 0;
			while(toRemove < input.length() && Character.isDigit(input.charAt(toRemove))){
				toRemove +=1;
			}
			if(toRemove > 0 ){
				result = input.substring(toRemove);
			}
		}
		return result;
	}

	
	public String digitsAndDashOnly(String input){
		return input.trim().replaceAll("[^\\-0-9]", "");
	}
	
	public String digitsAndDotOnly(String input){
		return input.trim().replaceAll("[^\\.0-9]", "");
	}

	public String trimLeadingZeroes(String input, int minimumCharacters){
		String trimmed = trim(input);
		if(trimmed.length()>minimumCharacters){
			String trimablePart = input.substring(0, trimmed.length()-minimumCharacters);
			String trimmedPart = trimablePart.replaceFirst("^(0+)", "");
			trimmed = trimmedPart+trimmed.substring(trimmed.length()-minimumCharacters);
		}
		return trimmed;
	}
	
//	public Map<String, String> getPromotionParamsAsMap(Promotion promotion){
//		Map<String, String> resultMap = new HashMap<String, String>();
//		for(PromotionParamValue p: promotion.getPromotionParamValues()){
//			resultMap.put(p.getPromotionTemplateParam().getName(), p.getValue());
//		}
//		return resultMap;
//	}
	
//	public Map<String, String> getDistributerCommissionBasisParamsAsMap(DistributerCommissionBasis distributerCommissionBasis){
//		Map<String, String> resultMap = new HashMap<String, String>();
//		for(DistributerCommissionParamValue dcpv : distributerCommissionBasis.getDistributerCommissionParamValues()){
//			resultMap.put(dcpv.getDistributerCommissionParam().getName(), dcpv.getValue());
//		}
//		return resultMap;
//	}
	
	/**
	 * Ako se dati: <br>
	 * keys - 5, 10, 15 <br>
	 * rightLimits - 10000, 20000, + <br>
	 * keySuffix - % <br>
	 * valueSuffix - rsd <br>
	 * Kreira stepenasti prikaz: <br>
	 * 
	 * 5% : 0 - 10000 rsd; 10% : 10000 - 20000 rsd; 15% : 20000+ rsd; 
	 * 
	 * @param keys
	 * @param rightLimits
	 * @param keySuffix
	 * @param intervalSuffix
	 * @return
	 */
//	public StringBuffer formatDistributerParamValuesAsStairsValues(List<String> keys, List<String> rightLimits, String keySuffix, String intervalSuffix) {
//		StringBuffer resultStringBuffer = new StringBuffer();
//		if (Calculator.getInstance().isEmpty(keys) || Calculator.getInstance().isEmpty(rightLimits) || keys.size() != rightLimits.size()) {
//			return resultStringBuffer;
//		}
//		String leftLimit = "0";
//		for (int i = 0; i < keys.size(); i++) {
//			String key = keys.get(i);
//			String rightLimit = rightLimits.get(i);
//			String interval = null;
//			if (DistributerCommissionParamService.INF.equals(rightLimit)) {
//				interval = leftLimit + DistributerCommissionParamService.INF;
//			} else {
//				interval = leftLimit + DASH_SEPARATOR + rightLimit;
//			}
//			resultStringBuffer.append(key + keySuffix + COLON_SEPARATOR + interval + BLANKO + intervalSuffix + DISTRIBUTER_PARAM_VALUE_DELIMITER); 
//			leftLimit = rightLimit;
//		}
//		return resultStringBuffer;
//	}
	
	/**
	 * Ako su dati: <br>
	 * value - 5 <br>
	 * suffix - % <br>
	 * 
	 * rezultat: 5%
	 * 
	 * @param value
	 * @param suffix
	 * @return
	 */
	public StringBuffer formatDistributerParamValuesAsSingleValue(String value, String suffix) {
		return new StringBuffer(value + suffix + TextUtil.DISTRIBUTER_PARAM_VALUE_DELIMITER);
	}

	/**
	 * Metoda koja iz mape izvlaci samo one ciji se kljucevi nalaze u nizu selectedParams
	 * @param inputMap
	 * @param selectedParams
	 * @return
	 */
	public Map<String, String> getMapEntries(Map<String, String> inputMap, String[] selectedParams) {
		Map<String, String> resultMap = new HashMap<String, String>();
		if(inputMap != null && inputMap.size()>0){
			for(String key : selectedParams){
				String value = inputMap.get(key);
				if(value != null){
					resultMap.put(key, value);
				}
			}
		}
		return resultMap;
	}
	
	/**
	 * Metoda koja vraca mapu sa header-om i indexom na kojima se nalazi u headerRow-u (case insensitive)
	 * Ako neki header nedostaje, zapis sa ERROR kljucem
	 * 
	 * @param headerRow
	 * @param header
	 * @return
	 */
	public Map<String, Integer> createXlsMap(HSSFRow headerRow, String[] header){
		Map<String, Integer> columnMap = new HashMap<String, Integer>();
		Integer invalidColumnIndex = 0;
		for(String title : header){
			Integer index = getStringIndex(headerRow, title);
			if(index != null){
				invalidColumnIndex = index;
				columnMap.put(title, index);
			}else{
				log.warn("Neuspelo kreiranje mape iz reda!");
				columnMap.put("ERROR", invalidColumnIndex);
				return columnMap;
			}
		}
		log.info("Uspesno kreiranje mape iz reda.");
		return columnMap;
	}

	/**
	 * Metoda vraca index na kom se nalazi podatak (case insensitive)
	 * @param headerRow
	 * @param value
	 * @return
	 */
	public Integer getStringIndex(HSSFRow headerRow, String value){
		if(headerRow != null & value != null){
			int minColIx = headerRow.getFirstCellNum();
			int maxColIx = headerRow.getLastCellNum();
			for(int i= minColIx; i<= maxColIx; i++){
				String colIValue = null;
				try{
					colIValue = headerRow.getCell(i).getStringCellValue();
				}catch (Exception e) {
					log.warn("Vrednost celije nije String!");
				}
				if(colIValue != null && value.trim().toLowerCase().equals(colIValue.trim().toLowerCase())){
					return i;
				}
			}
		}
		log.warn("U redu nije pronadjen podatak: "+ value);
		return null;
	}
	
	/**
	 * Metoda za mapiranje rednog broja kolone u String u nazivu kolone
	 * @param headerRow
	 * @param adslExcelImportTemplate
	 * @return
	 */
	public Map<String, Integer> mapColumnNameToIndex(HSSFRow headerRow, Map<String, Integer> columnMap){
		if(columnMap==null) {
			columnMap = new HashMap<String, Integer>();
		}
		int minColIx = headerRow.getFirstCellNum();
		int maxColIx = headerRow.getLastCellNum();
		for(int i=minColIx; i<maxColIx; i++){
			HSSFCell cell = headerRow.getCell(i);
			if(cell==null){
				continue;
			}
			columnMap.put(cell.getStringCellValue(), i);
		}

		return columnMap;
	}

	
	/**
	 * Metoda koja od stringa pravi listu, sa trimovanjem
	 * @param input
	 * @param delimiter
	 * @return
	 */
	public List<String> splitString(String input, String delimiter){
		List<String> resultList = new ArrayList<String>();
		if(input != null && delimiter != null && delimiter.length()>0){
			StringTokenizer tokenizer = new StringTokenizer(input, delimiter);
			while(tokenizer.hasMoreTokens()){
				String element = tokenizer.nextToken().trim();
				if(element.length()>0){
					resultList.add(element);
				}
			}
		}
		return resultList;
	}
	
	/**
	 * Metoda za parsiranje xls celije u datum
	 * @param cell
	 * @return
	 */
	public Date getDateFromCell(HSSFCell cell ){
		Date date = null;
		if(cell != null && cell.getCellType() == Cell.CELL_TYPE_NUMERIC && org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)){
			date = cell.getDateCellValue();
		}else{
			log.warn("Neregularan format datuma: "+ (cell != null? cell.toString() : "null"));
		}
		return date;
	}
	
	/**
	 * Vraca sadrzaj xls celije kao string
	 * 
	 * @param cell
	 * @return
	 */
//	public String getCellStringValue(HSSFCell cell) {
//		if (cell == null) {
//			return null;
//		}
//		Date dateCellValue = getDateFromCell(cell);
//		int cellType = cell.getCellType();
//		if (dateCellValue != null) {
//			return DateUtil.formatter.format(dateCellValue);
//		} else if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
//			Double doubleCellValue = cell.getNumericCellValue();
//			String stringCellValue = doubleToString(doubleCellValue);
//			if (stringCellValue.matches(ValidationsUtil.INTEGER_REGEX)) {
//				return String.valueOf((int) doubleCellValue.doubleValue());
//			}
//			return stringCellValue;
//		} else if (cellType == HSSFCell.CELL_TYPE_BOOLEAN) {
//			Boolean booleanCellValue = cell.getBooleanCellValue();
//			return booleanCellValue.toString();
//		} else if (cellType == HSSFCell.CELL_TYPE_STRING) {
//			return cell.getStringCellValue();
//		}
//		return null;
//	}
	
	/**
	 * Red xls-a vraca u formatu <br>
	 * vrednost|vrednost|vrednost
	 * 
	 * @param row
	 * @return
	 */
//	public String rowToString(int rowIndex, HSSFRow row) {
//		int minColIx = row.getFirstCellNum();
//		int maxColIx = row.getLastCellNum();
//		StringBuilder result = new StringBuilder(String.valueOf(rowIndex) + "|");
//		for (int i = minColIx; i < maxColIx; i++) {
//			HSSFCell cell = row.getCell(i);
//			if (cell == null) {
//				result.append("empty|");
//				continue;
//			}
//			result.append(getCellStringValue(cell) + "|");
//		}
//		return result.toString().replaceAll("(.*)\\|$", "$1");
//	}
	
	/**
	 * Metoda koja vraca prvu vrednost (pre prvog delimitera), u slucaju da vrednost sadrzi delimiter, inace vraca celu vrednost
	 * @param value
	 * @param delimiter
	 * @return
	 */
	public String getFirstValue(String value, String delimiter){
		String result = value;
		if(value != null && delimiter != null && delimiter.length()>0){
			int firstDelimiterIndex = value.indexOf(delimiter);
			if(firstDelimiterIndex>0){
				result = value.substring(0, firstDelimiterIndex).trim();
			}
		}
		return result;
	}
	
	/**
	 * Metoda koja vraca prvu vrednost (pre prvog delimitera), u slucaju da vrednost sadrzi delimiter, inace vraca celu vrednost
	 * @param value
	 * @param delimiter
	 * @return
	 */
	public String getFirstValueLimitedTo(String value, String delimiter, Integer limitedTo){
		String result = value;
		if(value != null && delimiter != null && delimiter.length()>0){
			int firstDelimiterIndex = value.indexOf(delimiter);
			if(firstDelimiterIndex>0){
				result = value.substring(0, firstDelimiterIndex).trim();
			}
		}
		if(result != null){
			if(limitedTo != null && result.length()>limitedTo){
				result = result.substring(0, limitedTo);
			}
		}
		return result;
	}
	
	/**
	 * Metoda za proveru captcha texta
	 * 
	 * @return
	 */
//	public boolean checkCaptcha(String captchaResponse){
//		boolean skipCaptcha = SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Operator;
//		Boolean isResponseCorrect = skipCaptcha;
//		if(!skipCaptcha) {
//			Map<String, Object> session = ActionContext.getContext().getSession();
//			String sessionId = (String) session.get("SessionId");
//	        String captchaId = sessionId;
//	        try{
//       			isResponseCorrect = CaptchaServiceSingleton.getInstance().validateResponseForID(captchaId,
//        				captchaResponse);
//        	}catch (CaptchaServiceException e) {
//        		log.warn("Doslo je do exceptiona pri validaciji jCaptcha: "+e.getMessage());
//        		isResponseCorrect = false;
//			}
//		}		
//		return isResponseCorrect;
//	}
	
	/**
	 * Metoda za logovanje stackTrace-a greske
	 * @param e
	 */
	public void logExceptionStackTrace(Exception e){
		log.warn("Cause: "+ e.getCause());
		log.warn("Message: "+ e.getMessage());
		log.warn("Class: "+ e.getClass());
		StackTraceElement[] trace = e.getStackTrace();
		if(trace != null && trace.length>0){
			for(StackTraceElement element : trace){
				log.warn("TRACE-- Class: "+element.getClassName()+" - Line: "+ element.getLineNumber()+ " - Greska: " + element.toString());
			}
		}
	}
	
	
	/**
	 * Metoda koja iz fajla izvlaci listu redova koje treba procesirati
	 * @param xlsFile
	 * @param columnMap - ovaj parametar se puni u samoj metodi, i predstavlja mapu kolona, po imenu/rednom broju
	 * @return
	 */
	public List<HSSFRow> parseXLSFile(File xlsFile, Map<String, Integer> columnMap){
		List<HSSFRow> rowList = null;
		if (columnMap != null) {
			try {
				InputStream is = new FileInputStream(xlsFile);
				HSSFWorkbook wb = new HSSFWorkbook(is);
				if(wb!=null){
					HSSFSheet sheet = wb.getSheetAt(0);
					if(sheet != null){
						HSSFRow headerRow = sheet.getRow(0);
						/*mapa - key -redni broj kolone, value - detail*/
						mapColumnNameToIndex(headerRow, columnMap); 
						int firstDataRow = sheet.getFirstRowNum()+1;
						int lastDataRow = sheet.getLastRowNum();
						log.info("Broj redova u fajlu: "+ (lastDataRow- firstDataRow));
						rowList = new ArrayList<HSSFRow>();
						for(int i=firstDataRow;i<=lastDataRow;i++){
							HSSFRow row = sheet.getRow(i);
							rowList.add(row);
						}
					}
				}
			} catch (FileNotFoundException e) {
				log.warn("Doslo je do greske prilikom uploada fajla!!!");
				log.warn("Greska: "+ e.getCause());
			} catch (IOException e) {
				log.warn("Doslo je do greske prilikom uploada fajla!!!");
				log.warn("Greska: "+ e.getCause());
			}			
			if(rowList == null || rowList.size()==0) {
				log.warn("Neuspelo parsiranje fajla. Pojavio se problem pri parsiranju fajla na redove.");
			}
			log.info("Ukupan broj parsiranih redova u fajlu: "+ (rowList != null? rowList.size():0));
		} else {
			log.error("Parametar columnMap prosledjujemo po referenci, mora biti instanciran.");
		}
		return rowList;
	}
	
	/**
	 * Vraca true ako je input null ili nema nijedan karakter osim blanko znaka
	 * @param input
	 * @return
	 */
	public boolean isEmpty(String input){
		return input == null || input.trim().length() ==0;
	}
	
	public boolean notEmpty(String input){
		return input != null && input.trim().length() >0;
	}

	/**
	 * Metoda koja iz inicijalnog teksta sklanja sve stringove is liste toRemove
	 * @param initialText
	 * @param toRemove
	 * @return
	 */
	public String removeFromInitialText(String initialText, String... toRemove){
		if(initialText != null){
			if(toRemove != null && toRemove.length>0){
				for(String removeElement : toRemove){
					initialText = initialText.replaceAll(removeElement, "");
				}
			}
		}
		return initialText;
	}
	
	/**
	 * Metoda koja iz inicijalnog teksta sklanja sve stringove is liste toRemove
	 * @param initialText
	 * @param toRemove
	 * @return
	 */
	public String getIfNotInIgnoreList(String initialText, String... ignoreList){
		if(initialText != null){
			if(ignoreList != null && ignoreList.length>0){
				for(String ignoreElement : ignoreList){
					if(initialText.equals(ignoreElement)){
						return null;
					}
				}
			}
		}
		return initialText;
	}
	
	/**
	 * Metoda koja iz inicijalnog teksta sklanja sve stringove is liste toRemove
	 * @param initialText
	 * @param toRemove
	 * @return
	 */
	public String stringSetFromList(List<String> values){
		StringBuffer result = new StringBuffer("(");
		int index = 0;
		if(values != null && values.size()>0){
			int size = values.size();
			for(String value : values){
				result.append("'"+value+"'");
				if(index++ != size-1){
					result.append(", ");
				}
			}
		}
		result.append(")");
		return result.toString();
	}
	
	/**
	 * Metoda koja pakuje sve ulazne podatke u listu i razdvaja ih separatorom
	 * @param initialText
	 * @param toRemove
	 * @return
	 */
	public String concatenateStringsWithSeparator(List<String> values, String separator){
		StringBuffer result = new StringBuffer();
		int index = 0;
		if(values != null && values.size()>0){
			int size = values.size();
			for(String value : values){
				result.append(value);
				if(index++ != size-1){
					result.append(":");
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * Kreira pun naziv domena
	 * 
	 * @param strings
	 * @return
	 */
	public String createFullDomainName(String domainName, String suffix){
		return domainName.concat(DOT).concat(suffix);
	}
	
	/**
	 * Vraca lepo formatiran zapis imena iz liste
	 * npr. Registracija domena i Hosting
	 * npr. Registracija domena, Hosting i ADSL
	 * 
	 * @param names
	 * @return
	 */
	public String getFormatedDifferentNames(List<String> names) {
		Set<String> services = new HashSet<String>();
		StringBuilder builder = new StringBuilder();
		for (String name : names) {
			if (!services.contains(name)) {
				builder.append(name + SEPARATOR);
				services.add(name);
			}
		}
		String result = builder.toString();
		return result.replaceAll("^(.*)"+SEPARATOR+"$", "$1").replaceAll("^(.*)"+SEPARATOR+"(.*)$", "$1 i $2").replaceAll(SEPARATOR, ", ");
	}

	public char getValidAlignment(String candidate){
		char alignment = 'L';
		if(candidate != null){
			if ("c".equals(candidate.trim().toLowerCase())){
				alignment = ALIGN_CENTER_CHAR;
			}
			if ("r".equals(candidate.trim().toLowerCase())){
				alignment = ALIGN_RIGHT_CHAR;
			}
			if ("l".equals(candidate.trim().toLowerCase())){
				alignment = ALIGN_LEFT_CHAR;
			}
		}
		return alignment;
	}
	
	/**
	 * Metoda za dohvatanje naziva korisnickog obrasca za narudzbenicu
	 * 
	 * @param isCompany da li je kompanija 
	 * @param isForeign da li je stranac
	 * @return
	 */
	public String getOrderListUserFormName(boolean isCompany, boolean isForeign) {
		if (isForeign)
			return isCompany ? USER_FORM_FOREIGN_COM_NAME : USER_FORM_FOREIGN_IND_NAME;
		else
			return isCompany ? USER_FORM_COM_NAME : USER_FORM_IND_NAME;
	}
	
	/**
	 * Na osnovu parametara odlucuje se za odgovarajuci mail_template
	 * @param isCompany
	 * @param isResume
	 * @param isForeign
	 * @return
	 */
	public String getOrderListMailTemplate(boolean isCompany, boolean isResume, boolean isForeign) {
		if (isForeign)
			return MAIL_TEMPLATE_FOREIGN_CLIENT;
		else if (isResume) {
			return isCompany ? MAIL_TEMPLATE_ORDERLIST_RESUME_COMPANY : MAIL_TEMPLATE_ORDERLIST_RESUME_INDIVIDUAL;
		} else {
			return isCompany ? MAIL_TEMPLATE_ORDERLIST_NEW_COMPANY : MAIL_TEMPLATE_ORDERLIST_NEW_INDIVIDUAL;
		}
	}
	
	/**
	 * Metoda za generisanje .txt fajla, sa prosledjenim imenom i redovima
	 * @param fileName
	 * @param rows
	 * @return
	 */
	public File generateTxtFile(String fileName, List<String> rows, String encoding){
		if(fileName != null && rows != null && rows.size() >0 ){
			String location  = /*"/tmp/";//*/System.getProperty("catalina.home")+ PropertiesUtil.getInstance().getTempFilesFolder();
			log.info("File created at: " + location);
			File generatedFile = new File(location + fileName + TXT_FILE_EXTENSION);
			PrintWriter writer = null;
			try {
					String fileEncoding = encoding != null ? encoding: "UTF-8";
			      	writer = new PrintWriter(generatedFile, fileEncoding);
			      	for(String row : rows){
			      		writer.println(row);
			      	}
			      	return generatedFile;	
			  } catch (FileNotFoundException e) {
				  log.warn("Pojavila se greska pri generisanju fajla, fajl nije pronadjen: "+ e.getMessage());
			  } catch (IOException e) {
				  log.warn("Pojavila se IO greska pri generisanju fajla: "+ e.getMessage());
			  }finally {
				  try {
					  writer.close();
					  } catch (Exception ex) {
						  log.warn("Pojavila se greska pri zatvaranju fajla: "+ex.getMessage());
					  }
			  }
		}else{
			log.info("Nedostaju obavezni podaci, ime fajla ili redovi za upis!");
		}
		return null;
	}
	
	/**
	 * Metoda koja proverava da li je broj koji je argument "lep"
	 * @param number
	 * @return
	 */
	public boolean isNiceVoIPNumber(String number){
		if(number == null || number.length()<3){
			log.info("Duzina broja je manja od 3! Broj: "+number);
			return false;
		}
		char current = number.charAt(0);
		int count = 1;
		for(int i=1; i<number.length(); i++){
			char temp = number.charAt(i);
			if(current == temp){
				count++;
				if(count>2){
					log.info("Broj: "+number+" je lep broj!");
					return true;
				}
			}
			else{
				current = temp;
				count = 1;
			}
		}
		log.info("Broj: "+number+" NIJE lep broj!");	
		return false;
	}
	
	/**
	 * Ako je objekat nije null vraca toString objekta
	 * @param object
	 * @return
	 */
	public String toString(Object string) {
		return string != null ? string.toString() : "";
	}
	
	/**
	 * Ako je objekat nije null vraca toString objekta
	 * @param object
	 * @return
	 */
	public String toString(Idable idable) {
		return idable != null ? idable.toString(): "NULL";
	}
	
	public String toString(Integer integer) {
		return integer != null ? integer.toString() : "0";
	}
	
	public String toString(Double double1) {
		return double1 != null ? double1.toString() : "0";
	}
	
	public Integer toInteger(Integer integer) {
		return integer != null ? integer : 0;
	}
	
	public Double toDouble(Double double1) {
		return double1 != null ? double1 : 0.0d;
	}
	
	/**
	 * Provera da li je korisnik uneo ispravnu captchu-u (nl.captcha.Captcha)
	 * @param captchaResponse
	 * @return
	 */
//	public boolean isCaptchaOK(String captchaResponse) {
//		boolean skipCaptcha = SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof Operator;
//		boolean correct = skipCaptcha;
//		if (!skipCaptcha) {
//			HttpServletRequest request = ServletActionContext.getRequest();
//			Captcha captcha = (Captcha) request.getSession(false).getAttribute(Captcha.NAME);
//			correct = captcha.isCorrect(captchaResponse);
//		}
//		return correct;
//	}
	
	/**
	 * Metoda za proveru jednakosti dva Objekta
	 * @param one
	 * @param two
	 * @return
	 */
	public boolean checkEqual(Object one, Object two){
		boolean equals = false;
		if(one != null){
			equals = one.equals(two);
		}else{
			equals = two == null;
		}
		return equals;
	}
	
	/**
	 * Metoda za proveru jednakosti dva Objekta
	 * @param one
	 * @param two
	 * @return
	 */
	public boolean checkDifferent(Object one, Object two){
		boolean different = true;
		if(one != null){
			different = !one.equals(two);
		}else{
			different = two != null;
		}
		return different;
	}
	
	/**
	 * Metoda koja originalText produzuje na duzinu totalLength karakterima characterToFill
	 * @param originalText
	 * @param characterToFill
	 * @param totalLength
	 * @return
	 */
	public String fillTrailing(String originalText, char characterToFill, int totalLength){
		if(originalText != null && originalText.length()<=totalLength){
			String resultText = originalText;
			int charactersToFillCount = totalLength - originalText.length();
			if(charactersToFillCount>0){
				StringBuffer sb = new StringBuffer(originalText);
				for(int i = 0; i<charactersToFillCount; i++){
					sb.append(characterToFill);
				}
				resultText = sb.toString();
			}
			return resultText;
		}
		return null;		
	}
	
	/**
	 * Metoda koja originalText produzuje na duzinu totalLength karakterima characterToFill
	 * @param originalText
	 * @param characterToFill
	 * @param totalLength
	 * @return
	 */
	public String fillLeading(String originalText, char characterToFill, int totalLength){
		if(originalText != null && originalText.length()<=totalLength){
			String resultText = originalText;
			int charactersToFillCount = totalLength - originalText.length();
			if(charactersToFillCount>0){
				StringBuffer sb = new StringBuffer();
				for(int i = 0; i<charactersToFillCount; i++){
					sb.append(characterToFill);
				}
				resultText = sb.toString()+originalText;
			}
			return resultText;
		}
		return null;		
	}
	
	/**
	 * Menja inicijalni string sa toInsert pocev od pozicije startPos
	 * 
	 * Ako je toInsert krace od length, dopunjava ga sa fillChar na kraju
	 * 
	 * @param initialString
	 * @param toInsert
	 * @param startPos
	 * @param length
	 * @param fillChar
	 * @return
	 */
	public String changeString(String initialString, String toInsert, int startPos, int length, char fillChar){
		String result = null;
		if(initialString != null && length>0 && toInsert != null){
			if(toInsert.length()<= length){
				String replacement = fillTrailing(toInsert, fillChar, length);
				result = initialString.substring(0, startPos)+replacement+initialString.substring(startPos+length);
			}
		}		
		return result;
	}
	
	
	

	/**
	 * Metoda koja kreira Halcom red u fajlu za izabrani servis, prema template redu
	 * @param templateRow
	 * @param serviceName
	 * @param serviceTotalAmount
	 * @return
	 */
//	public String createHalcomRowForService(String templateRow,	String serviceName, AtomicDouble serviceTotalAmount) {
//		/*BEOTEL NET*/
//		String result = changeString(templateRow, "BEOTEL NET", HALC0M_ACCOUNT_OWNER_START_POS, HALC0M_ACCOUNT_OWNER_LENGTH,' ');
//		/*BROJ RACUNA - sve 0*/
//		result = changeString(result, "0", HALC0M_ACCOUNT_NO_START_POS, HALC0M_ACCOUNT_NO_LENGTH,'0');
//		/*IZNOS*/
//		int amount = (int)(serviceTotalAmount.doubleValue() * 100.0);
//		String amountReady = fillLeading(amount+"", '0', HALC0M_AMOUNT_INT_LENGTH + HALC0M_AMOUNT_DEC_LENGTH);
//		result = changeString(result, amountReady, HALC0M_AMOUNT_INT_START_POS, HALC0M_AMOUNT_INT_LENGTH + HALC0M_AMOUNT_DEC_LENGTH,'-');
//		/*sifra placanja - 189*/
//		result = changeString(result, "189", HALC0M_PAYMENT_TYPE_START_POS, HALC0M_PAYMENT_TYPE_LENGTH,' ');
//		/*broj dokumenta 1, 2*/
//		result = changeString(result, "", HALC0M_DOCUMENT_NO_START_POS, HALC0M_DOCUMENT_NO_LENGTH,' ');
//		result = changeString(result, "", HALC0M_DOCUMENT_NO_INV_START_POS, HALC0M_DOCUMENT_NO_INV_LENGTH,' ');
//		/*model*/
//		result = changeString(result, "-", HALC0M_CONTROL_NO_START_POS, HALC0M_CONTROL_NO_LENGTH,' ');
//		result = changeString(result, "-", HALC0M_CONTROL_NO_INV_START_POS, HALC0M_CONTROL_NO_INV_LENGTH,' ');
//		/*Opis*/
//		result = changeString(result, "Zbirno " +(serviceName.length()>4? serviceName.substring(0, 4):serviceName)+" fizicka lica", HALC0M_DESCRIPTION_START_POS, HALC0M_DESCRIPTION_LENGTH,' ');
//		/*IME*/
//		result = changeString(result, "BeotelNet", HALC0M_PAYER_PART1_START_POS, HALC0M_PAYER_PART1_LENGTH,' ');
//		/*ADRESA*/
//		result = changeString(result, "-", HALC0M_PAYER_PART2_START_POS, HALC0M_PAYER_PART2_LENGTH,' ');
//		/*KONTROLNI BROJ*/
//		result = changeString(result, "", HALC0M_PAYMENT_ID_START_POS, HALC0M_PAYMENT_ID_LENGTH,' ');		
//		return result;
//	}
	
	/**
	 * Metoda koja proverava da li smo mi izvrsili uplatu za red iz Halcom fajla
	 * @param halcomRow
	 * @return
	 */
	public boolean isOutgoingPayment(String halcomRow){
		if(halcomRow != null && halcomRow.length()>HALC0M_PAYMENT_ID_START_POS){
			if(HALC0M_OUTGOING_PAYMENT_DIRECTION.equals(halcomRow.substring(HALC0M_PAYMENT_DIRECTION_START_POS, HALC0M_PAYMENT_DIRECTION_START_POS+HALC0M_PAYMENT_DIRECTION_LENGTH)))
				return true;
			}
		return false;
	}
	
	/**
	 * Pomocni metod koji cisti string tj. ako je string prazan vraca null inace vraca nepromenjen string
	 * 
	 * @param value
	 * @return
	 */
	public String clear(String value) {
		if (value == null || value.trim().equals(EMPTY))
			return null;
		return trim(value);
	}
	
	
	/**
	 * double na dve decimale ili ako je bez decimala onda kao int <br>
	 * 2.3333 => 2.33 <br>
	 * 2.3	  => 2.30 <br>
	 * <br>
	 * 2.0	  => 2 <br>
	 * @param d
	 * @return
	 */
	public String doubleToString(double d) {
		return String.format("%." + (d == (int) d ? "0" : "2") + "f", d);
	}
	
	/**
	 * @see #doubleToString(double)
	 * 
	 * @param 
	 * @return
	 */
	public String doubleToString(Double d) {
		if (d == null) {
			return "0.00";
		} else {
			return doubleToString(d.doubleValue());
		}
	}
	
	/**
	 * Metod koji dati JSON formata uklanje sve atribute koji imaju vrednost NULL i vraca tako 
	 * ociscen JSON. exceptAttributes NE CISTI, njih ostavlja sa null vrednostima  
	 * 
	 * @param jsonObject
	 * @param exceptAttributes ove attribute NE DIRA
	 * @return
	 */
	public String clearJSONObjectFromNULLs(String json, String... exceptAttributes) {
		if (json == null) {
			log.error("Nije prodledjen JSON za ciscenje!!!");
			return null;
		}
		json = json.replaceAll("\"[^\"]+?\":null,", "");
		json = json.replaceAll(",\"[^\"]+?\":null", "");
		json = json.replaceAll("\"[^\"]+?\":null", "");
		
		if (!Calculator.getInstance().isEmpty(exceptAttributes)) {
			StringBuilder exceptAttributesBuilder = new StringBuilder();
			for (String attribute : exceptAttributes) {
				exceptAttributesBuilder.append(",\"" + attribute + "\":null");
			}
			String exceptAttributesBuilderContent = exceptAttributesBuilder.toString();
			json = json.replaceAll("(.*)\\}$", "$1" + exceptAttributesBuilderContent + "}");
			json = json.replaceAll("^\\{,(.*)", "{$1");
		}
		return json;
	}
	
	/**
	 * Ako je JSON samo lista bez def atributa koje je pretstavlja, omotavamo je u odgovarajuci format.<br>
	 * npr. dato => [{"name":"nenad"},{"name":"dragan"}] i objectName users pretvaramo u <br>
	 * {"users":[{"name":"nenad"},{"name":"dragan"}]}
	 * 
	 * @param json
	 * @param objectName
	 * @return
	 */
	public String wrapUndefinedJSONList(String json, String objectName) {
		if (json == null || objectName == null) {
			log.error("Nisu prosledjeni odgovarajuci parametri za umotavanje!!!");
			return null;
		}
		String wrappedJson = "{\"" + objectName + "\":" + json + "}"; 
		log.info("Wrapped JSON: " + wrappedJson);
		return wrappedJson;
	}
	
	/**
	 * Parsira dati string u integer. Vrsi trimovanje i hvatanje exceptiona. Ako nesto nije u redu ili je string null vraca NULL.
	 * 
	 * @param stringInteger
	 * @return
	 */
	public Integer parseInt(String stringInteger) {
		if (stringInteger == null) {
			log.error("Greska pri parsiranju stringa u broj, prosledjen NULL");
			return null;
		}
		try {
			return Integer.parseInt(stringInteger.trim());
		} catch (NumberFormatException e) {
			log.error("Dati string: " + stringInteger + " ne moze da se isparsira u broj!!!");
			return null;
		}
	}
	
	/**
	 * Parsira dati string u double. Vrsi trimovanje i hvatanje exceptiona. Ako nesto nije u redu ili je string null vraca NULL.
	 * 
	 * @param stringDouble
	 * @return
	 */
	public Double parseDouble(String stringDouble) {
		if (stringDouble == null) {
			log.error("Greska pri parsiranju stringa u broj, prosledjen NULL");
			return null;
		}
		try {
			return Double.parseDouble(stringDouble.trim());
		} catch (NumberFormatException e) {
			log.error("Dati string: " + stringDouble + " ne moze da se isparsira u broj!!!");
			return null;
		}
	}
	
	
	/**
	 * Parsira dati string u double. Vrsi trimovanje i hvatanje exceptiona. Ako nesto nije u redu ili je string null vraca NULL.
	 * 
	 * @param stringDouble
	 * @return
	 */
	public Float parseFloat(String stringFloat) {
		if (stringFloat == null) {
			log.error("Greska pri parsiranju stringa u broj, prosledjen NULL");
			return null;
		}
		try {
			return Float.parseFloat(stringFloat.trim());
		} catch (NumberFormatException e) {
			log.error("Dati string: " + stringFloat + " ne moze da se isparsira u broj!!!");
			return null;
		}
	}
	/**
	 * Iz prisledjenog stringa discountParam koji je specificnog formata <br>
	 * ##period1#discount1##period2#discount2##period#discount3 ... tj. <br> predstavlja parove (period1, popust1), (period2, popust2), (period3, popust3), ... <br>
	 * izvlacimo popust koji odgovara prosledjenom periodu 
	 * 
	 * @param discountParam
	 * @param period
	 * @return
	 */
	public Double extractDiscountForPeriod(String discountParam, Integer period) {
		if (discountParam != null) {
			// extract vrednosti popusta za dati period
			String stringDouble = discountParam.replaceAll(".*##" + period + "#(.+?)(##.*|$)", "$1");
			return TextUtil.getInstance().parseDouble(stringDouble);
		}
		return null;
	}
	
	/**
	 * discountParam mora biti sledeceg formata: <br> 
	 * ##period1#discount1##period2#discount2##period#discount3 tj. <br> predstavlja parove (period1, popust1), (period2, popust2), (period3, popust3), ... <br>
	 * <br>
	 * Metod radi: <br>
	 * 1. Ako je discountParam null kreira novi string u odgovarajucem formatu za dati period i njegov popust <br>
	 * 2. Ako je postojala vrednost popusta za dati period onda je gazi prosledjenom vrednoscu <br>
	 * 3. Inace apenduje na vec postojeci string jer vrednost popusta nije postojala za dati period
	 * 
	 * @param discountParam
	 * @param period
	 * @param discount
	 * @return novi sadrzaj 
	 */
	public String populateDiscountForPeriod(String discountParam, Integer period, Double discount) {
		if (discountParam == null) {
			// kreiramo iznova string
			discountParam = "##" + period + "#" + discount;
		} else if (extractDiscountForPeriod(discountParam, period) != null) {
			// gazimo staru vrednost popusta za dati period
			discountParam = discountParam.replaceAll("(.*##" + period + ")#.+?(##.*|$)", "$1#" + discount + "$2");
		} else {
			// apendujemo 
			discountParam += "##" + period + "#" + discount;
		}
		return discountParam;
	}
	
	/**
	 * Kreira mapu tipa: period => popust na osnovu prosledjenog discountParam-a koji je specificnog formata <br>
	 * ##period1#popust1##period2#popust2##period3#popust3 ... <br>
	 * NPR. <b>##12#10.0##24#20.0##36#30.0</b>
	 * 
	 * 
	 * @param discountParam
	 * @return
	 */
	public Map<Integer, Double> getMapDiscountsByPeriods(String discountParam) {
		log.info("Kreiramo mapu period=>popust za string: " + discountParam);
		Map<Integer, Double> mapDiscountsByPeriods = new TreeMap<Integer, Double>();
		Matcher matcher = DISCOUNT_PARAM_PATTERN.matcher(discountParam);
		while (matcher.find()) {
			String singleDiscountParam = matcher.group(1); 
			Integer period = parseInt(singleDiscountParam.replaceAll(SINGLE_DISCOUNT_PARAM_REGEX, "$1"));
			Double discount = parseDouble(singleDiscountParam.replaceAll(SINGLE_DISCOUNT_PARAM_REGEX, "$2"));
			mapDiscountsByPeriods.put(period, discount);
			log.info("Dodajemo discount par: " + period + " => " + discount);
		}
		return mapDiscountsByPeriods;
	}
	
	/**
	 * Pomocni metod koji kreira mapu na osnovu sadrzaja string inputParams <br>
	 * inputParams je specificnog formata: <b>paramName1|paramValue1#paramName2|paramValue2#paramName3|paramValue3#</b><br>
	 * mapa ce biti oblika [paramName1 => paramValue1, paramName2 => paramValue2, ...]
	 * 
	 * @return ako string nije validan vraca se NULL
	 */
	public Map<String, String> getInputParamsMap(String inputParams) {
		if (isEmpty(inputParams)) {
			log.warn("Prosledjen prazan inputParams za kreiranje mape paramName paramValue!");
			return null;
		} else if (!inputParams.matches(INPUT_PARAMS_MAP_REGEX)) {
			log.error("Prosledjeni string za kreiranje mape paramName, paramValue nije validan: " + inputParams);
			return null;
		}
		Map<String, String> inputParamsMap = new TreeMap<String, String>();
		String[] paramNameParamValuePairs = inputParams.split(INPUT_SEPARATOR);
		for (String paramNameParamValuePair : paramNameParamValuePairs) {
			String[] paramValue = paramNameParamValuePair.split("\\" + NAME_VALUE_SEPARATOR);
			inputParamsMap.put(paramValue[0], paramValue[1]);
		}
		return inputParamsMap;
	}
	
	/**
	 * Metod koji nam kreira listu flexova potrebnu za ajax prikaz ponude na scheduler stranici.
	 * Svaki flex iz liste predstavlja red tj. jedan TR a svaki njegov flexParam redom predstavlje
	 * po jedan TD u redu
	 * 
	 * @return
	 */
//	public List<FlexDisplay> getAjaxFlexDisplayList(OfferItem offerItem) {
//		Map<Integer, List<OfferItemOption>> mapOptionListByOfferPackageId = offerItem.getMapOptionListByOfferPackageId();
//
//		List<FlexDisplay> flexDisplays = new ArrayList<FlexDisplay>();
//		// kreiramo header tj. flexDisplay kome su napunjeni svi flexParami
//		// koji ce predstavljeti sadrzaje td-ova celija za prikaz tabele
//		FlexDisplay headerFlexDisplay = createHeaderAjaxFlexDisplay(offerItem);
//		flexDisplays.add(headerFlexDisplay);
//		for (Integer offerPackageId : mapOptionListByOfferPackageId.keySet()) {
//			// naizmenicno red za setup 
//			FlexDisplay setupRow = createRowAjaxFlexDisplay(offerItem, mapOptionListByOfferPackageId.get(offerPackageId), OfferItemOption.AMOUNT_TYPE_SETUP);
//			flexDisplays.add(setupRow);
//			// red za price
//			FlexDisplay priceRow = createRowAjaxFlexDisplay(offerItem, mapOptionListByOfferPackageId.get(offerPackageId), OfferItemOption.AMOUNT_TYPE_PRICE);
//			flexDisplays.add(priceRow);
//		}
//		return flexDisplays;
//	}
	
	/**
	 * Kreira header red u strukturi flexDisplaya pocevsi od flexParam1 do flexParam9. Red koji ce biti prikazan
	 * u tableli na pregledu ponuda preko ajax-a na scheduler stranici. Svaki flexParam redom predstavlja
	 * sadrzaj td-a u tabeli za prikaz
	 * 
	 * @return
	 */
//	private FlexDisplay createHeaderAjaxFlexDisplay(OfferItem offerItem) {
//		FlexDisplay flexDisplay = new FlexDisplay();
//		// ispisujmo red tj. kreiram flexdisplay sledece sadrzine
//		// flexParam1, flexParam2, ..., flexParam9 punimo sa
//		// servisName, defaultPeriod M, 0%, period1 meseci, popust1 %, period2 meseci, popust2 %, period3 meseci, popust3 %
//		flexDisplay.setFlexParam1(offerItem.getService());
//		// ovde imamo listu gde su dati moguci periodi tj. 
//		// prvi je deafultAccountingPeriod, ostali su jedan za drugim discount periodi
//		List<Integer> periods = offerItem.getListPeriods();
//		int paramSuffix = 2;
//		for (Integer period : periods) {
//			TextUtil.getInstance().populateFlexDisplayStringParam(flexDisplay, paramSuffix++, period.toString() + "meseci");
//			TextUtil.getInstance().populateFlexDisplayStringParam(flexDisplay, paramSuffix++, TextUtil.getInstance().toString(offerItem.getDiscount(period)) + "%");
//		}
//		return flexDisplay;
//	}
	
	/**
	 * Kreira red (setup ili price) u strukturi flexDisplay pocevsi od flexParam1 do flexParam9. Red 
	 * koji ce biti prikazan kao pregled ponuda preko ajax-a na str, scheduler-a
	 * 
	 * @param offerItemOptions
	 * @param type string 'setup' ili 'price'
	 * @return
	 */
//	private FlexDisplay createRowAjaxFlexDisplay(OfferItem offerItem, List<OfferItemOption> offerItemOptions, String type) {
//		FlexDisplay flexDisplay = new FlexDisplay();
//		// ispisujmo red tj. kreiram flexdisplay sledece sadrzine
//		// flexParam1, flexParam2, ..., flexParam9 punimo sa
//		// nazivPakete, (setup|price), defaultPeriod, defaultIznos, period1, iznos1, period2, iznos2, period3, iznos3 
//		flexDisplay.setFlexParam1(type.equals(OfferItemOption.AMOUNT_TYPE_SETUP) ? offerItemOptions.get(0).getOfferPackage().getName() : " ");
//		// ovde imamo listu gde su dati moguci periodi tj. 
//		// prvi je deafultAccountingPeriod, ostali su jedan za drugim discount periodi
//		List<Integer> periods = offerItem.getListPeriods();
//		int paramSuffix = 2;
//		for (Integer period : periods) {
//			TextUtil.getInstance().populateFlexDisplayStringParam(flexDisplay, paramSuffix++, type);
//			OfferItemOption offerItemOption = findByPeriod(period, offerItemOptions);
//			if (offerItemOption == null)
//				continue;
//			String value = TextUtil.getInstance().toString(offerItemOption.getAmount(type));
//			TextUtil.getInstance().populateFlexDisplayStringParam(flexDisplay, paramSuffix++, value);
//		}
//		return flexDisplay;
//	}
	
	/**
	 * Metod koji na osnovu broja meseci vraca tekst kako se to pisi. <br>
	 * NPR. ulaz: 1 izlaz: 1 mesec <br>
	 * NPR. ulaz: 2 izlaz: 2 meseca  <br>
	 * ...
	 * NPR. ulaz: 5 izlaz: 5 meseci  <br>
	 * ...
	 * @param mounthNumber
	 * @return
	 */
	public String getGrammaticallyCorrectTextNumberOfMonths(int mounthNumber) {
		int remainderTen = mounthNumber % 10;
		int remainderHundred = mounthNumber % 100;
		StringBuilder basis = new StringBuilder(String.valueOf(mounthNumber) +  " mesec");
		if (remainderHundred > 9 && remainderHundred < 21) {
			basis.append('i');
		} else if (remainderTen > 1 && remainderTen < 5) {
			basis.append('a');
		} else if (remainderTen != 1){
			basis.append('i');
		}
		return basis.toString();
	} 
	
	/**
	 * Iz liste offerItemOptions nalazi onu opciju koja ima dati period
	 * 
	 * @param period
	 * @param offerItemOptions
	 * @return
	 */
//	public OfferItemOption findByPeriod(int period, List<OfferItemOption> offerItemOptions) {
//		if (Calculator.getInstance().isEmpty(offerItemOptions)) {
//			return null;
//		}
//		for (OfferItemOption offerItemOption : offerItemOptions) {
//			if (offerItemOption.getPeriod().intValue() == period)
//				return offerItemOption;
//		}
//		return null;
//	}
	
	/**
	 * Popunjave polje flexParam'N' sa vrednoscu 'value' flexDisplay-a gde je 'N' paramSuffix. Zgodno
	 * ako iteriramo pa jedan za drugim postavljamo parametre. tj. <br>
	 * setFlexParam1() <br>
	 * setFlexParam2() <br>
	 * setFlexParam3() ....
	 * 
	 * @param flexDisplay
	 * @param paramSuffix
	 * @param value
	 */
//	public void populateFlexDisplayStringParam(FlexDisplay flexDisplay, Integer paramSuffix, String value) {
//		try {
//			Method method = FlexDisplay.class.getMethod("setFlexParam" + paramSuffix, String.class);
//			method.invoke(flexDisplay, value);
//		} catch (Exception e) {
//			log.error("Greska pri izvrsavanju metode: setFlexParam" + paramSuffix + " pri punjenju value: " + value);
//		}
//	}
	
	/**
	 * Metod proverava da li je trenutni profil produkcioni i tada vraca true inace vraca false.
	 * 
	 * @return
	 */
	public boolean isProductionProfile() {
		String profile = PropertiesUtil.getInstance().getProfileName();
		if (profile == null || !profile.equals("production")) {
			log.info("Za profil: " + profile + " se ne izvrsava ova akcija");
			return false;
		}
		return true;
	}
	
	/**
	 * Pomocni metod koji kreira u pravilnom formatu content-disposition response-header polje 
	 * koje treba da se ponudi klijentu kao naziv file-a sa sve ekstenziom koji ce snimiti. <br>
	 * (Kad hoce operater da snimi excel na lokalnu masinu, pa izadje meni sa opicijama za snimanje odredjenog tipa file-a)
	 * 
	 * @param fileName
	 * @return
	 */
	public String createContentDisposition(String fileName) {
		return CONTENT_DISPOSITION_PATTERN.replaceAll("(.*)##(.*)", "$1" + fileName + "$2");
	}
	
	/**
	 * Za ip adresu u ip pool obliku <br> 
	 * NPR. 127.0.0.1/30 izracunava opseg 2 ^ (32 - 30) <br>
	 * NPR. 127.0.0.1/29 izracunava opseg 2 ^ (32 - 29) <br>  
	 * Inace ako je obicna, vracamo 1
	 * @param ipAddress
	 * @return
	 */
//	public Integer calculateIpPoolRange(String ipAddress) {
//		if (ipAddress.matches(ValidationsUtil.IP_POOL_REGEX)) {
//			Integer suffix = parseInt(ipAddress.substring(ipAddress.indexOf(SLASH) + 1));
//			return (int) Math.pow(2, 32 - suffix);
//		} else {
//			return 1;
//		}
//	}
	
	/**
	 * Ispravan kamila naziv prebacuje u naziv sa donjim crtama<br>
	 * NPR.<br>
	 * autoReminderTime => auto_reminder_time
	 * autoURLconnection => auto_url_connection
	 * 
	 * @param camelCase
	 * @return
	 */
	public String toUnderscoreNotation(String camelCase) {
		if (isEmpty(camelCase))
			return null;
		StringBuffer stringBuffer = new StringBuffer();
		char[] camelCaseChars = camelCase.toCharArray();
		boolean wasMultiplUpperCaseChars = false;
		for (int i = 0; i < camelCaseChars.length; i++) {
			if (Character.isUpperCase(camelCaseChars[i])) {
				stringBuffer.append('_');
				stringBuffer.append(Character.toLowerCase(camelCaseChars[i]));
				// provera da li imamo vezano vise velikih slova
				for (int j = i + 1; j < camelCaseChars.length; j++, i++) {
					if (Character.isUpperCase(camelCaseChars[j])) {
						stringBuffer.append(Character.toLowerCase(camelCaseChars[j]));
						wasMultiplUpperCaseChars = true;
					} else {
						break;
					}
				}
				if (wasMultiplUpperCaseChars) {
					stringBuffer.append('_');
					wasMultiplUpperCaseChars = false;
				}
			} else {
				stringBuffer.append(camelCaseChars[i]);
			}
		}
		// na kraju uklanjamo '_' na pocetku stringa ako postoji
		return stringBuffer.toString().replaceAll("^_(.*)$", "$1");
	}

	/**
	 * Metoda koja deli string po delimitereu i parsira delove u Int
	 * @param input
	 * @param delimitter
	 * @return
	 */
	public List<Integer> splitStringToIntegers(String input, char delimitter, List<Integer> available){
		List<Integer> result = null;
		if(input != null){
			List<String> splitted = splitString(input, delimitter+"");
			
			if(splitted != null && splitted.size()>0){
				result = new ArrayList<Integer>();
				for(String candidate : splitted){
					Integer number = parseInt(candidate);
					if(number != null){
						if(available == null || Calculator.getInstance().isElementInList(number, available)){
							result.add(number);
						}
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Da li liste imaju iste elemente (ocekuje se da su elementi unitar listi unique)
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean isListsHasSameElements(List first, List second) {
		boolean firstEmpty = Calculator.getInstance().isEmpty(first);
		boolean secondEmpty = Calculator.getInstance().isEmpty(second);
		if (!firstEmpty && !secondEmpty) {
			Set firstSet = new HashSet(first);
			Set secondSet = new HashSet(second);
			return firstSet.containsAll(secondSet) && secondSet.containsAll(firstSet);
		} else if (firstEmpty && secondEmpty) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Da li ugvor ima IPTV (ili je iz IPTV servisa ili u sklopu servisa ima IPTV)
	 * 
	 * @param agreement
	 * @return
	 */
//	public boolean hasIptv(Agreement agreement) {
//		boolean hasIPTV = false;
//		Integer agreementId = null;
//		if (agreement == null) {
//			log.error("Nema obaveznog parametra!!!");
//		}else{
//			agreementId = agreement.getId();
//			if (hasIptv(agreement.getServicePackage())){
//
//			log.info("Ugovor sa ID: " + agreementId + " je na IPTV paketu");
//			hasIPTV = true;
//			}else if(agreement.getParentAgreement()!= null){
//				if(hasIptv (agreement.getParentAgreement().getServicePackage())){
//					log.info("Ugovor sa ID: "+ agreementId + " je u bundle paketu koji sadrzi IPTV");
//					hasIPTV = true;
//				}
//			}
//		}
//		log.info("Ugovor sa ID: "+ agreementId + (hasIPTV ?" IMA": " NEMA")+ " IPTV.");
//		return hasIPTV;
//	}
//	
	/**
	 * Da li je brend domen paket
	 * 
	 * @param agreement
	 * @return
	 */
//	public boolean isBrandDomainPackage(Agreement agreement) {
//		if (agreement == null) {
//			log.info("Nedostaje obavezan parametar!!!");
//			return false;
//		}
//		Package servicePackage = agreement.getServicePackage();
//		return servicePackage.getService().getName().equals(ServiceService.DOMAIN_REGISTRATION_SERVICE_NAME) && servicePackage.getName().contains("@");
//	}
	
	/**
	 * Da li je ugovor za IPTV koji je fake bundle
	 * 
	 * @param agreement
	 * @return
	 */
//	public boolean isFakeIptvBundle(Package servicePackage) {
//		boolean fakeIptvBundle = false;
//		if (servicePackage != null) {
//			String packageName = servicePackage.getName();
//			String serviceName = servicePackage.getService().getName();
//			boolean validService = Calculator.getInstance().isElementInList(serviceName, Arrays.asList(new String[] {ServiceService.ADSL_SERVICE_NAME, ServiceService.WIFI_SERVICE_NAME, ServiceService.IPTV_SERVICE_NAME}));
//			if(validService){
//				boolean iptvAgreement = packageName.toUpperCase().contains(ServiceService.IPTV_SERVICE_NAME);
//				fakeIptvBundle = iptvAgreement;
//			}
//			
//		}
//		return fakeIptvBundle;		
//	}
	
	/**
	 * Da li je rec o DUO IPTV paketu (ADSL IPTV paketu)?
	 * 
	 * @param servicePackage
	 * @return
	 */
	public boolean isDuoIptv(Package servicePackage) {
		String packageName = servicePackage.getName();
		return packageName.toUpperCase().contains("DUO");
	}
	
	/**
	 * Da li IPTV paket ili paket koji sadrzi IPTV?
	 * NPR. (ADSL sa nabudzenim IPTV)
	 * 
	 * @param agreement
	 * @return
	 */
//	public boolean hasIptv(Package servicePackage) {
//		if (servicePackage == null) {
//			log.error("Nema obaveznog parametra!!!");
//			return false;
//		}
//		if (servicePackage.getService().getName().equals(ServiceService.IPTV_SERVICE_NAME)) {
//			return true;
//		}
//		return servicePackage.getName().toUpperCase().contains(ServiceService.IPTV_SERVICE_NAME);
//	}
//	
	/**
	 * Da li dati uredjaj pripada grupi STB uredjaja
	 * 
	 * @param equipmentModel
	 * @return
	 */
//	public boolean isStbEquipment(EquipmentDevice equipmentDevice) {
//		if (equipmentDevice == null || equipmentDevice.getModel() == null || equipmentDevice.getModel().getEquipmentModelGroup() == null) {
//			return false;
//		}
//		return EquipmentModelGroupService.STB_DEVICE.equals(equipmentDevice.getModel().getEquipmentModelGroup().getName());
//	}
	
	/**
	 * Metoda za generisanje username-a za ADSL
	 * 
	 * poslovnim se na broj ispred dodaje A, fizickim licima C sa izuzetkom IPTV korisnika kojima se dodaje T
	 * 
	 * @param clientType
	 * @param packageName
	 * @param areaCode
	 * @param adslPhone
	 * @return
	 */
//	public String generateAdslUsername(String clientType, String packageName, String areaCode, String adslPhone) {
//		String prefix = ClientService.CLIENT_TYPE_COMPANY.equals(clientType) ? "A" : "C";
//		if(packageName != null && packageName.toUpperCase().contains(ServiceService.IPTV_SERVICE_NAME)){
//			prefix = "T";
//		}
//		return prefix + areaCode.trim() + adslPhone.trim();
//	}
	
	/**
	 * Uklanja sekvencu sa kraja stringa
	 * 
	 * @param string
	 * @param endingSequence
	 * @return
	 */
	public String trimEndingStringSequence(String string, String endingSequence) {
		if (isEmpty(string)) {
			return string;
		}
		return string.replaceAll("(.*)" + endingSequence + "\\s*$", "$1");
	}
	
	/**
	 * Da li su sve vrednosti za svaki kljuc neprazne.
	 * 
	 * @param map
	 * @return
	 */
	public boolean checkAllValuesSet(Map<String, String> map) {
		if (Calculator.getInstance().isEmpty(map)) {
			return true;
		}
		for (Entry<String, String> entry : map.entrySet()) {
			if (isEmpty(entry.getValue())) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Metoda koja od liste objekata s nazivom izvlaci listu naziva
	 * @param nameables
	 * @return
	 */
//	public List<String> getNames(List<Nameable> nameables){
//		List<String> result = new ArrayList<String>();
//		if(nameables != null && nameables.size()>0){
//			for(Nameable nameable : nameables){
//				result.add(nameable.getName());
//			}
//		}
//		return result;
//	}
	
	public static enum TelekomPackageSpeed {
		UNKNOWN(-1, "-", "-"),
		MINI(1, "1536", "256"),
		MIDI4(2, "4096", "1024 "),
		MAXI6(4, "6144", "1024"),
		MAXI8(5, "8192", "1024"),
		MEGA16(6, "16384", "1024"),
		MIDI(7, "5120", "1024"),
		MAXI(8, "10240", "1024"),
		MEGA(9, "20480", "1024");
		
		private int telekomPackage;
	    private String downloadSpeed;
	    private String uploadSpeed;
	    
	    private static Map<Integer, TelekomPackageSpeed> codeToSpeedMapping;
 
		private TelekomPackageSpeed(int telekomPackage, String downloadSpeed, String uploadSpeed) {
			this.telekomPackage = telekomPackage;
			this.downloadSpeed = downloadSpeed;
			this.uploadSpeed = uploadSpeed;
		}
	    
	    public static TelekomPackageSpeed getSpeed(int telekomPackage) {
	    	if(codeToSpeedMapping == null) {
	    		initMapping();
	    	}
	    	TelekomPackageSpeed result = codeToSpeedMapping.get(telekomPackage);
	    	return result!=null?result:codeToSpeedMapping.get(-1);
	    }
	    
	    private static void initMapping() {
	    	codeToSpeedMapping = new HashMap<Integer, TelekomPackageSpeed>();
	        for (TelekomPackageSpeed s : values()) {
	        	codeToSpeedMapping.put(s.telekomPackage, s);
	        }
	    }

		public int getTelekomPackage() {
			return telekomPackage;
		}

		public String getDownloadSpeed() {
			return downloadSpeed;
		}

		public String getUploadSpeed() {
			return uploadSpeed;
		}
	    
	    
	}
	
	/**
	 * Metoda koja ispituje da li je prosledjeni paket veratov.
	 * @param packageName
	 * @return
	 */
//	public boolean isVeratPackage(String packageName) {
//		for(String veratPackageName : PackageService.VERAT_PACKAGES_SET) {
//			if(veratPackageName.equals(packageName)) {
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	public String getAppropriateServiceName(String serviceName) {
//		for (SERVICE_NAME name : SERVICE_NAME.values()) {
//			if (name.name().equals(serviceName.toUpperCase())) {
//				return name.toCorrectName();
//			}
//		}
//		return null;
//	}
	
	/**
	 * Mrezni grupu-lokalni prefix prevodi u ino prefix <br>
	 * NPR. 012 => 38112
	 * 
	 * @param localePhonePrefix
	 * @return
	 */
//	public String parseLocalePhonePrefixToInternationalPhonePrefix(String localePhonePrefix) {
//		if (isEmpty(localePhonePrefix) || !localePhonePrefix.startsWith(ValidationsUtil.LOCALE_PHONE_NUMBER_PREFIX)) {
//			log.info("Nije prosledjen odgovarajuci ulazni parametar: " + localePhonePrefix);
//			return localePhonePrefix;
//		}
//		return localePhonePrefix.replaceFirst(ValidationsUtil.LOCALE_PHONE_NUMBER_PREFIX, ValidationsUtil.INTERNATIONAL_PHONE_NUMBER_PREFIX);
//	}
	
	/**
	 * Metoda za konvertovanje broja telefona u internacionalni format
	 * @param phoneNumber
	 * @return
	 */
//	public String parseFixedPhoneNumberToInternational(String phoneNumber){
//		String result = null;
//		if(phoneNumber != null){
//			if (ValidationsUtil.getInstance().checkMatches(phoneNumber, ValidationsUtil.INTERNATIONAL_PHONE_NUMBER_REGEXP)){
//				log.info("Broj "+phoneNumber + " je vec u internacionalnom formatu");
//				result = phoneNumber;
//			}else if(ValidationsUtil.getInstance().checkMatches(phoneNumber, ValidationsUtil.PHONE_NUMBER_REGEXP)){
//				log.info("Broj "+phoneNumber + " je u ino formatu i skidamo mu prefix");
//				result = phoneNumber.replaceFirst(LOCAL_PHONE_PREFIX, INTERNATIONAL_PHONE_PREFIX);				
//			}else{
//				log.warn("Neregularan format fiksnog telefona "+ phoneNumber);
//			}
//		}else{
//			log.warn("Nije prosledjen broj telefona za proveru validnosti");
//		}
//		return result;
//	}
	
	/**
	 * Metoda za konvertovanje broja telefona izinternacionalnog u lokanli format
	 * @param phoneNumber
	 * @return
	 */
//	public String parseFixedPhoneNumberToLocal(String phoneNumber){
//		String result = null;
//		if(phoneNumber != null){
//			if (ValidationsUtil.getInstance().checkMatches(phoneNumber, ValidationsUtil.PHONE_NUMBER_REGEXP)){
//				log.info("Broj "+phoneNumber + " je vec u lokalnom formatu");
//				result = phoneNumber;
//			}else if(ValidationsUtil.getInstance().checkMatches(phoneNumber, ValidationsUtil.INTERNATIONAL_PHONE_NUMBER_REGEXP)){
//				log.info("Broj "+phoneNumber + " je u ino formatu i skidamo mu prefix");
//				result = phoneNumber.replaceFirst(INTERNATIONAL_PHONE_PREFIX, LOCAL_PHONE_PREFIX);				
//			}else{
//				log.warn("Neregularan format fiksnog telefona "+ phoneNumber);
//			}
//		}else{
//			log.warn("Nije prosledjen broj telefona za proveru validnosti");
//		}
//		return result;
//	}

	/**
	 * Metoda za parsiranje polja history(iz objekta uplate) iz porte da bi nasli id usera koji je izvrsio
	 * nalazimo id tako sto trazimo rec 'USER=' i posle nje se nalazi id usera
	 * 
	 * @param cdrHistory
	 * @return
	 */
	public Integer parsePortaUserId(String cdrHistory) {
		String parse = null;
		parse = cdrHistory.substring(cdrHistory.indexOf("USER=") + 5);
		if (parse.contains("\t"))
			parse = parse.substring(0, parse.indexOf("\t"));
		log.info("Parse: " + parse);
		return parseInt(parse);
	}
	
	/**
	 * Kreira nisku blanka duzine length
	 * 
	 * @param length
	 * @return
	 */
	public String[] createBlankoArray(int length) {
		String[] array = new String[length];
		for (int i = 0; i < array.length; i++) {
			array[i] = BLANKO;
		}
		return array;
	}
	
	public String translateEmailSeparatorFromSemicolonToComa(String email){
		if(email != null){
			email = email.replaceAll(TextUtil.SEMICOLON_SEPARATOR, TextUtil.COMMA_SEPARATOR);
		}
		return email;
	}
	
	@SuppressWarnings("rawtypes")
	public String toString(List list) {
		if (list == null) {
			return "null";
		} else {
			return list.toString();
		}
	}
	
//	public String transliterateToEnglishAlphabet(String text) {
//		return StringUtils.replaceEach(text, SERBIAN_SPECIAL_CHARACTERS_LAT, SERBIAN_NO_DECORATION_CHARACTERS_LAT);
//	}
	
	
	public List<String[]> toStringArrayList(List<Object[]> objectArrayList, boolean nullToEmpty){
		List<String[]> result = null;
		if(!Calculator.getInstance().isEmpty(objectArrayList)){
			result = new ArrayList<String[]>();
			for(Object[] objArray: objectArrayList){
				if(!Calculator.getInstance().isEmpty(objArray)){
					String[] resultArray = new String[objArray.length];
					int i = 0;
					for(Object obj : objArray){
						resultArray[i++] = toStringSafe(obj, nullToEmpty);
					}
					result.add(resultArray);
				}
			}
		}
		return result;
	}
	
	
	private String toStringSafe(Object obj, boolean toEmpty){
		if (obj == null) {
			return toEmpty?EMPTY:null;
		} else {
			return obj.toString();
		}
	}
	
	public String formatThreshold(String originalThreshold){
		String result = EMPTY;
		String extension = EMPTY;
		if(originalThreshold != null && !isEmpty(originalThreshold)){
			if(originalThreshold.contains(PERCENT)){
				extension = PERCENT;
				originalThreshold = originalThreshold.replace(PERCENT, EMPTY);
			}
			result = doubleToString(Calculator.getInstance().getDoubleValue(originalThreshold));
		}
		return result+extension;
	}
	
	/**
	 * Metoda koja proverava da li inicijalni tekst sadrzi neki od elemenata
	 * @param initialText
	 * @param toRemove
	 * @return
	 */
	public boolean containsAny(String initialText, String... elements){
		boolean contains = false;
		if(initialText != null){
			if(elements != null && elements.length>0){
				for(String element : elements){
					contains = initialText.contains(element);
					if(contains){
						log.info("Ulazni string "+initialText+" sadrzi: "+element);
						break;
					}
				}
			}
		}
		log.info("Ulazni string "+initialText+ (contains?"": "NE ")+"sadrzi trazene elemente");
		return contains;
	}
	
	/**
	 * Listu sendTo emails pretvara u string u potrebnom formatu za sendTo param mailSendera
	 * 
	 * @param sendToEmails
	 * @return
	 */
	public String createSendToEmailString(String... sendToEmails) {
		StringBuilder sendToEmailBuilder = new StringBuilder();
		if (sendToEmails != null) {
			for (int i = 0; i < sendToEmails.length - 1; i++) {
				String email = sendToEmails[i]; 
				sendToEmailBuilder.append(email);
				sendToEmailBuilder.append(SEMICOLON_SEPARATOR);
			}
			sendToEmailBuilder.append(sendToEmails[sendToEmails.length - 1]);
		}
		return sendToEmailBuilder.toString();
	} 
	
	public String toASCII(String input){
		return java.net.IDN.toASCII(input);
	}
	
	public String toPunycode(String input){
		return java.net.IDN.toUnicode(input);
	}
	
	/**
	 * Metoda za proveru validnosti headera
	 * @param columnMap
	 * @return
	 */
	public boolean isColumnMapValid(Map<String, Integer> columnMap, String[] HEADER){
		for(String title : HEADER){
			if(columnMap.get(title) == null){
				return false;
			}
		}
		return true;
	}
	
	public String convertIntegerToString(Integer number){
		String result = "0";
		if(number != null){
			result = number +"";
		}
		return result;
	}
	
	public String extractStringIntegerValueFromMap(Map<String, Integer> map, String key){
		Integer value = null;
		if(map != null && key != null){
			value = map.get(key);
		}
		return convertIntegerToString(value);
	}
	
	public String extractStringDoubleValueFromMap(Map<String, Double> map, String key){
		Double value = null;
		if(map != null && key != null){
			value = map.get(key);
		}
		if(value == null){
			value = 0d;
		}
		return doubleToString(value);
	}
	
	public String trimToNoNullAndReplaceBlanksWithUnderscore (String input){
		String output = input != null? input.trim().replaceAll("\\s+", UNDERSCORE): "";
		return output;
	}
	
	/**
	 * Provera da li je korisnik uneo ispravnu captchu-u (nl.captcha.Captcha)
	 * @param captchaResponse
	 * @return
	 */
//	public Map<String, String> extractRequestParameters(List<String> names) {
//		Map<String, String> parameterMap = new HashMap<String, String>();
//		HttpServletRequest request = ServletActionContext.getRequest();
//		if(request != null && names != null && names.size()>0){
//			for (String paramName : names) {
//				String value = request.getParameter(paramName);
//				parameterMap.put(paramName, value);
//			}
//		}
//		return parameterMap;
//	}
	
	public List<String> generateStringArray(String prefix, int count){
		List<String> list = new ArrayList<String>();
		for(int i=0; i<count ; i++){
			list.add(prefix+i);
		}
		return list;
	}
	
	public String generate2GoIprvUsername (String input){
		String output = input.replaceFirst(IptvService.SUBSCRIBER_UID_PREFIX, EMPTY).concat(IptvService.SUBSCRIBER_2GO_SUFIX);
		return output;
	}
	
	public String join(String[] parts, String appender) {
		StringBuilder joinedString = new StringBuilder(0);
		if (Calculator.getInstance().isEmpty(parts)) {
			log.error("Nije prosledjena niska za spajanje!!!");
		} else {
			joinedString = new StringBuilder();
			if (appender == null) {
				appender = "";
			}
			for (int i = 0; i < parts.length - 1; i++) {
				joinedString.append(parts[i]);
				joinedString.append(appender);
			}
			joinedString.append(parts[parts.length - 1]);
		}
		return joinedString.toString();
	}
	
	public String createEmailAddress (String username, String domain){
		String address = TextUtil.EMPTY;
		if(notEmpty(username) && notEmpty(domain)){
			address = username + AT + domain;
		}
		return address;
	}
	
}

