package iptv.rest.v2.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Operator extends NullAttributeModel implements Idable, Uidable{
	private String uid;

	private Integer id;

	private Date updated_at;

	private String description;

	private String name;

	private Date created_at;

	public String getUid() {
		return uid;
	}

	public Integer getId() {
		return id;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
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

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	@Override
	public String toString() {
		return "ClassPojo [uid = " + uid + ", id = " + id + ", updated_at = " + updated_at + ", description = "
				+ description + ", name = " + name + ", created_at = " + created_at + "]";
	}
}
