package com.mycompany.schedules;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author chalu
 */
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String number;
    private final String name;
    private ArrayList<Subject> prohibitedSubjects = new ArrayList<>();
    private transient ArrayList<ArrayList<ScheduleHour>> schedule = new ArrayList<>();

    public Classroom(String number, String name) {
        this.number = number;
        this.name = name;
        resetSchedule();
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Subject> getProhibitedSubjects() {
        return prohibitedSubjects;
    }

    public ArrayList<ArrayList<ScheduleHour>> getSchedule() {
        return schedule;
    }

    public void setProhibitedSubjects(ArrayList<Subject> prohibitedSubjects) {
        this.prohibitedSubjects = prohibitedSubjects;
    }

    public void setSchedule(ArrayList<ArrayList<ScheduleHour>> schedule) {
        this.schedule = schedule;
    }

    public void resetSchedule() {
        schedule = new ArrayList<>();
        for (int y = 0; y < 5; y++) {
            schedule.add(y, new ArrayList<>());
            for (int x = 0; x < 8; x++) {
                schedule.get(y).add(x, null);
            }
        }
    }

    @Override
    public String toString() {
        return number + " - " + name;
    }
}
