package net.beotel.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PartnerCenovnik {

	private int partnerId;
	
	private List<String> tipovi=new ArrayList<>();
	private List<String> cene=new ArrayList<>();
	private List<LocalDateTime> dopDate=new ArrayList<>();
	private String cenaOsn;
	private LocalDateTime osnDate;
	private List<String> kombinacije=new ArrayList<>(1024);
	private List<String> kombinacijeIme=new ArrayList<>(1024);
	private List<LocalDateTime> komDate=new ArrayList<>();
	
	public List<LocalDateTime> getDopDate() {
		return dopDate;
	}
	public void setDopDate(List<LocalDateTime> dopDate) {
		this.dopDate = dopDate;
	}
	public LocalDateTime getOsnDate() {
		return osnDate;
	}
	public void setOsnDate(LocalDateTime osnDate) {
		this.osnDate = osnDate;
	}
	public List<LocalDateTime> getKomDate() {
		return komDate;
	}
	public void setKomDate(List<LocalDateTime> komDate) {
		this.komDate = komDate;
	}
	public List<String> getKombinacijeIme() {
		return kombinacijeIme;
	}
	public void setKombinacijeIme(List<String> kombinacijeIme) {
		this.kombinacijeIme = kombinacijeIme;
	}
	public List<String> getTipovi() {
		return tipovi;
	}
	public void setTipovi(List<String> tipovi) {
		this.tipovi = tipovi;
	}
	public List<String> getCene() {
		return cene;
	}
	public void setCene(List<String> cene) {
		this.cene = cene;
	}
	public String getCenaOsn() {
		return cenaOsn;
	}
	public void setCenaOsn(String cenaOsn) {
		this.cenaOsn = cenaOsn;
	}
	public List<String> getKombinacije() {
		return kombinacije;
	}
	public void setKombinacije(List<String> kombinacije) {
		this.kombinacije = kombinacije;
	}
	public int getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(int partnerId) {
		this.partnerId = partnerId;
	}
	public String toString(){
		
		String ret="[";
  		for(String s:this.tipovi){
  			
  			ret+=","+s;
  		}
  		
  		ret=ret.replaceFirst(",", "");
  		ret+="]";
  		return ret;
	}

}
