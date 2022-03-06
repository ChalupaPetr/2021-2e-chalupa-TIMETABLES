package com.mycompany.timetables;

import java.util.HashMap;

/**
 * @author chalu
 */
public class TimetableHour {

    private final Class participantClass;
    private final HashMap<Subject, Classroom> classrooms;
    private final SubjectGroup subjects;

    public TimetableHour(Class participantClass, HashMap<Subject, Classroom> classrooms, SubjectGroup subjects) {
        this.participantClass = participantClass;
        this.classrooms = classrooms;
        this.subjects = subjects;
    }

    public Class getParticipantClass() {
        return participantClass;
    }

    public HashMap<Subject, Classroom> getClassrooms() {
        return classrooms;
    }

    public String getClassroomsString() {
        return classrooms.toString().replace("{", "").replace("}", "");
    }

    public SubjectGroup getSubjects() {
        return subjects;
    }

}
