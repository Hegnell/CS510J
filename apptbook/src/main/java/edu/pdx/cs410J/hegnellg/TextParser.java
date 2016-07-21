// Gustaf Hegnell
// Project 3
// CS 510J
// 7/20/2016

package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.AbstractAppointmentBook;
import edu.pdx.cs410J.AppointmentBookParser;
import edu.pdx.cs410J.ParserException;
import java.io.*;

/**
 * This class represents a TextParser, which is a text parser for an appointment book. It reads from a specified text
 * file, and creates a new appointment book from the appointments that are stored in the file. The text file needs to
 * be in a specific format, the first line containing the name of the owner of the appointment book, and all other lines
 * containing one appointment each in this format: beginDate beginTime&&endDate endTime&&description
 *
 * @author Gustaf Hegnell
 */
public class TextParser implements AppointmentBookParser {
    private BufferedReader bufferedReader; // To read from the file.
    private String expectedOwner; // The person who is trying to access the book.

    /**
     *
     * @param file This is the file that will be read from.
     * @param expectedOwner This specifies who is trying to access the address book.
     * @throws FileNotFoundException If the file is not found.
     */
    public TextParser(File file, String expectedOwner) throws FileNotFoundException {
        this.bufferedReader = new BufferedReader(new FileReader(file));
        this.expectedOwner = expectedOwner;
    }

    /**
     *
     * @return The appointment book created from parsing the text file.
     * @throws ParserException If the file is misformatted in any way.
     */
    @Override
    public AbstractAppointmentBook parse() throws ParserException {
        AppointmentBook appointmentBook = new AppointmentBook(expectedOwner);
        String actualOwner;

        // Do some quick checking to make sure the file isn't empty and that the owner is the same and who is using it.
        try {
            actualOwner = bufferedReader.readLine();
        } catch (IOException e) {
            throw new ParserException("Could not read the owner from the file, are you sure the file name was correct?");
        }

        if (actualOwner == null) {
            throw new ParserException("The owner could not be read from the file, the file was most likely empty.");
        }

        if (!actualOwner.equals(expectedOwner)) {
            throw new ParserException("The owner name given in the text file does not match the owner specified on the command line.");
        }


        try {
            // Prime the loop.
            String next = bufferedReader.readLine();
            int count = 1;

            while (next != null) {
                // Split the line using the delimeter.
                String[] details  = next.split("&&");
                if (details.length != 3) {
                    throw new ParserException("The formatting of appointment " + count + " is incorrect in the text file.");
                }

                // Extract the details from the line.
                String beginTimeString = details[0];
                String endTimeString = details[1];
                String description = details[2];

                // Make sure date and time are both specified.
                String[] beginInfo = beginTimeString.split(" ");
                String[] endInfo = endTimeString.split(" ");

                if (beginInfo.length != 3) {
                    throw new ParserException("The start of appointment " + count + " was not formatted correctly in the text file.");
                }

                if (endInfo.length != 3) {
                    throw new ParserException("The end of appointment " + count + " was not formatted correctly in the text file.");
                }

                // Extract the time and dates to perform error checking.
                String beginDate = beginInfo[0];
                String beginTime = beginInfo[1];
                String beginAMPM = beginInfo[2];
                String endDate = endInfo[0];
                String endTime = endInfo[1];
                String endAMPM = endInfo[2];

                // Perform error checking on the dates and times provided in the text file.
                if (!validTime(beginTime + " " + beginAMPM)) {
                    throw new ParserException("The begin time was not a valid time for appointment \"" + description + "\" in the text file.");
                } else if (!validTime(endTime + " " + endAMPM)) {
                    throw new ParserException("The end time was not a valid time for appointment \"" + description + "\" in the text file.");
                } else if (!validDate(beginDate)) {
                    throw new ParserException("The begin date was not a valid date for appointment \"" + description + "\" in the text file.");
                } else if (!validDate(endDate)) {
                    throw new ParserException("The end date was not a valid date for appointment \"" + description + "\" in the text file.");
                }

                // Make sure a description was given.
                if (description.equals("")) {
                    throw new ParserException("There was no description given for appointment " + count + " in the text file.");
                }

                // Appointment looks good, so create it and add it to the appointment book.
                Appointment appointment = new Appointment(beginTimeString, endTimeString, description);
                appointmentBook.addAppointment(appointment);
                next = bufferedReader.readLine();
                count++;
            }

            bufferedReader.close();
        } catch (IOException e) {
            throw new ParserException("There was an issue reading the appointments from the file, the file is not formatted correctly.");
        }

        // Return the created appointment book.
        return appointmentBook;
    }

    /**
     *  This function takes a time and checks whether it is valid or not.
     *
     * @param time A string representation of the time in 12 hour format..
     * @return A boolean, true if the time is a valid time in the correct format, false otherwise.
     */
    private boolean validTime(String time) {
        return time.matches("(1[0-2]|[0-9]):[0-5]\\d\\s(am|pm|AM|PM)");
    }

    /**
     *  This function takes a date and checks whether it is valid or not.
     *
     * @param date A string representation of a date in mm/dd/yyyy or m/d/yyyy format.
     * @return A boolean, true if the time is a valid date in the correct format, false otherwise.
     */
    private boolean validDate(String date) {
        return date.matches("(0?[1-9]|1[012])/(0?[1-9]|1\\d|2\\d|3[01])/\\d\\d\\d\\d");
    }
}
