package iptv.rest.service;

import java.util.List;

import iptv.rest.v2.model.Package;
import iptv.rest.v2.model.SubscriberPackage;

public interface IptvService {
	public static final String CH_PACKAGES = "ch packages";
	public static final String PROMO_CH_PACKAGES = "promo ch packages";
	public static final String PROMO_PERIOD = "promo period";
	public static final int PROMO_TOLERANCE_DAYS = 5;
	public static final String PIN = "iptv_pin";
	public static final String SUBSCRIBER_UID = "iptv_subscriber_uid";
	
	public static final String TEST_DEVICE_SN = "M11422TW5965";
	public static final String TEST_DEVICE_MAC = "00029bc767de";
	public static final String TEST_DEVICE_SUBSCRIBER = "btnsupporthq";
	public static final String SUBSCRIBER_UID_PREFIX = "btn_";
	public static final String SUBSCRIBER_2GO_SUFIX = ".2go";
	
	/*
	 * uredjaj i subscriber za stage
	 * 
	 * 
	public static final String TEST_DEVICE_SN = "M11422TW6032";  //M15000TW6000
	public static final String TEST_DEVICE_MAC = "00029BC7682B".toLowerCase(); //00009BC7002B
	public static final String TEST_DEVICE_SUBSCRIBER = "btn_bojan.nedeljkovic";
	public static final int TEST_AGREEMENT_ID = 206589;*/
	
	/**
	 * Metoda za dodavanje subscribera
	 * @param agreement
	 * @param technicalParams
	 * @return
	 */
	public boolean addSubscriberWithDefaultProfile(String uid, String pin, String firstName, String lastName);
	
	/**
	 * Nalazi listu paketa koja je dodeljenja subscriberu
	 * 
	 * @param subscriberUid
	 * @return
	 */
	public List<String> findSubscriberPackages(String subscriberUid);
	
	/**
	 * @author nemanja
	 * Metoda uzima sve pakete sa platforme i vraća listu objekata SubscriberPackage
	 * 
	 * @return
	 */
	public List<Package> getAllPackages();
	
	/**
	 * Metoda za update paketa kanala kod promene paketa.
	 * Brisu se kanali koji su na starom paketu a nisu na novom,
	 * a dodaju oni koju su na novom paketu.
	 * 
	 * @param subscriberUid
	 * @param subscriberPackages
	 * @return
	 */
	public boolean updateSubscriberPackages(String subscriberUid, List<String> subscriberPackages);
	
	/**
	 * Uklanja sve pakete subscribera datog subscribera (stopiramo ga)
	 * @param subscriberUid
	 * @return
	 */
	public boolean removeSubscriberPackages(String subscriberUid);
	
	/**
	 * Metoda za trajno brsanje subscribera
	 * @param subscriberUid
	 * @return
	 */
	public boolean deleteSubscriber(String subscriberUid);
	
	/**
	 * Metod koji dodaje STB uredjaj na platformu
	 * @param device
	 * @return
	 */
	public boolean addDevice(String deviceUid, String model, String serialNo, String subscriberUid);
	
	/**
	 * Metoda za trajno brisanje uredjaja
	 * @param deviceUid
	 * @return
	 */
	public boolean deleteDevice(String deviceUid);
	

	
	/**
	 * Metoda za azuriranje PIN-a
	 * @param subscriberUid
	 * @param pin
	 * @return
	 */
	public boolean updateSubscriberPin(String subscriberUid, String pin);
	
	/**
	 * Metoda za azuriranje status-a
	 * @param subscriberUid
	 * @param status
	 * @return
	 */
	public boolean updateSubscriberStatus(String subscriberUid, String status);

	/**
	 * Metoda za nalazenje parental PIN-a
	 * @param subscriberUid
	 * @return
	 */
	public String findParentalPin(String subscriberUid);
	
	/**
	 * Metoda za nalazenje broja uredjaja na platformi
	 * @return
	 */
	public int findDevicesCount();
	
	
	/**
	 * Metoda za povezivanje uredjaja i subscribera
	 * 
	 * subscriberUid == null - raskidanje veze
	 * 
	 * @param deviceUid
	 * @param modelUid
	 * @param subscriberUid

	 * @return
	 */
	public boolean connectDeviceToSubscriber(String deviceUid, String modelUid, String subscriberUid);
	
}
