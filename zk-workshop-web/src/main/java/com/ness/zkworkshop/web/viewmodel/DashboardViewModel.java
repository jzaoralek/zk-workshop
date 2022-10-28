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
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardViewModel extends BaseVM {

	private boolean editMode;
	private String borderColor;
	private String backgrColor;

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
	public void addWidgetModalCmd(@BindingParam("type") String type) {
		Map<String, Object> args = new HashMap<>();
		args.put("modalArg", type);
		openModal("/pages/add-widget-modal.zul", args);
	}

	/**
     * Prechod na graf.
	 */
	@Command
	public void goToWeatherCmd() {
		if (this.editMode) {
			return;
		}
		redirectToPage("fn7");
	}

	/**
	 * Prechod na strom.
	 */
	@Command
	public void goToTreeCmd() {
		if (this.editMode) {
			return;
		}
		redirectToPage("fn6");
	}

	/**
	 * Prechod na seznam subjektů.
	 */
	@Command
	public void goToSubjektListCmd() {
		if (this.editMode) {
			return;
		}
		redirectToPage("fn9");
	}

	/**
	 * Prechod na grid.
	 */
	@Command
	public void goToGridCmd() {
		if (this.editMode) {
			return;
		}
		redirectToPage("fn3");
	}

	@NotifyChange("backgrColor")
	@Command
	public void setBackgroundColorCmd(@BindingParam("event") InputEvent event) {
		this.backgrColor = "margin-bottom: 5px; background: " + event.getValue();
	}

	private void redirectToPage(String pageId) {
		Executions.sendRedirect(pageConfig.getPage(pageId).getUri());
	}

	public boolean isEditMode() {
		return editMode;
	}

	public String getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(String borderColor) {
		this.borderColor = borderColor;
	}

	public String getBackgrColor() {
		return backgrColor;
	}

	public void setBackgrColor(String backgrColor) {
		this.backgrColor = backgrColor;
	}
}
