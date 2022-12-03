package com.ness.zkworkshop.web.util;

import com.ness.zkworkshop.web.config.DashboardConfig;
import com.ness.zkworkshop.web.service.DashboardService;
import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Messagebox;

public final class DashboardUtils {

    private DashboardUtils() {};

    /**
     * Vraci dashboardId z parametru requestu, pokud prazdny vraci default dashboard id.
     * @return
     */
    public static Long getRequestDashboardId() {
        String dashboardId = WebUtils.getRequestParameter("dashboardId");
        if (dashboardId != null && !"".equals(dashboardId)) {
            return Long.valueOf(dashboardId);
        }
        return DashboardServiceSessionImpl.DEFAULT_DASHBOARD_ID;
    }

    /**
     * Odstraneni dashboardu.
     * @param dashboardConfig
     * @param dashboardService
     * @param postDeleteAction
     */
    public static void deleteDashboard(DashboardConfig dashboardConfig, DashboardService dashboardService, Runnable postDeleteAction) {
        Messagebox.show("Opravdu si přejete odstranit dashboard " + dashboardConfig.getName() + "?",
                "Potvrzení", Messagebox.OK | Messagebox.CANCEL,
                Messagebox.QUESTION,
                new org.zkoss.zk.ui.event.EventListener(){
                    public void onEvent(Event e){
                        if(Messagebox.ON_OK.equals(e.getName())){
                            dashboardService.deleteDashboard(dashboardConfig.getId());
                            if (postDeleteAction != null) {
                                postDeleteAction.run();
                            }
                            Clients.showNotification(String.format("Dashboard "+dashboardConfig.getName()+" byl odstraněn."),
                                    Clients.NOTIFICATION_TYPE_INFO,
                                    null,
                                    null,
                                    2000);
                        }
                    }
                }
        );
    }
}
