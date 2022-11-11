package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.*;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PortalLayoutController extends SelectorComposer<Component> {

    private DashboardPanelLibrary dashboardPanelLibrary = new DashboardPanelLibrary();
    private DashboardConfig dashboardConfig;

    @Wire
    private Portallayout portalLayout;

    private boolean editMode;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init((Boolean)editMode);

        // pridani widgetu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.ADD_WIDGET, data -> addWidget((DashboardPanel)data, 0));
        // prepnuti do editacniho rezimu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.EDIT_MODE, data ->  updateEditMode((Boolean)data));
    }

    private void init(Boolean editMode) {
        this.editMode = editMode;
        portalLayout.setMaximizedMode("whole");

        this.dashboardConfig = initDashboardConfig();
        // sloupce dashboardu
        int cols = this.dashboardConfig.getCols();
        String colWdth = 100/cols+"%";

        Portalchildren portalChildren = null;
        for (int i = 0; i < cols; i++) {
            portalChildren = new Portalchildren();
            portalChildren.setWidth(colWdth);
            portalLayout.appendChild(portalChildren);
        }

        // TODO: konfigurace dashboardu uzivatele
        for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
            // TODO: doresit dashboard index row
            panelConfig.getDashRow();
            addWidget(dashboardPanelLibrary.getDashboardPanelMap().get(panelConfig.getWidgetType()).get(panelConfig.getWidgetIndex()),
                    panelConfig.getDashCol());
        }
    }

    private DashboardConfig initDashboardConfig() {
        // TODO: ziskat konfiguraci z databaze
        List<DashboardPanelConfig> panelConfigList = new ArrayList<>();
        panelConfigList.add(new DashboardPanelConfig(0, 0, DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE, 0));
        panelConfigList.add(new DashboardPanelConfig(1, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0));

        return new DashboardConfig(4, panelConfigList);
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

    private void addWidget(DashboardPanel panel, int dashboardCol) {
        List<? extends Component> panelchildren = portalLayout.getChildren();
        Component firstChild = panelchildren.get(dashboardCol);

        Panel panelToAdd = new Panel();
        panelToAdd.setTitle(panel.getTitle());
        panelToAdd.setBorder("normal");
        panelToAdd.setStyle("margin-bottom:10px");
        panelToAdd.setSclass("portal-widget-panel");

        if (!"".equals(panel.getPanelUri())) {
            // panelToAdd.addEventListener(Events.ON_CLICK, event -> Executions.sendRedirect(panel.getPanelUri()));
        }
        Panelchildren panelchilds = new Panelchildren();
        panelchilds.setStyle("overflow-y: auto;");
        if (EnumSet.of(DashboardPanelLibrary.WidgetType.DATA_GRID, DashboardPanelLibrary.WidgetType.CHART, DashboardPanelLibrary.WidgetType.MESSAGES).contains(panel.getType())) {
            panelchilds.appendChild(new Include(panel.getContentSrc()));
            panelToAdd.appendChild(panelchilds);
        } else if (panel.getType() == DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE) {
            panelchilds.appendChild(new Calendar());
            panelToAdd.appendChild(panelchilds);
        }

        if (panel.getType() == DashboardPanelLibrary.WidgetType.MENU_ITEM) {
            // polozlku menu nelze sbalit ani maximalizovat
            panelToAdd.setCollapsible(false);
            panelToAdd.setMaximizable(false);
            // odlišná default barva
            panelToAdd.setStyle("background: #FFFFCC");
        }

        // Add customization button.
        addCustTools(panelchilds, firstChild, panelToAdd);

        // Editacni rezim.
        updateEditModePanel(panelToAdd, editMode);

        // saveStatus();
    }

    /**
     * Pridani tlacitka s popupem pro customizaci panelu.
     * @param panelchilds
     * @param firstChild
     * @param panelToAdd
     */
    private void addCustTools(Panelchildren panelchilds,
                              Component firstChild,
                              Panel panelToAdd) {
        Hbox panelCustHbox = new Hbox();
        panelCustHbox.setVisible(editMode);
        panelCustHbox.setStyle("margin: 10px;");

        // Button
        Button panelCustBtn = new Button();
        panelCustBtn.setIconSclass("z-icon-ellipsis-h");
        panelCustBtn.setStyle("background: #F9FCFF; color: #8A8A8A;");
        panelCustBtn.setTooltiptext(Labels.getLabel("web.settings"));
        // Popup
        Popup panelCustPopup = new Popup();
        Vlayout panelCustPopupVlayout = new Vlayout();

        Hlayout captionHlayout = new Hlayout();
        captionHlayout.setSclass("z-caption-label");
        captionHlayout.appendChild(new Label(Labels.getLabel("web.settings")));
        panelCustPopupVlayout.appendChild(captionHlayout);

        Grid panelCustSettingsGrid = new Grid();
        panelCustSettingsGrid.setHflex("min");
        Columns cols = new Columns();
        cols.appendChild(new Column());
        cols.appendChild(new Column());
        panelCustSettingsGrid.appendChild(cols);
        Rows rows = new Rows();

        // Title row
        Row titleRow = new Row();
        titleRow.appendChild(new Label(Labels.getLabel("web.title")));
        Textbox titleRowTxt = new Textbox(panelToAdd.getTitle());
        titleRowTxt.addEventListener(Events.ON_CHANGE, event -> panelToAdd.setTitle(titleRowTxt.getValue()));
        titleRowTxt.addEventListener(Events.ON_OK, event -> panelToAdd.setTitle(titleRowTxt.getValue()));
        titleRow.appendChild(titleRowTxt);
        rows.appendChild(titleRow);

        // Background color
        Row backgroundColorRow = new Row();
        backgroundColorRow.appendChild(new Label(Labels.getLabel("web.header")));
        Colorbox backgrColorbox = new Colorbox();
        backgrColorbox.setColor("#F9FCFF");
        backgroundColorRow.appendChild(backgrColorbox);
        rows.appendChild(backgroundColorRow);

        // Border color
        Row borderColorRow = new Row();
        borderColorRow.appendChild(new Label(Labels.getLabel("web.border")));
        Colorbox borderColorBox = new Colorbox();
        borderColorBox.setColor("#d9d9d9");
        borderColorRow.appendChild(borderColorBox);

        backgrColorbox.addEventListener(Events.ON_CHANGE, event -> setPanelStyle(panelToAdd, backgrColorbox.getColor(), borderColorBox.getColor()));
        borderColorBox.addEventListener(Events.ON_CHANGE, event -> setPanelStyle(panelToAdd, backgrColorbox.getColor(), borderColorBox.getColor()));

        rows.appendChild(borderColorRow);

        panelCustSettingsGrid.appendChild(rows);
        panelCustPopupVlayout.appendChild(panelCustSettingsGrid);
        panelCustPopup.appendChild(panelCustPopupVlayout);

        panelCustHbox.appendChild(panelCustPopup);
        panelCustBtn.setPopup(panelCustPopup);
        panelCustHbox.appendChild(panelCustBtn);

        panelchilds.appendChild(panelCustHbox);

        firstChild.appendChild(panelToAdd);
    }

    /**
     * Nastaveni barvy pozadi a ohraniceni podle colorpickeru.
     */
    private void setPanelStyle(Panel panel, String backgrColor, String borderColor) {
        panel.setStyle("background: " + backgrColor + "; border-color: " + borderColor);
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
        // tlacitko pro customizaci
        if (panel.getPanelchildren() != null) {
            Component lastChild = panel.getPanelchildren().getLastChild();
            if (lastChild instanceof Hbox) {
                lastChild.setVisible(editMode);
            }
        }
    }
}
