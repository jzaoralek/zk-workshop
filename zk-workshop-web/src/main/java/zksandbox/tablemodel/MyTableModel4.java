package zksandbox.tablemodel;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Hbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Vbox;

import zksandbox.dto.MyCustomDto;

public class MyTableModel4 extends TableModel<MyCustomDto> {

	/**
	 * 
	 */
	public MyTableModel4() {
		initModel();
	}

	@Override
	protected List<Column> buildColumns() {
		List<Column> columns = new ArrayList<>();
		columns.add(Column.hflexColumn("Column 1 (min)", "min"));
		columns.add(Column.fixColumn("Column 2", "300px"));
		columns.add(Column.fixColumn("Column 3", "300px"));
		columns.add(Column.fixColumn("Column 4 (fixed)", "300px"));
		columns.add(Column.fixColumn("Column 5", "300px"));
		columns.add(Column.fixColumn("Column 6", "300px"));
		columns.add(Column.fixColumn("Column 7", "300px"));
		columns.add(Column.fixColumn("Column 8", "300px"));
		columns.add(Column.fixColumn("Column 9", "300px"));
		columns.add(Column.fixColumn("Column 10", "300px"));
		columns.add(Column.fixColumn("Column 11", "300px"));
		columns.add(Column.fixColumn("Column 12", "300px"));
		columns.add(Column.fixColumn("Column 13", "300px"));
		columns.add(Column.fixColumn("Column 14", "300px"));
		columns.add(Column.fixColumn("Column 15", "300px"));
		columns.add(Column.fixColumn("Column 16", "300px"));
		columns.add(Column.fixColumn("Column 17", "300px"));
		columns.add(Column.fixColumn("Column 18", "300px"));
		columns.add(Column.fixColumn("Column 19", "300px"));

		return columns;
	}

	@Override
	protected List<MyCustomDto> buildRows() {
		int records = 10;

		List<MyCustomDto> rows = new ArrayList<>(records);
		for (int i = 0; i < records; i++) {
			MyCustomDto row = MyCustomDto.defaultInstance();
			row.setCol1("Row index: " + i);
			rows.add(row);
		}

		return rows;
	}

	private Listcell createComplicatedListcell(String label) {
		//Component[] c = new Component[]{new Vbox(new Component[]{new Label(label)})};
		Hbox h = new Hbox(new Component[] { new Vbox(new Component[] { new Hbox(new Component[] { new Vbox(new Component[] { new Label(label) }) }) }) });

		Listcell listcell = new Listcell();
		listcell.appendChild(h);
		return listcell;
	}

	@Override
	protected Object renderRowItem(int columnIndex, Column column, MyCustomDto rowData) {
		switch (columnIndex) {
			case 0:
				return createComplicatedListcell(rowData.getCol1());
			case 1:
				return createComplicatedListcell(rowData.getCol2());
			case 2:
				return createComplicatedListcell(rowData.getCol3());
			case 3:
				return createComplicatedListcell(rowData.getCol4());
			case 4:
				return createComplicatedListcell(rowData.getCol5());
			case 5:
				return createComplicatedListcell(rowData.getCol6());
			case 6:
				return createComplicatedListcell(rowData.getCol7());
			default:
				return createComplicatedListcell(rowData.getCol7());
		}
	}

}
