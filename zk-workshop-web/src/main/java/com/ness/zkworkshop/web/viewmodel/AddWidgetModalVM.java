package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.DependsOn;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;

import java.util.*;

public class AddWidgetModalVM extends BaseVM {

    private String modalArg;
    private DashboardPanelConfig.WidgetType widgetTypeSelected;
    private Map<DashboardPanelConfig.WidgetType, List<DashboardPanel>> dashboardPanelMap;
    private DashboardPanel dashboardPanelSelected;
    private DashboardPanelConfig dashboardPanelConfig = new DashboardPanelConfig();

    @Init
    public void init() {
        modalArg = (String)Executions.getCurrent().getArg().get("modalArg");
        widgetTypeSelected = DashboardPanelConfig.WidgetType.valueOf(modalArg);
        dashboardPanelMap = dashboardPanelConfig.getDashboardPanelMap();
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
        return EnumSet.of(DashboardPanelConfig.WidgetType.CALENDAR_SIMPLE,
                    DashboardPanelConfig.WidgetType.DATA_GRID,
                    DashboardPanelConfig.WidgetType.MENU_ITEM,
                    DashboardPanelConfig.WidgetType.CHART,
                    DashboardPanelConfig.WidgetType.MESSAGES).contains(widgetTypeSelected);
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

    public DashboardPanelConfig.WidgetType getWidgetTypeSelected() {
        return widgetTypeSelected;
    }

    public void setWidgetTypeSelected(DashboardPanelConfig.WidgetType widgetTypeSelected) {
        this.widgetTypeSelected = widgetTypeSelected;
    }

    public DashboardPanel getDashboardPanelSelected() {
        return dashboardPanelSelected;
    }

    public void setDashboardPanelSelected(DashboardPanel dashboardPanelSelected) {
        this.dashboardPanelSelected = dashboardPanelSelected;
    }
}