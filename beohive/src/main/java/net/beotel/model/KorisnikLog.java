package net.beotel.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="KORISNIK_LOG")
public class KorisnikLog {

	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE, generator="PACKAGE_SEQ")
	@SequenceGenerator(name="PACKAGE_SEQ", sequenceName="PACKAGE_SEQ")
	@Column(name="ID")
	private int id;
	
	@Column(name="TIP_PROMENE")
	private String tip;
	
	@Column(name="STANJE_PRE")
	private String pre;
	
	@Column(name="STANJE_POSLE")
	private String posle;
	
	@Column(name="DATUM")
	private Date datum;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Korisnik korisnik;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Operater operater;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTip() {
		return tip;
	}
	public void setTip(String tip) {
		this.tip = tip;
	}
	public String getPre() {
		return pre;
	}
	public void setPre(String pre) {
		this.pre = pre;
	}
	public String getPosle() {
		return posle;
	}
	public void setPosle(String posle) {
		this.posle = posle;
	}
	public Date getDatum() {
		return datum;
	}
	public void setDatum(Date datum) {
		this.datum = datum;
	}
	public Korisnik getKorisnik() {
		return korisnik;
	}
	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
	public Operater getOperater() {
		return operater;
	}
	public void setOperater(Operater operater) {
		this.operater = operater;
	}
	
	public String toString(){
		
		return "id:"+this.id;
	}
	
}
