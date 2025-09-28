package app;

public class Gradle {
    String name;
    Float value;

    public Gradle(String name, Float value) {
        this.name = name;
        this.value = value;
    }

    @Override public String toString() {
        return "Nome: " + name + ", Nota: " + value;
    }
}
