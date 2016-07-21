// Gustaf Hegnell
// Project 3
// CS 510J
// 7/20/2016

package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookDumper;

import java.io.*;
import java.util.*;

/**
 * This class represents a TextDumper, which is used to dump the contents of an appointment book to a specified file. The
 * owner of the appointment book, and all of the appointments stored in it, are written to the file in a specified format.
 * The owner of the file is written to the first line, and then each appointment is written to an individual line in this
 * format: beginDate beginTime&&endDate endTime&&description
 *
 * @author Gustaf Hegnell
 */
public class TextDumper implements AppointmentBookDumper {
    private PrintWriter printWriter; // To write to the file.

    /**
     *
     * @param file The file to dump the appointment to.
     * @throws IOException If the TextDumper fails to write the contents.
     */
    public TextDumper(File file) throws IOException {
        this.printWriter = new PrintWriter(new FileWriter(file));
    }

    /**
     *
     * @param abstractAppointmentBook The appointment book whose contents should be dumped.
     * @throws IOException If the contents can not be dumped succesfully.
     */
    @Override
    public void dump(AbstractAppointmentBook abstractAppointmentBook) throws IOException {
        printWriter.println(abstractAppointmentBook.getOwnerName());

        Iterator iterator = abstractAppointmentBook.getAppointments().iterator();

        // Go through each appointment and write it out to the file.
        while (iterator.hasNext()) {
            Appointment appointment = (Appointment) iterator.next();
            printWriter.println(appointment.getFullBeginString() + "&&" + appointment.getFullEndString() + "&&" + appointment.getDescription());
        }

        printWriter.flush();
        printWriter.close();
    }
}
