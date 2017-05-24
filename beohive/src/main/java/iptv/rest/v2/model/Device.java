package iptv.rest.v2.model;

import java.util.Date;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends NullAttributeModel implements Idable, Uidable {
	private String uid;

	private Integer id;

	private Date updated_at;

	private Integer operator_id;

	private Date created_at;

	private Integer subscriber_id;
	
	private String subscriber_uid;

	private Integer device_type_id;
	
	private String device_type_uid;
	
	private String device_class;

	private DeviceMetadata metadata;
	
	public String getUid() {
		return uid;
	}
	
	public String extractFilteredUid(){
		if(getUid() != null && getUid().contains(":")){
			return getUid().replaceAll(":", "");
		}
		return getUid();
	}

	public Integer getId() {
		return id;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public Integer getOperator_id() {
		return operator_id;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public Integer getSubscriber_id() {
		return subscriber_id;
	}

	public Integer getDevice_type_id() {
		return device_type_id;
	}

	public DeviceMetadata getMetadata() {
		return metadata;
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

	public void setOperator_id(Integer operator_id) {
		this.operator_id = operator_id;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public void setSubscriber_id(Integer subscriber_id) {
		this.subscriber_id = subscriber_id;
	}

	public void setDevice_type_id(Integer device_type_id) {
		this.device_type_id = device_type_id;
	}

	public void setMetadata(DeviceMetadata metadata) {
		this.metadata = metadata;
	}
	
	public String getSubscriber_uid() {
		return subscriber_uid;
	}

	public void setSubscriber_uid(String subscriber_uid) {
		this.subscriber_uid = subscriber_uid;
	}

	public String getDevice_type_uid() {
		return device_type_uid;
	}

	public void setDevice_type_uid(String device_type_uid) {
		this.device_type_uid = device_type_uid;
	}

	public String getDevice_class() {
		return device_class;
	}

	public void setDevice_class(String device_class) {
		this.device_class = device_class;
	}

	@Override
	public String toString() {
		return "ClassPojo [uid = " + uid + ", id = " + id + ", updated_at = " + updated_at + ", operator_id = "
				+ operator_id + ", created_at = " + created_at + ", subscriber_id = " + subscriber_id+ ", subscriber_uid = " + subscriber_uid
				+ ", device_type_id = " + device_type_id + ", device_type_uid = " + device_type_uid + ", device_class = " + device_class + "" +
						", metadata = " + metadata + "]";
	}
	
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        Device that = (Device) o;
 
        if (getUid() != null ? !getUid().equals(that.getUid()) : that.getUid() != null) {
        	return false;
        }
 
        return true;
    }

}