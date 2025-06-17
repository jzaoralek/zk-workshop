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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
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
    @Wire
    private Label tooltipText;
    @Wire
    private Label currentMonth;

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
        calendarItemSimple.setTitle("Simple item content");
        calendarItemSimple.setContent("Simple item title");
        calendarItemSimple.setBegin(Date.from(LocalDateTime.now().plusHours(1).truncatedTo(ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        calendarItemSimple.setEndDate(Date.from(LocalDateTime.now().plusHours(2).truncatedTo(ChronoUnit.HOURS).atZone(ZoneId.systemDefault()).toInstant()));
        calendarItemSimple.setStyle("background-colo0093f9r: #"); //affects the whole item
        calendarItemSimple.setHeaderStyle("background-color: red; color: white;"); //affects the header node, may override setStyle for this node
        calendarItemSimple.setContentStyle("background-color: #00f7d6; color: blue;"); //affects the content node, may override setStyle for this node
        model.add(calendarItemSimple);
        
        updateCurrentMonth();
    }
    
    private void updateCurrentMonth() {
    	LocalDate localdate = calendars.getCurrentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    	String month = localdate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("cs", "CZ"));
    	
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.FULL, new Locale("cs", "CZ")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("cs", "CZ")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.NARROW, new Locale("cs", "CZ")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.NARROW_STANDALONE, new Locale("cs", "CZ")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.SHORT, new Locale("cs", "CZ")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("cs", "CZ")));
    	
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.FULL, new Locale("en", "US")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, new Locale("en", "US")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.NARROW, new Locale("en", "US")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.NARROW_STANDALONE, new Locale("en", "US")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.SHORT, new Locale("en", "US")));
    	System.out.println(localdate.getMonth().getDisplayName(TextStyle.SHORT_STANDALONE, new Locale("en", "US")));
    	
    	currentMonth.setValue(month + " " + localdate.getYear());
    }

    /**
     * Prechod na aktualni datum.
     */
    @Listen(Events.ON_CLICK + " = #today")
    public void gotoToday(){
        TimeZone timeZone = calendars.getDefaultTimeZone();
        calendars.setCurrentDate(Calendar.getInstance(timeZone).getTime());
        updateCurrentMonth();
    }

    /**
     * Prechod na dalsi den/tyden/mesic.
     * Podle vybraneho rezimu.
     */
    @Listen(Events.ON_CLICK + " = #next")
    public void gotoNext(){
        calendars.nextPage();
        updateCurrentMonth();
    }

    /**
     * Prechod na predchozi den/tyden/mesic.
     * Podle vybraneho rezimu.
     */
    @Listen(Events.ON_CLICK + " = #prev")
    public void gotoPrev(){
        calendars.previousPage();
        updateCurrentMonth();
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

    @Listen(CalendarsEvent.ON_ITEM_TOOLTIP +"= calendars")
    public void showTooltip(CalendarsEvent event) {
        tooltipText.setValue(event.getCalendarItem().getContent() + "\n" + event.getCalendarItem().getTitle());
    }
}