// import java.util.ArrayList;

// import app.*;
// import javax.swing.*;
// // import services.GenerateRandomClassroom;

// void main() {
//     IO.println("Hello, World!");

//     // GenerateRandomClassroom generateRandomClassroom = GenerateRandomClassroom.getInstance();
//     Classroom classroom = new Classroom("Sala 1");

//     Student claudiane = new Student("Claudiane");
//     Student guilherme = new Student("Guilherme");
//     Student gabriel = new Student("Gabriel");
//     Student joao = new Student("João");

//     classroom.students.add(claudiane);
//     classroom.students.add(guilherme);
//     classroom.students.add(gabriel);
//     classroom.students.add(joao);

//     claudiane.gradles.add(new Gradle("Matemática", 10.0));
//     claudiane.gradles.add(new Gradle("Português", 9.0));
//     claudiane.gradles.add(new Gradle("Geografia", 8.0));

//     guilherme.gradles.add(new Gradle("Matemática", 1.0));
//     guilherme.gradles.add(new Gradle("Português", 6.0));
//     guilherme.gradles.add(new Gradle("Geografia", 7.0));

//     gabriel.gradles.add(new Gradle("Matemática", 5.0));
//     gabriel.gradles.add(new Gradle("Português", 8.0));
//     gabriel.gradles.add(new Gradle("Geografia", 3.0));

//     joao.gradles.add(new Gradle("Matemática", 7.0));
//     joao.gradles.add(new Gradle("Português", 7.0));
//     joao.gradles.add(new Gradle("Geografia", 7.0));

//     ArrayList<Student> rankStudents = classroom.getRank();

//     int count = 1;
//     IO.println("rank dos estudantes:");
//     for(Student student : rankStudents) {
//         IO.println(count++ + " - " + student);
//     }

//     JFrame frame = new JFrame("Classroom");
//     JButton button = new JButton("Generate Classroom");

//     frame.add(button);
//     frame.setSize(620, 420);
//     frame.setVisible(true);
// }

import app.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

void main() {
    Classroom classroom = new Classroom("Sala 1");

    JFrame frame = new JFrame("Classroom Manager");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(900, 500);
    frame.setLayout(new BorderLayout());

    // ==== MODELO DA TABELA ====
    DefaultTableModel tableModel = new DefaultTableModel();
    tableModel.addColumn("Aluno"); // primeira coluna fixa
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);

    // ==== FORMULÁRIO ====
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

    JTextField studentNameField = new JTextField();
    JComboBox<String> studentSelector = new JComboBox<>();

    // 👉 disciplinas vindas de Gradle.subjects (editável!)
    JComboBox<String> subjectSelector = new JComboBox<>(Gradle.subjects.toArray(new String[0]));
    subjectSelector.setEditable(true);

    JTextField gradeField = new JTextField();

    JButton addStudentButton = new JButton("Adicionar Aluno");
    JButton addGradeButton = new JButton("Adicionar Nota");
    JButton showRankButton = new JButton("Exibir Rank");

    formPanel.add(new JLabel("Novo Aluno:"));
    formPanel.add(studentNameField);
    formPanel.add(new JLabel("Selecionar Aluno:"));
    formPanel.add(studentSelector);
    formPanel.add(new JLabel("Disciplina:"));
    formPanel.add(subjectSelector); // JComboBox editável
    formPanel.add(new JLabel("Nota:"));
    formPanel.add(gradeField);
    formPanel.add(addStudentButton);
    formPanel.add(addGradeButton);

    // ==== AREA DE RANK ====
    JTextArea outputArea = new JTextArea();
    outputArea.setEditable(false);
    JScrollPane outputScroll = new JScrollPane(outputArea);

    // ==== BOTÕES ====
    JPanel bottomPanel = new JPanel();
    bottomPanel.add(showRankButton);

    frame.add(formPanel, BorderLayout.NORTH);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.add(outputScroll, BorderLayout.EAST);
    frame.add(bottomPanel, BorderLayout.SOUTH);

    // ==== LÓGICA ====

    // Adicionar aluno
    addStudentButton.addActionListener(e -> {
        String name = studentNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite o nome do aluno.");
            return;
        }
        Student student = new Student(name);
        classroom.students.add(student);

        // adiciona linha na tabela
        tableModel.addRow(new Object[] { name });
        studentSelector.addItem(name);

        studentNameField.setText("");
    });

    // Adicionar nota ao aluno selecionado
    addGradeButton.addActionListener(e -> {
        int selectedIndex = studentSelector.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Selecione um aluno.");
            return;
        }
        String subject = (String) subjectSelector.getSelectedItem();
        String gradeStr = gradeField.getText().trim();
        if (subject == null || gradeStr.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Escolha a disciplina e digite a nota.");
            return;
        }
        try {
            double gradeValue = Double.parseDouble(gradeStr);

            // se a disciplina digitada não existe ainda → adiciona
            if (!Gradle.subjects.contains(subject)) {
                Gradle.subjects.add(subject);
                subjectSelector.addItem(subject);
            }

            Student student = classroom.students.get(selectedIndex);
            student.addGradle(subject, gradeValue);

            // verificar se já existe coluna para essa matéria
            int colIndex = -1;
            for (int i = 1; i < tableModel.getColumnCount(); i++) {
                if (tableModel.getColumnName(i).equals(subject)) {
                    colIndex = i;
                    break;
                }
            }

            // se não existe, cria nova coluna
            if (colIndex == -1) {
                tableModel.addColumn(subject);
                colIndex = tableModel.getColumnCount() - 1;
            }

            // atualizar célula correta
            int row = selectedIndex;
            tableModel.setValueAt(student.name, row, 0);
            tableModel.setValueAt(gradeValue, row, colIndex);

            gradeField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Nota inválida.");
        }
    });

    // Mostrar ranking
    showRankButton.addActionListener(e -> {
        ArrayList<Student> rankStudents = classroom.getRank();
        outputArea.setText("--- Ranking ---\n");
        int count = 1;
        for (Student student : rankStudents) {
            outputArea.append(count++ + " - " + student.name +
                    " (Média: " + String.format("%.2f", student.average()) + ")\n");
        }
    });

    frame.setVisible(true);
}
