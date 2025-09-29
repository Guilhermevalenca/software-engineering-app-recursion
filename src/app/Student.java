package app;

import java.util.ArrayList;

public class Student {
    public String name;

    private ArrayList<Gradle> gradles = new ArrayList<>();

    public Student(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Aluno: " + name + ", Media: " + average();
    }

    public void showGrades() {
        for (Gradle gradle : gradles) {
            IO.println(gradle);
        }
    }

    public double average() {
        double sum = 0;
        for (Gradle gradle : gradles) {
            sum += gradle.value;
        }
        return sum / gradles.size();
    }

    public void addGradle(String subject, double value) {
        for (Gradle gradle : gradles) {
            if (gradle.name.equals(subject)) {
                gradle.value = value;
                return;
            }
        }
        gradles.add(new Gradle(subject, value));
    }

    public void removeGradle(String subject) {
        for (Gradle gradle : gradles) {
            if (gradle.name.equals(subject)) {
                gradles.remove(gradle);
                return;
            }
        }
    }
}
