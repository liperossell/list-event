package com.ciliosencantados.model;

import com.ciliosencantados.bean.CalendarFactory;
import com.ciliosencantados.exception.BusinessException;
import com.ciliosencantados.util.DateTimeUtil;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Calendario {
    public static final String CALENDAR_ID = "primary";
    private final Calendar service;
    private DateTime dateTime;

    public Calendario() {
        this.service = CalendarFactory.INSTANCE();
    }

    public List<Event> listarEventos() throws IOException {
        return getEventsOfTheDay().getItems();
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

    public String salvarEvento() throws IOException, BusinessException {
        if (existEvent()) {
            throw new BusinessException("Já existe um evento nesse horário");
        }
        Event event = createEvent();
        return String.format("Event created: %s\n", event.getHtmlLink());
    }

    private boolean existEvent() throws IOException {
        return getEventsOfTheDay().getItems().stream().anyMatch(it -> this.dateTime.getValue() >= it.getStart().getDateTime().getValue() && this.dateTime.getValue() <= it.getEnd().getDateTime().getValue());
    }

    private Event createEvent() throws IOException {
        Event event = new Event().setSummary("Novo agendamento").setLocation("Rua Doralice Ramos Pinho, 664. Jardim Cidade Florianópolis, São José-SC").setDescription("Isso é um teste");
        EventDateTime start = getStartEventDateTime();
        EventDateTime end = getEndEventDateTime();
        event.setStart(start);
        event.setEnd(end);
        getEvents().insert(CALENDAR_ID, event).execute();

        return event;
    }

    private EventDateTime getEndEventDateTime() {
        return new EventDateTime().setDateTime(DateTimeUtil.addHours(dateTime, 2)).setTimeZone("America/Sao_Paulo");
    }

    private EventDateTime getStartEventDateTime() {
        return new EventDateTime().setDateTime(dateTime).setTimeZone("America/Sao_Paulo");
    }
    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
