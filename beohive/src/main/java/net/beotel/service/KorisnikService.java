package net.beotel.service;

import java.util.List;

import iptv.rest.v2.model.Package;
import net.beotel.model.Cenovnik;
import net.beotel.model.Korisnik;
import net.beotel.model.Operater;
import net.beotel.model.Partner;
import net.beotel.model.Uredjaj;

public interface KorisnikService {
	/** @author nemanja
	 * 	Dodaje novog korisnika sa forme i vraća njegov UID
	 * @param korisnik - Objekat Korisnik sa podacima pokupljenih sa forme
	 * */
	public String dodajNovogKorisnika(Korisnik korisnik);
	public void addDevicesToKorisnik(Uredjaj ur, Korisnik kor);
	public void convertToSubscriberAndSend(Korisnik korisnik);
	public boolean addPackagesToSubscriber(String naziviPaketa, String korisnikUid);
	List<Cenovnik> getListOfCenovnik(Partner partner);
	public String mergePackagesNames(String[] paketi);
	public List<Package> getAllPackagesFromPlatform();
	public void createInitialCreationLog(Korisnik korisnik, Operater opr);
	public String createUidWithFirstAndListName(String ime, String prezime, Partner partner);
	public String[] setDefaultImePrezimeIfEmpty(String ime, String prezime, Partner partner);
}
