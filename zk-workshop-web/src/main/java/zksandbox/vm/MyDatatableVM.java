package zksandbox.vm;

import org.zkoss.bind.annotation.Init;

import zksandbox.tablemodel.MyTableModel;

public class MyDatatableVM {

	private MyTableModel tableModel;
	private String stateIcon;

	@Init
	private void init() {
		tableModel = new MyTableModel();

		stateIcon = "z-icon-thumbs-up";
	}

	public MyTableModel getTableModel() {
		return tableModel;
	}

	public String getStateIcon() {
		return stateIcon;
	}

	public void setStateIcon(String stateIcon) {
		this.stateIcon = stateIcon;
	}

}
