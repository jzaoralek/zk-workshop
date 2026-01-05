package com.ness.zkworkshop.web.viewmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zkmax.ui.util.Toast;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

import com.ness.zkworkshop.web.model.Todo;
import lombok.Getter;
import lombok.Setter;

public class ZKupgradeVM {

    @Getter
    private List<Todo> itemList;
    @Getter
    @Setter
    private Set<Todo> itemSelectedSet = new HashSet<>();
    @Getter
    @Setter
    private Todo selectedItem;
    @Getter
    private List<String> itemList2;
    @Getter
    private ListModel<Todo> model;

    @Init
    public void init() {
        itemList = new ArrayList<>();
        itemList.add(new Todo(1, "North America"));
        itemList.add(new Todo(2, "South America"));
        itemList.add(new Todo(3, "Europe"));
        itemList.add(new Todo(4, "Asia"));
        itemList.add(new Todo(5, "Africa"));
        itemList.add(new Todo(6, "Oceania"));
        itemList.add(new Todo(7, "Antarctica"));

        // test commit 1

        itemList2 = Arrays.asList(new String[] {"North America", "South America", "Europe", "Asia", "Africa", "Oceania", "Antarctica"});

        model = new ListModelList<>(itemList);
    }

    @Command
    public void onSelectCmd() {
        Toast.show("Vybrané položky: " + itemSelectedSet);
    }

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
