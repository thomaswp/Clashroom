/**
 * CurseBuff.java 1.0 May 13, 2013
 * 
 * COPYRIGHT (c) 2013 David B. Belyea. All Rights Reserved
 */
package com.clashroom.shared.battle.buffs;

import com.clashroom.shared.Constant;

public class CurseBuff extends Buff {

    private float power;

    public CurseBuff(float power) {
        this.power = power;
        setModifiers();
    }

    @Override
    public String getName() {

        return "Cursed";
    }

    @Override
    public String getDescription() {

        return "This Dragon has been cursed and sufferes a decrease in "
                                        + Constant.STAT_AGI + " !";
    }

    @Override
    public String getIcon() {

        return "heal-royal-1.png";
    }

    @Override
    public void setModifiers() {
        setModifier(Stat.Agi, 0, power);
    }

}
