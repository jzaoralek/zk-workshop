package zksandbox.vm;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

public class TestPageVM {

	@Init
	private void init() {

	}

	@Command
	@NotifyChange("openTestModal")
	public void openTestModal() {
		Window window = (Window) Executions.createComponents("/pages/test-modal.zul", null, null);
		window.doModal();
	}
}
