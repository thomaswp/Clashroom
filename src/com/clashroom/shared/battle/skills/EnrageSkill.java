package com.clashroom.shared.battle.skills;

import com.clashroom.shared.Constant;
import com.clashroom.shared.battle.buffs.Buff;
import com.clashroom.shared.battle.buffs.EnragedBuff;

public class EnrageSkill extends ActiveSkill {

    private static final long serialVersionUID = 1L;

    public EnrageSkill() {
        super("Enrage", "evil-eye-red-1.png", Attribute.Strength, Target.Self,
                                        true, 0, ACCURACY_PERFECT, 0, 30);
    }

    @Override
    public int getSkillPointCost() {
        return 1;
    }

    @Override
    public String getDescription() {
        return "Builds anger to increase the " + Constant.STAT_STR + " of this Dragon.";
    }

    @Override
    public Buff getBuff() {

        return new EnragedBuff(1.5f);
    }

}
