package net.beotel.dao;

import net.beotel.model.Korisnik;

public interface KorisnikDao {

	public void dodajNovogKorisnika(Korisnik korisnik);
	public int chcekNoSubscribersWithSameUid(String uid);
	public Korisnik getkorisnikById(int id);
}