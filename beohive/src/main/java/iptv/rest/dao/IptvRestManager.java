package iptv.rest.dao;

import iptv.rest.v2.model.Response;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.List;
import java.util.Stack;

import net.beotel.model.template.Idable;
import net.beotel.rest.RestManager;
import net.beotel.util.Calculator;
import net.beotel.util.TextUtil;

import org.apache.log4j.Logger;

import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class IptvRestManager extends RestManager {
	private static final String UNIVERSAL_ATTRIBUTE_LIST_RESULT = "data";
	private static final Logger log = Logger.getLogger(IptvRestManager.class);
	
	public IptvRestManager(String url, String credentials) {
		super(url, credentials);
	}
	
	/**
	 * Usled uniformnosti mozemo na osnovu klase i prosldjenog urlSuffixa naci trazeni item sa platforme.
	 * Pozeljno bi bilo da itemi implementiraju @see {@link Idable}
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param resultClass
	 * @return
	 */
	public <T> T findItem(String urlPathSuffix, Class<T> resultClass) {
		T result = null;
		if (TextUtil.getInstance().isEmpty(urlPathSuffix)) {
			log.error("Nedostaje obavezan parametar!!!");
			return result;
		}
		List<T> items = findAllItems(urlPathSuffix, resultClass);
		if (Calculator.getInstance().singleElement(items)) {
			result = Calculator.getInstance().first(items);
			Integer id = result instanceof Idable ? ((Idable) result).getId() : null;
			log.info("Za urlPathSuffix: " + urlPathSuffix + " uspesno pronadjen item ID: " + id);
		} else if (Calculator.getInstance().isEmpty(items)) {
			log.error("Nisu pronadjeni items za urlPathSuffix: " + urlPathSuffix);
		} else {
			log.error("Za urlPathSuffix: " + urlPathSuffix + " vraceno vise rezultata!!!");
		}
		return result;
	}
	
	/**
	 * Nalazi listu itema klase resultClass na osnovu zahteva unutar urlPathSuffix  
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param resultClass
	 * @return
	 */
	public <T> List<T> findAllItems(String urlPathSuffix, Class<T> resultClass) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.GET);
		List<T> results = generateListObjectsUsingOnlyDataAttributValue(resultClass, urlConnection);
		closeConnection(urlConnection);
		return results;
	}  
	
	/**
	 * Usled unifikacije olaksano dodavanje itema na platformu
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param item
	 * @return
	 */
	public <T> boolean addItem(String urlPathSuffix, Idable item) {
		boolean successfullyAdded = false;
		if (item == null) {
			log.error("Nedostaje obavezan parametar!!!");
			return successfullyAdded;
		}
		Response response = addObject(urlPathSuffix, Response.class, item);
		if (isInsertResponseValid(response)) {
			item.setId(response.getData().getId());
			log.info("Uspesno dodat item ID: " + item.getId());
			successfullyAdded = true;
		} else {
			log.error("Greska pri dodavanja itema za urlPathSuffix: " + urlPathSuffix);
		}
		return successfullyAdded;
	}
	
	/**
	 * Brisanja itema koristeci url
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @return
	 */
	public <T> boolean updateItem(String urlPathSuffix, Object onlyFilledAttributesForUpdate) {
		boolean successfullyUpdated = false;
		if (TextUtil.getInstance().isEmpty(urlPathSuffix)) {
			log.error("Nedostaje obavezan parametar!!!");
			return successfullyUpdated;
		}
		Response response = updateObject(urlPathSuffix, Response.class, onlyFilledAttributesForUpdate);
		if (isUpdateResponseValid(response)) {
			log.info("Uspesno update item sa urlPathSuffix-a: " + urlPathSuffix);
			successfullyUpdated = true;
		} else {
			log.warn("Greska pri update itema za urlPathSuffix: " + urlPathSuffix);
		}
		return successfullyUpdated;
	}
	
	/**
	 * Brisanja itema koristeci url
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @return
	 */
	public <T> boolean deleteItem(String urlPathSuffix) {
		boolean successfullyDeleted = false;
		if (TextUtil.getInstance().isEmpty(urlPathSuffix)) {
			log.error("Nedostaje obavezan parametar!!!");
			return successfullyDeleted;
		}
		Response response = deleteObject(urlPathSuffix, Response.class);
		if (isUpdateResponseValid(response)) {
			log.info("Uspesno obrisan item sa urlPathSuffix-a: " + urlPathSuffix);
			successfullyDeleted = true;
		} else {
			log.error("Greska pri brisanju itema za urlPathSuffix: " + urlPathSuffix);
		}
		return successfullyDeleted;
	}
	
	/**
	 * Da li je respon validan?
	 * 
	 * @param response
	 * @return
	 */
	private boolean isInsertResponseValid(Response response) {
		if (response == null) {
			log.error("Nije prosledjen respons na testiranje!!!");
			return false;
		}
		if (response.isStatusValid()) {
			log.info("Operacija prosla u redu i dodati item ID: " + response.getData().getId());
			return true;
		} else {
			log.error("Operacija NIJE prosla u redu!!!");
			return false;
		}
	}
	
	/**
	 * Da li je respon validan?
	 * 
	 * @param response
	 * @return
	 */
	private boolean isUpdateResponseValid(Response response) {
		if (response == null) {
			log.error("Nije prosledjen respons na testiranje!!!");
			return false;
		}
		if (response.isStatusValid()) {
			log.info("Operacija prosla u redu i affected rows je: " + response.getData().getAffectedRows());
			return true;
		} else {
			log.error("Operacija NIJE prosla u redu!!!");
			return false;
		}
	}
	
	/**
	 * Koristeci samo json listu iz responsa vezanu za attribut "data", generisemo rezultujucu listu objekata
	 * 
	 * @param <T>
	 * @param valueType
	 * @return
	 */
	private <T> List<T> generateListObjectsUsingOnlyDataAttributValue(Class<?> resultClass, HttpURLConnection urlConnection) {
		try {
			String jsonResponse = getResponseContent(urlConnection);
			log.info("Odgovor servera: "+jsonResponse);
			JavaType collectionJavaType =  TypeFactory.defaultInstance().constructCollectionType(List.class, resultClass);
			return prepareForGeneratingObjectFromJson(extractListValueOfAttribute(UNIVERSAL_ATTRIBUTE_LIST_RESULT, jsonResponse), collectionJavaType);
		} catch (JsonParseException e) {
			log.warn("JSON parser greska: " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			log.warn("Greska pri mapiranju objekta: " + e.getLocalizedMessage());
		} catch (IOException e) {
			log.warn("Greska pri kreiranju objekta na osnovu JSON-a: " + e.getLocalizedMessage());
		}
		return null;
	}
	
	/**
	 * Izvlaci vrednost datog attributa sa datim imenom iz json stringa. <br>
	 * BITNO: Ocekujemo da je vrednost data u formatu json liste. 
	 * 
	 * @param attributeOfListValue
	 * @param jsonString
	 * @return
	 */
	private String extractListValueOfAttribute(String attributeOfListValue, String jsonString) {
		String result = null;
		if (TextUtil.getInstance().isEmpty(attributeOfListValue) || TextUtil.getInstance().isEmpty(jsonString)) {
			log.error("Nedostaju obavezni parametri!!!");
			return result;
		}
		String attributeInJsonFormat = "\"" + attributeOfListValue + "\":";   
		// ne moze regExp mora potisni automat (moze da resi i obicna varijabla ali ovako je interesantnije ...)
		Stack<Character> brackets = new Stack<Character>();
		int startIndexOfAttribute = jsonString.indexOf(attributeInJsonFormat);
		int startIndexAttributeValueList = startIndexOfAttribute + attributeInJsonFormat.length();
		StringBuilder resultBuilder = new StringBuilder();
		for (int i = startIndexAttributeValueList; i < jsonString.length(); i++) {
			char currentChar = jsonString.charAt(i);
			resultBuilder.append(currentChar);
			if (currentChar == TextUtil.SQUARE_BRACKET_LEFT.charValue()) {
				brackets.push(TextUtil.SQUARE_BRACKET_LEFT);
			} else if (currentChar == TextUtil.SQUARE_BRACKET_RIGHT.charValue()) {
				brackets.pop();
				if (brackets.empty()) {
					break;
				}
			} 
		}
		result = resultBuilder.toString();
		log.info("Extractovana lista: " + result);
		return result;
	}
}
