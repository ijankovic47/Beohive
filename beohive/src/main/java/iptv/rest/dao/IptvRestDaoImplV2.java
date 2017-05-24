package iptv.rest.dao;

import iptv.rest.util.IptvBInteractiveUtilV2;
import iptv.rest.v2.model.Channel;
import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Package;
import iptv.rest.v2.model.Profile;
import iptv.rest.v2.model.ProfileChannel;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;
import iptv.rest.v2.model.Uidable;

import java.util.List;

import net.beotel.model.template.Idable;
import net.beotel.util.PropertiesUtil;
import net.beotel.util.TextUtil;

import org.apache.log4j.Logger;

public class IptvRestDaoImplV2 implements IptvRestDaoV2 {
	private static final Logger log = Logger.getLogger(IptvRestDaoImplV2.class);
	
	private IptvRestManager iptvRestManager;
	
	public IptvRestDaoImplV2(){
		iptvRestManager = new IptvRestManager(PropertiesUtil.getInstance().getIptvRestUrl(), PropertiesUtil.getInstance().getIptvRestCredentials());
	}
	
	public IptvRestDaoImplV2(String url, String credentials){
		iptvRestManager = new IptvRestManager(url, credentials);
	}	
	
	@Override
	public Subscriber findSubscriber(String uid) {
		return findByUid(Subscriber.class, SUBSCRIBERS_SUFIX, uid);
	}
	
	@Override
	public boolean addSubscriber(Subscriber subscriber) {
		return addIdable(SUBSCRIBERS_SUFIX, subscriber);
	}
	
	@Override
	public boolean updateSubscriber(Subscriber subscriber) {
		return updateUidable(SUBSCRIBERS_SUFIX, subscriber);
	}
	
	@Override
	public boolean deleteSubscriber(String uid)  {
		return deleteUidable(SUBSCRIBERS_SUFIX, uid);
	}
	
	@Override
	public Profile findProfile(String uid) {
		return findByUid(Profile.class, PROFILES_SUFIX, uid);
	}
	
	@Override
	public boolean addProfile(Profile profile) {
		return addIdable(PROFILES_SUFIX, profile);
	}
	
	@Override
	public boolean addDevice(Device device) {
		return addIdable(DEVICES_SUFIX, device);
	} 
	
	@Override
	public Device findDevice(String uid) {
		uid = IptvBInteractiveUtilV2.getInstance().convertToValidMac(uid);
		return findByUid(Device.class, DEVICES_SUFIX, uid);
	}
	
	@Override
	public List<Device> findAllSubscriberDevices(String subscriberUid) {
		if (TextUtil.getInstance().isEmpty(subscriberUid)) {
			log.error("Nedostaje obavezan parametar!!!");
			return null;
		}
		String urlPathSuffix = DEVICES_SUFIX+"?subscriber_uid=" + subscriberUid;
		return iptvRestManager.findAllItems(urlPathSuffix, Device.class);
	}
	
	@Override
	public boolean updateDevice(Device device) {
		return updateUidable(DEVICES_SUFIX, device);
	}
	
	@Override
	public boolean deleteDevice(String uid) {
		uid = IptvBInteractiveUtilV2.getInstance().convertToValidMac(uid);
		return deleteUidable(DEVICES_SUFIX, uid);
	}
	
	@Override
	public boolean addSubscriberPackage(SubscriberPackage subscriberPackage) {
		return addIdable(SUBSCRIBER_PACKAGES_SUFIX, subscriberPackage);
	}
	
	@Override
	public boolean deleteSubscriberPackage(Integer subscriberPackageId) {
		if (subscriberPackageId == null) {
			log.error("Nedostaje obavezan parametar!!!");
			return false;
		}
		String urlPathSuffix = SUBSCRIBER_PACKAGES_SUFIX+"/" + subscriberPackageId;
		return iptvRestManager.deleteItem(urlPathSuffix);
	}
	
