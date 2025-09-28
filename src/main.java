import java.util.ArrayList;

import app.*;
// import services.GenerateRandomClassroom;

void main() {
    IO.println("Hello, World!");

    // GenerateRandomClassroom generateRandomClassroom = GenerateRandomClassroom.getInstance();
    Classroom classroom = new Classroom("Sala 1");
    
    Student claudiane = new Student("Claudiane");
    Student guilherme = new Student("Guilherme");
    Student gabriel = new Student("Gabriel");
    Student joao = new Student("João");

    classroom.students.add(claudiane);
    classroom.students.add(guilherme);
    classroom.students.add(gabriel);
    classroom.students.add(joao);

    claudiane.gradles.add(new Gradle("Matemática", 10.0));
    claudiane.gradles.add(new Gradle("Português", 9.0));
    claudiane.gradles.add(new Gradle("Geografia", 8.0));

    guilherme.gradles.add(new Gradle("Matemática", 1.0));
    guilherme.gradles.add(new Gradle("Português", 6.0));
    guilherme.gradles.add(new Gradle("Geografia", 7.0));

    gabriel.gradles.add(new Gradle("Matemática", 5.0));
    gabriel.gradles.add(new Gradle("Português", 8.0));
    gabriel.gradles.add(new Gradle("Geografia", 3.0));

    joao.gradles.add(new Gradle("Matemática", 7.0));
    joao.gradles.add(new Gradle("Português", 7.0));
    joao.gradles.add(new Gradle("Geografia", 7.0));

    ArrayList<Student> rankStudents = classroom.getRank();

    int count = 1;
    IO.println("rank dos estudantes:");
    for(Student student : rankStudents) {
        IO.println(count++ + " - " + student);
    }
}