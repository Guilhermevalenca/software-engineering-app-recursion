package app;

import java.util.ArrayList;

public class Classroom {
    public String name;
    public ArrayList<Student> students = new ArrayList<>();

    public Classroom(String name) {
        this.name = name;
    }

    @Override public String toString() {
        return "Sala: " + name + ", Alunos: " + students;
    }

    public ArrayList<Student> getRank() {
        ArrayList<Student> rank = new ArrayList<>(students);
        
        return rankAverageStudents(rank);
    }

    private ArrayList<Student> rankAverageStudents(ArrayList<Student> remaining) {
        if (remaining.isEmpty()) {
            return remaining;
        }

        Student topStudent = remaining.get(0);
        for (Student student : remaining) {
            if (student.average() > topStudent.average()) {
                topStudent = student;
            }
        }

        remaining.remove(topStudent);

        ArrayList<Student> ranked = rankAverageStudents(remaining);

        ranked.add(0, topStudent);

        return ranked;
    }
}
