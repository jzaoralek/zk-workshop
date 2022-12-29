package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.model.DashboardPanel;
import com.ness.zkworkshop.web.service.DashboardService;
import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;
import com.ness.zkworkshop.web.util.EventQueueHelper;
import com.ness.zkworkshop.web.util.WebUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Button;
import org.zkoss.zul.Label;
import org.zkoss.zul.Toolbar;
import org.zkoss.zul.Toolbarbutton;

import java.util.List;

/**
 * Composer pro generovani pdkazu na dashboardy v navmenu
 */
public class NavmenuDashboardController extends SelectorComposer<Component> {

    private DashboardService dashboardService = new DashboardServiceSessionImpl();

    @Wire
    private Toolbar dashboardIntToolbar;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        init();

        // event listener prejmenovani dashboardu
        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.DASHBOARD_RENAME, data -> renameDashboard((DashboardConfig)data));
    }

    /**
     * Iniciace odkazu na dashboardy.
     */
    private void init() {
        List<DashboardConfig> dashboardList = dashboardService.getDashboardAll();
        String dashboardIdReqParam = WebUtils.getRequestParameter("dashboardId");
        Button toolbarBtn;

        for (DashboardConfig dashCfg : dashboardList) {
            if (dashCfg.getId().toString().equals(dashboardIdReqParam) || ((dashboardIdReqParam == null || dashboardIdReqParam == "") && dashCfg.isDefault())) {
                // zvyrazneni aktualne zobrazene polozky pomoci Button
                toolbarBtn = new Button();
            } else {
                toolbarBtn = new Toolbarbutton();
            }
            toolbarBtn.setId(dashCfg.getId().toString());
            toolbarBtn.setLabel(dashCfg.getName());
            toolbarBtn.addEventListener(Events.ON_CLICK, event -> Executions.sendRedirect("/index.zul?dashboardId=" + dashCfg.getId()));
            dashboardIntToolbar.appendChild(toolbarBtn);
            // pokud vice nez jeden dashboard, umistime za odkaz na default oddelovac at odlisime custom dashboardy
            if (dashCfg.isDefault() && dashboardList.size() > 1) {
                dashboardIntToolbar.appendChild(buildSeparatorLbl());
            }
        }

        Button dashboardAdminBtn = new Toolbarbutton();
        dashboardAdminBtn.setLabel("Dashboard - admin");
        dashboardAdminBtn.addEventListener(Events.ON_CLICK, event -> Executions.sendRedirect("/pages/dashboard/dashboard-admin.zul"));
        dashboardIntToolbar.appendChild(dashboardAdminBtn);

    }

    private Label buildSeparatorLbl() {
        Label ret = new Label("|");
        ret.setStyle("margin-left: 5px; margin-right: 5px");

        return ret;
    }

    /**
     * Prejmenovani dashboardu
     * @param dashboardSelected
     */
    private void renameDashboard(DashboardConfig dashboardSelected) {
        for (Component toolbarChild : dashboardIntToolbar.getChildren()) {
            if (toolbarChild instanceof Toolbarbutton
                    && ((Toolbarbutton)toolbarChild).getId().equals(dashboardSelected.getId().toString())) {
                ((Toolbarbutton)toolbarChild).setLabel(dashboardSelected.getName());
            }
        }
    }
}
