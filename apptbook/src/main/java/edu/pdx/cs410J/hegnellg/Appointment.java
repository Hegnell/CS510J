package edu.pdx.cs410J.hegnellg;

import edu.pdx.cs410J.AbstractAppointment;

public class Appointment extends AbstractAppointment {

    public String beginTimeString;
    public String endTimeString;
    public String description;

    public Appointment(String beginTimeString, String endTimeString, String description) {
        this.beginTimeString = beginTimeString;
        this.endTimeString = endTimeString;
        this.description = description;
    }

    @Override
    public String getBeginTimeString() { return beginTimeString; }

    public void setBeginTimeString(String beginTimeString) { this.beginTimeString = beginTimeString; }

    @Override
    public String getEndTimeString() {
    return endTimeString;
  }

    public void setEndTimeString(String endTimeString) { this.endTimeString = endTimeString; }

    @Override
    public String getDescription() {
    return description;
  }

    public void setDescription(String description) { this.description = description; }
}
