package com.clashroom.shared.battle.skills;

import java.util.List;

import com.clashroom.shared.battle.buffs.Buff;

public class FogSkill extends PassiveSkill {
	private static final long serialVersionUID = 1L;

	public FogSkill() {
		super("Fog", "fog-air-1.png", Attribute.Intelligence);
	}

	@Override
	public int getSkillPointCost() {
		return 3;
	}

	@Override
	public String getDescription() {
		return "This dragon is constantly surrounded with a thick fog, making it harder to hit.";
	}

	@Override
	protected void addBuffs(List<Buff> buffs) {
		buffs.add(new FogBuff());
	}
	
	public static class FogBuff extends Buff {

		@Override
		public String getName() {
			return "Fog";
		}

		@Override
		public String getDescription() {
			return "A fog surrounds this dragon, making it more difficult to hit.";
		}

		@Override
		public void setModifiers() {
			setModifier(Stat.Dodge, 0, 1.1f);
		}

		@Override
		public String getIcon() {
			return "fog-air-1.png";
		}
		
	}

}
