// Gustaf Hegnell
// Project 4
// CS 510J
// 7/27/2016

package edu.pdx.cs410J.hegnellg;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This servlet ultimately provides a REST API for working with an
 * <code>AppointmentBook</code>.  However, in its current state, it is an example
 * of how to use HTTP and Java servlets to store simple key/value pairs.
 */
public class AppointmentBookServlet extends HttpServlet
{
    private final Map<String, AppointmentBook> data = new HashMap<>(); // A Hashmap of all the appointment books.

    /**
     * Handles an HTTP GET request from a client by writing the value of the key
     * specified in the "key" HTTP parameter to the HTTP response.  If the "key"
     * parameter is not specified, all of the key/value pairs are written to the
     * HTTP response.
     */
    @Override
    protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        // Try and get the params.
        String owner = getParameter("owner", request);
        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);

        PrintWriter pw = response.getWriter();
        PrettyPrinter prettyPrinter = new PrettyPrinter(pw);

        // Default endpoint was hit.
        if (owner == null) {
            pw.println("The base endpoint currently has no functionality.");
            pw.println("Please provide an owner in the request to look up appointments.");
            response.setStatus( HttpServletResponse.SC_OK );
            return;
        }

        AppointmentBook book = this.data.get(owner);

        // Check for missing parameters.
        if (beginTime == null && endTime != null) {
            missingRequiredParameter(response, "beginTime");
            return;
        } else if (beginTime != null && endTime == null) {
            missingRequiredParameter(response, "endTime");
            return;
        // We are in search mode
        } else if (beginTime != null && endTime != null) {
            // Make sure dates were valid.
            if (!validDate(beginTime)) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Begin date was invalid.");
                return;
            } else if (!validDate(endTime)) {
                response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "End date was invalid.");
                return;
            }
            if (book == null) {
                pw.println("There is no appointment book for this owner.");
                response.setStatus( HttpServletResponse.SC_OK );
                return;
            } else {
                ArrayList<Appointment> search = book.searchAppointments(beginTime, endTime);
                AppointmentBook temp = new AppointmentBook(owner);
                for (Appointment appt : search) {
                    temp.addAppointment(appt);
                }
                prettyPrinter.dump(temp);
            }
        } else {
            if (book == null) {
                pw.println("No Appointment Book was found for this owner.");
            } else {
                prettyPrinter.dump(book);
            }
        }

        pw.flush();
        response.setStatus( HttpServletResponse.SC_OK );
    }

    /**
     * Handles an HTTP POST request by storing the key/value pair specified by the
     * "key" and "value" request parameters.  It writes the key/value pair to the
     * HTTP response.
     */
    @Override
    protected void doPost( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException
    {
        response.setContentType( "text/plain" );

        // Need to get the appointment book of the owner first.
        String owner = getParameter("owner", request);
        String beginTime = getParameter("beginTime", request);
        String endTime = getParameter("endTime", request);
        String description = getParameter("description", request);

        // Check for missing parameters.
        if (owner == null) {
            missingRequiredParameter(response, "owner");
            return;
        } else if (beginTime == null) {
            missingRequiredParameter(response, "beginTime");
            return;
        } else if (endTime == null) {
            missingRequiredParameter(response, "endTime");
            return;
        } else if (description == null) {
            missingRequiredParameter(response, "description");
            return;
        }

        // Make sure dates were valid.
        if (!validDate(beginTime)) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "Begin date was invalid.");
            return;
        } else if (!validDate(endTime)) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, "End date was invalid.");
            return;
        }

        // Look up book, if none exists make a new one.
        AppointmentBook book = this.data.get(owner);
        if (book == null) {
            book = new AppointmentBook(owner);
        }

        // Create the appointment and add it to the address book.
        Appointment appointment = new Appointment(beginTime, endTime, description);
        book.addAppointment(appointment);

        this.data.put(owner, book);

        response.setStatus( HttpServletResponse.SC_OK);
    }

    /**
     * Writes an error message about a missing parameter to the HTTP response.
     *
     * The text of the error message is created by {@link Messages#missingRequiredParameter(String)}
     */
    private void missingRequiredParameter( HttpServletResponse response, String parameterName )
        throws IOException
    {
        String message = Messages.missingRequiredParameter(parameterName);
        response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED, message);
    }

    /**
     * Returns the value of the HTTP request parameter with the given name.
     *
     * @return <code>null</code> if the value of the parameter is
     *         <code>null</code> or is the empty string
     */
    private String getParameter(String name, HttpServletRequest request) {
      String value = request.getParameter(name);
      if (value == null || "".equals(value)) {
        return null;

      } else {
        return value;
      }
    }

    /**
     *  This function takes a date and checks whether it is valid or not.
     *
     * @param date A string representation of a date in mm/dd/yyyy or m/d/yyyy format.
     * @return A boolean, true if the time is a valid date in the correct format, false otherwise.
     */
    private static boolean validDate(String date) {
        // Wasn't sure how specific this had to be, but covers most.
        return date.matches("(0?[1-9]|1[012])/(0?[1-9]|1\\d|2\\d|3[01])/\\d\\d\\d\\d\\s(1[0-2]|[0-9]):[0-5]\\d\\s(am|pm|AM|PM)");
    }
}
