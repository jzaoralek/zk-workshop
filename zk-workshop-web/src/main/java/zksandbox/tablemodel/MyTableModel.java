package zksandbox.tablemodel;

import java.util.ArrayList;
import java.util.List;

import zksandbox.dto.MyCustomDto;

public class MyTableModel extends TableModel<MyCustomDto> {

	private int defaultRecordsCount = 0;

	/**
	 * 
	 */
	public MyTableModel() {
		defaultRecordsCount = 50;
		initModel();
	}

	/**
	 * 
	 * @param defaultRecordsCount
	 */
	public MyTableModel(int defaultRecordsCount) {
		this.defaultRecordsCount = defaultRecordsCount;
		initModel();
	}


	@Override
	protected List<Column> buildColumns() {
		List<Column> columns = new ArrayList<>();
		//columns.add(Column.fixColumn("Column 1 (fixed)", "200px"));
		columns.add(Column.hflexColumn("Column 1 (min)", "min"));
		columns.add(Column.hflexColumn("Column 2", "1"));
		columns.add(Column.hflexColumn("Column 3", "1"));
		columns.add(Column.fixColumn("Column 4 (fixed)", "300px"));
		columns.add(Column.fixColumn("Column 5 (fixed)", "100px"));
		columns.add(Column.fixColumn("Column 6 (fixed)", "300px"));
		columns.add(Column.fixColumn("Column 7 (fixed)", "300px"));
		columns.add(Column.fixColumn("Column 8 (fixed)", "300px"));
		columns.add(Column.fixColumn("Column 9 (fixed)", "300px"));
		return columns;
	}

	@Override
	protected List<MyCustomDto> buildRows() {
		int records = defaultRecordsCount;

		List<MyCustomDto> rows = new ArrayList<>(records);
		for (int i = 0; i < records; i++) {
			MyCustomDto row = MyCustomDto.defaultInstance();
			row.setCol1("Row index: " + (i + 1));
			rows.add(row);
		}

		return rows;
	}

	/**
	 * 
	 * @param records
	 */
	public void generateNewRows(int records) {
		List<MyCustomDto> rows = new ArrayList<>(records);
		for (int i = 0; i < records; i++) {
			MyCustomDto row = MyCustomDto.defaultInstance();
			row.setCol1("Row index: " + (i + 1));
			rows.add(row);
		}
		setRows(rows);
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
			case 4:
				return rowData.getCol5();
			case 5:
				return rowData.getCol6();
			case 6:
				return rowData.getCol7();
			case 7:
				return rowData.getCol8();
			case 8:
				return rowData.getCol9();

			default:
				return null;
		}
	}

}
