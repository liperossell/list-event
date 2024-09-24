package com.ciliosencantados.controller;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.ciliosencantados.model.Calendario;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Objects.nonNull;

@Path("/")
public class EventController {
    @GET
    public List<Event> list(@QueryParam("dateTime") DateTime dateTime) throws IOException {
        ZonedDateTime zdt = ZonedDateTime.now();
        DateTime dateTime1 = new DateTime(zdt.toEpochSecond() * 1000L);

        if (nonNull(dateTime)) {
            dateTime1 = dateTime;
        }

        Calendario calendario = new Calendario();
        calendario.setDateTime(dateTime1);
        return calendario.listarEventos();
    }
}
