package com.clashroom.shared.dragons;

import com.clashroom.shared.data.DragonEntity;

public class DragonHatchling extends DragonClass {

	@Override
	public String getName() {
		return "Dragon Hatchling";
	}
	
	@Override
	public String getImageName() {
		return "hatchling-dragon.png";
	}

	@Override
	public String getDescription() {
		return "This dragon is fresh out of the egg, but don't be fooled by it's size. Dragon hatchlings " +
				"have powerful magical abilities!";
	}

	@Override
	public double getStrengthFactor() {
		return 0;
	}

	@Override
	public double getAgilityFactor() {
		return 0;
	}

	@Override
	public double getIntelligenceFactor() {
		return 0;
	}

	@Override
	public double getHpFactor() {
		return 0;
	}

	@Override
	public double getMpFactor() {
		return 0;
	}
}
