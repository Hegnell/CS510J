// Gustaf Hegnell
// Project 4
// CS 510J
// 7/27/2016

package edu.pdx.cs410J.hegnellg;

import com.google.common.annotations.VisibleForTesting;
import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;

/**
 * A helper class for accessing the rest client
 */
public class AppointmentBookRestClient extends HttpRequestHelper
{
    private static final String WEB_APP = "apptbook";
    private static final String SERVLET = "appointments";

    private final String url;


    /**
     * Creates a client to the appointment book REST service running on the given host and port
     * @param hostName The name of the host
     * @param port The port
     */
    public AppointmentBookRestClient( String hostName, int port )
    {
        this.url = String.format( "http://%s:%d/%s/%s", hostName, port, WEB_APP, SERVLET );
    }

    /**
     *
     * @param owner The owner of the appointment book.
     * @param begin The begin time to search between.
     * @param end The end time to search between.
     * @return The HTTP response.
     * @throws IOException
     */
    public Response searchForAppointments(String owner, String begin, String end) throws IOException {
        return getFromMyURL("owner", owner, "beginTime", begin, "endTime", end);
    }

    /**
     * A helper method that takes a set of keys and values and makes a GET to the server.
     *
     * @param keysAndValues A set of keys and values.
     * @return The HTTP response.
     * @throws IOException
     */
    public Response getFromMyURL(String... keysAndValues) throws IOException {
      return get(this.url, keysAndValues);
    }

    /**
     *
     * @param owner The owner of the appointment book.
     * @param beginTime The begin time of the appointment.
     * @param endTime The end time of the appointment.
     * @param description A description of the appointment.
     * @return The HTTP response from the server.
     * @throws IOException
     */
    public Response addAppointment(String owner, String beginTime, String endTime, String description) throws IOException {
        return postToMyURL("owner", owner, "beginTime", beginTime, "endTime", endTime, "description", description);
    }

    /**
     * A helper method that takes an array of keys and values, and makes a post to the server.
     *
     * @param keysAndValues The set of keys and values.
     * @return The HTTP response from the server.
     * @throws IOException
     */
    @VisibleForTesting
    public Response postToMyURL(String... keysAndValues) throws IOException {
        return post(this.url, keysAndValues);
    }
}
