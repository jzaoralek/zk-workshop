package com.ness.zkworkshop.web.viewmodel;

import java.util.Arrays;
import java.util.List;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zk.ui.util.Clients;

import com.ness.zkworkshop.web.service.UserCredential;

public class FormVM {

    private enum StavKeDniSelectionMode {INTERVAL, LIST, ALL}

    private StavKeDniSelectionMode stavKeDniMode;

    @Init
    public void init() {
        stavKeDniMode = StavKeDniSelectionMode.INTERVAL;
    }

    @Command
    public void submitCmd() {
        Clients.showNotification(stavKeDniMode.name());
    }

    public List<StavKeDniSelectionMode> getStavKeDniModeValues(){
        return Arrays.asList(StavKeDniSelectionMode.values());
    }

    public StavKeDniSelectionMode getStavKeDniMode() {
        return stavKeDniMode;
    }
    public void setStavKeDniMode(StavKeDniSelectionMode stavKeDniMode) {
        this.stavKeDniMode = stavKeDniMode;
    }
}