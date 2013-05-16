package com.clashroom.client.widget;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.FlexTable;

/**
 * Allows stationary headers on a {@link FlexTable} while
 * scrolling the rest of the cells. Most of FlexTable's methods
 * are wrapped by this class. The bundle contains a FlexTable,
 * the first row of which is the headers and the second row of
 * which is a {@link ScrollPanel} with another FlexTable inside,
 * containing the rest of the content.
 */
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
	
	/**
	 * See {@link FlexTable#setText(int, int, String)}
	 */
	public void setText(int row, int column, String text) {
		innerTable.setText(row, column, text);
	}
	
	/**
	 * See {@link FlexTable#setHTML(int, int, String)}
	 */
	public void setHTML(int row, int column, String html) {
		innerTable.setHTML(row, column, html);
	}
	
	/**
	 * See {@link FlexTable#setWidget(int, int, IsWidget)}
	 */
	public void setWidget(int row, int column, IsWidget widget) {
		innerTable.setWidget(row, column, widget);
	}
	
	/**
	 * See {@link FlexTable#getRowCount()}
	 */
	public int getRowCount() {
		return innerTable.getRowCount();
	}
	
	/**
	 * See {@link FlexTable#getCellCount(int)}
	 */
	public int getCellCount(int row) {
		return innerTable.getCellCount(row);
	}
	
	/**
	 * See {@link FlexTable#insertRow(int)}
	 */
	public void insertRow(int beforeRow) {
		innerTable.insertRow(beforeRow);
	}

	/**
	 * See {@link FlexTable#removeRow(int)}
	 */
	public void removeRow(int row) {
		innerTable.removeRow(row);
	}
	
	/**
	 * Clears the innerTable
	 */
	public void clearNonHeaders() {
		innerTable.clear();
	}
	
	/**
	 * Sets the headers in the outerTable to these headers
	 * @param headers
	 */
	public void setHeaders(String... headers) {
		for (int i = 0; i < headers.length; i++) {
			outerTable.setText(0, i, headers[i]);
		}
		outerTable.getFlexCellFormatter().setColSpan(1, 0, headers.length);
	}
	
	/**
	 * Sets the widths of the header columns in the outerTable
	 * @param widths
	 */
	public void setHeaderWidths(String... widths) {
		for (int i = 0; i < widths.length; i++) {
			outerTable.getColumnFormatter().setWidth(i, widths[i]);
		}
	}
	
	/**
	 * Sets the widths of the columns in the innerTable
	 * @param widths
	 */
	public void setColumnWidths(String... widths) {
		for (int i = 0; i < widths.length; i++) {
			innerTable.getColumnFormatter().setWidth(i, widths[i]);
		}
	}
	
	/**
	 * Adds styles to the outerTable. These are not respective
	 * to columns, the method simply allows you to add multiple
	 * styles simultaneously.
	 * @param styles
	 */
	public void addHeaderStyles(String... styles) {
		for (String style : styles) {
			outerTable.getRowFormatter().addStyleName(0, style);
		}
	}

}
