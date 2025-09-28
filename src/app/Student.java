package app;

import java.util.ArrayList;

public class Student {
    String name;

    ArrayList<Gradle> gradles = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return name + " " + gradles;
    }
}
