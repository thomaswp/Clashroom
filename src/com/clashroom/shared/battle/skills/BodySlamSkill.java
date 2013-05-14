package com.clashroom.shared.battle.skills;

import com.clashroom.shared.battle.battlers.Battler;

public class BodySlamSkill extends ActiveSkill {

    private static final long serialVersionUID = 1L;

    public BodySlamSkill() {
        super("Body Slam", "link-blue-1.png", Attribute.Strength, Target.One,
                                        false, 100, 0.75, 0.20, 30);
    }

    @Override
    public int getSkillPointCost() {
        return 5;
    }

    @Override
    public String getDescription() {

        return "Slams a single opponent with the force of gravity, " +
        		"dealing twice the normal damage, but with decreased accuracy.";
    }

    @Override
    protected double getAttackModifier(Battler attacker) {
        return attacker.getMeleeModifier();
    }
}