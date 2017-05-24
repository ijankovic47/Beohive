/**
 * 
 */
package iptv.rest.util;


import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Profile;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;

import java.util.Date;
import java.util.List;

import net.beotel.util.Calculator;

import org.apache.log4j.Logger;

import java.util.Arrays;

/**
 * @author Bojan
 *
 */
public class IptvBInteractiveUtilV2 {

	private static final Integer REGION_ID = 1;
	
	private static final Integer OPERATOR_ID = 1;

	private static final  String DEFAULT_SUBSCRIBER_TYPE = "POSTPAID";
	private static final  Integer DEFAULT_SKIN_ID = 1;
	
	public static final String DEFAULT_PIN = "12345";
	public static final String SUBSCRIBER_STATUS_ACTIVE = "ACTIVE";
	public static final String SUBSCRIBER_STATUS_SUSPENDED = "SUSPENDED";
	public static final String SUBSCRIBER_STATUS_INACTIVE = "INACTIVE";
	public static final String[] SUBSCRIBER_STATUSES = new String[] {SUBSCRIBER_STATUS_ACTIVE, SUBSCRIBER_STATUS_SUSPENDED, SUBSCRIBER_STATUS_INACTIVE};
	private static final String DEFAULT_DEVICE_CLASS = "STB";	
	private static final int DEFAULT_PARENTAL_RATING = 12;
	public static final String VIP1113_MODEL_UID = "vip1113";
	
	public String MAC_ADDRESS_REGEX = "^([a-f0-9]{12})$";

	private static Logger log = Logger.getLogger(IptvBInteractiveUtilV2.class);
	
	public static final String ARENA_IPTV_PLATFORM_PACKAGE = "Arena Sport 234";
	public static final String ADULTS_IPTV_PLATFORM_PACKAGE = "PinkXXX";
	public static final String TIMESHIFT_PLATFORM_PACKAGE = "basictimeshift";
	
	public static final String[] BINTERACTIVE_PLATFORM_PACKAGES = new String[]{"BeoInfo", "BeoTV Basic", "Pink", ARENA_IPTV_PLATFORM_PACKAGE, ADULTS_IPTV_PLATFORM_PACKAGE, "ArenaPromo", "PinkXXXPromo", TIMESHIFT_PLATFORM_PACKAGE};
	public static final String[] BINTERACTIVE_FULL_PLATFORM_PACKAGES = new String[]{"BeoInfo", "BeoTV Basic", "Pink", "Arena Sport 234", "PinkXXX", "basictimeshift"};
	public static final String BTN_2GO_PLATFORM_PACKAGE = "basictimeshift";
	
	
	private static IptvBInteractiveUtilV2 instance;

	public static IptvBInteractiveUtilV2 getInstance() {
		if (instance == null) {
			instance = new IptvBInteractiveUtilV2();
		}
		return instance;
	}

	/**
	 * Metoda konstruktor koja ucitava potreban fajl.
	 */
	private IptvBInteractiveUtilV2() {
	}

	
	public String convertToValidMac(String input){
		String result = null;
		if(input != null){
			input = input.toLowerCase().replaceAll(":", "");
			if(input.matches(MAC_ADDRESS_REGEX)){
				result = new StringBuilder(input).insert(2, ":").insert(5, ":").insert(8, ":").insert(11, ":").insert(14, ":").toString();
			}
		}
		return result;
	}

	/**
	 * Metoda za kreiranje Device objekta od potrebnih podataka
	 * @param deviceUid
	 * @param subscriberUid
	 * @param modelUid
	 * @param deviceClass
	 * @return
	 */
	public Device createDevice(String deviceUid, String subscriberUid, String modelUid, String deviceClass){
		Device device = null;
		deviceUid = convertToValidMac(deviceUid);
		if(deviceUid != null){
			log.info("Kreiramo uredjaj sa UID: "+ deviceUid + ", model: "+modelUid +", subscriber: "+subscriberUid);
			device = new Device();
			device.setCreated_at(new Date());
			device.setDevice_type_uid(modelUid!=null?modelUid:VIP1113_MODEL_UID);
			device.setOperator_id(OPERATOR_ID);
			device.setSubscriber_uid(subscriberUid);
			device.setUid(deviceUid);
			device.setUpdated_at(new Date());
			device.setDevice_class(deviceClass != null ? deviceClass: DEFAULT_DEVICE_CLASS);
		}else{
			log.warn("Neuspelo kreiranje uredjaja, nedostaje MAC ili model...");
		}
		return device;
	}
	
