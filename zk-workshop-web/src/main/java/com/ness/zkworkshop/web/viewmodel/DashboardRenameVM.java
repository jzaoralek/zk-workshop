package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.service.DashboardService;
import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Window;

import java.util.ArrayList;

public class DashboardRenameVM {

    private DashboardService dashboardService = new DashboardServiceSessionImpl();

    private DashboardConfig dashboardSrc;

    @Init
    public void init() {
        dashboardSrc = (DashboardConfig)Executions.getCurrent().getArg().get("dashboardSrc");
    }

    @Command
    public void renameCmd(@BindingParam("modal") Window modal) {
        dashboardService.updateDashboard(dashboardSrc.getId(), dashboardSrc);
        // redirect to new dashboard
        Executions.sendRedirect("index.zul?dashboardId=" + dashboardSrc.getId());
    }

    public DashboardConfig getDashboardSrc() {
        return dashboardSrc;
    }
}