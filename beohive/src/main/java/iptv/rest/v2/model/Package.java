package iptv.rest.v2.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Package extends NullAttributeModel implements Idable, Uidable {
	private String uid;

	private Integer id;

	private Date updated_at;

	private String name;

	private Integer region_id;

	private Date created_at;

	private String type;
	
	private String quota;
	
	private Integer service_id;
	
	private Integer operator_id;
	
	public String getUid() {
		return uid;
	}

	public Integer getId() {
		return id;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public String getName() {
		return name;
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

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getQuota() {
		return quota;
	}

	public void setQuota(String quota) {
		this.quota = quota;
	}

	public Integer getService_id() {
		return service_id;
	}

	public void setService_id(Integer service_id) {
		this.service_id = service_id;
	}

	public Integer getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Integer operator_id) {
		this.operator_id = operator_id;
	}

	@Override
	public String toString() {
		return "ClassPojo [uid = " + uid + ", id = " + id + ", updated_at = " + updated_at + ", name = " + name
				+ ", region_id = " + region_id + ", created_at = " + created_at + ", type = " + type 
				+ ", operator_id = " + operator_id + ", service_id = " + service_id + ", quota = " + quota +"]";
	}
}
