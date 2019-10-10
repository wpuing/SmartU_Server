package com.jimi.su.server.entity;

public enum GSensor {

	B2("B2"), B25("B25"), B25E("B25E"), B4("B4"), RM1("RM1"), SC1("Sc1"), D23("D23");

	private String string;


	private GSensor(String string) {
		this.string = string;
	}


	public static Boolean isGSensor(String string) {
		for (GSensor gSensor : GSensor.values()) {
			if (string.equals(gSensor.string)) {
				return true;
			}
		}
		return false;
	}
}
