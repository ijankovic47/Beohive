package net.beotel.util;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

import javax.persistence.Query;


import org.apache.log4j.Logger;

//import org.apache.commons.lang.WordUtils;
//import com.google.common.collect.ImmutableMap;
//import com.google.common.util.concurrent.AtomicDouble;
//
//import net.beotel.helper.Label;
//import net.beotel.helper.NameValueHelper;
//import net.beotel.model.Operator;
//import net.beotel.model.Package;
//import net.beotel.model.PhoneInvoice;
//import net.beotel.model.PromotionParamValue;
//import net.beotel.model.PromotionTemplateParam;
import net.beotel.model.template.Idable;
//import net.beotel.model.template.Nameable;
//import net.beotel.util.AbstractClassRecognizer.Recognizer;


public class Calculator {

	private static Logger log = Logger.getLogger(Calculator.class);
	private static Calculator instance;
	private static final String shortDateFormat = "dd-MM-yy";
	public static SimpleDateFormat shortDateFormatter = new SimpleDateFormat(shortDateFormat);
	public static final int ADSL_RAND_PASSWORD_LENGTH = 10;
	public static final String RANDOM_GENERATOR_CHARACTERS =  "abcdefghjkmnprstuvwxyzACDEFHIJKLMNOPQRSTUVWXYZ234579";
	public static final String RANDOM_GENERATOR_NUMBER_CHARACTERS = "23456789";
	public static final String RANDOM_GENERATOR_UPPER_CHARACTERS = "ACDEFHIJKLMNOPQRSTUVWXY";
	public static final String RANDOM_GENERATOR_LOWER_CHARACTERS = "abcdefghjkmnprstuvwxyz";
	public static final String RANDOM_GENERATOR_SPECIAL_CHARACTERS = "#!$";
	public static final String CONTROL_NO_SEPARATOR = "-";
	public static final DecimalFormat FOUR_DECIMAL_FORMAT = new DecimalFormat("#.####");
	public static final DecimalFormat TWOO_DECIMAL_FORMAT = new DecimalFormat("#.##");
	
//	private static final Map<Class<?>, Class<?>> WRAPPERS_TO_PRIMITIVES
//	    = new ImmutableMap.Builder<Class<?>, Class<?>>()
//	      .put(Boolean.class, boolean.class)
//	      .put(Byte.class, byte.class)
//	      .put(Character.class, char.class)
//	      .put(Double.class, double.class)
//	      .put(Float.class, float.class)
//	      .put(Integer.class, int.class)
//	      .put(Long.class, long.class)
//	      .put(Short.class, short.class)
//	      .put(Void.class, void.class)
//	      .build();
	
	public static Calculator getInstance() {
		if (instance == null) {
			instance = new Calculator();
		}
		return instance;
	}

	/**
	 * Komparator za sortiranje Mape po vrednosti
	 * @author bojan
	 *
	 */
//    public static class AtomicValueDescendingComparator implements Comparator<String> {
//
//        Map<String, AtomicDouble> base;
//
//        public AtomicValueDescendingComparator(Map<String, AtomicDouble> base) {
//            this.base = base;
//        }
//
//        @Override
//        public int compare(String a, String b) {
//            if (base.get(b).doubleValue() >= base.get(a).doubleValue()) {
//                return 1;
//            } else {
//                return -1;
//            }
//        }
//    }
//	

	/**
	 * Racuna dve kontrolne cifre po mod 97 na osnovu broja telefona koji se koristi u pozivu na broj
	 * 
	 * @param phone
	 * @return
	 */
	public String getSpecialNo(String inputNumber) {
		while (inputNumber.startsWith("0"))
			inputNumber = inputNumber.replaceFirst("0", "");
		Long number= 0l;
		try{
			number = Long.valueOf((inputNumber.trim()));
		}
		catch (Exception ex){
			return null;	
		}
		Double a = Double.valueOf(number)*100/97;
		Long d = (98 - Math.round((a-Math.floor(a))*97));
		int k = d.intValue();

		String code97 = ((k<10)?"0":"")+k;
		return code97;
	}
	
	/**
	 * Metoda koja generise poziv na broj za prvu voip uplatu
	 * format: 11+datum+0+agreement_id
	 * @param agreementId
	 * @return
	 */
//	public String generateFirstVoipInvocationNumber(String agreementId){
//		Date date = new Date();
//		String dateFormated = shortDateFormatter.format(date);
//		String datePart =  dateFormated.replaceAll("-", "");
//		String temp = ValidationsUtil.VOIP_SERVICE_CODE+ datePart+"0"+agreementId; 
//		String controlNo = getSpecialNo(temp);
//		return controlNo+ CONTROL_NO_SEPARATOR + temp;
//		
//	}

	
	/**
	 * Metoda za prikaz double sa 2 decimale
	 * @param d
	 * @return
	 */
	public String doubleToString2(double d) {  
		DecimalFormat fmt = new DecimalFormat("0.00"); 
		fmt.setMinimumFractionDigits(2);
		String string = fmt.format(d);  
		return string;  
	} 
	
	/**
	 * Metoda koja generise naziv Batcha za Calling kartice kod prebacivanja na drugog customera
	 * @param oldBatchName
	 * @return
	 */
	public String generateBatchName(String oldBatchName){
		String newBatchName = "";
		log.info("Input batch name: "+ oldBatchName);
		if(oldBatchName.startsWith("CC_")){
			newBatchName = oldBatchName.replaceFirst("CC_", "CC1_");
			log.info("Output batch name: "+newBatchName);
			return newBatchName;
		}
		String tempOrder = oldBatchName.substring(2, oldBatchName.indexOf('_'));
		log.info("Temp order: "+ tempOrder);
		int replacementInt = Integer.parseInt(tempOrder)+1; 
		newBatchName = oldBatchName.replaceFirst(tempOrder, replacementInt+"");
		log.info("Output batch name: "+newBatchName);
		return newBatchName;
	}
	
    /**
     * Metoda za generisanje random sekvence duzine length
     * @param length
     * @return
     */
	public String randomGenerator(int length){
		return randomGenerator(length, RANDOM_GENERATOR_CHARACTERS);
	}
	
	public String randomNumberGenerator(int length){
		return randomGenerator(length, RANDOM_GENERATOR_NUMBER_CHARACTERS);
	}
	
	/**
	 * Generise pseudo slucajnu nisku koja sadrzi odredjen broj razlicitih tipova karaktera
	 * 
	 * @param specialChars
	 * @param upperChars
	 * @param lowerChars
	 * @param numbers
	 * @return
	 */
	public String randomGenerator(int specialChars, int upperChars, int lowerChars, int numbers){
		String randomCharsWithOrderSaving = randomGeneratorWithOrderSaving(specialChars, upperChars, lowerChars, numbers);
		return shuffle(new StringBuffer(randomCharsWithOrderSaving));
		}

	public String randomGeneratorWithOrderSaving(int specialChars, int upperChars, int lowerChars, int numbers){
		StringBuffer buff = new StringBuffer("");
		buff.append(randomGenerator(specialChars, RANDOM_GENERATOR_SPECIAL_CHARACTERS));
		buff.append(randomGenerator(lowerChars, RANDOM_GENERATOR_LOWER_CHARACTERS));
		buff.append(randomGenerator(upperChars, RANDOM_GENERATOR_UPPER_CHARACTERS));
		buff.append(randomGenerator(numbers, RANDOM_GENERATOR_NUMBER_CHARACTERS));
		return buff.toString();
	}
	
	public String randomGenerator(int upperChars, int numbers){
		StringBuffer buff = new StringBuffer("");
		buff.append(randomGenerator(upperChars, RANDOM_GENERATOR_UPPER_CHARACTERS));
		buff.append(randomGenerator(numbers, RANDOM_GENERATOR_NUMBER_CHARACTERS));
		return buff.toString();
	}
	
	private String randomGenerator(int length, String charSet) {
		Random rn = new Random();
		StringBuffer buff = new StringBuffer("");
		for (int i=0;i<length;i++){
			buff.append(charSet.charAt(rn.nextInt(charSet.length())));
		}
		log.info("Izgenerisan slucajan niz: "+ buff);
		return buff.toString();
	}
	
	/**
	 * Mesa sadrzaj stringBuffer-a
	 * 
	 * @param stringBuffer
	 * @return
	 */
	private String shuffle(StringBuffer stringBuffer) {
		Random random = new Random();
		for (int i = stringBuffer.length() - 1; i > 1; i--) {
			int swapWith = random.nextInt(i);
			char tempChar = stringBuffer.charAt(swapWith);
			stringBuffer.setCharAt(swapWith, stringBuffer.charAt(i));
			stringBuffer.setCharAt(i, tempChar);
		}
		return stringBuffer.toString();
	}

