package com.clashroom.shared.dragons;

import com.clashroom.shared.data.DragonEntity;

public class SkyDragon extends DragonClass {

	@Override
	public String getName() {
		return "Sky Dragon";
	}
	
	@Override
	public String getImageName() {
		return "sky-dragon.png";
	}
	
	@Override
	public String getDescription() {
		return "This dragon in master of the sky and the wind element. It can fire " +
				"powerful gusts from its wings and mouth.";
	}

	@Override
	public double getStrengthFactor() {
		return 0.40;
	}

	@Override
	public double getAgilityFactor() {
		return 0.9;
	}

	@Override
	public double getIntelligenceFactor() {
		return 0.55;
	}

	@Override
	public double getHpFactor() {
		return 0.5;
	}

	@Override
	public double getMpFactor() {
		return 0.65;
	}


}
