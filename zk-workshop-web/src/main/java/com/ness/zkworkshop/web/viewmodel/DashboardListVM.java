package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import com.ness.zkworkshop.web.service.DashboardService;
import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;
import com.ness.zkworkshop.web.util.DashboardUtils;
import org.zkoss.bind.BindUtils;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.NotifyChange;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

import java.util.List;
import java.util.stream.Collectors;

/**
 * VM pro zobrazeni seznamu dashboardu s moznosti prechodu a odstraneni.
 */
public class DashboardListVM extends BaseVM {

    private DashboardService dashboardService = new DashboardServiceSessionImpl();
    private List<DashboardConfig> dashboardConfigList;

    @Init
    public void init() {
        loadData();
    }

    private void loadData() {
        dashboardConfigList = dashboardService.getDashboardAll();
        BindUtils.postNotifyChange(this, "dashboardConfigList");
    }

    /**
     * Prechod na dashboard.
     * @param dashboardConfig
     */
    @Command
    public void goToDashboardCmd(@BindingParam("item") DashboardConfig dashboardConfig) {
        Executions.sendRedirect("index.zul?dashboardId=" + dashboardConfig.getId());
    }

    @NotifyChange("dashboardConfigList")
    @Command
    public void deleteDashboardCmd(@BindingParam("item") DashboardConfig dashboardConfig) {
        DashboardUtils.deleteDashboard(dashboardConfig, dashboardService, this::loadData);
    }

    public List<DashboardConfig> getDashboardConfigList() {
        return dashboardConfigList;
    }
}
