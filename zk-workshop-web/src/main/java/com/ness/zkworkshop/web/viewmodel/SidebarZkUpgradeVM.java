package com.ness.zkworkshop.web.viewmodel;

import org.zkoss.zk.ui.Executions;

public class SidebarZkUpgradeVM {

    public boolean isNavItemSelected(String href) {
        return href.contains(Executions.getCurrent().getDesktop().getRequestPath());
    }
}
