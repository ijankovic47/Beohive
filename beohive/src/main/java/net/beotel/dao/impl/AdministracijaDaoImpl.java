package net.beotel.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import iptv.rest.dao.IptvRestDaoImplV2;
import iptv.rest.dao.IptvRestDaoV2;
import iptv.rest.service.BInteractiveServiceImplV2;
import iptv.rest.service.IptvService;
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
import net.beotel.model.OperaterDetails;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;
import net.beotel.service.CenovnikService;

@Repository
public class AdministracijaDaoImpl implements AdministracijaDao {

	@Autowired
	private SessionFactory sessionFactory;
	Query q;
	
	@Autowired
	private OperatorDao operatorDao;
	
	@Autowired
	private CenovnikService cenovnikService;
	
	private IptvRestDaoV2 iptvRestDaoV2 = new IptvRestDaoImplV2();
	private BInteractiveServiceImplV2 bis=new BInteractiveServiceImplV2(iptvRestDaoV2);
	
	@Override
	@Transactional
	public Korisnik findKorisnikByUredjaj(String snMac) {
		
		Session s=sessionFactory.getCurrentSession();
		Uredjaj uredjaj=null;
	    Korisnik k=null;
	    int partnerId=getOperater().getPartner().getId();
	    System.out.println(partnerId);
	    if(partnerId!=1503){
	    	q=s.createQuery("from Uredjaj where SERIJSKI_BR='"+snMac+"' and PARTNER_ID='"+partnerId+"'").setMaxResults(1);
	    }
	    else{
		q=s.createQuery("from Uredjaj where SERIJSKI_BR='"+snMac+"'").setMaxResults(1);
	    }
		Uredjaj u=(Uredjaj) q.uniqueResult();
		
	    if(u!=null){
	    	uredjaj=u;
	    }
	    else{
	    	if(partnerId!=1503){
	    	q=s.createQuery("from Uredjaj where MAC_ADRESA='"+snMac+"' and PARTNER_ID='"+partnerId+"'").setMaxResults(1);
	    	}
	    	else{
	    		q=s.createQuery("from Uredjaj where MAC_ADRESA='"+snMac+"'").setMaxResults(1);
	    	}
		    u=(Uredjaj) q.uniqueResult();
		    uredjaj=u;
	    }
	    
		if(uredjaj!=null){
			q=s.createQuery("from Korisnik where UREDJAJ_ID='"+uredjaj.getId()+"'");
			k=(Korisnik) q.uniqueResult();
		}
		return k;
	}

	@Override
	@Transactional
	public Korisnik findKorisnikByUid(String uid) {
		
		int partnerId=getOperater().getPartner().getId();
		Session s=sessionFactory.getCurrentSession();
		if(partnerId!=1503){
			q=s.createQuery("from Korisnik where USERNAME='"+uid+"' and PARTNER_ID='"+partnerId+"'").setMaxResults(1);
		}
		else{
			q=s.createQuery("from Korisnik where USERNAME='"+uid+"'").setMaxResults(1);
		}
		
		Korisnik k=(Korisnik) q.uniqueResult();
		
		return k;
	}

	@Override
	@Transactional
	public Korisnik getKorisnikById(int id) {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Korisnik where id='"+id+"'").setMaxResults(1);
		
		Korisnik k=(Korisnik) q.uniqueResult();
		
		return k;
	}

	@Override
	@Transactional
	public void actDeact(int korId) {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Korisnik where id='"+korId+"'").setMaxResults(1);
		
		Korisnik k=(Korisnik) q.uniqueResult();
		
		KorisnikLog kl=new KorisnikLog();
		k.setUpdated_at(new Date());
		kl.setDatum(new Date());
		kl.setKorisnik(k);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Operater operater=operatorDao.getOperatorByUsername(user.getUsername(), "username");
		kl.setOperater(operater);
		kl.setOperater(operater);
		kl.setTip("promena statusa korisnika");
		
		if(k.getStatus().equals("ACTIVE")){
			k.setStatus("INACTIVE");
			
			k.setPaketi("");
			
			s.update(k);
			
			bis.removeSubscriberPackages(k.getUid());    //rest
			kl.setPre("ACTIVE");
			kl.setPosle("INACTIVE");
			
    		Subscriber subsc=iptvRestDaoV2.findSubscriber(k.getUid());
    		subsc.setStatus("INACTIVE");
    		iptvRestDaoV2.updateSubscriber(subsc);
		
		}
		else{
			
			k.setStatus("ACTIVE");
			String op=getOsnovniPaket(k.getPartner());
            k.setPaketi(op);
            
            s.update(k);          //kako je ovo radilo bez update ??
            
            String[] paketi=op.split("#");
            
            Map<String, String> mapaPaketa=cenovnikService.mapaPaketa();
            
            for(String pak:paketi){
           	 
           	 String paketUid=mapaPaketa.get(pak);
           	 
            SubscriberPackage subscriberPackage = new SubscriberPackage();
   			
    		subscriberPackage = IptvBInteractiveUtilV2.getInstance().createSubscriberPackage(paketUid, k.getUid());
    		iptvRestDaoV2.addSubscriberPackage(subscriberPackage);
    		
            }
            
    		Subscriber subsc=iptvRestDaoV2.findSubscriber(k.getUid());
    		subsc.setStatus("ACTIVE");
    		iptvRestDaoV2.updateSubscriber(subsc);
    		
    		kl.setPre("INACTIVE");
			kl.setPosle("ACTIVE");
		}
		s.save(kl);
		
	}

