package zksandbox.vm;

import org.zkoss.bind.annotation.Init;

import zksandbox.tablemodel.MyTableModel2;

public class MyDatatableVM2 {

	private MyTableModel2 tableModel;

	@Init
	private void init() {
		tableModel = new MyTableModel2();
	}

	public MyTableModel2 getTableModel() {
		return tableModel;
	}

}
