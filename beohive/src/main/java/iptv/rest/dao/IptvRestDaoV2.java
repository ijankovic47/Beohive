package iptv.rest.dao;

import iptv.rest.v2.model.Channel;
import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Package;
import iptv.rest.v2.model.Profile;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;

import java.util.List;

public interface IptvRestDaoV2 {

	String CHANNELS_SUFIX = "v1/channels";
	String SUBSCRIBERS_SUFIX = "v1/subscribers";
	String PROFILES_SUFIX = "v1/profiles";
	String DEVICES_SUFIX = "v1/devices";
	String PACKAGES_SUFIX = "v1/packages";
	String SUBSCRIBER_PACKAGES_SUFIX = "v1/subscriber_packages";
	String SUBSCRIBER_TYPES_SUFIX = "v1/subscriber_types";
	String PROFILE_CHANNELS_SUFIX = "v1/profile_channels";
	
	/**
	 * Pokusava da nadje jedinstvenog subscribara po uid-u
	 * 
	 * @param uid
	 * @return
	 */
	public Subscriber findSubscriber(String uid);

	/**
	 * Dodaje subscribera na platformu
	 * 
	 * @param subscriber
	 * @return
	 */
	public boolean addSubscriber(Subscriber subscriber);
	
	/**
	 * Metoda za azuriranje subscribera
	 * @param subscriber
	 * @return
	 */
	boolean updateSubscriber(Subscriber subscriber);
	

	/**
	 * Brisanje subscribera po uidu
	 * 
	 * @param uid
	 * @return
	 */
	public boolean deleteSubscriber(String uid);
	
	/**
	 * Metoda za nalazenje svih subscribera
	 * @return
	 */
	public List<Subscriber> findAllSubscribers();

	/**
	 * Dodaje profil nekog subscribera na platformu
	 * 
	 * @param profile
	 * @return
	 */
	public boolean addProfile(Profile profile);

	/**
	 * Nalazi profil po uid-u
	 * 
	 * @param uid
	 * @return
	 */
	public Profile findProfile(String uid);


	/**
	 * Dodavanje device-a na platformu
	 * 
	 * @param device
	 * @return
	 */
	public boolean addDevice(Device device);

	/**
	 * Metod za upldate device-a
	 * 
	 * @param deviceUidForUpdate (Ovo je uid device koga cemo updateovati)
	 * @param attributesWithNewValuesDevice (Ovo je struktura koja cuva nove vrednosti koje cemo upisati u deviceUidForUpdate)
	 * @return
	 */
	public boolean updateDevice(Device device);

	/**
	 * Uklanja device sa platforme
	 * 
	 * @param uid
	 * @return
	 */
	public boolean deleteDevice(String uid);

	/**
	 * Nalazi sve kanale 
	 * @author nemanja
	 * 
	 * @return
	 */
	public List<Package> findAllPackages();
	
	/**
	 * Nalazi sve kanale subscribera
	 * 
	 * @param subscriberUid
	 * @return
	 */
	public List<SubscriberPackage> findAllSubscriberPackages(String subscriberUid);

	/**
	 * Dodaje novi subscriberPackage
	 * 
	 * @param subscriberPackage
	 * @return
	 */
	public boolean addSubscriberPackage(SubscriberPackage subscriberPackage);
	
	
	/**
	 * Metoda za nalazenje paketa
	 * @param uid
	 * @return
	 */
	public Package findPackageById(Integer id);

	/**
	 * Uklanja subscriberPackage nekom subscriberu
	 * 
	 * @param subscriberPackageId
	 * @return
	 */
	public boolean deleteSubscriberPackage(Integer subscriberPackageId);

	/**
	 * Dodaje profilChannel na platformu
	 * 
	 * @param profileChannel
	 * @return
	 */
//	public boolean addProfileChannel(ProfileChannel profileChannel);

	/**
	 * Nalazi profileChannel datog profila za dati kanal
	 * 
	 * @param profileUid
	 * @param chanelUid
	 * @return
	 */
//	public ProfileChannel findProfileChannel(String profileUid, String chanelUid);

	/**
	 * Ukalanja profileChannel
	 * 
	 * @param profileChannelId
	 * @return
	 */
//	public boolean deleteProfileChannel(Integer profileChannelId);

	/**
	 * Vrsi update profileChannelUidForUpdate, a nove vrednosti su neNull polja attributesWithNewValuesProfile
	 * 
	 * @param profileChannelUidForUpdate
	 * @param attributesWithNewValuesProfile
	 * @return
	 */
//	public boolean updateProfileChannel(Integer profileChannelIdForUpdate, ProfileChannel attributesWithNewValuesProfile);

	/**
	 * Nalazi sve moguce tipove subscribera
	 * 
	 * @return
	 */
	//public List<SubscriberType> findAllSubscribberTypes();

	/**
	 * Nalazi uredjaj po uid (To moze biti MAC kod nas)
	 * 
	 * @param uid
	 * @return
	 */
	public Device findDevice(String uid);

	/**
	 * Nalazi sve uredjaje za datog subscribera
	 * 
	 * @param subscriberUid
	 * @return
	 */
	public List<Device> findAllSubscriberDevices(String subscriberUid);

	/**
	 * Uklanje sve profile channels za dati profile
	 * 
	 * @param profile_uid
	 * @return
	 */
//	public boolean deleteProfileChannels(String profile_uid);

	/**
	 * Dodavanje izabranog kanala
	 * @param channel
	 * @return
	 */
//	boolean addChannel(Channel channel);

	/**
	 * Metoda za nalazenje kanala
	 * @param uid
	 * @return
	 */
	public Channel findChannel(String uid);

	/**
	 * Metoda za nalazenje svih kanala
	 * @return
	 */
	public List<Channel> findAllChannels();

	/**
	 * Metoda za azuriranje kanala
	 * @param channel
	 * @return
	 */
	boolean updateChannel(Channel channel);
	
	/**
	 * Metoda za azuriranje profila
	 * @param profile
	 * @return
	 */
	boolean updateProfile(Profile profile);

	
	Subscriber findSubscriberById(Integer id);

	/**
	 * Metoda za nalazenje svih uredjaja na platformi
	 * @return
	 */
	List<Device> findAllDevices();
	
	boolean deleteProfile(String profileUid);
}
