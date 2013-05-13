package com.clashroom.shared.battle.dragons;

import java.util.HashMap;

import com.clashroom.shared.battle.skills.BlessingSkill;
import com.clashroom.shared.battle.skills.HealSkill;
import com.clashroom.shared.battle.skills.Skill;

public class LionDragon extends DragonClass {

	@Override
	public String getName() {
		return "Lion Dragon";
	}
	
	@Override
	public String getImageName() {
		return "lion-dragon.png";
	}
	
	@Override
	public String getDescription() {
		return "This graceful dragon is quick and powerful. It's lightening strikes will leave even the" +
				"toughest creatures in a trance.";
	}

	@Override
	public double getStrengthFactor() {
		return 0.65;
	}

	@Override
	public double getAgilityFactor() {
		return 0.75;
	}

	@Override
	public double getIntelligenceFactor() {
		return 0.35;
	}

	@Override
	public double getHpFactor() {
		return 0.75;
	}

	@Override
	public double getMpFactor() {
		return 0.60;
	}

	@Override
	protected void fillSkillTree(HashMap<Skill, Integer> skillTree) {
		skillTree.put(new HealSkill(), 4);
		skillTree.put(new BlessingSkill(), 8);
	}


}
