// Gustaf Hegnell
// Project 4
// CS 510J
// 7/27/2016

package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.web.HttpRequestHelper;

import java.io.IOException;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.util.*;

/**
 * The main class for project 4 that parses the command line and performs the expected behavior with the appointment.
 * If a hostname and port are provided, it will connect to the server and add the appointment to the appropriate
 * appointment book on the server, or optionally search for particular appointments between two given dates. If no hostname
 * and port are provided, it will simply add the appointment to a local appointment book, and then optionally print out
 * a description of the provided appointment.
 *
 * @author Gustaf Hegnell
 */
public class Project4 {

    public static void main(String... args) {
        String hostName = null;
        String portString = null;
        boolean searchOption = false;
        boolean printOption = false;
        boolean doneWithOptions = false;
        boolean hostOption = false;
        boolean portOption = false;
        int port = 0;
        ArrayList<String> appointmentArgs = new ArrayList<>();

        if (args.length == 0) {
            error("Missing command line arguments");
        }

        // Program should not do anything if -README is specified, so it needs to be included here first.
        for (String arg : args) {
            if (arg.equals("-README")) {
                exitWithReadme();
            }
        }

        // Go through args and determine options and args provided.
        for (int i = 0; i < args.length; i++) {
            String arg = args[i];

            // Check for empty quotes basically.
            if (arg.length() < 1) {
                error("\"Looks like you provided an empty argument, did you forget to include a description?");
            }

            if (arg.charAt(0) == '-') {
                if (!doneWithOptions) {
                    if (arg.equals("-print")) {
                        printOption = true;
                    } else if (arg.equals("-search")) {
                        searchOption = true;
                    } else if (arg.equals("-host")) {
                        hostOption = true;
                        hostName = args[++i];
                        // Check for situation where the provided option but no hostname.
                        if (hostName.startsWith("-")) {
                            error("Looks like you forgot to provide a hostname.");
                        }
                    } else if (arg.equals("-port")) {
                        portOption = true;
                        portString = args[++i];
                        if (portString.startsWith("-")) {
                            error("Looks like you forgot to provide a port");
                        }
                    } else if (arg.startsWith("-")) {
                        error("Unknown option " + arg + " provided.");
                    } else {
                        doneWithOptions = true;
                        appointmentArgs.add(arg);
                    }
                } else {
                    error("Looks like your options and args are out of order.");
                }
            } else {
                appointmentArgs.add(arg);
            }
        }

        // If either host or port option is turned on, both need to be turned on.
        if ((hostOption && !portOption) || (!hostOption && portOption)) {
            error("If using the web server option, both hostname and port must be specified.");
        }

        if (printOption && searchOption) {
            error("Print and search options can not be used together.");
        }

        // There has to be exactly 8 args provided to add an appointment, or 7 if search is enabled.
        if (searchOption && (appointmentArgs.size() != 7)) {
            error("Wrong number of arguments provided.");
        } else if (!searchOption && appointmentArgs.size() != 8) {
            System.out.println(appointmentArgs);
            error("Wrong number of arguments provided.");
        }

        String owner;
        String description = null;
        String beginDateString;
        String beginTimeString;
        String beginTimeAMPM;
        String endDateString;
        String endTimeString;
        String endTimeAMPM;

        // Different number of params for search mode and normal mode.
        if (searchOption) {
            owner = appointmentArgs.get(0);
            beginDateString = appointmentArgs.get(1);
            beginTimeString = appointmentArgs.get(2);
            beginTimeAMPM = appointmentArgs.get(3);
            endDateString = appointmentArgs.get(4);
            endTimeString = appointmentArgs.get(5);
            endTimeAMPM = appointmentArgs.get(6);
        } else {
            // Separate out the provided args into what they represent.
            owner = appointmentArgs.get(0);
            description = appointmentArgs.get(1);
            beginDateString = appointmentArgs.get(2);
            beginTimeString = appointmentArgs.get(3);
            beginTimeAMPM = appointmentArgs.get(4);
            endDateString = appointmentArgs.get(5);
            endTimeString = appointmentArgs.get(6);
            endTimeAMPM = appointmentArgs.get(7);
        }

        // Perform error checking on the dates and times provided.
        if (!validTime(beginTimeString + " " + beginTimeAMPM)) {
            error("The begin time was incorrectly formatted. Valid format is 12 hour times (1:19 pm) and only real times are accepted.");
        } else if (!validTime(endTimeString + " " + endTimeAMPM)) {
            error("The end time was incorrectly formatted. Valid format is 12 hour times (1:19 pm) and only real times are accepted.");
        } else if (!validDate(beginDateString)) {
            error("The begin date was incorrectly formatted. Valid dates are m/d/yyyy or mm/dd/yyyy");
        } else if (!validDate(endDateString)) {
            error("The end date was incorrectly formatted. Valid dates are m/d/yyyy or mm/dd/yyyy");
        }

        String begin = beginDateString + " " + beginTimeString + " " + beginTimeAMPM;
        String end = endDateString + " " + endTimeString + " " + endTimeAMPM;
        AppointmentBook appointmentBook = new AppointmentBook(owner);

        // Print the appointment.
        if (printOption) {
            Appointment appointment = new Appointment(begin, end, description);
            appointmentBook.addAppointment(appointment);
            System.out.println("\nDescription of newly created appointment:\n");
            System.out.println(appointment);
        }

        // If no host and port were specified, then we are done here.
        if (!hostOption && !portOption) {
            System.exit(0);
        }

        // We are using a server so try and parse the port provided.
        try {
            port = Integer.parseInt(portString);
        } catch (NumberFormatException e) {
            error("The port needs to be an integer.");
        }

        AppointmentBookRestClient client = new AppointmentBookRestClient(hostName,port);
        HttpRequestHelper.Response response;

        // Search for given appointments.
        if (searchOption) {
            try {
                response = client.searchForAppointments(owner, begin, end);
                checkResponseCode(HttpURLConnection.HTTP_OK, response);
                System.out.println(response.getContent());
            } catch (IOException e) {
                error("Error while contacting server: " + e.getMessage());
            }

        } else {
            try {
                response = client.addAppointment(owner, begin, end, description);
                checkResponseCode(HttpURLConnection.HTTP_OK, response);
                System.out.println(response.getContent());
            } catch (IOException e) {
                error("Error while contacting server: " + e.getMessage());
            }
        }

        System.exit(0);
    }

