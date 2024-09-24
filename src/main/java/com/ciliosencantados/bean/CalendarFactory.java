package com.ciliosencantados.bean;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.DataStoreCredentialRefreshListener;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Set;

@ApplicationScoped
public class CalendarFactory {
    private static final NetHttpTransport HTTP_TRANSPORT;
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static final String APPLICATION_NAME = "EncantadasLash";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final Set<String> SCOPES = Set.of(CalendarScopes.CALENDAR);
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final Calendar CALENDAR;

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            CALENDAR = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials()).setApplicationName(APPLICATION_NAME).build();
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Credential getCredentials() throws IOException {
        // Load client secrets.
        InputStream in = CalendarFactory.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));
        FileDataStoreFactory dataStore = new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH));
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(dataStore).addRefreshListener(new DataStoreCredentialRefreshListener(clientSecrets.getDetails().getClientId(), dataStore)).setAccessType("offline").build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8989).build();

        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    public static Calendar INSTANCE() {
        return CALENDAR;
    }
}
