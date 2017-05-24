package iptv.rest.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDetail {
	private Integer id;

	private Integer affectedRows;

	public Integer getId() {
		return id;
	}

	public Integer getAffectedRows() {
		return affectedRows;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setAffectedRows(Integer affectedRows) {
		this.affectedRows = affectedRows;
	}

}
