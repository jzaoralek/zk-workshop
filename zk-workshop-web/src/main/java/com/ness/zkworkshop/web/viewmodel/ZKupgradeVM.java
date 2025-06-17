package com.ness.zkworkshop.web.viewmodel;

import org.zkoss.bind.annotation.Command;
import org.zkoss.zkmax.ui.util.Toast;

public class ZKupgradeVM {

    @Command
    public void showToastWarnCmd() {
        Toast.show("Upozornění", "warning", "top_right", 5000, true);
    }

    @Command
    public void showToastInfoCmd() {
        Toast.show("Info text", "info", "top_right", 5000, true);
    }

    @Command
    public void showToastErrorCmd() {
        Toast.show("Chyba", "error", "top_right", 5000, true);
    }
}