	@Override
	public List<SubscriberPackage> findAllSubscriberPackages(String subscriberUid) {
		if (TextUtil.getInstance().isEmpty(subscriberUid)) {
			log.error("Nedostaje obavezan parametar!!!");
			return null;
		}
		String urlPathSuffix = SUBSCRIBER_PACKAGES_SUFIX+"?subscriber_uid=" + subscriberUid;
		return iptvRestManager.findAllItems(urlPathSuffix, SubscriberPackage.class);
	}
	
	public boolean addProfileChannel(ProfileChannel profileChannel) {
		return addIdable(PROFILE_CHANNELS_SUFIX, profileChannel);
	}
	
	public boolean deleteProfileChannel(Integer profileChannelId) {
		if (profileChannelId == null) {
			log.error("Nedostaje obavezan parametar!!!");
			return false;
		}
		String urlPathSuffix = "profile_channels/" + profileChannelId;
		return iptvRestManager.deleteItem(urlPathSuffix);
	}
	
	public ProfileChannel findProfileChannel(String profileUid, String chanelUid) {
		if (TextUtil.getInstance().isEmpty(profileUid) || TextUtil.getInstance().isEmpty(chanelUid)) {
			log.error("Nedostaje obavezan parametar!!!");
			return null;
		}
		String urlPathSuffix = "profile_channels?profile_uid=" + profileUid + "&channel_uid=" + chanelUid;
		return iptvRestManager.findItem(urlPathSuffix, ProfileChannel.class);
	}
	
	public boolean updateProfileChannel(Integer profileChannelIdForUpdate, ProfileChannel attributesWithNewValuesProfile) {
		boolean successfullyUpdated = false;
		if (profileChannelIdForUpdate == null || attributesWithNewValuesProfile == null) {
			log.error("Nedostaje obavezan parametar!!!");
			return successfullyUpdated;
		} 
		String urlPathSuffix = "profile_channels/" + profileChannelIdForUpdate;
		successfullyUpdated = iptvRestManager.updateItem(urlPathSuffix, attributesWithNewValuesProfile);
		return successfullyUpdated;
	}

	public boolean deleteProfileChannels(String profile_uid) {
		if (TextUtil.getInstance().isEmpty(profile_uid)) {
			log.error("Nedostaje obavezan parametar!!!");
			return false;
		}
		String urlPathSuffix = "profile_channels?profile_uid=" + profile_uid;
		return iptvRestManager.deleteItem(urlPathSuffix);
	}
	
	public boolean addChannel(Channel channel) {
		return addIdable(CHANNELS_SUFIX, channel);
	}
	
	@Override
	public Channel findChannel(String uid) {
		return findByUid(Channel.class, CHANNELS_SUFIX, uid);
	}
	
	@Override
	public List<Channel> findAllChannels() {
		String urlPathSuffix = CHANNELS_SUFIX;
		return iptvRestManager.findAllItems(urlPathSuffix, Channel.class);
	}
	
	@Override
	public boolean updateChannel(Channel channel) {
		return updateUidable(CHANNELS_SUFIX, channel);
	}

	@Override
	public boolean updateProfile(Profile profile) {
		return updateUidable(PROFILES_SUFIX, profile);
	}
	
	/**
	 * Metoda za nalazenje svih objekata po UID-u
	 * @param resultClazz
	 * @param urlSuffix
	 * @param uid
	 * @return
	 */
	private <T extends Uidable> T findByUid(Class<T> resultClazz, String urlSuffix, String uid){
		T found = null;
		if (!TextUtil.getInstance().isEmpty(uid) && resultClazz != null && urlSuffix != null) {
			String urlPathSuffix = urlSuffix+"?uid=" + uid;
			found = iptvRestManager.findItem(urlPathSuffix, resultClazz);
		}else{
			log.error("Nedostaje obavezan parametar, UID, klasa objekta koji trazimo ili sufix!!!");
		}
		log.info("Objekat tipa "+resultClazz.getName()+" sa UID: "+uid + (found != null? " JE":" NIJE")+" pronadjen");
		return found;
	}
	
