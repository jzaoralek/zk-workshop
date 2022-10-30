package com.ness.zkworkshop.web.controller;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zkex.zul.Colorbox;
import org.zkoss.zul.Panel;
import org.zkoss.zul.Textbox;

public class DashboardPanelController extends SelectorComposer<Component> {

    private Panel panel;

    @Wire("#colorPicker")
    private Colorbox colorPicker;

    @Wire("#borderColorPicker")
    private Colorbox borderColorPicker;

    @Wire("#titleTextbox")
    private Textbox titleTextbox;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        panel = (Panel)comp;
        colorPicker.setColor("#F9FCFF");
        borderColorPicker.setColor("#d9d9d9");
        titleTextbox.setValue(panel.getTitle());
        setPanelStyle();
    }

    @Listen("onChange = #colorPicker")
    public void onColorChanged(Event event) {
        setPanelStyle();
    }

    @Listen("onChange = #borderColorPicker")
    public void onBorderColorChanged(Event event) {
        setPanelStyle();
    }

    @Listen("onChange = #titleTextbox")
    public void onTitleChanged(Event event) {
        panel.setTitle(titleTextbox.getValue());
    }

    @Listen("onOK = #titleTextbox")
    public void onTitleOK(Event event) {
        panel.setTitle(titleTextbox.getValue());
    }

    /**
     * Nastaveni barvy pozadi a ohraniceni podle colorpickeru.
     */
    private void setPanelStyle() {
        panel.setStyle("background: " + colorPicker.getColor() + "; border-color: " + borderColorPicker.getColor());
    }
}