	/**
	 * Metoda za ispis broja (prevod broja u String)
	 * @param number
	 * @return
	 */
	public String numberAsString(int number){
		String result = "-";
		switch(number){
			case 1: result = "jedan"; break;
			case 2: result = "dva"; break;
			case 3: result = "tri"; break;
			case 4: result = "cetiri"; break;
			case 5: result = "pet"; break;
			case 6: result = "sest"; break;
			case 7: result = "sedam"; break;
			case 8: result = "osam"; break;
			case 9: result = "devet"; break;
			case 10: result = "deset"; break;
			case 11: result = "jedanaest"; break;
			case 12: result = "dvanaest"; break;
			case 13: result = "trinaest"; break;
			case 14: result = "cetrnaest"; break;
			case 15: result = "petnaest"; break;
			case 16: result = "sesnaest"; break;
			case 17: result = "sedamnaest"; break;
			case 18: result = "osamnaest"; break;
			case 19: result = "devetnaest"; break;
			case 20: result = "dvadeset"; break;
			case 21: result = "dvadeset jedan"; break;
			case 22: result = "dvadeset dva"; break;
			case 23: result = "dvadeset tri"; break;
			case 24: result = "dvadeset cetiri"; break;
			case 25: result = "dvadeset pet"; break;
		}
		return result;
	}
	
	/**
	 * Metoda za secenje Floata pri prebacivanju na double
	 * 
	 * @param f
	 * @return
	 */
	public Double floatToDouble(Float f) {  
		DecimalFormat fmt = new DecimalFormat("0.00"); 
		fmt.setMinimumFractionDigits(2);
		Double d = Double.valueOf(fmt.format(f));
		return d;  
	} 
	
	/**
	 * Metoda za generisanje poziva na broj
	 * @param serviceNumber
	 * @param agreementNumber
	 * @return
	 */
//	public String generateInvocationNumber(String serviceNumber, String agreementNumber){
//		while(agreementNumber.length()<8){
//			agreementNumber = "0"+agreementNumber;
//		}
//		String temp = ValidationsUtil.INDIVIDUAL_DOCUMENT_NO_PREFIX + serviceNumber + agreementNumber; 
//		String controlNo = getSpecialNo(temp);
//		return controlNo+ CONTROL_NO_SEPARATOR + temp;
//	}

	/**
	 * Metoda za generisanje poziva na broj za narudzbenicu
	 * @param serviceNumber
	 * @param agreementNumber
	 * @return
	 */
//	public String generateOrderListInvocationNumber(String orderListNumber, boolean company){
//		while(orderListNumber.length()<8){
//			orderListNumber = "0"+orderListNumber;
//		}
//		String temp = (company?ValidationsUtil.COMPANY_ORDER_LIST_PREFIX:ValidationsUtil.INDIVIDUAL_DOCUMENT_NO_PREFIX) + ValidationsUtil.ORDER_PAYMENT_CODE + orderListNumber; 
//		String controlNo = getSpecialNo(temp);
//		return controlNo+ CONTROL_NO_SEPARATOR + temp;
//	}
	/**
	 * Metoda za razdvajanje pozivnog broja i broja telefona
	 * @param phoneNumber
	 * @return niz[0]=pozivni broj, niz[1]=broj telefona 
	 */
	public String[] splitPhoneNumber(String phoneNumber){
		if(phoneNumber!=null && phoneNumber.length()>7){
			String[] splitted = new String[2];
			if(phoneNumber.charAt(3)!='0'){
				splitted[0] = phoneNumber.substring(0, 3);
				splitted[1] = phoneNumber.substring(3);
				return splitted;
			}
			else {
				splitted[0] = phoneNumber.substring(0, 4);
				splitted[1] = phoneNumber.substring(4);
				return splitted;
			}
		}
		return null;
	}


	/*polje se formira kao header1*detail1|header2*detail2....*/
	public String[][] splitDetailsForMaster(String showDetails) {
		String[][] result = null;
		List<String> headers = new ArrayList<String>();
		List<String> data = new ArrayList<String>();
		String[] firstSplit = showDetails.split("\\|\\|");
		if(firstSplit!= null && firstSplit.length>0){
			for(String row: firstSplit){
				String[] secondSplit = row.split("\\|");
				if(secondSplit!= null && secondSplit.length==2){
					headers.add(secondSplit[0]);
					data.add(secondSplit[1]);
				}
			}
		}
		if(headers.size()>0){
			result = new String[2][headers.size()];
			for(int i=0; i<headers.size(); i++){
				result[0][i] = headers.get(i);
				result[1][i] = data.get(i);
			}
		}
		
		return result;
	}
	
	/**
	 * Metoda za deljenje Stringa prema delimiteru
	 * @param input
	 * @param delimiter
	 * @return
	 */
	public List<String> tokenizeString(String input, String delimiter){
		String[] unfilteredList = input.split(delimiter);
		
		List<String> filteredList = new ArrayList<String>();
		for(String element : unfilteredList){
			if(element!= null && element.trim().length()>0){
				filteredList.add( element);
			}
		}
		log.info("Broj delova u String-u "+input+ " posle podele sa "+delimiter+ ": " + filteredList.size());
		return filteredList;
	}

	/**
	 * Pomocna metoda za stvaranje mape za loyalty discount
	 * u formatu period->discount
	 * 
	 * @param periodHash
	 * @param discountHash
	 * @return
	 */
	public Map<Integer, Double> createLoyaltyDiscountMap(String periodHash,
			String discountHash) {
		Map<Integer, Double> resultMap = new HashMap<Integer, Double>();
		String[] periodArray = periodHash.split("#");
		String[] discounArray = discountHash.split("#");
		int i=0;
		for(String period:periodArray){
			resultMap.put(Integer.valueOf(period), Double.valueOf(discounArray[i++]));
		}
		return resultMap;
	}


	/**
	 * Pomocna metoda za izracunavanje loyalty popusta za prosledjeni countDays
	 * 
	 * @param countDays
	 * @param discountMap
	 * @return
	 */
	public Double calculateLoyaltyDiscount(Integer countDays, Map<Integer, Double> discountMap) {
		int keyEntry=0;	
		for(Integer i:discountMap.keySet()){
			if(countDays>keyEntry && countDays>=i && keyEntry<i){
				keyEntry = i;		
			}
		}
		
		return discountMap.get(keyEntry);
	}

	/**
	 * Metoda koja pravi Mapu objekata name->value za objekte koji implementiraju interfejs
	 * @param helperList
	 * @return
	 */
//	public Map<String, String> getHelperListAsMap(List<? extends NameValueHelper> helperList){
//		Map<String, String> resultMap = new HashMap<String, String>();
//		for(NameValueHelper helper: helperList){
//			resultMap.put(helper.getName(), helper.getValue());
//		}
//		return resultMap;
//	}
	
	/**
	 * Metoda koja pravi Mapu od dva niza 
	 * @param keyArray
	 * @param valueArray
	 * @return
	 */
	public Map<String, String> createMap(String[] keyArray, String[] valueArray){
		Map<String, String> resultMap = new HashMap<String, String>();
		for(int i=0;i<keyArray.length;i++){
			resultMap.put(keyArray[i], valueArray[i]);
		}
		return resultMap;
	}
	
	/**
	 * Metoda za dohvatanje float vrednosti iz Stringa
	 * @param input
	 * @return
	 */
	public float getFloatValue(String input){
		float result = 0;
		if(input == null || input.trim().length()==0){
			return result;
		}
		try{
			result = Float.parseFloat(input.trim());
		}catch(Exception e){
			log.info("Pojavio se problem pri parsiranju vrednosti: "+ input +" u float!");
			return 0;
		}
		return result;
	}
	

	/**
	 * Metoda za kreiranje double vrednosti iz Stringa
	 * @param input
	 * @return
	 */
	public Double getDoubleValue(String input) {
		Double tempResult = getDoubleValueOrNull(input);
		return (tempResult != null? tempResult.doubleValue():0d);
	}
	
