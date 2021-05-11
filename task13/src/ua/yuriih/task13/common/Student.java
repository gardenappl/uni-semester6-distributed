package ua.yuriih.task13.common;

import java.io.Serializable;
import java.time.LocalDate;

public class Student implements Serializable {
    private int id;

    private int groupId;

    private final String name;
    private final LocalDate birthDate;
    private float averageScore;
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

    public float getAverageScore() {
        return averageScore;
    }

    public void setAverageScore(float averageScore) {
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

    public void setId(int id) {
        this.id = id;
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
