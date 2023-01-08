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
import java.util.List;

/**
 * VM pro zalozeni noveho dashboardu.
 */
public class DashboardCreateModalVM extends BaseVM {

    private DashboardService dashboardService = new DashboardServiceSessionImpl();

    private int cols = 1;
    private String name;
    /** Dashboard ke kopirovani. */
    private DashboardConfig dashboardSrc;
    private boolean copyMode;
    private DashboardServiceSessionImpl.DashboardType dashboardType;
    /** Zalozeni default dashboardu. */
    private Boolean adminMode;

    @Init
    public void init() {
        Object dashSrcObj = Executions.getCurrent().getArg().get("dashboardSrc");
        if (dashSrcObj != null) {
            dashboardSrc = (DashboardConfig)dashSrcObj;
            cols = dashboardSrc.getCols();
            copyMode = true;
        }

        Object dashboardTypeObj = Executions.getCurrent().getArg().get("dashboardType");
        dashboardType = dashboardTypeObj != null ? (DashboardServiceSessionImpl.DashboardType)dashboardTypeObj : DashboardServiceSessionImpl.DashboardType.INT;

        Object adminModeObj = Executions.getCurrent().getArg().get("adminMode");
        adminMode = adminModeObj != null ? (Boolean)adminModeObj : false;
    }

    @Command
    public void createCmd(@BindingParam("modal") Window modal) {
        Long newDashboardId = null;
        if (!copyMode) {
            if (!adminMode) {
                // vytvareni noveho custom dashboardu
                newDashboardId = dashboardService.createDashboard(name, cols, new ArrayList<DashboardPanelConfig>(), dashboardType);
            } else {
                // zalozeni default dashboardu
                newDashboardId = dashboardService.createDefaultDashboard(name, cols, new ArrayList<DashboardPanelConfig>(), dashboardType);
            }
        } else {
            // rezim kopirovani dashboardu
            // panely defaultne odstranitelne
            List<DashboardPanelConfig> copyPnlCfgList = dashboardSrc.copyPanelConfigList();
            copyPnlCfgList.forEach(i -> i.setRemovable(true));
            newDashboardId = dashboardService.createDashboard(name, dashboardSrc.getCols(), copyPnlCfgList, dashboardType);
        }

        Clients.showNotification(Labels.getLabel("web.msg.info.changesSaved"),
                Clients.NOTIFICATION_TYPE_INFO,
                null,
                null,
                2000);

        if (!adminMode) {
            // redirect to new dashboard
            Executions.sendRedirect("/index.zul?dashboardId=" + newDashboardId);
        } else {
            // vytvoreni default dashboardum pouze reload
            Executions.sendRedirect("");
        }
    }

    public String getTitle() {
        if (!copyMode) {
            return "Vytvoření nového dashboardu";
        } else {
            return "Vytvoření kopie dashboardu";
        }
    }

    public int getCols() {
        return cols;
    }

    public boolean isCopyMode() {
        return copyMode;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}