	/**
	 * Metoda za kreiranje double vrednosti iz Stringa
	 * @param input
	 * @return
	 */
	public Double getDoubleValueOrNull(String input) {
		Double result = null;
		if(input != null){
			input = input.trim();
			if(input.length()>0){
				try{
					result = Double.parseDouble(input.trim());
				}catch(Exception e){
					log.info("Pojavio se problem pri parsiranju vrednosti: "+ input +" u double!");
					result = null;
				}
			}
		}
		return result;
	}
	
	/**
	 * Metoda za dohvatanje int vrednosti iz Stringa
	 * @param input
	 * @return
	 */
	public int getIntValue(String input){
		int result = 0;
		if(input == null){
			return result;
		}
		try{
			result = Integer.parseInt(input.trim());
		}catch(Exception e){
			log.info("Pojavio se problem pri parsiranju vrednosti: "+ input +" u int!");
			return 0;
		}
		return result;
	}
	
	/**
	 * Metoda koja proverava da li je element sa zadatom vrednoscu u listi (postoji equal)
	 * @param <T>
	 * @param element
	 * @param list
	 * @return
	 */
	public <T> boolean isElementInList(T element, List<T> list){
		if (isEmpty(list)) {
			return false;
		}
		return list.contains(element);
	}

	/**
	 * Metoda koja proverava da li je element sa zadatom vrednoscu u listi (postoji equal)
	 * @param <T>
	 * @param element
	 * @param list
	 * @return
	 */
	public <T> boolean isElementInCollection(T element, Collection<T> collection){
		if (isEmpty(collection) | element == null) {
			return false;
		}
		return collection.contains(element);
	}
	
	
	/**
	 * Metoda koji ulazni String parsira u objekte tipa label
	 * @param parameterList
	 * @return
	 */
//	public List<Label> getLabelListFromString(String parameterList){
//		if(parameterList==null){
//			return null;
//		}
//		List<Label>labelList = new ArrayList<Label>();
//		String[] tripletsList = parameterList.split(";");
//		for(String triplet: tripletsList){
//			if(triplet.contains(",")){
//				triplet =triplet.replace("(", "");
//				triplet =triplet.replace(")", "");
//				triplet =triplet.trim();
//				String[] tempLabel= triplet.split(",");
//				if(tempLabel.length>4 && tempLabel.length<7){
//					try{
//						Label label = new Label();
//						label.setLabel(tempLabel[4].trim());
//						label.setX(new Integer(tempLabel[1].trim()));
//						label.setY(new Integer(tempLabel[2].trim()));
//						label.setPageNo(new Integer(tempLabel[0].trim()));
//						label.setFont(new Integer(tempLabel[3].trim()));
//						if(tempLabel.length == 6){
//							label.setAlignment(TextUtil.getInstance().getValidAlignment(tempLabel[5].trim()));
//						}
//						labelList.add(label);
//					}
//					catch(NumberFormatException e){
//						log.warn("Neuspelo parsiranje ulaznog stringa. NumberFormatException!");
//						return null;
//					}
//					catch(NullPointerException e){
//						log.warn("Neuspelo parsiranje ulaznog stringa. NullPointerException!");
//						return null;
//					}
//				}
//			}
//		}
//		return labelList;
//	}

	/**
	 * Metoda za generisanje poziva na broj
	 * @param serviceNumber
	 * @param agreementNumber
	 * @return
	 */
//	public String generateEquipmentInvocationNumber(String serviceNumber, String agreementNumber){
//		while(agreementNumber.length()<8){
//			agreementNumber = "0"+agreementNumber;
//		}
//		
//		String temp = ValidationsUtil.EQUIPMENT_DOCUMENT_NO_PREFIX + serviceNumber + agreementNumber; 
//		String controlNo = getSpecialNo(temp);
//		return controlNo+ CONTROL_NO_SEPARATOR + temp;
//	}
	
	/**
	 * Metoda za racunanje parcijalne cene prema tokenima
	 * @param periodPrice
	 * @param defaultPrice
	 * @param period
	 * @param defaultPeriod
	 * @param tokenCount
	 * @return
	 */
//	public double calculatePartialTokenPrice(double periodPrice, double defaultPrice, String period, String defaultPeriod, int tokenCount){
//		/*deo koji otpada na tokene*/
//		double coefToken = DateUtil.getInstance().dividePeriods(tokenCount+defaultPeriod, period);
//		/*deo od izabrane cene*/
//		double coefFullPrice = 1-coefToken;
//		double calculatedPrice = defaultPrice*tokenCount+ periodPrice*coefFullPrice;
//		return calculatedPrice;
//	}
	
	/**
	 * Metoda koja uvecava initialAmount za gratis %
	 * @param initialAmount
	 * @param gratis
	 * @return
	 */
	public double addQuantityGratis(double initialAmount, double gratis){
		log.info("Gratis amount: " + Calculator.getInstance().doubleToString2(gratis));
		double amount = initialAmount*(1+gratis/100);
		log.info("Total amount: "+ amount);
		return amount;
	}
	
	/**
	 * Metoda za nalazenje razlike dve liste
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public <T> List<T> getArrayDifference(List<T> a, List<T> b) {
	    List<T> result = new ArrayList<T>();
		if(a != null){
		    for (T element : a) {
		        if(b==null || !b.contains(element)){
		        	result.add(element);
		        }
		    }
		}
		return result;
	}
	
	/**
	 * Metoda za nalazenje unije
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public <T> List<T> getArrayUnion(List<T> a, List<T> b) {
	    List<T> result = a!=null? a:new ArrayList<T>();
		if(b != null){
		    for (T element : b) {
		        if(a==null || !a.contains(element)){
		        	result.add(element);
		        }
		    }
		}
		return result;
	}
	
	
	
	/**
	 * Metoda za nalazenje preseka
	 * @param <T>
	 * @param a
	 * @param b
	 * @return
	 */
	public <T> List<T> getArrayIntersection(List<T> a, List<T> b) {
	    List<T> result = new ArrayList<T>();
		if(a!= null && b != null){
		    for (T element : b) {
		        if(a.contains(element)){
		        	result.add(element);
		        }
		    }
		}
		return result;
	}
	
	public <T> Set<T> findIntersection(Collection<T> a, Collection<T> b) {
	    Set<T> result = new HashSet<T>();
		if(a!= null && b != null){
		    for (T element : b) {
		        if(a.contains(element)){
		        	result.add(element);
		        }
		    }
		}
		return result;
	}
	
	/**
	 * Metoda koja vraca ulaznu listu, sem u slucaju da je ona prazna
	 * Ako je lista prazna vraca null
	 * @param a
	 * @return
	 */
	public <T> List<T> getNonEmptyList(List<T> a){
		if(a!= null && a.size()==0){
			a= null;
		}
		return a;
	}
	
	/**
	 * Metoda koja vraca ulaznu listu, sem u slucaju da je ona null
	 * Ako je null,vraca praznu listu
	 * @param a
	 * @return
	 */
	public <T> List<T> getEmptyIfNullList(List<T> a){
		if(a== null){
			a= new ArrayList<T>();
		}
		return a;
	}
	
	/**
	 * Metoda koja pravi listu od jednog, unetog elementa
	 * @param a
	 * @return
	 */
	public <T> List<T> createArrayListWithSingleElement(T a){
		List<T> list = new ArrayList<T>();
		if (a != null) {
			list.add(a);
		}
		return list;
	}
	
	/**
	 * Metoda koja pravi listu od niza (ako nema vraca praznu listu)
	 * @param a
	 * @return
	 */
	public <T> List<T> createListFromArray(T[] a) {
		List<T> list = new ArrayList<T>();
		if (!isEmpty(a)) {
			list.addAll(Arrays.asList(a));
		}
		return list;
	}
	
	/**
	 * Metoda za proveru da li je odstupanje vrednosti od ocekivane u dozvoljenim okvirima
	 * @param value
	 * @param expected
	 * @param underTolerance
	 * @param overTolerance
	 * @return
	 */
	public boolean isValueAcceptable(double value, double expected,	double underTolerance, double overTolerance) {
		return value > expected - underTolerance && value < expected + overTolerance;
	}
	
