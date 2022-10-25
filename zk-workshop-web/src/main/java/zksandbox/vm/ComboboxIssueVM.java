package zksandbox.vm;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

public class ComboboxIssueVM {

	private List<DataItem> dataList;
	private DataItem selected;

	@Init
	private void init() {
		dataList = new ArrayList<DataItem>();
		dataList.add(new DataItem("Test 1"));
		dataList.add(new DataItem("Test 2"));
		dataList.add(new DataItem("Test 3 ERROR  ")); // last char is "space"
		dataList.add(new DataItem("Test 4"));
	}

	@Command
	public void dataItemSelectedCmd() {
		System.out.println("dataItemSelectedCmd: " + selected);
	}

	public List<DataItem> getDataList() {
		return dataList;
	}

	public void setDataList(List<DataItem> dataList) {
		this.dataList = dataList;
	}

	public DataItem getSelected() {
		return selected;
	}

	public void setSelected(DataItem selected) {
		this.selected = selected;
	}

	/**
	 * 
	 *
	 */
	public static class DataItem {

		private String label;

		public DataItem(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		@Override
		public String toString() {
			return label;
		}

	}
}
