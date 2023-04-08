package com.ness.zkworkshop.web.controller;

import com.ness.zkworkshop.web.model.DemoCalendarModel;
import com.ness.zkworkshop.web.util.CalendarItemGenerator;
import org.zkoss.calendar.Calendars;
import org.zkoss.calendar.api.CalendarItem;
import org.zkoss.calendar.event.CalendarsEvent;
import org.zkoss.calendar.impl.*;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.event.SelectEvent;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class CalendarController extends SelectorComposer {

    @Wire
    private Calendars calendars;
    @Wire
    private Textbox filter;
    private DemoCalendarModel model;
    @Wire
    private Combobox calendarModeCombo;
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
        model = new DemoCalendarModel(CalendarItemGenerator.generateList());
        // iniciace rezimu na default mesic
        calendarModeCombo.setSelectedIndex(2);

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

    /**
     * Prechod na aktualni datum.
     */
    @Listen(Events.ON_CLICK + " = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
    }

    /**
     * Prechod na dalsi den/tyden/mesic.
     * Podle vybraneho rezimu.
     */
    @Listen(Events.ON_CLICK + " = #next")
    public void gotoNext(){
        calendars.nextPage();
    }

    /**
     * Prechod na predchozi den/tyden/mesic.
     * Podle vybraneho rezimu.
     */
    @Listen(Events.ON_CLICK + " = #prev")
    public void gotoPrev(){
        calendars.previousPage();
    }

    /**
     * Prepinani rezimu den/tyden/mesic.
     */
    @Listen(Events.ON_SELECT + " = #calendarModeCombo")
    public void changeMode(){
        String selectedValue = calendarModeCombo.getSelectedItem().getValue();
        if ("day".equals(selectedValue)) {
            calendars.setMold("default");
            calendars.setDays(1);
        } else if ("week".equals(selectedValue)) {
            calendars.setMold("default");
            calendars.setDays(7);
        } else {
            calendars.setMold("month");
        }
    }

    /*
    public void onChangeMode(SelectEvent event) {
        Comboitem selectedItem = calendarModeCombo.getSelectedItem();
        String selectedValue = selectedItem.getValue();
        if ("day".equals(selectedValue)) {
            calendars.setMold("default");
            calendars.setDays(1);
        } else if ("week".equals(selectedValue)) {
            calendars.setMold("default");
            calendars.setDays(7);
        } else {
            calendars.setMold("month");
        }
    }
     */

    /**
     * Filtrovani.
     */
    @Listen(Events.ON_CLICK + " = #applyFilter")
    public void applyFilter(){
        model.setFilterText(filter.getValue());
        calendars.setModel(model);
    }

    /**
     * Zruseni filtru.
     */
    @Listen(Events.ON_CLICK + " = #resetFilter")
    public void resetFilter(){
        filter.setText("");
        model.setFilterText("");
        calendars.setModel(model);
    }

    /**
     * Detail eventu.
     * @param event
     */
    @Listen(CalendarsEvent.ON_ITEM_EDIT + "  = #calendars")
    public void createEvent(CalendarsEvent event) {
        //to display a shadow when editing
        event.stopClearGhost();

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