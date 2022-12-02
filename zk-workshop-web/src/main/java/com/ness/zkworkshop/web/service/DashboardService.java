package com.ness.zkworkshop.web.service;

import com.ness.zkworkshop.web.config.DashboardConfig;

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
    Long createDashboard(DashboardConfig dashCfg);
    void updateDashboard(Long dashboardId, DashboardConfig dashCfg);
    void deleteDashboard(Long dashboardId);
}
