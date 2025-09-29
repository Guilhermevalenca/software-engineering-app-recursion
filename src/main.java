import app.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

void main() {
    Classroom classroom = new Classroom("Sala 1");

    JFrame frame = new JFrame("Classroom Manager");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(1000, 500);
    frame.setLayout(new BorderLayout());

    DefaultTableModel tableModel = new DefaultTableModel();
    tableModel.addColumn("Aluno");
    tableModel.addColumn("Média");
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);

    JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

    JTextField studentNameField = new JTextField();
    JComboBox<String> studentSelector = new JComboBox<>();

    JComboBox<String> subjectSelector = new JComboBox<>(Gradle.subjects.toArray(new String[0]));
    subjectSelector.setEditable(true);

    JTextField gradeField = new JTextField();

    JButton addStudentButton = new JButton("Adicionar Aluno");
    JButton addGradeButton = new JButton("Adicionar Nota");
    JButton showRankButton = new JButton("Exibir Rank");
    JButton manageSubjectsButton = new JButton("Gerenciar Disciplinas");

    formPanel.add(new JLabel("Novo Aluno:"));
    formPanel.add(studentNameField);
    formPanel.add(new JLabel("Selecionar Aluno:"));
    formPanel.add(studentSelector);
    formPanel.add(new JLabel("Disciplina:"));
    formPanel.add(subjectSelector);
    formPanel.add(new JLabel("Nota:"));
    formPanel.add(gradeField);
    formPanel.add(addStudentButton);
    formPanel.add(addGradeButton);
    formPanel.add(manageSubjectsButton);

    JTextArea outputArea = new JTextArea();
    outputArea.setEditable(false);
    JScrollPane outputScroll = new JScrollPane(outputArea);

    JPanel bottomPanel = new JPanel();
    bottomPanel.add(showRankButton);

    frame.add(formPanel, BorderLayout.NORTH);
    frame.add(scrollPane, BorderLayout.CENTER);
    frame.add(outputScroll, BorderLayout.EAST);
    frame.add(bottomPanel, BorderLayout.SOUTH);

    addStudentButton.addActionListener(e -> {
        String name = studentNameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Digite o nome do aluno.");
            return;
        }
        Student student = new Student(name);
        classroom.students.add(student);

        tableModel.addRow(new Object[] { name, 0.0 });
        studentSelector.addItem(name);

        studentNameField.setText("");
    });

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

            if (!Gradle.subjects.contains(subject)) {
                Gradle.subjects.add(subject);
                subjectSelector.addItem(subject);
                tableModel.addColumn(subject);
            }

            Student student = classroom.students.get(selectedIndex);
            student.addGradle(subject, gradeValue);

            int colIndex = -1;
            for (int i = 2; i < tableModel.getColumnCount(); i++) {
                if (tableModel.getColumnName(i).equals(subject)) {
                    colIndex = i;
                    break;
                }
            }
            if (colIndex == -1) {
                tableModel.addColumn(subject);
                colIndex = tableModel.getColumnCount() - 1;
            }

            int row = selectedIndex;
            tableModel.setValueAt(student.name, row, 0);
            tableModel.setValueAt(gradeValue, row, colIndex);

            tableModel.setValueAt(student.average(), row, 1);

            gradeField.setText("");
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Nota inválida.");
        }
    });

    showRankButton.addActionListener(e -> {
        ArrayList<Student> rankStudents = classroom.getRank();
        outputArea.setText("--- Ranking ---\n");
        int count = 1;
        for (Student student : rankStudents) {
            outputArea.append(count++ + " - " + student.name +
                    " (Média: " + String.format("%.2f", student.average()) + ")\n");
        }
    });

    manageSubjectsButton.addActionListener(e -> {
        JDialog dialog = new JDialog(frame, "Disciplinas Cadastradas", true);
        dialog.setSize(400, 300);
        dialog.setLayout(new BorderLayout());

        DefaultListModel<String> subjectListModel = new DefaultListModel<>();
        for (String subj : Gradle.subjects) {
            subjectListModel.addElement(subj);
        }
        JList<String> subjectList = new JList<>(subjectListModel);
        JScrollPane scrollPaneSubjects = new JScrollPane(subjectList);

        dialog.add(scrollPaneSubjects, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        JButton removeButton = new JButton("Remover");
        buttonPanel.add(removeButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        removeButton.addActionListener(ev -> {
            int selectedIndex = subjectList.getSelectedIndex();
            if (selectedIndex != -1) {
                String removedSubject = subjectListModel.getElementAt(selectedIndex);
                int confirm = JOptionPane.showConfirmDialog(dialog,
                        "Deseja realmente remover a disciplina \"" + removedSubject + "\"?",
                        "Confirmar remoção",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    subjectListModel.remove(selectedIndex);
                    Gradle.subjects.remove(removedSubject);
                    subjectSelector.removeItem(removedSubject);

                    int colToRemove = -1;
                    for (int i = 2; i < tableModel.getColumnCount(); i++) {
                        if (tableModel.getColumnName(i).equals(removedSubject)) {
                            colToRemove = i;
                            break;
                        }
                    }
                    if (colToRemove != -1) {
                        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(colToRemove));
                    }

                    for (int row = 0; row < classroom.students.size(); row++) {
                        Student student = classroom.students.get(row);
                        student.removeGradle(removedSubject);
                        tableModel.setValueAt(student.average(), row, 1);
                    }
                }
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    });

    frame.setVisible(true);
}

private static void removeColumn(JTable table, int colIndex) {
    table.getColumnModel().removeColumn(table.getColumnModel().getColumn(colIndex));
}
