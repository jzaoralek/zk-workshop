package com.ness.zkworkshop.web.model;

import java.util.Date;

public class Message {

    private String from;
    private String subject;
    private Date sent;
    private String text;

    public Message(String from, String subject, Date sent, String text) {
        this.from = from;
        this.subject = subject;
        this.sent = sent;
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getSent() {
        return sent;
    }

    public void setSent(Date sent) {
        this.sent = sent;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
