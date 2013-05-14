package com.clashroom.shared.battle.buffs;

import com.clashroom.shared.Constant;

public class EnragedBuff extends Buff {

    private float strength;

    public EnragedBuff(float strength) {
        this.strength = strength;
        setModifiers();
    }

    @Override
    public String getName() {
        return "Enraged";
    }

    @Override
    public String getDescription() {
        return "This Dragon is Enraged and has extra " + Constant.STAT_STR;
    }

    @Override
    public void setModifiers() {

        setModifier(Stat.Str, 0, strength);
    }

    @Override
    public String getIcon() {
        return "evil-eye-red-1.png";
    }

}
