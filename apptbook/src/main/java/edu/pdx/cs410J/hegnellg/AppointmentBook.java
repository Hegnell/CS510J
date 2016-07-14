// Gustaf Hegnell
// Project 2
// CS 510J
// 7/13/2016

package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.AbstractAppointmentBook;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents an appointment book. An appointment book has an owner which is a represented by a String,
 * and also a list of appointments contained in the appointment book.
 *
 * @author Gustaf Hegnell
 */
public class AppointmentBook extends AbstractAppointmentBook<Appointment> {

    private String owner;
    private List<Appointment> appointments;

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
     * @return A list of all the appointments in the appointment book.
     */
    @Override
    public List<Appointment> getAppointments() {
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
}
