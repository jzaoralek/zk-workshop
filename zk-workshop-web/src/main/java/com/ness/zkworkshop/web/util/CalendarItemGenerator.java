package com.ness.zkworkshop.web.util;
import org.zkoss.calendar.impl.*;

import java.time.*;
import java.util.*;

public class CalendarItemGenerator {
    static public List<String> titles = Arrays.asList("Visit Customers", "Weekly Meeting", "Product Release");
    static public ZoneId zoneId = ZoneId.systemDefault();
    static private Random random = new Random(System.currentTimeMillis());

    static public DefaultCalendarItem generate(LocalDateTime dateTime, String title){
        DefaultCalendarItem item =  new DefaultCalendarItem.Builder()
                .withTitle(title)
                .withContent("Popis události dne: " + dateTime)
                .withBegin(dateTime)
                .withEnd(dateTime.plusHours(2))
                .withZoneId(zoneId)
                .build();

        return item;
    }

    public static List generateList(){
        List<DefaultCalendarItem> items = new LinkedList();
        int i = -1;
        for (String title : titles){
            items.add(generate(LocalDateTime.now().plusDays(i++), title));
        }
        return  items;
    }
}
