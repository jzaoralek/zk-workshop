package com.ness.zkworkshop.web.model;

import com.ness.zkworkshop.web.config.DashboardPanelLibrary;

/**
 * Objekt obsahujici zakladni informace o panelu.
 * Pouziti v DashboardPanelLibrary a v pridavani noveho panelu.
 */
public class DashboardPanel {

    private final long id;
    private String title;
    private String style;
    private String contentSrc;
    private String panelUri;
    private String imgUri;
    private DashboardPanelLibrary.WidgetType type;

    public DashboardPanel(long id, String title, String contentSrc, String panelUri, String imgUri, DashboardPanelLibrary.WidgetType type) {
        this.id = id;
        this.title = title;
        this.contentSrc = contentSrc;
        this.panelUri = panelUri;
        this.imgUri = imgUri;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getContentSrc() {
        return contentSrc;
    }

    public void setContentSrc(String contentSrc) {
        this.contentSrc = contentSrc;
    }

    public DashboardPanelLibrary.WidgetType getType() {
        return type;
    }

    public void setType(DashboardPanelLibrary.WidgetType type) {
        this.type = type;
    }

    public String getPanelUri() {
        return panelUri;
    }

    public String getImgUri() {
        return imgUri;
    }

    public void setPanelUri(String panelUri) {
        this.panelUri = panelUri;
    }
}
