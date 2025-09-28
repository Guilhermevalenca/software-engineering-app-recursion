package app;

import java.util.ArrayList;

public class Gradle {
    public static ArrayList<String> subjects = new ArrayList<>();
    public String name;
    public Double value;

    public Gradle(String name, double value) {
        if (Gradle.subjects.size() > 0) {
            if (!Gradle.subjects.contains(name)) {
                Gradle.subjects.add(name);
            }
        } else {
            Gradle.subjects.add(name);
        }
        this.value = value;
        this.name = name;
    }

    @Override
    public String toString() {
        return "Nome: " + name + ", Nota: " + value;
    }
}
