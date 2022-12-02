package com.ness.zkworkshop.web.service;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardServiceImpl implements DashboardService {

    public static final Long DEFAULT_DASHBOARD_ID = 0L;
    private static final String DASHBOARD_CFG_MAP = "dashboardConfigMap";

    public DashboardServiceImpl() {
        Object dashCfgMapObj = Executions.getCurrent().getSession().getAttribute(DASHBOARD_CFG_MAP);
        if (dashCfgMapObj == null) {
            Map<Long, DashboardConfig> dashCfgMap = new HashMap<>();
            List<DashboardPanelConfig> defDashPanelConfigList = new ArrayList<>();
            defDashPanelConfigList.add(new DashboardPanelConfig(0, 0, DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE, 0, "Kalendář", "margin-bottom:10px"));
            defDashPanelConfigList.add(new DashboardPanelConfig(1, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0, "Správy", "margin-bottom:10px") );
            dashCfgMap.put(0L, new DashboardConfig("Výchozí", 4, defDashPanelConfigList));

            List<DashboardPanelConfig> custDashPanelConfigList = new ArrayList<>();
            custDashPanelConfigList.add(new DashboardPanelConfig(2, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0, "Správy", "margin-bottom:10px") );
            dashCfgMap.put(1L, new DashboardConfig("Custom", 3, custDashPanelConfigList));

            Executions.getCurrent().getSession().setAttribute(DASHBOARD_CFG_MAP, dashCfgMap);
        }
    }

    private Map<Long, DashboardConfig> getDashboardCfgMapSession() {
        return (Map<Long, DashboardConfig>)Executions.getCurrent().getSession().getAttribute(DASHBOARD_CFG_MAP);
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
        return null;
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
        Executions.getCurrent().getSession().setAttribute("dashboardConfigMap", dashCfgMap);
    }

    @Override
    public void deleteDashboard(Long dashboardId) {

    }
}
