package com.mycompany.timetables;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import javafx.scene.paint.Color;

/**
 * @author chalu
 */
public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private Color color;

    public Subject(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException {
        name = aInputStream.readUTF();
        double[] colorRGB = (double[]) aInputStream.readObject();
        color = Color.color(colorRGB[0], colorRGB[1], colorRGB[2]);
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws ClassNotFoundException, IOException {
        aOutputStream.writeUTF(name);
        double[] colorRGB = {color.getRed(), color.getGreen(), color.getBlue()};
        aOutputStream.writeObject(colorRGB);
    }

    @Override
    public String toString() {
        return name;
    }
}
