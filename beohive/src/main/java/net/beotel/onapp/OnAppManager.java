package net.beotel.onapp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.beotel.rest.RestManager;
import net.beotel.util.Calculator;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class OnAppManager extends RestManager {
	private static Logger log = Logger.getLogger(OnAppManager.class);

	public OnAppManager(String url, String credentials){
		super(url, credentials);
	}

	/**
	 * Vraca listu objekata tipa elementClass, wrapperClass je omot svakog
	 * takvog elementa, kao NPR. <br>
	 * UserWrapper je omot objekta User. <br>
	 * JSON odgovor je NPR. [{"user":{..atributs..}}, {"user":{..atributs..}},
	 * {"user":{..atributs..}}, ...] <br>
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param wrapperClass
	 * @param elementClass
	 * @return
	 */
	public <T> List<T> findAllOnAppObjects(String urlPathSuffix, Class<?> wrapperClass, Class<T> elementClass) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.GET);

		// JavaType moramo da definisemo kada je kao response anonimna JSON
		// list-a tj.
		// list objekata NPR. [{element1}, {element2}, ..., {elementN}]
		// U vecini slucajeva reziltat je objekat odredjene klase, pa ga mozemo
		// gadjati po klasi
		// ali ovde se vraca JSON lista
		JavaType collectionJavaType = TypeFactory.defaultInstance().constructCollectionType(List.class, wrapperClass);
		List<?> wrappers = generateObjectFromResponseContent(collectionJavaType, urlConnection);
		closeConnection(urlConnection);

		return extractListElementsFromListWrappers(wrappers, elementClass);
	}
	
	/**
	 * Vraca listu objekata tipa elementClass
	 * JSON odgovor je NPR. [{"user":{..atributs..}}, {"user":{..atributs..}},
	 * {"user":{..atributs..}}, ...] <br>
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param elementClass
	 * @return
	 */
	public <T> List<T> findAllOnAppObjects(String urlPathSuffix, Class<T> elementClass) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.GET);

		// JavaType moramo da definisemo kada je kao response anonimna JSON
		// list-a tj.
		// list objekata NPR. [{element1}, {element2}, ..., {elementN}]
		// U vecini slucajeva reziltat je objekat odredjene klase, pa ga mozemo
		// gadjati po klasi
		// ali ovde se vraca JSON lista
		JavaType collectionJavaType = TypeFactory.defaultInstance().constructCollectionType(List.class, elementClass);
		List<T> elements = generateObjectFromResponseContent(collectionJavaType, urlConnection);
		closeConnection(urlConnection);

		return elements;
	}

	/**
	 * Izvlacimo iz liste wrapera zapakovane elemente, smesta ih u novu listu
	 * tipa elementType i vraca tu listu kao rezultat. <br>
	 * <br>
	 * 
	 * NPR. Ako je prosledjena List(UserWrapper) svaki element UserWraper ima
	 * atribut User, za rezultat cemo dobiti List(User)
	 * 
	 * @param <T>
	 * @param wrapperList
	 * @param elementType
	 * @return
	 */
	public <T> List<T> extractListElementsFromListWrappers(List<?> wrapperList, Class<T> elementType) {
		if (Calculator.getInstance().isEmpty(wrapperList)) {
			log.warn("Prosledjena prazna lista za ekstrahovanje elemenata!!!");
			return null;
		}
		List<T> resultListElements = new ArrayList<T>();
		for (Object wrapper : wrapperList) {
			Object element = extractElementFromWrapper(wrapper, elementType, UnwrapElement.class);
			if (element != null)
				resultListElements.add(elementType.cast(element));
		}
		log.info("Pronadjeno " + (resultListElements != null ? resultListElements.size() : 0) + " elemenata u listi.");
		return resultListElements;
	}

	/**
	 * Template metod koji pronalazi onApp objekat po ID
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param wrapperClassType
	 *            omot klasa u kojoj se nalazi zeljeni objekat
	 * @param elementClassType
	 *            klasa zeljenog objekta
	 * @return
	 */
	public <T> T findOnAppObjectById(String urlPathSuffix, Class<?> wrapperClassType, Class<T> elementClassType) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.GET);

		Object wrapper = generateObjectFromResponseContent(wrapperClassType, urlConnection);

		closeConnection(urlConnection);
		if (wrapper != null) {
			return extractElementFromWrapper(wrapper, elementClassType, UnwrapElement.class);
		}
		return null;
	}


	/**
	 * Template metod koji pronalazi onApp objekat po ID
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param elementClassType
	 *            klasa zeljenog objekta
	 * @return
	 */
	public <T> T findOnAppObjectById(String urlPathSuffix, Class<T> elementClassType) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.GET);

		T element = generateObjectFromResponseContent(elementClassType, urlConnection);

		closeConnection(urlConnection);
		return element;
	}
	
	/**
	 * Metod koji dodaje onApp objekat u onApp
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 * @param wrapperClassType
	 *            klasa omot objekta koji dodajemo (NPR. UserWrapper je omot za
	 *            User-a)
	 * @param objectForAdding
	 *            objekat koji se dodaje (NPR. User)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T addOnAppObject(String urlPathSuffix, Class<?> wrapperClassType, T objectForAdding) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.POST);

		Object objectWrapper = createInstanceFromClassDefaultConstructor(wrapperClassType);
		insertElementInWrapper(objectWrapper, objectForAdding, WrapElement.class);
		sendData(objectWrapper, urlConnection);

		Object responseWrapper = generateObjectFromResponseContent(wrapperClassType, urlConnection);

		closeConnection(urlConnection);
		if (responseWrapper != null) {
			return (T) extractElementFromWrapper(responseWrapper, objectForAdding.getClass(), UnwrapElement.class);
		}
		return null;
	}
	
	/**
	 * Metod koji dodaje onApp objekat u onApp
	 * 
	 * @param <T>
	 * @param urlPathSuffix
	 *            User-a)
	 * @param objectForAdding
	 *            objekat koji se dodaje (NPR. User)
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T addOnAppObject(String urlPathSuffix, T objectForAdding) {
		HttpURLConnection urlConnection = createConnection(urlPathSuffix, RequestMethod.POST);

		sendData(objectForAdding, urlConnection);

		T responseObject = (T) generateObjectFromResponseContent(objectForAdding.getClass(), urlConnection);

		closeConnection(urlConnection);
		return responseObject;
	}
	
	/**
	 * Kreira instancu objekta po defaultnom konstruktoru tipa classType
	 * 
	 * @param <T>
	 * @param classType
	 * @return
	 */
	private <T> T createInstanceFromClassDefaultConstructor(Class<T> classType) {
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
		}
		return null;
	}

	/**
	 * Iz objekta wraper za koji pretpostavljamo da ima na odgovarajucoj metodi
	 * postavljenu anotaciju annotate (ona metoda koja nam vraca bas taj
	 * zapakovan element tj. njegov getter) Pozivajuci taj geter vracamo
	 * zapakovani element
	 * 
	 * 
	 * @param <T>
	 * @param wrapper
	 * @param elementType
	 * @param annotationType
	 * @return
	 */
	private <T> T extractElementFromWrapper(Object wrapper, Class<T> elementType,
			Class<? extends Annotation> annotationType) {
		Method method = getClassMethodAnnotatedWithGivenAnnotation(wrapper.getClass(), annotationType);
		if (method == null) {
			log.error("Nije pronadjena anotacija " + annotationType + " na bilo kojoj od metoda klase " + wrapper.getClass().getName());
			return null;
		}
		try {
			Object element = method.invoke(wrapper);
			return elementType.cast(element);
		} catch (Exception e) {
			log.error("Greska pri invoku metode " + method.getName() + " exception: " + e.getLocalizedMessage());
			return null;
		}
	}

	/**
	 * U wrapper objektu poziva metodu sa datom anotaciom i kao argument joj
	 * prosledjuje elementForInsertion. Metoda koja ima datu anotacije je setter
	 * sa argumentom elementForInsertion objekta wrapper.
	 * 
	 * @param wrapper
	 * @param elementForInsertion
	 * @param annotationType
	 */
	private void insertElementInWrapper(Object wrapper, Object elementForInsertion,
			Class<? extends Annotation> annotationType) {
		Method method = getClassMethodAnnotatedWithGivenAnnotation(wrapper.getClass(), annotationType);
		if (method == null) {
			log.error("Nije pronadjena anotacija " + annotationType + " na bilo kojoj od metoda klase " + wrapper.getClass().getName());
		}
		try {
			method.invoke(wrapper, elementForInsertion);
		} catch (Exception e) {
			log.error("Greska pri invoku metode " + method.getName() + " exception: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Metod koji vraca prvu metodu (Pretpostavka da samo jedna metoda ima datu
	 * annotation) is proizvoljne klase type naznacenu datom anotaciom
	 * 
	 * @param type
	 *            tip klase u kojoj trazimo metode sa datom anotacijom
	 * @param annotation
	 *            trazena anotacija kojom smo naznacili ciljnu metodu
	 * @return
	 */
	private Method getClassMethodAnnotatedWithGivenAnnotation(Class<?> type, Class<? extends Annotation> annotation) {
		List<Method> allMethods = new ArrayList<Method>(Arrays.asList(type.getDeclaredMethods()));
		for (Method method : allMethods) {
			if (method.isAnnotationPresent(annotation)) {
				return method;
			}
		}
		return null;
	}
	
}
