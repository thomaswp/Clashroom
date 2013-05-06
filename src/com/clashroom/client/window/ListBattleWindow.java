package com.clashroom.client.window;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import com.clashroom.client.Clashroom;
import com.clashroom.client.HomePage;
import com.clashroom.client.Page;
import com.clashroom.client.Styles;
import com.clashroom.client.battle.BattlePage;
import com.clashroom.client.services.BattleService;
import com.clashroom.client.services.BattleServiceAsync;
import com.clashroom.shared.Constant;
import com.clashroom.shared.Formatter;
import com.clashroom.shared.battle.BattleFactory;
import com.clashroom.shared.battle.battlers.Battler;
import com.clashroom.shared.entity.BattleEntity;
import com.clashroom.shared.entity.QueuedBattleEntity;
import com.clashroom.shared.entity.UserEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ListBattleWindow extends Composite {

	private static DateTimeFormat dateFormat = 
			DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT); 
	
	private final BattleServiceAsync battleService = GWT
			.create(BattleService.class);

	public final static String NAME = "ListBattles";
	
	public ListBattleWindow() {
		
		VerticalPanel panel = new VerticalPanel();
		panel.addStyleName(NAME);
		Label title = new Label("Battles");
		title.addStyleName(Styles.text_title);
		panel.add(title);
		
		final FlexTable table = new FlexTable();
		table.addStyleName(Styles.table);
		table.getRowFormatter().addStyleName(0,Styles.gradient);
		table.getRowFormatter().addStyleName(0, Styles.table_header);
		panel.add(table);
		initWidget(panel);
		
		String[] headers = new String[] {
				"Date", "Battle", "Challengers", "Victor", Constant.TERM_EXP_SHORT + " Gained"
		};
		
		for (int i = 0; i < headers.length; i++) {
			table.setText(0, i, headers[i]);
		}

		battleService.getBattles(new AsyncCallback<List<BattleEntity>>() {
			@Override
			public void onSuccess(List<BattleEntity> result) {
				Collections.sort(result, new Comparator<BattleEntity>() {
					@Override
					public int compare(BattleEntity o1, BattleEntity o2) {
						return o1.getDate().compareTo(o2.getDate());
					}
				});
				int row = 1;
				for (int i = 0; i < result.size(); i++) {
					BattleEntity entity = result.get(i);
					BattleFactory factory = entity.getBattleFactory();
					Hyperlink link = new Hyperlink(factory.getName(), 
							BattlePage.getToken(entity.getId()));
					
					table.insertRow(row);
					
					int col = 0;
					

					boolean teamA = entity.getTeamAIds().contains(Clashroom.getLoginInfo().getUserId());
					List<Battler> challengers = teamA ? entity.getBattleFactory().getTeamB() : 
						entity.getBattleFactory().getTeamA();
					String enemies = "";
					for (Battler battler : challengers) {
						enemies = Formatter.appendList(enemies, battler.description);
					}
					
					
					table.setText(row, col++, dateFormat.format(entity.getDate()));
					table.setWidget(row, col++, link);
					table.setText(row, col++, enemies);
					table.setText(row, col++, entity.isTeamAVictor() ? 
							factory.getTeamAName() : factory.getTeamBName());
					table.setText(row, col++, "" + (teamA ? entity.getTeamAExp() : entity.getTeamBExp()));
					
					row++;
				}
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
						return o1.getTime().compareTo(o2.getTime());
					}
				});
				int row = table.getRowCount();
				for (int i = 0; i < result.size(); i++) {
					QueuedBattleEntity entity = result.get(i);
					
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
			}
			
			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

}
