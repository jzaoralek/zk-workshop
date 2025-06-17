package com.ness.zkworkshop.web.viewmodel;

import com.ness.zkworkshop.web.model.Message;
import com.ness.zkworkshop.web.model.Subjekt;
import org.zkoss.bind.annotation.Init;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MessageListVM {

    public List<Message> getMessageList() {
        return messageList;
    }

    @Init
    public void init() {
        messageList = new ArrayList<>();
        Date received = null;
        for (int i = 1; i <= 10; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(i, Calendar.DATE);
            messageList.add(new Message("sender"+i+"@gmail.com", "Subject "+i, cal.getTime(), "Text zprávy " + i + " ..."));
        }
    }

    private List<Message> messageList;
}
