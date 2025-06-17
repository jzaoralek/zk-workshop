package zksandbox.vm;

import org.zkoss.zul.ListModel;
import zksandbox.tablemodel.FakeListModel;

public class LoadOnDemandViewModel  {

    private ListModel<String> hugeList = new FakeListModel(250000);

    public ListModel<String> getHugeList() {
        return hugeList;
    }
}