package zksandbox.vm;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;

import zksandbox.tablemodel.MyTableModel;

public class SplitPageVM {

	private MyTableModel tableModel;

	@Init
	private void init() {
		tableModel = new MyTableModel(0);
	}

	@Command
	@NotifyChange("tableModel")
	public void changeCountCmd(@BindingParam("count") Integer count) {
		tableModel.generateNewRows(count);
	}

	public MyTableModel getTableModel() {
		return tableModel;
	}

}
