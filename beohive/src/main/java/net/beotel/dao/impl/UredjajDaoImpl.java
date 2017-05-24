/**
 * 
 */
package net.beotel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import iptv.rest.service.BInteractiveServiceImplV2;
import net.beotel.dao.UredjajDao;
import net.beotel.model.ModelUr;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;

/**
 * @author nemanja
 *
 */
@Repository
public class UredjajDaoImpl implements UredjajDao {
	private static Logger LOG = Logger.getLogger(UredjajDaoImpl.class);
	@Autowired
	private SessionFactory sessionFactory;
	
	Query q;
	
	@Transactional
	public List<Uredjaj> dodajNoveUredjaje(Uredjaj ur, List<String[]> snMac){
		
		Session session = sessionFactory.getCurrentSession();
	
		Uredjaj u;
		int count=1;
		List<Integer> indeksi=new ArrayList<>();
		for(String[] s:snMac){
			
			q=session.createQuery("from Uredjaj where serijski_br='"+s[0]+"'").setMaxResults(1);
			if(!q.list().isEmpty()){
				throw new HibernateException("s"+count);
			}
			q=session.createQuery("from Uredjaj where mac_adresa='"+s[1]+"'").setMaxResults(1);
			if(!q.list().isEmpty()){
				throw new HibernateException("m"+count);
			}
			
			
			u=new Uredjaj();
			
			q=session.createQuery("from ModelUr where id='"+ur.getModel().getId()+"'").setMaxResults(1);
			ModelUr m=(ModelUr) q.uniqueResult();
			
			u.setModel(m);
			
			q=session.createQuery("from Partner where id='"+ur.getPartner().getId()+"'").setMaxResults(1);
			Partner p=(Partner) q.uniqueResult();
			if(p!=null){
				u.setPartner(p);
			}
			else{
			u.setPartner(null);
			
			}
			u.setSn(s[0]);
			u.setMacAdresa(s[1]);
			u.setStatus(ur.getStatus());
			int i=(int) session.save(u);
			indeksi.add(i);
			count++;
		}	
		String sql="";
		for(int i:indeksi){
			sql+=",'"+i+"'";
		}
		sql=sql.replaceFirst(",", "");
		
		q=session.createQuery("from Uredjaj where id in ("+sql+")");
		List<Uredjaj> uredjaji=q.list();
		
		return uredjaji;
	}

	@Override
	@Transactional
	public List<Uredjaj> getAll() {
	   
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Uredjaj");
		List<Uredjaj> uredjaji=q.list();
		
		return uredjaji;
	}

	@Override
	@Transactional
	public Uredjaj getUredjaj(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Uredjaj where id='"+id+"'").setMaxResults(1);
		
		Uredjaj u=(Uredjaj) q.uniqueResult();
		
		return u;
	}

	@Override
	@Transactional
	public void updateUredjaj(Uredjaj ur) throws HibernateException {
		
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Uredjaj where serijski_br='"+ur.getSn()+"' and id!='"+ur.getId()+"'").setMaxResults(1);
		if(!q.list().isEmpty()){
			throw new HibernateException("1");
		}
		q=session.createQuery("from Uredjaj where mac_adresa='"+ur.getMacAdresa()+"' and id!='"+ur.getId()+"'").setMaxResults(1);
		if(!q.list().isEmpty()){
			throw new HibernateException("2");
		}
	    q=session.createQuery("from Uredjaj where id='"+ur.getId()+"'").setMaxResults(1);
	    Uredjaj u=(Uredjaj) q.uniqueResult();
	    
	    q=session.createQuery("from Partner where id='"+ur.getPartner().getId()+"'").setMaxResults(1);
	    Partner p=(Partner) q.uniqueResult();
	    u.setMacAdresa(ur.getMacAdresa());
	    u.setModel(ur.getModel());
	    u.setPartner(p);
	    u.setSn(ur.getSn());
	    u.setStatus(ur.getStatus());
	    session.update(u);
		
	}

	@Override
	@Transactional
	public void delUredjaj(int id) throws HibernateException {
		
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Korisnik where UREDJAJ_ID='"+id+"'").setMaxResults(1);
		if(!q.list().isEmpty()){
			
			throw new HibernateException("Ne mozete ukoniti uredjaj iz sistema jer je zauzet !");
		}
		q=session.createQuery("from Uredjaj where id='"+id+"'").setMaxResults(1);
		Uredjaj u=(Uredjaj) q.uniqueResult();
		session.delete(u);
		
	}

