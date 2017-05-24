package iptv.rest.v2.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;



@JsonIgnoreProperties(ignoreUnknown = true)
public class Subscriber extends NullAttributeModel implements Idable, Uidable {
	private String uid;

	private Integer id;

	private String type;
	
	private String first_name;

	private String last_name;

	private String status;

	private String password;

	private Integer region_id;
	
	private String region_uid;

	private Date created_at;
	
	private Date updated_at;
	
	private Integer operator_id;
	
	
	public String getUid() {
		return uid;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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

	public Integer getRegion_id() {
		return region_id;
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

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
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

	public String getRegion_uid() {
		return region_uid;
	}

	public void setRegion_uid(String region_uid) {
		this.region_uid = region_uid;
	}
	
	public Integer getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Integer operator_id) {
		this.operator_id = operator_id;
	}

	@Override
	public String toString() {
		return "ClassPojo [uid = " + uid + ", id = " + id + ", first_name = " + first_name + ", type = "
				+ type + ", last_name = " + last_name + ", updated_at = " + updated_at
				+ ", status = " + status + ", password = " + password + ", region_id = "
				+ region_id + ", created_at = " + created_at +  ", operator_id = "
						+ operator_id + "]";
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
