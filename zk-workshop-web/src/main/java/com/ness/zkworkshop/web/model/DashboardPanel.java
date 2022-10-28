package com.ness.zkworkshop.web.model;

import com.ness.zkworkshop.web.viewmodel.AddWidgetModalVM;

public class DashboardPanel {

    private String title;
    private String contentSrc;
    private String panelUri;
    private AddWidgetModalVM.WidgetType type;

    public DashboardPanel(String title, String contentSrc, String panelUri, AddWidgetModalVM.WidgetType type) {
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

    public AddWidgetModalVM.WidgetType getType() {
        return type;
    }

    public void setType(AddWidgetModalVM.WidgetType type) {
        this.type = type;
    }

    public String getPanelUri() {
        return panelUri;
    }

    public void setPanelUri(String panelUri) {
        this.panelUri = panelUri;
    }
}
