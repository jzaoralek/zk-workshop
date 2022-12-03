package com.ness.zkworkshop.web.service;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service pro nacitani CRUD v session.
 * Bude nahrazena service pracujici s DAO.
 */
public class DashboardServiceSessionImpl implements DashboardService {

    public static final Long DEFAULT_DASHBOARD_ID = 0L;
    private static final String DASHBOARD_CFG_MAP = "dashboardConfigMap";

    public DashboardServiceSessionImpl() {
        Object dashCfgMapObj = Executions.getCurrent().getSession().getAttribute(DASHBOARD_CFG_MAP);
        if (dashCfgMapObj == null) {
            Map<Long, DashboardConfig> dashCfgMap = new HashMap<>();
            List<DashboardPanelConfig> defDashPanelConfigList = new ArrayList<>();
            defDashPanelConfigList.add(new DashboardPanelConfig(0, 0, DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE, 0, "Kalendář", "margin-bottom:10px"));
            defDashPanelConfigList.add(new DashboardPanelConfig(1, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0, "Správy", "margin-bottom:10px") );
            dashCfgMap.put(0L, new DashboardConfig(DEFAULT_DASHBOARD_ID, "Výchozí", 4, defDashPanelConfigList));

            List<DashboardPanelConfig> custDashPanelConfigList = new ArrayList<>();
            custDashPanelConfigList.add(new DashboardPanelConfig(2, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0, "Správy", "margin-bottom:10px") );
            dashCfgMap.put(1L, new DashboardConfig(1L, "Custom", 3, custDashPanelConfigList));

            storeDashboardCfgMapToSession(dashCfgMap);
        }
    }

    private Map<Long, DashboardConfig> getDashboardCfgMapSession() {
        return (Map<Long, DashboardConfig>)Executions.getCurrent().getSession().getAttribute(DASHBOARD_CFG_MAP);
    }

    private void storeDashboardCfgMapToSession(Map<Long, DashboardConfig> dashCfgMap) {
        Executions.getCurrent().getSession().setAttribute(DASHBOARD_CFG_MAP, dashCfgMap);
    }

    @Override
    public DashboardConfig getDashboard(Long dashboardId) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession();
        DashboardConfig ret = dashCfgMap.get(dashboardId);
        if (ret == null) {
            // nenalezeno v konfiguraci, vraci defaault dashboard
            return dashCfgMap.get(DEFAULT_DASHBOARD_ID);
        }
        return ret;
    }

    @Override
    public List<DashboardConfig> getDashboardAll() {
        return new ArrayList<>(getDashboardCfgMapSession().values());
    }

    @Override
    public Long createDashboard(DashboardConfig dashCfg) {
        return null;
    }

    @Override
    public void updateDashboard(Long dashboardId, DashboardConfig dashCfg) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession();
        dashCfgMap.remove(dashboardId);
        dashCfgMap.put(dashboardId, dashCfg);
        storeDashboardCfgMapToSession(dashCfgMap);
    }

    @Override
    public void deleteDashboard(Long dashboardId) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession();
        dashCfgMap.remove(dashboardId);
        storeDashboardCfgMapToSession(dashCfgMap);
    }
}