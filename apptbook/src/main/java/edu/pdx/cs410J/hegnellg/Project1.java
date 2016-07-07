// Gustaf Hegnell
// Project 1
// CS 510J
// 7/6/2016

package edu.pdx.cs410J.hegnellg;

import java.util.ArrayList;

/**
 * The main class for the CS410J appointment book Project 1, this class parses the command line arguments
 * provided to the program, creates an appointment, creates ann appointment book, and then puts that
 * appointment in the appointment book. It gets the owner of the appointment book, description, and time of
 * the appointment all from the command line. Providing the -README option will print out a description of the program
 * along with how to use it.
 *
 * @author Gustaf Hegnell
 */
public class Project1 {

    /**
     * The main method, does all of the command line parsing and creates the appointment using those details.
     *
     * @param args The command line arguments provided.
     */
    public static void main(String[] args) {

        if (args.length == 0) {
            exitWithError("Missing command line arguments");
        }

        boolean printDescription = false;
        boolean doneWithOptions = false;
        ArrayList<String> options = new ArrayList<>();
        ArrayList<String> appointmentArgs = new ArrayList<>();

        // Go through the command line arguments and split them into options or args.
        for (String arg : args) {
            // Program should not do anything if -README is specified, so it needs to be included here first.
            if (arg.equals("-README")) {
                exitWithReadme();
            }
            // Check for empty quotes basically.
            if (arg.length() < 1) {
                exitWithError("Looks like you provided an empty argument, did you forget to include a description?");
            }
            // It's an option.
            if (arg.charAt(0) == '-') {
                if (!doneWithOptions) {
                    options.add(arg);
                } else {
                    exitWithError("Whoops, looks like your options are out of order. Options have to come before args.");
                }
            // It's an arg.
            } else {
                appointmentArgs.add(arg);
                doneWithOptions = true;
            }
        }

        // Go through the options provided and determine course of action.
        for (String option : options) {
            if (option.equals("-print")) {
                printDescription = true;
            } else {
                exitWithError("Unexpected option provided. Use -README to see the available options.");
            }
        }

        // Currently there has to be exactly six arguments provided.
        if (appointmentArgs.size() > 6) {
            exitWithError("Too many args provided. Arguments are owner, description, begin time, and end time (in that order).");
        } else if (appointmentArgs.size() < 6) {
            exitWithError("Too few args provided. Arguments are owner, description, begin time, and end time (in that order).");
        }

        // Separate out the provided args into what they represent.
        String owner = appointmentArgs.get(0);
        String description = appointmentArgs.get(1);
        String beginDateString = appointmentArgs.get(2);
        String beginTimeString = appointmentArgs.get(3);
        String endDateString = appointmentArgs.get(4);
        String endTimeString = appointmentArgs.get(5);


        // Perform error checking on the dates and times provided.
        if (!validTime(beginTimeString)) {
            exitWithError("The begin time was incorrectly formatted. Valid format is hh:mm and only real times are accepted.");
        } else if (!validTime(endTimeString)) {
            exitWithError("The end time was incorrectly formatted. Valid format is hh:mm and only real times are accepted.");
        } else if (!validDate(beginDateString)) {
            exitWithError("The begin date was incorrectly formatted. Valid dates are m/d/yyyy or mm/dd/yyyy");
        } else if (!validDate(endDateString)) {
            exitWithError("The end date was incorrectly formatted. Valid dates are m/d/yyyy or mm/dd/yyyy");
        }

        // Create the appointment and add it to the appointment book.
        AppointmentBook appointmentBook = new AppointmentBook(owner);
        Appointment appointment = new Appointment(beginDateString + " " + beginTimeString, endDateString + " " + endTimeString, description);
        appointmentBook.addAppointment(appointment);

        // If -print option was provided, print out description of new appointment.
        if (printDescription) {
            System.out.println("\nDescription of created appointment:\n");
            System.out.println(appointment);
        }
    }

    /**
     *  This function takes a time and checks whether it is valid or not.
     *
     * @param time A string representation of the time in hh:mm format.
     * @return A boolean, true if the time is a valid time in the correct format, false otherwise.
     */
    private static boolean validTime(String time) {
        if (time.matches("([01]\\d|2[0-3]|[0-9]):[0-5]\\d")) {
            return true;
        } else {
            return false;
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
        if (date.matches("(0?[1-9]|1[012])/(0?[1-9]|1\\d|2\\d|3[01])/\\d\\d\\d\\d")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *  This function takes a string describing an error, prints it to the user, and exits with an error.
     *
     * @param error The error.
     */
    private static void exitWithError(String error) {
        System.err.println(error);
        System.exit(1);
    }

    /**
     *  This function prints out a decription of the program and exits afterwards.
     */
    private static void exitWithReadme() {
        System.out.println("\n\nGustaf Hegnell - Project 1\n");
        System.out.println("This is a small program which allows the creation of an appointment from");
        System.out.println("command line arguments, and adds that appointment to an appointment book.");
        System.out.println("An appointment consists of a start time, an end time, and a description of");
        System.out.println("the appointment, which can be provided using the command line options described below.\n");
        System.out.println("usage: [options] <args>");
        System.out.println("args are (in this order):");
        System.out.println("\towner\t\t\tThe person whose owns the appt book");
        System.out.println("\tdescription\t\tA description of the appointment");
        System.out.println("\tbeginTime\t\tWhen the appt begins (24-hour time)");
        System.out.println("\tendTime\t\t\tWhen the appt ends (24-hour time)");
        System.out.println("options are (options may appear in any order):");
        System.out.println("\t-print\t\t\tPrints a description of the new appointment");
        System.out.println("\t-README\t\t\tPrints a README for this project and exits");
        System.out.println("Begin time and end time should be in the format: mm/dd/yyyy hh:mm");
        System.exit(0);
    }
}