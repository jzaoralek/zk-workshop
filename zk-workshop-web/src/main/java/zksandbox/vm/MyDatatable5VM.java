package zksandbox.vm;

import org.zkoss.bind.annotation.Init;

import zksandbox.tablemodel.MyTableModel5;

public class MyDatatable5VM {

	private MyTableModel5 tableModel;

	@Init
	private void init() {
		tableModel = new MyTableModel5();
	}

	public MyTableModel5 getTableModel() {
		return tableModel;
	}

}
