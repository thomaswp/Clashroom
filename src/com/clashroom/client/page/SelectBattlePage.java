package com.clashroom.client.page;

import java.util.List;

import com.clashroom.client.BattleService;
import com.clashroom.client.BattleServiceAsync;
import com.clashroom.client.FlowControl;
import com.clashroom.shared.BattleFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;

public class SelectBattlePage extends Page {

	private final BattleServiceAsync battleService = GWT
			.create(BattleService.class);

	public final static String NAME = "SelectBattle";
	
	public SelectBattlePage() {
		this(NAME);
	}
	
	public SelectBattlePage(String token) {
		super(token);
		
		Window.setTitle("Select Battle");
		
		final FlexTable table = new FlexTable();
		initWidget(table);

		battleService.getBattles(new AsyncCallback<List<BattleFactory>>() {
			@Override
			public void onSuccess(List<BattleFactory> result) {
				for (int i = 0; i < result.size(); i++) {
					final BattleFactory factory = result.get(i);
					//table.setHTML(i, 0, factory.getName());
					//Hyperlink link = new Hyperlink(factory.getName(), "battle");
					Button button = new Button(factory.getName());
					button.addClickHandler(new ClickHandler() {
						@Override
						public void onClick(ClickEvent event) {
							FlowControl.go(new BattlePage(factory));
						}
					});
					table.setWidget(i, 0, button);
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				caught.printStackTrace();
			}
		});
	}

}
