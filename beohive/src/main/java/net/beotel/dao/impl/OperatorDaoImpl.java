package net.beotel.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Repository;

import net.beotel.dao.OperatorDao;
import net.beotel.model.Operater;

@Repository
public class OperatorDaoImpl implements OperatorDao{
	
	@Autowired
	private SessionFactory sessionFactory;
	private List<Operater> operateri;
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Operater> getAllOperators() {
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "FROM Operater ORDER BY partner.name ASC, username ASC";		
		operateri = new ArrayList<Operater>();
		operateri = (List<Operater>) session.createQuery(HQL).list();
		return operateri;
	}

	@Override
	@Transactional
	public void addNewOperator(Operater operater) {
		Session session = sessionFactory.getCurrentSession();
		session.save(operater);
	}
	
	@Override
	@Transactional
	public Operater getOperatorById(int id) {
		Session session = sessionFactory.getCurrentSession();
		Operater operater = (Operater) session.createQuery("FROM Operater WHERE id =:id")
									.setInteger("id", id)
									.setMaxResults(1)
									.uniqueResult();
				
		return operater;
	}

	@Override
	@Transactional
	public void updateOperator(Operater updOperater) {
		Session session = sessionFactory.getCurrentSession();
		session.update(updOperater);
	}	
	
	@Override
	@Transactional
	public void deleteOperator(int id) throws HibernateException{
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "DELETE FROM Operater WHERE id =:id";
		int deleted = session.createQuery(HQL).setInteger("id", id).executeUpdate();
		System.out.println("Obrisan: "+deleted+" operatera.");		
	}
	
	@Override
	@Transactional
	public Operater getOperatorByUsername(String value, String field) {
		System.out.println(value+" "+field);
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "FROM Operater WHERE "+field+"=:value";
		Operater opr = (Operater) session.createQuery(HQL).setString("value", value)
												.setMaxResults(1)
											.uniqueResult();
		return opr;
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Operater> getAllOperatersForPartner(int partId) {
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "FROM Operater WHERE partner.id = :partId";
		operateri = (List<Operater>) session.createQuery(HQL).setInteger("partId", partId).list();
		return operateri;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Operater> getAllOperatersByPartnerPrefix(String prefix) {
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "FROM Operater WHERE partner.prefix = :prefix ORDER BY username ASC";
		operateri = (List<Operater>) session.createQuery(HQL).setString("prefix", prefix).list();
		return operateri;
	}
	
	@Override
	@Transactional
	public void changePasswordForOpr(int id, String password) {
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "UPDATE Operater SET password =:password WHERE id =:id";
		final String encPass = new Md5PasswordEncoder().encodePassword(password, null);
		
		int status = session.createQuery(HQL).setString("password", encPass).setInteger("id", id).executeUpdate();
		if(status == 0)
			throw new HibernateException("Neuspešno promenjena lozinka!");
	}
}
