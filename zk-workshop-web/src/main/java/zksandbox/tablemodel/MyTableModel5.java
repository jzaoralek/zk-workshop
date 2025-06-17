package zksandbox.tablemodel;

import java.util.ArrayList;
import java.util.List;

import zksandbox.dto.MyCustomDto2;

public class MyTableModel5 extends TableModel<MyCustomDto2> {

	/**
	 *
	 */
	public MyTableModel5() {
		initModel();
	}

	@Override
	protected List<Column> buildColumns() {
		List<Column> columns = new ArrayList<>();
		columns.add(Column.hflexColumn("Column 1 (min)", "min"));
		columns.add(Column.hflexColumn("Column 2", "1"));
		columns.add(Column.hflexColumn("Column 3", "1"));
		columns.add(Column.fixColumn("Column 4 (fixed)", "300px"));
		columns.add(Column.hflexColumn("Column 5", "min"));
		columns.add(Column.hflexColumn("Column 6", "min"));
		columns.add(Column.hflexColumn("Column 7", "min"));
		columns.add(Column.hflexColumn("Column 8", "min"));
		columns.add(Column.hflexColumn("Column 9", "min"));
		columns.add(Column.hflexColumn("Column 10", "min"));

		columns.add(Column.hflexColumn("Column 11", "min"));
		columns.add(Column.hflexColumn("Column 12", "min"));
		columns.add(Column.hflexColumn("Column 13", "min"));
		columns.add(Column.hflexColumn("Column 14", "min"));
		columns.add(Column.hflexColumn("Column 15", "min"));

		columns.add(Column.hflexColumn("Column 16", "min"));
		columns.add(Column.hflexColumn("Column 17", "min"));
		columns.add(Column.hflexColumn("Column 18", "min"));
		columns.add(Column.hflexColumn("Column 19", "min"));
		columns.add(Column.hflexColumn("Column 20", "min"));

		columns.add(Column.hflexColumn("Column 21", "min"));
		columns.add(Column.hflexColumn("Column 22", "min"));
		columns.add(Column.hflexColumn("Column 23", "min"));
		columns.add(Column.hflexColumn("Column 24", "min"));
		columns.add(Column.hflexColumn("Column 25", "min"));

		columns.add(Column.hflexColumn("Column 26", "min"));
		columns.add(Column.hflexColumn("Column 27", "min"));
		columns.add(Column.hflexColumn("Column 28", "min"));
		columns.add(Column.hflexColumn("Column 29", "min"));
		columns.add(Column.hflexColumn("Column 30", "min"));

		return columns;
	}

	@Override
	protected List<MyCustomDto2> buildRows() {
		int records = 500;

		List<MyCustomDto2> rows = new ArrayList<>(records);
		for (int i = 0; i < records; i++) {

			int cols = 30;
			MyCustomDto2 row = new MyCustomDto2(cols);

			for (int j = 0; j < cols; j++) {
				row.setColumnData(j, "col " + (j + 1));
			}
			row.setColumnData(0, "Row index " + (i + 1));
			rows.add(row);
		}

		return rows;
	}


	@Override
	protected Object renderRowItem(int columnIndex, Column column, MyCustomDto2 rowData) {
		return rowData.getColumnData(columnIndex);
	}

}