	/**
	 * Metoda koja proverava da li je dozvoljeno slanje maila
	 * 
	 * Zabranjeno je slanje s svih profila sem produkcije, osim za @isp.beotel.net i @office.verat.net mailove
	 * 
	 * @param emailAddress
	 * @return
	 */
	public boolean mailSendingForbidden(String emailAddress){
		boolean forbidden = false;
		boolean isProductionEnv = "production".equals(PropertiesUtil.getInstance().getProfileName());
		if(!isProductionEnv){
			boolean localMail = emailAddress.contains("@isp.beotel.net") || emailAddress.contains("@office.verat.net");
			forbidden = !localMail;
		}
		log.info("Mail na adresu "+emailAddress+ (forbidden?" NE saljemo.": " saljemo"));
		return forbidden;
	}
	
	
	/**
	 * Metoda za dohvatanje najveceg boja iz liste
	 * @param numbers
	 * @return
	 */
	public int max(int... numbers ) {
		int max = -1;
		if (numbers != null && numbers.length > 0) {
			max = numbers[0];
			for (Integer num : numbers) {
				if (num > max) {
					max = num;
				}
			}
		}
		return max;
	}
	
	/**
	 * Metoda za dohvatanje najmanjeg boja iz liste
	 * @param numbers
	 * @return
	 */
	public int min(int... numbers) {
		int min = -1;
		if (numbers != null && numbers.length > 0) {
			min = numbers[0];
			for (Integer num : numbers) {
				if (num < min) {
					min = num;
				}
			}
		}
		return min;
	}
	
	/**
	 * Metoda za dohvatanje najmanjeg boja iz kolekcije
	 * @param numbers
	 * @return
	 */
	public <T extends Comparable<T>> T min(Collection<T> collection) {
		T min = null;
		if(notEmpty(collection)){
			for (Iterator<T> iterator = collection.iterator(); iterator.hasNext();) {
			  T element = iterator.next();
			  if(element != null){
				  if(min == null || min.compareTo(element)>0){
					  min = element;
					  log.info("Pronadjen trenutno najmanji element: "+min);
				  }
			  }
			}
		}
		return min;
	}
	
	/**
	 * Metoda za sumiranje po kljucevima
	 * @param sumMap
	 * @param entry
	 */
	public Map<String, Double> addToSumMap(Map<String, Double> sumMap, String key, Double value){
		if(key != null && value != null && sumMap != null){
			Double oldValue = sumMap.get(key);
			Double newValue =  oldValue != null? oldValue+value : value;
			sumMap.put(key, newValue);
		}
		return sumMap;
	}
	
	/**
	 * Metoda za brojanje po kljucevima
	 * @param sumMap
	 * @param entry
	 */
	public Map<String, Integer> addToCountMap(Map<String, Integer> countMap, String key, int value){
		if(key != null && countMap != null){
			Integer oldValue = countMap.get(key);
			Integer newValue = oldValue != null? oldValue+value : value;
			countMap.put(key, newValue);
		}
		return countMap;
	}
	
