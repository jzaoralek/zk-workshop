package zksandbox.vm;

import org.zkoss.bind.annotation.Init;

import zksandbox.model.tree.Hier;
import zksandbox.model.tree.StringTreeElement;

public class TreeToListboxDemoVM {

	@Init
	public void init() {
		Hier<StringTreeElement> hier = new Hier<StringTreeElement>();
	}
}