    /**
     *  This function takes a time and checks whether it is valid or not.
     *
     * @param time A string representation of the time in 12 hour format.
     * @return A boolean, true if the time is a valid time in the correct format, false otherwise.
     */
    private static boolean validTime(String time) {
        return time.matches("(1[0-2]|[0-9]):[0-5]\\d\\s(am|pm|AM|PM)");
    }

    /**
     *  This function takes a date and checks whether it is valid or not.
     *
     * @param date A string representation of a date in mm/dd/yyyy or m/d/yyyy format.
     * @return A boolean, true if the time is a valid date in the correct format, false otherwise.
     */
    private static boolean validDate(String date) {
        // Wasn't sure how specific this had to be, but covers most.
        return date.matches("(0?[1-9]|1[012])/(0?[1-9]|1\\d|2\\d|3[01])/\\d\\d\\d\\d");
    }

    /**
     * Makes sure that the give response has the expected HTTP status code
     * @param code The expected status code
     * @param response The response from the server
     */
    private static void checkResponseCode( int code, HttpRequestHelper.Response response )
    {
        if (response.getCode() != code) {
            error(String.format("Expected HTTP code %d, got code %d.\n\n%s", code,
                                response.getCode(), response.getContent()));
        }
    }

    /**
     *  This function takes a string describing an error, prints it to the user, and exits with an error.
     *
     * @param message A message describing the error.
     */
    private static void error( String message ) {
        PrintStream err = System.err;
        err.println("** " + message);

        System.exit(1);
    }


    /**
     *  This function prints out a decription of the program and exits afterwards.
     */
    private static void exitWithReadme() {
        System.out.println("\n\nGustaf Hegnell - Project 4\n");
        System.out.println("This is a small program which allows the creation of an appointment from");
        System.out.println("command line arguments, and adds that appointment to an appointment book.");
        System.out.println("Optionally, the program can connect to a server and add the appointment to an");
        System.out.println("appointment book stored on that server. Additionally, the search option can be specified");
        System.out.println("which allows the user to search for all appointments on the server from a given");
        System.out.println("owner's appointment book that exist between certain times.");
        System.out.println("usage: [options] <args>");
        System.out.println("args are (in this order):");
        System.out.println("\towner\t\t\tThe person whose owns the appt book");
        System.out.println("\tdescription\t\tA description of the appointment");
        System.out.println("\tbeginTime\t\tWhen the appt begins (12-hour time)");
        System.out.println("\tendTime\t\t\tWhen the appt ends (12-hour time)");
        System.out.println("options are (options may appear in any order):");
        System.out.println("\t-print\t\t\tPrints a description of the new appointment");
        System.out.println("\t-host hostname\tThe name of the host.");
        System.out.println("\t-port portNumber\tThe port number.");
        System.out.println("\t-search\t\t\tSearch for appointments between given times.");
        System.out.println("\t-README\t\t\tPrints a README for this project and exits");
        System.out.println("Begin time and end time should be in the format: mm/dd/yyyy 12 hour time (06/06/2016 1:15 pm)");
        System.exit(0);
    }
}