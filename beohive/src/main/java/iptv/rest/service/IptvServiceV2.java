package iptv.rest.service;

import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Subscriber;

import java.util.List;

/**
 * Interfejs za rad sa iptv sistemom, sadrzi potrebne metode
 * @author bojan
 *
 */
public interface IptvServiceV2 extends IptvService{
	
	/**
	 * Trazi subscribera po subscriberUid-u
	 * 
	 * @param subscriberUid
	 * @return
	 */
	public Subscriber findSubscriber(String subscriberUid);


	/**
	 * Trazi uredjaj po deviceUid-u (u sun-u je to MAC)
	 * 
	 * @param deviceUid
	 * @return
	 */
	public Device findDevice(String deviceUid);

	/**
	 * Nalazi sve device koji pripadaju subscriberu
	 * 
	 * @param subscriberUid
	 * @return
	 */
	public List<Device> findSubscriberDevices(String subscriberUid);
	
	/**
	 * Metoda za azuriranje uredjaja za subscribera
	 * @param subscriberUid
	 * @param devices
	 * @return
	 */
	public boolean updateSubscriberDevices(String subscriberUid, List<Device> devices);

	/**
	 * Metoda za dodavanje BeoInfo paketa svima - JEDNOKRATNO!!!
	 * @return
	 */
	int addBeoTVToAll();


	List<Subscriber> findAllSubscribers();


}
