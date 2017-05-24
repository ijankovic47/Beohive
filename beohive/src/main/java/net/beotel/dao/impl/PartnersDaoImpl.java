package net.beotel.dao.impl;


import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.engine.transaction.jta.platform.internal.SynchronizationRegistryBasedSynchronizationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.beotel.dao.PartnersDao;
import net.beotel.model.Cenovnik;
import net.beotel.model.Operater;
import net.beotel.model.Partner;

@Repository
public class PartnersDaoImpl implements PartnersDao{

	@Autowired
	private SessionFactory sessionFactory;
	private List<Partner> partners;
	Query q;
			
	public PartnersDaoImpl(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;
	}

	@Override
	@Transactional
	public int savePartner(Partner partner) throws HibernateException{
		
		Session session = sessionFactory.getCurrentSession();     
		q=session.createQuery("from Partner where prefix='"+partner.getPrefix()+"'");
		
		if(q.list().size()>0){
			Partner p=(Partner) q.uniqueResult();
			System.out.println(p.getName());
			throw new HibernateException("1");
		}
		q=session.createQuery("from Partner where name='"+partner.getName()+"'");
		if(q.list().size()>0){
			throw new HibernateException("2");
		}
		return (int) session.save(partner);
		
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Partner> getPartners(){
		
		Session session = sessionFactory.getCurrentSession();
		
		partners = new ArrayList<Partner>();
		                                                                      //ucitavanje liste svih partnera
		
			partners = (List<Partner>) session.createQuery("FROM Partner").list();
	
		return partners;		
	}

	@Override
	@Transactional
	public Partner getPartner(int id) {
		
		Session s=sessionFactory.getCurrentSession();
	    q=s.createQuery("from Partner where id='"+id+"'").setMaxResults(1);
		Partner partner=(Partner) q.uniqueResult();                //ucitavanje odredjenog partnera pomocu id-a
		
		return partner;
		
	}

	@Override
	@Transactional
	public void editPartner(Partner partner) {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Partner where name='"+partner.getName()+"' and id!='"+partner.getId()+"'");
		if(q.list().size()>0){
			
			throw new HibernateException("Duplo ime partnera!");
		}
		q=s.createQuery("from Partner where id='"+partner.getId()+"'");
		
		Partner p=(Partner) q.list().get(0);
		                                                          //update partnera posle editovanja
		p.setName(partner.getName());
		p.setMaxNoOp(partner.getMaxNoOp());
		
		if(partner.getStatus()==0 && p.getStatus()==1){
			
			for(Operater o:p.getOperators()){
				
				o.setActive((byte) 0);
			}
		}
		
		p.setStatus(partner.getStatus());
		
		
		s.update(p);
		
	}

	
	


}
