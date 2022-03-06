package com.mycompany.timetables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author chalu
 */
public class SubjectGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    private ArrayList<Subject> subjects = new ArrayList<>();
    private int dotation;
    private boolean twoHours;

    public SubjectGroup(ArrayList<Subject> subjects, int dotation, boolean twoHours) {
        this.subjects = subjects;
        this.dotation = dotation;
        this.twoHours = twoHours;
    }

    public ArrayList<Subject> getSubjects() {
        return subjects;
    }
    public String getSubjectsString() {
        return subjects.toString().replace("[", "").replace("]", "");
    }

    public int getDotation() {
        return dotation;
    }

    public boolean isTwoHours() {
        return twoHours;
    }

    public void setSubjects(ArrayList<Subject> subjects) {
        this.subjects = subjects;
    }

    public void setDotation(int dotation) {
        this.dotation = dotation;
    }

    public void setTwoHours(boolean twoHours) {
        this.twoHours = twoHours;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
        int subjectsSize = aInputStream.readInt();
        subjects = new ArrayList<>();
        for (int i = 0; i < subjectsSize; i++) {
            subjects.add((Subject) aInputStream.readObject());
        }
        dotation = aInputStream.readInt();
        twoHours = aInputStream.readBoolean();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws ClassNotFoundException, IOException {
        aOutputStream.writeInt(subjects.size());
        for (Subject s : subjects) {
            aOutputStream.writeObject(s);
        }
        aOutputStream.writeInt(dotation);
        aOutputStream.writeBoolean(twoHours);
    }

    @Override
    public String toString() {
        return subjects.toString().replace("[", "").replace("]", "");
    }

}
