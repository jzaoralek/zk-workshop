package com.ness.zkworkshop.web.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Konfigurace dashboardu.
 */
public class DashboardConfig {
    private final int cols;
    private List<DashboardPanelConfig> panelConfigList;

    public DashboardConfig(int cols, List<DashboardPanelConfig> panelConfigList) {
        this.cols = cols;
        this.panelConfigList = panelConfigList;
    }

    public void addPanelConfig(DashboardPanelConfig config) {
        if (panelConfigList == null) {
            panelConfigList = new ArrayList<>();
        }
        panelConfigList.add(config);
    }

    public void removePanelConfig(DashboardPanelConfig config) {
        if (panelConfigList == null) {
            return;
        }
    }

    public int getCols() {
        return cols;
    }

    public List<DashboardPanelConfig> getPanelConfigList() {
        return panelConfigList;
    }
}
