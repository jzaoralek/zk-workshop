package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import java.util.*;

public class AddWidgetModalVM extends BaseVM {

    public enum WidgetType {
        DATA_GRID,
        CALENDAR_EVENTS,
        CALENDAR_SIMPLE,
        SPREADSHEET,
        MENU_ITEM,
        DETAIL,
        MESSAGES,
        NOTIFICATIONS,
        DOCUMENT;
    }

    private String modalArg;
    private AddWidgetModalVM.WidgetType widgetTypeSelected;
    private Map<WidgetType, List<DashboardPanel>> dashboardPanelMap;
    private DashboardPanel dashboardPanelSelected;

    @Init
    public void init() {
        modalArg = (String)Executions.getCurrent().getArg().get("modalArg");
        widgetTypeSelected = AddWidgetModalVM.WidgetType.valueOf(modalArg);
        initDashboardPanels();
    }

    /**
     * Konfigurace, bude nahrazeno nactenim z databaze.
     */
    private void initDashboardPanels() {
        dashboardPanelMap = new HashMap<>();
        // CALENDAR_SIMPLE
        dashboardPanelMap.put(WidgetType.CALENDAR_SIMPLE, Arrays.asList(new DashboardPanel("Kalendář", "", "", WidgetType.CALENDAR_SIMPLE)));
        // DATA_GRID
        List<DashboardPanel> dataGridList = new ArrayList<>();
        dataGridList.add(new DashboardPanel("Subjekty", "/pages/subjekt-list-core.zul", pageConfig.getPage("fn9").getUri(), WidgetType.DATA_GRID));
        dataGridList.add(new DashboardPanel("Úkoly", "/pages/todolist-core.zul", pageConfig.getPage("fn3").getUri(), WidgetType.DATA_GRID));
        dashboardPanelMap.put(WidgetType.DATA_GRID, dataGridList);
        // MENU_ITEM
        dashboardPanelMap.put(WidgetType.MENU_ITEM, Arrays.asList(new DashboardPanel("Subjekty", "", pageConfig.getPage("fn9").getUri(), WidgetType.MENU_ITEM)));

        changeWidgetTypeCmd();
    }

    @Command
    public void changeWidgetTypeCmd() {
        dashboardPanelSelected = dashboardPanelMap.get(widgetTypeSelected).get(0);
    }

    @Command
    public void addWidgetCmd(@BindingParam("modal") Window modal) {
        EventQueueHelper.publish(EventQueueHelper.SdatEvent.ADD_WIDGET, dashboardPanelSelected);
        closeModalCmd(modal);
    }

    @DependsOn("widgetTypeSelected")
    public boolean isTypeSupported() {

        return EnumSet.of(WidgetType.CALENDAR_SIMPLE, WidgetType.DATA_GRID, WidgetType.MENU_ITEM).contains(widgetTypeSelected);
    }

    /**
     * Nabidka konkretnich panelu k pridani podle vybraneho typu.
     * @return
     */
    @DependsOn("widgetTypeSelected")
    public List<DashboardPanel> getDashboardPanelListByType() {
        return dashboardPanelMap.get(widgetTypeSelected);
    }

    public String getModalArg() {
        return modalArg;
    }

    public WidgetType getWidgetTypeSelected() {
        return widgetTypeSelected;
    }

    public void setWidgetTypeSelected(WidgetType widgetTypeSelected) {
        this.widgetTypeSelected = widgetTypeSelected;
    }

    public DashboardPanel getDashboardPanelSelected() {
        return dashboardPanelSelected;
    }

    public void setDashboardPanelSelected(DashboardPanel dashboardPanelSelected) {
        this.dashboardPanelSelected = dashboardPanelSelected;
    }
}
