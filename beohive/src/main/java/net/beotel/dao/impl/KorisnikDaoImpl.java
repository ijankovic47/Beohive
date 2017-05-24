package net.beotel.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import net.beotel.dao.KorisnikDao;
import net.beotel.model.Korisnik;

@Repository
public class KorisnikDaoImpl implements KorisnikDao{

	@Autowired
	private SessionFactory sessionFactory;

	@Override
	@Transactional
	public void dodajNovogKorisnika(Korisnik korisnik) {
		Session session = sessionFactory.getCurrentSession();
		session.save(korisnik);
	}
	
	@Override
	@Transactional
	public int chcekNoSubscribersWithSameUid(String uid) {
		Session session = sessionFactory.getCurrentSession();
//		final String HQL = "FROM Korisnik WHERE EXISTS (FROM Korisnik WHERE username LIKE :uid)";
		final String HQL = "SELECT COUNT(*) FROM Korisnik WHERE username LIKE :uid";
		
		Long count = (Long) session.createQuery(HQL).setString("uid", "%"+uid+"%").uniqueResult();
		
		System.out.println("Pronasao UID ("+uid+"): "+count);
				
		return count.intValue();
	}

	@Override
	@Transactional
	public Korisnik getkorisnikById(int id) {
		Session session = sessionFactory.getCurrentSession();
		final String HQL = "FROM Korisnik WHERE id=:id";
		Korisnik kor = (Korisnik) session.createQuery(HQL).setInteger("id", id).uniqueResult();
		return kor;
	}
	
}
