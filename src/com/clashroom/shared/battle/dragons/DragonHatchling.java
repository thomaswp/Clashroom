package com.clashroom.shared.battle.dragons;

import java.util.HashMap;

import com.clashroom.shared.battle.skills.EggShellSkill;
import com.clashroom.shared.battle.skills.FireBreathSkill;
import com.clashroom.shared.battle.skills.FireballSkill;
import com.clashroom.shared.battle.skills.Skill;

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
		return 0.40;
	}

	@Override
	public double getAgilityFactor() {
		return 0.45;
	}

	@Override
	public double getIntelligenceFactor() {
		return 0.85;
	}

	@Override
	public double getHpFactor() {
		return 0.35;
	}

	@Override
	public double getMpFactor() {
		return 0.85;
	}

	@Override
	protected void fillSkillTree(HashMap<Skill, Integer> skillTree) {
		skillTree.put(new FireballSkill(), 1);
		skillTree.put(new EggShellSkill(), 2);
		skillTree.put(new FireBreathSkill(), 8);
	}
}
