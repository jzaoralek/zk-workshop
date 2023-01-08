package com.ness.zkworkshop.web.service;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import org.zkoss.zk.ui.Executions;

import java.util.*;

/**
 * Service pro nacitani CRUD v session.
 * Bude nahrazena service pracujici s DAO.
 */
public class DashboardServiceSessionImpl implements DashboardService {

    public enum DashboardType {
        INT,
        ZBER;
    }
    public static final Long DEFAULT_DASHBOARD_ID = 0L;
    private static final String DASHBOARD_INT_CFG_MAP = "dashboardConfigMap";
    private static final String DASHBOARD_ZBER_CFG_MAP = "dashboardZberConfigMap";

    public DashboardServiceSessionImpl() {
        // Iniciace - INT portal
        Object dashCfgIntMapObj = Executions.getCurrent().getSession().getAttribute(DASHBOARD_INT_CFG_MAP);
        if (dashCfgIntMapObj == null) {
            Map<Long, DashboardConfig> dashCfgMap = new HashMap<>();
            List<DashboardPanelConfig> defDashPanelConfigList = new ArrayList<>();
            defDashPanelConfigList.add(new DashboardPanelConfig(0, 0, DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE, 0, "Kalendář", "margin-bottom:10px", false));
            defDashPanelConfigList.add(new DashboardPanelConfig(1, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0, "Správy", "margin-bottom:10px", false) );
            dashCfgMap.put(DEFAULT_DASHBOARD_ID, new DashboardConfig(DEFAULT_DASHBOARD_ID, "Výchozí dashboard", 4, defDashPanelConfigList));

            storeDashboardCfgMapToSession(dashCfgMap, DashboardType.INT);
        }

        // Iniciace - ZBER portal
        Object dashCfgZberMapObj = Executions.getCurrent().getSession().getAttribute(DASHBOARD_ZBER_CFG_MAP);
        if (dashCfgZberMapObj == null) {
            Map<Long, DashboardConfig> dashCfgMap = new HashMap<>();
            List<DashboardPanelConfig> defDashPanelConfigList = new ArrayList<>();
            defDashPanelConfigList.add(new DashboardPanelConfig(0, 0, DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE, 0, "Kalendář", "margin-bottom:10px", false));
            defDashPanelConfigList.add(new DashboardPanelConfig(1, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0, "Správy", "margin-bottom:10px", false) );
            dashCfgMap.put(DEFAULT_DASHBOARD_ID, new DashboardConfig(DEFAULT_DASHBOARD_ID, "Výchozí dashboard", 4, defDashPanelConfigList));

            storeDashboardCfgMapToSession(dashCfgMap, DashboardType.ZBER);
        }
    }

    private Map<Long, DashboardConfig> getDashboardCfgMapSession(DashboardType type) {
        if (type == DashboardType.INT) {
            return (Map<Long, DashboardConfig>)Executions.getCurrent().getSession().getAttribute(DASHBOARD_INT_CFG_MAP);
        } else {
            return (Map<Long, DashboardConfig>)Executions.getCurrent().getSession().getAttribute(DASHBOARD_ZBER_CFG_MAP);
        }
    }

    private void storeDashboardCfgMapToSession(Map<Long, DashboardConfig> dashCfgMap, DashboardType type) {
        if (type == DashboardType.INT) {
            Executions.getCurrent().getSession().setAttribute(DASHBOARD_INT_CFG_MAP, dashCfgMap);
        } else {
            Executions.getCurrent().getSession().setAttribute(DASHBOARD_ZBER_CFG_MAP, dashCfgMap);
        }
    }

    @Override
    public DashboardConfig getDashboard(Long dashboardId, DashboardType type) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession(type);
        DashboardConfig ret = dashCfgMap.get(dashboardId);
        if (ret == null) {
            // nenalezeno v konfiguraci, vraci defaault dashboard
            return dashCfgMap.get(DEFAULT_DASHBOARD_ID);
        }
        return ret;
    }

    @Override
    public boolean isDefaultPresent(DashboardType type) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession(type);
        DashboardConfig ret = dashCfgMap.get(DEFAULT_DASHBOARD_ID);
        return ret != null;
    }

    @Override
    public List<DashboardConfig> getDashboardAll(DashboardType type) {
        return new ArrayList<>(getDashboardCfgMapSession(type).values());
    }

    @Override
    public Long createDashboard(String name, int cols, List<DashboardPanelConfig> panelConfigList, DashboardType type) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession(type);
        // generovani noveho ID
        Optional<Long> maxIdOpt = dashCfgMap.keySet().stream().max(Long::compareTo);
        Long newId = null;
        if (maxIdOpt.isPresent()) {
            newId = maxIdOpt.get() + 1;
            dashCfgMap.put(newId, new DashboardConfig(newId, name, cols, panelConfigList));
            storeDashboardCfgMapToSession(dashCfgMap, type);
            return newId;
        }

        return -1L;
    }

    public Long createDefaultDashboard(String name, int cols, List<DashboardPanelConfig> panelConfigList, DashboardServiceSessionImpl.DashboardType type) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession(type);
        dashCfgMap.put(DEFAULT_DASHBOARD_ID, new DashboardConfig(DEFAULT_DASHBOARD_ID, name, cols, panelConfigList));
        storeDashboardCfgMapToSession(dashCfgMap, type);
        return DEFAULT_DASHBOARD_ID;
    }

    @Override
    public void updateDashboard(Long dashboardId, DashboardConfig dashCfg, DashboardType type) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession(type);
        dashCfgMap.remove(dashboardId);
        dashCfgMap.put(dashboardId, dashCfg);
        storeDashboardCfgMapToSession(dashCfgMap, type);
    }

    @Override
    public void deleteDashboard(Long dashboardId, DashboardType type) {
        Map<Long, DashboardConfig> dashCfgMap = getDashboardCfgMapSession(type);
        dashCfgMap.remove(dashboardId);
        storeDashboardCfgMapToSession(dashCfgMap, type);
    }
}