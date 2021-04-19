package ua.yuriih.task10.model;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class Student implements Cloneable {
    private final int id;

    private int groupId;

    private final String name;
    private final LocalDate birthDate;
    private int averageScore;
    private boolean hasScholarship;

    public Student(int id, int groupId, String name, LocalDate birthDate, boolean hasScholarship) {
        this.id = id;
        this.groupId = groupId;
        this.name = name;
        this.birthDate = birthDate;
        this.hasScholarship = hasScholarship;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(int averageScore) {
        this.averageScore = averageScore;
    }

    public boolean hasScholarship() {
        return hasScholarship;
    }

    public void setHasScholarship(boolean hasScholarship) {
        this.hasScholarship = hasScholarship;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    @Override
    public Student clone() {
        try {
            return (Student) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", groupId=" + groupId +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", averageScore=" + averageScore +
                ", hasScholarship=" + hasScholarship +
                '}';
    }
}
