package net.beotel.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import iptv.rest.dao.IptvRestDaoImplV2;
import iptv.rest.dao.IptvRestDaoV2;
import iptv.rest.service.BInteractiveServiceImplV2;
import iptv.rest.service.IptvService;
import iptv.rest.util.IptvBInteractiveUtilV2;
import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Package;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;
import net.beotel.controllers.KorisnikController;
import net.beotel.dao.AdministracijaDao;
import net.beotel.dao.CenovnikDao;
import net.beotel.dao.KorisnikDao;
import net.beotel.dao.OperatorDao;
import net.beotel.dao.PartnersDao;
import net.beotel.dao.UredjajDao;
import net.beotel.dao.impl.OperatorDaoImpl;
import net.beotel.model.Cenovnik;
import net.beotel.model.Korisnik;
import net.beotel.model.KorisnikLog;
import net.beotel.model.Operater;
import net.beotel.model.OperaterDetails;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;

@Service
public class KorisnikServiceImpl implements KorisnikService{

	private static final Logger LOG = Logger.getLogger(KorisnikServiceImpl.class);
	@Autowired
	private KorisnikDao korisnikDaoImpl;
	@Autowired
	private PartnersDao partnersDaoImpl; 
	@Autowired
	private UredjajDao uredjajDaoImpl; 
	@Autowired
	private CenovnikDao cenovnikDaoImpl;
	@Autowired
	private OperatorDao operatorDaoImpl;
	@Autowired
	private AdministracijaDao administracijaDaoImpl;
	
	private IptvRestDaoV2 iptvRestDaoV2 = new IptvRestDaoImplV2();
	private IptvService iptvService = new BInteractiveServiceImplV2(iptvRestDaoV2);
	private IptvBInteractiveUtilV2 interactiveUtil;
		
