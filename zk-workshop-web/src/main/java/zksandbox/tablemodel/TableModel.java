package zksandbox.tablemodel;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Auxheader;
import org.zkoss.zul.Button;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Hlayout;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Vbox;
import org.zkoss.zul.event.ColSizeEvent;
import org.zkoss.zul.event.ListDataEvent;
import org.zkoss.zul.event.ZulEvents;

public abstract class TableModel<ROW> {

	private static final int COLUMN_MIN_WIDTH = 100; //pixels

	private List<Column> columns;
	private List<ROW> rows;
	protected InternalListModel listModel = new InternalListModel();

	protected abstract List<Column> buildColumns();

	protected abstract List<ROW> buildRows();

	/**
	 * Produce String or Listcell
	 * 
	 * @param columnIndex
	 * @param column
	 * @param rowData
	 * @return Returns String or Listcell
	 */
	protected abstract Object renderRowItem(int columnIndex, Column column, ROW rowData);

	/**
	 * 
	 */
	protected void initModel() {
		columns = buildColumns();
		rows = buildRows();
		listModel.setList(rows);
	}

	/**
	 * 
	 * @param rows
	 */
	public void setRows(List<ROW> rows) {
		listModel.setList(rows);
	}

	/**
	 * 
	 * @param listhead
	 * @param listbox
	 */
	public void generateHeaders(Listhead listhead, Listbox listbox) {
		listhead.addEventListener(ZulEvents.ON_COL_SIZE, e -> onColSizeEvent((ColSizeEvent) e));

		AtomicInteger effectivelyFinalInt = new AtomicInteger();
		for (Column column : columns) {
			Listheader listheader = new Listheader();
			Hlayout hlayout = new Hlayout();
			hlayout.setHflex("1");
			
			Vbox vboxLabel = new Vbox();
			vboxLabel.setHflex("1");
			vboxLabel.setAlign("start");
			vboxLabel.appendChild(new Label(column.getLabel()));
			hlayout.appendChild(vboxLabel);
			
			Vbox vboxMenu = new Vbox();
			vboxMenu.setHflex("1");
			vboxMenu.setAlign("end");
			
			Button btn = new Button();
			btn.setIconSclass("z-icon-caret-down");
			btn.setTooltip(String.valueOf(effectivelyFinalInt.getAndIncrement() + 1));
			
			
			Menupopup vytvoritMenuPopup = new Menupopup();
			Menuitem menuItem = new Menuitem();
			menuItem.setLabel("Zamykání");
			menuItem.addEventListener(Events.ON_CLICK, event -> lockColumnBtnOnClick(listbox, Integer.valueOf(btn.getTooltip())));
			menuItem.setParent(vytvoritMenuPopup);
			vboxMenu.appendChild(vytvoritMenuPopup);
			
			btn.setPopup(vytvoritMenuPopup);
			btn.setPopupAttributes(vytvoritMenuPopup, "after_end", null, null, null);
			// btn.addEventListener(Events.ON_CLICK, event -> lockColumnBtnOnClick(listbox, Integer.valueOf(btn.getTooltip())));			
			
			vboxMenu.appendChild(btn);
			hlayout.appendChild(vboxMenu);
			
			listheader.appendChild(hlayout);
			
			if (column.getHflex() != null) {
				listheader.setHflex(column.getHflex());
				listheader.setWidgetOverride("flexMinWidth", "300");
			} else if (column.getWidth() != null) {
				listheader.setWidth(column.getWidth());
			}
			listheader.setSort("auto");
			listheader.setDraggable("true");
			listheader.setDroppable("true");

			listhead.appendChild(listheader);
		}
	}
	
	private void lockColumnBtnOnClick(Listbox listbox, int colIdx) {
		if (listbox.getFrozen() != null) {
			Frozen frozen = listbox.getFrozen();
			int frozenColumns = frozen.getColumns();
			if (frozenColumns == 0 || frozenColumns != colIdx) {
				// zamknuti do daneho slouce
				frozen.setColumns(colIdx);
			} else if (frozenColumns == colIdx) {
				// odemknuti sloupce
				frozen.setColumns(colIdx - 1);
			} else {
				frozen.setColumns(0);
			}
		}
	}

	/**
	 * 
	 * @param auxhead
	 */
	public void generateFilters(Auxhead auxhead) {
		for (Column column : columns) {
			Textbox t = new Textbox();
			t.setHflex("1");
			Auxheader header = new Auxheader();
			header.appendChild(t);

			auxhead.appendChild(header);
		}
	}

	public List<Column> getColumns() {
		return columns;
	}

	public ListModelList<ROW> getListModel() {
		return listModel;
	}

	/**
	 * InternalListModel
	 * 
	 */
	private final class InternalListModel extends ListModelList<ROW> {

		private static final long serialVersionUID = 1L;

		private void setList(List<ROW> list) {
			_list = list;
			fireEvent(ListDataEvent.CONTENTS_CHANGED, -1, -1);
		}
	}

	/**
	 * 
	 * @param table
	 * @return
	 */
	public ListitemRenderer<ROW> getItemRenderer() {
		return new ListitemRenderer<ROW>() {
			@Override
			public void render(Listitem item, ROW data, int index) {
				renderRowItem(item, data);
			}
		};
	};

	/**
	 * 
	 * @param item
	 * @param data
	 * @param table
	 */
	private void renderRowItem(Listitem item, ROW row) {
		for (int i = 0; i < columns.size(); i++) {
			Object cellValue = renderRowItem(i, columns.get(i), row);

			if (cellValue == null) {
				item.appendChild(new Listcell());
			} else if (cellValue instanceof String) {
				Label label = new Label((String) cellValue);
				Listcell cell = new Listcell();
				cell.appendChild(label);
				item.appendChild(cell);
			} else if (cellValue instanceof Listcell) {
				item.appendChild((Component) cellValue);
			} else {
				throw new IllegalArgumentException("Illegal type: " + cellValue.getClass().getName());
			}
		}
	}

	/**
	 * 
	 * @param event
	 */
	private void onColSizeEvent(ColSizeEvent event) {
		int w = widthFromPx(event.getWidth());
		if (w < COLUMN_MIN_WIDTH) {
			w = COLUMN_MIN_WIDTH;
		}

		Listheader listheader = (Listheader) event.getColumn();
		if (listheader.getHflex() != null) {
			listheader.setHflex(null);
		}

		listheader.setWidth(w + "px");
	}

	/**
	 * 
	 * @param width
	 * @return
	 */
	private int widthFromPx(String width) {
		if (width == null) {
			return 0;
		}
		return Integer.parseInt(width.replace("px", ""));
	}
}
