package net.beotel.serviceImpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import iptv.rest.dao.IptvRestDaoImplV2;
import iptv.rest.dao.IptvRestDaoV2;
import iptv.rest.service.BInteractiveServiceImplV2;
import iptv.rest.service.IptvService;
import iptv.rest.service.IptvServiceV2;
import iptv.rest.util.IptvBInteractiveUtilV2;
import iptv.rest.v2.model.Device;
import iptv.rest.v2.model.Subscriber;
import iptv.rest.v2.model.SubscriberPackage;
import net.beotel.dao.AdministracijaDao;
import net.beotel.dao.OperatorDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Korisnik;
import net.beotel.model.KorisnikLog;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;
import net.beotel.service.AdministracijaService;
import net.beotel.service.CenovnikService;
import net.beotel.service.KorisnikService;

@Service
public class AdministracijaServiceImpl implements AdministracijaService {

	@Autowired
	AdministracijaDao administracijaDao;

	@Autowired
	private CenovnikService cenovnikService;

	@Autowired
	private OperatorDao operatorDao;

	private IptvRestDaoV2 iptvRestDaoV2 = new IptvRestDaoImplV2();
	private IptvService iptvService = new BInteractiveServiceImplV2(iptvRestDaoV2);
	private BInteractiveServiceImplV2 bis = new BInteractiveServiceImplV2(iptvRestDaoV2);
	private IptvBInteractiveUtilV2 iptvUtil = IptvBInteractiveUtilV2.getInstance();

	@Autowired
	private KorisnikService ks;

	@Override
	public String[] getOsnovniPaket(Partner p) {

		String[] op = new String[0];
		for (Cenovnik c : p.getCenovnici()) {

			if (c.getStatus() == 1 && c.getTip() == 'o') {
				op = c.getPaketi().split("#");
				break;
			}
		}
		return op;
	}

	@Override
	public List<String> getDoplatniPaketi(Partner p) {

		List<String> doplatniPaketi = new ArrayList<>();

		for (Cenovnik c : p.getCenovnici()) {

			if (c.getStatus() == 1 && (c.getTip() == 'd' || c.getTip() == 'p')) {
				doplatniPaketi.add(c.getPaketi());
			}
		}

		return doplatniPaketi;
	}

