package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.config.DashboardPanelConfig;
import com.ness.zkworkshop.web.config.SidebarPageConfig;
import com.ness.zkworkshop.web.config.SidebarPageConfigImpl;
import com.ness.zkworkshop.web.converer.EnumLabelConverter;
import org.zkoss.bind.Converter;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zul.Window;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public abstract class BaseVM {

    //config provider
    protected SidebarPageConfig pageConfig = new SidebarPageConfigImpl();

    public List<DashboardPanelConfig.WidgetType> getWidgetTypes() {
        return Arrays.asList(DashboardPanelConfig.WidgetType.values());
    }

    protected static void openModal(String page, Map<String, Object> args) {
        Window window = (Window) Executions.createComponents(page, null, args);
        window.doModal();
    }

    @Command
    public void closeModalCmd(@BindingParam("modal") Window modal) {
        modal.detach();
    }

    public Converter<String, Enum<?>, Component> getEnumLabelConverter() {
        return new EnumLabelConverter();
    }
}
