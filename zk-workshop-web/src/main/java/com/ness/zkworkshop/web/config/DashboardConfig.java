package com.ness.zkworkshop.web.config;

import com.ness.zkworkshop.web.service.DashboardServiceSessionImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Konfigurace dashboardu obsahujici pocet sloupcu a seznam panelu.
 */
public class DashboardConfig {
    private final Long id;
    private String name;
    private final int cols;
    private List<DashboardPanelConfig> panelConfigList;

    public DashboardConfig(Long id, String name, int cols, List<DashboardPanelConfig> panelConfigList) {
        this.id = id;
        this.name = name;
        this.cols = cols;
        this.panelConfigList = panelConfigList;
    }

    public List<DashboardPanelConfig> copyPanelConfigList() {
        if (panelConfigList == null) {
            return new ArrayList<>();
        }
        List<DashboardPanelConfig> ret = new ArrayList<>();
        panelConfigList.stream().forEach(i -> ret.add(new DashboardPanelConfig(i)));

        return ret;
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

    public boolean isDefault() {
        return id == DashboardServiceSessionImpl.DEFAULT_DASHBOARD_ID;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCols() {
        return cols;
    }

    public List<DashboardPanelConfig> getPanelConfigList() {
        return panelConfigList;
    }
}
