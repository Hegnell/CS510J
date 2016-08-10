// Gustaf Hegnell
// Project 5
// CS 510J
// 8/10/2016

package edu.pdx.cs410J.hegnellg.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import java.util.ArrayList;

/**
 * A GWT remote service that returns appointment books.
 */
@RemoteServiceRelativePath("ping")
public interface PingService extends RemoteService {

    /**
     *  This function takes an owner of an appointment book and an appointment,
     *  and then adds that appointments to their appointment book.
     *
     * @param owner The appointment book owner.
     * @param appointment The appointment to add.
     * @return The created appointment.
     */
    Appointment ping(String owner, Appointment appointment);

    /**
     * This function takes an owner as a parameter and returns the appointment book associated with that
     * owner.
     *
     * @param owner The appointment book owner.
     * @return The appointment book that belongs to that owner.
     */
    AppointmentBook search(String owner);

}
