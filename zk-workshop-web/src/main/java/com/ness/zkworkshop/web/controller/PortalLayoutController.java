package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.util.EventQueueHelper;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkmax.zul.Portallayout;
import org.zkoss.zul.Button;
import org.zkoss.zul.Calendar;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Panelchildren;

import java.util.ArrayList;
import java.util.List;

public class PortalLayoutController extends SelectorComposer<Component> {

    @Wire
    private Portallayout portalLayout;

    /**
     * Řešeno přes event queue, protože composer nedokáže registrovat globální command
     */
    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);

        EventQueueHelper.queueLookup(EventQueueHelper.SdatEventQueues.DASHBOARD_QUEUE)
                .subscribe(EventQueueHelper.SdatEvent.ADD_WIDGET, data -> addPortalWidget());
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

    private void addPortalWidget() {
        List<? extends Component> panelchildren = portalLayout.getChildren();
        Component firstChild = panelchildren.get(0);

        Panel panelToAdd = new Panel();
        // panelToAdd.setId("calendar" + System.currentTimeMillis());
        panelToAdd.setTitle("Calendar added");
        panelToAdd.setBorder("normal");
        panelToAdd.setCollapsible(true);
        panelToAdd.setClosable(true);
        panelToAdd.setMaximizable(true);
        panelToAdd.setStyle("margin-bottom:10px");

        Panelchildren panelchilds = new Panelchildren();
        panelchilds.appendChild(new Calendar());
        panelToAdd.appendChild(panelchilds);

        firstChild.appendChild(panelToAdd);
        // saveStatus();
    }
}