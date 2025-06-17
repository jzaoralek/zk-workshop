package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.model.CalendarConfig;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;

import java.util.ArrayList;
import java.util.List;

/**
 * VM pro konfiguraci barev polozek kalendare.
 */
public class CalendarConfigVM {

    public List<CalendarConfig> getCalCfgList() {
        return calCfgList;
    }

    @Init
    public void init() {
        this.calCfgList = buildFakeCfgList();
    }

    @Command
    public void submitCmd() {
        this.calCfgList.forEach(i -> System.out.println(i.getName() + " - " + i.getValue()));
    }

    private List<CalendarConfig> buildFakeCfgList() {
        List<CalendarConfig> ret = new ArrayList<>();
        CalendarConfig item1 = new CalendarConfig();
        item1.setType(CalendarConfig.CalendarConfigType.ITEM_COLOR);
        item1.setName(CalendarConfig.CalendarConfigName.CERT_EXP_ITEM_COLOR);
        item1.setValue("#FFCC00");
        ret.add(item1);

        CalendarConfig item2 = new CalendarConfig();
        item2.setType(CalendarConfig.CalendarConfigType.ITEM_COLOR);
        item2.setName(CalendarConfig.CalendarConfigName.SYSTEM_DOWNTIME_ITEM_COLOR);
        item2.setValue("#99FF99");
        ret.add(item2);

        CalendarConfig item3 = new CalendarConfig();
        item3.setType(CalendarConfig.CalendarConfigType.ITEM_COLOR);
        item3.setName(CalendarConfig.CalendarConfigName.REPORT_DEADLINE_ITEM_COLOR);
        item3.setValue("#FF0033");
        ret.add(item3);

        return ret;
    }

    private List<CalendarConfig> calCfgList;
}
