package net.beotel.dao;

import java.util.List;

import net.beotel.model.Korisnik;
import net.beotel.model.KorisnikLog;
import net.beotel.model.Uredjaj;

public interface AdministracijaDao {

	public Korisnik findKorisnikByUredjaj(String snMac);
	public Korisnik findKorisnikByUid(String uid);
	public Korisnik getKorisnikById(int id);
	//public void insertKorPaketi(int korId, List<String> paketi);
	public void actDeact(int urId);
	public List<Uredjaj> getSlobodneUredjajePartnera(int parId);
	public void zameniUredjaj(int korId, int urId);
	public void packageAddRemove(int korId, String paket);
	public void stopKorisnik(int korId);
	public void saveKorisnikLog(KorisnikLog kl);
	public List<KorisnikLog> getKorisnikLog(int korId);
}
