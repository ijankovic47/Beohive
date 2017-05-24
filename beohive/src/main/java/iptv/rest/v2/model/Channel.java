package iptv.rest.v2.model;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Channel extends NullAttributeModel implements Idable, Uidable {
	private String uid;

	private Integer position;

	private URLMetadata url_metadata;

	private Integer region_id;

	private Integer epg_channel_id;

	private Boolean parental_hidden;

	private Boolean is_mcast;

	private Integer rec_duration;

	private Boolean promotable;

	private Integer id;

	private Integer ts_rec_duration;

	private Date updated_at;

	private Boolean is_ott;

	private String url_mcast;

	private Boolean hidden;

	private Boolean recordable;

	private String name;

	private Boolean timeshiftable;

	private Date created_at;

	private String url_ott;

	private String short_name;
	
	private Integer operator_id;
	
	private Boolean token_access;
	
	private String dvbt_tag;
	
	private String stream_priority;
	

	public String getUid() {
		return uid;
	}

	public Integer getPosition() {
		return position;
	}

	public URLMetadata getUrl_metadata() {
		return url_metadata;
	}

	public Integer getRegion_id() {
		return region_id;
	}

	public Integer getEpg_channel_id() {
		return epg_channel_id;
	}

	public Boolean getParental_hidden() {
		return parental_hidden;
	}

	public Boolean getIs_mcast() {
		return is_mcast;
	}

	public Integer getRec_duration() {
		return rec_duration;
	}

	public Boolean getPromotable() {
		return promotable;
	}

	public Integer getId() {
		return id;
	}

	public Integer getTs_rec_duration() {
		return ts_rec_duration;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public Boolean getIs_ott() {
		return is_ott;
	}

	public String getUrl_mcast() {
		return url_mcast;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public Boolean getRecordable() {
		return recordable;
	}

	public String getName() {
		return name;
	}

	public Boolean getTimeshiftable() {
		return timeshiftable;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public String getUrl_ott() {
		return url_ott;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setPosition(Integer position) {
		this.position = position;
	}

	public void setUrl_metadata(URLMetadata url_metadata) {
		this.url_metadata = url_metadata;
	}

	public void setRegion_id(Integer region_id) {
		this.region_id = region_id;
	}

	public void setEpg_channel_id(Integer epg_channel_id) {
		this.epg_channel_id = epg_channel_id;
	}

	public void setParental_hidden(Boolean parental_hidden) {
		this.parental_hidden = parental_hidden;
	}

	public void setIs_mcast(Boolean is_mcast) {
		this.is_mcast = is_mcast;
	}

	public void setRec_duration(Integer rec_duration) {
		this.rec_duration = rec_duration;
	}

	public void setPromotable(Boolean promotable) {
		this.promotable = promotable;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTs_rec_duration(Integer ts_rec_duration) {
		this.ts_rec_duration = ts_rec_duration;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public void setIs_ott(Boolean is_ott) {
		this.is_ott = is_ott;
	}

	public void setUrl_mcast(String url_mcast) {
		this.url_mcast = url_mcast;
	}

	public void setHidden(Boolean hidden) {
		this.hidden = hidden;
	}

	public void setRecordable(Boolean recordable) {
		this.recordable = recordable;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTimeshiftable(Boolean timeshiftable) {
		this.timeshiftable = timeshiftable;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public void setUrl_ott(String url_ott) {
		this.url_ott = url_ott;
	}
	
	public String getShort_name() {
		return short_name;
	}

	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}

	public Integer getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Integer operator_id) {
		this.operator_id = operator_id;
	}

	public Boolean getToken_access() {
		return token_access;
	}

	public void setToken_access(Boolean token_access) {
		this.token_access = token_access;
	}

	public String getDvbt_tag() {
		return dvbt_tag;
	}

	public void setDvbt_tag(String dvbt_tag) {
		this.dvbt_tag = dvbt_tag;
	}

	public String getStream_priority() {
		return stream_priority;
	}

	public void setStream_priority(String stream_priority) {
		this.stream_priority = stream_priority;
	}

	@Override
	public String toString() {
		return "ClassPojo [uid = " + uid + ", position = " + position + ", url_metadata = " + url_metadata
				+ ", region_id = " + region_id + ", epg_channel_id = " + epg_channel_id + ", parental_hidden = "+ parental_hidden 
				+ ", is_mcast = " + is_mcast + ", rec_duration = " + rec_duration + ", promotable = "+ promotable 
				+ ", operator_id = " + operator_id + ", id = " + id + ", ts_rec_duration = "+ ts_rec_duration 
				+ ", updated_at = " + updated_at + ", is_ott = " + is_ott + ", url_mcast = "+ url_mcast 
				+ ", token_access = " + token_access + ", dvbt_tag = " + dvbt_tag + ", stream_priority = "+ stream_priority 
				+ ", hidden = " + hidden + ", recordable = " + recordable +", name = " + name+ ", short_name = " + short_name
				+ ", timeshiftable = " + timeshiftable + ", created_at = " + created_at + ", url_ott = " + url_ott
				+ "]";
	}
}
