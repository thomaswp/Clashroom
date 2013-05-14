package com.clashroom.shared.battle.skills;

public class LionFireSkill extends ActiveSkill {

    private static final long serialVersionUID = 1L;

    public LionFireSkill() {

        super("Lion Fire", "needles-fire-1.png", Attribute.Intelligence,
                                        Target.Splash, false, 115, 1, 0.15, 75);
    }

    @Override
    public String getDescription() {

        return "Spray Lion Fire amongst your enemies, dealing splash damage.";
    }

    @Override
    public int getSkillPointCost() {

        return 1;
    }

}
