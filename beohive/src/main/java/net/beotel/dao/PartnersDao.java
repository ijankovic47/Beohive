package net.beotel.dao;

import java.util.HashMap;
import java.util.List;

import net.beotel.model.Cenovnik;
import net.beotel.model.Partner;

public interface PartnersDao {
	
	public int savePartner(Partner partner);   //upisuje prosledjeni objekat u bazu
	public List<Partner> getPartners();      //vraca listu svih partnera u bazi
	public Partner getPartner(int id);         //vraca odredjenog partnera na osnovu id-a
	public void editPartner(Partner partner); 
	
}
