package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import com.ness.zkworkshop.web.viewmodel.AddWidgetModalVM;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Calendar;
import org.zkoss.zul.Include;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PortalLayoutController extends SelectorComposer<Component> {

    private DashboardPanelConfig dashboardPanelConfig = new DashboardPanelConfig();

    @Wire
    private Portallayout portalLayout;

    private boolean editMode;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init((Boolean)editMode);

        // pridani widgetu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.ADD_WIDGET, data -> addWidget((DashboardPanel)data));
        // prepnuti do editacniho rezimu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.EDIT_MODE, data ->  updateEditMode((Boolean)data));
    }

    private void init(Boolean editMode) {
        this.editMode = editMode;
        portalLayout.setMaximizedMode("whole");

        // TODO: konfigurace dashboardu uživatele
        int cols = 4;
        String colWdth = 100/cols+"%";

        Portalchildren portalChildren = null;
        for (int i = 0; i < cols; i++) {
            portalChildren = new Portalchildren();
            portalChildren.setWidth(colWdth);
            portalLayout.appendChild(portalChildren);
        }

        // TODO: konfigurace dashboardu uzivatele
        addWidget(dashboardPanelConfig.getDashboardPanelMap().get(DashboardPanelConfig.WidgetType.CALENDAR_SIMPLE).get(0));
        addWidget(dashboardPanelConfig.getDashboardPanelMap().get(DashboardPanelConfig.WidgetType.DATA_GRID).get(1));
    }

    @Listen("onPortalMove = #portalLayout")
    public void saveStatus() {
        int i = 0;
        for (Component portalChild : portalLayout.getChildren()) {
            List<String> portletIds = new ArrayList<String>();
            for (Component portlet : portalChild.getChildren())
                portletIds.add(portlet.getId());
            Executions.getCurrent().getSession().setAttribute("PortalChildren" + i++, portletIds);
        }
    }

    @Listen("onPortalDrop = #portalLayout")
    public void dropStatus() {
        System.out.println("onPortalDrop()");
    }

    @Listen("onCreate = #portalLayout")
    public void initStatus() {
        List<? extends Component> panelchildren = portalLayout.getChildren();
        for (int i = 0; i < panelchildren.size(); i++) {
            List<String> panelIds = (List<String>) Executions.getCurrent().getSession().getAttribute("PortalChildren" + i);
            if (panelIds != null) {
                for (String panelId : panelIds) {
                    Panel newPanel = (Panel)portalLayout.getFellow(panelId);
                    if (panelchildren.size() > 0)
                        panelchildren.get(i).insertBefore(newPanel, panelchildren.get(0));
                    else
                        newPanel.setParent(panelchildren.get(i));

                }
            }
        }
    }

    private void addWidget(DashboardPanel panel) {
        List<? extends Component> panelchildren = portalLayout.getChildren();
        Component firstChild = panelchildren.get(0);

        Panel panelToAdd = new Panel();
        panelToAdd.setTitle(panel.getTitle());
        panelToAdd.setBorder("normal");
        panelToAdd.setStyle("margin-bottom:10px");
        panelToAdd.setSclass("portal-widget-panel");

        updateEditModePanel(panelToAdd, editMode);

        if (!"".equals(panel.getPanelUri())) {
            panelToAdd.addEventListener(Events.ON_CLICK, event -> Executions.sendRedirect(panel.getPanelUri()));
        }
        Panelchildren panelchilds = new Panelchildren();
        panelchilds.setStyle("overflow-y: auto;");
        if (EnumSet.of(DashboardPanelConfig.WidgetType.DATA_GRID, DashboardPanelConfig.WidgetType.CHART, DashboardPanelConfig.WidgetType.MESSAGES).contains(panel.getType())) {
            panelchilds.appendChild(new Include(panel.getContentSrc()));
            panelToAdd.appendChild(panelchilds);
        } else if (panel.getType() == DashboardPanelConfig.WidgetType.CALENDAR_SIMPLE) {
            panelchilds.appendChild(new Calendar());
            panelToAdd.appendChild(panelchilds);
        }

        if (panel.getType() == DashboardPanelConfig.WidgetType.MENU_ITEM) {
            // polozlku menu nelze sbalit ani maximalizovat
            panelToAdd.setCollapsible(false);
            panelToAdd.setMaximizable(false);
            // odlišná default barva
            panelToAdd.setStyle("background: #FFFFCC");
        }

        firstChild.appendChild(panelToAdd);
        // saveStatus();
    }

    private void updateEditMode(boolean editMode) {
        this.editMode = editMode;
        List<? extends Component> panelchildren = portalLayout.getChildren();
        Portalchildren portalchidren = null;
        for (Component comp : portalLayout.getChildren()) {
            portalchidren = (Portalchildren)comp;
            for (Component pnl : portalchidren.getChildren()) {
                updateEditModePanel((Panel)pnl, editMode);
            }
        }
    }

    private void updateEditModePanel(Panel panel, boolean editMode) {
        panel.setDraggable(String.valueOf(editMode));
        panel.setCollapsible(!editMode);
        panel.setSizable(editMode);
        panel.setMaximizable(!editMode);
        panel.setClosable(editMode);
    }
}
