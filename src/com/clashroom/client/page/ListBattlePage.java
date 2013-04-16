package com.clashroom.client.page;

import java.util.List;

import com.clashroom.client.FlowControl;
import com.clashroom.client.services.BattleService;
import com.clashroom.client.services.BattleServiceAsync;
import com.clashroom.shared.BattleFactory;
import com.clashroom.shared.Debug;
import com.clashroom.shared.battlers.Battler;
import com.clashroom.shared.data.BattleEntity;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Hyperlink;

public class ListBattlePage extends Page {

	private static DateTimeFormat dateFormat = 
			DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_SHORT); 
	
	private final BattleServiceAsync battleService = GWT
			.create(BattleService.class);

	public final static String NAME = "ListBattles";
	
	public ListBattlePage() {
		this(NAME);
	}
	
	public ListBattlePage(String token) {
		super(token);
		
		Window.setTitle("Select Battle");
		
		final FlexTable table = new FlexTable();
		table.setWidth("600px");
		initWidget(table);
		
		String[] headers = new String[] {
				"Date", "Battle", "Challengers", "Victor", "Exp Gained"
		};
		
		for (int i = 0; i < headers.length; i++) {
			table.setText(0, i, headers[i]);
		}

		battleService.getBattles(new AsyncCallback<List<BattleEntity>>() {
			@Override
			public void onSuccess(List<BattleEntity> result) {
				for (int i = 0; i < result.size(); i++) {
					BattleEntity entity = result.get(i);
					BattleFactory factory = entity.getBattleFactory();
					Hyperlink link = new Hyperlink(factory.getName(), 
							BattlePage.getToken(entity.getId()));
					int row = i + 1;
					int col = 0;
					String enemies = "";
					for (Battler battler : entity.getBattleFactory().getTeamB()) {
						if (enemies.length() > 0) enemies += ", ";
						enemies += battler.description;
					}

					table.setText(row, col++, dateFormat.format(entity.getDate()));
					table.setWidget(i+1, col++, link);
					table.setText(row, col++, enemies);
					table.setText(row, col++, entity.isTeamAVictor() ? 
							factory.getTeamAName() : factory.getTeamBName());
					table.setText(row, col++, "" + entity.getTeamAExp());
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

}
