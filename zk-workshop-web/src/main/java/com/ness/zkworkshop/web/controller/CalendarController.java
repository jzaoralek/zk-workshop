package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.model.DemoCalendarModel;
import com.ness.zkworkshop.web.util.CalendarItemGenerator;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarItem;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.Label;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.Window;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CalendarController extends SelectorComposer {

    @Wire("calendars")
    private Calendars calendars;
    @Wire
    private Textbox filter;
    private DemoCalendarModel model;
    //the in editing calendar ui event
    private CalendarsEvent calendarsEvent = null;

    // @Wire("tooltipText")
    // private Label tooltipText;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        initModel();
        calendars.setModel(model);
    }

    private void initModel() {
        CalendarItemGenerator.zoneId = calendars.getDefaultTimeZone().toZoneId();
        // model = new SimpleCalendarModel(CalendarItemGenerator.generateList());
        model = new DemoCalendarModel(CalendarItemGenerator.generateList());

        DefaultCalendarItem calendarItem = new DefaultCalendarItem.Builder()
                .withTitle("Custom událost")
                .withContent("Popis custom události")
                .withBegin(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS))
                .withEnd(LocalDateTime.now().truncatedTo(ChronoUnit.HOURS).plusHours(2))
                .withZoneId(calendars.getDefaultTimeZone().toZoneId())
                .build();
        model.add(calendarItem);

        SimpleCalendarItem calendarItemSimple = new SimpleCalendarItem();
        calendarItemSimple.setTitle("Simple calendar item title");
        calendarItemSimple.setContent("Simple calendar item content");
        calendarItemSimple.setBegin(Date.from(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        calendarItemSimple.setEndDate(Date.from(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        calendarItemSimple.setStyle("background-colo0093f9r: #"); //affects the whole item
        calendarItemSimple.setHeaderStyle("background-color: red; color: white;"); //affects the header node, may override setStyle for this node
        calendarItemSimple.setContentStyle("background-color: #00f7d6; color: blue;"); //affects the content node, may override setStyle for this node
        model.add(calendarItemSimple);
    }

    //control the calendar position
    @Listen("onClick = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
    }
    @Listen("onClick = #next")
    public void gotoNext(){
        calendars.nextPage();
    }
    @Listen("onClick = #prev")
    public void gotoPrev(){
        calendars.previousPage();
    }

    //control page display
    @Listen("onClick = #pageDay")
    public void changeToDay(){
        calendars.setMold("default");
        calendars.setDays(1);
    }
    @Listen("onClick = #pageWeek")
    public void changeToWeek(){
        calendars.setMold("default");
        calendars.setDays(7);
    }
    @Listen("onClick = #pageMonth")
    public void changeToYear(){
        calendars.setMold("month");
    }

    //control the filter
    @Listen("onClick = #applyFilter")
    public void applyFilter(){
        model.setFilterText(filter.getValue());
        calendars.setModel(model);
    }
    @Listen("onClick = #resetFilter")
    public void resetFilter(){
        filter.setText("");
        model.setFilterText("");
        calendars.setModel(model);
    }

    //listen to the calendar-edit of a event data
    @Listen(CalendarsEvent.ON_ITEM_EDIT + "  = #calendars")
    public void createEvent(CalendarsEvent event) {
        calendarsEvent = event;
        //to display a shadow when editing
        calendarsEvent.stopClearGhost();

        Map<String, Object> args = new HashMap<>();
        args.put("calendarItem", event.getCalendarItem());
        Window window = (Window) Executions.createComponents("/pages/calendar-event.zul", null, args);
        window.doModal();
    }

    /*
    @Listen(CalendarsEvent.ON_ITEM_TOOLTIP +"= calendars")
    public void showTooltip(CalendarsEvent event) {
        tooltipText.setValue(event.getCalendarItem().getTitle() + "-" + event.getCalendarItem().getContent());
    }
    */
}