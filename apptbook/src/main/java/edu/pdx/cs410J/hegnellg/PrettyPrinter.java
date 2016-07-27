// Gustaf Hegnell
// Project 4
// CS 510J
// 7/27/2016

package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.text.DateFormat;
import java.util.*;

/**
 * This class represents a Pretty Printer, which is a class used to print out an appointment book is a human readable
 * format.
 *
 * @author Gustaf Hegnell
 */
public class PrettyPrinter implements AppointmentBookDumper {
    private PrintWriter printWriter; // To print to the file.
    private boolean printToFile;     // Are we printing to a file?

    /**
     * Basic constructor to be used if pretty printing to standard out.
     */
    public PrettyPrinter() {
        this.printWriter = null;
        this.printToFile = false;
    }

    /**
     * Constructor that supports providing the printWriter to use from outside of this class.
     *
     * @param printWriter The printWriter to write to.
     */
    public PrettyPrinter(PrintWriter printWriter) {
        this.printWriter = printWriter;
        this.printToFile = true;
    }

    /**
     * Constructor to be used if pretty printing to a file.
     *
     * @param file The file destination.
     * @throws IOException
     */
    public PrettyPrinter(File file) throws IOException {
        this.printWriter = new PrintWriter(new FileWriter(file));
        this.printToFile = true;
    }

    /**
     * This method takes an appointment book and attempts to pretty print it. If a file has been provided as the
     * destination, then it will print to that file. If not, it will print to standard out.
     *
     * @param abstractAppointmentBook The appointment book to be pretty printed.
     * @throws IOException
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        String owner = abstractAppointmentBook.getOwnerName();
        ArrayList<Appointment> appointments = (ArrayList<Appointment>) abstractAppointmentBook.getAppointments();
        DateFormat dateFormat = DateFormat.getDateTimeInstance();
        printALine("\n#################################################################################", printToFile);
        printALine("#################################################################################", printToFile);
        printALine("############################# APPOINTMENT BOOK ##################################", printToFile);
        printALine("#################################################################################", printToFile);
        printALine("#################################################################################\n\n", printToFile);
        printALine("Appointment Book Owner: " + owner + "\n", printToFile);
        printALine("Appointments are sorted chronologically by starting time. If appointments begin at the same", printToFile);
        printALine("time, then the one that ends first will be listed first.\n\n", printToFile);

        // Go through each appointment in the book and print it out nicely.
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            printALine("----------------------------- Appointment " + (i + 1) + " ------------------------------", printToFile);
            printALine("\nDescription: " + appointment.getDescription() + "\n", printToFile);
            printALine("Begins: " + dateFormat.format(appointment.getBeginTime()), printToFile);
            printALine("Ends: " + dateFormat.format(appointment.getEndTime()), printToFile);
            printALine("Duration: " + appointment.getAppointmentLength() + " minutes\n", printToFile);
        }

        if (printToFile) {
            printWriter.flush();
            printWriter.close();
        }

    }

    /**
     * Helper method to determine correct destination to print the string, a file or standard out.
     * @param string The string to print.
     * @param printToFile Is the destinations a file?
     */
    private void printALine(String string, boolean printToFile) {
        if (printToFile) {
            printWriter.println(string);
        } else {
            System.out.println(string);
        }
    }
}

