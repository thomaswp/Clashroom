package com.clashroom.client.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.FlexTable;

public class ScrollableFlexTable extends Composite {
	
	private FlexTable innerTable, outerTable;
	private ScrollPanel scrollPanel;
	
	public FlexTable getInnerTable() {
		return innerTable;
	}
	
	public FlexTable getOuterTable() {
		return outerTable;
	}
	
	public ScrollPanel getScrollPanel() {
		return scrollPanel;
	}
	
	public ScrollableFlexTable() {
		outerTable = new FlexTable();
		innerTable = new FlexTable();
		scrollPanel = new ScrollPanel();
		outerTable.setWidget(1, 0, scrollPanel);
		scrollPanel.add(innerTable);
		outerTable.setCellSpacing(0);
		initWidget(outerTable);
	}
	
	public void setText(int row, int column, String text) {
		innerTable.setText(row, column, text);
	}
	
	public void setHTML(int row, int column, String html) {
		innerTable.setHTML(row, column, html);
	}
	
	public void setWidget(int row, int column, IsWidget widget) {
		innerTable.setWidget(row, column, widget);
	}
	
	public int getRowCount() {
		return innerTable.getRowCount();
	}
	
	public int getCellCount(int row) {
		return innerTable.getCellCount(row);
	}
	
	public void insertRow(int beforeRow) {
		innerTable.insertRow(beforeRow);
	}

	public void removeRow(int row) {
		innerTable.removeRow(row);
	}
	
	public void clearNonHeaders() {
		innerTable.clear();
	}
	
	public void setHeaders(String... headers) {
		for (int i = 0; i < headers.length; i++) {
			outerTable.setText(0, i, headers[i]);
		}
		outerTable.getFlexCellFormatter().setColSpan(1, 0, headers.length);
	}
	
	public void setHeaderWidths(String... widths) {
		for (int i = 0; i < widths.length; i++) {
			outerTable.getColumnFormatter().setWidth(i, widths[i]);
		}
	}
	
	public void setColumnWidths(String... widths) {
		for (int i = 0; i < widths.length; i++) {
			innerTable.getColumnFormatter().setWidth(i, widths[i]);
		}
	}
	
	public void addHeaderStyles(String... styles) {
		for (String style : styles) {
			outerTable.getRowFormatter().addStyleName(0, style);
		}
	}

}
