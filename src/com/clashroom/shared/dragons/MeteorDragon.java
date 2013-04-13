package com.clashroom.shared.dragons;

import java.util.HashMap;

import com.clashroom.shared.data.DragonEntity;
import com.clashroom.shared.skills.Skill;

public class MeteorDragon extends DragonClass {

	@Override
	public String getName() {
		return "Meteor Dragon";
	}
	
	@Override
	public String getImageName() {
		return "meteor-dragon.png";
	}
	
	@Override
	public String getDescription() {
		return "This hulking dragon will pack quite a punch. It may not be the fastest or the smartest, " +
				"but it can smash and burn whatever it catches.";
	}

	@Override
	public double getStrengthFactor() {
		return 1.0;
	}

	@Override
	public double getAgilityFactor() {
		return 0.45;
	}

	@Override
	public double getIntelligenceFactor() {
		return 0.30;
	}

	@Override
	public double getHpFactor() {
		return 0.90;
	}

	@Override
	public double getMpFactor() {
		return 0.35;
	}

	@Override
	protected void fillSkillTree(HashMap<Skill, Integer> skillTree) {
		// TODO Auto-generated method stub
		
	}


}
