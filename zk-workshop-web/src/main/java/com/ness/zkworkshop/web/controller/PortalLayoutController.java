package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.config.DashboardPanelLibrary;
import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.service.DashboardService;
import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;
import com.ness.zkworkshop.web.util.DashboardUtils;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import com.ness.zkworkshop.web.util.WebUtils;
import org.javatuples.Pair;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
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

    private static final String WIDGET_TYPE = "widgetType";
    private static final String WIDGET_INDEX = "widgetIndex";
    private static final String WIDGET_REMOVABLE = "widgetRemovable";

    private DashboardService dashboardService = new DashboardServiceSessionImpl();

    @Wire
    private Portallayout portalLayout;

    private boolean editMode;
    private Long dashboardId;
    private boolean adminMode;
    private DashboardServiceSessionImpl.DashboardType dashboardType;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();

        // event listener pridani widgetu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.ADD_WIDGET, data -> addNewWidget((Pair<DashboardPanel, DashboardServiceSessionImpl.DashboardType>)data));
        // event listener prepnuti do editacniho rezimu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.EDIT_MODE, data ->  updateEditMode((Boolean)data));
    }

    private void init() {
        // admin mode
        String adminModeStr = (String)portalLayout.getAttribute("adminMode");
        if (adminModeStr != null && !"".equals(adminModeStr)) {
            adminMode = Boolean.parseBoolean(adminModeStr);
        }

        // dashboard type (INT | ZBER)
        String dashboardTypeStr = (String)portalLayout.getAttribute("dashboardType");
        if (dashboardTypeStr != null && !"".equals(dashboardTypeStr)) {
            dashboardType = DashboardServiceSessionImpl.DashboardType.valueOf(dashboardTypeStr);
        } else {
            dashboardType = DashboardServiceSessionImpl.DashboardType.INT;
        }

        portalLayout.setMaximizedMode("whole");
        this.dashboardId = DashboardUtils.getRequestDashboardId();
        this.dashboardConfig = dashboardService.getDashboard(dashboardId, dashboardType);
        // sloupce dashboardu
        int cols = this.dashboardConfig.getCols();
        String colWdth = 100/cols+"%";

        Portalchildren portalChildren = null;
        for (int i = 0; i < cols; i++) {
            portalChildren = new Portalchildren();
            portalChildren.setWidth(colWdth);
            portalLayout.appendChild(portalChildren);
        }

        DashboardPanel dashPnl = null;
        for (DashboardPanelConfig panelConfig : dashboardConfig.getPanelConfigList()) {
            dashPnl = dashboardPanelLibrary.getDashboardPanelMap().get(panelConfig.getWidgetType()).get(panelConfig.getWidgetIndex());
            dashPnl.setTitle(panelConfig.getTitle());
            dashPnl.setStyle(panelConfig.getStyle());
            dashPnl.setRemovable(panelConfig.isRemovable());
            addWidget(dashPnl,
                    panelConfig.getDashCol(),
                    panelConfig.getWidgetIndex());
        }
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
        // ulozeni do session
        dashboardService.updateDashboard(dashboardId, dashboardConfig, dashboardType);
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
     * @param panelTypePair
     */
    private void addNewWidget(Pair<DashboardPanel, DashboardServiceSessionImpl.DashboardType> panelTypePair) {
        // kontrola shodnosti dashboardType
        if (panelTypePair.getValue1() != this.dashboardType) {
            return;
        }
        // prvni sloupec
        int dashCol = 0;
        DashboardPanel panel = panelTypePair.getValue0();
        int widgetIdx = dashboardPanelLibrary.getDashWidgetIdx(panel);
        // kontrola zda-li pridavany panel jiz neni na dashboardu
        if (getDashPanelCfg(panel.getType(), widgetIdx) != null) {
            Clients.showNotification("Widget '" + panel.getTitle() + "' již dashboard obsahuje.",
                                    Clients.NOTIFICATION_TYPE_WARNING, null, null, 5000,true);
            return;
        }

        // Pridavany widget odstranitelny pokud je pridavany uzivatelem,
        // v admin rezimu defaultne neodstrnitelny.
        panel.setRemovable(!adminMode);

        int dashRow = addWidget(panel, dashCol, widgetIdx);

        // store added widget to config
        DashboardPanelConfig dashboardPanelConfig = new DashboardPanelConfig(dashCol, dashRow, panel.getType(), widgetIdx, panel.getTitle(), panel.getStyle(), panel.isRemovable());
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
        panelToAdd.setClientAttribute(WIDGET_REMOVABLE, String.valueOf(panel.isRemovable()));

        //panelToAdd.setTitle(panel.getTitle());
        panelToAdd.setBorder("normal");
        panelToAdd.setStyle(panel.getStyle());
        panelToAdd.setSclass("portal-widget-panel");

        // caption
        Caption caption = new Caption(panel.getTitle());
        if (!"".equals(panel.getImgUri())) {
            caption.setImage(panel.getImgUri());
        }
        panelToAdd.appendChild(caption);

        if (!"".equals(panel.getPanelUri())) {
            // panelToAdd.addEventListener(Events.ON_CLICK, event -> Executions.sendRedirect(panel.getPanelUri()));
        }
        Panelchildren panelchilds = new Panelchildren();
        panelchilds.setStyle("overflow-y: auto;");
        if (EnumSet.of(DashboardPanelLibrary.WidgetType.DATA_GRID,
                        DashboardPanelLibrary.WidgetType.CHART,
                        DashboardPanelLibrary.WidgetType.MESSAGES,
                        DashboardPanelLibrary.WidgetType.MODULE,
                        DashboardPanelLibrary.WidgetType.CALENDAR).contains(panel.getType())) {
            panelchilds.appendChild(new Include(panel.getContentSrc()));
            panelToAdd.appendChild(panelchilds);
            panelToAdd.getCaption().addEventListener(Events.ON_CLICK, event -> Executions.sendRedirect(panel.getPanelUri()));
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
        titleRowTxt.addEventListener(Events.ON_CHANGE, event -> setPanelTitle(panelToAdd, titleRowTxt.getValue()));
        titleRowTxt.addEventListener(Events.ON_OK, event -> setPanelTitle(panelToAdd, titleRowTxt.getValue()));
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

        // Removability - jen v admin režimu
        if (adminMode) {
            Row removabilityRow = new Row();
            removabilityRow.appendChild(new Label(Labels.getLabel("web.removable")));
            Listbox removableListbox = new Listbox();
            removableListbox.setMold("select");
            removableListbox.setWidth("50px");
            Listitem itemYes = new Listitem();
            itemYes.setLabel("Ano");
            itemYes.setValue(true);
            Listitem itemNo = new Listitem();
            itemNo.setLabel("Ne");
            itemNo.setValue(false);

            if (Boolean.valueOf(panelToAdd.getClientAttribute(WIDGET_REMOVABLE))) {
                itemYes.setSelected(true);
            } else {
                itemNo.setSelected(true);
            }
            removableListbox.appendChild(itemYes);
            removableListbox.appendChild(itemNo);
            removableListbox.addEventListener(Events.ON_SELECT, event -> setPanelRemovable(panelToAdd, itemYes.isSelected()));
            removabilityRow.appendChild(removableListbox);
            rows.appendChild(removabilityRow);
        }

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
        String style = "background: " + backgrColor + " !important; border-color: " + borderColor + " !important;";
        panel.setStyle(style);
        // ulozeni do panel config
        DashboardPanelConfig panelCfgToUpdate = getDashPanelCfg(panel);
        panelCfgToUpdate.setStyle(style);

        storeDashboardConfig();
    }

    /**
     * Nastaveni odstranitelnosti panelu.
     */
    private void setPanelRemovable(Panel panel, boolean value) {
        // ulozeni do panel config
        DashboardPanelConfig panelCfgToUpdate = getDashPanelCfg(panel);
        panelCfgToUpdate.setRemovable(value);

        storeDashboardConfig();
    }

    /**
     * Nastaveni title panelu.
     */
    private void setPanelTitle(Panel panel, String title) {
        panel.setTitle(title);
        // ulozeni do panel config
        DashboardPanelConfig panelCfgToUpdate = getDashPanelCfg(panel);
        panelCfgToUpdate.setTitle(title);

        storeDashboardConfig();
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
        if (adminMode) {
            // admin - povoleno odstranit vzdy v editacnim rezimu
            panel.setClosable(editMode);
        } else {
            // uzivatel - muze odstranit v editacnim rezimu, jen pokud je povoleno
            panel.setClosable(editMode && Boolean.valueOf(panel.getClientAttribute(WIDGET_REMOVABLE)));
        }
        // tlacitko pro customizaci
        if (panel.getPanelchildren() != null) {
            Component lastChild = panel.getPanelchildren().getLastChild();
            if (lastChild instanceof Hbox) {
                lastChild.setVisible(editMode);
            }
        }
    }
}