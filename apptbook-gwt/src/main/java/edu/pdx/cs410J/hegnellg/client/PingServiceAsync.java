// Gustaf Hegnell
// Project 5
// CS 510J
// 8/10/2016

package edu.pdx.cs410J.hegnellg.client;

import com.google.gwt.user.client.rpc.AsyncCallback;


/**
 * The asynchronous client side interface for communicating with the server.
 */
public interface PingServiceAsync {

    /**
     * This function takes an owner and an appointment to add to that owners appointment book, as well as an
     * asynchronous callback function to return to once it has been added on the server.
     *
     * @param owner The appointment book owner.
     * @param appointment The appointment to add.
     * @param async The asynchronous callback function.
     */
    void ping(String owner, Appointment appointment, AsyncCallback<Appointment> async);

    /**
     * This function takes an owner and returns the appointments book beloning to that owner using an asynchronous
     * callback.
     *
     * @param owner The appointment book owner.
     * @param async The asynchronous callback function.
     */
    void search(String owner, AsyncCallback<AppointmentBook> async);
}
