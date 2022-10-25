package zksandbox.composers;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zul.Auxhead;
import org.zkoss.zul.Button;
import org.zkoss.zul.Checkbox;
import org.zkoss.zul.Frozen;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listhead;
import org.zkoss.zul.Listheader;
import org.zkoss.zul.Spinner;
import org.zkoss.zul.Vlayout;

import zksandbox.component.CustomAutopaging;
import zksandbox.tablemodel.TableModel;

public class TablemodelComposer extends SelectorComposer<Listbox> {

	private static final long serialVersionUID = 1L;

	private TableModel<?> tableModel;
	private Listbox listbox;
	private CustomAutopaging autopaging;

	@Override
	public void doAfterCompose(Listbox listbox) throws Exception {
		super.doAfterCompose(listbox);
		initComponent(listbox);
	}

	/**
	 * 
	 * @param listbox
	 */
	private void initComponent(final Listbox listbox) {
		this.listbox = listbox;
		tableModel = (TableModel<?>) listbox.getAttribute("tableModel", true);
		generateHeadersAndFilters();
		listbox.setItemRenderer(tableModel.getItemRenderer());
		listbox.addEventListener("onAfterRender", e -> onAfterRender(e));
		setupPaging();
		setupControls();
	}

	private void setupControls() {
		// single/multiple mode
		Checkbox multipleCheck = (Checkbox) listbox.getFellowIfAny("allowMultipleModeCheck");
		if (multipleCheck != null) {
			multipleCheck.addEventListener(Events.ON_CLICK, event -> onListboxMultipleChange(multipleCheck));
		}

		// Fifth column is hidden (checkbox)
		Vlayout colsVisMenuOptions = (Vlayout) listbox.getFellowIfAny("colsVisMenuOptions");
		// generateShowHideCheckboxes(colsVisMenuOptions);
		
		Vlayout colsLockMenuContent = (Vlayout) listbox.getFellowIfAny("colsLockMenuContent");
		generateColsLockMenuContent(colsLockMenuContent);
	}

	/*
	 * 
	 */
	private void generateShowHideCheckboxes(Vlayout colsVisMenuOptions) {
		if (colsVisMenuOptions == null) {
			return;
		}

		for (Object o : listbox.getListhead().getChildren()) {
			if (o instanceof Listheader) {
				Listheader listheader = (Listheader) o;
				Label label = (Label) listheader.getFirstChild();

				Checkbox c = new Checkbox(label.getValue());
				c.setAttribute("x_listheader", listheader);
				c.setChecked(true);
				c.addEventListener(Events.ON_CHECK, e -> onColsVisMenuOptionsCheckboxChange(e, c));
				colsVisMenuOptions.appendChild(c);
			}
		}
	}
	
	/*
	 * 
	 */
	private void generateColsLockMenuContent(Vlayout colsLockMenuContent) {
		if (colsLockMenuContent == null) {
			return;
		}
		
		if (listbox.getFrozen() == null) {
			return;
		}
		
		int columnCount = listbox.getListhead().getChildren().size();
		
		Spinner spinner = new Spinner();
		spinner.setConstraint("no empty,max " + columnCount);
		spinner.addEventListener(Events.ON_OK, e -> onColsColLockSpinnerChange(e, spinner));
		spinner.setValue(listbox.getFrozen().getColumns());
		colsLockMenuContent.appendChild(spinner);
		
		Button btnLockColSubmit = new Button();
		btnLockColSubmit.setLabel("OK");
		btnLockColSubmit.addEventListener(Events.ON_CLICK, e -> onColsColLockSpinnerChange(e, spinner));
		colsLockMenuContent.appendChild(btnLockColSubmit);
	}

	/**
	 * 
	 * @param e
	 * @param c
	 */
	private void onColsVisMenuOptionsCheckboxChange(Event e, Checkbox c) {
		Listheader listheader = (Listheader) c.getAttribute("x_listheader");
		listheader.setVisible(c.isChecked());
	}
	
	/**
	 * 
	 * @param e
	 * @param c
	 */
	private void onColsColLockSpinnerChange(Event e, Spinner c) {
		lockColumnBtnOnClick(c.getValue());
	}
	
	private void lockColumnBtnOnClick(int colIdx) {
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
	 */
	private void setupPaging() {
		Boolean noPaging = (Boolean) listbox.getAttribute("noPaging");
		if (noPaging == null) {
			noPaging = false;
		}

		if (noPaging) {
			listbox.setAutopaging(false);
			listbox.setMold(null);
		} else {
			autopaging = CustomAutopaging.createInstance(listbox, 31);
		}
	}

	/**
	 * 
	 */
	private void generateHeadersAndFilters() {
		for (Component cmp : listbox.getChildren()) {
			if (cmp instanceof Listhead) {
				tableModel.generateHeaders((Listhead) cmp, listbox);
			}

			if (cmp.getId().equals("tableFilters")) {
				tableModel.generateFilters((Auxhead) cmp);
			}
		}
	}

	/**
	 * 
	 * @param e
	 */
	private void onAfterRender(Event e) {
		boolean isPaging = "paging".equals(listbox.getMold());
		if (!isPaging) {
			listbox.renderAll();
		}
	}

	/**
	 * 
	 * @param multipleCheck
	 */
	private void onListboxMultipleChange(Checkbox multipleCheck) {
		listbox.setMultiple(multipleCheck.isChecked());
		listbox.setCheckmark(multipleCheck.isChecked());
	}

	/**
	 * 
	 * @param checkbox
	 */
	private void onColumnHiddenChange(Checkbox checkbox, int column) {
		Listheader col = (Listheader) listbox.getListhead().getChildren().get(column);
		col.setVisible(!checkbox.isChecked());
	}
}