	public boolean proveriSinhronizaciju(int korId) {

		Korisnik k = administracijaDao.getKorisnikById(korId);

		Subscriber ps = iptvRestDaoV2.findSubscriber(k.getUid());
		if (ps == null) {
			System.out.println("KORISNIK NE POSTOJI NA PLATFORMI");
			return false;
		}
		if (!ps.getStatus().equals(k.getStatus())) {
			System.out.println("STATUS KORISNIKA NIJE SINHRONIZOVAN");
			return false;
		}

		Uredjaj u = k.getUredjaj();
		List<Device> devices = iptvRestDaoV2.findAllSubscriberDevices(k.getUid());
		if (devices.size() == 0 && u!=null) {
			System.out.println("KORISNIK NEMA UREDJAJ NA PLATFORMI");
			return false;
		}
		
		
		if(u==null&&devices.size()>0){
			
			System.out.println("KORISNIK JE STOPIRAN");
			return false;
		}
		if(!devices.isEmpty()){
		if (!devices.get(0).getUid().equals(u.getMacAdresa())) {

			System.out.println("MAC ADRESA JE RAZLICITA");
			return false;
		}}
		List<SubscriberPackage> subscriberPackages = iptvRestDaoV2.findAllSubscriberPackages(ps.getUid());
		List<String> paketiSubscriber = new ArrayList<>();
		List<String> paketiKorisnika = new ArrayList<>();
		for (SubscriberPackage sp : subscriberPackages) {

			iptv.rest.v2.model.Package p = iptvRestDaoV2.findPackageById(sp.getPackage_id());
			paketiSubscriber.add(p.getUid());
		}
		if (k.getPaketi() != null) {
			String[] paketiKorisnik = k.getPaketi().split("#");
			Map<String, String> mapaPaketa = cenovnikService.mapaPaketa();

			for (String pom : paketiKorisnik) {
				paketiKorisnika.add(mapaPaketa.get(pom));
			}
		}
		if (paketiKorisnika.size() != paketiSubscriber.size()) {
			System.out.println("PAKETI NISU ISTI");
			
			return false;
		}
		if (paketiKorisnika.size() > 0 && paketiSubscriber.size() > 0) {

			for (String pakK : paketiKorisnika) {
				if (paketiSubscriber.contains(pakK)) {
					continue;
				} else {
					System.out.println("PAKETI NISU ISTI");
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void sinhronizuj(int korId) {

		Korisnik k = administracijaDao.getKorisnikById(korId);

		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Operater operater = operatorDao.getOperatorByUsername(user.getUsername(), "username");

		Subscriber ps = iptvRestDaoV2.findSubscriber(k.getUid());
		if (ps == null) {

			dodajSubscribera(k, operater);

			// u slucaju da nema korisnika na platformi
		}
		if (ps != null && !ps.getStatus().equals(k.getStatus())) {

			izjednaciStatuseKorisnika(k, operater, ps);

		}

		Uredjaj u = k.getUredjaj();
		List<Device> devices = iptvRestDaoV2.findAllSubscriberDevices(k.getUid());
		
		if(u==null){
			if(devices.size()>0){
				iptvRestDaoV2.deleteDevice(devices.get(0).getUid());
			}
		}
		else{
		if (devices.size() > 0 && !devices.get(0).getUid().equals(u.getMacAdresa())) {

			iptvRestDaoV2.deleteDevice(devices.get(0).getUid());

			bis.addDevice(u.getMacAdresa(), u.getModel().getImeNaPlatformi(), u.getSn(), k.getUid());

			KorisnikLog kl = new KorisnikLog();
			kl.setDatum(new Date());
			kl.setKorisnik(k);
			kl.setOperater(operater);
			kl.setTip("sinhronizacija uredjaja korisnika");
			kl.setPre(devices.get(0).getUid());
			kl.setPosle(u.getMacAdresa());

			administracijaDao.saveKorisnikLog(kl);

			// sinhronizacija uredjaja
		} else if (devices.isEmpty()) {

			bis.addDevice(u.getMacAdresa(), u.getModel().getImeNaPlatformi(), u.getSn(), k.getUid());

			KorisnikLog kl = new KorisnikLog();
			kl.setDatum(new Date());
			kl.setKorisnik(k);
			kl.setOperater(operater);
			kl.setTip("sinhronizacija uredjaja korisnika");
			kl.setPre("Bez uredjaja");
			kl.setPosle(u.getMacAdresa());

			administracijaDao.saveKorisnikLog(kl);
		}
		}
		if (!isPaketiSinhro(k, ps)) {
			
			sinhronizujPakete(k,operater);
			
		}
	}

	private void dodajSubscribera(Korisnik k, Operater o) {

		Subscriber sub = iptvUtil.createSubscriber(k.getUid(), k.getIme(), k.getPrezime(), "", k.getStatus());
		iptvRestDaoV2.addSubscriber(sub);

		KorisnikLog kl = new KorisnikLog();
		kl.setDatum(new Date());
		kl.setKorisnik(k);
		kl.setTip("sinhronizacija sa platformom");
		kl.setOperater(o);

		kl.setPre("Korisnik " + k.getUid() + " nije registrovan na platformi");
		kl.setPre("Korisnik " + k.getUid() + " je registrovan na platformi");

		administracijaDao.saveKorisnikLog(kl);
	}

	private void izjednaciStatuseKorisnika(Korisnik k, Operater operater, Subscriber ps) {

		KorisnikLog kl = new KorisnikLog();
		kl.setDatum(new Date());
		kl.setKorisnik(k);
		kl.setTip("sinhronizacija statusa korisnika");
		kl.setOperater(operater);
		kl.setPre(ps.getStatus());
		kl.setPosle(k.getStatus());

		administracijaDao.saveKorisnikLog(kl);

		ps.setStatus(k.getStatus());
		iptvRestDaoV2.updateSubscriber(ps); // sinhronizacija statusa korisnika
	}

	private boolean isPaketiSinhro(Korisnik k, Subscriber ps) {

		List<SubscriberPackage> subscriberPackages = iptvRestDaoV2.findAllSubscriberPackages(ps.getUid());
		List<String> paketiSubscriber = new ArrayList<>();
		List<String> paketiKorisnika = new ArrayList<>();
		for (SubscriberPackage sp : subscriberPackages) {

			iptv.rest.v2.model.Package p = iptvRestDaoV2.findPackageById(sp.getPackage_id());
			paketiSubscriber.add(p.getUid());
		}
		if (k.getPaketi() != null) {
			String[] paketiKorisnik = k.getPaketi().split("#");
			Map<String, String> mapaPaketa = cenovnikService.mapaPaketa();

			for (String pom : paketiKorisnik) {
				paketiKorisnika.add(mapaPaketa.get(pom));
			}
		}
		if (paketiKorisnika.size() != paketiSubscriber.size()) {
			return false;
		}
		if (paketiKorisnika.size() > 0 && paketiSubscriber.size() > 0) {

			for (String pakK : paketiKorisnika) {
				if (paketiSubscriber.contains(pakK)) {
					continue;
				} else {
					return false;
				}
			}
		}

		return true;
	}
    private void sinhronizujPakete(Korisnik k, Operater o){
    	
    	Map<String, String> mapaPaketa = cenovnikService.mapaPaketa();
    	List<SubscriberPackage> sp=iptvRestDaoV2.findAllSubscriberPackages(k.getUid());
    	List<String> paketiSub=iptvService.findSubscriberPackages(k.getUid());
    	
    	String pre="";
    	
    	if(!paketiSub.isEmpty()){
    	for(String pom:paketiSub){
    	for(Entry e:mapaPaketa.entrySet()){
    		
    		if(e.getValue().equals(pom)){
    			pre+="#"+e.getKey();
    			break;
    		}
    	}
    	}
    	pre=pre.replaceFirst("#", "");
    	}
    	else{
    		pre="Nema paketa";
    	}
    	
    	bis.removeSubscriberPackages(k.getUid());

		if (k.getPaketi() != null) {
			String[] paketi = k.getPaketi().split("#");

			

			for (String pak : paketi) {

				String paketUid = mapaPaketa.get(pak);

				SubscriberPackage subscriberPackage = new SubscriberPackage();

				subscriberPackage = IptvBInteractiveUtilV2.getInstance().createSubscriberPackage(paketUid,
						k.getUid());
				iptvRestDaoV2.addSubscriberPackage(subscriberPackage);
				

			}
		}
		KorisnikLog kl=new KorisnikLog();
		kl.setDatum(new Date());
		kl.setOperater(o);
		kl.setTip("sinhronizacija paketa");
		kl.setKorisnik(k);
		kl.setPre(pre);
		if(k.getPaketi()!=null){
		kl.setPosle(k.getPaketi());
		}
		else{
			kl.setPosle("Nema paketa");
		}
		
		administracijaDao.saveKorisnikLog(kl);
    }

	@Override
	public Double getObracun(int korId) {
		
		Korisnik k=administracijaDao.getKorisnikById(korId);
        String[] op=getOsnovniPaket(k.getPartner());
          
        List<String> osnovniPaket=Arrays.asList(op);
        
		List<KorisnikLog> logovi=administracijaDao.getKorisnikLog(korId);   //logovi tipa 'promena paketa'
		
		List<KorisnikLog> logoviSorted=sortLogByDate(logovi);
		
		Map<String, Boolean> korisceniPaketi=new HashMap<>();     //mapa ce da sadrzi sve koriscene dop. pakete koriscene u toku meseca duze od 24h
		
		KorisnikLog kl=null;
		KorisnikLog ex=null;

		//List<KorisnikLog> vazeciLogovi=getVazeciLogovi(logoviSorted);
		
        for(int i=0;i<logoviSorted.size();i++){
			
			
			kl=logoviSorted.get(i);
			String[] klPaketi=kl.getPosle().split("#");
			String[] klPaketiEx=new String[0];
			if(i>0){
				klPaketiEx=logoviSorted.get(i-1).getPosle().split("#");
			}
			long hours=0;
			long diff=0;
		    
			if(i>0){
				diff = logoviSorted.get(i).getDatum().getTime() - logoviSorted.get(i-1).getDatum().getTime();
			    hours=TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
			}
			
			if(klPaketi.length<klPaketiEx.length && hours>=24){
				
				
			List<String> klPaketiPom=Arrays.asList(klPaketi);
			List<String> klPaketiExPom=Arrays.asList(klPaketiEx);
			
			List<String> pom=new ArrayList<>();
			
			for(String s:klPaketiExPom){
				
				if(!klPaketiPom.contains(s)){
					pom.add(s);
				}
			}
			for(String s:pom){
				korisceniPaketi.put(s, true);
			}
			}
			
			for(String s:klPaketi){
				
				if(!osnovniPaket.contains(s)){
					
					
					if(!korisceniPaketi.containsKey(s)){
						
						korisceniPaketi.put(s, false);
					}
					else{
						
						if(korisceniPaketi.get(s)==false && i>0){
							
							diff = logoviSorted.get(i).getDatum().getTime() - logoviSorted.get(i-1).getDatum().getTime();
						    hours=TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
							
							if(hours>=24){
								korisceniPaketi.put(s, true);
							}
						}
					}
				}
			}
			
		}
		
		for(Entry e:korisceniPaketi.entrySet()){
			System.out.println(e.getKey()+" "+e.getValue());
		}
		
		return null;
	}
	
	private List<KorisnikLog> sortLogByDate(List<KorisnikLog> kl){
		
		boolean a=true;
		KorisnikLog pom=null;
		while(a){
			
			a=false;
			for(int i=0;i<kl.size()-1;i++){
				
				if(kl.get(i).getDatum().after(kl.get(i+1).getDatum())){
					pom=kl.get(i);
					kl.set(1, kl.get(i+1));
					kl.set(i+1, pom);
					a=true;
					break;
				}
			}
		}
		return kl;
	}
	
	private List<KorisnikLog> getVazeciLogovi(List<KorisnikLog> logovi){
		
		List<KorisnikLog> kl=new ArrayList<>();
		kl.add(logovi.get(0));
		for(int i=1;i<logovi.size();i++){
			
			long diff = logovi.get(i-1).getDatum().getTime() - logovi.get(i).getDatum().getTime();
		    long hours=TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
			if(hours>=24){
				
				kl.add(logovi.get(i));
			}
		}
		return kl;
	}
	
}


