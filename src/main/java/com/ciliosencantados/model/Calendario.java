package com.ciliosencantados.model;

import com.ciliosencantados.bean.CalendarFactory;
import com.ciliosencantados.util.DateTimeUtil;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.List;

public class Calendario {
    public static final String CALENDAR_ID = "primary";
    public static final String GREEN_COLOR_ID = "10";
    private final Calendar service;
    private DateTime dateTime;

    public Calendario() {
        this.service = CalendarFactory.CALENDAR;
    }

    public List<Event> listarEventos() throws IOException {
        return getEventsOfTheDay().getItems().stream().filter(it -> GREEN_COLOR_ID.equals(it.getColorId())).toList();
    }

    private Events getEventsOfTheDay() throws IOException {
        return getPrimaryCalendarEvents().setTimeMax(getTimeMax()).setTimeMin(getTimeMin()).setOrderBy("startTime").setSingleEvents(true).execute();
    }

    private DateTime getTimeMin() {
        return DateTimeUtil.atStartOfDay(dateTime);
    }

    private DateTime getTimeMax() {
        return DateTimeUtil.atEndOfDay(dateTime);
    }

    private Calendar.Events.List getPrimaryCalendarEvents() throws IOException {
        return getEvents().list(CALENDAR_ID);
    }

    private Calendar.Events getEvents() {
        return this.service.events();
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
