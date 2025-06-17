package com.ness.zkworkshop.web.model;

import java.util.Date;

/**
 * Slouzi pro nastaveni kalendare, napr. barev polozek.
 */
public class CalendarConfig {

    public enum CalendarConfigType {
        ITEM_COLOR;
    }

    public enum CalendarConfigName {
        /* Barva polozky kalendare pro Koncici platnost certifikatu. */
        CERT_EXP_ITEM_COLOR,
        /** Barva polozky kalendare pro Planovanou odstavku systemu. */
        SYSTEM_DOWNTIME_ITEM_COLOR,
        /** Barva polozky kalendare pro Termin predlozeni vykazu.  */
        REPORT_DEADLINE_ITEM_COLOR;
    }

    private long id;
    private CalendarConfigType type;
    private CalendarConfigName name;
    private String value;
    private Date modifAt;

    public long getId() {
        return id;
    }

    public CalendarConfigType getType() {
        return type;
    }

    public void setType(CalendarConfigType type) {
        this.type = type;
    }

    public CalendarConfigName getName() {
        return name;
    }

    public void setName(CalendarConfigName name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Date getModifAt() {
        return modifAt;
    }

    public void setModifAt(Date modifAt) {
        this.modifAt = modifAt;
    }

    public String getModifBy() {
        return modifBy;
    }

    public void setModifBy(String modifBy) {
        this.modifBy = modifBy;
    }

    private String modifBy;
}