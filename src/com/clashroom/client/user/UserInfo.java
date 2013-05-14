package com.clashroom.client.user;

import java.beans.Beans;

import com.clashroom.client.FlowControl;
import com.clashroom.client.Styles;
import com.clashroom.client.services.UserInfoServiceAsync;
import com.clashroom.client.widget.AnimatedProgressBar;
import com.clashroom.client.widget.ProgressBar;
import com.clashroom.client.widget.ProgressBar.TextFormatter;
import com.clashroom.shared.Constant;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.dragons.DragonClass;
import com.clashroom.shared.battle.skills.Skill;
import com.clashroom.shared.entity.DragonEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.NumberLabel;

public class UserInfo extends Composite {

	private DragonEntity dragon;

	public void setUser(UserEntity user, boolean isMe) {
		dragon = user.getDragon();

		DragonClass dragonClass = dragon.getDragonClass();

		labelDragonName.setText(dragon.getName());
		imageDragon.setUrl(Constant.IMG_BATTLER + dragonClass.getImageName());
		labelLevel.setText(" " + dragon.getLevel());
		progressBarExp.setMinProgress(0);
		progressBarExp.setMaxProgress(DragonClass.getNextLevelExp(dragon.getLevel()));
		progressBarExp.animateSetProgress(dragon.getExperience());

		labelHp.setValue((int)dragon.getMaxHp());
		labelMp.setValue((int)dragon.getMaxMp());
		labelStr.setValue((int)dragon.getStrength());
		labelAgi.setValue((int)dragon.getAgility());
		labelInt.setValue((int)dragon.getIntelligence());

		labelMelee.setValue(Battler.getMeleeModifier((int)dragon.getStrength()));
		labelSpellMod.setValue(Battler.getSpellModifier((int)dragon.getIntelligence(), dragon.getLevel()));
		labelDodge.setText(Math.round(100 * Battler.getDodgeChance((int)dragon.getAgility(), dragon.getLevel())) + "%");
		labelCrit.setText(Math.round(100 * Battler.getCriticalChance((int)dragon.getAgility(), dragon.getLevel())) + "%");

		vPanelSkills.addStyleName(Styles.profile_skills);
		labelSp.setText(Formatter.format("You have %s%s to spend.", user.getSkillPoints(), 
				Constant.TERM_SKILL_POINT_SHORT));
		labelSp.addStyleName("indent");
		labelSp.setVisible(isMe);
		for (Skill skill : dragonClass.getSkills()) {
			addSkill(skill, user, dragonClass, isMe);
		}
	}

