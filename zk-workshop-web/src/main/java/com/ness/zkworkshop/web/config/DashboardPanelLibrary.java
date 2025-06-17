package com.ness.zkworkshop.web.config;

import com.ness.zkworkshop.web.model.DashboardPanel;

import java.util.*;

/**
 * Knihovna panelu pro pridani na dashboard.
 * Pouziti pro init nastaveni a pridavani panelu.
 */
public final class DashboardPanelLibrary {

    private final SidebarPageConfig pageConfig = new SidebarPageConfigImpl();

    public enum WidgetType {
        DATA_GRID,
        CALENDAR_SIMPLE,
        MENU_ITEM,
        MODULE,
        CHART,
        MESSAGES,
        CALENDAR;
    }

    private Map<WidgetType, List<DashboardPanel>> dashboardPanelMap;

    public DashboardPanelLibrary() {
        initDashboardPanels();
    }

    private void initDashboardPanels() {
        dashboardPanelMap = new HashMap<>();
        // CALENDAR_SIMPLE
        dashboardPanelMap.put(WidgetType.CALENDAR_SIMPLE, Arrays.asList(new DashboardPanel(1, "Kalendář", "", "", "", WidgetType.CALENDAR_SIMPLE)));
        // DATA_GRID
        List<DashboardPanel> dataGridList = new ArrayList<>();
        dataGridList.add(new DashboardPanel(1, "Subjekty", "/pages/subjekt-list-core.zul", "", pageConfig.getPage("fn9").getUri(), WidgetType.DATA_GRID));
        dataGridList.add(new DashboardPanel(2, "Úkoly", "/pages/todolist-core.zul", "", pageConfig.getPage("fn3").getUri(), WidgetType.DATA_GRID));
        dataGridList.add(new DashboardPanel(3, "Hierarchie", "/pages/tree-core.zul", "", pageConfig.getPage("fn6").getUri(), WidgetType.DATA_GRID));
        dashboardPanelMap.put(WidgetType.DATA_GRID, dataGridList);
        // MENU_ITEM
        List<DashboardPanel> menuItemList = Arrays.asList(new DashboardPanel(1,"Subjekty", "", "", pageConfig.getPage("fn9").getUri(), WidgetType.MENU_ITEM),
                new DashboardPanel(2,"Úkoly", "", pageConfig.getPage("fn3").getUri(), "", WidgetType.MENU_ITEM));
        dashboardPanelMap.put(WidgetType.MENU_ITEM, menuItemList);
        // CHART
        dashboardPanelMap.put(WidgetType.CHART, Arrays.asList(new DashboardPanel(1, "Graf", "/pages/chart-core.zul", pageConfig.getPage("fn7").getUri(), "", WidgetType.CHART)));
        // MESSAGES
        dashboardPanelMap.put(WidgetType.MESSAGES, Arrays.asList(new DashboardPanel(1, "Správy", "/pages/messages-core.zul", "", "", WidgetType.MESSAGES)));
        // MODULE
        dashboardPanelMap.put(WidgetType.MODULE, Arrays.asList(new DashboardPanel(1, "Výkazy", "/pages/module-vykazy.zul", "", "/sources/imgs/doc.png", WidgetType.MODULE)));
        // CALENDAR
        dashboardPanelMap.put(WidgetType.CALENDAR, Arrays.asList(new DashboardPanel(1, "Kalendář událostí", "/pages/calendar-widget.zul", pageConfig.getPage("fn10").getUri(), "/sources/imgs/doc.png", WidgetType.CALENDAR)));
    }

    public int getDashWidgetIdx(DashboardPanel panel) {
        List<DashboardPanel> panelForTypeList = dashboardPanelMap.get(panel.getType());
        for (DashboardPanel dashPanel : panelForTypeList) {
            if (dashPanel.getId() == panel.getId()) {
                return panelForTypeList.indexOf(dashPanel);
            }
        }

        throw new IllegalStateException("DashboardPanelMap neobsahuje dashboardPanel type: " + panel.getType() + ", panelUri: " + panel.getPanelUri());
    }

    public Map<WidgetType, List<DashboardPanel>> getDashboardPanelMap() {
        return dashboardPanelMap;
    }
}
