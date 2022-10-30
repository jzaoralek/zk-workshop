package com.ness.zkworkshop.web.util;

import org.zkoss.util.resource.Labels;

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
}
