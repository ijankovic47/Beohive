package net.beotel.dao;
import java.util.List;

import net.beotel.model.ModelUr;
import net.beotel.model.Uredjaj;;

public interface UredjajDao {

	public List<Uredjaj> getAll(); 
	public List<Uredjaj> dodajNoveUredjaje(Uredjaj uredjaj, List<String[]> snMac);
	public Uredjaj getUredjaj(int id);
	public void updateUredjaj(Uredjaj uredjaj);
	public void delUredjaj(int id);
	public List<Uredjaj> getUredjajByPartnerId(int id);
	public Uredjaj getUredjajBySn(String sn, int id);
	public Uredjaj getUredjajByMac(String mac, int id);
	public List<Uredjaj> getFilter(int partnerId, int modelId);
	public List<Uredjaj> getSlobodneUredjaje(int partnerId);
	/**
	 * 	Metoda uzima jedan uredjaj za prosledjene FK, model Id i Partner ID @author nemanja
	 *  @param mid - Model ID
	 *  @param pid - PArtner ID
	 * */
	public Uredjaj getDeviceByParnerAndModel(int mid, int pid);
	/**
	 * Uzima listu Modela iz tabele Uredjaj, ali samo po jedan model, za zadatog partnera
	 * @author nemanja
	 * 
	 * @param pid - Partner ID
	 */
	public List<ModelUr> getModelFromUredjajForPartner(int pid);
	/**
	 * 	Metoda uzima LISTU uredjaja za prosledjen Partner ID @author nemanja
	 *  @param pid - PArtner ID
	 * */
	public List<Uredjaj> getAllFreeDevicesByPartnerId(int pid);
}
