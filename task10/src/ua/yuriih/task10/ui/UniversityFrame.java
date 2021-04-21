package ua.yuriih.task10.ui;

import org.xml.sax.SAXException;
import ua.yuriih.task10.controller.UniversityDao;
import ua.yuriih.task10.model.Group;
import ua.yuriih.task10.model.Student;

import javax.swing.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class UniversityFrame extends JFrame {
    //lazy man's UI
    private static final int WIDTH = 600;
    private static final int HEIGHT = 500;
    private static final int BOTTOM_BAR_HEIGHT = 50;
    private static final int STUDENT_FIELD_HEIGHT = 25;
    private static final int MARGIN = 5;

    private static class StudentItem {
        public final Student student;

        public StudentItem(Student student) {
            this.student = student;
        }

        @Override
        public String toString() {
            return student.getName();
        }
    }

    private static class GroupItem {
        public final Group group;

        public GroupItem(Group group) {
            this.group = group;
        }

        @Override
        public String toString() {
            return group.getName();
        }
    }

    private final UniversityDao dao;

    private final DefaultListModel<StudentItem> studentsListModel = new DefaultListModel<>();
    private final DefaultListModel<GroupItem> groupsListModel = new DefaultListModel<>();
    private final DefaultComboBoxModel<GroupItem> studentGroupBoxModel = new DefaultComboBoxModel<>();

    private final JList<StudentItem> studentsList;
    private final JList<GroupItem> groupsList;
    private final JTextField studentNameField;
    private final JTextField studentBirthDateField;
    private final JCheckBox studentHasScholarhsipBox;
    private final JSpinner studentAverageScoreSpinner;
    private final JComboBox<GroupItem> studentGroupBox;

    private final String fileName;

    public UniversityFrame(UniversityDao dao, String fileName) {
        super("University");

        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setLayout(null);

        this.dao = dao;
        this.fileName = fileName;


        groupsList = new JList<>(groupsListModel);
        groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupsList.setBounds(0, 0, WIDTH / 3 - MARGIN, HEIGHT - BOTTOM_BAR_HEIGHT);
        groupsList.addListSelectionListener(listSelectionEvent -> {
            updateStudentsList();
        });
        add(groupsList);


        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(0, HEIGHT - BOTTOM_BAR_HEIGHT, WIDTH / 6, BOTTOM_BAR_HEIGHT / 2);
        deleteButton.addActionListener(actionEvent -> {
            GroupItem selected = groupsList.getSelectedValue();
            if (selected == null)
                return;

            dao.deleteGroup(selected.group.getId());
            updateGroupsList();
        });
        add(deleteButton);


        JButton addButton = new JButton("Add");
        addButton.setBounds(WIDTH / 6, HEIGHT - BOTTOM_BAR_HEIGHT, WIDTH / 6, BOTTOM_BAR_HEIGHT / 2);
        addButton.addActionListener(actionEvent -> {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter name for the new group:"
            );
            if (input == null || input.length() == 0)
                return;

            synchronized (dao) {
                int id = dao.getFreeGroupId();
                dao.addGroup(new Group(id, input));
            }
            updateGroupsList();
        });
        add(addButton);


        studentNameField = new JTextField(100);
        studentNameField.setBounds(WIDTH / 3 * 2, 0, WIDTH / 3, STUDENT_FIELD_HEIGHT);
        add(studentNameField);


        studentBirthDateField = new JTextField(100);
        studentBirthDateField.setBounds(WIDTH / 3 * 2, STUDENT_FIELD_HEIGHT, WIDTH / 3, STUDENT_FIELD_HEIGHT);
        add(studentBirthDateField);


        studentHasScholarhsipBox = new JCheckBox();
        studentHasScholarhsipBox.setText("Has scholarship");
        studentHasScholarhsipBox.setBounds(WIDTH / 3 * 2, STUDENT_FIELD_HEIGHT * 3, WIDTH / 3, STUDENT_FIELD_HEIGHT);
        add(studentHasScholarhsipBox);


        studentAverageScoreSpinner = new JSpinner(new SpinnerNumberModel(100.0, 0.0, 100.0, 0.1));
        studentAverageScoreSpinner.setBounds(WIDTH / 3 * 2, STUDENT_FIELD_HEIGHT * 4, WIDTH / 4, STUDENT_FIELD_HEIGHT);
        add(studentAverageScoreSpinner);


        studentGroupBox = new JComboBox<>(studentGroupBoxModel);
        studentGroupBox.setBounds(WIDTH / 3 * 2, STUDENT_FIELD_HEIGHT * 5, WIDTH / 4, STUDENT_FIELD_HEIGHT);
        add(studentGroupBox);


        studentsList = new JList<>(studentsListModel);
        studentsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentsList.setBounds(WIDTH / 3, 0, WIDTH / 3 - MARGIN, HEIGHT - BOTTOM_BAR_HEIGHT);
        studentsList.addListSelectionListener(listSelectionEvent -> {
            updateStudentData();
        });
        add(studentsList);


        JButton studentUpdateButton = new JButton("Update");
        studentUpdateButton.setBounds(WIDTH / 3 * 2, STUDENT_FIELD_HEIGHT * 6, WIDTH / 3, STUDENT_FIELD_HEIGHT);
        studentUpdateButton.addActionListener(actionEvent -> {
            StudentItem selected = studentsList.getSelectedValue();
            if (selected == null)
                return;
            Student newStudent = new Student(
                    selected.student.getId(),
                    ((GroupItem)studentGroupBox.getSelectedItem()).group.getId(),
                    studentNameField.getText(),
                    LocalDate.parse(studentBirthDateField.getText()),
                    studentHasScholarhsipBox.isSelected()
            );
            newStudent.setAverageScore(((Number)studentAverageScoreSpinner.getValue()).floatValue());

            dao.updateStudent(newStudent);
            updateStudentsList();
        });
        add(studentUpdateButton);


        deleteButton = new JButton("Delete");
        deleteButton.setBounds(WIDTH / 3, HEIGHT - BOTTOM_BAR_HEIGHT, WIDTH / 6, BOTTOM_BAR_HEIGHT / 2);
        deleteButton.addActionListener(actionEvent -> {
            StudentItem selected = studentsList.getSelectedValue();
            if (selected == null)
                return;

            dao.deleteStudent(selected.student.getId());
            updateStudentsList();
        });
        add(deleteButton);


        addButton = new JButton("Add");
        addButton.setBounds(WIDTH / 2, HEIGHT - BOTTOM_BAR_HEIGHT, WIDTH / 6, BOTTOM_BAR_HEIGHT / 2);
        addButton.addActionListener(actionEvent -> {
            String name = JOptionPane.showInputDialog(
                    this,
                    "Enter name for the new student:",
                    null,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (name == null || name.length() == 0)
                return;

            LocalDate birthDate = LocalDate.parse(JOptionPane.showInputDialog(
                    this,
                    "Enter date of birth:",
                    null,
                    JOptionPane.QUESTION_MESSAGE
            ));

            boolean hasScholarship = Boolean.parseBoolean((String)JOptionPane.showInputDialog(
                    this,
                    "Does this student have a scholarship?",
                    null,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] { "true", "false" },
                    null
            ));

            synchronized (dao) {
                int id = dao.getFreeStudentId();
                dao.addStudent(new Student(id, groupsList.getSelectedValue().group.getId(),
                        name, birthDate, hasScholarship));
            }
            updateStudentsList();
        });
        add(addButton);


        JButton loadButton = new JButton("Load");
        loadButton.setBounds(WIDTH / 4, HEIGHT - BOTTOM_BAR_HEIGHT / 2, WIDTH / 4, BOTTOM_BAR_HEIGHT / 2);
        loadButton.addActionListener(actionEvent -> {
            try {
                dao.readFromFile("test.xml");
                updateGroupsList();
                updateStudentsList();
            } catch (IOException | SAXException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        add(loadButton);


        JButton saveButton = new JButton("Save");
        saveButton.setBounds(WIDTH / 2, HEIGHT - BOTTOM_BAR_HEIGHT / 2, WIDTH / 4, BOTTOM_BAR_HEIGHT / 2);
        saveButton.addActionListener(actionEvent -> {
            try {
                dao.saveToFile("test.xml");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                        this,
                        e.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
        add(saveButton);


        updateGroupsList();
        updateStudentsList();
        updateStudentData();
    }

    private void updateGroupsList() {
        groupsListModel.clear();
        studentGroupBoxModel.removeAllElements();

        for (Group group : dao.getAllGroups()) {
            GroupItem item = new GroupItem(group);
            groupsListModel.addElement(item);
            studentGroupBoxModel.addElement(item);
        }
    }

    private void updateStudentsList() {
        studentsListModel.clear();
        GroupItem selected = groupsList.getSelectedValue();
        if (selected == null)
            return;

        for (Student student : dao.getAllStudentsFromGroup(selected.group.getId()))
            studentsListModel.addElement(new StudentItem(student));
    }

    private void updateStudentData() {
        StudentItem selected = studentsList.getSelectedValue();
        if (selected == null) {
            studentNameField.setText("");
            studentNameField.setEnabled(false);

            studentBirthDateField.setText("");
            studentBirthDateField.setEnabled(false);

            studentHasScholarhsipBox.setSelected(false);
            studentHasScholarhsipBox.setEnabled(false);

            studentAverageScoreSpinner.setValue(100f);
            studentAverageScoreSpinner.setEnabled(false);

            studentGroupBox.setEnabled(false);
        } else {
            studentNameField.setText(selected.student.getName());
            studentNameField.setEnabled(true);

            studentBirthDateField.setText(selected.student.getBirthDate().toString());
            studentBirthDateField.setEnabled(true);

            studentHasScholarhsipBox.setSelected(selected.student.hasScholarship());
            studentHasScholarhsipBox.setEnabled(true);

            studentAverageScoreSpinner.setValue(selected.student.getAverageScore());
            studentAverageScoreSpinner.setEnabled(true);

            int groupId = selected.student.getGroupId();
            int groupItemIndex = -1;
            for (int i = 0; i < studentGroupBox.getItemCount(); i++) {
                if (studentGroupBox.getItemAt(i).group.getId() == groupId) {
                    groupItemIndex = i;
                    break;
                }
            }
            studentGroupBox.setSelectedIndex(groupItemIndex);
            studentGroupBox.setEnabled(true);
        }
    }
}