	@Override
	@Transactional
	public List<Uredjaj> getSlobodneUredjajePartnera(int parId) {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Uredjaj where PARTNER_ID='"+parId+"'");        
		List<Uredjaj> uredjaji=q.list();
		q=s.createQuery("from Korisnik where PARTNER_ID='"+parId+"'");        
		List<Korisnik> korisnici=q.list();
		List<Uredjaj> uredjajiPom=new ArrayList<>(uredjaji);
		
		for(Uredjaj u:uredjajiPom){
		  for(Korisnik k:korisnici){
			
			  if(k.getUredjaj()!=null){
				if(k.getUredjaj().equals(u)){
					uredjaji.remove(u);
					break;
				}
				
			}}
		}
		
		return uredjaji;
	}

	@Override
	@Transactional
	public void zameniUredjaj(int korId, int urId) {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Korisnik where id='"+korId+"'").setMaxResults(1);
		Korisnik k=(Korisnik) q.uniqueResult();
		q=s.createQuery("from Uredjaj where id='"+k.getUredjaj().getId()+"'").setMaxResults(1);
		Uredjaj korUr=(Uredjaj) q.uniqueResult();
		q=s.createQuery("from Uredjaj where id='"+urId+"'").setMaxResults(1);
		Uredjaj newUr=(Uredjaj) q.uniqueResult();
		
		newUr.setStatus(korUr.getStatus());
		korUr.setStatus(0);
		
		s.update(newUr);
		s.update(korUr);
		
		
		q=s.createQuery("from Uredjaj where id='"+newUr.getId()+"'").setMaxResults(1);
		Uredjaj u=(Uredjaj) q.uniqueResult();
	
		k.setUredjaj(u);
		k.setUpdated_at(new Date());
		
		s.update(k);          //nasa baza
		
		KorisnikLog kl=new KorisnikLog();
		kl.setDatum(new Date());
	
		kl.setKorisnik(k);
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		Operater operater=operatorDao.getOperatorByUsername(user.getUsername(), "username");
		
		kl.setOperater(operater);
		kl.setTip("zamena uredjaja");
		kl.setPre(korUr.getSn());              //log
		kl.setPosle(newUr.getSn());
		
		s.save(kl);
		
		List<Device> devices=iptvRestDaoV2.findAllSubscriberDevices(k.getUid());   //rest
		if(devices.size()>0){
		iptvRestDaoV2.deleteDevice(devices.get(0).getUid());
		}
		bis.addDevice(u.getMacAdresa(), u.getModel().getImeNaPlatformi(), u.getSn(), k.getUid());
		
	}

