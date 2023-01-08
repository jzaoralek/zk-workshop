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
    DashboardConfig getDashboard(Long dashboardId, DashboardServiceSessionImpl.DashboardType type);
    boolean isDefaultPresent(DashboardServiceSessionImpl.DashboardType type);
    List<DashboardConfig> getDashboardAll(DashboardServiceSessionImpl.DashboardType type);
    Long createDashboard(String name, int cols, List<DashboardPanelConfig> panelConfigList, DashboardServiceSessionImpl.DashboardType type);
    Long createDefaultDashboard(String name, int cols, List<DashboardPanelConfig> panelConfigList, DashboardServiceSessionImpl.DashboardType type);
    void updateDashboard(Long dashboardId, DashboardConfig dashCfg, DashboardServiceSessionImpl.DashboardType type);
    void deleteDashboard(Long dashboardId, DashboardServiceSessionImpl.DashboardType type);
}
