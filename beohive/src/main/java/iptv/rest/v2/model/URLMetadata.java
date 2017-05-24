package iptv.rest.v2.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import net.beotel.rest.NullAttributeModel;


@JsonIgnoreProperties(ignoreUnknown = true)
public class URLMetadata extends NullAttributeModel{
	
	public URLMetadata() {
		super();
	}
	
	public URLMetadata(String input) {
		super();
	}

	private String port;

	private String address;

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "ClassPojo [port = " + port + ", address = " + address +"]";
	}
}
