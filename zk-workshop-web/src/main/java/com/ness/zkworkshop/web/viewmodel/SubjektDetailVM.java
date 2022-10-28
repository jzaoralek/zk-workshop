package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.model.Subjekt;
import org.zkoss.bind.annotation.Init;
import org.zkoss.bind.annotation.QueryParam;

public class SubjektDetailVM {

    private Subjekt subjekt;

    @Init
    public void init(@QueryParam("id") final String id) {
        subjekt = new Subjekt(Long.valueOf(id),"kod" + id, "nazev" + id);
    }

    public Subjekt getSubjekt() {
        return subjekt;
    }
}
