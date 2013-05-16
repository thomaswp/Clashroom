package com.clashroom.client.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.clashroom.client.Clashroom;
import com.clashroom.client.HomePage;
import com.clashroom.client.Styles;
import com.clashroom.client.battle.BattlePage;
import com.clashroom.client.services.BattleService;
import com.clashroom.client.services.BattleServiceAsync;
import com.clashroom.client.user.UserInfoPage;
import com.clashroom.client.widget.ScrollableFlexTable;
import com.clashroom.shared.Constant;
import com.clashroom.shared.Debug;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.battle.battlers.DragonBattler;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.QueuedBattleEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A Window on the {@link HomePage} to show a list of battles that this
 * user has been in. Links to to the {@link BattlePage} to show the given
 * battles.
 */
public class ListBattleWindow extends Composite implements IWindow {

	//battles newer than this time (in ms) will flash when loaded
	private final static int BOLD_BATTLE_TIME = 24 * 60 * 60 * 1000; //24 hours in ms
	
	private static DateTimeFormat dateFormat = 
			DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT); 
	
	private final BattleServiceAsync battleService = GWT
			.create(BattleService.class);

	private List<BattleEntity> battles;
	private List<QueuedBattleEntity> futureBattles;
	private ScrollableFlexTable table;
	
	
	public final static String NAME = "ListBattles";
	
	public ListBattleWindow() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName(NAME + "Window");
		Label title = new Label("Battles");
		title.addStyleName(Styles.text_title);
		panel.add(title);
		
		table = new ScrollableFlexTable();
		table.setHeaders("Date", "Battle", "Challengers", "Victor", Constant.TERM_EXP_SHORT + " Gained");
		table.setHeaderWidths("15%", "25%", "25%", "15%", "20%");
		table.setColumnWidths("15%", "25%", "25%", "20%", "15%");
		table.addHeaderStyles(Styles.table_header, Styles.gradient);
		table.getInnerTable().addStyleName(Styles.table);
		table.getOuterTable().addStyleName(Styles.outer_table);
		table.getScrollPanel().setHeight("213px");
		
		panel.add(table);
		
		initWidget(panel);

		battleService.getBattles(new AsyncCallback<List<BattleEntity>>() {
			@Override
			public void onSuccess(List<BattleEntity> result) {
				Collections.sort(result, new Comparator<BattleEntity>() {
					@Override
					public int compare(BattleEntity o1, BattleEntity o2) {
						return o2.getDate().compareTo(o1.getDate());
					}
				});
				battles = result;
				populate();
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
		
		battleService.getScheduledBattles(new AsyncCallback<List<QueuedBattleEntity>>() {
			
			@Override
			public void onSuccess(List<QueuedBattleEntity> result) {
				Collections.sort(result, new Comparator<QueuedBattleEntity>() {
					@Override
					public int compare(QueuedBattleEntity o1, QueuedBattleEntity o2) {
						return o2.getTime().compareTo(o1.getTime());
					}
				});
				futureBattles = result;
				populate();
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}
	
	private void populate() {
		if (battles == null || futureBattles == null) return;
		int row = 1;
		for (int i = 0; i < futureBattles.size(); i++) {
			QueuedBattleEntity entity = futureBattles.get(i);
			
			int col = 0;

			boolean teamA = entity.getTeamAIds().contains(Clashroom.getLoginInfo().getUserId());
			String enemies = teamA ? entity.getTeamBName() : entity.getTeamAName();
			
			table.setText(row, col++, dateFormat.format(entity.getTime()));
			table.setText(row, col++, Formatter.format("%s v %s",
					entity.getTeamAName(), entity.getTeamBName()));
			table.setText(row, col++, enemies);
			table.setText(row, col++, "");
			table.setText(row, col++, "");
			
			row++;
		}
		for (int i = 0; i < battles.size(); i++) {
			BattleEntity entity = battles.get(i);
			BattleFactory factory = entity.getBattleFactory();
			Hyperlink link = new Hyperlink(factory.getName(), 
					BattlePage.getToken(entity.getId()));
			
			
			if (System.currentTimeMillis() - entity.getDate().getTime() < BOLD_BATTLE_TIME) {
				table.getInnerTable().getRowFormatter().addStyleName(row, Styles.table_row_bold);
			}
			
			int col = 0;
			
			boolean teamA = entity.getTeamAIds().contains(Clashroom.getLoginInfo().getUserId());
			List<Battler> challengers = teamA ? entity.getBattleFactory().getTeamB() : 
				entity.getBattleFactory().getTeamA();
			String enemies = "";
			for (Battler battler : challengers) {
				String desc = SafeHtmlUtils.htmlEscape(battler.description);
				if (battler instanceof DragonBattler) {
					desc = Formatter.format("<a href=#%s?id=%s>%s</a>", 
							UserInfoPage.NAME, ((DragonBattler) battler).playerId, desc);
				}
				enemies = Formatter.appendList(enemies, desc);
			}
			
			
			table.setText(row, col++, dateFormat.format(entity.getDate()));
			table.setWidget(row, col++, link);
			table.setWidget(row, col++, new HTML(enemies));
			table.setText(row, col++, entity.isTeamAVictor() ? 
					factory.getTeamAName() : factory.getTeamBName());
			table.setText(row, col++, "" + (teamA ? entity.getTeamAExp() : entity.getTeamBExp()));
			
			row++;
		}
	}

	@Override
	public void onReceiveUserInfo(UserEntity user) {
		//Doesn't do anythign with the UserEntity at present
	}

}
