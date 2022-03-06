package com.mycompany.schedules;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author chalu
 */
public class Class implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String year;
    private final String name;
    private ArrayList<SubjectGroup> subjectGroups = new ArrayList<>();
    private transient ArrayList<ArrayList<ScheduleHour>> schedule = new ArrayList<>();

    public Class(String year, String name) {
        this.year = year;
        this.name = name;
    }

    public String getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public ArrayList<SubjectGroup> getSubjectGroups() {
        return subjectGroups;
    }

    public ArrayList<ArrayList<ScheduleHour>> getSchedule() {
        return schedule;
    }

    public void setSubjectGroups(ArrayList<SubjectGroup> subjectGroups) {
        this.subjectGroups = subjectGroups;
    }

    public void setSchedule(ArrayList<ArrayList<ScheduleHour>> schedule) {
        this.schedule = schedule;
    }

    @Override
    public String toString() {
        return year + "." + name;
    }
}
