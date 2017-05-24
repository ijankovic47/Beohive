/**
 * 
 */
package net.beotel.model;

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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


/**
 * @author nemanja
 *
 */
@Entity
@Table(name="UREDJAJ")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class Uredjaj {

	@Id
	@GeneratedValue (strategy=GenerationType.SEQUENCE, generator="PACKAGE_SEQ")
	@SequenceGenerator(name="PACKAGE_SEQ", sequenceName="PACKAGE_SEQ")
	@Column(name="ID")
	private Integer id;
	@Size(min = 5, max = 20, message = "Serijski broj mora imati izmedju 5 i 20 karaktera.")
	@Column(name="SERIJSKI_BR")
	private String sn;
	@Size(min = 5, max = 20, message = "MAC adresa mora imati izmedju 5 i 20 karaktera.")
	@Column(name="MAC_ADRESA")
	private String macAdresa;
	@Column(name="STATUS")
	private int status;

	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@NotNull(message="Model mora biti izabran.")
	@ManyToOne(fetch=FetchType.EAGER)
	private ModelUr model;
	
	@NotNull(message="Partner mora biti izabran.")
	@ManyToOne(fetch=FetchType.EAGER)
	private Partner partner;
	
	@OneToOne(fetch=FetchType.EAGER, mappedBy="uredjaj")
	private Korisnik korisnik;
	
	
	public Korisnik getKorisnik() {
		return korisnik;
	}
	public void setKorisnik(Korisnik korisnik) {
		this.korisnik = korisnik;
	}
	public void setModel(ModelUr model) {
		this.model = model;
	}
	public ModelUr getModel() {
		return model;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getMacAdresa() {
		return macAdresa;
	}
	public void setMacAdresa(String macAdresa) {
		this.macAdresa = macAdresa;
	}

	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	
	public String toString(){
		
		return "sn:"+this.sn+" mac:"+this.macAdresa+" model:"+this.model;
	}
}
