package net.beotel.dao;

import java.util.List;

import net.beotel.model.Cenovnik;

public interface CenovnikDao {

	public void insertCenovnik(int partnerId, List<Cenovnik> cenovnici);
	public List<Cenovnik> getListOfCenovnikForPartner(int id);
}
