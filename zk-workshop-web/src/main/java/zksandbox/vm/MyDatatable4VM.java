package zksandbox.vm;

import org.zkoss.bind.annotation.Init;

import zksandbox.tablemodel.MyTableModel4;

public class MyDatatable4VM {

	private MyTableModel4 tableModel;

	@Init
	private void init() {
		tableModel = new MyTableModel4();
	}

	public MyTableModel4 getTableModel() {
		return tableModel;
	}

}
