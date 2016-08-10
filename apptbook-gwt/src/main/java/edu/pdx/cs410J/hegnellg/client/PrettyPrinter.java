// Gustaf Hegnell
// Project 5
// CS 510J
// 8/10/2016

package edu.pdx.cs410J.hegnellg.client;

import com.google.gwt.i18n.client.DateTimeFormat;
import edu.pdx.cs410J.AbstractAppointmentBook;
import java.util.*;

/**
 * This class represents a Pretty Printer, which is a class used to print out an appointment book is a human readable
 * format.
 *
 * @author Gustaf Hegnell
 */
public class PrettyPrinter {

    private StringBuilder builder;

    /**
     * Basic constructor to be used if pretty printing to standard out.
     */
    public PrettyPrinter() {
        this.builder = new StringBuilder();
    }

    /**
     * This method takes an appointment book and attempts to pretty print it. If a file has been provided as the
     * destination, then it will print to that file. If not, it will print to standard out.
     *
     * @param abstractAppointmentBook The appointment book to be pretty printed.
     */
    public String dump(AbstractAppointmentBook abstractAppointmentBook) {
        String owner = abstractAppointmentBook.getOwnerName();
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) abstractAppointmentBook.getAppointments();
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.DATE_TIME_LONG);
        builder.append("\n#################################################################################\n");
        builder.append("#################################################################################\n");
        builder.append("############################# APPOINTMENT BOOK ###################################\n");
        builder.append("#################################################################################\n");
        builder.append("#################################################################################\n\n\n");
        builder.append("Appointment Book Owner: " + owner + "\n\n");
        builder.append("Appointments are sorted chronologically by starting time. If appointments begin at the same\n");
        builder.append("time, then the one that ends first will be listed first.\n\n");


        // Go through each appointment in the book and print it out nicely.
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            builder.append("----------------------------- Appointment " + (i + 1) + " ------------------------------");
            builder.append("\nDescription: " + appointment.getDescription() + "\n");
            builder.append("Begins: " + dateFormat.format(appointment.getBeginTime()) + "\n");
            builder.append("Ends: " + dateFormat.format(appointment.getEndTime()) + "\n");
            builder.append("Duration: " + appointment.getAppointmentLength() + " minutes\n\n");
        }
        return builder.toString();
    }

}