	public Device createDevice(IptvDevice iptvDevice, String subscriberUid){
		Device result = null;
		if(iptvDevice != null){
			result = createDevice(iptvDevice.getUid(), subscriberUid, iptvDevice.getModelUid(), iptvDevice.getTypeUid());
		}
		return result;
	}
	
	public Subscriber createSubscriber(String uid, String firstName, String secondName, String password, String status){
		Subscriber subscriber = null;
		if(uid != null && password != null){
			log.info("Kreiramo subscribera sa sledecim podacima: ");
			log.info("Uid: "+uid +", ime: "+firstName+", prezime: "+secondName);
			subscriber = new Subscriber();
			subscriber.setCreated_at(new Date());
			subscriber.setFirst_name(firstName);
			subscriber.setLast_name(secondName);
			subscriber.setUid(uid);
			subscriber.setOperator_id(OPERATOR_ID);
			subscriber.setPassword(password);
			subscriber.setRegion_id(REGION_ID);
			subscriber.setType(DEFAULT_SUBSCRIBER_TYPE);
			subscriber.setStatus(status != null? status:SUBSCRIBER_STATUS_ACTIVE);
			subscriber.setUpdated_at(new Date());
		}else{
			log.warn("Nedostaje uid ili pin za kreiranje subscribera...");
		}
		return subscriber;
	}

	public Profile createDefaultProfile(Subscriber subscriber, String pinn){
		Profile profile = null;
		String pin = pinn != null ? pinn : DEFAULT_PIN;
		if(subscriber != null){
			log.info("Kreiramo profil za subscribera: "+subscriber.getUid());
			profile = new Profile();
			profile.setCreated_at(new Date());
			profile.setName(subscriber.getFirst_name()+ " "+ subscriber.getLast_name());
			profile.setIs_default(true);
			profile.setLogin_name(subscriber.getUid());
			profile.setMain_pin(pin);
			profile.setOperator_id(OPERATOR_ID);
			profile.setParental_pin(pin);
			profile.setParental_rating(DEFAULT_PARENTAL_RATING);
			profile.setSkin_id(DEFAULT_SKIN_ID);
			profile.setSubscriber_uid(subscriber.getUid());
			profile.setUid(subscriber.getUid());
			profile.setUpdated_at(new Date());
		}else{
			log.warn("Nije prosledjen subscriber, ne mozemo da kreiramo profil...");
		}
		return profile;
	}
	
	public SubscriberPackage createSubscriberPackage(String packageUid, String subscriberUid){
		SubscriberPackage subscriberPackage = null;
		if(packageUid != null && subscriberUid != null){
			log.warn("Subscriberu "+subscriberUid+" dodajemo paket: "+packageUid);
			subscriberPackage = new SubscriberPackage();
			subscriberPackage.setPackage_uid(packageUid);
			subscriberPackage.setSubscriber_uid(subscriberUid);
			subscriberPackage.setActive_from(new Date());
			subscriberPackage.setActive_to(null);
		}else{
			log.warn("Nije prosledjen subscriberUid ili packageUid");
		}
		return subscriberPackage;
	}
	
	public SubscriberPackage createSubscriberPackageFromIds(Integer packageId, Integer subscriberId){
		SubscriberPackage subscriberPackage = null;
		if(packageId != null && subscriberId != null){
			log.warn("Subscriberu sa ID "+subscriberId+" dodajemo paket sa ID: "+packageId);
			subscriberPackage = new SubscriberPackage();
			subscriberPackage.setPackage_id(packageId);
			subscriberPackage.setSubscriber_id(subscriberId);
			subscriberPackage.setActive_from(new Date());
			subscriberPackage.setActive_to(null);
		}else{
			log.warn("Nije prosledjen subscriberUid ili packageUid");
		}
		return subscriberPackage;
	}
	
	@SuppressWarnings("unchecked")
	public List<String> findPlatformPackages(List<String> originalPackages){
		return Calculator.getInstance().findArrayIntersection(originalPackages, Arrays.asList(BINTERACTIVE_PLATFORM_PACKAGES));
	}
	
}
