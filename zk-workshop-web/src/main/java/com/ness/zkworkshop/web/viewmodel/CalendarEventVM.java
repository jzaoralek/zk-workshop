package com.ness.zkworkshop.web.viewmodel;

import org.zkoss.bind.annotation.Init;
import org.zkoss.calendar.api.CalendarItem;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.zk.ui.Executions;

import java.util.Date;

public class CalendarEventVM extends BaseVM {
    private CalendarItem item;

    @Init
    public void init() {
        item = (CalendarItem) Executions.getCurrent().getArg().get("calendarItem");
    }

    public String getTitle() {
        return item.getTitle();
    }

    public String getContent() {
        return item.getContent();
    }

    public Date getBegin() {
        return Date.from(item.getBegin());
    }

    public Date getEnd() {
        return Date.from(item.getEnd());
    }

    public String getStyle() {
        return item.getContentStyle();
    }
}