package iptv.rest.v2.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriberPackage extends NullAttributeModel implements Idable {
	private Integer id;

	private Date active_to;

	private Date updated_at;

	private Date created_at;

	private Date active_from;

	private Integer subscriber_id;
	
	private String subscriber_uid;

	private Integer package_id;
	
	private String package_uid;

	public Integer getId() {
		return id;
	}

	public Date getActive_to() {
		return active_to;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public Date getActive_from() {
		return active_from;
	}

	public Integer getSubscriber_id() {
		return subscriber_id;
	}

	public Integer getPackage_id() {
		return package_id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setActive_to(Date active_to) {
		this.active_to = active_to;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public void setActive_from(Date active_from) {
		this.active_from = active_from;
	}

	public void setSubscriber_id(Integer subscriber_id) {
		this.subscriber_id = subscriber_id;
	}

	public void setPackage_id(Integer package_id) {
		this.package_id = package_id;
	}

	public String getSubscriber_uid() {
		return subscriber_uid;
	}

	public void setSubscriber_uid(String subscriber_uid) {
		this.subscriber_uid = subscriber_uid;
	}

	public String getPackage_uid() {
		return package_uid;
	}

	public void setPackage_uid(String package_uid) {
		this.package_uid = package_uid;
	}

	@Override
	public String toString() {
		return "ClassPojo [id = " + id + ", active_to = " + active_to + ", updated_at = " + updated_at
				+ ", created_at = " + created_at + ", active_from = " + active_from + ", subscriber_id = "
				+ subscriber_id + ", package_id = " + package_id + ", subscriber_uid = "+ subscriber_uid + ", package_uid = " + package_uid + "]";
	}
}
