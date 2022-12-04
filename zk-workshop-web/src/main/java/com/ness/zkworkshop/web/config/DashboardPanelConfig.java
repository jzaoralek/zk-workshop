package com.ness.zkworkshop.web.config;

/**
 * Konfigurace pozice daneho widgetu na dashboardu.
 */
public class DashboardPanelConfig {
    /** Sloupec dashboardu kde je umisten panel */
    private int dashCol;
    /** Radek dashboardu kde je umísten panel. */
    private int dashRow;
    /** Nadpis panelu */
    private String title;
    /** Styl panelu */
    private String style;
    /** Typ panelu dashboardu. */
    private DashboardPanelLibrary.WidgetType widgetType;
    /** Poradi panelu v ramci typu panelu dashboardu. */
    private int widgetIndex;

    public DashboardPanelConfig(int dashCol, int dashRow, DashboardPanelLibrary.WidgetType widgetType, int widgetIndex, String title, String style) {
        this.dashCol = dashCol;
        this.dashRow = dashRow;
        this.widgetType = widgetType;
        this.widgetIndex = widgetIndex;
        this.title = title;
        this.style = style;
    }

    public DashboardPanelConfig(DashboardPanelConfig dashboardPanelConfig) {
        this.dashCol = dashboardPanelConfig.getDashCol();
        this.dashRow = dashboardPanelConfig.getDashRow();
        this.widgetType = dashboardPanelConfig.getWidgetType();
        this.widgetIndex = dashboardPanelConfig.getWidgetIndex();
        this.title = dashboardPanelConfig.getTitle();
        this.style = dashboardPanelConfig.getStyle();
    }

    public int getDashCol() {
        return dashCol;
    }

    public void setDashCol(int dashCol) {
        this.dashCol = dashCol;
    }

    public int getDashRow() {
        return dashRow;
    }

    public void setDashRow(int dashRow) {
        this.dashRow = dashRow;
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

    public DashboardPanelLibrary.WidgetType getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(DashboardPanelLibrary.WidgetType widgetType) {
        this.widgetType = widgetType;
    }

    public int getWidgetIndex() {
        return widgetIndex;
    }

    public void setWidgetIndex(int widgetIndex) {
        this.widgetIndex = widgetIndex;
    }
}
