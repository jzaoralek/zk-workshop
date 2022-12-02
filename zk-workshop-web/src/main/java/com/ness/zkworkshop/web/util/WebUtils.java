package com.ness.zkworkshop.web.util;

import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

import javax.servlet.http.HttpServletRequest;

public final class WebUtils {

    private WebUtils() {};

    /**
     * Metoda pro ziskani lokalizovane reprezentace polozky enumu z resource bundle
     * @param e
     * @return
     */
    public static String getMessageItemFromEnum(Enum<?> e){
        return Labels.getLabel("enum." + e.getDeclaringClass().getSimpleName() + "." + e.name());
    }

    public static String getRequestParameter(String atr) {
        HttpServletRequest req = getHttpServletRequest();
        if (req == null) {
            return null;
        }
        return req.getParameter(atr);
    }

    public static HttpServletRequest getHttpServletRequest() {
        Execution exec = Executions.getCurrent();
        if (exec == null) {
            return null;
        }
        return (HttpServletRequest) exec.getNativeRequest();
    }
}
