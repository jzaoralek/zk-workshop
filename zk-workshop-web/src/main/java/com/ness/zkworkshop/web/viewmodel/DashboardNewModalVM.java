package com.ness.zkworkshop.web.viewmodel;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.zul.Window;

/**
 * VM pro zalozeni noveho dashboardu.
 */
public class DashboardNewModalVM extends BaseVM {

    private int colCount;
    private String name;

    @Command
    public void createCmd(@BindingParam("modal") Window modal) {
        closeModalCmd(modal);
    }

    public void setColCount(int colCount) {
        this.colCount = colCount;
    }

    public void setName(String name) {
        this.name = name;
    }
}
