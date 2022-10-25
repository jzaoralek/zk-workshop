package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.SidebarPageConfig;
import com.ness.zkworkshop.web.config.SidebarPageConfigImpl;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.zk.ui.Executions;

public class MyViewModel {

	//config provider
	private SidebarPageConfig pageConfig = new SidebarPageConfigImpl();

	private int count;

	@Init
	public void init() {
		count = 100;
	}

	@Command
	@NotifyChange("count")
	public void cmd() {
		++count;
	}
	
	@Command
	public void themeSelectedCmd() {
		Executions.sendRedirect("");	
	}

	public int getCount() {
		return count;
	}
}
