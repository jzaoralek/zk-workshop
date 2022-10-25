package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.SidebarPageConfig;
import com.ness.zkworkshop.web.config.SidebarPageConfigImpl;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

public class DashboardViewModel {

	//config provider
	private SidebarPageConfig pageConfig = new SidebarPageConfigImpl();

	boolean editMode;

	@NotifyChange("editMode")
	@Command
	public void editModeCmd() {
		this.editMode = true;
	}

	@NotifyChange("editMode")
	@Command
	public void saveCmd() {
		this.editMode = false;
		Clients.showNotification(Labels.getLabel("web.msg.info.changesSaved"),
				Clients.NOTIFICATION_TYPE_INFO,
				null,
				null,
				2000);
	}

	@Command
	public void addWidgetCmd() {
		EventQueueHelper.publish(EventQueueHelper.SdatEvent.ADD_WIDGET, null);
	}

	/**
     * Prechod na graf.
	 */
	@Command
	public void goToWeatherCmd() {
		redirectToPage("fn7");
	}

	/**
	 * Prechod na strom.
	 */
	@Command
	public void goToTreeCmd() {
		redirectToPage("fn6");
	}

	/**
	 * Prechod na grid.
	 */
	@Command
	public void goToGridCmd() {
		redirectToPage("fn3");
	}

	private void redirectToPage(String pageId) {
		Executions.sendRedirect(pageConfig.getPage(pageId).getUri());
	}

	public boolean isEditMode() {
		return editMode;
	}
}