	private void addSkill(final Skill skill, UserEntity user, DragonClass dragonClass, boolean isMe) {
		boolean learned = user.getDragon().getSkills().contains(skill.getId());

		HorizontalPanel hPanel = new HorizontalPanel();
		hPanel.addStyleName(learned ? Styles.profile_skill_learned :
			Styles.profile_skill_unlearned);
		if (skill.isActive()) hPanel.addStyleName(Styles.profile_skill_active);
		hPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		Image image = new Image(Constant.IMG_ICON + skill.getIcon());
		image.setWidth("100%");
		SimplePanel simplePanelImage = new SimplePanel(image);
		simplePanelImage.addStyleName(Styles.profile_skill_border);
		hPanel.add(simplePanelImage);

		VerticalPanel vPanelText = new VerticalPanel();
		hPanel.add(vPanelText);
		HorizontalPanel hPanelName = new HorizontalPanel();
		hPanelName.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		vPanelText.add(hPanelName);

		Label labelName = new Label(skill.getName(), false);
		labelName.addStyleName(Styles.profile_skill_name);
		hPanelName.add(labelName);
		Label labelDescription = new Label(skill.getDescription());
		hPanelName.add(labelDescription);

		if (!learned) {
			HorizontalPanel hPanelRequirements = new HorizontalPanel();
			vPanelText.add(hPanelRequirements);
			String cost = Formatter.format(
					"[Costs %s%s]", skill.getSkillPointCost(), 
					Constant.TERM_SKILL_POINT_SHORT);
			int reqLevel = dragonClass.getSkillTree().get(skill);
			if (isMe && reqLevel <= dragon.getLevel() && user.getSkillPoints() >= skill.getSkillPointCost()) {
				
				final Anchor anchorSkillPoint = new Anchor(Formatter.format("[Learn for %s%s]", 
						skill.getSkillPointCost(), Constant.TERM_SKILL_POINT_SHORT));
				anchorSkillPoint.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (anchorSkillPoint.isEnabled()) {
							anchorSkillPoint.setText("[Confirm]");
							anchorSkillPoint.setEnabled(false);
							return;
						}
						
						userInfoService.learnSkill(skill.getId(), new AsyncCallback<Void>() {
							@Override
							public void onSuccess(Void result) {
								FlowControl.go(new UserInfoPage());
							}

							@Override
							public void onFailure(Throwable caught) {
								caught.printStackTrace();
								FlowControl.go(new UserInfoPage());
							}
						});
					}
				});
				hPanelRequirements.add(anchorSkillPoint);
			} else {
				Label labelSkillPoints = new Label(cost);
				hPanelRequirements.add(labelSkillPoints);
			}
			if (reqLevel > dragon.getLevel()) {
				Label labelLevelReq = new Label("Requires level " + reqLevel);
				hPanelRequirements.add(labelLevelReq);
			}
		}

		vPanelSkills.add(hPanel);
	}

	private Label labelDragonName;
	private Image imageDragon;
	private AnimatedProgressBar progressBarExp;
	private Label labelLevel;
	private NumberLabel<Integer> labelHp;
	private NumberLabel<Integer> labelMp;
	private NumberLabel<Integer> labelStr;
	private NumberLabel<Integer> labelAgi;
	private NumberLabel<Integer> labelInt;
	

	private NumberLabel<Double> labelSpellMod;
	private NumberLabel<Double> labelMelee;
	private Label labelDodge;
	private Label labelCrit;
	private VerticalPanel vPanelSkills;
	private Label labelSp;
	private UserInfoServiceAsync userInfoService;

	public void setUserService(UserInfoServiceAsync userInfoService) {
		this.userInfoService = userInfoService;
	}

	public UserInfo() {

		VerticalPanel vPanelMain = new VerticalPanel();
		if (Beans.isDesignTime()) {
			vPanelMain.setStyleName(UserInfoPage.NAME);
			vPanelMain.addStyleDependentName(Styles.position_relative);
			vPanelMain.setWidth("600px");
		}
		initWidget(vPanelMain);

		HorizontalPanel hPanelImage = new HorizontalPanel();
		vPanelMain.add(hPanelImage);

		SimplePanel simplePanelImage = new SimplePanel();
		imageDragon = new Image("clear.cache.gif");
		simplePanelImage.addStyleName(Styles.profile_portrait);
		simplePanelImage.add(imageDragon);
		hPanelImage.add(simplePanelImage);

		VerticalPanel vPanelNameAndStats = new VerticalPanel();
		hPanelImage.add(vPanelNameAndStats);

		HorizontalPanel hPanelName = new HorizontalPanel();
		vPanelNameAndStats.add(hPanelName);

		labelDragonName = new Label("Dragon");
		labelDragonName.addStyleName(Styles.profile_name);
		hPanelName.add(labelDragonName);

		labelLevel = new Label(" 1");
		labelLevel.addStyleName(Styles.profile_lvl);
		hPanelName.add(labelLevel);

		progressBarExp = new AnimatedProgressBar();
		progressBarExp.setWidth("95%");
		vPanelMain.add(progressBarExp);
		progressBarExp.setTextFormatter(new TextFormatter() {
			@Override
			protected String getText(ProgressBar bar, double curProgress) {
				return Formatter.format("%s: %s/%s", Constant.TERM_EXP_SHORT, (int)curProgress, 
						(int)bar.getMaxProgress());
			}
		});

		HorizontalPanel hPanelStats = new HorizontalPanel();
		hPanelStats.addStyleName(Styles.profile_stats);
		vPanelNameAndStats.add(hPanelStats);

		HorizontalPanel hPanelHp = new HorizontalPanel();

		VerticalPanel vPanelStats1 = new VerticalPanel();
		hPanelStats.add(vPanelStats1);

		Label labelHpPrompt = new Label(Constant.STAT_HP + ": ");
		hPanelHp.add(labelHpPrompt);

		labelHp = new NumberLabel<Integer>();
		hPanelHp.add(labelHp);
		vPanelStats1.add(hPanelHp);

		HorizontalPanel hPanelMp = new HorizontalPanel();
		vPanelStats1.add(hPanelMp);

		Label labelMpPrompt = new Label(Constant.STAT_MP +": ");
		hPanelMp.add(labelMpPrompt);

		labelMp = new NumberLabel<Integer>();
		hPanelMp.add(labelMp);

		VerticalPanel vPanelStats2 = new VerticalPanel();
		hPanelStats.add(vPanelStats2);

		HorizontalPanel hPanelStr = new HorizontalPanel();
		vPanelStats1.add(hPanelStr);

		Label labelStrPrompt = new Label(Constant.STAT_STR + ": ");
		hPanelStr.add(labelStrPrompt);

		labelStr = new NumberLabel<Integer>();
		hPanelStr.add(labelStr);

		HorizontalPanel hPanelAgi = new HorizontalPanel();
		vPanelStats1.add(hPanelAgi);

		Label labelAgiPrompt = new Label(Constant.STAT_AGI +": ");
		hPanelAgi.add(labelAgiPrompt);

		labelAgi = new NumberLabel<Integer>();
		hPanelAgi.add(labelAgi);

		HorizontalPanel hPanelInt = new HorizontalPanel();
		vPanelStats1.add(hPanelInt);

		Label labelIntPrompt = new Label(Constant.STAT_INT + ": ");
		hPanelInt.add(labelIntPrompt);

		labelInt = new NumberLabel<Integer>();
		hPanelInt.add(labelInt);
		
		HorizontalPanel hPanelSpellMod = new HorizontalPanel();
		vPanelStats2.add(hPanelSpellMod);
		
		Label labelSpellModPrompt = new Label("Melee Bonus: ");
		hPanelSpellMod.add(labelSpellModPrompt);
		
		labelMelee = new NumberLabel<Double>();
		hPanelSpellMod.add(labelMelee);
		
		hPanelSpellMod = new HorizontalPanel();
		vPanelStats2.add(hPanelSpellMod);
		
		labelSpellModPrompt = new Label("Spell Bonus: ");
		hPanelSpellMod.add(labelSpellModPrompt);
		
		labelSpellMod = new NumberLabel<Double>();
		hPanelSpellMod.add(labelSpellMod);
		
		hPanelSpellMod = new HorizontalPanel();
		vPanelStats2.add(hPanelSpellMod);
		
		labelSpellModPrompt = new Label("Dodge Chance: ");
		hPanelSpellMod.add(labelSpellModPrompt);
		
		labelDodge = new Label();
		hPanelSpellMod.add(labelDodge);
		
		hPanelSpellMod = new HorizontalPanel();
		vPanelStats2.add(hPanelSpellMod);
		
		labelSpellModPrompt = new Label("Critical Chance: ");
		hPanelSpellMod.add(labelSpellModPrompt);
		
		labelCrit = new Label();
		hPanelSpellMod.add(labelCrit);
		
		Label skillsLabel = new Label(Constant.TERM_SKILL_PL);
		skillsLabel.addStyleName(Styles.text_title);
		vPanelMain.add(skillsLabel);

		vPanelSkills = new VerticalPanel();
		vPanelMain.add(vPanelSkills);

		labelSp = new Label("New label");
		vPanelSkills.add(labelSp);
	}

}
