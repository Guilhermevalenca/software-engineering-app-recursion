package app;

import java.util.ArrayList;

public class Classroom {
    String name;
    ArrayList<Student> students = new ArrayList<>();

    public Classroom(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "Sala: " + name + ", Alunos: " + students;
    }
}
