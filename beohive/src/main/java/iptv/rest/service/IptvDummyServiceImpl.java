package iptv.rest.service;

import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Package;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;

import java.util.List;

public class IptvDummyServiceImpl implements IptvServiceV2{

	public IptvDummyServiceImpl(){
	}
	
	@Override
	public boolean addSubscriberWithDefaultProfile(String uid, String pin, String firstName, String lastName) {
		return true;
	}

	@Override
	public List<String> findSubscriberPackages(String subscriberUid) {
		return null;
	}

	@Override
	public boolean updateSubscriberPackages(String subscriberUid, List<String> subscriberPackages) {
		return true;
	}

	@Override
	public boolean removeSubscriberPackages(String subscriberUid) {
		return true;
	}

	@Override
	public boolean deleteSubscriber(String subscriberUid) {
		return true;
	}

	@Override
	public boolean deleteDevice(String deviceUid) {
		return true;
	}

	@Override
	public boolean addDevice(String deviceUid, String model, String serialNo, String subscriberUid) {
		return true;
	}


	@Override
	public boolean updateSubscriberPin(String subscriberUid, String pin) {
		return true;
	}

	@Override
	public boolean updateSubscriberStatus(String subscriberUid, String status) {
		return true;
	}

	@Override
	public String findParentalPin(String subscriberUid) {
		return null;
	}

	@Override
	public int findDevicesCount() {
		return 0;
	}

	@Override
	public Subscriber findSubscriber(String subscriberUid) {
		return null;
	}

	@Override
	public Device findDevice(String deviceUid) {
		return null;
	}

	@Override
	public List<Device> findSubscriberDevices(String subscriberUid) {
		return null;
	}

	@Override
	public boolean connectDeviceToSubscriber(String deviceUid, String modelUid, String subscriberUid) {
		return true;
	}

	@Override
	public boolean updateSubscriberDevices(String subscriberUid, List<Device> devices) {
		return true;
	}

	@Override
	public int addBeoTVToAll() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Subscriber> findAllSubscribers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Package> getAllPackages() {
		// TODO Auto-generated method stub
		return null;
	}

}
