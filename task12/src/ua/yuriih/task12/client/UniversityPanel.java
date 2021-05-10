package ua.yuriih.task12.client;

import ua.yuriih.task12.client.operations.*;
import ua.yuriih.task12.common.Group;
import ua.yuriih.task12.common.Student;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;

public class UniversityPanel extends JPanel {
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

    private final ObjectInputStream in;
    private final ObjectOutputStream out;

    private final DefaultListModel<StudentItem> studentsListModel = new DefaultListModel<>();
    private final DefaultListModel<GroupItem> groupsListModel = new DefaultListModel<>();
    private final DefaultComboBoxModel<GroupItem> studentGroupBoxModel = new DefaultComboBoxModel<>();

    private final JButton studentAddButton;
    private final JButton studentDeleteButton;
    private final JList<StudentItem> studentsList;
    private final JList<GroupItem> groupsList;
    private final JTextField studentNameField;
    private final JTextField studentBirthDateField;
    private final JCheckBox studentHasScholarhsipBox;
    private final JSpinner studentAverageScoreSpinner;
    private final JComboBox<GroupItem> studentGroupBox;

    public UniversityPanel(ObjectInputStream in, ObjectOutputStream out) {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(null);

        this.in = in;
        this.out = out;


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

            DeleteGroup op = new DeleteGroup();
            try {
                op.performQuery(in, out, selected.group.getId());
            } catch (IOException e) {
                showErrorMessage(e);
            }
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

            try {
                AddGroup op = new AddGroup();
                op.performQuery(in, out, new Group(0, input));
            } catch (IOException e) {
                showErrorMessage(e);
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

            try {
                UpdateStudent op = new UpdateStudent();
                op.performQuery(in, out, newStudent);
            } catch (IOException e) {
                showErrorMessage(e);
            }
            updateStudentsList();
        });
        add(studentUpdateButton);


        studentDeleteButton = new JButton("Delete");
        studentDeleteButton.setBounds(WIDTH / 3, HEIGHT - BOTTOM_BAR_HEIGHT, WIDTH / 6, BOTTOM_BAR_HEIGHT / 2);
        studentDeleteButton.addActionListener(actionEvent -> {
            StudentItem selected = studentsList.getSelectedValue();
            if (selected == null)
                return;

            try {
                DeleteStudent op = new DeleteStudent();
                op.performQuery(in, out, selected.student.getId());
            } catch (IOException e) {
                showErrorMessage(e);
            }
            updateStudentsList();
        });
        add(studentDeleteButton);


        studentAddButton = new JButton("Add");
        studentAddButton.setBounds(WIDTH / 2, HEIGHT - BOTTOM_BAR_HEIGHT, WIDTH / 6, BOTTOM_BAR_HEIGHT / 2);
        studentAddButton.addActionListener(actionEvent -> {
            String input = JOptionPane.showInputDialog(
                    this,
                    "Enter name for the new student:",
                    null,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (input == null || input.length() == 0)
                return;
            String name = input;

            input = JOptionPane.showInputDialog(
                    this,
                    "Enter date of birth:",
                    null,
                    JOptionPane.QUESTION_MESSAGE
            );
            if (input == null || input.length() == 0)
                return;
            LocalDate birthDate = LocalDate.parse(input);

            input = (String)JOptionPane.showInputDialog(
                    this,
                    "Does this student have a scholarship?",
                    null,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[] { "true", "false" },
                    null
            );
            if (input == null || input.length() == 0)
                return;
            boolean hasScholarship = Boolean.parseBoolean(input);

            try {
                AddStudent op = new AddStudent();
                op.performQuery(in, out, new Student(
                        0,
                        groupsList.getSelectedValue().group.getId(),
                        name,
                        birthDate,
                        hasScholarship
                ));
            } catch (IOException e) {
                showErrorMessage(e);
            }
            updateStudentsList();
        });
        add(studentAddButton);

        updateGroupsList();
        updateStudentsList();
        updateStudentData();
    }

    private void updateGroupsList() {
        groupsListModel.clear();
        studentGroupBoxModel.removeAllElements();

        try {
            GetAllGroups op = new GetAllGroups();
            ArrayList<Group> groups = op.performQuery(in, out);
            for (Group group : groups) {
                GroupItem item = new GroupItem(group);
                groupsListModel.addElement(item);
                studentGroupBoxModel.addElement(item);
            }
        } catch (IOException e) {
            showErrorMessage(e);
        }
    }

    private void updateStudentsList() {
        studentsListModel.clear();
        GroupItem selected = groupsList.getSelectedValue();
        if (selected == null) {
            studentAddButton.setEnabled(false);
            studentDeleteButton.setEnabled(false);
        } else {
            try {
                GetStudentsFromGroup op = new GetStudentsFromGroup();
                ArrayList<Student> students = op.performQuery(in, out, selected.group.getId());
                for (Student student : students)
                    studentsListModel.addElement(new StudentItem(student));

                studentAddButton.setEnabled(true);
                studentDeleteButton.setEnabled(true);

            } catch (IOException e) {
                showErrorMessage(e);
            }
        }
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

    private void showErrorMessage(Exception e) {
        JOptionPane.showMessageDialog(
                this,
                e.getLocalizedMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
