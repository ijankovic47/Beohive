package net.beotel.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="KORISNIK")
public class Korisnik {

	@Id
	@GeneratedValue (strategy=GenerationType.AUTO, generator="PACKAGE_SEQ")
	@SequenceGenerator(name="PACKAGE_SEQ", sequenceName="PACKAGE_SEQ")
	private Integer id;
	@Column(name="USERNAME")
	private String uid;
	@Column(name="IME")
	private String ime;
	@Column(name="PREZIME")
	private String prezime;
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATED")
	private Date created_at;
	@Column(name="UPDATED")
	private Date updated_at;	
	@Column(name="PAKETI")
	private String paketi;
	
	@OneToOne(fetch=FetchType.EAGER)
	private Uredjaj uredjaj;
	@ManyToOne(fetch=FetchType.EAGER)
	private Partner partner;
	@OneToOne
	private Operater operater;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getIme() {
		return ime;
	}
	public void setIme(String ime) {
		this.ime = ime;
	}
	public String getPrezime() {
		return prezime;
	}
	public void setPrezime(String prezime) {
		this.prezime = prezime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	public Date getUpdated_at() {
		return updated_at;
	}
	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	public Uredjaj getUredjaj() {
		return uredjaj;
	}
	public void setUredjaj(Uredjaj uredjaj) {
		this.uredjaj = uredjaj;
	}
	public String getPaketi() {
		return paketi;
	}
	public void setPaketi(String paketi) {
		this.paketi = paketi;
	}
	public Operater getOperater() {
		return operater;
	}
	public void setOperater(Operater operater) {
		this.operater = operater;
	}	
	
	public String toString(){
		
	    if(this.partner!=null){
	    	return this.ime+" "+this.prezime+"("+this.partner.getPrefix()+")";
	    }
	    else{
	    	return this.ime+" "+this.prezime;
	    }
		
	}
}