package zksandbox.vm;

import org.zkoss.zul.ListModel;
import zksandbox.tablemodel.FakeListModel;

public class LoadOnDemandViewModel  {

    private ListModel<String> hugeList = new FakeListModel(30000);

    public ListModel<String> getHugeList() {
        return hugeList;
    }
}