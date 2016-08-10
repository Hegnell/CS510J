// Gustaf Hegnell
// Project 5
// CS 510J
// 8/10/2016

package edu.pdx.cs410J.hegnellg.client;

import com.google.common.annotations.VisibleForTesting;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextArea;


import java.util.ArrayList;

/**
 * A basic GWT class that handles client requests and server responses regarding appointment books.
 *
 * @author Gustaf Hegnell
 */
public class AppointmentBookGwt implements EntryPoint {
    // All of the UI components are initialized here.
    private final Alerter alerter;
    private final TextBox ownerText = new TextBox();
    private final TextBox descriptionText = new TextBox();
    private final TextBox startTimeText = new TextBox();
    private final TextBox startDateText = new TextBox();
    private final TextBox endTimeText = new TextBox();
    private final TextBox endDateText = new TextBox();
    private final Button addAppointmentButton = new Button();
    private final Button searchAppointmentsButton = new Button();
    private final Button prettyPrinterButton = new Button();
    private final Button readmeButton = new Button();
    private final Button clearButton = new Button();
    private final TextArea textArea = new TextArea();
    private static PingServiceAsync async;

    /**
     * Default constructor which initializes an alerter.
     */
    public AppointmentBookGwt() {
        this(new Alerter() {
            @Override
            public void alert(String message) {
            Window.alert(message);
          }
        });
      }

    /**
     * Secondary constructor that takes an alerter as a parameter.
     *
     * @param alerter The alerter to be used.
     */
    @VisibleForTesting
    AppointmentBookGwt(Alerter alerter) {
        this.alerter = alerter;
        addWidgets();
    }

    /**
     * Function to set up all of the widgets of the UI.
     */
    private void addWidgets() {
        setupTextBox(ownerText, "Owner");
        setupTextBox(descriptionText, "Description");
        setupTextBox(startTimeText, "Start Time (hh:mm am/pm)");
        setupTextBox(endTimeText, "End Time (hh:mm am/pm)");
        setupTextBox(startDateText, "Start Date (mm/dd/yyyy)");
        setupTextBox(endDateText, "End Date (mm/dd/yyyy)");
        addAppointmentButton(addAppointmentButton, "Add appointment");
        searchAppointmentsButton(searchAppointmentsButton, "Search appointments");
        prettyPrinterButton(prettyPrinterButton, "Print appointment book");
        clearButton(clearButton, "Clear");
        readmeButton(readmeButton, "Help");
    }

    /**
     * Function specifies what is to be done when the module loads.
     */
    @Override
    public void onModuleLoad() {
        async = GWT.create(PingService.class);
        RootPanel rootPanel = RootPanel.get();
        VerticalPanel left = new VerticalPanel();
        VerticalPanel right = new VerticalPanel();
        rootPanel.add(setupLeftPanel(left));
        rootPanel.add(setupRightPanel(right));
    }

    /**
     * Helper function to set an alert in the browser.
     */
    @VisibleForTesting
    interface Alerter {
        void alert(String message);
    }

