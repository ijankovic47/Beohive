package net.beotel.model;

import java.io.Serializable;
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
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;


@Entity
@Table(name="PARTNER")
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class,
property = "id")
public class Partner implements Serializable {

	@Id
	@GeneratedValue (strategy=GenerationType.AUTO, generator="PACKAGE_SEQ")
	@SequenceGenerator(name="PACKAGE_SEQ", sequenceName="PACKAGE_SEQ")
	private Integer id;
	
	@Column(name="PREFIX")
	@Size(min = 5, max = 20, message = "Prefiks mora imati izmedju 5 i 20 karaktera.")
	@Pattern(regexp = "^[a-zA-Z\\-]+$", message = "Prefiks moze sadrzati samo slova i '-'.")
	private String prefix;
	
	@Column(name="NAME")
	@Size(min = 5, max = 30, message = "Ime mora imati izmedju 5 i 30 karaktera.")
	@Pattern(regexp = "^[a-zA-Z\\-]+$", message = "Ime moze sadrzati samo slova i '-'.")
	private String name;
	
	@Column(name="MAX_ACTIVE_OPERATORS")
	private int maxNoOp;
	
	@Column(name="STATUS")
	private int status;
	
	@OneToMany(fetch=FetchType.EAGER,mappedBy="partner")
	private List<Operater> operators=new ArrayList<>();
	
	@OneToMany(mappedBy="partner", fetch=FetchType.EAGER)
	private List<Cenovnik> cenovnici=new ArrayList<>();
	
	public List<Cenovnik> getCenovnici() {
		return cenovnici;
	}
	public void setCenovnici(List<Cenovnik> cenovnici) {
		this.cenovnici = cenovnici;
	}
	/**@OneToMany(fetch=FetchType.EAGER, mappedBy="partner")
	private List<Uredjaj> uredjaji=new ArrayList<>();
	
	public List<Uredjaj> getUredjaji() {
		return uredjaji;
	}
	public void setUredjaji(List<Uredjaj> uredjaji) {
		this.uredjaji = uredjaji;
	}**/
	public List<Operater> getOperators() {
		return operators;
	}
	public void setOperators(List<Operater> operators) {
		this.operators = operators;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPrefix() {
		return prefix;
	}
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getMaxNoOp() {
		return maxNoOp;
	}
	public void setMaxNoOp(int maxNoOp) {
		this.maxNoOp = maxNoOp;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	public String toString(){
		
		return this.getPrefix();
	}
	
	
	
}
