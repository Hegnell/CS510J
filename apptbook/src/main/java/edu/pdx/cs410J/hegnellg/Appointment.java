// Gustaf Hegnell
// Project 3
// CS 510J
// 7/20/2016

package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.AbstractAppointment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class represents an Appointment. An appointment has a start time, an end time, and a description of what the
 * appointment is.
 *
 * @author Gustaf Hegnell
 */
public class Appointment extends AbstractAppointment implements Comparable<Appointment> {

    private String beginTimeString; // The Start time of the appointment in String form.
    private String endTimeString;   // The end time of the appointment in String form.
    private String description;     // A description of the appointment.

    private Date beginTime;         // A date with the start time.
    private Date endTime;           // A date with the end time.


    /**
     * Default constructor for an Appointment.
     */
    public Appointment(){}

    /**
     *
     * @param beginTimeString String representing the start time of the appointment.
     * @param endTimeString String representing the end time of the appointment.
     * @param description String is a description of the appointment.
     */
    public Appointment(String beginTimeString, String endTimeString, String description) {
        this.beginTimeString = beginTimeString;
        this.endTimeString = endTimeString;
        this.description = description;

        this.beginTime = extractDate(beginTimeString);
        this.endTime = extractDate(endTimeString);
    }

    /**
     *
     * @return String representing the start time of the appointment.
     */
    @Override
    public String getBeginTimeString() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        return dateFormat.format(beginTime);
    }

    /**
     *
     * @return String representing the end time of the appointment.
     */
    @Override
    public String getEndTimeString() {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
        return dateFormat.format(endTime);
    }

    public String getFullBeginString() {
        return beginTimeString;
    }

    public String getFullEndString() {
        return endTimeString;
    }
    /**
     *
     * @return String with a description of the appointment.
     */
    @Override
    public String getDescription() {
        return description;
    }

    /**
     *
     * @return The start time of the appointment.
     */
    @Override
    public Date getBeginTime() {
        return beginTime;
    }

    /**
     *
     * @return The end time of the appointment.
     */
    @Override
    public Date getEndTime() {
        return endTime;
    }


    /**
     * This function takes another appointment as an argument and compares them first by start time, then end time, and
     * finally by description. Returns an integer representing their natural ordering.
     *
     * @param o The other appointment to compare to.
     * @return Returns an integer representing whether this object is less than, equal, or greater than the other object.
     */
    @Override
    public int compareTo(Appointment o) {
        int beginCompare = this.getBeginTime().compareTo(o.getBeginTime());
        if (beginCompare != 0) {
            return beginCompare;
        } else {
            int endCompare = this.getEndTime().compareTo(o.getEndTime());
            if (endCompare != 0) {
                return endCompare;
            } else {
                return this.getDescription().compareTo(o.getDescription());
            }
        }
    }

    /**
     * This function takes a string of a date that the user input and returns a Date object representing that date.
     *
     * @param date The date in string format.
     * @return The date parsed from the string.
     */
    private Date extractDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        try {
            Date actualDate = simpleDateFormat.parse(date);
            return actualDate;
        } catch (ParseException e) {
            System.out.println("There was an error with your date format.");
            System.exit(1);
        }
        return null;
    }

    /**
     * This function simply looks at the start and end time for this appointment and calculates how long it is in minutes.
     * @return The length of this appointment in minutes.
     */
    public long getAppointmentLength() {
        // Get the length in milliseconds
        long length = endTime.getTime() - beginTime.getTime();
        return length/1000/60;
    }
}
