package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zk.ui.util.Notification;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zkmax.ui.event.PortalDropEvent;
import org.zkoss.zkmax.zul.Portalchildren;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.*;
import org.zkoss.zul.Calendar;

import java.util.*;

public class PortalLayoutController extends SelectorComposer<Component> {

    private DashboardPanelLibrary dashboardPanelLibrary = new DashboardPanelLibrary();
    private DashboardConfig dashboardConfig;

    private static final String DASHBOARD_CONFIG = "dashboardConfig";
    private static final String WIDGET_TYPE = "widgetType";
    private static final String WIDGET_INDEX = "widgetIndex";

    @Wire
    private Portallayout portalLayout;

    private boolean editMode;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();

        // pridani widgetu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.ADD_WIDGET, data -> addNewWidget((DashboardPanel)data));
        // prepnuti do editacniho rezimu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.EDIT_MODE, data ->  updateEditMode((Boolean)data));
    }

    private void init() {
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

        for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
            addWidget(dashboardPanelLibrary.getDashboardPanelMap().get(panelConfig.getWidgetType()).get(panelConfig.getWidgetIndex()),
                    panelConfig.getDashCol(),
                    panelConfig.getWidgetIndex());
        }
    }

    /**
     * Init konfigurace dashboardu.
     * Z DB, session.
     * @return
     */
    private DashboardConfig initDashboardConfig() {
        DashboardConfig dashboardConfig = getDashboardConfig();
        if (dashboardConfig == null) {
            // TODO: ziskat konfiguraci z databaze
            List<DashboardPanelConfig> panelConfigList = new ArrayList<>();
            panelConfigList.add(new DashboardPanelConfig(0, 0, DashboardPanelLibrary.WidgetType.CALENDAR_SIMPLE, 0));
            panelConfigList.add(new DashboardPanelConfig(1, 0, DashboardPanelLibrary.WidgetType.DATA_GRID, 0));
            dashboardConfig = new DashboardConfig(4, panelConfigList);
            storeDashboardConfig();
        }

        return dashboardConfig;
    }

    /**
     * Ziskani DashboardConfig ze session.
     * @return
     */
    private DashboardConfig getDashboardConfig() {
        return (DashboardConfig)Executions.getCurrent().getSession().getAttribute(DASHBOARD_CONFIG);
    }

    /**
     * Vraci DashboardPanelConfig na zaklade Panel z konfigurace..
     * @param panel
     * @return
     */
    private DashboardPanelConfig getDashPanelCfg(Panel panel) {
        DashboardPanelLibrary.WidgetType widgetType = DashboardPanelLibrary.WidgetType.valueOf(panel.getClientAttribute(WIDGET_TYPE));
        Integer widgetIndex = Integer.valueOf(panel.getClientAttribute(WIDGET_INDEX));

        return getDashPanelCfg(widgetType, widgetIndex);
    }

    /**
     * Vraci DashboardPanelConfig z konfigurace na zaklade vstupnich argumentu.
     * @param widgetType
     * @param widgetIndex
     * @return
     */
    private DashboardPanelConfig getDashPanelCfg(DashboardPanelLibrary.WidgetType widgetType, Integer widgetIndex) {
        for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
            if (panelConfig.getWidgetType() == widgetType
                    && panelConfig.getWidgetIndex() == widgetIndex) {
                return panelConfig;
            }
        }

        return null;
    }

    /**
     * Aktualizace dashboardConfig podle skutecneho stavu, serazeni List<DashboardPanelConfig> podle sloupcu a radku a ulozeni dashboardConfig do session.
     */
    private void storeDashboardConfig() {
        if (dashboardConfig != null && dashboardConfig.getPanelConfigList() != null) {
            // serazeni podle sloupcu a radku, aby po reloadu v init() byly ve stejnem poradi
            Collections.sort(dashboardConfig.getPanelConfigList(), Comparator.comparing(DashboardPanelConfig::getDashCol)
                    .thenComparing(DashboardPanelConfig::getDashRow));
        }

        /*
        if (dashboardConfig != null && dashboardConfig.getPanelConfigList() != null) {
            for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
                System.out.println("Col: " + panelConfig.getDashCol() + ", Row: " + panelConfig.getDashRow() + ", type: " + panelConfig.getWidgetType());
            }
        }
        */

        // ulozeni do session
        Executions.getCurrent().getSession().setAttribute(DASHBOARD_CONFIG, dashboardConfig);
    }

    /**
     * Premisteni panelu na jinou pozici.
     * @param event
     */
    @Listen("onPortalDrop = #portalLayout")
    public void dropStatus(PortalDropEvent event) {
        int dashCol = event.getDroppedColumnIndex();
        int dashRow = event.getDroppedIndex();

        int dashColOrig = event.getDraggedColumnIndex();
        int dashRowOrig = event.getDraggedIndex();

        Panel panel = event.getDragged();
        DashboardPanelConfig movedDashPnlCfg = getDashPanelCfg(panel);
        // aktualizace umisteni prenaseneho panelu
        movedDashPnlCfg.setDashCol(dashCol);
        movedDashPnlCfg.setDashRow(dashRow);

        /* Aktualizace indexu panelu. */
        if (dashColOrig != dashCol) {
            // Presun mezi sloupci.
            for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
                if (panelConfig != movedDashPnlCfg && panelConfig.getDashCol() == dashCol) {
                    if (panelConfig.getDashRow() >= dashRow) {
                        // zvyseni indexu panelu pod prenesenym panelem
                        panelConfig.setDashRow(panelConfig.getDashRow() + 1);
                    }
                }
            }
        } else {
            // Presun v jednom sloupci.
            if (dashRowOrig < dashRow) {
                // Presun dolu
                for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
                    if (panelConfig != movedDashPnlCfg && panelConfig.getDashCol() == dashCol) {
                        if (panelConfig.getDashRow() <= dashRow && panelConfig.getDashRow() > dashRowOrig) {
                            // zvyseni indexu panelu pod prenesenym panelem
                            panelConfig.setDashRow(panelConfig.getDashRow() - 1);
                        }
                    }
                }
            } else {
                // Presun nahoru
                for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
                    if (panelConfig != movedDashPnlCfg && panelConfig.getDashCol() == dashCol) {
                        if (panelConfig.getDashRow() >= dashRow && panelConfig.getDashRow() < dashRowOrig) {
                            // zvyseni indexu panelu pod prenesenym panelem
                            panelConfig.setDashRow(panelConfig.getDashRow() + 1);
                        }
                    }
                }
            }
        }

        storeDashboardConfig();
    }

    /**
     * Pridani noveho widgetu uzivatelem.
     * Do prvniho sloupce na posledni pozici.
     * Ulozeni do dashboardConfig.
     * @param panel
     */
    private void addNewWidget(DashboardPanel panel) {
        // prvni sloupec
        int dashCol = 0;
        int widgetIdx = dashboardPanelLibrary.getDashWidgetIdx(panel);
        // kontrola zda-li pridavany panel jiz neni na dashboardu
        if (getDashPanelCfg(panel.getType(), widgetIdx) != null) {
            Clients.showNotification("Widget '" + panel.getTitle() + "' již dashboard obsahuje.",
                                    Clients.NOTIFICATION_TYPE_WARNING, null, null, 5000,true);
            return;
        }

        int dashRow = addWidget(panel, dashCol, widgetIdx);

        // store added widget to config
        DashboardPanelConfig dashboardPanelConfig = new DashboardPanelConfig(dashCol, dashRow, panel.getType(), widgetIdx);
        this.dashboardConfig.addPanelConfig(dashboardPanelConfig);
        storeDashboardConfig();
    }

    /**
     * Pridani widgetu na dashboard.
     * @param panel
     * @param dashboardColIdx
     * @param widgetIdx
     * @return pozice widgetu ve slupci.
     */
    private int addWidget(DashboardPanel panel, int dashboardColIdx, int widgetIdx) {
        List<? extends Component> panelchildren = portalLayout.getChildren();
        Component dashboardCol = panelchildren.get(dashboardColIdx);

        Panel panelToAdd = new Panel();
        // Identifikator panelu, pouziti pripresouvani v dropStatus()
        panelToAdd.setClientAttribute(WIDGET_TYPE, panel.getType().name());
        panelToAdd.setClientAttribute(WIDGET_INDEX, String.valueOf(widgetIdx));

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
        addCustTools(panelchilds, panelToAdd);

        // Editacni rezim.
        updateEditModePanel(panelToAdd, editMode);

        // Event listener pro odebrani panelu
        panelToAdd.addEventListener(Events.ON_CLOSE, event -> removeWidget(panelToAdd, panel, widgetIdx));

        dashboardCol.appendChild(panelToAdd);

        return dashboardCol.getChildren().size() - 1;
        // saveStatus();
    }

    /**
     * Odebrani widgetu z dashboardu.
     * @param panel
     * @param dashboardPanel
     * @param widgetIdx
     */
    private void removeWidget(Panel panel, DashboardPanel dashboardPanel, int widgetIdx) {
        DashboardPanelConfig panelCfgToRemove = null;
        for (DashboardPanelConfig panelCfg : this.dashboardConfig.getPanelConfigList()) {
            if (panelCfg.getWidgetType() == dashboardPanel.getType()
                    && panelCfg.getWidgetIndex() == widgetIdx) {
                panelCfgToRemove = panelCfg;
            }
        }
        this.dashboardConfig.removePanelConfig(panelCfgToRemove);

        // po odstraneni zmensit vsem pod panelem index
        for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
            if (panelConfig.getDashCol() == panelCfgToRemove.getDashCol()) {
                if (panelConfig.getDashRow() > panelCfgToRemove.getDashRow()) {
                    // zvyseni indexu panelu pod prenesenym panelem
                    panelConfig.setDashRow(panelConfig.getDashRow() - 1);
                }
            }
        }

        storeDashboardConfig();
    }

    /**
     * Pridani tlacitka s popupem pro customizaci panelu.
     * @param panelchilds
     * @param panelToAdd
     */
    private void addCustTools(Panelchildren panelchilds,
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
    }

    /**
     * Nastaveni barvy pozadi a ohraniceni podle colorpickeru.
     */
    private void setPanelStyle(Panel panel, String backgrColor, String borderColor) {
        panel.setStyle("background: " + backgrColor + "; border-color: " + borderColor);
    }

    /**
     * Aktualizace editacniho rezimu.
     * Povoluje odpovidajici akce.
     * @param editMode
     */
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

    /**
     * Nastaveni editacniho rezimu panelu.
     * @param panel
     * @param editMode
     */
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