	@Override
	@Transactional
	public void packageAddRemove(int korId, String paket) {
		
		KorisnikLog kl=new KorisnikLog();
		kl.setTip("promena paketa");
		kl.setDatum(new Date());
		
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Korisnik where id='"+korId+"'").setMaxResults(1);
		
		Korisnik k=(Korisnik) q.uniqueResult();
		
		kl.setKorisnik(k);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
	    OperaterDetails od=(OperaterDetails) auth.getPrincipal();
	    
	    q=s.createQuery("from Operater where id='"+od.getId()+"'").setMaxResults(1);
	    
	    Operater op=(Operater) q.uniqueResult();
	    
	    kl.setOperater(op);
		
		String[] paketi=k.getPaketi().split("#");
		
		List<String> paketiPom=new ArrayList<>();
		for(String pak:paketi){
			paketiPom.add(pak);
		}
		
		if(paketiPom.contains(paket)){
			
			String pre="";
			String posle="";
			for(String pom:paketiPom){
				pre+="#"+pom;
			}
			pre=pre.replaceFirst("#", "");
			kl.setPre(pre);
			paketiPom.remove(paket);
			for(String pom:paketiPom){
				posle+="#"+pom;
			}
			posle=posle.replaceFirst("#", "");
			kl.setPosle(posle);
		}
		else{
			String pre="";
			String posle="";
			for(String pom:paketiPom){
				pre+="#"+pom;
			}
			pre=pre.replaceFirst("#", "");
			kl.setPre(pre);
			paketiPom.add(paket);
			for(String pom:paketiPom){
				posle+="#"+pom;
			}
			posle=posle.replaceFirst("#", "");
			kl.setPosle(posle);
		}
		String forSet="";
		for(String pak:paketiPom){
			forSet+="#"+pak;
		}
		forSet=forSet.replaceFirst("#", "");
		k.setPaketi(forSet);
		
		k.setUpdated_at(new Date());
		s.update(k);
		s.save(kl);
		
		Map<String, String> mapaPaketa=cenovnikService.mapaPaketa();
		
		List<SubscriberPackage> paketiSubscriber=iptvRestDaoV2.findAllSubscriberPackages(k.getUid());
		
		for(SubscriberPackage sp:paketiSubscriber){
			
			iptv.rest.v2.model.Package p=iptvRestDaoV2.findPackageById(sp.getPackage_id());
			if(p.getUid().equals(mapaPaketa.get(paket))){
				
				iptvRestDaoV2.deleteSubscriberPackage(sp.getId());
				return;
			}
		}

        	 
         SubscriberPackage subscriberPackage = new SubscriberPackage();
			
 		 subscriberPackage = IptvBInteractiveUtilV2.getInstance().createSubscriberPackage(mapaPaketa.get(paket), k.getUid());
 		
 		 iptvRestDaoV2.addSubscriberPackage(subscriberPackage);
		
}
	     private String getOsnovniPaket(Partner p){
	    	 
	    	 String op="";
	    	 for(Cenovnik c:p.getCenovnici()){
	    		 
	    		 if(c.getStatus()==1&&c.getTip()=='o'){
	    			 op=c.getPaketi();
	    			 break;
	    		 }
	    	 }
	    	 return op;
	     }

		@Override
		@Transactional
		public void stopKorisnik(int korId) {
			
			KorisnikLog kl=new KorisnikLog();
	
			Session s=sessionFactory.getCurrentSession();
			q=s.createQuery("from Korisnik where id='"+korId+"'").setMaxResults(1);
			Korisnik k=(Korisnik) q.uniqueResult();
			
			kl.setPre(k.getStatus());
			
			k.setStatus("SUSPENDED");
			k.setPaketi("");
			k.setUpdated_at(new Date());
			k.setUredjaj(null);
			
			s.update(k);
			
			kl.setKorisnik(k);
			kl.setDatum(new Date());
			kl.setTip("promena stanja korisnika");
			kl.setPosle("SUSPENDED");
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Operater operater=operatorDao.getOperatorByUsername(user.getUsername(), "username");
			
			kl.setOperater(operater);
			
			s.save(kl);
			
			Subscriber subsc=iptvRestDaoV2.findSubscriber(k.getUid());
    		subsc.setStatus("SUSPENDED");
    		iptvRestDaoV2.updateSubscriber(subsc);  //rest
    		List<Device> devices=iptvRestDaoV2.findAllSubscriberDevices(k.getUid());   //rest
    		if(devices.size()>0){
    		iptvRestDaoV2.deleteDevice(devices.get(0).getUid());
    		}
    		List<SubscriberPackage> sp=iptvRestDaoV2.findAllSubscriberPackages(k.getUid());
    		
    		for(SubscriberPackage spa:sp){
    			
    			iptvRestDaoV2.deleteSubscriberPackage(spa.getId());
    		}
		}

		@Override
		@Transactional
		public void saveKorisnikLog(KorisnikLog kl) {
		
			Session s=sessionFactory.getCurrentSession();
			s.save(kl);
			
		}

		@Override
		@Transactional
		public List<KorisnikLog> getKorisnikLog(int korId) {
			
			Session s=sessionFactory.getCurrentSession();
			q=s.createQuery("from KorisnikLog where KORISNIK_ID='"+korId+"' and TIP_PROMENE='promena paketa'");
			
			List<KorisnikLog> logovi=q.list();
			
			return logovi;
			
		}
		private Operater getOperater(){
			
			User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Operater operater=operatorDao.getOperatorByUsername(user.getUsername(), "username");
			
			return operater;
		}
}
