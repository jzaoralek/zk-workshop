package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.model.EmailContacts;
import com.ness.zkworkshop.web.model.EmailLabels;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.Init;
import org.zkoss.zul.ListModel;
import org.zkoss.zul.ListModelList;

public class ChosenboxVM {

    private ListModelList<String> contactsModel = new ListModelList<String>(EmailContacts.getContacts());
    private ListModel<String> labelsModel = new ListModelList<String>(EmailLabels.getLabels());

    @Init
    public void init() {
    }

    @Command("newContact")
    public void newContact(@BindingParam("contact") String contact) {
        contactsModel.add(contact);
        contactsModel.addToSelection(contact);
    }

    public ListModel<String> getContactsModel() {
        return contactsModel;
    }

    public ListModel<String> getLabelsModel() {
        return labelsModel;
    }
}