	/**
	 * Metoda za nalazenje svih objekata po UID-u
	 * @param resultClazz
	 * @param urlSuffix
	 * @param uid
	 * @return
	 */
	private <T extends Idable> T findById(Class<T> resultClazz, String urlSuffix, Integer id){
		T found = null;
		if (id != null && resultClazz != null && urlSuffix != null) {
			String urlPathSuffix = urlSuffix+"?id=" + id;
			found = iptvRestManager.findItem(urlPathSuffix, resultClazz);
		}else{
			log.error("Nedostaje obavezan parametar, UID, klasa objekta koji trazimo ili sufix!!!");
		}
		log.info("Objekat tipa "+resultClazz.getName()+" sa ID: "+id + (found != null? " JE":" NIJE")+" pronadjen");
		return found;
	}
	
	/**
	 * Metoda za azuriranje UID-able objekata
	 * @param urlSuffix
	 * @param attributesToUpdate
	 * @return
	 */
	private <T extends Uidable> boolean updateUidable(String urlSuffix, T attributesToUpdate) {
		String className = "UNDEFINED";
		boolean successfullyUpdated = false;
		if (attributesToUpdate != null && !TextUtil.getInstance().isEmpty(attributesToUpdate.getUid())) {
			className = attributesToUpdate.getClass().getName();
			String uid = attributesToUpdate.getUid();
			String urlPathSuffix = urlSuffix+"?uid=" + uid;
			successfullyUpdated = iptvRestManager.updateItem(urlPathSuffix, attributesToUpdate);
			return successfullyUpdated;
		}else{
			log.error("Nedostaje obavezan parametar uid ili objekat za update!!!");
		}
		log.info("Rezultat azuriranja objekta tipa "+className+" sa UID :"+(attributesToUpdate != null? attributesToUpdate.getUid():null)+": "+successfullyUpdated);
		return successfullyUpdated;
	}
	
	/**
	 * Metoda za dodavanje ID-able objekta
	 * @param urlSuffix
	 * @param idable
	 * @return
	 */
	private <T extends Idable> boolean addIdable(String urlSuffix, T idable){
		String className = "UNDEFINED";
		boolean successfullyAdded = false;
		if (idable != null) {
			successfullyAdded = iptvRestManager.addItem(urlSuffix, idable);
			className = idable.getClass().getName();
		}else{
			log.warn("Nije prosledjen objekat za snimanje!");
		}
		log.info("Rezultat dodavanje objekta tipa "+className+": "+successfullyAdded);
		return successfullyAdded;			
	}
	
	/**
	 * Metoda za brisanje UIdable objekta
	 * @param urlSuffix
	 * @param uid
	 * @return
	 */
	private boolean deleteUidable(String urlSuffix, String uid){
	boolean deleted = false;
		if (!TextUtil.getInstance().isEmpty(uid) && !TextUtil.getInstance().isEmpty(urlSuffix) ) {
			deleted = iptvRestManager.deleteItem(urlSuffix+"?uid="+uid);
		}else{
			log.error("Nedostaje obavezan parametar!!!");
		}
		log.info("Rezultat brisanja objekta sa UID "+uid+" sa sufixom "+urlSuffix+": " +deleted);
		return deleted;
	}

	@Override
	public List<Subscriber> findAllSubscribers() {
		String urlPathSuffix = SUBSCRIBERS_SUFIX;
		return iptvRestManager.findAllItems(urlPathSuffix, Subscriber.class);
	}

	@Override
	public Package findPackageById(Integer id) {
		return findById(Package.class, PACKAGES_SUFIX, id);
	}
	
	@Override
	public Subscriber findSubscriberById(Integer id) {
		return findById(Subscriber.class, SUBSCRIBERS_SUFIX, id);
	}

	@Override
	public List<Device> findAllDevices() {
		return iptvRestManager.findAllItems(DEVICES_SUFIX, Device.class);
	}

	@Override
	public boolean deleteProfile(String profileUid) {
		return deleteUidable(PROFILES_SUFIX, profileUid);
	}

	@Override
	public List<Package> findAllPackages() {
		return iptvRestManager.findAllItems(PACKAGES_SUFIX, Package.class);
	}

}
