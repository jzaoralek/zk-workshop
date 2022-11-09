package com.ness.zkworkshop.web.config;

import com.ness.zkworkshop.web.model.DashboardPanel;

import java.util.*;

/**
 * Konfigurace panelu dashboardu.
 * Pouziti pro init nastaveni a pridavani panelu.
 */
public final class DashboardPanelConfig {

    protected SidebarPageConfig pageConfig = new SidebarPageConfigImpl();

    public enum WidgetType {
        DATA_GRID,
        CALENDAR_SIMPLE,
        MENU_ITEM,
        CHART,
        MESSAGES;
        /*
        NOTIFICATIONS,
        CALENDAR_EVENTS,
        SPREADSHEET,
        DETAIL,
        DOCUMENT;
         */
    }

    private Map<WidgetType, List<DashboardPanel>> dashboardPanelMap;

    public DashboardPanelConfig() {
        initDashboardPanels();
    }

    private void initDashboardPanels() {
        dashboardPanelMap = new HashMap<>();
        // CALENDAR_SIMPLE
        dashboardPanelMap.put(WidgetType.CALENDAR_SIMPLE, Arrays.asList(new DashboardPanel("Kalendář", "", "", WidgetType.CALENDAR_SIMPLE)));
        // DATA_GRID
        List<DashboardPanel> dataGridList = new ArrayList<>();
        dataGridList.add(new DashboardPanel("Subjekty", "/pages/subjekt-list-core.zul", pageConfig.getPage("fn9").getUri(), WidgetType.DATA_GRID));
        dataGridList.add(new DashboardPanel("Úkoly", "/pages/todolist-core.zul", pageConfig.getPage("fn3").getUri(), WidgetType.DATA_GRID));
        dataGridList.add(new DashboardPanel("Hierarchie", "/pages/tree-core.zul", pageConfig.getPage("fn6").getUri(), WidgetType.DATA_GRID));
        dashboardPanelMap.put(WidgetType.DATA_GRID, dataGridList);
        // MENU_ITEM
        List<DashboardPanel> menuItemList = Arrays.asList(new DashboardPanel("Subjekty", "", pageConfig.getPage("fn9").getUri(), WidgetType.MENU_ITEM),
                new DashboardPanel("Úkoly", "", pageConfig.getPage("fn3").getUri(), WidgetType.MENU_ITEM));
        dashboardPanelMap.put(WidgetType.MENU_ITEM, menuItemList);
        // CHART
        dashboardPanelMap.put(WidgetType.CHART, Arrays.asList(new DashboardPanel("Graf", "/pages/chart-core.zul", pageConfig.getPage("fn7").getUri(), WidgetType.CHART)));
        // MESSAGES
        dashboardPanelMap.put(WidgetType.MESSAGES, Arrays.asList(new DashboardPanel("Správy", "/pages/messages-core.zul", "", WidgetType.MESSAGES)));
    }

    public Map<WidgetType, List<DashboardPanel>> getDashboardPanelMap() {
        return dashboardPanelMap;
    }
}
