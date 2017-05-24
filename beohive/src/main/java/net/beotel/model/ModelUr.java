package net.beotel.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="MODEL")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class ModelUr {

	@Id
	@GeneratedValue (strategy=GenerationType.AUTO, generator="PACKAGE_SEQ")
	@SequenceGenerator(name="PACKAGE_SEQ", sequenceName="PACKAGE_SEQ")
	private int id;
	
	@Column(name="IME")
	@Size(min = 5, max = 20, message = "Ime mora imati izmedju 5 i 20 karaktera.")
	private String ime;

	@Column(name="IME_NA_PLATFORMI")
	@Size(min = 5, max = 20, message = "Ime na platformi mora imati izmedju 5 i 20 karaktera.")
	private String imeNaPlatformi;
	
	public String getImeNaPlatformi() {
		return imeNaPlatformi;
	}

	public void setImeNaPlatformi(String imeNaPlatformi) {
		this.imeNaPlatformi = imeNaPlatformi;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getIme() {
		return ime;
	}

	public void setIme(String ime) {
		this.ime = ime;
	}

	public String toString(){
		
		return this.ime;
	}
}
