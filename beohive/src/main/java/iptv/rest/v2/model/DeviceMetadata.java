package iptv.rest.v2.model;

import net.beotel.rest.NullAttributeModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceMetadata extends NullAttributeModel{
	
	public DeviceMetadata() {
		super();
	}
	
	public DeviceMetadata(String input) {
		super();
	}

	private String os;

	private String uid;
	
	private String interfaces;

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(String interfaces) {
		this.interfaces = interfaces;
	}

	@Override
	public String toString() {
		return "ClassPojo [os = " + os + ", uid = " + uid +", interfaces = " + interfaces +"]";
	}
}
