package net.beotel.dao.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.beotel.dao.ModelDao;
import net.beotel.model.ModelUr;
import net.beotel.model.Partner;

@Repository
public class ModelDaoImpl implements ModelDao {

	@Autowired
	private SessionFactory sessionFactory;
	private List<Partner> partners;
	Query q;
	
	@Override
	@Transactional
	public List<ModelUr> getModels() {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from ModelUr");
		
		List<ModelUr> modeli=q.list();
		
		return modeli;
	}

	@Override
	@Transactional
	public int insertModel(ModelUr model) throws HibernateException{
	
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from ModelUr where ime='"+model.getIme()+"'");
		if(q.list().size()>0){
			throw new HibernateException("Duplo ime modela");
		}
		q=s.createQuery("from ModelUr where IME_NA_PLATFORMI='"+model.getImeNaPlatformi()+"'");
		if(q.list().size()>0){
			throw new HibernateException("Duplo ime modela na platformi");
		}
		return (int) s.save(model);
		
	}

	@Override
	@Transactional
	public ModelUr getModel(int id) {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from ModelUr where id='"+id+"'").setMaxResults(1);
		
		ModelUr model=(ModelUr) q.uniqueResult();
		
		return model;
	}

	@Override
	@Transactional
	public void editModel(ModelUr model) throws HibernateException {
	
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from ModelUr where ime='"+model.getIme()+"' and id!='"+model.getId()+"'");
		if(q.list().size()>0){
			throw new HibernateException("Duplo ime modela");
		}
		q=s.createQuery("from ModelUr where IME_NA_PLATFORMI='"+model.getImeNaPlatformi()+"' and id!='"+model.getId()+"'");
		if(q.list().size()>0){
			throw new HibernateException("Duplo ime modela na platformi");
		}
		q=s.createQuery("from ModelUr where id='"+model.getId()+"'").setMaxResults(1);
		ModelUr mod=(ModelUr) q.uniqueResult();
		
		mod.setIme(model.getIme());
		mod.setImeNaPlatformi(model.getImeNaPlatformi());
		
		s.update(mod);

	}

	@Override
	@Transactional
	public void deleteModel(int id) throws HibernateException {
		
		Session s=sessionFactory.getCurrentSession();
		q=s.createQuery("from Uredjaj where MODEL_ID='"+id+"'").setMaxResults(1);
		if(!q.list().isEmpty()){
			throw new HibernateException("Model ne moze biti obrisan jer ima uredjaja tog modela u sistemu !");
		}
		q=s.createQuery("from ModelUr where id='"+id+"'").setMaxResults(1);
		ModelUr model=(ModelUr) q.uniqueResult();
		s.delete(model);
	}

}
