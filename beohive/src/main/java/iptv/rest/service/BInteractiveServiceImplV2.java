package iptv.rest.service;

import iptv.rest.dao.IptvRestDaoImplV2;
import iptv.rest.dao.IptvRestDaoV2;
import iptv.rest.util.IptvBInteractiveUtilV2;
import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Package;
import iptv.rest.v2.model.Profile;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.beotel.util.Calculator;

import org.apache.log4j.Logger;


public class BInteractiveServiceImplV2 implements IptvServiceV2{
	
	private static Logger log = Logger.getLogger(BInteractiveServiceImplV2.class);
	private IptvRestDaoV2 iptvRestDao;
	
	public BInteractiveServiceImplV2(IptvRestDaoV2 iptvRestDao){
		this.iptvRestDao = iptvRestDao;
	}

	static Map<Integer, String> packagesMap = new HashMap<Integer, String>();
	
	@Override
	public boolean addSubscriberWithDefaultProfile(String uid, String pin, String firstName, String lastName) {
		boolean allCreated = false;
		Subscriber subscriber = IptvBInteractiveUtilV2.getInstance().createSubscriber(uid, firstName, lastName, Calculator.getInstance().randomGenerator(10), IptvBInteractiveUtilV2.SUBSCRIBER_STATUS_ACTIVE);
		boolean subscriberAdded = iptvRestDao.addSubscriber(subscriber);
		if(subscriberAdded){
			log.info("Uspesno dodat subscriber sa UID: "+subscriber.getUid());
			Profile profile = IptvBInteractiveUtilV2.getInstance().createDefaultProfile(subscriber, pin);
			if(profile != null){
				allCreated = iptvRestDao.addProfile(profile);
			}
		}
		return allCreated;
	}

	@Override
	public List<String> findSubscriberPackages(String subscriberUid) {
		List<String> packages = new ArrayList<String>();
		if(subscriberUid != null){
			List<SubscriberPackage> subscriberPackages = iptvRestDao.findAllSubscriberPackages(subscriberUid);
			if(!Calculator.getInstance().isEmpty(subscriberPackages)){
				for(SubscriberPackage subscriberPackage: subscriberPackages){
					Integer packageId = subscriberPackage.getPackage_id();
					String packageUid = findPackageNameById(packageId);
					if(packageUid != null){
						packages.add(packageUid);
					}
				}
			}
		}
		return packages;
	}

	@Override
	public boolean removeSubscriberPackages(String subscriberUid) {
		boolean allDeleted = true;
		List<SubscriberPackage> subscriberPackages = iptvRestDao.findAllSubscriberPackages(subscriberUid);
		if(!Calculator.getInstance().isEmpty(subscriberPackages)){
			for(SubscriberPackage subscriberPackage: subscriberPackages){
				allDeleted &= iptvRestDao.deleteSubscriberPackage(subscriberPackage.getId());
			}
		}
		return allDeleted;
	}
	
	
	@Override
	public boolean updateSubscriberPackages(String subscriberUid, List<String> subscriberPackages) {
		if(Calculator.getInstance().isEmpty(subscriberPackages)){
			return removeSubscriberPackages(subscriberUid);
		}
		boolean fixed = true;
		List<String> foundOKPackages = new ArrayList<String>();
		List<SubscriberPackage> existingPackages = iptvRestDao.findAllSubscriberPackages(subscriberUid);
		if(!Calculator.getInstance().isEmpty(existingPackages)){
			for(SubscriberPackage existing : existingPackages){
				if(!Calculator.getInstance().isElementInList(existing.getPackage_uid(), subscriberPackages)){
					log.info("Subscriberu " + subscriberUid + " brisemo paket sa UID: "+existing.getPackage_uid());
					fixed &= iptvRestDao.deleteSubscriberPackage(existing.getId());
				}else{
					log.info("Subscriberu " + subscriberUid + " ima regularan paket sa UID: "+existing.getPackage_uid());
					foundOKPackages.add(existing.getPackage_uid());
				}
			}
		}
		List<String> toAdd = Calculator.getInstance().findArrayDifference(subscriberPackages, foundOKPackages);
		if(!Calculator.getInstance().isEmpty(toAdd)){
			for(String packageToAdd : toAdd){
				SubscriberPackage subscriberPackage = IptvBInteractiveUtilV2.getInstance().createSubscriberPackage(packageToAdd, subscriberUid);
					fixed &= iptvRestDao.addSubscriberPackage(subscriberPackage);
					log.info("Subscriberu " + subscriberUid + " dodajemo paket sa UID: "+toAdd);
			}
		}
		return fixed;
	}

