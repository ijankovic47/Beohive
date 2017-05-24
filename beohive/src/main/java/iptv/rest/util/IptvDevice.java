package iptv.rest.util;

public class IptvDevice {
	
	private String uid;
	
	private String serialNo;
	
	private String modelUid;
	
	private String typeUid;
	
	public IptvDevice(){
	}
	
	public IptvDevice(String uid, String serialNo, String modelUid, String typeUid){
		this.uid = uid;
		this.serialNo = serialNo;
		this.modelUid = modelUid;
		this.typeUid = typeUid;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}
	
	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public String getModelUid() {
		return modelUid;
	}

	public void setModelUid(String modelUid) {
		this.modelUid = modelUid;
	}

	public String getTypeUid() {
		return typeUid;
	}

	public void setTypeUid(String typeUid) {
		this.typeUid = typeUid;
	}
}
