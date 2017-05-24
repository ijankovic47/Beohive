package net.beotel.model;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscriber {
	private String uid;

	private Integer id;
	
	private String first_name;

	private String last_name;

	private String status;

	private String password;

	private Date created_at;
	
	private Date updated_at;
	
	private Partner partner;
	
	
	public String getUid() {
		return uid;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public String getStatus() {
		return status;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
	
	public Partner getPartner() {
		return partner;
	}

	public void setPartner(Partner partner) {
		this.partner = partner;
	}

	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Subscriber that = (Subscriber) o;
 
        if (getUid() != null ? !getUid().equals(that.getUid()) : that.getUid() != null) {
        	return false;
        }
 
        return true;
    }
}
