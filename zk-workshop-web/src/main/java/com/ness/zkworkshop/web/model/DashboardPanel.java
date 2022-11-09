package com.ness.zkworkshop.web.model;

import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.viewmodel.AddWidgetModalVM;

public class DashboardPanel {

    private String title;
    private String contentSrc;
    private String panelUri;
    private DashboardPanelConfig.WidgetType type;

    public DashboardPanel(String title, String contentSrc, String panelUri, DashboardPanelConfig.WidgetType type) {
        this.title = title;
        this.contentSrc = contentSrc;
        this.panelUri = panelUri;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContentSrc() {
        return contentSrc;
    }

    public void setContentSrc(String contentSrc) {
        this.contentSrc = contentSrc;
    }

    public DashboardPanelConfig.WidgetType getType() {
        return type;
    }

    public void setType(DashboardPanelConfig.WidgetType type) {
        this.type = type;
    }

    public String getPanelUri() {
        return panelUri;
    }

    public void setPanelUri(String panelUri) {
        this.panelUri = panelUri;
    }
}
