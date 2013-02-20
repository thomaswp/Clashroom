package com.clashroom.server.data;

import java.io.Serializable;


public class Dragon implements Serializable {
	private static final long serialVersionUID = 1L;
	
	public String name;
	
	public Dragon(String name) {
		this.name = name;
	}
}