	@Override
	public boolean deleteSubscriber(String subscriberUid) {
		/*ako nadjemo subscribera, pokusavamo brisanje*/
		boolean deleted = true;
		Subscriber subscriber = iptvRestDao.findSubscriber(subscriberUid);
		if(subscriber != null){
			deleted = iptvRestDao.deleteSubscriber(subscriberUid);
		}
		return deleted;
	}

	@Override
	public boolean deleteDevice(String deviceUid) {
		/*ako nadjemo uredjaj, pokusavamo brisanje*/
		boolean deleted = true;
		Device device = iptvRestDao.findDevice(deviceUid);
		if(device != null){
			deleted = iptvRestDao.deleteDevice(deviceUid);
		}
		return deleted;
	}

	@Override
	public boolean addDevice(String deviceUid, String modelUid, String serialNo, String subscriberUid) {
		Device device = IptvBInteractiveUtilV2.getInstance().createDevice(deviceUid, subscriberUid, modelUid, null);
		return iptvRestDao.addDevice(device);
	}
	


	@Override
	public Subscriber findSubscriber(String subscriberUid) {
		return iptvRestDao.findSubscriber(subscriberUid);
	}

	@Override
	public Device findDevice(String deviceUid) {
		Device device = iptvRestDao.findDevice(deviceUid);
		if(device != null && device.getSubscriber_id() != null){
			Subscriber found = iptvRestDao.findSubscriberById(device.getSubscriber_id());
			if(found != null){
				device.setSubscriber_uid(found.getUid());
			}
		}
		return device;
	}

	@Override
	public List<Device> findSubscriberDevices(String subscriberUid) {
		return iptvRestDao.findAllSubscriberDevices(subscriberUid);
	}

	@Override
	public boolean connectDeviceToSubscriber(String deviceUid, String modelUid, String subscriberUid) {
		boolean connected = false;
		Device deviceFound = findDevice(deviceUid);
		if(deviceFound != null){
			String currentSubscriberUid = deviceFound.getSubscriber_uid();
			if(currentSubscriberUid != null){
				if(currentSubscriberUid.equals(subscriberUid)){
					log.info("Uredjaj je vec povezan na pravog subscribera");
					connected = true;
				}
			}else{
				if(subscriberUid == null){
					log.info("Uredjaj je vec oslobodjen.");
					connected = true;					
				}
			}
			if(!connected){
				boolean validSubscriber = true;
				if(subscriberUid != null){
					Subscriber existingSubscriber = findSubscriber(subscriberUid);
					if(existingSubscriber == null){
						validSubscriber = false;
					}
				}
				if(validSubscriber){
					Device device= IptvBInteractiveUtilV2.getInstance().createDevice(deviceUid, subscriberUid, modelUid, null);
					if(subscriberUid == null){
						device.setExceptAttributes(new String[] { "subscriber_id" });
					}
					connected = iptvRestDao.updateDevice(device);
				}
			}
		}
		return connected;
	}

	@Override
	public boolean updateSubscriberDevices(String subscriberUid, List<Device> devices) {
		boolean fixed = true;
		List<Device> foundOKDevices = new ArrayList<Device>();
		List<Device> existingDevices = iptvRestDao.findAllSubscriberDevices(subscriberUid);
		if(!Calculator.getInstance().isEmpty(existingDevices)){
			/*brisemo postojeci visak uredjaja*/
			for(Device existing : existingDevices){
				if(!Calculator.getInstance().isElementInList(existing, devices)){
					existing.setSubscriber_uid(null);
					existing.setExceptAttributes(new String[] { "subscriber_id" });
					boolean deviceDisconnected = iptvRestDao.updateDevice(existing);
					log.info("Rezultat brisanja uredjaja sa UID: "+ existing.getUid() + " i subscribera: "+ subscriberUid +": "+deviceDisconnected);
					fixed &= deviceDisconnected;
				}else{
					foundOKDevices.add(existing);
				}
			}
		}
		if(!Calculator.getInstance().isEmpty(devices)){
			/*povezujemo nove uredjaje*/
			for(Device newDevice : devices){
				if(!Calculator.getInstance().isElementInList(newDevice, foundOKDevices)){
					Device exists = findDevice(newDevice.getUid());
					if(exists == null){
						newDevice.setSubscriber_uid(subscriberUid);
						fixed &= iptvRestDao.addDevice(newDevice);
					}else{
						newDevice.setSubscriber_uid(subscriberUid);
						fixed &= iptvRestDao.updateDevice(newDevice);
						log.info("Rezultat operacije posle povezivanja uredjaja sa ");
					}
				}
			}
		}
		return fixed;
	}

