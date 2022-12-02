package com.ness.zkworkshop.web.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Konfigurace dashboardu obsahujici pocet sloupcu a seznam panelu.
 */
public class DashboardConfig {
    private final String name;
    private final int cols;
    private List<DashboardPanelConfig> panelConfigList;

    public DashboardConfig(String name, int cols, List<DashboardPanelConfig> panelConfigList) {
        this.name = name;
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
        panelConfigList.remove(config);
    }

    public int getCols() {
        return cols;
    }

    public List<DashboardPanelConfig> getPanelConfigList() {
        return panelConfigList;
    }
}
