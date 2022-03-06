package com.mycompany.timetables;

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
    private transient ArrayList<ArrayList<TimetableHour>> timetable = new ArrayList<>();

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

    public ArrayList<ArrayList<TimetableHour>> getTimetable() {
        return timetable;
    }

    public void setSubjectGroups(ArrayList<SubjectGroup> subjectGroups) {
        this.subjectGroups = subjectGroups;
    }

    public void setTimetable(ArrayList<ArrayList<TimetableHour>> timetable) {
        this.timetable = timetable;
    }

    @Override
    public String toString() {
        return year + "." + name;
    }
}
