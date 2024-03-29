package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.javatuples.Pair;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import java.util.*;

public class AddWidgetModalVM extends BaseVM {

    private String modalArg;
    private DashboardPanelLibrary.WidgetType widgetTypeSelected;
    private Map<DashboardPanelLibrary.WidgetType, List<DashboardPanel>> dashboardPanelMap;
    private DashboardPanel dashboardPanelSelected;
    private DashboardPanelLibrary dashboardPanelLibrary = new DashboardPanelLibrary();
    private DashboardServiceSessionImpl.DashboardType dashboardType;

    @Init
    public void init() {
        modalArg = (String)Executions.getCurrent().getArg().get("modalArg");
        widgetTypeSelected = DashboardPanelLibrary.WidgetType.valueOf(modalArg);
        dashboardType = DashboardServiceSessionImpl.DashboardType.valueOf((String)Executions.getCurrent().getArg().get("dashboardType"));
        dashboardPanelMap = dashboardPanelLibrary.getDashboardPanelMap();
        changeWidgetTypeCmd();
    }

    @Command
    public void changeWidgetTypeCmd() {
        dashboardPanelSelected = dashboardPanelMap.get(widgetTypeSelected).get(0);
    }

    @Command
    public void addWidgetCmd(@BindingParam("modal") Window modal) {
        EventQueueHelper.publish(EventQueueHelper.SdatEvent.ADD_WIDGET, Pair.with(dashboardPanelSelected, dashboardType));
        closeModalCmd(modal);
    }

    @DependsOn("widgetTypeSelected")
    public boolean isTypeSupported() {
        return EnumSet.of(DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE,
                    DashboardPanelLibrary.WidgetType.CALENDAR,
                    DashboardPanelLibrary.WidgetType.DATA_GRID,
                    DashboardPanelLibrary.WidgetType.MENU_ITEM,
                    DashboardPanelLibrary.WidgetType.CHART,
                    DashboardPanelLibrary.WidgetType.MESSAGES,
                    DashboardPanelLibrary.WidgetType.MODULE).contains(widgetTypeSelected);
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

    public DashboardPanelLibrary.WidgetType getWidgetTypeSelected() {
        return widgetTypeSelected;
    }

    public void setWidgetTypeSelected(DashboardPanelLibrary.WidgetType widgetTypeSelected) {
        this.widgetTypeSelected = widgetTypeSelected;
    }

    public DashboardPanel getDashboardPanelSelected() {
        return dashboardPanelSelected;
    }

    public void setDashboardPanelSelected(DashboardPanel dashboardPanelSelected) {
        this.dashboardPanelSelected = dashboardPanelSelected;
    }
}