	@Override
	public String dodajNovogKorisnika(Korisnik korisnik) {
		OperaterDetails opr = (OperaterDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Korisnik kor = new Korisnik();
		try{
			
			final int pid = opr.getPartner().getId();				// Parner ID
			final int did = korisnik.getUredjaj().getId();			// Device ID
			final String ime = korisnik.getIme(); 
			final String prezime = korisnik.getPrezime();
			final Partner partner = partnersDaoImpl.getPartner(pid);
			final Uredjaj uredjaj = uredjajDaoImpl.getUredjaj(did);
			final Operater operater = operatorDaoImpl.getOperatorById(opr.getId());
						
			final String[] imePrez = setDefaultImePrezimeIfEmpty(ime, prezime, partner); 
			final String uid = createUidWithFirstAndListName(imePrez[0], imePrez[1], partner);
						
			kor.setIme(imePrez[0]);
			kor.setPrezime(imePrez[1]);
			kor.setStatus(korisnik.getStatus());
			kor.setUid(uid);
			kor.setPartner(partner);
			kor.setUredjaj(uredjaj);
			kor.setCreated_at(new Date());
			kor.setUpdated_at(new Date());
			kor.setPaketi(korisnik.getPaketi());
			kor.setOperater(operater);			
			
			LOG.info("Korisnik sa UID: "+kor.getUid()+" uspesno kreiran.");
			
			korisnikDaoImpl.dodajNovogKorisnika(kor);			
			this.convertToSubscriberAndSend(kor);
			this.addDevicesToKorisnik(uredjaj, kor);					
			addPackagesToSubscriber(korisnik.getPaketi(), uid);			
			createInitialCreationLog(kor, operater);
			
			LOG.info("Korisnik "+uid+" uspesno kreiran i sa odabranim paketima sacuvan na Beohive i na platformu.");
			return uid;
		}catch (NullPointerException ex) {
			LOG.error("Neuspesno dodeljeni parametri za dodavanje novog korisnika. | Exc: "+ex.getMessage());
			return null;
		}catch(Exception ex){
			LOG.error("Neuspelo cuvanje korisnika i uredjaja za tog korisnika. Uzrok: "+ex.getMessage());
			return null;
		}
	}
	/*
	 * 	Konvertuje prosledjenog korisnika u Subscribera metodom createSubscriber()
	 * 	Šalje novog Subscribera na platformu preko REST metoda iz DAO
	 * */
	public void convertToSubscriberAndSend(Korisnik korisnik){
		final String uid = korisnik.getUid();
		boolean added = iptvService.addSubscriberWithDefaultProfile(uid, null, korisnik.getIme(), korisnik.getPrezime());
		if(added)
			LOG.info("Subscriber "+uid+" kreiran i poslat na platformu.");
	}
	/*
	 * 	Kreira prosledjeni uredjaj za proslednjenog korisika i salje taj uredja na servisni sloj REST API-a
	 * */
	public void addDevicesToKorisnik(Uredjaj ur, Korisnik kor){		
		final String deviceUid = ur.getMacAdresa();
		final String model = ur.getModel().getImeNaPlatformi();
		final String serialNo = ur.getSn();
		final String subscriberUid = kor.getUid();
			
		boolean added = iptvService.addDevice(deviceUid, model, serialNo, subscriberUid);
		if(added)
			LOG.info("Uredjaj uspesno dodat na platformu.");
	}	
	
	public boolean addPackagesToSubscriber(String naziviPaketa, String korisnikUid){
		interactiveUtil = IptvBInteractiveUtilV2.getInstance();		
		List<String> paketiUid = preparePackagesForSubscriber(naziviPaketa);
		try{
			for(String paketUid : paketiUid){
				SubscriberPackage sp = interactiveUtil.createSubscriberPackage(paketUid, korisnikUid);
				iptvRestDaoV2.addSubscriberPackage(sp);
			}
			return true;
		}catch (Exception e) {
			return false;
		}		
	}
	
	@Override
	public List<Cenovnik> getListOfCenovnik(Partner partner) {
		final int pid = partner.getId();
		List<Cenovnik> currCens = cenovnikDaoImpl.getListOfCenovnikForPartner(pid);
		return currCens;
	}
	@Override
	public String mergePackagesNames(String[] paketi) {
		StringJoiner joiner = new StringJoiner("#");
		for(int i=0; i< paketi.length; i++)
			joiner.add(paketi[i]);
		String joinedString = joiner.toString();
		return joinedString;
	}
	
	public List<String> preparePackagesForSubscriber(String pkgKorisnika){
		String[] paketi = pkgKorisnika.split("#");
		List<Package> pkgs = getAllPackagesFromPlatform();
		List<String> packageUids = new ArrayList<String>();		
		for(int i=0; i<paketi.length; i++){
			for(int j=0; j<pkgs.size(); j++){
				if(paketi[i].equals(pkgs.get(j).getName())){
					packageUids.add(i, pkgs.get(j).getUid());
					continue;
				}
			}			
		}		
		return packageUids;
	}
	/*
	 * 		Generise korisnikov UID od prefixa, imena i prezimena, plus ukoliko korisnik već postoji na uid dodaje broj ponavljanja
	 * */
	public String createUidWithFirstAndListName(String ime, String prezime, Partner partner){
		String prefix = partner.getPrefix();		
		String uid = (prefix+"_"+ime+"."+prezime).toLowerCase();
		
		if(prezime.equals(""))
			uid = uid.replace(".", ""); 
			
		final int suffixNo = checkHowMenySubscribersExsists(uid);
		if(suffixNo == 0){
			return uid;
		}else{
			return (uid+""+suffixNo).toLowerCase();
		}		
	}
		
	private int checkHowMenySubscribersExsists(String uid){
		return korisnikDaoImpl.chcekNoSubscribersWithSameUid(uid);
	}
	
	@Override
	public List<Package> getAllPackagesFromPlatform() {
		List<Package> paketi = iptvService.getAllPackages();
		return paketi;
	}	
	
	public String[] setDefaultImePrezimeIfEmpty(String ime, String prezime, Partner partner){
		final String imep = partner.getName();
		String[] imePrezime = new String[]{ime, prezime};			
		
		if((ime == null || ime.equals("")) || (prezime == null || prezime.equals("")))
			imePrezime[0] = imep;
				
		return imePrezime;	
	}
	
	@Override
	public void createInitialCreationLog(Korisnik korisnik, Operater opr) throws HibernateException{
		KorisnikLog log = new KorisnikLog();
//		LocalDateTime now = LocalDateTime.now();
		Date now = new Date();
		log.setDatum(now);
		log.setKorisnik(korisnik);
		log.setOperater(opr);
		log.setTip("promena paketa");
		log.setPre("Nema paketa");
		log.setPosle(korisnik.getPaketi());
		
		administracijaDaoImpl.saveKorisnikLog(log);
	}
}