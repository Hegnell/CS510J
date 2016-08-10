// Gustaf Hegnell
// Project 5
// CS 510J
// 8/10/2016

package edu.pdx.cs410J.hegnellg.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import edu.pdx.cs410J.hegnellg.client.Appointment;
import edu.pdx.cs410J.hegnellg.client.AppointmentBook;
import edu.pdx.cs410J.hegnellg.client.PingService;
import java.util.*;


/**
 * The server-side implementation of the appointment book service.
 */
public class PingServiceImpl extends RemoteServiceServlet implements PingService
{
  // A map to store all of the appointment books.
  private final Map<String, AppointmentBook> data = new HashMap<>();

  /**
   * This function takes an owner and an appointment, looks up the appointment book belonging to that owner, and
   * adds the appointment to the book.
   *
   * @param owner The appointment book owner.
   * @param appointment The appointment to add.
   * @return  The newly created appointment.
     */
  @Override
  public Appointment ping(String owner, Appointment appointment) {
    // Look up book, if none exists make a new one.
    AppointmentBook book = this.data.get(owner);
    if (book == null) {
      book = new AppointmentBook(owner);
    }
    book.addAppointment(appointment);

    this.data.put(owner, book);
    return appointment;
  }

  /**
   * This function looks up an appointment book belonging to the specified owner, and then returns that book.
   *
   * @param owner The appointment book owner.
   * @return The appointment book belonging to the owner.
     */
  @Override
  public AppointmentBook search(String owner) {
    AppointmentBook book = this.data.get(owner);
    if (book == null) {
      return null;
    } else {
      return book;
    }
  }

  /**
   *
   * @param unhandled The throwable action.
     */
  @Override
  protected void doUnexpectedFailure(Throwable unhandled) {
    unhandled.printStackTrace(System.err);
    super.doUnexpectedFailure(unhandled);
  }

}
