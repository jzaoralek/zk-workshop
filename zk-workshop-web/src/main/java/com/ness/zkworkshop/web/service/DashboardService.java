package com.ness.zkworkshop.web.service;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;

import java.util.List;

public interface DashboardService {
    /**
     * Vraci dashboard podle ID.
     * Pokud nenalezen vraci DEFAULT dashboard.
     * @param dashboardId
     * @return
     */
    DashboardConfig getDashboard(Long dashboardId);
    List<DashboardConfig> getDashboardAll();
    Long createDashboard(String name, int cols, List<DashboardPanelConfig> panelConfigList);
    void updateDashboard(Long dashboardId, DashboardConfig dashCfg);
    void deleteDashboard(Long dashboardId);
}
