package zksandbox.vm;

import java.util.ArrayList;
import java.util.List;

import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import zksandbox.dto.TabItem;

public class TabboxIssueVM {

	private List<TabItem> data;
	private TabItem selectedTab;

	@Init
	private void init() {
		initData(70);
	}

	private void initData(int dataCount) {
		data = new ArrayList<>(dataCount);
		for (int i = 0; i < dataCount; i++) {
			data.add(new TabItem("Tab " + (i + 1), "data " + i));
		}
		selectedTab = data.get(0);
	}

	@Command
	public void selectFirstCmd() {
		selectedTab = data.get(0);
		BindUtils.postNotifyChange(null, null, this, "selectedTab");
	}

	@Command
	public void selectLastCmd() {
		selectedTab = data.get(data.size() - 1);
		BindUtils.postNotifyChange(null, null, this, "selectedTab");
	}

	public List<TabItem> getData() {
		return data;
	}

	public TabItem getSelectedTab() {
		return selectedTab;
	}

	public void setSelectedTab(TabItem selectedTab) {
		this.selectedTab = selectedTab;
	}

}
