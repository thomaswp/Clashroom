package com.clashroom.shared.battle.skills;

import java.util.List;

import com.clashroom.shared.Constant;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.buff.Buff;

public class EggShellSkill extends PassiveSkill {   
	private static final long serialVersionUID = 1L;

	public EggShellSkill() {
		super("Tough Egg Shell", "protect-blue-1.png", Attribute.None);
	}

	@Override
	public int getSkillPointCost() {
		return 2;
	}

	@Override
	public String getDescription() {
		return "A tough egg shell protects you, giving you a permanent bonus to " + Constant.STAT_HP;
	}

	@Override
	protected void addBuffs(List<Buff> buffs) {
		buffs.add(new EggShellBuff());
	}
	
	public static class EggShellBuff extends Buff {
		@Override
		public String getName() {
			return "Egg Shell";
		}

		@Override
		public String getDescription() {
			return "A tough egg shell protects this Dragon, giving it extra " + Constant.STAT_HP + ".";
		}

		@Override
		public void setModifiers() {
			setModifier(Stat.MaxHp, 0, 1.1f);
		}

		@Override
		public String getIcon() {
			return "protect-blue-1.png";
		}
	}

}
