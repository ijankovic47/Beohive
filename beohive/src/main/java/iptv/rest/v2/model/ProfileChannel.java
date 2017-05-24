package iptv.rest.v2.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProfileChannel extends NullAttributeModel implements Idable {
	private Integer id;

	private Integer position;

	private Boolean parental_hidden;

	private Boolean hidden;

	private Date created_at;

	private Date updated_at;

	private String profile_uid;

	private Integer profile_id;

	private String name;

	private String channel_uid;

	private Integer channel_id;

	public Integer getId() {
		return id;
	}

	public Integer getPosition() {
		return position;
	}

	public String getProfile_uid() {
		return profile_uid;
	}

	public Integer getProfile_id() {
		return profile_id;
	}

	public String getName() {
		return name;
	}

	public String getChannel_uid() {
		return channel_uid;
	}

	public Integer getChannel_id() {
		return channel_id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public void setProfile_uid(String profile_uid) {
		this.profile_uid = profile_uid;
	}

	public void setProfile_id(Integer profile_id) {
		this.profile_id = profile_id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setChannel_uid(String channel_uid) {
		this.channel_uid = channel_uid;
	}

	public void setChannel_id(Integer channel_id) {
		this.channel_id = channel_id;
	}

	public Boolean getParental_hidden() {
		return parental_hidden;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public void setParental_hidden(Boolean parental_hidden) {
		this.parental_hidden = parental_hidden;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	@Override
	public String toString() {
		return "ClassPojo [position = " + position + ", profile_uid = " + profile_uid + ", name = " + name
				+ ", channel_uid = " + channel_uid + "]";
	}
}
