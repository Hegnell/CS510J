// Gustaf Hegnell
// Project 5
// CS 510J
// 8/10/2016

package edu.pdx.cs410J.hegnellg.client;

import edu.pdx.cs410J.AbstractAppointmentBook;


import com.google.gwt.i18n.client.DateTimeFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Date;

/**
 * This class represents an appointment book. An appointment book has an owner which is a represented by a String,
 * and also a list of appointments contained in the appointment book.
 *
 * @author Gustaf Hegnell
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

    private String owner; // The owner of the appointment book.
    private List<Appointment> appointments; // The appointments.

    /**
     * Default constructor for an appointment book.
     */
    public AppointmentBook() {
        this.appointments = new ArrayList<>();
    }

    /**
     * This is the constructor for an appointment book, an owner must be provided.
     *
     * @param owner A String representing the owner of the appointment book.
     */
    public AppointmentBook(String owner) {
        this.owner = owner;
        appointments = new ArrayList<>();
    }

    /**
     *
     * @return A String representing the owner.
     */
    @Override
    public String getOwnerName() {
        return owner;
    }

    /**
     *
     * @return A sorted list of all the appointments in the appointment book.
     */
    @Override
    public List<Appointment> getAppointments() {
        Collections.sort(appointments);
        return appointments;
    }

    /**
     * This function takes an appointment as an argument and adds it to the appointment book.
     *
     * @param appointment An appointment to be added to the book.
     */
    @Override
    public void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * This function takes two dates as parameters and returns all of the appointments in this appointment book
     * that occur between those dates.
     *
     * @param startString The start date.
     * @param endString The end date.
     * @return A list of all apppointments that occur between the given dates.
     */
    public ArrayList<Appointment> searchAppointments(String startString, String endString) {
        ArrayList<Appointment> matches = new ArrayList<>();
        Date start = extractDate(startString);
        Date end = extractDate(endString);
        for (Appointment appt : appointments) {
            if (appt.occursBetween(start, end)) {
                matches.add(appt);
            }
        }
        return matches;
    }

    /**
     * This function takes a string of a date that the user input and returns a Date object representing that date.
     *
     * @param date The date in string format.
     * @return The date parsed from the string.
     */
    public Date extractDate(String date) {
        DateTimeFormat simpleDateFormat = DateTimeFormat.getFormat("MM/dd/yyyy hh:mm a");
        try {
            Date actualDate = simpleDateFormat.parse(date);
            return actualDate;
        } catch (Exception e) {
            System.out.println("There was an error with your date format.");
        }
        return null;
    }
}