	@Override
	public boolean updateSubscriberPin(String subscriberUid, String pin) {
		boolean updated = false;
		Subscriber subscriber = findSubscriber(subscriberUid);
		if(subscriber != null){
			Profile profile = iptvRestDao.findProfile(subscriberUid);
			if(profile != null){
				updated = iptvRestDao.deleteProfile(subscriberUid);
			}else{
				updated = true;
			}
			if (updated){
				Profile newProfile = IptvBInteractiveUtilV2.getInstance().createDefaultProfile(subscriber, pin);
				if(newProfile != null){
					updated = iptvRestDao.addProfile(newProfile);
				}
			}
		}
		return updated;
	}
	
	@Override
	public boolean updateSubscriberStatus(String subscriberUid, String status){
		boolean updated = false;
		Subscriber subscriber = findSubscriber(subscriberUid);
		if(subscriber != null && status != null && Calculator.getInstance().isElementInList(status, Arrays.asList(IptvBInteractiveUtilV2.SUBSCRIBER_STATUSES))){
			subscriber.setStatus(status);
			subscriber.setUpdated_at(new Date());
			updated = iptvRestDao.updateSubscriber(subscriber);
		}
		return updated;
	}
	
	private String findPackageNameById(Integer id){
		String packageName = packagesMap.get(id);
		if(packageName == null){
			Package pack = iptvRestDao.findPackageById(id);
			if(pack != null){
				packageName = pack.getUid();
				packagesMap.put(id, packageName);
			}
		}
		return packageName;
	}
	
	@Override
	public String findParentalPin(String subscriberUid){
		String parentalPin = null;
		Profile profile = iptvRestDao.findProfile(subscriberUid);
		if(profile != null){
			parentalPin = profile.getParental_pin();
		}
		return parentalPin;
	}

	@Override
	public int findDevicesCount(){
		int devicesCount = 3000;
		List<Device> devices = iptvRestDao.findAllDevices();
		if(!Calculator.getInstance().isEmpty(devices)){
			devicesCount = devices.size();
		}
		return devicesCount;
	}
	
	@Override
	public int addBeoTVToAll(){
		int added = 0;
		List<Subscriber> all = iptvRestDao.findAllSubscribers();
		IptvRestDaoV2 test = new IptvRestDaoImplV2("https://194.106.172.226:1443/api/", "beotel:badminpass33");
//		List<Subscriber> all = new ArrayList<Subscriber>();
//		Subscriber ja = iptvRestDao.findSubscriber("bojan.nedeljkovic");
//		all.add(ja);
		int max = 2000;
		for(Subscriber single : all){
			String subscriberUid = single.getUid();
			List<String> packages= findSubscriberPackages(subscriberUid);
			if(Calculator.getInstance().isElementInList("BeoTV Basic", packages) && !Calculator.getInstance().isElementInList("basictimeshift", packages)){
				SubscriberPackage subscriberPackage = IptvBInteractiveUtilV2.getInstance().createSubscriberPackage("basictimeshift", subscriberUid);
				boolean packageAdded = iptvRestDao.addSubscriberPackage(subscriberPackage);
				test.addSubscriberPackage(subscriberPackage);
				if(packageAdded){
					added++;
					log.info("Dodato paketa: "+added);
					if(added>max){
						break;
					}
				}
			}else{
				log.info("Subscriber "+subscriberUid+ " vec ima timeshift paket");
			}
		}
		return added;
	}
	
	@Override
	public List<Subscriber> findAllSubscribers(){
		return iptvRestDao.findAllSubscribers();
	}

	@Override
	public List<Package> getAllPackages() {
		return iptvRestDao.findAllPackages();
	}
}
