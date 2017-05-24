package net.beotel.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.beotel.model.Cenovnik;
import net.beotel.model.Partner;
import net.beotel.model.PartnerCenovnik;

public interface CenovnikService {

	/**
	 * Vraca listu svih paketa na platformi
	 * 
	 *
	 * @return
	 */
	public List<String> listaPaketa();
	
	/**
	 * Pravi cenovnike za smestanje u bazu od podataka sakupljenih sa forme
	 * 
	 * @param pc
	 * 
	 */
	public void createCenovnik(PartnerCenovnik pc);
	
	/**
	 * Od cenovnika u bazi konvertuje u model pogodan za prikaz na formi
	 * 
	 * @param cenovnici
	 * @return
	 */
	public PartnerCenovnik getCenovnik(List<Cenovnik> cenovnici);
	
	/**
	 * Popunjava listu 'kombinacije' svim mogucim kombinacijama clanova niza 'array' 
	 * sa duzinom 'length'.
	 * 
	 * @param kombinacije,buffer,array,start,length
	 * 
	 */
	public void createKombinacije(List<String> kombinacije,Stack<String> buffer, String[] array, int start, int length);
	
	/**
	 * Smesta sve izabrane doplatne pakete u niz
	 * 
	 * @param pc
	 * @return
	 */
	public String[] getDoplata(PartnerCenovnik pc);
	
	/**
	 * Pronalazi sve neaktuelne cenovnike partnera na osnovu parametra tip.
	 * 
	 * 
	 * @param cenovnici, tip
	 * @return
	 */
	public List<List<String>> istorija(List<Cenovnik> cenovnici, char tip);
	
	public String[] osnovniPaket(PartnerCenovnik pc);
	public List<String[]> doplatniPaketi(PartnerCenovnik pc);
	public List<String[]> kombinacije(PartnerCenovnik pc);
	public Map<String,String> mapaPaketa();
}
