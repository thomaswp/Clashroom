package com.clashroom.shared;

import java.io.Serializable;

public class Battler implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public String name;
	
	@Override
	public String toString() {
		return name;
	}
}
