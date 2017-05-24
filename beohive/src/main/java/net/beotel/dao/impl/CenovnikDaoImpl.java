package net.beotel.dao.impl;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.beotel.dao.CenovnikDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Partner;

@Repository
public class CenovnikDaoImpl implements CenovnikDao{

	@Autowired
	private SessionFactory sessionFactory;
	Query q;
	
	@Override
	@Transactional
	public void insertCenovnik(int partnerId, List<Cenovnik> cenovnici) {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Partner where id='"+partnerId+"'").setMaxResults(1);
		Partner p=(Partner) q.uniqueResult();
		
		List<Cenovnik> neMenjati=new ArrayList<>();
		
		//int size=cenovnici.size();       //haha :D
		                            
		List<Cenovnik> pom=new ArrayList<>(cenovnici);    //mora new, ista referenca je bila !!!
		
		for(int i=0;i<cenovnici.size();i++){
			Cenovnik nc=cenovnici.get(i);
			
			for(int j=0;j<p.getCenovnici().size();j++){
				Cenovnik pc=p.getCenovnici().get(j);
				if(pc.getStatus()==1 && pc.getTip()==nc.getTip() && pc.getPaketi().equals(nc.getPaketi()) && pc.getCena()==nc.getCena()){
					pom.remove(nc);
					neMenjati.add(pc);
			
				}
			}
		}
	
		for(Cenovnik c:p.getCenovnici()){
			if(neMenjati.contains(c)|| c.getStatus()==0){
				continue;
			}
			q=s.createQuery("from Cenovnik where id='"+c.getId()+"'").setMaxResults(1);
			Cenovnik cen=(Cenovnik) q.uniqueResult();
			cen.setStatus(0);
			cen.setDatumDo(LocalDateTime.now());
			s.update(cen);
		}
		
		for(Cenovnik c:pom){
			
			c.setPartner(p);
			s.save(c);
		}
	}
	/**
	 * Metoda koja vraća listu cenovnika koji pripadaju zadatom partneru
	 * @author nemanja	
	 * @param parner ID
	 * @return lista cenovnika
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Cenovnik> getListOfCenovnikForPartner(int id) {
		Session session = sessionFactory.getCurrentSession();
		List<Cenovnik> cenovnici = new ArrayList<Cenovnik>();
		final String HQL = "FROM Cenovnik WHERE partner.id=:id AND status=1 ORDER BY tip ASC";
		cenovnici = session.createQuery(HQL).setInteger("id", id).list();
		
		return cenovnici;
	}
}
