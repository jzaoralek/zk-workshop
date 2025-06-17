package zksandbox.tablemodel;

import java.util.ArrayList;
import java.util.List;

import zksandbox.dto.MyCustomDto;

public class MyTableModel2 extends TableModel<MyCustomDto> {

	/**
	 * 
	 */
	public MyTableModel2() {
		initModel();
	}

	@Override
	protected List<Column> buildColumns() {
		List<Column> columns = new ArrayList<>();
		columns.add(Column.hflexColumn("Column 1 (min)", "min"));
		columns.add(Column.hflexColumn("Column 2", "1"));
		columns.add(Column.hflexColumn("Column 3", "1"));
		columns.add(Column.fixColumn("Column 4 (fixed)", "300px"));
		return columns;
	}

	@Override
	protected List<MyCustomDto> buildRows() {
		int records = 43;

		List<MyCustomDto> rows = new ArrayList<>(records);
		for (int i = 0; i < records; i++) {
			MyCustomDto row = MyCustomDto.defaultInstance();
			row.setCol1("Row index: " + i);
			rows.add(row);
		}

		return rows;
	}

	@Override
	protected Object renderRowItem(int columnIndex, Column column, MyCustomDto rowData) {
		switch (columnIndex) {
			case 0:
				return rowData.getCol1();
			case 1:
				return rowData.getCol2();
			case 2:
				return rowData.getCol3();
			case 3:
				return rowData.getCol4();

			default:
				return null;
		}
	}

}
