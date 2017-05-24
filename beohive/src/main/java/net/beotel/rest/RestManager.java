package net.beotel.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.bind.DatatypeConverter;

import net.beotel.onapp.OnAppManager;
import net.beotel.util.TextUtil;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RestManager {
	public static final int SUCCESS_CREATION = 201;
	public static final int NO_CONTENT = 204;

	private static Logger log = Logger.getLogger(RestManager.class);
	
	/**
	 * Ovo ce biti dodeljeno atributima koje hocemo da posaljemo sa vrednoscu null.
	 * Posto neke platforme ne ocekuju null vrednosti zato cistimo json za slanje, dok
	 * druge bas ocekuju null vrednost. Tada gde treba da bude postavljena null vrednost, atributu dodeljujemo NULL_OBJECT 
	 */
	public static final Serializable NULL_OBJECT = new Serializable() {
		private static final long serialVersionUID = -2728199866439374813L;
	};

	private String url;
	private String credentials;

	public RestManager(String url, String credentials){
		this.url = url;
		this.credentials = credentials;
	}
	
	/**
	 * Metod koji dati objekat prebacuje u JSON i salje preko konekcije.
	 * 
	 * @param URLConnection
	 * @param json
	 */
	public void sendData(Object objectToSend, HttpURLConnection urlConnection) {
		try {
			prepareSendingData(objectToSend, urlConnection);
		} catch (IOException e) {
			log.error("Greska pri slanju podataka preko konekcije: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Metod za slanje sirovih podataka tj. json u izvornom formatu
	 * 
	 * @param rawJSONData
	 */
	public void sendRawJSONData(String rawJSONData, HttpURLConnection urlConnection) {
		try {
			prepareSendingRawData(rawJSONData, urlConnection);
		} catch (IOException e) {
			log.error("Greska pri slanju podataka preko konekcije: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Od responsa konekcije generise objekat na osnovu klase objekta
	 * 
	 * @param <T>
	 * @param valueType
	 * @return
	 */
	public <T> T generateObjectFromResponseContent(Class<T> valueType, HttpURLConnection urlConnection) {
		try {
			String jsonResponse = getResponseContent(urlConnection);
			return prepareForGeneratingObjectFromJson(jsonResponse, valueType);
		} catch (JsonParseException e) {
			log.error("JSON parser greska: " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			log.error("Greska pri mapiranju objekta: " + e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Greska pri kreiranju objekta na osnovu JSON-a" + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Od responsa konekcije generise objekat na osnovu JavaType objekta
	 * 
	 * @param <T>
	 * @param valueType
	 * @return
	 */
	public <T> T generateObjectFromResponseContent(JavaType javaType, HttpURLConnection urlConnection) {
		try {
			String jsonResponse = getResponseContent(urlConnection);
			return prepareForGeneratingObjectFromJson(jsonResponse, javaType);
		} catch (JsonParseException e) {
			log.error("JSON parser greska: " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			log.error("Greska pri mapiranju objekta: " + e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Greska pri kreiranju objekta na osnovu JSON-a: " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Generise objkat na osnovu json-a. Potrebno je specificirati kojoj klasi
	 * pripada objekat kroz parametar valueType
	 * 
	 * @param <T>
	 * @param json
	 * @param valueType
	 * @return
	 */
	public <T> T generateObjectFromJSONResponseContent(String json, Class<T> valueType) {
		try {
			return prepareForGeneratingObjectFromJson(json, valueType);
		} catch (JsonParseException e) {
			log.error("JSON parser greska: " + e.getLocalizedMessage());
		} catch (JsonMappingException e) {
			log.error("Greska pri mapiranju objekta: " + e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Greska pri kreiranju objekta na osnovu JSON-a: " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Kreira konekciju i postavlje odgovarajuci requestMethod komunikacije
	 * (GET, POST, PUT, ...)
	 * 
	 * @param urlPathSiffix
	 *            (users.json, billing_plan.json, users/19.json, ...)
	 * @param requestMethod
	 * @return
	 */
	public HttpURLConnection createConnection(String urlPathSuffix, RequestMethod requestMethod) {
		try {
			String urlPath = url + TextUtil.getInstance().toString(urlPathSuffix);
			return prepareConnectionCreation(urlPath, requestMethod);
		} catch (MalformedURLException e) {
			log.error("Lose formiran URL za pravljenje konekcije: " + e.getLocalizedMessage());
		} catch (ProtocolException e) {
			log.error("Protokol greska pri pravljenu konekcije: " + e.getLocalizedMessage());
		} catch (UnsupportedEncodingException e) {
			log.error("Nepodrzan metod za enkodiranje basicAuth-a: " + e.getLocalizedMessage());
		} catch (IOException e) {
			log.error("Greska pri kreiranju konekcije: " + e.getLocalizedMessage());
		}
		return null;
	}

	/**
	 * Vraca nam odgovor servera preko date konekcije. Ideja je da je i odgovor
	 * JSON. Naravno to zavisi od definicije konekcije.
	 * 
	 * @param URLConnection
	 * @return
	 */
	public String getResponseContent(HttpURLConnection urlConnection) {
		try {
			return prepareResponseConnectionContent(urlConnection);
		} catch (IOException e) {
			log.error("Greska pri uzmianju odgovora iz konekcije: " + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * Prekida datu konekciju
	 * 
	 * @param URLConnection
	 */
	public void closeConnection(HttpURLConnection urlConnection) {
		if (urlConnection == null) {
			log.error("Konekcije za zatvaranje ne postoji!!!");
			return;
		}
		urlConnection.disconnect();
	}

	/**
	 * @see OnAppManager#sendJSONOverConnection(HttpURLConnection, String)
	 * 
	 * @param URLConnection
	 * @param json
	 * @throws IOException
	 */
	private void prepareSendingData(Object objectToSend, HttpURLConnection urlConnection) throws IOException {
		if (urlConnection == null) {
			log.error("Ne postoji konekcija za pokusaj slanja podataka!!!");
			return;
		}
		if (objectToSend == null) {
			log.error("Nema podataka za slanje!!!");
			return;
		}
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(objectToSend);
		String[] exceptAttributes = null;
		if (objectToSend instanceof NullAttributeModel) {
			exceptAttributes = ((NullAttributeModel) objectToSend).getExceptAttributes();
		}
		json = TextUtil.getInstance().clearJSONObjectFromNULLs(json, exceptAttributes);
		log.info("Data za slanje: " + json);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
		outputStreamWriter.write(json);
		outputStreamWriter.close();
	}

	private void prepareSendingRawData(String rawJSONData, HttpURLConnection urlConnection) throws IOException {
		if (urlConnection == null) {
			log.error("Ne postoji konekcija za pokusaj slanja podataka!!!");
			return;
		}
		if (rawJSONData == null) {
			log.error("Nema podataka za slanje!!!");
			return;
		}
		log.info("Data za slanje: " + rawJSONData);
		OutputStreamWriter outputStreamWriter = new OutputStreamWriter(urlConnection.getOutputStream());
		outputStreamWriter.write(rawJSONData);
		outputStreamWriter.close();
	}

	/**
	 * 
	 * @param urlPath
	 * @param requestMethod
	 * @return
	 * @throws IOException
	 */
	private HttpURLConnection prepareConnectionCreation(String urlPath, RequestMethod requestMethod) throws IOException {
		setHTTPSCommunicationAsTrustWithoutRequiredCert();
		URL url = new URL(urlPath);
		boolean secure = false;
		log.info("Konekcija se uspostavlja sa URL: " + urlPath);
		if(urlPath != null && urlPath.startsWith("https://")){
			secure = true;
		}
		URLConnection urlPlainConnection = url.openConnection();
		HttpURLConnection urlConnection = secure?(HttpsURLConnection)urlPlainConnection:(HttpURLConnection)urlPlainConnection;
		urlConnection.setRequestProperty("Content-Type", "application/json");
		urlConnection.setDoOutput(true);
		urlConnection.setRequestMethod(requestMethod.toString());
		String basicAuth = "Basic "	+ DatatypeConverter.printBase64Binary(credentials.getBytes("UTF-8"));
		urlConnection.setRequestProperty("Authorization", basicAuth);
		return urlConnection;
	}


	/**
	 * @see CloudServiceWrapper#generateObjectFromJson(String, Class);
	 * 
	 * @param <T>
	 * @param json
	 * @param valueType
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	private <T> T prepareForGeneratingObjectFromJson(String json, Class<T> valueType) throws JsonParseException,
			JsonMappingException, IOException {
		if (json == null) {
			log.error("Nije prosledjen sadrzaj za JSON parsiranje!!!");
			return null;
		}
		log.info("JSON za generisanje: " + json);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, valueType);
	}

	/**
	 * @see CloudServiceWrapper#generateObjectFromJson(String, JavaType);
	 * @param <T>
	 * @param json
	 * @param javaType
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	protected <T> T prepareForGeneratingObjectFromJson(String json, JavaType javaType) throws JsonParseException, JsonMappingException, IOException {
		if (json == null) {
			log.error("Nije prosledjen sadrzaj za JSON parsiranje!!!");
			return null;
		}
		log.info("JSON za generisanje: " + json);
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.readValue(json, javaType);
	}

	/**
	 * @see OnAppManager#getResponseFromConnectionInputStream(HttpURLConnection)
	 * @param URLConnection
	 * @return
	 * @throws MalformedURLException
	 * @throws ProtocolException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	private String prepareResponseConnectionContent(HttpURLConnection urlConnection) throws MalformedURLException, ProtocolException, UnsupportedEncodingException, IOException {
		String response = null;
		if (urlConnection != null) {
			response = readInputStreamLine(urlConnection.getInputStream());
			String errorResponse = readInputStreamLine(urlConnection.getErrorStream());
			if (!TextUtil.getInstance().isEmpty(errorResponse)) {
				log.error("ERROR_STREAM_RESPONSE: " + errorResponse);
			}
			log.info("Dohvacen response content: " + response);
		} else {
			log.error("Konekcija nije postignuta, nema niske za citanje!!!");
		}
		return response;
	}
	
	/**
	 * Vraca procitanu liniju iz input stream-a
	 * 
	 * @param inputStream
	 * @return
	 * @throws IOException
	 */
	private String readInputStreamLine(InputStream inputStream) throws IOException {
		String line = null;
		if (inputStream == null) {
			log.error("Nije prosledjen input stream!!!");
		} else {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			if (bufferedReader != null) {
				line = bufferedReader.readLine();
				bufferedReader.close();
			}
		}
		return line;
	}

	/**
	 * Vraca HTTP response code izvedene akcije
	 * 
	 * @param URLConnection
	 * @return
	 */
	public Integer getResponseCode(HttpURLConnection urlConnection) {
		Integer responseCode = null;
		if (urlConnection != null){
			try {
				responseCode = urlConnection.getResponseCode();
			} catch (IOException e) {
				log.error("Greska pri uzimanju responseCode-a iz konekcije: " + e.getLocalizedMessage());
			}
		}else{
			log.error("Nema konekcije!");
		}
		return responseCode;
	}

	/**
	 * Metod koji proverava da li je responseCode transakcije jednak ocekivanom
	 * 
	 * @param expectedResponseCode
	 *            ocekivani response code
	 * @return
	 */
	public boolean checkResponseCodeValidity(int expectedResponseCode, HttpURLConnection urlConnection) {
		Integer transactionResponseCode = getResponseCode(urlConnection);
		if (transactionResponseCode == null) {
			log.error("Greska pri uzimanju responseCode transakcije, vracen NULL!");
			return false;
		}
		log.info("Transakcija zavrsena kodom: " + transactionResponseCode + " a ocekivan code: " + expectedResponseCode);
		return transactionResponseCode.intValue() == expectedResponseCode;
	}

	/**
	 * Metod koji dodaje objekat
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param resultClazz
	 * @param objectForAdding objekat koji se dodaje (NPR. User)
	 * @return
	 */
	public <T> T addObject(String urlPathSuffix, Class<T> resultClazz, Object objectForAdding) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.POST);

		sendData(objectForAdding, urlConnection);

		T result = generateObjectFromResponseContent(resultClazz, urlConnection);

		closeConnection(urlConnection);
		return result;
	}
	
	/**
	 * Metod za brisanje objekata
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param resultClazz (klasa ocekivanog rezultujuceg objekta)
	 * @return
	 */
	public <T> T deleteObject(String urlPathSuffix, Class<T> resultClazz) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.DELETE);

		T result = generateObjectFromResponseContent(resultClazz, urlConnection);

		closeConnection(urlConnection);
		return result;
	}
	
	/**
	 * Metod za update objekata
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param resultClazz (klasa ocekivanog rezultujuceg objekta)
	 * @return
	 */
	public <T> T updateObject(String urlPathSuffix, Class<T> resultClazz, Object onlyFilledAttributesForUpdate) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.PUT);

		sendData(onlyFilledAttributesForUpdate, urlConnection);
		
		T result = generateObjectFromResponseContent(resultClazz, urlConnection);

		closeConnection(urlConnection);
		return result;
	}

	/**
	 * Metod omogucava da se komunikacija ostvarai preko https-a a da pritom i
	 * <b>SAMO U SLUCAJU DA NISU POTREBNI SERTIFIKATI</b>
	 */
	private void setHTTPSCommunicationAsTrustWithoutRequiredCert() {
		TrustManager[] trustAllCerts = createAllTrustManager();
		initializeSSLSocketFactory(trustAllCerts);
		setAllTrustedHosts();
	}

	/**
	 * Kreiramo TrustManager nisku koja ne vrsi nikakvu validaciju
	 * 
	 * @return
	 */
	private TrustManager[] createAllTrustManager() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			@Override
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}
		} };
		return trustAllCerts;
	}

	/**
	 * Inicijalizujemo SSLSocketFactory sa prosledjenim TrustManager-ima
	 * 
	 * @param trustAllCerts
	 */
	private void initializeSSLSocketFactory(TrustManager[] trustAllCerts) {
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			log.error("Neuspsno kreiranje sslContext za SSL protokol: " + e.getLocalizedMessage());
		} catch (KeyManagementException e) {
			log.error("Greska pri obradi kljuceva za enkripciju: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Postavljamo da verujemo svim hostovima sa kojma komuniciramo
	 */
	private void setAllTrustedHosts() {
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	}
	
	public boolean doTransactionAndCheckResponseCode(String urlPathSuffix, RequestMethod requestMethod, Integer expectedCode){
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, requestMethod);
		boolean transactionValidity = checkResponseCodeValidity(expectedCode, urlConnection);
		closeConnection(urlConnection);
		return transactionValidity;
	}
	
	public boolean doDoubleTransactionAndCheckResponseCode(String urlPathSuffix, RequestMethod requestMethod, Integer expectedCode){
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, requestMethod);
		urlConnection = createConnection(urlPathSuffix, requestMethod);
		boolean transactionValidity = checkResponseCodeValidity(expectedCode, urlConnection);
		closeConnection(urlConnection);
		return transactionValidity;
	}
	
	public boolean sendObjectAndCheckResponseCode(String urlPathSuffix, RequestMethod requestMethod, Integer expectedCode, Object objectForSending){
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, requestMethod);
		sendData(objectForSending, urlConnection);
		boolean transactionValidity = checkResponseCodeValidity(expectedCode, urlConnection);
		closeConnection(urlConnection);
		return transactionValidity;
	}
	
	public <T> T doTransactionAndReturnContentObject(String urlPathSuffix, RequestMethod requestMethod, Class<T> clazz){
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, requestMethod);
		T content = generateObjectFromResponseContent(clazz, urlConnection);
		closeConnection(urlConnection);
		return content;
	}

	public <T> T doTransactionAndReturnContentObject(String urlPathSuffix, RequestMethod requestMethod, JavaType javaType){
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, requestMethod);
		T content = generateObjectFromResponseContent(javaType, urlConnection);
		closeConnection(urlConnection);
		return content;
	}
	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCredentials() {
		return credentials;
	}

	public void setCredentials(String credentials) {
		this.credentials = credentials;
	}
}
