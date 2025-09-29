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

    // ==== MODELO DA TABELA ====
    DefaultTableModel tableModel = new DefaultTableModel();
    tableModel.addColumn("Aluno"); // primeira coluna fixa
    tableModel.addColumn("Média"); // coluna de média
    JTable table = new JTable(tableModel);
    JScrollPane scrollPane = new JScrollPane(table);

    // ==== FORMULÁRIO ====
    JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));

    JTextField studentNameField = new JTextField();
    JComboBox<String> studentSelector = new JComboBox<>();

    // disciplinas vindas de Gradle.subjects (editável)
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

        tableModel.addRow(new Object[] { name, 0.0 });
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
                tableModel.addColumn(subject); // cria coluna na tabela
            }

            Student student = classroom.students.get(selectedIndex);
            student.addGradle(subject, gradeValue);

            // atualizar célula da nota
            int colIndex = -1;
            for (int i = 2; i < tableModel.getColumnCount(); i++) { // a partir da coluna 2 (coluna média é 1)
                if (tableModel.getColumnName(i).equals(subject)) {
                    colIndex = i;
                    break;
                }
            }
            if (colIndex == -1) { // se não existir, cria
                tableModel.addColumn(subject);
                colIndex = tableModel.getColumnCount() - 1;
            }

            int row = selectedIndex;
            tableModel.setValueAt(student.name, row, 0);
            tableModel.setValueAt(gradeValue, row, colIndex);

            // atualizar média
            tableModel.setValueAt(student.average(), row, 1);

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

    // Gerenciar disciplinas
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

                    // Remove coluna da tabela
                    int colToRemove = -1;
                    for (int i = 2; i < tableModel.getColumnCount(); i++) {
                        if (tableModel.getColumnName(i).equals(removedSubject)) {
                            colToRemove = i;
                            break;
                        }
                    }
                    if (colToRemove != -1) {
                        // remover a coluna do JTable
                        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(colToRemove));
                    }

                    // Remove a nota da disciplina de cada aluno e atualiza média
                    for (int row = 0; row < classroom.students.size(); row++) {
                        Student student = classroom.students.get(row);
                        student.removeGradle(removedSubject);
                        tableModel.setValueAt(student.average(), row, 1); // atualizar média
                    }
                }
            }
        });

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    });

    frame.setVisible(true);
}

// método auxiliar para remover coluna do JTable
private static void removeColumn(JTable table, int colIndex) {
    table.getColumnModel().removeColumn(table.getColumnModel().getColumn(colIndex));
}
