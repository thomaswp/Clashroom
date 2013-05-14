/**
 * Curse.java 1.0 May 13, 2013
 * 
 * COPYRIGHT (c) 2013 David B. Belyea. All Rights Reserved
 */
package com.clashroom.shared.battle.skills;

import com.clashroom.shared.battle.buffs.Buff;
import com.clashroom.shared.battle.buffs.CurseBuff;

public class CurseSkill extends ActiveSkill {

    private static final long serialVersionUID = 1L;

    public CurseSkill() {
        super("Curse", "heal-royal-1.png", Attribute.Intelligence, Target.One,
                                        false, 0, ACCURACY_PERFECT, 0, 65);
    }
    
    @Override
    public int getSkillPointCost() {
        return 3;
    }

    @Override
    public String getDescription() {
        return "Causes a target Dragon to take more damage";
    }

    @Override
    public Buff getBuff() {
        return new CurseBuff(.80f);
    }

}
