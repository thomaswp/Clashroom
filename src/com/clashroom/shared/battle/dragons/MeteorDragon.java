package com.clashroom.shared.battle.dragons;

import java.util.HashMap;

import com.clashroom.shared.battle.skills.BodySlamSkill;
import com.clashroom.shared.battle.skills.CurseSkill;
import com.clashroom.shared.battle.skills.EnrageSkill;
import com.clashroom.shared.battle.skills.Skill;

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
		return 0.95;
	}

	@Override
	public double getAgilityFactor() {
		return 0.40;
	}

	@Override
	public double getIntelligenceFactor() {
		return 0.30;
	}

	@Override
	public double getHpFactor() {
		return 0.85;
	}

	@Override
	public double getMpFactor() {
		return 0.35;
	}

	@Override
	protected void fillSkillTree(HashMap<Skill, Integer> skillTree) {
		skillTree.put(new EnrageSkill(), 1);
		skillTree.put(new CurseSkill(), 5);
		skillTree.put(new BodySlamSkill(), 8);
	}


}
