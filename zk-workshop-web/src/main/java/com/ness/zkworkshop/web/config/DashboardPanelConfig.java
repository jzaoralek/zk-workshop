package com.ness.zkworkshop.web.config;

/**
 * Konfigurace pozice daneho widgetu na dashboardu.
 */
public class DashboardPanelConfig {
    /** Sloupec dashboardu kde je umisten panel */
    private int dashCol;
    /** Radek dashboardu kde je umísten panel. */
    private int dashRow;
    /** Typ panelu dashboardu. */
    private DashboardPanelLibrary.WidgetType widgetType;
    /** Poradi panelu v ramci typu panelu dashboardu. */
    private int widgetIndex;

    public DashboardPanelConfig(int dashCol, int dashRow, DashboardPanelLibrary.WidgetType widgetType, int widgetIndex) {
        this.dashCol = dashCol;
        this.dashRow = dashRow;
        this.widgetType = widgetType;
        this.widgetIndex = widgetIndex;
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
