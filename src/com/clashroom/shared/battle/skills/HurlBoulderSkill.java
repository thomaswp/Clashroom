/**
 * HurlBoulderSkill.java 1.0 May 13, 2013
 * 
 * COPYRIGHT (c) 2013 David B. Belyea. All Rights Reserved
 */
package com.clashroom.shared.battle.skills;

/**
 * Start with summary description line
 * 
 * @author deagle
 * @version 1.0
 * 
 */
public class HurlBoulderSkill extends ActiveSkill {

    public HurlBoulderSkill() {

        super("Hurl Boulder", "rock-orange-1.png", Attribute.Strength,
                                        Target.One, false, 80, .70, 0, 10);
    }

    @Override
    public int getSkillPointCost() {

        return 2;
    }

    @Override
    public String getDescription() {

        return "Hurls a huge boulder at your opponent";
    }
}
