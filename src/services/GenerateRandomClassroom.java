package services;

import interfaces.IService;
import app.*;
import java.util.ArrayList;


public class GenerateRandomClassroom implements IService<ArrayList<Classroom>> {
    private static GenerateRandomClassroom instance = null;

    public static GenerateRandomClassroom getInstance() {
        if (instance == null) {
            instance = new GenerateRandomClassroom();
        }
        return instance;
    }

    private GenerateRandomClassroom() {}

   public ArrayList<Classroom> execute() {
        ArrayList<Classroom> classrooms = new ArrayList<>();
        return classrooms;
    }
}
