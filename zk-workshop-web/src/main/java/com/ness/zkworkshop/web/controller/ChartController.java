package com.ness.zkworkshop.web.controller;

import org.zkoss.chart.Charts;
import org.zkoss.chart.Legend;
import org.zkoss.chart.PlotLine;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Wire;

public class ChartController extends SelectorComposer<Component> {

    @Wire
    Charts chart;
    

    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
                
        chart.getTitle().setX(-20);
    
        chart.getSubtitle().setX(-20);
    
        chart.getYAxis().setTitle("Teplota (°C)");
        PlotLine plotLine = new PlotLine();
        plotLine.setValue(0);
        plotLine.setWidth(1);
        plotLine.setColor("#808080");
        chart.getYAxis().addPlotLine(plotLine);

        chart.getTooltip().setValueSuffix("°C");

        Legend legend = chart.getLegend();
        legend.setLayout("vertical");
        legend.setAlign("right");
        legend.setVerticalAlign("middle");
        legend.setBorderWidth(0);
    }
}