package com.example.hospitalmanagementsystemfrontendfinal.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class Appointment {
    private int apptId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String dayName;
    private String time;
    private String name;
    private String ageOrDepartment;
    private String dateString;

    public Appointment() {}

    public Appointment(int apptId, LocalDate date, String time, String name) {
        this.apptId = apptId;
        this.date = date;
        this.dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        this.time = time;
        this.name = name;
    }

    public Appointment(int apptId, LocalDate date, String time, String name, String ageOrDepartment) {
        this(apptId, date, time, name);
        this.ageOrDepartment = ageOrDepartment;
    }


    public int getApptId() { return apptId; }
    public LocalDate getDate() { return date; }
    public String getDayName() { return dayName; }
    public String getTime() { return time; }
    public String getName() { return name; }
    public String getAgeOrDepartment() { return ageOrDepartment; }
    public String getDateString() {return dateString;}

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
    public void setDate(LocalDate date) {
        this.date = date;
        this.dayName = date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }
    public void setTime(String time) { this.time = time; }
    public void setName(String name) { this.name = name; }
    public void setAgeOrDepartment(String ageOrDepartment) { this.ageOrDepartment = ageOrDepartment; }

    public void setApptId(int apptId) {
        this.apptId = apptId;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "apptId=" + apptId +
                ", date=" + date +
                ", dayName='" + dayName + '\'' +
                ", time='" + time + '\'' +
                ", name='" + name + '\'' +
                ", ageOrDepartment='" + ageOrDepartment + '\'' +
                '}';
    }
}

