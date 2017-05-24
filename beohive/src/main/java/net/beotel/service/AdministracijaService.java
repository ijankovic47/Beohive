package net.beotel.service;

import java.util.List;

import net.beotel.model.Partner;

public interface AdministracijaService {

	public String[] getOsnovniPaket(Partner p);
	public List<String> getDoplatniPaketi(Partner p);
	public void sinhronizuj(int korId);
	public boolean proveriSinhronizaciju(int korId);
	public Double getObracun(int korId);
	
}
