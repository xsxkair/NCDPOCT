package com.xsx.ncd.define;

public enum DeviceTypeCodeEnum {
	NCD_YGFXY("NCD_YGFXY"),
	NCD_XTY("NCD_XTY");
	
	private final String typeCode;

	private DeviceTypeCodeEnum(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getTypeCode() {
		return typeCode;
	};
	
	
}
