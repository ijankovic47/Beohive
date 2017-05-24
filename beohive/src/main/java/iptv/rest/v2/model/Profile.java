package iptv.rest.v2.model;

import java.util.Date;

import net.beotel.model.template.Idable;
import net.beotel.rest.NullAttributeModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Profile extends NullAttributeModel implements Idable, Uidable {

	private Integer id;

	private String parental_pin;

	private String uid;

	private String name;

	private String main_pin;

	private Integer parental_rating;

	private String subscriber_uid;

	private String login_name;

	private Boolean is_default;

	private String refresh_token;

	private String operator_uid;

	private Integer operator_id;

	private Integer subscriber_id;

	private Integer skin_id;

	private Date updated_at;

	private Date created_at;
	
	public String getParental_pin() {
		return parental_pin;
	}

	public String getUid() {
		return uid;
	}

	public String getMain_pin() {
		return main_pin;
	}

	public Integer getParental_rating() {
		return parental_rating;
	}

	public String getSubscriber_uid() {
		return subscriber_uid;
	}

	public String getLogin_name() {
		return login_name;
	}

	public Boolean getIs_default() {
		return is_default;
	}

	public String getRefresh_token() {
		return refresh_token;
	}

	public String getOperator_uid() {
		return operator_uid;
	}

	public void setParental_pin(String parental_pin) {
		this.parental_pin = parental_pin;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setMain_pin(String main_pin) {
		this.main_pin = main_pin;
	}

	public void setParental_rating(Integer parental_rating) {
		this.parental_rating = parental_rating;
	}

	public void setSubscriber_uid(String subscriber_uid) {
		this.subscriber_uid = subscriber_uid;
	}

	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}

	public void setIs_default(Boolean is_default) {
		this.is_default = is_default;
	}

	public void setRefresh_token(String refresh_token) {
		this.refresh_token = refresh_token;
	}

	public void setOperator_uid(String operator_uid) {
		this.operator_uid = operator_uid;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSubscriber_id() {
		return subscriber_id;
	}

	public void setSubscriber_id(Integer subscriber_id) {
		this.subscriber_id = subscriber_id;
	}

	public Integer getOperator_id() {
		return operator_id;
	}

	public void setOperator_id(Integer operator_id) {
		this.operator_id = operator_id;
	}

	public Integer getSkin_id() {
		return skin_id;
	}

	public void setSkin_id(Integer skin_id) {
		this.skin_id = skin_id;
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setUpdated_at(Date updated_at) {
		this.updated_at = updated_at;
	}

	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ClassPojo [parental_pin = " + parental_pin + ", uid = " + uid + ", name = " + name
				+ ", main_pin = " + main_pin + ", parental_rating = "
				+ parental_rating + ", subscriber_uid = " + subscriber_uid + ", login_name = " + login_name
				+ ", is_default = " + is_default + ", refresh_token = "
				+ refresh_token + ", operator_uid = " + operator_uid + "]";
	}
}