	@Override
	@Transactional
	public List<Uredjaj> getUredjajByPartnerId(int id) {
		
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Uredjaj where partner_id='"+id+"'");
		
		List<Uredjaj> uredjaji=q.list();
		
		return uredjaji;
	}

	@Override
	@Transactional
	public Uredjaj getUredjajBySn(String sn, int id) {
		
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Uredjaj where sn='"+sn+"' and partner_id='"+id+"'").setMaxResults(1);
		
		Uredjaj u=(Uredjaj) q.uniqueResult();
		
		return u;
		
		
	}

	@Override
	@Transactional
	public Uredjaj getUredjajByMac(String mac, int id) {
		
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Uredjaj where macAdresa='"+mac+"' and partner_id='"+id+"'").setMaxResults(1);
		
		Uredjaj u=(Uredjaj) q.uniqueResult();
		
		return u;
	}

	@Override
	@Transactional
	public List<Uredjaj> getFilter(int partnerId, int modelId) {
		
		List<Uredjaj> uredjaji=new ArrayList<Uredjaj>();
		
		Session session = sessionFactory.getCurrentSession();
		q=session.createQuery("from Uredjaj where "+ (partnerId > 0 ? "PARTNER_ID='"+partnerId+"' and " : "1=1 and ") 
				+ (modelId > 0 ? "MODEL_ID='"+modelId+"'" : "1=1")  );
		
		uredjaji=q.list();
		
		return uredjaji;
	}

	@Override
	@Transactional
	public List<Uredjaj> getSlobodneUredjaje(int partnerId) {
		
        List<Uredjaj> uredjaji=new ArrayList<Uredjaj>();
		
		Session session = sessionFactory.getCurrentSession();
		
		q=session.createQuery("from Uredjaj where PARTNER_ID='"+partnerId+"'");
		uredjaji=q.list();
		
		return uredjaji;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Uredjaj> getAllFreeDevicesByPartnerId(int pid){
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "FROM Uredjaj u WHERE u.partner.id =:pid AND u.id NOT IN(SELECT k.uredjaj.id FROM Korisnik k)";
		final String SQL = "SELECT * FROM UREDJAJ u WHERE u.PARTNER_ID =:pid AND NOT EXISTS(SELECT k.UREDJAJ_ID FROM KORISNIK k WHERE u.ID = k.UREDJAJ_ID)";
		
//		List<Uredjaj> freeDevs = (ArrayList<Uredjaj>) session.createQuery(HQL).setInteger("pid", pid).list();
		List<Uredjaj> freeDevs = session.createSQLQuery(SQL).addEntity(Uredjaj.class).setInteger("pid", pid).list();
					
		LOG.info("Uzimam listu "+freeDevs.size()+" slobodnih uredjaja iz baze. ");	
		return freeDevs;		
	}
	
	@Override
	@Transactional
	public Uredjaj getDeviceByParnerAndModel(int mid, int pid) {
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "FROM Uredjaj u WHERE u.partner.id =:pid AND u.model.id =:mid AND u.id NOT IN(SELECT k.uredjaj.id FROM Korisnik k)";
		final String SQL = "SELECT * FROM UREDJAJ u WHERE u.PARTNER_ID = "+pid+" AND u.MODEL_ID = "+mid+" AND NOT EXISTS (SELECT * FROM KORISNIK k WHERE  u.ID = k.UREDJAJ_ID)";
		
		Uredjaj dev = (Uredjaj) session.createQuery(HQL).setInteger("pid", pid).setInteger("mid", mid).setMaxResults(1).uniqueResult();
				
		if(dev != null){
			LOG.info("Pronadjen slobodan uredjaj za dodelu novom korisniku.");			
			return dev;
		} else
			throw new NullPointerException("Nije pronadjen slobodan uredjaj.");
			
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<ModelUr> getModelFromUredjajForPartner(int pid){
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "SELECT MIN(U.model) FROM Uredjaj U WHERE U.partner.id = :pid GROUP BY U.model";
		List<ModelUr> modeli = new ArrayList<>();
		modeli = session.createQuery(HQL).setInteger("pid", pid).list();
		return modeli;
	}
}
