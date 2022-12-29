package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.service.DashboardService;
import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;
import com.ness.zkworkshop.web.util.DashboardUtils;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.InputEvent;
import org.zkoss.zk.ui.util.Clients;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardViewModel extends BaseVM {

	private DashboardService dashboardService = new DashboardServiceSessionImpl();

	private boolean editMode;
	private String borderColor;
	private String backgrColor;
	private DashboardConfig dashboardSelected;
	private List<DashboardConfig> dashboardList;
	private Boolean adminMode;

	@Init
	public void init(@BindingParam("adminMode") Boolean adminMode) {
		this.adminMode = adminMode;
		this.dashboardSelected = dashboardService.getDashboard(DashboardUtils.getRequestDashboardId());
		this.dashboardList = dashboardService.getDashboardAll();
	}

	@NotifyChange("editMode")
	@Command
	public void editModeCmd() {
		this.editMode = true;
		postEditModeChange();
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
		postEditModeChange();
	}

	/**
	 * ODeslani zmeny editMode do PortalLayoutController.
	 */
	private void postEditModeChange() {
		EventQueueHelper.publish(EventQueueHelper.SdatEvent.EDIT_MODE, Boolean.valueOf(this.editMode));
	}

	@Command
	public void addWidgetModalCmd(@BindingParam("type") String type) {
		Map<String, Object> args = new HashMap<>();
		args.put("modalArg", type);
		openModal("/pages/add-widget-modal.zul", args);
	}

	@Command
	public void widgetListModalCmd() {
		openModal("/pages/dashboard-list.zul", null);
	}

	@Command
	public void createDashboardCmd(@BindingParam("copyMode") Boolean copyMode) {
		Map<String, Object> args = new HashMap<>();
		if (copyMode != null && copyMode) {
			args.put("dashboardSrc", dashboardSelected);

		}
		openModal("/pages/dashboard-create.zul", args);
	}

	/**
	 * Prechod na dashboard.
	 * @param id
	 */
	@Command
	public void goToDashboardCmd(@BindingParam("item") Long id) {
		Executions.sendRedirect("index.zul?dashboardId=" + id);
	}

	@Command
	public void renameDashboardCmd() {
		Map<String, Object> args = new HashMap<>();
		args.put("dashboardSrc", dashboardSelected);
		openModal("/pages/dashboard-rename.zul", args);
	}

	@Command
	public void deleteDashboardCmd() {
		if (dashboardSelected.isDefault()) {
			Clients.alert("Výchozí dashbord nelze odstranit.");
			return;
		}
		DashboardUtils.deleteDashboard(dashboardSelected, dashboardService, this::redirectToDefault);
	}

	@Command
	public void renameCmd() {
		dashboardService.updateDashboard(dashboardSelected.getId(), dashboardSelected);
		EventQueueHelper.publish(EventQueueHelper.SdatEvent.DASHBOARD_RENAME, dashboardSelected);
	}

	private void redirectToDefault() {
		Executions.sendRedirect("index.zul?dashboardId=" + DashboardServiceSessionImpl.DEFAULT_DASHBOARD_ID);
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

	public DashboardConfig getDashboardSelected() {
		return dashboardSelected;
	}

	public List<DashboardConfig> getDashboardList() {
		return dashboardList;
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
