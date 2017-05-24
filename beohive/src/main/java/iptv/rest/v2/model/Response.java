package iptv.rest.v2.model;

import net.beotel.util.TextUtil;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Response {
	private String status;
	
	private ResponseDetail data;

	public String getStatus() {
		return status;
	}

	public ResponseDetail getData() {
		return data;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setData(ResponseDetail data) {
		this.data = data;
	}
	
	public boolean isStatusValid() {
		return status != null && status.toUpperCase().equals(TextUtil.SUCCESS_STRING_RESPONSE) && data != null;
	}
	
}
