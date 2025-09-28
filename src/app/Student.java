package app;

import java.util.ArrayList;

public class Student {
    String name;

    public ArrayList<Gradle> gradles = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "Aluno: " + name + ", Media: " + average();
    }

    public double average() {
        double sum = 0;
        for (Gradle gradle : gradles) {
            sum += gradle.value;
        }
        return sum / gradles.size();
    }
}