	/**
	 * Dodaje parametar query-u ako je ispunjen jedan od uslova REDOM: <br>
	 * 1. ako je lista ne sme biti null ili prazna <br>
	 * 2. ako je String ne sme biti null ili prazan <br>
	 * 3. ako nije null 
	 * @param query
	 * @param paramName
	 * @param paramValue
	 */
	public boolean setParameter(Query query, String paramName, Object paramValue) {
		if (paramValue instanceof Collection<?> && !isEmptyCollection((Collection<?>) paramValue)) {
			query.setParameter(paramName, paramValue);
			return true;
		}
		if (paramValue instanceof String && !TextUtil.getInstance().isEmpty((String) paramValue)) {
			query.setParameter(paramName, paramValue);
			return true;
		}
		if (paramValue != null) {
			query.setParameter(paramName, paramValue);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Metoda koja uneti element postavlja na pocetak liste, ako ga ima
	 * 
	 * ako je addIfNotExistrs true, dodaje elment na pocetak, ako ga nije bilo u listi
	 * @param originalList
	 * @param moveToFront
	 * @param addIfNotExists
	 * @return
	 */
	public <T> List<T> createRearangedList(List<T> originalList, T moveToFront, boolean addIfNotExists){
		List<T> resultList = new ArrayList<T>();
		boolean inList = false;
		if(originalList!= null){
			for(T element : originalList){
				if(moveToFront.equals(element)){
					resultList.add(0, element);
					inList = true;
				}else{
					resultList.add(element);
				}
			}
		}
		if(addIfNotExists && !inList){
			resultList.add(0, moveToFront);
		}
		return resultList;
	}
	
	/**
	 * Da li je lista prazna ili null?
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isEmpty(List list) {
		return list == null || list.size() == 0;
	}
	
	@SuppressWarnings("rawtypes")
	public boolean isEmpty(Collection collection) {
		return collection == null || collection.size() == 0;
	}
	
	/**
	 * Vraca prvi element iz liste, naravno ako takav postoji
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public <T> T first(List<T> list) {
		if (isEmpty(list)) {
			return null;
		} else {
			return list.get(0);
		}
	}
	
	/**
	 * Vraca poslednji element iz liste, naravno ako takav postoji
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public <T> T last(List<T> list) {
		if (isEmpty(list)) {
			return null;
		} else {
			return list.get(list.size() - 1);
		}
	}
	
	/**
	 * Koliko lista ima elemenata, ako uopste postoji
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public <T> int size(List<T> list) {
		if (isEmpty(list)) {
			return 0;
		} else {
			return list.size();
		}
	}
	
	/**
	 * Koliko kolekcija ima elemenata, ako uopste postoji
	 * 
	 * @param <T>
	 * @param collection
	 * @return
	 */
	public <T> int size(Collection<T> collection) {
		if (collection == null) {
			return 0;
		} else {
			return collection.size();
		}
	}
	
	/**
	 * Da li lista ima samo jedan element!!!
	 * 
	 * @param list
	 * @return
	 */
	public <T> boolean singleElement(List<T> list) {
		return size(list) == 1;
	}
	
	/**
	 * Da li je skup prazan?
	 * 
	 * @param set
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isEmpty(Set set) {
		return set == null || set.size() == 0;
	}
	
	/**
	 * Da li je niska prazna?
	 * 
	 * @param <T>
	 * @param array
	 * @return
	 */
	public <T> boolean isEmpty(T[] array) {
		return array == null || array.length == 0;
	}
	
	/**
	 * Da li je mapa prazna ili null?
	 * @param list
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean isEmpty(Map map) {
		return map == null || map.size() == 0;
	}
	
	/**
	 * Da li je kolekcija prazna
	 * 
	 * @param collection
	 * @return
	 */
	public boolean isEmptyCollection(Collection<?> collection) {
		return collection == null || collection.size() == 0;
	}
	
	/**
	 * Metoda za proveru sume elemenata liste
	 * @param elements
	 * @param totalExpected
	 * @return
	 */
//	public double calculateDifferenceFromTotal(Collection<AtomicDouble> elements, double totalExpected){
//		double total = 0d;
//		if(elements != null && elements.size()>0){
//			for (AtomicDouble element : elements){
//				total = element.doubleValue();
//			}
//		}
//		return total-totalExpected; 
//	}
	
	/**
	 * Skracuje double na 4 decimale
	 * @param input
	 * @return
	 */
	public double cutToMostFourDecimals(double input){
		return Double.valueOf(FOUR_DECIMAL_FORMAT.format(input));
	}
	
	/**
	 * Skracuje double na 2 decimale
	 * @param input
	 * @return
	 */
	public double cutToMostTwooDecimals(double input){
		return Double.valueOf(TWOO_DECIMAL_FORMAT.format(input));
	}
	
	/**
	 * Metoda za konverotvanje liste BigDecimal objekata u Integer
	 * 
	 * Oracle brojne vrednosti vraca u BigDecimal formatu, nece drugacije
	 * 
	 * @param bigDecimalList
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> convertBigDecimalToIntList(List<BigDecimal> bigDecimalList){
		return toIntegerListFromNumbers((List<Number>) (List<?>) bigDecimalList);
	}
	
	public List<Integer> toIntegerListFromNumbers(List<Number> numbers){
		List<Integer> intList = new ArrayList<Integer>();
		if(numbers != null && numbers.size()>0){
			for(Number element : numbers){
				intList.add(element.intValue());
			}
		}
		return intList;	
	}
	
	/**
	 * Metod za ASC sortiranje proizvoljne liste za cije elemente ocekujemo implementiran Comparable interface
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public <T> List sort(List<T> list) {
		if (isEmpty(list)) {
			log.error("Proslednjena prazna lista za sortiranje!");
			return list;
		}
		if (!(list.get(0) instanceof Comparable)) {
			log.error("Elementi liste klase tipa: " + list.get(0).getClass() + " nemaju implementiran comparable!");
			return list;
		}
		Collections.sort(list, new Comparator<T>() {

			@Override
			public int compare(T o1, T o2) {
				return ((Comparable) o1).compareTo(o2);
			}

		});
		return list;
	}
	/**
	 * Sortira pakete tako da isplivaju paketi cija imena sadrze neku od reci iz liste priorityWords
	 * vodeci racuna o redosledu reci u samoj listi i prosledjenom redosledu paketa. <br>
	 * NPR. <br>
	 * paketi [ADSL MINI, ADSL MIDI, ADSL MAXI, ADSL MEGA, ADSL IPTV MINI, ADSL IPTV MIDI] <br>
	 * prioritetne reci [IPTV, MEGA] <br>
	 * rezultat => [ADSL IPTV MINI, ADSL IPTV MIDI, ADSL MEGA, ADSL MINI, ADSL MIDI, ADSL MAXI]
	 * 
	 * 
	 * @param packages
	 * @param priorityWords
	 * @return
	 */
	public List<Package> sort(List<Package> packages, List<String> priorityWords) {
		if (isEmpty(packages) || isEmpty(priorityWords)) {
			return packages;
		}
		List<Package> resultList = new ArrayList<Package>();
		Set<Package> swapedPackagesSet = new HashSet<Package>();
		for (String priorityWord : priorityWords) {
			for (Package pack : packages) {
				if (pack.getName().toLowerCase().contains(priorityWord.toLowerCase())) {
					resultList.add(pack);
					swapedPackagesSet.add(pack);
				}
			}
		}
		for (Package pack : packages) {
			if (!swapedPackagesSet.contains(pack)) {
				resultList.add(pack);
			}
		}
		return resultList;
	}
	
	public <T> List<T> removeDuplicatesFormList(List<T> withDuplicates){
		if (isEmpty(withDuplicates)) {
			return withDuplicates;
		} else {
			return new ArrayList<T>(new LinkedHashSet<T>(withDuplicates));
		}
	}
	
	/**
	 * Konvertuje listu Stringova koja su zapravo integeri u listu integera
	 * 
	 * @param integerAsStringList
	 * @return
	 */
	public List<Integer> toIntegerList(List<String> integerAsStringList) {
		List<Integer> integers = new ArrayList<Integer>();
		if (isEmpty(integerAsStringList)) {
			return integers;
		}
		for (String integerAsString : integerAsStringList) {
			Integer integer = TextUtil.getInstance().parseInt(integerAsString);
			if (integer != null) {
				integers.add(integer);
			}
		}
		return integers;
	}

	public Map<Integer, Double> negateValuesInMap(Map<Integer, Double> priceMap) {
		Map<Integer, Double> result = new HashMap<Integer, Double>();
		if(!isEmpty(priceMap)){
			for(Entry<Integer, Double> element : priceMap.entrySet()){
				result.put(element.getKey(), -element.getValue());
			}
		}
		return result;
	}
	
	
	/**
	 * Metoda koja od mape pravi mapu mapa, pri cemu su u jednoj oni sa kljucevima iz niza, a u drugoj ostali
	 * @param input
	 * @param existsName
	 * @param noExistsName
	 * @param values
	 * @return
	 */
	public Map<String, Map<String, String>> splitMap(Map<String, String> input, String existsName, String noExistsName, String[] values){
		Map<String, Map<String, String>> result = new HashMap<String, Map<String,String>>();

		if(input != null && input.size()>0 && existsName != null && noExistsName != null){
			List<String> valuesAsList = Arrays.asList(values);
			Map<String, String> existsMap = new HashMap<String, String>();
			Map<String, String> noExistsMap = new HashMap<String, String>();
			for(Entry<String, String> element : input.entrySet()){
				if(isElementInList(element.getKey(), valuesAsList)){
					existsMap.put(element.getKey(), element.getValue());
				}else{
					noExistsMap.put(element.getKey(), element.getValue());
				}
			}
			result.put(existsName, existsMap);
			result.put(noExistsName, noExistsMap);
		}
		return result;
	}
	
	public double getMax(double first, double second){
		double result = first;
		if(first < second){
			result = second;
		}
		return result;
	}
	
	public Integer parsePortaUserIdFromHistory(String cdrHistory){
		String parse = null;
		parse = cdrHistory.substring(cdrHistory.indexOf("USER=")+5);
		if(parse.contains("\t"))
			parse = parse.substring(0,parse.indexOf("\t"));
		log.info("Parse: "+parse);

		return Integer.parseInt(parse);
	}
	
	/**
	 * Null je ili nema ID
	 * 
	 * @param idable
	 * @return
	 */
	public boolean isEmptyIdable(Idable idable) {
		return idable == null || idable.getId() == null;
	}

	/**
	 * Null je ili nema ID
	 * 
	 * @param idable
	 * @return
	 */
	public boolean notEmptyIdable(Idable idable) {
		return idable != null && idable.getId() != null;
	}
	
	/**
	 * Null je ili nema name
	 * 
	 * @param nameable
	 * @return
	 */
//	public boolean isEmptyNameable(Nameable nameable) {
//		return nameable == null || TextUtil.getInstance().isEmpty(nameable.getName());
//	}
	
	/**
	 * Izracunava poziv na broj za racune za fiksnu (za sada nista pametno ne vraca)
	 * 
	 * @param phoneInvoice
	 * @return
	 */
//	public String generatePhoneInvocationNumber(PhoneInvoice phoneInvoice) {
//		String phoneInvoiceId = phoneInvoice.getId().toString();
//		while (phoneInvoiceId.length() < 8) {
//			phoneInvoiceId = "0" + phoneInvoiceId;
//		}
//		String temp = ValidationsUtil.INDIVIDUAL_DOCUMENT_NO_PREFIX + ValidationsUtil.PHONE_PAYMENT_CODE + phoneInvoiceId; 
//		String controlNo = getSpecialNo(temp);
//		return controlNo + CONTROL_NO_SEPARATOR + temp;
//	}
	
	/**
	 * Metoda za povezivanje listi u jednu
	 * @param parts
	 * @return
	 */
	public <T> List<T> concatenateLists(List<T>... parts){
		List<T> result = new ArrayList<T>();
		if(!isEmpty(parts)){
			for(List<T> element: parts){
				if(!isEmpty(element)){
					result.addAll(element);
				}				
			}
		}
		return result;
	}
	
	/**
	 * Iz prosledjene liste "Idable" objekata vraca listu njihvih ID-a
	 * 
	 * @param idables
	 * @return
	 */
	public <T extends Idable> List<Integer> extractIds(List<T> idables) {
		if (Calculator.getInstance().isEmpty(idables)) {
			log.warn("Nedostaje obavezan parametar!!!");
			return null;
		}
		List<Integer> ids = new ArrayList<Integer>();
		for (T t : idables) {
			ids.add(((Idable) t).getId());
		}
		return ids;
	}
	
	/**
	 * Za proizvoljnu listu izvlaci element iz liste sa datim nazivom i pakuje u listu...
	 * 
	 * @param wrapperList
	 * @param fieldName
	 * @return
	 */
//	@SuppressWarnings("unchecked")
//	public <T, T1> List<T> extractAttributesByName(List<T1> wrapperList, String fieldName) {
//		List<T> extractedElements = Collections.emptyList();
//		if (Calculator.getInstance().isEmpty(wrapperList)) {
//			log.error("Nema elemnata za izvlacenje elemenata...");
//		} else {
//			try {
//				extractedElements = new ArrayList<T>();
//				T1 t1 = Calculator.getInstance().first(wrapperList);
//				Method method = t1.getClass().getMethod("get" + WordUtils.capitalize(fieldName));
//				for (T1 wrapper : wrapperList) {
//					extractedElements.add((T) method.invoke(wrapper));
//				}
//			} catch (Exception e) {
//				log.error("Greska pri izvlacenju elementa: " + fieldName + ": " + e.getLocalizedMessage());
//			}
//		}
//		return extractedElements;
//	}
	
	/**
	 * Iz prosledjene liste "Idable" objekata vraca listu njihvih ID-a
	 * 
	 * @param idables
	 * @return
	 */
//	public <T extends Nameable> List<String> extractNames(Collection<T> namables) {
//		if (Calculator.getInstance().isEmpty(namables)) {
//			log.warn("Nedostaje obavezan parametar!!!");
//			return null;
//		}
//		List<String> names = new ArrayList<String>();
//		for (T t : namables) {
//			names.add(((Nameable) t).getName());
//		}
//		return names;
//	}
	
	/**
	 * Kreira listu sa elementima koji emplementiraju Idable interface. Postavlja im id
	 * 
	 * @param <T>
	 * @param ids
	 * @param clazz
	 * @return
	 */
	public <T extends Idable> List<T> createListIdableObjects(List<Integer> ids, Class<T> clazz) {
		if (Calculator.getInstance().isEmpty(ids)) {
			log.warn("Nedostaje obavezan parametar!!!");
			return null;
		}
		List<T> elements = new ArrayList<T>();
		for (Integer id : ids) {
			T t = createIdableObject(id, clazz);
			if (t == null) {
				log.error("Greska pri instancijranju objekta ili neimplementiranosti interface-a 'Idable'");
				return null;
			}
		}
		return elements;
	} 
	
	public <T extends Idable> T createIdableObject(Integer id, Class<T > clazz) {
		if (!Idable.class.isAssignableFrom(clazz)) {
			log.warn("Klasa ne implemntira Idable interface!!!");
			return null;
		}
		T t = createInstanceFromClassDefaultConstructor(clazz);
		((Idable) t).setId(id);
		return t;
	}
	
	/**
	 * Listu idable objects pakuje u mapu [id => idableObject]
	 * 
	 * @param <T>
	 * @param idableObjects
	 * @return
	 */
	public <T> Map<Integer, T> toIdableMap(Collection<T> idableObjects) {
		Map<Integer, T> idableObjectById = new LinkedHashMap<Integer, T>();
		if (isEmpty(idableObjects)) {
			log.warn("Nisu prosledjeni idable objekti za prebacivanje u map!!!");
			return idableObjectById;
		}
		if (!(idableObjects.iterator().next() instanceof Idable)) {
			log.error("Prosledjena lista mora da sadrzi Idable objekte");
			return idableObjectById;
		}
		for (T idableObject : idableObjects) {
			idableObjectById.put(((Idable) idableObject).getId(), idableObject);
		}
		return idableObjectById;
	}
	
	/**
	 * Kolekciju nambeable objects pakuje u mapu [name => nameableObject]
	 * 
	 * @param <T>
	 * @param idableObjects
	 * @return
	 */
//	public <T> Map<String, T> toNameableMap(Collection<T> nameableObjects) {
//		Map<String, T> nameableObjectByName = new HashMap<String, T>();
//		if (isEmpty(nameableObjects)) {
//			log.warn("Nisu prosledjeni nameable objekti za prebacivanje u map!!!");
//			return nameableObjectByName;
//		}
//		if (!(nameableObjects.iterator().next() instanceof Nameable)) {
//			log.error("Prosledjena lista mora da sadrzi Idable objekte");
//			return nameableObjectByName;
//		}
//		for (T nameableObject : nameableObjects) {
//			nameableObjectByName.put(((Nameable) nameableObject).getName(), nameableObject);
//		}
//		return nameableObjectByName;
//	}
	
	/**
	 * Kreira mapu [name => value] iz kolekcije nameValueHelpers-a
	 * 
	 * @param <T>
	 * @param idableObjects
	 * @return
	 */
//	public <T> Map<String, String> toValueByNameMap(Collection<T> nameValueHelperCollections) {
//		Map<String, String> valueByNameMap = new HashMap<String, String>();
//		if (isEmpty(nameValueHelperCollections)) {
//			log.warn("Nisu prosledjeni nameValue objekti za prebacivanje u map!!!");
//			return valueByNameMap;
//		}
//		if (!(nameValueHelperCollections.iterator().next() instanceof NameValueHelper)) {
//			log.error("Prosledjena lista koja ne sadrzi NameValueHelperse!!!");
//			return valueByNameMap;
//		}
//		for (T t : nameValueHelperCollections) {
//			NameValueHelper nameValueHelper = (NameValueHelper) t;
//			valueByNameMap.put(nameValueHelper.getName(), nameValueHelper.getValue());
//		}
//		return valueByNameMap;
//	}
	
	/**
	 * Listu idable objects pakuje u mapu [id => idableObject]
	 * 
	 * @param <T>
	 * @param idableObjects
	 * @return
	 */
//	public <T extends Nameable> Map<String, T> toNameableMap(List<T> nameableObjects) {
//		Map<String, T> nameableObjectsByName = new HashMap<String, T>();
//		if (isEmpty(nameableObjects)) {
//			log.warn("Nisu prosledjeni idable objekti za prebacivanje u map!!!");
//			return nameableObjectsByName;
//		}
//		if (!(first(nameableObjects) instanceof Nameable)) {
//			log.error("Prosledjena lista mora da sadrzi Nameable objekte");
//			return nameableObjectsByName;
//		}
//		for (T nameableObject : nameableObjects) {
//			nameableObjectsByName.put(((Nameable) nameableObject).getName(), nameableObject);
//		}
//		return nameableObjectsByName;
//	}
//	
	/**
	 * Iz Liste kreira novu listu sa pojedinacnim elementim iz niske objekata sa indeksom indexElement
	 * 
	 * @param <T>
	 * @param objectsList
	 * @param indexElement
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> List<T> extractElementsAsList(List<Object[]> objectsList, int indexElement) {
		if (isEmpty(objectsList) || indexElement < 0 || indexElement > objectsList.size() - 1) {
			log.error("Nepostojeci/Neodgovarajuci ulazni parametri!!!");
			return null;
		}
		List<T> list = new ArrayList<T>();
		for (Object[] objects : objectsList) {
			list.add((T) objects[indexElement]);
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public <K, V> Map<K, V> extractElementsAsMap(List<Object[]> objectsList, int indexOfKey, int indexOfValue) {
		if (isEmpty(objectsList) || indexOfKey < 0 || indexOfKey > objectsList.size() - 1 || indexOfValue < 0 || indexOfValue > objectsList.size() - 1) {
			log.error("Nepostojeci/Neodgovarajuci ulazni parametri!!!");
			return null;
		}
		Map<K, V> map = new HashMap<K, V>();
		for (Object[] objects : objectsList) {
			Object keyObject = objects[indexOfKey];
			Object valueObject = objects[indexOfValue];
			K key = keyObject != null ? (K) keyObject : null;
			V value = valueObject != null ? (V) valueObject : null;
			map.put(key, value);
		}
		return map;
	}
	
//	public Map<Integer, PromotionParamValue> createPromotionParamValueByPromotionTemplateParamId(List<PromotionParamValue> promotionParamValues) {
//		Map<Integer, PromotionParamValue> promotionParamValueByPromotionTemplateParamId = new LinkedHashMap<Integer, PromotionParamValue>();
//		if (!isEmpty(promotionParamValues)) {
//			for (PromotionParamValue promotionParamValue : promotionParamValues) {
//				PromotionTemplateParam promotionTemplateParam = promotionParamValue.getPromotionTemplateParam();
//				if (promotionTemplateParam != null) {
//					promotionParamValueByPromotionTemplateParamId.put(promotionTemplateParam.getId(), promotionParamValue);
//				}
//			}
//		}
//		return promotionParamValueByPromotionTemplateParamId;
//	}
	
	/**
	 * Kreira instancu objekta po defaultnom konstruktoru tipa classType
	 * 
	 * @param <T>
	 * @param classType
	 * @return
	 */
	public <T> T createInstanceFromClassDefaultConstructor(Class<T> classType) {
		try {
			Constructor<T> constructor = classType.getConstructor();
			Object object = constructor.newInstance();
			return classType.cast(object);
		} catch (SecurityException e) {
			log.error("Loader bacio SecurityException: " + e.getLocalizedMessage());
		} catch (IllegalArgumentException e) {
			log.error("Loader bacio IllegalArgumentException: " + e.getLocalizedMessage());
		} catch (NoSuchMethodException e) {
			log.error("Loader bacio NoSuchMethodException: " + e.getLocalizedMessage());
		} catch (InstantiationException e) {
			log.error("Loader bacio InstantiationException: " + e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			log.error("Loader bacio IllegalAccessException: " + e.getLocalizedMessage());
		} catch (InvocationTargetException e) {
			log.error("Loader bacio InvocationTargetException: " + e.getLocalizedMessage());
		} catch (Exception e) {
			log.error("Exception: " + e.getLocalizedMessage());
		}
		return null;
	}
	
	/**
	 * Iz proizvoljen kolekcije uklanja null elemente
	 * 
	 * @param collection
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void clearNullElements(Collection collection) {
		if (!isEmpty(collection)) {
			collection.removeAll(Collections.singleton(null));
		}
	}
	
	/**
	 * Da li je operater software
	 * 
	 * @param operator
	 * @return
	 */
//	public boolean isSoftwareOperator(Operator operator) {
//		return !isEmptyIdable(operator) && operator.getId().equals(1);
//	}
	
	/**
	 * Da li dve liste imaju identicne clanove
	 * 
	 * @param <T>
	 * @param list1
	 * @param list2
	 * @return
	 */
	public <T> boolean equalsList(List<T> list1, List<T> list2) {
		return (isEmpty(list1) && isEmpty(list2)) 
				||
			   (!isEmpty(list1) && !isEmpty(list2) && 
			    list1.size() == list2.size() && 
			    list1.containsAll(list2) && 
			    list2.containsAll(list1));
	}
	
	/**
	 * Kreira novo listu sa lowerCase elementima
	 * 
	 * @param list
	 * @return
	 */
	public List<String> toLowerCase(List<String> list) {
		List<String> lowerCaseList = null;
		if (!isEmpty(list)) {
			lowerCaseList = new ArrayList<String>(list.size());
			for (String element : list) {
				if (element != null) {
					lowerCaseList.add(element.toLowerCase());
				}
			}
		}
		return lowerCaseList;
	}
	
	/**
	 * Pomocna metoda za pronalazanje razlike 2 liste
	 * @param newChannelsArray
	 * @param oldChannelsArray
	 * @return
	 */
	public <T> List<T> findArrayDifference(List<T> compare, List<T> compareTo){
		List<T> different = new ArrayList<T>();
		if(!Calculator.getInstance().isEmpty(compare)){
			Set<T> newSet = new LinkedHashSet<T>(compare);
			if(!Calculator.getInstance().isEmpty(compareTo)){
				Set<T> oldSet = new LinkedHashSet<T>(compareTo);
				newSet.removeAll(oldSet);
			}
			different.addAll(newSet);
		}
		return different;
	}
	
	public Integer getId(Idable idable) {
		Integer id = null;
		if (idable != null) {
			id = idable.getId();
		}
		return id;
	}
	
	public boolean almostEqual(double a, double b, double eps) {
		return Math.abs(a - b) < eps;
	}

	/**
	 * Pomocna metoda za pronalazanje preseka 2 liste
	 * @param newChannelsArray
	 * @param oldChannelsArray
	 * @return
	 */
	public List<String> findArrayIntersection(List<String> compare, List<String> compareTo){
		List<String> intersection = new ArrayList<String>();
		if(!Calculator.getInstance().isEmpty(compare) && !Calculator.getInstance().isEmpty(compareTo)){
			Set<String> set1 = new HashSet<String>(compare);
			Set<String> set2 = new HashSet<String>(compareTo);
			set1.retainAll(set2);
			intersection.addAll(set1);
		}
		return intersection;
	}
	
//	public <T> List<T> populate(List<Object[]> rows, Class<T> clazz, String... fields) {
//		List<T> resultList = new ArrayList<T>();
//		Map<String, Object> fieldValueByFieldNameMap = new HashMap<String, Object>();
//		for (Object[] row : rows) {
//			for (int i = 0; i < fields.length; i++) {
//				fieldValueByFieldNameMap.put(fields[i], row[i]);
//			}
//			resultList.add(Calculator.getInstance().populate(fieldValueByFieldNameMap, clazz));
//			
//		}
//		return resultList;
//	}
	
//	public <T> T populate(Object[] row, Class<T> clazz, String... fields) {
//		Map<String, Object> fieldValueByFieldNameMap = new LinkedHashMap<String, Object>();
//		for (int i = 0; i < fields.length; i++) {
//			fieldValueByFieldNameMap.put(fields[i], row[i]);
//		}
//		return Calculator.getInstance().populate(fieldValueByFieldNameMap, clazz);
//	}
	
	/**
	 * Na osnovu mape fieldName -> fieldValue puni polja baznog objekta (ovog templatea) i vraca ga na kraju
	 * 
	 * @param fieldValueByFieldNameMap
	 * @return
	 */
//	public <T> T populate(Map<String, Object> fieldValueByFieldNameMap, Class<T> clazz) {
//		Map<String, Object> createdObjectsByFieldNameMap = new HashMap<String, Object>();
//		
//		T rootObject = createRootClassInstance(fieldValueByFieldNameMap, clazz);
//		
//		createdObjectsByFieldNameMap.put(TextUtil.BLANKO, rootObject);
//		log.debug("Popunjena root class: " + createdObjectsByFieldNameMap.get(TextUtil.BLANKO));
//		for (Entry<String, Object> entry : fieldValueByFieldNameMap.entrySet()) {
//			String fieldName = entry.getKey();
//			Object fieldValue = entry.getValue();
//			if (fieldValue != null) {
//				populateCreatedObjects(fieldName, fieldValue, createdObjectsByFieldNameMap);
//			}
//		}
//		return rootObject;
//	}
	
//	@SuppressWarnings("unchecked")
//	private <T> T createRootClassInstance(Map<String, Object> fieldValueByFieldNameMap, Class<T> clazz) {
//		T rootObject = null;
//		try {
//			AbstractClassRecognizer abstractClassRecognizer = clazz.getAnnotation(AbstractClassRecognizer.class);
//			Class<?> concretRootClass = null;
//			if (abstractClassRecognizer != null) {
//				log.debug("Pronadjen recogniser....: " +abstractClassRecognizer);
//				for (Entry<String, Object> entry : fieldValueByFieldNameMap.entrySet()) {
//					String fieldName = entry.getKey();
//					Object fieldValue = entry.getValue();
//					log.debug("fieldName: " + fieldName + " fieldValue: " + fieldValue);
//					if (fieldValue == null) {
//						continue;
//					}
//					concretRootClass = findConcretRootClass(abstractClassRecognizer, fieldValue);
//					if (concretRootClass != null) {
//						log.info("Recognizer pronasao concreateClass pravimo break...");
//						break;
//					}
//				}
//				rootObject = (T) Calculator.getInstance().createInstanceFromClassDefaultConstructor(concretRootClass);
//			} else {
//				log.debug("Rec je o konkretnoj klasi: " + clazz + " vrsimo instan instanciranje...");
//				rootObject = (T) Calculator.getInstance().createInstanceFromClassDefaultConstructor(clazz);
//			}
//		} catch (Exception e) {
//			log.error("Greska pri pokusaju trazenja AbstractClassRecognizer annot: " + e);
//		}
//		return rootObject;
//	}
	
	/**
	 * Na osnovu def anotacije abstraktne klase i vrednosti testPolja odredjuje konkretnu klasu zainstanciranje
	 * 
	 * @param abstractClassRecognizer
	 * @param testFieldValue
	 * @return
	 */
//	private Class<?> findConcretRootClass(AbstractClassRecognizer abstractClassRecognizer, Object testFieldValue) {
//		Class<?> concretRootClass = null;
//		Recognizer[] recognizers = abstractClassRecognizer.recognizers();
//		for (Recognizer recognizer : recognizers) {
//			if (!recognizer.fieldValue().equals(Recognizer.NOT_NULL)) {
//				log.debug("Recognizer NOT_NULL: " + recognizer);
//				log.debug("Recognizer.fieldValue(): " + recognizer.fieldValue());
//				log.debug("Current fieldValue: " + testFieldValue);
//				if (recognizer.fieldValue().toString().equals(testFieldValue.toString())) {
//					concretRootClass = recognizer.recognizedClass();
//					log.info("Recognizer postavljamo concreateClass: " + concretRootClass);
//					break;
//				}
//			} else {
//				log.debug("Recognizer sa fiksnom ocekivanjem: " + recognizer);
//				concretRootClass = recognizer.recognizedClass();
//				log.info("Recognizer postavljamo concreateClass: " + concretRootClass);
//				break;
//			}
//		}
//		return concretRootClass;
//	}
	
//	private void populateCreatedObjects(String fieldName, Object fieldValue, Map<String, Object> createdObjectsByFieldNameMap) {
//		log.debug("Operacija punjenja za field: " + fieldName + " vrednost: " + fieldValue);
//		
//		String[] fieldNameParts = fieldName.split(TextUtil.SPLIT_BY_DOT_REGEX);
//		log.debug("FieldNameParts: " + Arrays.toString(fieldNameParts));
//		String prevAppendedField = TextUtil.BLANKO;
//		
//		for (int i = 0; i < fieldNameParts.length; i++) {
//			String currentField = fieldNameParts[i];
//			String futureAppendedField = createValidAppendedField(prevAppendedField, currentField);
//			
//			if (i == fieldNameParts.length - 1) {
//				Object createdObject = createdObjectsByFieldNameMap.get(prevAppendedField);
//				log.debug("Poslednje polje za " + prevAppendedField + " za vrednost: " + fieldValue + " na objektu: " + createdObject);
//				setValueByReflection(createdObject, currentField, fieldValue);
//			} else {
//				log.debug("Nije poslednje polje, futureAppendedField: " + futureAppendedField);
//				if (!createdObjectsByFieldNameMap.containsKey(futureAppendedField)) {
//					Object rootObject = createdObjectsByFieldNameMap.get(prevAppendedField); 
//					log.debug("Root object klass: " + rootObject.getClass() + ", obrada currentField: " + currentField);
//					
//					Class<?> leafClass = getReturnTypeByReflection(rootObject, currentField);
//					Object leafObject = createInstanceOfClassAbstarctClassAware(leafClass);
//					log.debug("Kreirali smo leaf objekat klase: " + leafObject.getClass());
//					log.debug("Leaf objekat unosimo u mapu... za kljucem: " + futureAppendedField);
//					createdObjectsByFieldNameMap.put(futureAppendedField, leafObject);
//					
//					log.debug("Root objektu setujemo vrednost sa leafObjektom...");
//					setValueByReflection(rootObject, currentField, leafObject, leafClass);
//				} else {
//					log.debug("Posojalo je futureAppendedField: " + futureAppendedField);
//				}
//			}
//			prevAppendedField = futureAppendedField;
//			log.debug("Postavljamo prevAppendedField: " + prevAppendedField);
//		}
//	}
	
	/**
	 * Instancira klasu datog tipa. U slucaju da je prosledjena apst klasa onda instancira konkretnu klasu koja
	 * je prosledjena anotaciom {@link AbstractClassRecognizer}
	 * 
	 * @param clazz
	 * @return
	 */
//	private Object createInstanceOfClassAbstarctClassAware(Class<?> clazz) {
//		Object concreteClass = null;
//		try {
//			AbstractClassRecognizer abstractClassRecognizer = clazz.getAnnotation(AbstractClassRecognizer.class);
//			Class<?> concreteLeafClass = null;
//			if (abstractClassRecognizer != null) {
//				log.debug("Pronadjen recogniser, znaci rec je o apstr klasi, trazimo konkretnu klasu za instanciranje....");
//				concreteLeafClass = abstractClassRecognizer.concreteClass();
//				log.debug("Postavljamo konkretnu klasu: " + concreteLeafClass);
//				concreteClass = Calculator.getInstance().createInstanceFromClassDefaultConstructor(concreteLeafClass);
//			} else {
//				concreteClass = Calculator.getInstance().createInstanceFromClassDefaultConstructor(clazz);
//			}
//		} catch (Exception e) {
//			log.error("Greska pri proveri da li leafClass ima AbstractClassRecognizer anotaciju: " + e);
//		}
//		return concreteClass;
//	}
	
	/**
	 * Datom objektu za dato polje postavlja prosledjenu vrednost...
	 * 
	 * @param onObject
	 * @param field
	 * @param value
	 */
//	private void setValueByReflection(Object onObject, String field, Object value) {
//		if (value != null) {
//			setValueByReflection(onObject, field, value, value.getClass());
//		}
//	}
	
	/**
	 * Datom objektu za dato polje postavlja prosledjenu vrednost (Prosledjujemo velueType u slucaju kada je velue konkretna klasa apstraktne klase)...
	 * 
	 * @param onObject
	 * @param field
	 * @param value
	 * @param velueType 
	 */
//	private void setValueByReflection(Object onObject, String field, Object value, Class<?> velueType) {
//		String methodName = "set" + WordUtils.capitalize(field);
//		log.debug("Izvrsavamo metod: " + methodName + " na objekat: " + onObject);
//		Object valueForPopulating = value;
//		if (valueForPopulating instanceof java.sql.Timestamp) {
//			log.info("Posto baratamo samo sa java.util.Date a hibernate u row formatu vraca java.sql.Timestamp, vrsimo konveriziju...");
//			valueForPopulating = new Date(((java.sql.Timestamp) value).getTime());
//		}
//		try {
//			Method method = onObject.getClass().getMethod(methodName, velueType);
//			method.invoke(onObject, valueForPopulating);
//		} catch (Exception e) {
//			log.error("Greska pri izvrsavanju metode: " + methodName + " za objekt klase: " + value.getClass() + " --> " + e);
//			try {
//				log.info("Provera da li je " + methodName + " za objekt klase: " + value.getClass() + " primitiva..." );
//				Method method = onObject.getClass().getMethod(methodName, WRAPPERS_TO_PRIMITIVES.get(velueType));
//				method.invoke(onObject, valueForPopulating);
//			} catch (Exception ex) {
//				log.error("Greska pri izvrsavanju metode: " + methodName + " za objekt klase: " + value.getClass() + " sa primitivom --> " + ex);
//			} 
//		}
//	}
	
	/**
	 * Otkriva na osnovu naziva polja koje pripada klasi koji smo objekat instancirali kog je tipa... 
	 * 
	 * @param onObject
	 * @param field
	 * @return
	 */
//	private Class<?> getReturnTypeByReflection(Object onObject, String field) {
//		String methodName = "get" + WordUtils.capitalize(field);
//		log.debug("Na root izvrsavamo metod: " + methodName);
//		Class<?> retutnType = null;
//		try {
//			Method method = onObject.getClass().getMethod(methodName);
//			retutnType = method.getReturnType();
//		} catch (Exception e) {
//			log.error("Greska pri pozivanju metode: " + methodName + " na root objekat klase: " + onObject.getClass() + " --> " + e);
//		}
//		return retutnType;
//	}
	
	/**
	 * Spaja dve niske
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}
	
	/**
	 * Pomocni metod na koren apenduje tacku pa polje, sem na pocetku kad vraca samo polje za apendovanje.
	 * 
	 * @param currentRoot
	 * @param fieldForAppending
	 * @return
	 */
	private String createValidAppendedField(String currentRoot, String fieldForAppending) {
		if (currentRoot.equals(TextUtil.BLANKO)) {
			return new String(fieldForAppending);
		} else {
			return currentRoot.concat(TextUtil.DOT).concat(fieldForAppending);
		}
	}

	public boolean checkAllValuesSame(Collection<Integer> values) {
		boolean allSame = true;
		if(values != null && values.size()>1){
			Set<Integer> setValues = new HashSet<Integer>(values);
			allSame = setValues.size()<=1;
		}
		return allSame;
	}
	
	/**
	 * Vraca element iz liste sa izabranim indeksom, ako takav postoji, ako ne vraca default
	 * 
	 * @param <T>
	 * @param list
	 * @return
	 */
	public <T> T getElementWithIndexSafely(List<T> list, int index, T defaultValue) {
		T result = defaultValue;
		if (!isEmpty(list)) {
			if(list.size()>index){
				result = list.get(index);
			}
		}
		return result;
	}
	
	public <T> List<T> reverseList(List<T> myList) {
	    List<T> invertedList = new ArrayList<T>();
	    if(!isEmpty(myList)){
		    for (T t: myList) {
		        invertedList.add(0,t);
		    }
	    }
	    return invertedList;
	}
	
	@SuppressWarnings("unchecked")
	private <T> void combinationRec(Class<T> t, List<T[]> result, T[] array, Stack<T> stack, int start, int length) {
		if (stack.size() >= length) {
			return;
		} else {
			for (int i = start; i < array.length; i++) {
				stack.push(array[i]);
				if (stack.size() == length) {
					result.add(stack.toArray((T[]) Array.newInstance(t, length)));
				}
				combinationRec(t, result, array, stack, i + 1, length);
				stack.pop();
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public final <T> List<T[]> combination(T[] array, int length) {
		List<T[]> result = Collections.emptyList();
		if (array == null || array.length == 0 || length < 1 || length > array.length) {
			log.error("Neodgovarajuca niska/duzina!!!");
		} else {
			int start = 0;
			Stack<T> stack = new Stack<T>();
			result = new ArrayList<T[]>();
			Class<T> t = (Class<T>) array[0].getClass();
			combinationRec(t, result, array, stack, start, length);
		}
		return result;
	}
	

	/**
	 * Da li kolekcija nije prazna
	 * 
	 * @param collection
	 * @return
	 */
	public boolean notEmpty(Collection<?> collection) {
		return collection != null && collection.size() > 0;
	}
	
}

