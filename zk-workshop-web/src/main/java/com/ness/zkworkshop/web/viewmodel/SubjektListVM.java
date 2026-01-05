package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.model.Subjekt;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.Executions;

import java.util.ArrayList;
import java.util.List;

public class SubjektListVM {

    private List<Subjekt> subjektList;
    private Subjekt subjektSelected;

    @Init
    public void init() {
        subjektList = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            subjektList.add(new Subjekt(i, "kod"+i, "nazev" + i));
        }

        // test commit 3
    }

    @Command
    public void detail() {
        Executions.sendRedirect("/pages/subjekt-detail.zul?id="+subjektSelected.getId());
    }

    public List<Subjekt> getSubjektList() {
        return subjektList;
    }

    public Subjekt getSubjektSelected() {
        return subjektSelected;
    }

    public void setSubjektSelected(Subjekt subjektSelected) {
        this.subjektSelected = subjektSelected;
    }
}