package net.beotel.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="CENOVNIK")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class Cenovnik {

	@Id
	@GeneratedValue (strategy=GenerationType.AUTO, generator="PACKAGE_SEQ")
	@SequenceGenerator(name="PACKAGE_SEQ", sequenceName="PACKAGE_SEQ")
	@Column(name="ID")
	private int id;
	
	@Column(name="PAKETI")
	private String paketi;
	
	@Column(name="TIP")
	private char tip;
	
	@Column(name="AKTIVAN")
	private int status;
	
	@Column(name="CENA")
	private double cena;
	
	@Column(name="DATUM_OD")
	private LocalDateTime datumOd;
	@Column(name="DATUM_DO")
	private LocalDateTime datumDo;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private Operater operater;
	
	public Operater getOperater() {
		return operater;
	}

	public void setOperater(Operater operater) {
		this.operater = operater;
	}

	@ManyToOne(fetch=FetchType.EAGER)
	private Partner partner;
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPaketi() {
		return paketi;
	}

	public void setPaketi(String paketi) {
		this.paketi = paketi;
	}

	public char getTip() {
		return tip;
	}

	public void setTip(char tip) {
		this.tip = tip;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public double getCena() {
		return cena;
	}

	public void setCena(double cena) {
		this.cena = cena;
	}

	public LocalDateTime getDatumOd() {
		return datumOd;
	}

	public void setDatumOd(LocalDateTime datumOd) {
		this.datumOd = datumOd;
	}

	public LocalDateTime getDatumDo() {
		return datumDo;
	}

	public void setDatumDo(LocalDateTime datumDo) {
		this.datumDo = datumDo;
	}

	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

public String toString(){
		
		return this.paketi+" cena:"+this.getCena()+" status:"+this.getStatus()+" tip:"+this.getTip();
	}
	
	
}