    /**
     * This function sets up a text box to have a basic layout.
     * @param textBox The textbox to set up.
     * @param description The placeholder name to put in the box.
     */
    public void setupTextBox(final TextBox textBox, String description) {
        textBox.getElement().setAttribute("placeholder", description);
        textBox.getElement().setAttribute("class", "form-control");
        textBox.setAlignment(ValueBoxBase.TextAlignment.CENTER);
        textBox.setWidth("200px");

        textBox.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                textBox.getElement().getStyle().setBackgroundColor("white");
            }
        });
    }

    /**
     * This function takes a button as a parameter and sets it up to be a button that adds an appointment
     * by sending it to the server.
     *
     * @param button The button to initialize.
     * @param name The name to display on the button.
     */
    private void addAppointmentButton(final Button button, String name) {
        button.setText(name);
        button.getElement().getStyle().setBackgroundColor("lightblue");
        button.getElement().setAttribute("class", "btn btn-warning");

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                String owner = ownerText.getText();
                String description = descriptionText.getText();
                String startTime = startTimeText.getText();
                String startDate = startDateText.getText();
                String endTime = endTimeText.getText();
                String endDate = endDateText.getText();

                // Set fields to red if they are missing.
                boolean valid = true;
                if (owner == null || owner.equals("")) {
                    ownerText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (description == null || description.equals("")) {
                    descriptionText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (startTime == null || startTime.equals("")) {
                    startTimeText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (startDate == null || startDate.equals("")) {
                    startDateText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (endTime == null || endTime.equals("")) {
                    endTimeText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (endDate == null || endDate.equals("")) {
                    endDateText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }

                if (!valid) {
                    alerter.alert("Missing required fields.");
                    return;
                }

                final String begin = startDate + " " + startTime;
                final String end = endDate + " " + endTime;
                // Perform error checking on the dates and times provided.
                if (!validateInputs(startTime, endTime, startDate, endDate)) {
                    return;
                }

                Appointment appointment = new Appointment(begin, end, description);
                async.ping(owner, appointment, new AsyncCallback<Appointment>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        alerter.alert("There was an issue retrieving the appointment book.");
                    }

                    @Override
                    public void onSuccess(Appointment appointment) {
                        alerter.alert("Successfully created appointment!\n" + appointment.toString());
                    }
                });
            }
        });
    }

    /**
     * This function takes a button as a parameter and sets it up to be a button that searched for appointments
     * on the server.
     *
     * @param button The button to initialize.
     * @param name The name to display on the button.
     */
    private void searchAppointmentsButton(final Button button, String name) {
        button.setText(name);
        button.getElement().getStyle().setBackgroundColor("lightblue");
        button.getElement().setAttribute("class", "btn btn-warning");

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                final String owner = ownerText.getText();
                String startTime = startTimeText.getText();
                String startDate = startDateText.getText();
                String endTime = endTimeText.getText();
                String endDate = endDateText.getText();

                // Make fields red if missing.
                boolean valid = true;
                if (owner == null || owner.equals("")) {
                    ownerText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (startTime == null || startTime.equals("")) {
                    startTimeText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (startDate == null || startDate.equals("")) {
                    startDateText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (endTime == null || endTime.equals("")) {
                    endTimeText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }
                if (endDate == null || endDate.equals("")) {
                    endDateText.getElement().getStyle().setBackgroundColor("orangered");
                    valid = false;
                }

                if (!valid) {
                    alerter.alert("Missing required fields.");
                    return;
                }

                final String begin = startDate + " " + startTime;
                final String end = endDate + " " + endTime;

                // Perform error checking on the dates and times provided.
                if (!validateInputs(startTime, endTime, startDate, endDate)) {
                    return;
                }

                async.search(owner, new AsyncCallback<AppointmentBook>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        alerter.alert(throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(AppointmentBook appointments) {
                        if (appointments == null) {
                            alerter.alert("No appointment book found for that owner.");
                        } else {
                            ArrayList<Appointment> search = appointments.searchAppointments(begin, end);
                            AppointmentBook temp = new AppointmentBook(owner);
                            for (Appointment appt : search) {
                                temp.addAppointment(appt);
                            }
                            PrettyPrinter prettyPrinter = new PrettyPrinter();
                            textArea.setText(prettyPrinter.dump(temp));
                        }
                    }
                });
            }
        });
    }

    /**
     * This function takes a button as a parameter and sets it up to be a button that prints an appointment book
     * to the text area.
     *
     * @param button The button to initialize.
     * @param name The name to display on the button.
     */
    private void prettyPrinterButton(final Button button, String name) {
        button.setText(name);
        button.getElement().getStyle().setBackgroundColor("lightblue");
        button.getElement().setAttribute("class", "btn btn-warning");

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                final String owner = ownerText.getText();
                if (owner == null || owner.equals("")) {
                    alerter.alert("An owner needs to be specified.");
                    ownerText.getElement().getStyle().setBackgroundColor("orangered");
                    return;
                }
                async.search(owner, new AsyncCallback<AppointmentBook>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        alerter.alert(throwable.getMessage());
                    }

                    @Override
                    public void onSuccess(AppointmentBook appointments) {
                        if (appointments == null) {
                            alerter.alert("No appointment book found for that owner.");
                        } else {
                            PrettyPrinter prettyPrinter = new PrettyPrinter();
                            textArea.setText(prettyPrinter.dump(appointments));
                        }
                    }
                });
            }
        });
    }

    /**
     * This function takes a button as a parameter and sets it up to be a button that displays the readme when
     * clicked.
     *
     * @param button The button to initialize.
     * @param name The name to display on the button.
     */
    private void readmeButton(final Button button, String name) {
        button.setText(name);
        button.getElement().getStyle().setBackgroundColor("lightgreen");
        button.getElement().setAttribute("class", "btn btn-warning");

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                alerter.alert(readme());
            }
        });

    }

    /**
     * This function takes a button as a parameter and sets it up to be a button that clears the textarea.
     *
     * @param button The button to initialize.
     * @param name The name to display on the button.
     */
    private void clearButton(final Button button, String name) {
        button.setText(name);
        button.getElement().getStyle().setBackgroundColor("lightgreen");
        button.getElement().setAttribute("class", "btn btn-warning");

        button.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                textArea.setText("");
            }
        });

    }

    /**
     * This function does the setup work of initializing a panel to display all of the text boxes.
     *
     * @param left The panel to initialize.
     * @return The initialized panel.
     */
    private VerticalPanel setupLeftPanel(VerticalPanel left) {
        left.getElement().setAttribute("align", "left");
        left.getElement().getStyle().setMarginLeft(200, Style.Unit.PX);
        left.getElement().getStyle().setMarginTop(100, Style.Unit.PX);
        left.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        left.setSpacing(10);
        Label label = new Label("Provide information here!");
        left.add(label);
        left.add(ownerText);
        left.add(descriptionText);
        left.add(startDateText);
        left.add(endDateText);
        left.add(startTimeText);
        left.add(endTimeText);
        left.add(addAppointmentButton);
        left.add(searchAppointmentsButton);
        left.add(prettyPrinterButton);
        left.add(readmeButton);
        return left;
    }

    /**
     * This function does all the work of setting up a panel which holds a textare where appointments can be displayed.
     *
     * @param right The panel to initialize.
     * @return The initialized panel.
     */
    private VerticalPanel setupRightPanel(VerticalPanel right) {
        textArea.setCharacterWidth(92);
        textArea.setVisibleLines(50);
        Label label = new Label("APPOINTMENT BOOK");
        label.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        right.add(label);
        right.add(textArea);
        right.getElement().setAttribute("align", "center");
        right.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        right.setSpacing(10);
        right.add(clearButton);
        return right;
    }


    /**
     * This function validates the dates and times input by the user.
     *
     * @param startTime Start time of the appointment.
     * @param endTime The end time.
     * @param startDate The start date.
     * @param endDate The end date.
     * @return True if all fields are valid.
     */
    private boolean validateInputs(String startTime, String endTime, String startDate, String endDate) {
        // Perform error checking on the dates and times provided.
        if (!validTime(startTime)) {
            alerter.alert("The begin time was incorrectly formatted. Valid format is 12 hour times (1:19 pm) and only real times are accepted.");
            return false;
        } else if (!validTime(endTime)) {
            alerter.alert("The end time was incorrectly formatted. Valid format is 12 hour times (1:19 pm) and only real times are accepted.");
            return false;
        } else if (!validDate(startDate)) {
            alerter.alert("The begin date was incorrectly formatted. Valid dates are m/d/yyyy or mm/dd/yyyy");
            return false;
        } else if (!validDate(endDate)) {
            alerter.alert("The end date was incorrectly formatted. Valid dates are m/d/yyyy or mm/dd/yyyy");
            return false;
        }
        return true;
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
     *  This is a function that simply builds the readme/help info.
     *
     * @return A string representing the readme.
     */
    private static String readme() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n\nGustaf Hegnell - Project 5\n\n");
        sb.append("This is a small program which allows the creation of an appointment");
        sb.append("and the addition of that appointment into an appointment book.\n\n");
        sb.append("An appointment consists of a description, a start time, and an end time.\n\n");

        sb.append("An appointment can be added to an appointment book by providing all relevant");
        sb.append("information and clicking the \"Add appointment\" button.\n\n");
        sb.append("To search for specific appointments, provide an owner, start time, and end time.\n\n");
        sb.append("Clicking on \"Search appointments\" will search that owner's appointment book and");
        sb.append("return all appointments that occur between those times.\n\n");
        sb.append("To display all appointments in an appointment book, simply provide an owner and click the");
        sb.append("\"Print appointment book\" button. All appointments will be displayed.");
        sb.append("A help menu can be displayed by clicking the \"help\" button, but you are already looking at it...");
        return sb.toString();
    }
}
