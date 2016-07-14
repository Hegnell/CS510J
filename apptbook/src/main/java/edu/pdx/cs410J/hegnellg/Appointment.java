// Gustaf Hegnell
// Project 2
// CS 510J
// 7/13/2016

package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.AbstractAppointment;

/**
 * This class represents an Appointment. An appointment has a start time, an end time, and a description of what the
 * appointment is.
 *
 * @author Gustaf Hegnell
 */
public class Appointment extends AbstractAppointment {

    private String beginTimeString; // The Start time of the appointment in String form.
    private String endTimeString;   // The end time of the appointment in String form.
    private String description;     // A description of the appointment.

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
    }

    /**
     *
     * @return String representing the start time of the appointment.
     */
    @Override
    public String getBeginTimeString() {
        return beginTimeString;
    }

    /**
     *
     * @return String representing the end time of the appointment.
     */
    @Override
    public String getEndTimeString() {
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
}
