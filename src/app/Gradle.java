package app;

public class Gradle {
    String name;
    Double value;

    public Gradle(String name, double  value) {
        this.name = name;
        this.value = value;
    }

    @Override public String toString() {
        return "Nome: " + name + ", Nota: " + value;
    }
}
