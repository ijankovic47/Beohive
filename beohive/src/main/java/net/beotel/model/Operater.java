package net.beotel.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.acls.model.ObjectIdentityGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="OPERATER")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,
				  property = "id")
public class Operater implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy=GenerationType.AUTO, generator="PACKAGE_SEQ")
	@SequenceGenerator(name="PACKAGE_SEQ", sequenceName="PACKAGE_SEQ")
	private Integer id;
	
	@Size(max=20, min=2, message="{ime.size}") 
	@Pattern(regexp="[a-zA-Z]{0,20}", message="{ime.pattern}")
	@Column(name="IME")
	private String ime;
	
	@Size(max=30, min=2, message="{prezime.size}") 
	@Pattern(regexp="[a-zA-Z]{0,30}", message="{prezime.pattern}")
	@Column(name="PREZIME")
	private String prezime;
	
	@Size(max=20, min=2, message="{username.size}") 
	@Pattern(regexp="[a-zA-Z0-9_-]{0,20}", message="{username.pattern}")
	@Column(name="USERNAME")
	private String username;
	
	@Size(max=80, min=2, message="{password.size}") 
	@Column(name="PASSWORD")
	private String password;
	
	@NotEmpty(message="{email.empty}") @Email(message="{email.pattern}")
	@Column(name="EMAIL")
	private String email;
	
	@Column(name="ACTIVE")
	private byte active;
	
	@Transient
	private String role;
	
	@NotNull(message="Ne sme biti prazan")
	@Valid
	@ManyToOne(fetch=FetchType.EAGER)
	private Partner partner;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
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
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public byte getActive() {
		return active;
	}
	public void setActive(byte active) {
		this.active = active;
	}
	
	public Partner getPartner() {
		return partner;
	}
	public void setPartner(Partner partner) {
		this.partner = partner;
	}
	public String getRole() {
		if(getPartner().getName().equals("Beotel"))
			role = "Beotel";
		else
			role = "Operater";
		
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}		
	
	public Collection<GrantedAuthority> authorities() {
		List<GrantedAuthority> authority = new ArrayList<GrantedAuthority>(1);				
		if(getPartner().getName().equals("Beotel")){
			authority.add(new SimpleGrantedAuthority("ROLE_BEOTEL"));
		}else{
			authority.add(new SimpleGrantedAuthority("ROLE_OPERATER"));
		}
		return authority;
	}

	@Override
	public String toString() {
		String operater;
		if(getPartner() != null)
			operater = "Operater:{"+getIme()+", "+getPrezime()+", "+getEmail()+", "+getUsername()+", part:["+getPartner().getPrefix()+"]}";
		else
			operater = "Operater:{"+getIme()+", "+getPrezime()+", "+getEmail()+", "+getUsername()+"}";
		
		return operater;
	}
}